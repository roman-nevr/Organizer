package ru.rubicon21.organizer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import ru.rubicon21.organizer.DAO.DataManager;
import ru.rubicon21.organizer.adapter.MainWindowAdapter;
import ru.rubicon21.organizer.entity.Task;

import java.util.ArrayList;

/**
 * Created by roma on 22.03.2015.
 */

/*
   что активити делает:
   1.принимает номер задачи, которая является родительской
   2.отображает все задачи, у которых этот номер значится как родительский
   3.передает номер задачи, потомков которой надо показать
   4.внизу отображается кнопка добавить
   5.при долгом нажатии показывается всплывающее меню с пунктами изменить и завершить
   6.завершенные перемещаются вниз списка
 */

public class TaskDetails extends Activity {
    final String LOG_TAG = "myLogs";
    ListView lvMain;

    MainWindowAdapter mainWindowAdapter;
    ArrayList<Task> tasks;

    int parentID;

    final int CM_EDIT = 1;
    final int CM_DELETE = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        DataManager dm = new DataManager();

        parentID = getIntent().getIntExtra("parent_id", 0);

        lvMain = (ListView) findViewById(R.id.lvMain);

        registerForContextMenu(lvMain);

        tasks = new ArrayList<Task>();

        tasks = dm.getTasks(this, parentID);
        mainWindowAdapter = new MainWindowAdapter(this,tasks);

        Log.d(LOG_TAG,"income parentID : "+parentID+" ");

        Button buttonAddTask = (Button) findViewById(R.id.buttonAddTask);
        View.OnClickListener onClickListenerButtonAddTask = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TaskDetails.this, AddTask.class);
                intent.putExtra("parent_id",parentID);
                Log.d(LOG_TAG, "out parent_id : " + parentID + " ");
                startActivity(intent);
            }
        };
        buttonAddTask.setOnClickListener(onClickListenerButtonAddTask);

        lvMain.setAdapter(mainWindowAdapter);

        //обработка нажатия на элемент списка
        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Log.d(LOG_TAG, "itemClick: position = " + position + ", id = " + id);

                try {
                    Intent intent = new Intent(TaskDetails.this, TaskDetails.class);
                    intent.putExtra("parent_id", (tasks.get(position)).getTaskId());
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("NPE");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        DataManager dm = new DataManager();
        //parentID = getIntent().getIntExtra("parent_id", 0);
        tasks = dm.getTasks(TaskDetails.this, TaskDetails.this.parentID);
        if (tasks.isEmpty()){
            Toast.makeText(TaskDetails.this, R.string.nothingToShowMessage, Toast.LENGTH_LONG).show();
        }
        this.mainWindowAdapter.refreshDataSet(tasks);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) {
        //super.onCreateContextMenu(menu, view, menuInfo);
        if (view.getId() == R.id.lvMain){
            /*
            * AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
            * String title = ((MyItem) mAdapter.getItem(info.position)).getTitle();
            */
            AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
            String title = ((Task) mainWindowAdapter.getItem(info.position)).getTaskName();
            menu.setHeaderTitle(title);
            menu.add(0,CM_EDIT,0,R.string.cmTaskEdit);
            menu.add(0,CM_DELETE,0,R.string.cmTaskDelete);

        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        DataManager dm = new DataManager();
        //return super.onContextItemSelected(item);
        //AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        int position = ((AdapterContextMenuInfo) item.getMenuInfo()).position;
        Task task = this.tasks.get(position);
        switch (item.getItemId()){
            case CM_EDIT:
                Intent outIntent = new Intent(TaskDetails.this, EditTask.class);
                outIntent.putExtra("task_id", task.getTaskId());
                startActivity(outIntent);

                return true;
               // break;
            case CM_DELETE:
                int deleteResult = dm.deleteTask(TaskDetails.this, task);
                if (deleteResult >= 1){
                    Toast.makeText(TaskDetails.this,getResources().getString(R.string.deleteMessage)+" "+task.getTaskName(),
                            Toast.LENGTH_LONG).show();
                    TaskDetails.this.onResume();
                }else {
                    Toast.makeText(TaskDetails.this,getResources().getString(R.string.deleteNothingMessage)+" "+task.getTaskName(),
                            Toast.LENGTH_LONG).show();
                }

                return true;
               // break;
            default:
                return super.onContextItemSelected(item);
        }

    }
}
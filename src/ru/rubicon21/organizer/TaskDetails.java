package ru.rubicon21.organizer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import ru.rubicon21.organizer.DAO.GetData;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        lvMain = (ListView) findViewById(R.id.lvMain);
        tasks = new ArrayList<Task>();
        tasks = (new GetData()).getTasks();
        mainWindowAdapter = new MainWindowAdapter(this,tasks);

        Intent incomeIntent = getIntent();
        parentID = incomeIntent.getIntExtra("parent_id",0);

        Button buttonAddTask = (Button) findViewById(R.id.buttonAddTask);
        View.OnClickListener onClickListenerButtonAddTask = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TaskDetails.this, AddTask.class);
                intent.putExtra("parent_id",tasks.get(0).getParentId());
                Log.d(LOG_TAG, "parent_id : " + tasks.get(0).getParentId() + " ");
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
                    intent.putExtra("parent_id",position);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("NPE");
                }
            }
        });
    }
}
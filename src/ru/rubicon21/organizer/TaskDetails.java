package ru.rubicon21.organizer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        lvMain = (ListView) findViewById(R.id.lvMain);
        tasks = new ArrayList<Task>();
        tasks = (new GetData()).getTasks();
        mainWindowAdapter = new MainWindowAdapter(this,tasks);

        lvMain.setAdapter(mainWindowAdapter);

        //обработка нажатия на элемент списка
        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Log.d(LOG_TAG, "itemClick: position = " + position + ", id = " + id);

                try {
                    Intent intent = new Intent(TaskDetails.this, TaskDetails.class);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("NPE");
                }
            }
        });
    }
}
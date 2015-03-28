package ru.rubicon21.organizer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import ru.rubicon21.organizer.adapter.MainWindowAdapter;
import ru.rubicon21.organizer.entity.Task;

import java.util.ArrayList;

public class MainActivity extends Activity {

    final String LOG_TAG = "myLogs";
    ListView lvMain;

    MainWindowAdapter mainWindowAdapter;
    ArrayList<Task> tasks;

    /** Called when the activity is first created. */
    /*public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        lvMain = (ListView) findViewById(R.id.lvMain);
        tasks = new ArrayList<Task>();
        tasks = (new GetData()).getTestTasks();
        mainWindowAdapter = new MainWindowAdapter(this,tasks);

        lvMain.setAdapter(mainWindowAdapter);

        //обработка нажатия на элемент списка
        lvMain.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Log.d(LOG_TAG, "itemClick: position = " + position + ", id = " + id);

                try {
                    Intent intent = new Intent(MainActivity.this, TaskDetails.class);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("NPE");
                }
            }
        });
    }*/
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, TaskDetails.class);
        intent.putExtra("parent_id",0);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MainActivity.this.finish();
    }
}

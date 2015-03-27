package ru.rubicon21.organizer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;
import android.widget.Toast;
import ru.rubicon21.organizer.DAO.DataManager;
import ru.rubicon21.organizer.entity.Task;

import java.sql.SQLException;

/**
 * Created by roma on 25.03.2015.
 */
public class AddTask extends Activity {
    final String LOG_TAG = "myLogs";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_task);

        final EditText etTaskAddName = (EditText) findViewById(R.id.etTaskAddName);
        final EditText etTaskAddDescription = (EditText) findViewById(R.id.etTaskAddDescription);

        Button buttonSaveTask = (Button) findViewById(R.id.buttonSaveTask);
        Button buttonCancelSaveTask = (Button) findViewById(R.id.buttonCancelSaveTask);

        Intent intent = getIntent();
        final int parentId = intent.getIntExtra("parent_id",0);

        OnClickListener onClickButtonSaveTask = new OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                Log.d(LOG_TAG, "et "+etTaskAddName.getText().toString());
                if (!(TextUtils.isEmpty(etTaskAddName.getText().toString()))) {
                    Task task = new Task(etTaskAddName.getText().toString(), etTaskAddDescription.getText().toString());
                    task.setParentId(parentId);

                    try {
                        (new DataManager()).saveTask(AddTask.this, task);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        Log.d(LOG_TAG, task.toString());
                        AddTask.this.finish();
                    }
                } else {
                    Log.d(LOG_TAG, "toast");
                    Toast.makeText(AddTask.this,"Введите хотя бы имя", Toast.LENGTH_SHORT).show();
                }
            }
        };

        OnClickListener onClickButtonCancelSaveTask = new OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                Log.d(LOG_TAG, "finish");
                finish();
                Log.d(LOG_TAG, "finish");
                AddTask.this.finish();

            }
        };
        buttonSaveTask.setOnClickListener(onClickButtonSaveTask);
        buttonCancelSaveTask.setOnClickListener(onClickButtonCancelSaveTask);
    }
}
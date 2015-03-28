package ru.rubicon21.organizer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import ru.rubicon21.organizer.DAO.DataManager;
import ru.rubicon21.organizer.entity.Task;

/**
 * Created by roma on 28.03.2015.
 */
public class EditTask extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_task);
        Intent intent = getIntent();
        final int taskId = intent.getIntExtra("task_id",0);

        if (taskId == 0){
            Toast.makeText(this, R.string.editTaskErrorMessage, Toast.LENGTH_LONG).show();
            EditTask.this.finish();
        }

        final DataManager dm = new DataManager();
        final Task task = dm.getTask(this,taskId);

        TextView tvEditTaskName = (TextView) findViewById(R.id.tvTaskName);
        tvEditTaskName.setText(getResources().getString(R.string.tvEditTaskName));

        final EditText etTaskEditName = (EditText) findViewById(R.id.etTaskAddName);
        etTaskEditName.setText(task.getTaskName());

        TextView tvEditTaskDescription = (TextView) findViewById(R.id.tvTaskDescription);
        tvEditTaskDescription.setText(getResources().getString(R.string.tvEditTaskDescription));

        final EditText etTaskEditDescription = (EditText) findViewById(R.id.etTaskAddDescription);
        etTaskEditDescription.setText(task.getTaskDescription());

        Button buttonSaveTask = (Button) findViewById(R.id.buttonSaveTask);
        buttonSaveTask.setText(getResources().getString(R.string.buttonSaveEditTask));

        Button buttonCancelSaveTask = (Button) findViewById(R.id.buttonCancelSaveTask);

        OnClickListener onClickButtonSaveTask = new OnClickListener() {
            @Override
            public void onClick(View view) {
                task.setTaskName(etTaskEditName.getText().toString());
                task.setTaskDescription(etTaskEditDescription.getText().toString());
                dm.updateTask(EditTask.this, task);
                Toast.makeText(EditTask.this, R.string.savedEditMessage, Toast.LENGTH_LONG).show();
                EditTask.this.finish();
            }
        };

        OnClickListener onClickButtonCancelSaveTask = new OnClickListener() {
            @Override
            public void onClick(View view) {
                EditTask.this.finish();
            }
        };

        buttonSaveTask.setOnClickListener(onClickButtonSaveTask);
        buttonCancelSaveTask.setOnClickListener(onClickButtonCancelSaveTask);
    }
}
package ru.rubicon21.organizer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
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

    final String LOG_TAG = "myLogs";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_task);

        final InputMethodManager imm = (InputMethodManager) getSystemService(EditTask.this.INPUT_METHOD_SERVICE);

        Intent intent = getIntent();
        final int taskId = intent.getIntExtra("task_id",0);

        if (taskId == 0){
            Toast.makeText(this, R.string.ErrorMessage, Toast.LENGTH_LONG).show();
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

        final EditText etTaskPriority = (EditText) findViewById(R.id.etTaskPriority);
        etTaskPriority.setText(""+task.getPriority());

        Button buttonCancelSaveTask = (Button) findViewById(R.id.buttonCancelSaveTask);

        OnClickListener onClickButtonSaveTask = new OnClickListener() {
            @Override
            public void onClick(View view) {
                /*task.setTaskName(etTaskEditName.getText().toString());
                task.setTaskDescription(etTaskEditDescription.getText().toString());
                task.set
                dm.updateTaskById(EditTask.this, task);
                Toast.makeText(EditTask.this, R.string.savedEditMessage, Toast.LENGTH_LONG).show();
                EditTask.this.finish();*/
                imm.hideSoftInputFromWindow(etTaskEditName.getWindowToken(), 0);
                int priority = 0;
                try {
                    priority = Integer.parseInt(etTaskPriority.getText().toString());
                }catch (Exception e){
                    Toast.makeText(EditTask.this,"Неверное значение приоритета", Toast.LENGTH_SHORT).show();
                    return;
                }
                boolean priorityIsNormal = ((priority >= -10000)  && (priority <= 10000));
                boolean taskNameIsNormal = !(TextUtils.isEmpty(etTaskEditName.getText().toString()));
                Log.d(LOG_TAG, "et " + etTaskEditName.getText().toString());
                if (taskNameIsNormal) {
                    if (priorityIsNormal) {
                        //Task task = new Task(etTaskEditName.getText().toString(), etTaskEditDescription.getText().toString());

                        task.setPriority(priority);
                        task.setTaskName(etTaskEditName.getText().toString());
                        task.setTaskDescription(etTaskEditDescription.getText().toString());

                        int result = dm.updateTaskById(EditTask.this, task);
                        if (result == -1){
                            Toast.makeText(EditTask.this, "write fail", Toast.LENGTH_LONG).show();
                        }
                        Log.d(LOG_TAG, task.toString());
                        EditTask.this.finish();
                    } else {
                        //priority not normal
                        Toast.makeText(EditTask.this,"Введите разумное значение приоритета", Toast.LENGTH_SHORT).show();
                    }//end priority if
                } else {
                    //name not normal
                    Log.d(LOG_TAG, "toast");
                    Toast.makeText(EditTask.this,"Введите имя", Toast.LENGTH_SHORT).show();
                }
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
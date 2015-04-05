package ru.rubicon21.organizer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.*;
import ru.rubicon21.organizer.DAO.DataManager;
import ru.rubicon21.organizer.adapter.MainWindowAdapter;
import ru.rubicon21.organizer.entity.Task;

import java.util.ArrayList;

/**
 * Created by roma on 04.04.2015.
 */
public class TaskReplace extends Activity {

    ListView lvTaskReplace;
    Button buttonTaskReplaceCancel;
    EditText etSearch;



    Task task;
    Task taskReplaceTo;
    final String LOG_TAG = "myLogs";

    ArrayList<Task> tasks;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.replace_task);

        int taskId = getIntent().getIntExtra("task_id", 0);

        if (taskId == 0){
            Toast.makeText(this, R.string.ErrorMessage, Toast.LENGTH_LONG).show();
            TaskReplace.this.finish();
        }

        DataManager dm = new DataManager();
        task = dm.getTask(this,taskId);

        TextView tvTitleTaskReplace = (TextView) findViewById(R.id.tvTaskReplace);
        tvTitleTaskReplace.setText(task.getTaskName());
        lvTaskReplace = (ListView) findViewById(R.id.lvTaskReplace);
        buttonTaskReplaceCancel = (Button) findViewById(R.id.buttonTaskReplace);
        etSearch = (EditText) findViewById(R.id.etSearch);

        View.OnClickListener buttonCancel  = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TaskReplace.this.finish();
            }
        };
        buttonTaskReplaceCancel.setOnClickListener(buttonCancel);

        tasks = dm.findTaskByString(this, task, etSearch.getText().toString());
        tasks = removeBranchFromArrayList(tasks,task);


        if (task.getParentId() != 0){
            tasks.add(0, new Task(0, getResources().getString(R.string.taskRootNameCap), ""));
        }


        final MainWindowAdapter mainWindowAdapter = new MainWindowAdapter(this,tasks);
        lvTaskReplace.setAdapter(mainWindowAdapter);

        lvTaskReplace.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                taskReplaceTo = tasks.get(position);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TaskReplace.this);
                alertDialogBuilder.setTitle(R.string.dialogReplaceTitle);
                if (taskReplaceTo.getTaskId() == 0){
                    alertDialogBuilder.setMessage(R.string.taskRootNameMessage);
                }else {
                    alertDialogBuilder.setMessage(TaskReplace.this.getResources().getString(R.string.dialogReplaceMessage) +
                            " \"" + taskReplaceTo.getTaskName() + "\"?");
                }
                alertDialogBuilder.setPositiveButton(R.string.dialogOk, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        task.setParentId(taskReplaceTo.getTaskId());
                        Log.d(LOG_TAG, "Replaced task "+task.toString());
                        DataManager dm = new DataManager();
                        int result = dm.updateTaskById(TaskReplace.this, task);
                        if (result >= 1){
                            Log.d(LOG_TAG, "successes");
                            Toast.makeText(TaskReplace.this,getResources().getString(R.string.toastReplaceMessage)+
                                            " "+taskReplaceTo.getTaskName()+"",
                                    Toast.LENGTH_SHORT).show();
                        }
                        TaskReplace.this.finish();
                    }
                });
                alertDialogBuilder.setNegativeButton(R.string.dialogCancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //nothing doing
                    }
                });
                alertDialogBuilder.show();
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Toast.makeText(TaskRepalce.this, "onChanged :"+charSequence, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //Toast.makeText(TaskRepalce.this, "afterChanged :", Toast.LENGTH_SHORT).show();
                DataManager dm = new DataManager();
                tasks = dm.findTaskByString(TaskReplace.this, task, etSearch.getText().toString());
                tasks = removeBranchFromArrayList(tasks,task);
                mainWindowAdapter.refreshDataSet(tasks);
            }
        });

    }

    private ArrayList<Task> removeBranchFromArrayList(ArrayList<Task> tasks, Task parentTask){
        ArrayList<Task> resultTasks = new ArrayList<Task>();
        for (Task task:tasks){
            if (!isTaskInBranch(parentTask, task)){
                resultTasks.add(task);
            }
        }
        return resultTasks;
    }

     private boolean isTaskInBranch(Task parentTask, Task checkedTask){
         DataManager dm = new DataManager();
         boolean result = false;
        if ((parentTask.getTaskId() != checkedTask.getTaskId())){
            while ((checkedTask != null) && (checkedTask.getParentId() != 0)) {
                if (checkedTask.getParentId() == parentTask.getTaskId()){
                    result = true;
                }
                checkedTask = dm.getTask(TaskReplace.this, checkedTask.getParentId());
            }
        }else {
            result = true;
        }
        return result;
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.getCurrentFocus().clearFocus();
    }
}
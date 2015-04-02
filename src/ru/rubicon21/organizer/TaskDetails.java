package ru.rubicon21.organizer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.*;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnTouchListener;
import android.widget.*;
import android.widget.AdapterView.AdapterContextMenuInfo;
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
    LinearLayout llMain;

    float xDown, yDown;

    MainWindowAdapter mainWindowAdapter;
    ArrayList<Task> tasks;

    int parentID;

    final int CM_EDIT = 1;
    final int CM_DONE = 2;
    final int CM_DELETE = 3;

    final int DIALOG_DELETE = 1;

    final float minSwipe = (float) 0.2;

    final long delay = 300;
    boolean timeOut;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //int res = R.id.lvMain;
        LayoutInflater ltInflater = getLayoutInflater();


        llMain = (LinearLayout) findViewById(R.id.llMain);
        if (llMain == null){

            llMain = (LinearLayout) ltInflater.inflate(R.layout.main, null, false);
            Log.d(LOG_TAG, "fail");
            Log.d(LOG_TAG, llMain.toString());
        }
        lvMain = (ListView) findViewById(R.id.lvMain);
        if (lvMain == null){
            //lvMain = (ListView) ltInflater.inflate(R.id.lvMain, llMain, true );
            Log.d(LOG_TAG, "fail again");
        }
        registerForContextMenu(lvMain);

        //final int llMainWidth = llMain.getWidth();

        Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        final int layoutWidth = display.getWidth();

        //lvMain.setOnTouchListener(this);
        lvMain.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.d(LOG_TAG, "motion event " + motionEvent.toString());
                WaitingTimer timer = new WaitingTimer(delay, delay);
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        xDown = motionEvent.getX();
                        yDown = motionEvent.getY();
                        timeOut = false;
                        timer.start();
                        Log.d(LOG_TAG, "timer start");
                        //http://hashcode.ru/questions/25779/java-%D1%82%D0%B0%D0%B9%D0%BC%D0%B5%D1%80-%D0%B2-android-%D0%BF%D1%80%D0%B8%D0%BB%D0%BE%D0%B6%D0%B5%D0%BD%D0%B8%D0%B8
                        return false;
                    //break;
                    case MotionEvent.ACTION_MOVE:
                        timer.cancel();
                        timeOut = false;
                        Log.d(LOG_TAG, "timer cancel");
                        return false;
                    //break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        if (!timeOut) {
                            float ratio = (motionEvent.getX() - xDown) / layoutWidth;
                            if (ratio >= minSwipe) {
                                TaskDetails.this.finish();
                                return true;
                            }
                        } else {
                            //openContextMenu(TaskDetails.this.lvMain);
                            return false;
                        }
                        break;
                }
                return false;
            }
        });

        tasks = new ArrayList<Task>();

        DataManager dm = new DataManager();
        parentID = getIntent().getIntExtra("parent_id", 0);
        tasks = dm.getTasks(this, parentID);

        mainWindowAdapter = new MainWindowAdapter(this,tasks);

        Log.d(LOG_TAG,"income parentID : "+parentID+" ");
        if (parentID != 0){
            TaskDetails.this.setTitle(dm.getTask(this, parentID).getTaskName());
        }

        Button buttonAddTask = (Button) findViewById(R.id.buttonAddTask);
        View.OnClickListener onClickListenerButtonAddTask = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TaskDetails.this, AddTask.class);
                intent.putExtra("parent_id",parentID);
                Log.d(LOG_TAG, "out parent_id : " + parentID + " ");
                startActivity(intent);
               // overridePendingTransition(R.anim.in_open, R.anim.in_close);
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
                   // overridePendingTransition(R.anim.in_open, R.anim.in_close);
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
        int i =TaskDetails.this.lvMain.getHeight();
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

            AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
            Task task = (Task) mainWindowAdapter.getItem(info.position);
            String title = task.getTaskName();
            menu.setHeaderTitle(title);
            menu.add(0, CM_EDIT, 0, R.string.cmTaskEdit);
            if (task.isDone()){
                menu.add(0,CM_DONE,0,R.string.cmTaskContinue);
            }else {
                menu.add(0,CM_DONE,0,R.string.cmTaskDone);
            }
            menu.add(0,CM_DELETE,0,R.string.cmTaskDelete);

        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final DataManager dm = new DataManager();
        //return super.onContextItemSelected(item);
        //AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        int position = ((AdapterContextMenuInfo) item.getMenuInfo()).position;
        final Task task = this.tasks.get(position);
        switch (item.getItemId()){
            case CM_EDIT:
                Intent outIntent = new Intent(TaskDetails.this, EditTask.class);
                outIntent.putExtra("task_id", task.getTaskId());
                startActivity(outIntent);
                //overridePendingTransition(R.anim.in_open,R.anim.in_close);

                return true;
               // break;
            case CM_DONE:
                task.doneToggle();
                dm.updateTask(TaskDetails.this, task);
                TaskDetails.this.onResume();
                return true;
            case CM_DELETE:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage(R.string.dialogDeleteDescription);
                alertDialogBuilder.setPositiveButton(R.string.dialogDeleteOk, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int deleteResult = dm.deleteTaskRecursive(TaskDetails.this, task);
                        if (deleteResult >= 1){
                            Toast.makeText(TaskDetails.this,getResources().getString(R.string.deleteMessage)+" "+task.getTaskName(),
                                    Toast.LENGTH_LONG).show();
                            TaskDetails.this.onResume();
                        }else {
                            Toast.makeText(TaskDetails.this,getResources().getString(R.string.deleteNothingMessage)+" "+task.getTaskName(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
                alertDialogBuilder.setNegativeButton(R.string.dialogDeleteCancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alertDialogBuilder.show();
                Log.d(LOG_TAG, "open dialog");
                return true;
               // break;
            default:
                return super.onContextItemSelected(item);
        }


    }

    class WaitingTimer extends CountDownTimer {

        public WaitingTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
        }

        @Override
        public void onFinish() {
            timeOut = true;
            Log.d(LOG_TAG, "timer finish");
        }
    }


    //конец TaskDetails
}
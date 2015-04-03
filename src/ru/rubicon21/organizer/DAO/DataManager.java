package ru.rubicon21.organizer.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import ru.rubicon21.organizer.entity.Task;

import java.util.ArrayList;

/**
 * Created by roma on 22.03.2015.
 */
public class DataManager {

    ArrayList<Task> tasks;

    DBHelper dbHelper;

    final int DB_VERSION = 3;

    final String DB_NAME = "taskDB";
    final String DB_TABLE_NAME = "tasks";
    final String DB_TASK_ID = "id";
    final String DB_PARENT_ID = "parent_id";
    final String DB_TASK_NAME = "name";
    final String DB_TASK_DESCRIPTION = "description";
    final String DB_TASK_DONE = "done";
    final String DB_TASK_PRIORITY = "priority";

    final String LOG_TAG = "myLogs";


    public ArrayList<Task> getTestTasks(){
        tasks = new ArrayList<Task>();
        tasks.add(new Task(1, 0, "Тест", "Пробная запись"));
        tasks.add(new Task(2, 0, "Тест 2", "Вторая Пробная запись"));
        tasks.add(new Task(3, 0, "Тест", "Пробная запись"));
        tasks.add(new Task(4, 1, "Тест", "Пробная запись"));
        tasks.add(new Task(5, 1, "Тест", "Пробная запись"));
        tasks.add(new Task(6, 2, "Тест", "Пробная запись"));
        tasks.add(new Task(7, 2, "Тест", "Пробная запись"));
        tasks.add(new Task(8, 3, "Тест", "Пробная запись"));
        tasks.add(new Task(9, 4, "Тест", "Пробная запись"));
        tasks.add(new Task(10, 5, "Тест", "Пробная запись"));
        return tasks;
    }

    public ArrayList<Task> getTasks(Context context, int _parent_id){
        tasks = new ArrayList<Task>();
        dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = DB_PARENT_ID+" LIKE ?";
        String[] selectionArgs = {String.valueOf(_parent_id)};
        String orderByPriority = DB_TASK_DONE+" ASC,"+ DB_TASK_PRIORITY+" DESC";
        //String orderByPriority =DB_TASK_PRIORITY+" DESC,"+ DB_TASK_DONE;
        String groupByDone = DB_TASK_DONE;
        Cursor cursor = db.query(DB_TABLE_NAME, null, selection, selectionArgs, null, null, orderByPriority);
        if (cursor.moveToFirst()){
            int idColumnIndex = cursor.getColumnIndex(DB_TASK_ID);
            int parentIdColumnIndex = cursor.getColumnIndex(DB_PARENT_ID);
            int nameColumnIndex = cursor.getColumnIndex(DB_TASK_NAME);
            int descriptionColumnIndex = cursor.getColumnIndex(DB_TASK_DESCRIPTION);
            int doneColumnIndex = cursor.getColumnIndex(DB_TASK_DONE);
            int priorityColumnIndex = cursor.getColumnIndex(DB_TASK_PRIORITY);
            do{
                Task task = new Task(
                        cursor.getInt(idColumnIndex),
                        cursor.getInt(parentIdColumnIndex),
                        cursor.getString(nameColumnIndex),
                        cursor.getString(descriptionColumnIndex));
                if (cursor.getInt(doneColumnIndex) == 1){
                    task.setDone(true);
                }else {
                    task.setDone(false);
                }
                task.setPriority(cursor.getInt(priorityColumnIndex));
                tasks.add(task);
            }while (cursor.moveToNext());
        }else {
            //сделать оповещение о пустой базе
        }
        cursor.close();
        db.close();
        return  tasks;
    }

    public Task getTask (Context context, int _id){
        dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = DB_TASK_ID +" LIKE ?";
        String[] selectionArgs = {String.valueOf(_id)};

        Cursor cursor = db.query(DB_TABLE_NAME, null, selection, selectionArgs, null, null, null);
        Task task;// = new Task("","");
        if (cursor.moveToFirst()){
            int idColumnIndex = cursor.getColumnIndex(DB_TASK_ID);
            int parentIdColumnIndex = cursor.getColumnIndex(DB_PARENT_ID);
            int nameColumnIndex = cursor.getColumnIndex(DB_TASK_NAME);
            int descriptionColumnIndex = cursor.getColumnIndex(DB_TASK_DESCRIPTION);
            int doneColumnIndex = cursor.getColumnIndex(DB_TASK_DONE);
            int priorityColumnIndex = cursor.getColumnIndex(DB_TASK_PRIORITY);
            task =  new Task(
                    cursor.getInt(idColumnIndex),
                    cursor.getInt(parentIdColumnIndex),
                    cursor.getString(nameColumnIndex),
                    cursor.getString(descriptionColumnIndex));
            if (cursor.getInt(doneColumnIndex) == 1){
                task.setDone(true);
            }else {
                task.setDone(false);
            }
            task.setPriority(cursor.getInt(priorityColumnIndex));
            //task.setParentId(cursor.getInt(parentIdColumnIndex));

        }else {
            return null;
        }
        cursor.close();
        db.close();
        return task;
    }

    public long saveTask(Context context, Task task){
        dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DB_PARENT_ID, task.getParentId());
        contentValues.put(DB_TASK_NAME, task.getTaskName());
        contentValues.put(DB_TASK_DESCRIPTION, task.getTaskDescription());
        contentValues.put(DB_TASK_PRIORITY, task.getPriority());
        if (task.isDone()){
            contentValues.put(DB_TASK_DONE, 1);
        }else {
            contentValues.put(DB_TASK_DONE, 0);
        }
        long id = db.insert(DB_TABLE_NAME, null, contentValues);
        db.close();
        if (id == -1){

        }else {
            Log.d(LOG_TAG,"write successfully");
            Log.d(LOG_TAG, getTask(context, (int)id).toString());
        }
        return id;
    }

    public long updateTask(Context context, Task task){
        /*
        // подготовим значения для обновления
          cv.put("name", name);
          cv.put("email", email);
        // обновляем по id
          int updCount = db.update("mytable", cv, "id = ?",
          new String[] { id });
        */

        dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //contentValues.put(DB_TASK_ID, task.getTaskId());
        //contentValues.put(DB_PARENT_ID, task.getParentId());
        contentValues.put(DB_TASK_NAME, task.getTaskName());
        contentValues.put(DB_TASK_DESCRIPTION, task.getTaskDescription());
        contentValues.put(DB_TASK_PRIORITY, task.getPriority());
        if (task.isDone()){
            contentValues.put(DB_TASK_DONE, 1);
        }else {
            contentValues.put(DB_TASK_DONE, 0);
        }

        long id = db.update(DB_TABLE_NAME, contentValues, "id = ?", new String[]{String.valueOf(task.getTaskId())});
        db.close();
        if (id == -1){

        }else {
            Log.d(LOG_TAG,"write successfully");
            Log.d(LOG_TAG, getTask(context, task.getTaskId()).toString());
        }
        return id;
    }

    public int deleteTask(Context context, Task task){
        //написать удаление строк
        /* http://developer.android.com/training/basics/data-storage/databases.html
        // Define 'where' part of query.
        String selection = FeedEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { String.valueOf(rowId) };
        // Issue SQL statement.
        db.delete(table_name, selection, selectionArgs);
        */
        dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = DB_TASK_ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(task.getTaskId())};
        int num = db.delete(DB_TABLE_NAME,selection,selectionArgs);
        db.close();
        return num;
    }

    public int deleteTaskRecursive(Context context, Task task){
        //написать удаление строк
        /* http://developer.android.com/training/basics/data-storage/databases.html
        // Define 'where' part of query.
        String selection = FeedEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { String.valueOf(rowId) };
        // Issue SQL statement.
        db.delete(table_name, selection, selectionArgs);
        */
        dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (hasChildren(context, task)){
            String selectionForChildren = DB_PARENT_ID + " LIKE ?";
            String[] selectionArgsForChildren = {String.valueOf(task.getTaskId())};
            Cursor cursor = db.query(DB_TABLE_NAME, null, selectionForChildren, selectionArgsForChildren, null, null, null);
            if (cursor.moveToFirst()){
                int idColumnIndex = cursor.getColumnIndex(DB_TASK_ID);
                do{
                    deleteTaskRecursive(context, getTask(context, cursor.getInt(idColumnIndex)));
                }while (cursor.moveToNext());
            }
            cursor.close();
        }
        String selection = DB_TASK_ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(task.getTaskId())};
        int num = db.delete(DB_TABLE_NAME,selection,selectionArgs);
        db.close();
        return num;
    }

    public boolean hasChildren (Context context, Task task){
        dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int parentId = task.getTaskId();
        String selection = DB_PARENT_ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(parentId)};
        Cursor cursor = db.query(DB_TABLE_NAME, null, selection, selectionArgs, null, null, null);
        boolean result;
        if (cursor.moveToFirst()){
            result = true;
        }else {
            result = false;
        }
        cursor.close();
        db.close();
        return result;
    }

    /*
    PK
    |id|parent_id|name|description|has_children|
    |id|parent_id|name|description|done|
    |id|parent_id|name|description|done|priority
    */

    class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            // конструктор суперкласса
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(LOG_TAG, "--- onCreate database ---");
            // создаем таблицу с полями
            //V1 |id|parent_id|name|description|has_children|
            //V2 |id|parent_id|name|description|done|
            db.execSQL("create table "+DB_TABLE_NAME+" ("
                    + DB_TASK_ID +" integer primary key autoincrement,"
                    + DB_PARENT_ID+" integer,"
                    + DB_TASK_NAME+" text,"
                    + DB_TASK_DESCRIPTION+" text,"
                    + DB_TASK_DONE+" integer,"
                    + DB_TASK_PRIORITY+" integer"
                    + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if ((oldVersion == 1) && (newVersion == 2)){
                //ContentValues contentValues = new ContentValues();
                db.beginTransaction();
                try {
                    //
                    /*
                    db.execSQL("create temporary table people_tmp (id integer, name text, position text, posid integer);");

                    db.execSQL("insert into people_tmp select id, name, position, posid from people;");
                    db.execSQL("drop table people;");

                    db.execSQL("create table people (id integer primary key autoincrement, name text, posid integer);");

                    db.execSQL("insert into people select id, name, posid from people_tmp;");
                    db.execSQL("drop table people_tmp;");*/

                    db.execSQL("create temporary table tasks_tmp ("
                            + DB_TASK_ID +" integer,"
                            + DB_PARENT_ID+" integer,"
                            + DB_TASK_NAME+" text,"
                            + DB_TASK_DESCRIPTION+" text"
                            /*+ DB_TASK_DONE+" integer"*/
                            + ");");
                    db.execSQL("insert into tasks_tmp select " +DB_TASK_ID+", "+DB_PARENT_ID+", "
                            +DB_TASK_NAME+", "+DB_TASK_DESCRIPTION+" from "+DB_TABLE_NAME+
                            ";");
                    //db.execSQL("alter table people add column posid integer;");
                    db.execSQL("alter table tasks_tmp add column "+DB_TASK_DONE+" integer;");
                    db.execSQL("drop table "+DB_TABLE_NAME+";)");
                    db.execSQL("create table "+DB_TABLE_NAME+" ("
                            + DB_TASK_ID +" integer primary key autoincrement,"
                            + DB_PARENT_ID+" integer,"
                            + DB_TASK_NAME+" text,"
                            + DB_TASK_DESCRIPTION+" text,"
                            + DB_TASK_DONE+" integer"
                            + ");");
                    db.execSQL("insert into "+DB_TABLE_NAME+" select " +DB_TASK_ID+", "+DB_PARENT_ID+", "
                            +DB_TASK_NAME+", "+DB_TASK_DESCRIPTION+", "+DB_TASK_DONE+" from tasks_tmp;");
                    db.setTransactionSuccessful();
                }finally {
                    db.endTransaction();

                }
            }//end first if
            if ((oldVersion == 2) && (newVersion == 3)){
                //ContentValues contentValues = new ContentValues();
                db.beginTransaction();
                try {
                    db.execSQL("alter table "+DB_TABLE_NAME+" add column "+DB_TASK_PRIORITY+" integer;");
                    db.setTransactionSuccessful();
                }finally {
                    db.endTransaction();
                }
            }//end second if
        }//end onUpgrade

    }//end DBHelper

}//end class

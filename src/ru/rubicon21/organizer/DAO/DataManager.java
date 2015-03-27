package ru.rubicon21.organizer.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import ru.rubicon21.organizer.entity.Task;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by roma on 22.03.2015.
 */
public class DataManager {

    ArrayList<Task> tasks;

    DBHelper dbHelper;

    final String DB_NAME = "taskDB";
    final String DB_TABLE_NAME = "tasks";
    final String DB_ID = "id";
    final String DB_PARENT_ID = "parent_id";
    final String DB_TASK_NAME = "name";
    final String DB_TASK_DESCRIPTION = "description";
    final String DB_HAS_CHILDREN = "has_children";

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
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = DB_PARENT_ID+" LIKE ?";
        String[] selectionArgs = {String.valueOf(_parent_id)};
        Cursor cursor = db.query(DB_TABLE_NAME, null, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()){
            int idColumnIndex = cursor.getColumnIndex(DB_ID);
            int parentIdColumnIndex = cursor.getColumnIndex(DB_PARENT_ID);
            int nameColumnIndex = cursor.getColumnIndex(DB_TASK_NAME);
            int descriptionColumnIndex = cursor.getColumnIndex(DB_TASK_DESCRIPTION);
            do{
                tasks.add(new Task(
                        cursor.getInt(idColumnIndex),
                        cursor.getInt(parentIdColumnIndex),
                        cursor.getString(nameColumnIndex),
                        cursor.getString(descriptionColumnIndex)));

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
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = DB_ID+" LIKE ?";
        String[] selectionArgs = {String.valueOf(_id)};
        Cursor cursor = db.query(DB_TABLE_NAME, null, selection, selectionArgs, null, null, null);
        Task task = new Task("","");
        if (cursor.moveToFirst()){
            int idColumnIndex = cursor.getColumnIndex(DB_ID);
            int parentIdColumnIndex = cursor.getColumnIndex(DB_PARENT_ID);
            int nameColumnIndex = cursor.getColumnIndex(DB_TASK_NAME);
            int descriptionColumnIndex = cursor.getColumnIndex(DB_TASK_DESCRIPTION);
            task =  new Task(
                    cursor.getInt(idColumnIndex),
                    cursor.getInt(parentIdColumnIndex),
                    cursor.getString(nameColumnIndex),
                    cursor.getString(descriptionColumnIndex));
            //task.setParentId(cursor.getInt(parentIdColumnIndex));

        }else {
            //сделать оповещение о пустой базе
        }
        cursor.close();
        db.close();
        return task;
    }

    public void saveTask(Context context, Task task) throws SQLException{
        dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DB_PARENT_ID, task.getParentId());
        contentValues.put(DB_TASK_NAME, task.getTaskName());
        contentValues.put(DB_TASK_DESCRIPTION, task.getTaskDescription());
        long id = db.insert(DB_TABLE_NAME, null, contentValues);
        db.close();
        if (id == -1){
            throw new SQLException("write fail");
        }else {
            Log.d(LOG_TAG,"write successfully");
            Log.d(LOG_TAG, getTask(context, (int)id).toString());
        }
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
        String selection = DB_ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(task.getTaskId())};
        int num = db.delete(DB_TABLE_NAME,selection,selectionArgs);
        db.close();
        return num;
    }

    /*
    PK
    |id|parent_id|name|description|has_children|
    */

    class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            // конструктор суперкласса
            super(context, DB_NAME, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(LOG_TAG, "--- onCreate database ---");
            // создаем таблицу с полями
            //|id|parent_id|name|description|has_children|
            db.execSQL("create table "+DB_TABLE_NAME+" ("
                    + DB_ID+" integer primary key autoincrement,"
                    + DB_PARENT_ID+" integer,"
                    + DB_TASK_NAME+" text,"
                    + DB_TASK_DESCRIPTION+" text,"
                    + DB_HAS_CHILDREN+" integer"
                    + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

}

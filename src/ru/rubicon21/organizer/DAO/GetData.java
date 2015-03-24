package ru.rubicon21.organizer.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import ru.rubicon21.organizer.entity.Task;

import java.util.ArrayList;

/**
 * Created by roma on 22.03.2015.
 */
public class GetData {

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


    public ArrayList<Task> getTasks(){
        tasks = new ArrayList<Task>();
        tasks.add(new Task("Тест","Пробная запись"));
        tasks.add(new Task("Тест 2","Вторая Пробная запись"));
        return tasks;
    }

    /*
     PK
    |id|parent_id|name|description|has_children|

    */
    /*
    * switch (v.getId()) {
            case R.id.btnAdd:
                Log.d(LOG_TAG, "--- Insert in mytable: ---");
                // подготовим данные для вставки в виде пар: наименование столбца - значение

                cv.put("name", name);
                cv.put("email", email);
                // вставляем запись и получаем ее ID
                long rowID = db.insert("mytable", null, cv);
                Log.d(LOG_TAG, "row inserted, ID = " + rowID);
                break;
            case R.id.btnRead:
                Log.d(LOG_TAG, "--- Rows in mytable: ---");
                // делаем запрос всех данных из таблицы mytable, получаем Cursor
                Cursor c = db.query("mytable", null, null, null, null, null, null);

                // ставим позицию курсора на первую строку выборки
                // если в выборке нет строк, вернется false
                if (c.moveToFirst()) {

                    // определяем номера столбцов по имени в выборке
                    int idColIndex = c.getColumnIndex("id");
                    int nameColIndex = c.getColumnIndex("name");
                    int emailColIndex = c.getColumnIndex("email");

                    do {
                        // получаем значения по номерам столбцов и пишем все в лог
                        Log.d(LOG_TAG,
                                "ID = " + c.getInt(idColIndex) +
                                        ", name = " + c.getString(nameColIndex) +
                                        ", email = " + c.getString(emailColIndex));
                        // переход на следующую строку
                        // а если следующей нет (текущая - последняя), то false - выходим из цикла
                    } while (c.moveToNext());
                } else
                    Log.d(LOG_TAG, "0 rows");
                c.close();
                break;
            case R.id.btnClear:
                Log.d(LOG_TAG, "--- Clear mytable: ---");
                // удаляем все записи
                int clearCount = db.delete("mytable", null, null);
                Log.d(LOG_TAG, "deleted rows count = " + clearCount);
                break;
        }
        // закрываем подключение к БД
        dbHelper.close();
    }*/
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
            db.execSQL("create table "+DB_TABLE_NAME+" ("
                    + DB_ID+" integer primary key autoincrement,"
                    + DB_PARENT_ID+" integer,"
                    + DB_TABLE_NAME+" text,"
                    + DB_TASK_DESCRIPTION+" text,"
                    + DB_HAS_CHILDREN+" integer"
                    + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

}

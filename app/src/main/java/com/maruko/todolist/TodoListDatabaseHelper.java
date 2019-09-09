package com.maruko.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

public class TodoListDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "todolist";
    private static final int DB_VERSION = 1;

    TodoListDatabaseHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        updataMyDatabase(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        updataMyDatabase(db, oldVersion, newVersion);
    }

    private static void insertTask(SQLiteDatabase db, String task, int year, int month, int day, int hour, int minute, String note){
        ContentValues taskvalues = new ContentValues();
        taskvalues.put("TASK", task);
        taskvalues.put("YEAR", year);
        taskvalues.put("MONTH", month);
        taskvalues.put("DAY", day);
        taskvalues.put("HOUR", hour);
        taskvalues.put("MINUTE", minute);
        taskvalues.put("NOTE", note);
        db.insert("TASKT", null, taskvalues);
    }

    private void updataMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion){
        if(oldVersion < 1){
            db.execSQL("CREATE TABLE TASKT (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "TASK TEXT,"
                    + "YEAR INTEGER,"
                    + "MONTH INTEGER,"
                    + "DAY INTEGER,"
                    + "HOUR INTEGER,"
                    + "MINUTE INTEGER,"
                    + "NOTE TEXT);");

            insertTask(db, "Finish ToDoList app", 2019, 8, 1, 18, 0, null);
            insertTask(db, "Read Head First Android Development", 2019, 8, 2, 18, 0, null);
        }
    }
}

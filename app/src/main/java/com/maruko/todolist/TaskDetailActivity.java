package com.maruko.todolist;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

public class TaskDetailActivity extends AppCompatActivity {

    private ConstraintLayout linearLayout;
    private TextView tv_date;
    public static final String EXTRA_TASKID = "taskId";
    private int taskid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        setViews();
        setListeners();

        taskid = (Integer) getIntent().getExtras().get(EXTRA_TASKID);
        if(taskid != -1) {
            SQLiteOpenHelper todoListDatabaseHelper = new TodoListDatabaseHelper(this);
            try {
                SQLiteDatabase db = todoListDatabaseHelper.getReadableDatabase();
                Cursor cursor = db.query("TASKT",
                        new String[]{"TASK", "YEAR", "MONTH", "DAY", "HOUR", "MINUTE", "NOTE"},
                        "_id = ?",
                        new String[]{Integer.toString(taskid)},
                        null, null, null);

                if (cursor.moveToFirst()) {
                    String taskText = cursor.getString(0);
                    int year  = cursor.getInt(1);
                    int month = cursor.getInt(2);
                    int day = cursor.getInt(3);
                    String note = cursor.getString(6);

                    EditText taskname = (EditText) findViewById(R.id.taskdetail);
                    taskname.setText(taskText);

                    TextView date = (TextView) findViewById(R.id.taskdate);
                    String yearmonthday = year + "-" + format(month) + "-" + format(day);
                    date.setText(yearmonthday);

                    EditText tasknote = (EditText)findViewById(R.id.addnotes);
                    tasknote.setText(note);

                }
                cursor.close();
                db.close();
            } catch (SQLException e) {
                Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    private void setListeners(){
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerUtil.pickDate(TaskDetailActivity.this, tv_date, DatePickerUtil.THEME_HOLO_DARK);
            }
        });
    }

    private void setViews(){
        linearLayout = (ConstraintLayout) findViewById(R.id.tasklayout);
        tv_date = (TextView)findViewById(R.id.taskdate);
    }

    public void onClickDone(View view){
        SQLiteOpenHelper todolistdatabaseHelper = new TodoListDatabaseHelper(this);
        SQLiteDatabase db = todolistdatabaseHelper.getWritableDatabase();
        EditText taskdetail = (EditText) findViewById(R.id.taskdetail);
        TextView taskdate = (TextView)findViewById(R.id.taskdate);
        EditText tasknote = (EditText) findViewById(R.id.addnotes);

        String task = taskdetail.getText().toString();
        String time = taskdate.getText().toString();
        String note = tasknote.getText().toString();

        String[] subdate = time.split("-");
        int year = Integer.parseInt(subdate[0]);
        int month = Integer.parseInt(subdate[1]);
        int day = Integer.parseInt(subdate[2]);
        int hour = 8;
        int minute = 0;

        //taskid = (Integer) getIntent().getExtras().get(EXTRA_TASKID);


        inserttask(db, task, year, month, day, hour, minute, note, taskid);
        CheckBox checkBox = (CheckBox)findViewById(R.id.delete);
        if(checkBox.isChecked()){
            db.delete("TASKT", "_id=?", new String[]{Integer.toString(taskid)});
        }

        db.close();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private static void inserttask(SQLiteDatabase db, String task, int year, int month, int day, int hour, int minute, String note, int taskid){
        ContentValues taskvalues = new ContentValues();
        taskvalues.put("TASK", task);
        taskvalues.put("YEAR", year);
        taskvalues.put("MONTH", month);
        taskvalues.put("DAY", day);
        taskvalues.put("HOUR", hour);
        taskvalues.put("MINUTE", minute);
        taskvalues.put("NOTE", note);
        if(taskid == -1) {
            db.insert("TASKT", null, taskvalues);
        }else{
            db.update("TASKT", taskvalues, "_id=?", new String[]{Integer.toString(taskid)});
        }
    }

    private static String format(int i) {
        return (String.valueOf(i).length() == 1)?("0"+i): String.valueOf(i);
    }
}

package com.maruko.todolist;


import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.lang.annotation.Inherited;


/**
 * A simple {@link Fragment} subclass.
 */
public class TaskFragment extends Fragment {

    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_task, container, false);
    }

    @Override
    public void onStart(){
        super.onStart();
        View view = getView();
        NoScrollListView listtasks = null;
        if(view != null){
            listtasks = (NoScrollListView) view.findViewById(R.id.list_tasks);
            SQLiteOpenHelper todolistDatabaseHelper = new TodoListDatabaseHelper(getContext());
            try{
                db = todolistDatabaseHelper.getReadableDatabase();
                cursor = db.query("TASKT",
                        new String[] {"_id", "TASK"},
                        null, null, null, null, null);
                SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(getContext(),
                        android.R.layout.simple_list_item_1,
                        cursor,
                        new String[] {"TASK"},
                        new int[] {android.R.id.text1},
                        0);

                listtasks.setAdapter(listAdapter);
            }catch (SQLException e){
                Toast toast = Toast.makeText(getContext(), "Database Unavaiable", Toast.LENGTH_SHORT);
                toast.show();
            }
        }

        AdapterView.OnItemClickListener itemClickListener =
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> listtasks, View view, int position, long id) {
                        Intent intent = new Intent(getContext(), TaskDetailActivity.class);
                        intent.putExtra(TaskDetailActivity.EXTRA_TASKID, (int)id);
                        startActivity(intent);
                    }
                };
        listtasks.setOnItemClickListener(itemClickListener);

    }

    @Override
    public void onResume(){
        super.onResume();
        Cursor newCursor = db.query("TASKT",
                new String[] {"_id", "TASK"},
                null, null, null, null, null);
        View view = getView();
        if(view != null) {
            ListView listtasks = (ListView) view.findViewById(R.id.list_tasks);
            CursorAdapter cursorAdapter = (CursorAdapter) listtasks.getAdapter();
            cursorAdapter.changeCursor(newCursor);
            cursor = newCursor;
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        db.close();
        cursor.close();
    }
}

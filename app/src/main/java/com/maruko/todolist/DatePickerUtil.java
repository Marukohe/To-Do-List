package com.maruko.todolist;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

public class DatePickerUtil {

    private static int year = Calendar.getInstance().get(Calendar.YEAR);
    private static int month = Calendar.getInstance().get(Calendar.MONTH);
    private static int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

    public static final int THEME_HOLO_DARK = DatePickerDialog.THEME_DEVICE_DEFAULT_LIGHT;

    public static void pickDate(Context context, final TextView tv_date, int theme){
        DatePickerDialog dialog = new DatePickerDialog(context, theme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                String s = y + "-" + format(m + 1) + "-" + format(d + 1);
                tv_date.setText(s);
                year = y;
                month = m;
                day = d;
            }
        },year, month, day);
        dialog.show();
    }

    private static String format(int i) {
        return (String.valueOf(i).length() == 1)?("0"+i): String.valueOf(i);
    }

}

package com.example.mycloudmusic.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.example.mycloudmusic.R;

import java.util.Calendar;


public class DateDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private DateListener dateListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        //获取日历控件
        Calendar calendar = Calendar.getInstance();

        //获取年
        int year = calendar.get(Calendar.YEAR);

        //月
        int month = calendar.get(Calendar.MONTH);

        //获取天
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog pickerDialog = new DatePickerDialog(getContext(), R.style.Theme_AppCompat_Light_Dialog_Alert, this,year,month,day);

        return pickerDialog;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        String date = String.format("%d-%02d-%02d",year,month+1,dayOfMonth);

        dateListener.onSelected(date);

        dismiss();
    }

    public interface DateListener {
        void onSelected(String date);
    }

    public static DateDialogFragment getInstance(){
        DateDialogFragment fragment = new DateDialogFragment();
        return fragment;
    }

    public static void show(FragmentManager fragmentManager, DateListener listener){
        DateDialogFragment fragment = getInstance();
        fragment.dateListener = listener;
        fragment.show(fragmentManager,"DateDialogFragment");
    }
}

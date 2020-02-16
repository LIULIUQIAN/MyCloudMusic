package com.example.mycloudmusic.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.example.mycloudmusic.R;

public class ConfirmDialogFragment extends DialogFragment {

    private int messageResourceId;
    DialogInterface.OnClickListener onClickListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {



        return new AlertDialog.Builder(getContext())
                .setTitle(R.string.tips)
                .setMessage(messageResourceId)
                .setPositiveButton(R.string.confirm,onClickListener)
                .setNegativeButton(R.string.cancel,null)
                .create();
    }

    public static ConfirmDialogFragment getInstance(){
        ConfirmDialogFragment fragment = new ConfirmDialogFragment();
        return fragment;
    }

    public static void show(FragmentManager fragmentManager,int messageResourceId,DialogInterface.OnClickListener onClickListener){
        ConfirmDialogFragment fragment = getInstance();
        fragment.messageResourceId = messageResourceId;
        fragment.onClickListener = onClickListener;
        fragment.show(fragmentManager,"ConfirmDialogFragment");
    }

    public static void show(FragmentManager fragmentManager,DialogInterface.OnClickListener onClickListener){
        show(fragmentManager,R.string.confirm_message,onClickListener);
    }
}

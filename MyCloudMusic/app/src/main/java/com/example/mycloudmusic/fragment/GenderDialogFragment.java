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

public class GenderDialogFragment extends DialogFragment {

    private int selectedIndex;
    private DialogInterface.OnClickListener onClickListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        return new AlertDialog.Builder(getContext()).setTitle(R.string.select_gender)
                .setSingleChoiceItems(R.array.dialog_gender, selectedIndex, onClickListener).create();
    }

    public static GenderDialogFragment getInstance() {
        GenderDialogFragment fragment = new GenderDialogFragment();
        return fragment;
    }

    public static void show(FragmentManager fragmentManager, int selectedIndex, DialogInterface.OnClickListener onClickListener) {
        GenderDialogFragment fragment = getInstance();
        fragment.selectedIndex = selectedIndex;
        fragment.onClickListener = onClickListener;
        fragment.show(fragmentManager, "GenderDialogFragment");
    }
}

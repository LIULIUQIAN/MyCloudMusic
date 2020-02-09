package com.example.mycloudmusic.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.example.mycloudmusic.R;

public class CommentMoreDialogFragment extends DialogFragment {

    private DialogInterface.OnClickListener onClickListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setItems(R.array.dialog_comment_more, onClickListener);

        return builder.create();
    }

    public void show(FragmentManager fragmentManager, DialogInterface.OnClickListener onClickListener) {

        this.onClickListener = onClickListener;
        show(fragmentManager, "CommentMoreDialogFragment");
    }

    public static CommentMoreDialogFragment getInstance() {
        CommentMoreDialogFragment fragment = new CommentMoreDialogFragment();
        return fragment;
    }
}

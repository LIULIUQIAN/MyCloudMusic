package com.example.mycloudmusic.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.example.mycloudmusic.R;
import com.example.mycloudmusic.domain.event.CreateSheetEvent;
import com.example.mycloudmusic.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

public class CreateSheetDialogFragment extends DialogFragment {

    private EditText et_name;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View view = View.inflate(getActivity(), R.layout.dialog_create_sheet, null);
        et_name = view.findViewById(R.id.et_name);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.create_sheet).setView(view)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String data = et_name.getText().toString().trim();
                        if (TextUtils.isEmpty(data)){
                            ToastUtil.errorShortToast(R.string.hint_enter_sheet_name);
                        }else {
                            EventBus.getDefault().post(new CreateSheetEvent(data));
                        }

                    }
                });

        return builder.create();
    }

    public static CreateSheetDialogFragment getInstance(){

        CreateSheetDialogFragment fragment = new CreateSheetDialogFragment();
        return fragment;
    }

    public static void show(FragmentManager fragmentManager){

        CreateSheetDialogFragment fragment = getInstance();
        fragment.show(fragmentManager,"CreateSheetDialogFragment");
    }
}

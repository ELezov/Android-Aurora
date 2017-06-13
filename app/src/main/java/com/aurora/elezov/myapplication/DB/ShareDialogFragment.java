package com.aurora.elezov.myapplication.DB;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.aurora.elezov.myapplication.MainActivity;
import com.aurora.elezov.myapplication.MakeTravelNextActivity;
import com.aurora.elezov.myapplication.R;

/**
 * Created by ASUS on 13.06.2017.
 */

public class ShareDialogFragment extends DialogFragment implements TextView.OnEditorActionListener {


        private EditText mEditText;

        public interface EditNameDialogListener {
            void onFinishEditDialog(String inputText);
        }


    public ShareDialogFragment() {
            // Пустой конструктор должен быть прописан для DialogFragment
        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_share, container);
        mEditText = (EditText) view.findViewById(R.id.editText2);
        getDialog().setTitle("Укажите email получателя");

        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        mEditText.setOnEditorActionListener(this);

        return view;

    }
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            // Return input text to activity
            EditNameDialogListener activity = (EditNameDialogListener) getActivity();

            activity.onFinishEditDialog(mEditText.getText().toString());
            this.dismiss();
            return true;
        }
        return false;
    }
}



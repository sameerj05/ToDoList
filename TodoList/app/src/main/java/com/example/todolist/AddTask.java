package com.example.todolist;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Collections;
import java.util.List;

public class AddTask extends BottomSheetDialogFragment {

    public static final String TAG = "ActionBottomDialog";
    private EditText newTxt;
    private Button TskSvbtn;
    private Sqldatabase sqldb;
    private Context context;
    public static AddTask newInstance(){
        return new AddTask();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DlgStyle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.ntask, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        );

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        newTxt = getView().findViewById(R.id.nText);
        TskSvbtn = getView().findViewById(R.id.Button);


        boolean isUpdate = false;
        context=this.context;
        final Bundle bundle = getArguments();
        if(bundle != null){
            isUpdate = true;
            String task = bundle.getString("task");
            newTxt.setText(task);
            assert task != null;
            if(task.length()>0)
                TskSvbtn.setTextColor(getResources().getColor(R.color.black));

        }

        sqldb = new Sqldatabase(getActivity());
        sqldb.openDatabase();

        newTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence c, int st, int cnt, int aft) {
            }

            @Override
            public void onTextChanged(CharSequence c, int st, int bf, int cnt) {
                if(c.toString().equals("")){
                    TskSvbtn.setEnabled(false);
                    TskSvbtn.setTextColor(Color.GRAY);
                }
                else{
                    TskSvbtn.setEnabled(true);
                    TskSvbtn.setTextColor(getResources().getColor(R.color.teal_200));
                }
            }

            @Override
            public void afterTextChanged(Editable c) {
            }
        });

        final boolean finalIsUpdate = isUpdate;
        TskSvbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String text = newTxt.getText().toString();
                if (finalIsUpdate) {
                    sqldb.updTsk(bundle.getInt("id"), text);
                } else {
                    TaskObj task = new TaskObj();
                    task.setTsk(text);
                    task.setStatus(0);
                    sqldb.insTsk(task);

                }

                dismiss();

            }
        });
     }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog){
        Activity activity = getActivity();
        if(activity instanceof DlgCloseinr) {
          System.out.println("Dialog Close activity");
          ((DlgCloseinr) activity).handleDialogClose(dialog);
            }
        }

}


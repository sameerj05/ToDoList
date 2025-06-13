package com.example.todolist;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Collections;
import java.util.List;
import java.util.zip.Inflater;


public class TaskList extends Fragment  {
    private RecyclerView RecyclerVw;
    private FloatingActionButton fbtn;
    private List<TaskObj> taskLst;

    private ListAdapter listAdapter;
    private Sqldatabase sqlDB;
    public Context context;
    public MainActivity mainActivity;
    public ViewGroup cont;
    public LayoutInflater infl;
    public Activity activity;
    public FragmentManager fM;
    public static final String TAG = "TaskList";
    public TaskList() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getContext();
        sqlDB = new Sqldatabase(context);
        sqlDB.openDatabase();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        cont= container;
        infl=inflater;
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task_list, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {


        fM =   getParentFragmentManager();

        listAdapter = new ListAdapter(sqlDB, mainActivity, fM);

        context=view.getContext();
        activity = getActivity();
        if (context==null)
            System.out.println("Context is null in Tasklist");
        RecyclerVw = view.findViewById(R.id.RecyclerVw);

        RecyclerVw.setLayoutManager(new LinearLayoutManager(context));
        RecyclerVw.setAdapter(listAdapter);

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new RcItemHelper(listAdapter, cont, infl, activity,context));
        itemTouchHelper.attachToRecyclerView(RecyclerVw);


        taskLst = sqlDB.getAllTasks();
        Collections.reverse(taskLst);
        fbtn = view.findViewById(R.id.fbtn);

        listAdapter.setTasks(taskLst);
        RecyclerVw.invalidate();
        fbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("# in Tasklst before add " + taskLst.size());
                RecyclerVw.invalidate();
                AddTask.newInstance().show(fM, AddTask.TAG);
                taskLst.clear();
                taskLst=sqlDB.getAllTasks();
                System.out.println("# in Tasklst after add " + taskLst.size());
                Collections.reverse(taskLst);
                listAdapter.setTasks(taskLst);
                listAdapter.notifyDataSetChanged();
            }
        });
    }



}
package com.example.todolist;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.Collections;
import java.util.List;
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private List<TaskObj> tdList;
    private Sqldatabase sqldb;
    private MainActivity activity;
    private FragmentManager fragmentManager;
    public ListAdapter(Sqldatabase db, MainActivity activity, FragmentManager fm) {
        this.sqldb = db;
        this.activity = activity;
        this.fragmentManager=fm;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_lay, parent, false);
        return new ViewHolder(itemView);
    }
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
       sqldb.openDatabase();

        final TaskObj tsk = tdList.get(position);
        holder.task.setText(tsk.getTsk());
        holder.task.setChecked(toBoolean(tsk.getStatus()));
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sqldb.updSt(tsk.getId(), 1);
                } else {
                  sqldb.updSt(tsk.getId(), 0);
                }
            }
        });
    }
    public void deleteTsk(int position) {
        TaskObj tsk = tdList.get(position);
        sqldb.delTsk(tsk.getId());
        tdList.remove(position);
        notifyItemRemoved(position);
    }

    public void editTsk(int position) {
        TaskObj tsk = tdList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", tsk.getId());
        bundle.putString("task", tsk.getTsk());
        AddTask addTask = new AddTask();
        addTask.setArguments(bundle);
        if (fragmentManager == null)
            System.out.println("Fragment Manager is null");
        else {
            addTask.show(fragmentManager, AddTask.TAG);
        notifyItemChanged(position);
        notifyDataSetChanged();


        }

    }
    private boolean toBoolean(int i) {
        return i != 0;
    }
    public int getItemCount() {
        return tdList.size();
    }
    public Context getContext() {
        return getContext();
    }

    public void setTasks(List<TaskObj> tdList) {
        this.tdList = tdList;
        notifyDataSetChanged();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox task;

        ViewHolder(View view) {
            super(view);
            task = view.findViewById(R.id.tskCheck);
        }
    }
}

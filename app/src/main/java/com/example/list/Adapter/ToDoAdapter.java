package com.example.list.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.list.AddNewTask;
import com.example.list.MainActivity;
import com.example.list.Model.ToDoModel;
import com.example.list.R;
import com.example.list.Utils.DataBaseHelper;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.MyViewHolder> {


    private List<ToDoModel> mList;
    private MainActivity activity;
    private DataBaseHelper myDB;
    public ToDoAdapter(DataBaseHelper myDB,MainActivity activity){
        this.activity = activity;
        this.myDB = myDB;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final ToDoModel item = mList.get(position);
        holder.mCheckBox.setText(item.getTask());
        holder.mCheckBox.setChecked(toBoolean(item.getStatus()));

        // Set the checked and unchecked colors
        int colorUnchecked = Color.WHITE; // Set your desired color for unchecked state
        int colorChecked = Color.WHITE; // Set your desired color for checked state

        // Change the checkbox color when it's checked
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    myDB.updateStatus(item.getId() , 1);
                    holder.mCheckBox.setTextColor(colorChecked);
                } else {
                    myDB.updateStatus(item.getId() , 0);
                    holder.mCheckBox.setTextColor(colorUnchecked);
                }
            }
        });

        // Set text color initially based on checked state
        if (holder.mCheckBox.isChecked()) {
            holder.mCheckBox.setTextColor(colorChecked);
        } else {
            holder.mCheckBox.setTextColor(colorUnchecked);
        }
    }

    public boolean toBoolean(int num){
        return num!=0;
    }
    public Context getContext(){
        return activity;
    }
    public void setTasks(List<ToDoModel> mList){
        this.mList=mList;
        notifyDataSetChanged();
    }
    public void deleteTask(int position){
        ToDoModel item=mList.get(position);
        myDB.deleteTask(item.getId());
        mList.remove(position);
        notifyItemRemoved(position);
    }
    public void editItem(int position) {
        ToDoModel item = mList.get(position);

        // Creating an instance of AddNewTask
        AddNewTask task = AddNewTask.newInstance();

        // Prepare arguments
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId()); // Replace with appropriate item ID
        bundle.putString("task", item.getTask()); // Replace with appropriate task data

        // Set arguments to the fragment
        task.setArguments(bundle);

        // Show the fragment
        if (activity != null && activity.getSupportFragmentManager() != null) {
            task.show(activity.getSupportFragmentManager(), AddNewTask.TAG);
        }
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void notifyDataSetChanged(int position) {
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        CheckBox mCheckBox;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mCheckBox = itemView.findViewById(R.id.mcheckbox);
        }
    }
        }

package com.example.android.todolistfirebase;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.todolistfirebase.model.Task;

import java.util.ArrayList;

/**
 * Created by Jay-Ar Gabriel on 4/10/2017.
 */

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private ArrayList<Task> mDataSet;


    public TaskAdapter(ArrayList<Task> taskDataSet) {
        mDataSet = taskDataSet;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.task_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        TaskViewHolder viewHolder = new TaskViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        Task task = mDataSet.get(position);
        String id = task.getTaskId();

        holder.itemView.setTag(id);

        holder.bind(mDataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public ArrayList<Task> swapTask(ArrayList<Task> task) {

        if (mDataSet == null) {
            return null;
        }

        ArrayList<Task> temp = mDataSet;
        this.mDataSet = task;

        if (task != null) {
            this.notifyDataSetChanged();
        }

        return temp;

    }

    class TaskViewHolder extends RecyclerView.ViewHolder {

        TextView listItemTaskView;
        TextView listItemPriorityView;

        public TaskViewHolder(View itemView) {
            super(itemView);

            listItemTaskView = (TextView) itemView.findViewById(R.id.tv_item_task);
            listItemPriorityView = (TextView) itemView.findViewById(R.id.tv_item_priority);

        }

        void bind(Task task) {
            listItemTaskView.setText(task.getDescription());
            listItemPriorityView.setText(String.valueOf(task.getPriority()));
        }

    }

}

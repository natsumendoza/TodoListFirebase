package com.example.android.todolistfirebase;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.example.android.todolistfirebase.model.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<ArrayList<Task>> {

    private TaskAdapter mAdapter;
    private RecyclerView mTaskList;

    private ArrayList<Task> getTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getTasks = new ArrayList<>();

        mTaskList = (RecyclerView) findViewById(R.id.recyclerViewTasks);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mTaskList.setLayoutManager(layoutManager);

        mTaskList.setHasFixedSize(true);

        FloatingActionButton fabButton = (FloatingActionButton) findViewById(R.id.fab);
        fabButton.setOnClickListener(new FabButtonClickListener());

        mAdapter = new TaskAdapter(getTasks);

        mTaskList.setAdapter(mAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                String id = (String) viewHolder.itemView.getTag();

                AddTaskActivity.databaseTasks.child(id).removeValue();
                getSupportLoaderManager().restartLoader(0, null, MainActivity.this);

            }
        }).attachToRecyclerView(mTaskList);

        getSupportLoaderManager().initLoader(0, null, this);

    }

    @Override
    protected void onStart() {
        super.onStart();



        AddTaskActivity.databaseTasks.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                getTasks.clear();

                for (DataSnapshot taskSnapshot : dataSnapshot.getChildren()) {
                    getTasks.add(taskSnapshot.getValue(Task.class));
                }
                getSupportLoaderManager().restartLoader(0, null, MainActivity.this);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        getSupportLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public Loader<ArrayList<Task>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<ArrayList<Task>>(this) {

            ArrayList<Task> mTaskData = null;

            @Override
            protected void onStartLoading() {
                if (mTaskData != null) {
                    deliverResult(mTaskData);
                } else {
                    forceLoad();
                }
            }

            @Override
            public ArrayList<Task> loadInBackground() {
                return getTasks;
            }

            public void deliverResult(ArrayList<Task> tasks) {
                mTaskData = tasks;
                super.deliverResult(tasks);
            }

        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Task>> loader, ArrayList<Task> data) {
        mAdapter.swapTask(data);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Task>> loader) {
        mAdapter.swapTask(null);
    }

    class FabButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent addTaskIntent = new Intent(MainActivity.this, AddTaskActivity.class);
            startActivity(addTaskIntent);
        }
    }

}

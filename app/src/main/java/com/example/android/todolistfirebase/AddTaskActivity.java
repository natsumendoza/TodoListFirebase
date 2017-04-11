package com.example.android.todolistfirebase;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.todolistfirebase.model.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddTaskActivity extends AppCompatActivity {

    Button mAddButton;
    EditText mEditTextDesc;
    Spinner mSpinner;

    DatabaseReference databaseTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        mAddButton = (Button) findViewById(R.id.addButton);
        mEditTextDesc = (EditText) findViewById(R.id.editTextTaskDescription);
        mSpinner = (Spinner) findViewById(R.id.spinner);

        databaseTasks = FirebaseDatabase.getInstance().getReference("tasks");

        // Fill spinner with values
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.priorities_array, android.R.layout.simple_spinner_item);
        mSpinner.setAdapter(adapter);

        mAddButton.setOnClickListener(new AddButtonClickListener(this));
    }

    public void addTask(Context context) {

        String description = mEditTextDesc.getText().toString().trim();
        int priority = Integer.parseInt(mSpinner.getSelectedItem().toString());

        if (!TextUtils.isEmpty(description)) {

            String id = databaseTasks.push().getKey();

            Task task = new Task(id, description, priority);
            databaseTasks.child(id).setValue(task);

            Toast.makeText(context, "Task Added", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(context, "Please type the description of task", Toast.LENGTH_LONG).show();
        }

    }

    class AddButtonClickListener implements View.OnClickListener {

        private Context context;

        public AddButtonClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {

            addTask(context);
            finish();
        }

    }

}

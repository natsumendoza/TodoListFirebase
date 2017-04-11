package com.example.android.todolistfirebase.model;


import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Jay-Ar Gabriel on 4/10/2017.
 */

public class Task implements Serializable{

    private String taskId;
    private String description;
    private int priority;

    public Task () {

    }

    public Task(String taskId, String description, int priority) {
        this.taskId = taskId;
        this.description = description;
        this.priority = priority;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }
}

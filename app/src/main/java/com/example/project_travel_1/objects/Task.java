package com.example.project_travel_1.objects;

import java.io.Serializable;

public class Task implements Serializable {
    String task;
    boolean done;

    public Task(String task, boolean done) {
        this.task = task;
        this.done = done;
    }
    public Task()
    {}


    public void setTask(String task) {
        this.task = task;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String getTask() {
        return task;
    }

    public boolean isDone() {
        return done;
    }
}

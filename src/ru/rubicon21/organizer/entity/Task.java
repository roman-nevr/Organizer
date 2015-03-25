package ru.rubicon21.organizer.entity;

/**
 * Created by roma on 15.03.2015.
 */
public class Task {

    int id;
    String taskName;
    String taskDescription;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public Task(int _id, String _name, String _description) {
        id = _id;
        taskName = _name;
        taskDescription = _description;
    }
}
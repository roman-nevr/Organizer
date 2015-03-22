package ru.rubicon21.organizer.entity;

/**
 * Created by roma on 15.03.2015.
 */
public class Task {

    public String taskName;
    public String taskDescription;

    public Task(String _name, String _description) {
        taskName = _name;
        taskDescription = _description;
    }
}
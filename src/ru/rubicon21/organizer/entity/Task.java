package ru.rubicon21.organizer.entity;

/**
 * Created by roma on 15.03.2015.
 */
public class Task {

    /*
    PK
    |taskId|parent_id|name|description|has_children|
    */

    int taskId;
    int parentId;
    String taskName;
    String taskDescription;
    int hasChildren;

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
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

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(int hasChildren) {
        this.hasChildren = hasChildren;
    }

    public Task (String _name, String _description){
        taskName = _name;
        taskDescription = _description;
    }

    public Task (int _id, String _name, String _description) {
        taskId = _id;
        taskName = _name;
        taskDescription = _description;
    }

    public Task (int _id, int _parentID, String _name, String _description){
        taskId = _id;
        parentId = _parentID;
        taskName = _name;
        taskDescription = _description;
    }
}
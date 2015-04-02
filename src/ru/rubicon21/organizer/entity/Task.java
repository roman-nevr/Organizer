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
    boolean done;
    int priority;

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

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
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

    public void doneToggle(){
        if (this.isDone()){
            this.setDone(false);
        }else {
            this.setDone(true);
        }
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskId=" + taskId +
                ", parentId=" + parentId +
                ", taskName='" + taskName + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", done=" + done +
                '}';
    }
}
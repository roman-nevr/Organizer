package ru.rubicon21.organizer.DAO;

import ru.rubicon21.organizer.entity.Task;

import java.util.ArrayList;

/**
 * Created by roma on 22.03.2015.
 */
public class GetData {
    ArrayList<Task> tasks;
    public ArrayList<Task> getTasks(){
        tasks = new ArrayList<Task>();
        tasks.add(new Task("Тест","Пробная запись"));
        tasks.add(new Task("Тест 2","Вторая Пробная запись"));
        return tasks;
    }
}

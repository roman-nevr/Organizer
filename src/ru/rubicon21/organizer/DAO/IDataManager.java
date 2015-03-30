package ru.rubicon21.organizer.DAO;

import android.content.Context;
import ru.rubicon21.organizer.entity.Task;

import java.util.ArrayList;

/**
 * Created by roma on 30.03.2015.
 */
public interface IDataManager {

    public ArrayList<Task> getTasks(Context context, int _parent_id);

}

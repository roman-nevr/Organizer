package ru.rubicon21.organizer.adapter;

/**
 * Created by roma on 15.03.2015.
 */

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.CompoundButton.OnCheckedChangeListener;
import ru.rubicon21.organizer.R;
import ru.rubicon21.organizer.entity.Task;

import java.util.ArrayList;

public class MainWindowAdapter extends BaseAdapter {

    static final int COLOR_DONE = 0x999999;
    static final int COLOR_UNDONE = 0xFFFFFF;

    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Task> objects;

    public MainWindowAdapter (Context context, ArrayList<Task> tasks){
        ctx = context;
        objects = tasks;
        lInflater = (LayoutInflater) ctx.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if (view == null){
            view = lInflater.inflate(R.layout.item, viewGroup, false);
        }

        Task task = (Task) getItem(position);

        TextView tvTaskName = (TextView) view.findViewById(R.id.tvTaskName);
        tvTaskName.setText(task.getTaskName());
        TextView tvTaskDescription = (TextView) view.findViewById(R.id.tvTaskDescription);
        tvTaskDescription.setText(task.getTaskDescription());

        if (task.isDone()){

            tvTaskName.setTextColor(Color.parseColor("#777777"));
            tvTaskDescription.setTextColor(COLOR_DONE);
            tvTaskName.setPaintFlags(tvTaskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else {
            tvTaskName.setTextColor(Color.parseColor("#bbbbbb"));
            tvTaskDescription.setTextColor(COLOR_UNDONE);
            tvTaskName.setPaintFlags(tvTaskName.getPaintFlags() &
                    (Integer.MAX_VALUE - Paint.STRIKE_THRU_TEXT_FLAG));
        }
        return view;
    }

    public  void refreshDataSet(ArrayList<Task> newTasks){
        objects = newTasks;
        notifyDataSetChanged();
    }

}
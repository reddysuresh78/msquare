package com.ri.msquare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class TaskDetailsAdapter extends ArrayAdapter<Task> {

    // View lookup cache
    private static class ViewHolder {

        TextView startDate;
        TextView endDate;
        TextView totalHours;

     }

    public TaskDetailsAdapter(Context context, List<Task> words) {
        super(context, R.layout.task_details_list_item , words);

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.task_details_list_item, parent, false);


            viewHolder.startDate = (TextView) convertView.findViewById(R.id.tvStartDate_detail);
            viewHolder.endDate = (TextView) convertView.findViewById(R.id.tvEndDate_detail);
            viewHolder.totalHours = (TextView) convertView.findViewById(R.id.tvTotalMinutes_detail);

            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        Task task = null;
        if(position ==0 ) {
            task = new Task().setStartDate("Start Time      ").setEndDate("End Time         ").setTotalHours("Duration");
        }else {
            // Get the data item for this position
            task = getItem(position - 1);
        }
        viewHolder.startDate.setText(task.getStartDate());
        viewHolder.endDate.setText(task.getEndDate());
        viewHolder.totalHours.setText("" + task.getTotalHours());

        return convertView;
    }

}
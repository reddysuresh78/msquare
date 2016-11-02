package com.ri.msquare;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class TasksAdapter extends ArrayAdapter<Task> {
    private boolean mReadOnlyMode = true;

    // View lookup cache
    public static class ViewHolder {

        TextView name;
        TextView status;
        TextView startDate;
        TextView endDate;
        TextView totalHours;
        TextView currentHours;

        TextView currentStatus;

        TextView border;

        ImageButton modifyTaskStatus;
        ImageButton completeTask;

        CardView cardView;
     }

    public TasksAdapter(Context context, List<Task> words) {
        super(context, R.layout.task_list_item, words);

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Task task = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.task_list_item, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.tvName);
            viewHolder.status = (TextView) convertView.findViewById(R.id.tvStatus);
            viewHolder.startDate = (TextView) convertView.findViewById(R.id.tvStartDate);
            viewHolder.endDate = (TextView) convertView.findViewById(R.id.tvEndDate);
            viewHolder.totalHours = (TextView) convertView.findViewById(R.id.tvTotalHours);
            viewHolder.currentHours = (TextView) convertView.findViewById(R.id.tvCurrentHours);
            viewHolder.currentStatus= (TextView) convertView.findViewById(R.id.tvCurrentStatus);

            viewHolder.cardView = (CardView) convertView.findViewById(R.id.card_view);
            viewHolder.border  = (TextView) convertView.findViewById(R.id.tvBorder);

            viewHolder.modifyTaskStatus = (ImageButton) convertView.findViewById(R.id.ibModifyTaskStatus);
            viewHolder.completeTask = (ImageButton) convertView.findViewById(R.id.ibCompleteTask);

            viewHolder.modifyTaskStatus.setTag(viewHolder);
            viewHolder.completeTask.setTag(viewHolder);

             // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        viewHolder.name.setText(task.getName());
        viewHolder.status.setText(task.getStatus());

        viewHolder.startDate.setText(task.getStartDate());
        viewHolder.endDate.setText(task.getEndDate());
        viewHolder.totalHours.setText(""+ task.getTotalHours());
        viewHolder.currentHours.setText(""+ task.getCurrentHours());

        if(task.getStatus().equalsIgnoreCase("New")) {
            viewHolder.currentStatus.setText("Not Started");
        }else{
            viewHolder.currentStatus.setText("Paused");
        }

//        viewHolder.border.setHeight(viewHolder.cardView.getHeight());

        if(task.getStatus().equalsIgnoreCase("WIP")) {
            viewHolder.border.setBackgroundColor(Color.GREEN);
        }else{
            viewHolder.border.setBackgroundColor(Color.RED);
        }

        //TODO show the proper icon for task.
//        viewHolder.deleteWord.setVisibility(mReadOnlyMode ?View.GONE: View.VISIBLE);
//        viewHolder.editWord.setVisibility(mReadOnlyMode ?View.GONE: View.VISIBLE);

//        viewHolder.playSound.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(textToSpeech.isSpeaking()) {
//                    textToSpeech.stop();
//                }
//                String toSpeak = word.getWord();
//                textToSpeech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
//            }
//        });


//        viewHolder.image.setImageResource(word.getImageResourceId());

        return convertView;
    }
    public void setReadOnlyMode(boolean readOnlyMode) {
        this.mReadOnlyMode = readOnlyMode;
    }
}
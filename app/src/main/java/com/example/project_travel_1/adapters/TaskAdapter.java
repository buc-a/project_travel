package com.example.project_travel_1.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_travel_1.Interfaces.RecyclerViewInterface;
import com.example.project_travel_1.R;
import com.example.project_travel_1.objects.Task;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private final RecyclerViewInterface recyclerViewInterface;
    View view;
    private final List<Task> list;

    public TaskAdapter(Context context, RecyclerViewInterface recyclerViewInterface, List<Task> list) {
        this.inflater = LayoutInflater.from(context);
        this.recyclerViewInterface = recyclerViewInterface;
        this.list = list;
    }

    @NonNull
    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = inflater.inflate(R.layout.item_note, parent, false);
        return new TaskAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.ViewHolder holder, int position) {
        String task = list.get(position).getTask();
        holder.textView.setText(task);
        if ( list.get(position).isDone())
        {
            holder.imageView.setImageResource(R.drawable.check_mark_icon);
        }
        else
            holder.imageView.setImageResource(R.drawable.for_mark);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv_task);
            imageView = (ImageView) itemView.findViewById(R.id.image_to_do);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ( recyclerViewInterface != null)
                    {
                        int pos = getAdapterPosition();
                        if ( pos != RecyclerView.NO_POSITION)
                        {
                            imageView.setImageResource(R.drawable.check_mark_icon);
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });


        }
    }
}

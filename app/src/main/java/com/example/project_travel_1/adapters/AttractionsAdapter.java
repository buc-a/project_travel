package com.example.project_travel_1.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_travel_1.Interfaces.AttractionsInterface;
import com.example.project_travel_1.R;
import com.example.project_travel_1.objects.Attraction;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AttractionsAdapter extends RecyclerView.Adapter<AttractionsAdapter.ViewHolder>{

    private final AttractionsInterface attractionsInterface;
    private LayoutInflater inflater;
    private List<Attraction> attractions;

    public AttractionsAdapter(Context context, List<Attraction> attractions, AttractionsInterface attractionsInterface) {
        this.attractions = attractions;
        this.attractionsInterface = attractionsInterface;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public AttractionsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_attraction, parent,false);


        return new ViewHolder(view, attractionsInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull AttractionsAdapter.ViewHolder holder, int position) {
        Attraction attraction = attractions.get(position);

        if ( attraction.getImageURL().equals("null"))
        {
            holder.image_attraction.setImageResource(R.drawable.no_photo);
        }
        else {
            Picasso.get().load(attraction.getImageURL()).into(holder.image_attraction);
        }

        holder.btn.setText("выбрать");
        holder.tv_name_attraction.setText(attraction.getName_place());

    }

    @Override
    public int getItemCount() {
        return attractions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        final TextView tv_name_attraction;
        final ImageView image_attraction;
        final Button btn;


        public ViewHolder(View view, AttractionsInterface attractionsInterface) {
            super(view);

            image_attraction = view.findViewById(R.id.image_attraction);
            tv_name_attraction = view.findViewById(R.id.tv_attraction);
            btn = view.findViewById(R.id.btn_setAttraction);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (attractionsInterface != null )
                    {
                        btn.setVisibility(View.GONE);
                        int pos = getAdapterPosition();
                        if ( pos != RecyclerView.NO_POSITION)
                            attractionsInterface.likeAttraction(pos);
                    }
                }
            });


        }
    }
}

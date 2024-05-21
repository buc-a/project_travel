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
import com.example.project_travel_1.objects.Memory;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MemoryAdapter extends RecyclerView.Adapter<MemoryAdapter.ViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;
    private LayoutInflater inflater;
    private List<Memory> memory_list;

    public List<Memory> getData()
    {
        return memory_list;
    }

    public MemoryAdapter (Context context, List<Memory> memory_list, RecyclerViewInterface recyclerViewInterface)
    {
        this.memory_list = memory_list;

        this.recyclerViewInterface = recyclerViewInterface;

        this.inflater = LayoutInflater.from(context);
    }

    //возвращает view holder (проинициализировали list_memory)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item_memory, parent,false);
        return new ViewHolder(view, recyclerViewInterface);
    }


    //основной метод, наполняет текст вью  и тд
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Memory memory = memory_list.get(position);


        Picasso.get().load(memory.getPhoto_uri()).into(holder.image_place);
        holder.tv_place.setText(memory.getName());

    }


    //кол-во значений
    @Override
    public int getItemCount() {
        return memory_list.size();
    }

    //здесь инициализируются все поля, а заполняются они в адаптере
    public static class ViewHolder extends RecyclerView.ViewHolder{

        final TextView tv_place;
        final ImageView image_place;
        public ViewHolder(View view, RecyclerViewInterface recyclerViewInterface) {
            super(view);

            image_place = view.findViewById(R.id.title_photo);

            tv_place =  view.findViewById(R.id.tv_title_place);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recyclerViewInterface != null)
                    {
                        int pos = getAdapterPosition();

                        if ( pos != RecyclerView.NO_POSITION)
                        {
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });

        }
    }
}

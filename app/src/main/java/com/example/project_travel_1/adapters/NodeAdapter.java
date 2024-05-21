package com.example.project_travel_1.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_travel_1.Interfaces.RecyclerViewInterface;
import com.example.project_travel_1.R;
import com.example.project_travel_1.objects.Node;

import java.util.List;

public class NodeAdapter extends RecyclerView.Adapter<NodeAdapter.ViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;
    private LayoutInflater inflater;
    private List<Node> nodes;

    public NodeAdapter(Context context, List<Node> nodes, RecyclerViewInterface recyclerViewInterface) {
        this.recyclerViewInterface = recyclerViewInterface;
        this.nodes = nodes;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public NodeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_kart, parent, false);

        return new ViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull NodeAdapter.ViewHolder holder, int position) {
        Node node = nodes.get(position);

        holder.tv_1.setText(node.getTicket().getDestination());
        holder.tv_2.setText("Поездка "+node.getTicket().getData_to());
    }

    @Override
    public int getItemCount() {
        return nodes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        final TextView tv_1;
        final TextView tv_2;
        public ViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            tv_1 = (TextView) itemView.findViewById(R.id.tv_kart1);
            tv_2 = (TextView) itemView.findViewById(R.id.tv_kart2);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ( recyclerViewInterface != null)
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

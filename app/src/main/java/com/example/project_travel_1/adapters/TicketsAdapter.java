package com.example.project_travel_1.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_travel_1.Interfaces.TicketsInterface;
import com.example.project_travel_1.R;
import com.example.project_travel_1.objects.Ticket;

import java.util.List;

public class TicketsAdapter extends RecyclerView.Adapter<TicketsAdapter.ViewHolder> {

    private final TicketsInterface ticketsInterface;
    private final LayoutInflater inflater;
    private final List<Ticket> ticketList;

    public TicketsAdapter (TicketsInterface ticketsInterface, Context context, List<Ticket> ticketList)
    {
        this.ticketsInterface = ticketsInterface;
        this.ticketList = ticketList;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public TicketsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_ticket, parent,false);
        return new TicketsAdapter.ViewHolder(view, ticketsInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketsAdapter.ViewHolder holder, int position) {
        Ticket ticket = ticketList.get(position);


        holder.tv_price.setText(ticket.getPrice().toString());
        holder.tv_duration_to.setText(ticket.getDuration_to().toString() + " мин");
        holder.tv_duration_back.setText(ticket.getDuration_back().toString() + " мин");
        holder.tv_airline.setText(ticket.getAirline());
        holder.tv_departure_at.setText(ticket.getDeparture_at());
        holder.tv_return_at.setText(ticket.getReturn_at());

    }

    @Override
    public int getItemCount() {
        return ticketList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_price, tv_duration_to, tv_duration_back, tv_airline, tv_departure_at, tv_return_at;
        Button btn_choose_ticket;

        public ViewHolder(View view, TicketsInterface ticketsInterface) {
            super(view);

            tv_price = (TextView) view.findViewById(R.id.tv_price);
            tv_duration_to = (TextView) view.findViewById(R.id.tv_duration_to);
            tv_duration_back = (TextView) view.findViewById(R.id.tv_duration_back);
            tv_airline = (TextView) view.findViewById(R.id.tv_airline);
            tv_departure_at = (TextView) view.findViewById(R.id.tv_departure_at);
            tv_return_at = (TextView) view.findViewById(R.id.tv_return_at);
            btn_choose_ticket = (Button) view.findViewById(R.id.btn_choose_ticket);

            btn_choose_ticket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ticketsInterface != null )
                    {
                        int pos = getAdapterPosition();
                        if ( pos != RecyclerView.NO_POSITION)
                            ticketsInterface.choose(pos);
                    }
                }
            });

    }
    }
}

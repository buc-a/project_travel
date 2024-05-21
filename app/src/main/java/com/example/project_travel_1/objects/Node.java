package com.example.project_travel_1.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Node implements Serializable {
    List<Task> toDo = new ArrayList<>();
    Ticket ticket;
    List<Attraction> attractions = new ArrayList<>();

    public List<Task> getToDo() {
        return toDo;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public Node(List<Task> toDo, Ticket ticket, List<Attraction> attractions){
        this.toDo = toDo;
        this.ticket = ticket;
        this.attractions = attractions;
    }

    public List<Attraction> getAttractions() {
        return attractions;
    }

    public Node()
    {

    }

}

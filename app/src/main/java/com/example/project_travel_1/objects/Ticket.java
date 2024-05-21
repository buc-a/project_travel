package com.example.project_travel_1.objects;


import java.io.Serializable;
import java.util.List;

public class Ticket implements Serializable {
    String origin, destination, origin_airport, destination_airport, airline, departure_at, return_at, data_to, data_from;
    Integer price, duration_to, duration_back;

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public Ticket()
    {

    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setOrigin_airport(String origin_airport) {
        this.origin_airport = origin_airport;
    }

    public void setDestination_airport(String destination_airport) {
        this.destination_airport = destination_airport;
    }


    public void setFlight_number(String airline) {
        this.airline = airline;
    }

    public void setDeparture_at(String departure_at) {
        this.departure_at = departure_at;
    }

    public void setReturn_at(String return_at) {
        this.return_at = return_at;
    }


    public void setPrice(Integer price) {
        this.price = price;
    }



    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public String getOrigin_airport() {
        return origin_airport;
    }

    public String getDestination_airport() {
        return destination_airport;
    }


    public String getAirline() {
        return airline;
    }

    public String getDeparture_at() {
        return departure_at;
    }

    public String getReturn_at() {
        return return_at;
    }


    public Integer getPrice() {
        return price;
    }

    public Integer getDuration_to() {
        return duration_to;
    }

    public Integer getDuration_back() {
        return duration_back;
    }

    public void setDuration_to(Integer duration_to) {
        this.duration_to = duration_to;
    }

    public void setDuration_back(Integer duration_back) {
        this.duration_back = duration_back;
    }

    public String getData_to() {
        return data_to;
    }

    public String getData_from() {
        return data_from;
    }

    public Ticket(String origin, String destination, String origin_airport, String destination_airport, String airline, String departure_at, String return_at, Integer price, Integer duration_to, Integer duration_back, String data_to, String data_from) {
        this.origin = origin;
        this.destination = destination;
        this.origin_airport = origin_airport;
        this.destination_airport = destination_airport;
        this.airline = airline;
        this.departure_at = departure_at;
        this.return_at = return_at;
        this.price = price;
        this.duration_to = duration_to;
        this.duration_back = duration_back;
        this.data_to = data_to;
        this.data_from = data_from;


    }
}

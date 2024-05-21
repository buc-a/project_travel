package com.example.project_travel_1.objects;

public class Point {
    public Point(String iata, String name) {
        this.iata = iata;
        this.name = name;
    }

    String iata, name;

    public void setIata(String iata) {
        this.iata = iata;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIata() {
        return iata;
    }

    public String getName() {
        return name;
    }
}

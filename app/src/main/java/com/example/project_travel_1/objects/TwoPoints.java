package com.example.project_travel_1.objects;

import com.example.project_travel_1.objects.Point;

public class TwoPoints {
    public Point getOrigin() {
        return origin;
    }

    public TwoPoints(Point origin, Point destination) {
        this.origin = origin;
        this.destination = destination;
    }

    public void setOrigin(Point origin) {
        this.origin = origin;
    }

    public void setDestination(Point destination) {
        this.destination = destination;
    }

    public Point getDestination() {
        return destination;
    }

    Point origin, destination;
}

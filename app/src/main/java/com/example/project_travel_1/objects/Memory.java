package com.example.project_travel_1.objects;

import java.io.Serializable;

public class Memory  implements Serializable {
    private String place, description, photo_uri, key;

    public void setName(String name) {
        this.place = place;
    }

    public Memory()
    {

    }

    public String getPhoto_uri() {
        return photo_uri;
    }
    public void setKey(String key)
    {
        this.key = key;
    }

    public void setPhoto_uri(String photo_uri) {
        this.photo_uri = photo_uri;
    }

    public Memory(String place, String description, String photo_uri, String key) {
        this.place = place;
        this.photo_uri = photo_uri;
        this.description = description;
        this.key = key;
    }

    public Memory(String place, String description, String photo_uri) {
        this.place = place;
        this.photo_uri = photo_uri;
        this.description = description;
        this.key = key;
    }

    public String getKey()
    {
        return key;
    }


    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return place;
    }


    public String getDescription() {
        return description;
    }
}

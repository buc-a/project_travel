package com.example.project_travel_1.objects;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Attraction implements Parcelable {
    String imageURL, name_place;
    public Attraction()
    {

    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setName_place(String name_place) {
        this.name_place = name_place;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getName_place() {
        return name_place;
    }

    public Attraction(Parcel in)
    {
        String[] data = new String[2];
        in.readStringArray(data);
        imageURL = data[0];
        name_place = data[1];
    }
    public Attraction(String imageURL, String name_place) {
        this.imageURL = imageURL;
        this.name_place = name_place;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeStringArray(new String[] {imageURL, name_place});
    }
    public static final Parcelable.Creator<Attraction> CREATOR = new Parcelable.Creator<Attraction>(){

        @Override
        public Attraction createFromParcel(Parcel source) {
            return new Attraction(source);
        }

        @Override
        public Attraction[] newArray(int size) {
            return new Attraction[size];
        }
    };
}

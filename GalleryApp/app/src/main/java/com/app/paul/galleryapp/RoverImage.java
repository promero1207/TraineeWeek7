package com.app.paul.galleryapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class RoverImage {
    private List<Photos> photos;

    public RoverImage(List<Photos> photos) {
        this.photos = photos;
    }

    public List<Photos> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photos> photos) {
        this.photos = photos;
    }
}

class Photos implements Parcelable {
    private String img_src;
    public Rover rover;

    public Photos(String img_src, Rover rover) {
        this.img_src = img_src;
        this.rover = rover;
    }

    public String getImg_src() {
        return img_src;
    }

    public void setImg_src(String img_src) {
        this.img_src = img_src;
    }

    public Rover getRover() {
        return rover;
    }

    public void setRover(Rover rover) {
        this.rover = rover;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.img_src);
        dest.writeParcelable(this.rover, flags);
    }

    private Photos(Parcel in) {
        this.img_src = in.readString();
        this.rover = in.readParcelable(Rover.class.getClassLoader());
    }

    public static final Parcelable.Creator<Photos> CREATOR = new Parcelable.Creator<Photos>() {
        @Override
        public Photos createFromParcel(Parcel source) {
            return new Photos(source);
        }

        @Override
        public Photos[] newArray(int size) {
            return new Photos[size];
        }
    };


}

class Rover implements Parcelable {
    private String name;
    private String landing_date;
    private String launch_date;

    public Rover(String landing_date, String launch_date,String name) {
        this.landing_date = landing_date;
        this.launch_date = launch_date;
        this.name = name;
    }

    public String getLanding_date() {
        return landing_date;
    }

    public void setLanding_date(String landing_date) {
        this.landing_date = landing_date;
    }

    public String getLaunch_date() {
        return launch_date;
    }

    public void setLaunch_date(String launch_date) {
        this.launch_date = launch_date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.landing_date);
        dest.writeString(this.launch_date);
    }

    public Rover(Parcel in) {
        this.name = in.readString();
        this.landing_date = in.readString();
        this.launch_date = in.readString();
    }

    public static final Creator<Rover> CREATOR = new Creator<Rover>() {
        @Override
        public Rover createFromParcel(Parcel source) {
            return new Rover(source);
        }

        @Override
        public Rover[] newArray(int size) {
            return new Rover[size];
        }
    };
}




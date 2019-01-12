package com.cqut.faymong.betsoftware.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class shedule implements Parcelable {
    public String shedule_date;
    public String shedule_time;
    public String shedule_vs;

    public shedule() {
    }

    protected shedule(Parcel in){
        shedule_date = in.readString();
        shedule_time = in.readString();
        shedule_vs = in.readString();
    }


    public String getShedule_date() {
        return shedule_date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(shedule_date);
        dest.writeString(shedule_time);
        dest.writeString(shedule_vs);
    }

    public void setShedule_date(String shedule_date) {
        this.shedule_date = shedule_date;
    }

    public String getShedule_time() {
        return shedule_time;
    }

    public void setShedule_time(String shedule_time) {
        this.shedule_time = shedule_time;
    }

    public String getShedule_vs() {
        return shedule_vs;
    }

    public void setShedule_vs(String shedule_vs) {
        this.shedule_vs = shedule_vs;
    }

    public static final Creator<shedule> CREATOR = new Creator<shedule>() {
        @Override
        public shedule createFromParcel(Parcel in) {
            return new shedule(in);
        }

        @Override
        public shedule[] newArray(int size) {
            return new shedule[size];
        }
    };
}

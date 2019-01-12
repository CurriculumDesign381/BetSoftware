package com.cqut.faymong.betsoftware.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Record implements Parcelable{
    public String name;
    public String message;
    public long time;
    public int avatar;
    public String score;
    public  String hometeam;
    public String awayteam;
    public String evenid;
    public String domain;
    public String betaccount;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getBetaccount() {
        return betaccount;
    }

    public void setBetaccount(String betaccount) {
        this.betaccount = betaccount;
    }

    public Record() {
    }

    protected Record(Parcel in) {
        name = in.readString();
        message = in.readString();
        time = in.readLong();
        avatar = in.readInt();
        score = in.readString();
        hometeam = in.readString();
        awayteam = in.readString();
        evenid = in.readString();
        domain = in.readString();
        betaccount = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(message);
        dest.writeLong(time);
        dest.writeInt(avatar);
        dest.writeString(score);
        dest.writeString(hometeam);
        dest.writeString(awayteam);
        dest.writeString(evenid);
        dest.writeString(domain);
        dest.writeString(betaccount);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Record> CREATOR = new Parcelable.Creator<Record>() {
        @Override
        public Record createFromParcel(Parcel in) {
            return new Record(in);
        }

        @Override
        public Record[] newArray(int size) {
            return new Record[size];
        }
    };
}

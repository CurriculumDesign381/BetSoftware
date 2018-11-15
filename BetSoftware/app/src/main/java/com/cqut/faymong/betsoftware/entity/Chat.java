package com.cqut.faymong.betsoftware.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by YoKeyword on 16/6/30.
 */
public class Chat implements Parcelable {
    public String name;
    public String message;
    public long time;
    public int avatar;
    public String score;

    public Chat() {
    }

    protected Chat(Parcel in) {
        name = in.readString();
        message = in.readString();
        time = in.readLong();
        avatar = in.readInt();
        score = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(message);
        dest.writeLong(time);
        dest.writeInt(avatar);
        dest.writeString(score);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Chat> CREATOR = new Creator<Chat>() {
        @Override
        public Chat createFromParcel(Parcel in) {
            return new Chat(in);
        }

        @Override
        public Chat[] newArray(int size) {
            return new Chat[size];
        }
    };
}
package com.jyt.enterpriseinspect.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chenweiqi on 2017/3/2.
 */

public class Person implements Parcelable{
    public String userid;//402881ea5a8eb68f015a8ecdfc9b001e
    public String username;//郭经理
    public String user_id;
    public String user_name;
    public String realname;
    public Person() {
    }


    protected Person(Parcel in) {
        userid = in.readString();
        username = in.readString();
        user_id = in.readString();
        user_name = in.readString();
        realname = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userid);
        dest.writeString(username);
        dest.writeString(user_id);
        dest.writeString(user_name);
        dest.writeString(realname);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Person> CREATOR = new Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };
}

package com.jyt.enterpriseinspect.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chenweiqi on 2017/1/10.
 */
public class Type implements Parcelable {
    public String patrol_typename;
    public String content_id;
    public String patrol_bigtype;
    public String type_id;
    public boolean isChecked;

    public Type() {
    }

    protected Type(Parcel in) {
        patrol_typename = in.readString();
        content_id = in.readString();
        patrol_bigtype = in.readString();
        isChecked = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(patrol_typename);
        dest.writeString(content_id);
        dest.writeString(patrol_bigtype);
        dest.writeByte((byte) (isChecked ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Type> CREATOR = new Creator<Type>() {
        @Override
        public Type createFromParcel(Parcel in) {
            return new Type(in);
        }

        @Override
        public Type[] newArray(int size) {
            return new Type[size];
        }
    };
}

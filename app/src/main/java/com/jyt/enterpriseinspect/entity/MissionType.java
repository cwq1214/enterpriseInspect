package com.jyt.enterpriseinspect.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenweiqi on 2017/1/10.
 */

public class MissionType implements Parcelable{
    public List<Type> types ;
    public String type_name;
    public boolean isChecked;
    public String id;
    public String father_id;
    public List<MissionType> childrenMissionType = new ArrayList<>();


    protected MissionType(Parcel in) {
        types = in.createTypedArrayList(Type.CREATOR);
        type_name = in.readString();
        isChecked = in.readByte() != 0;
        id = in.readString();
        father_id = in.readString();
        childrenMissionType = in.createTypedArrayList(MissionType.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(types);
        dest.writeString(type_name);
        dest.writeByte((byte) (isChecked ? 1 : 0));
        dest.writeString(id);
        dest.writeString(father_id);
        dest.writeTypedList(childrenMissionType);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MissionType> CREATOR = new Creator<MissionType>() {
        @Override
        public MissionType createFromParcel(Parcel in) {
            return new MissionType(in);
        }

        @Override
        public MissionType[] newArray(int size) {
            return new MissionType[size];
        }
    };
}

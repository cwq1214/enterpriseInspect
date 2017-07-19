package com.jyt.enterpriseinspect.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by chenweiqi on 2017/1/10.
 */

public class Mission implements Parcelable{
    public String patrol_id=" ";
    public String patrol_type=" ";
    public String patrol_date="";
    public String company_village=" ";//安光村
    public String companyname =" ";
    public String company_name =" ";
    public String company_code=" ";
    public List<Type> contents;
    public String legal_person=" ";//法人
    public String legal_person_tel=" ";//法人电话
    public String register_address=" ";//地址
    public String response_person_tel=" ";//安全负责人电话
    public String response_person =" ";//安全负责人
    public String company_longitude;
    public String company_latitude;
    public String patrol_status = " ";

    public List<Person> persons;
    public List<Person> users;


    protected Mission(Parcel in) {
        patrol_id = in.readString();
        patrol_type = in.readString();
        patrol_date = in.readString();
        company_village = in.readString();
        companyname = in.readString();
        company_name = in.readString();
        company_code = in.readString();
        contents = in.createTypedArrayList(Type.CREATOR);
        legal_person = in.readString();
        legal_person_tel = in.readString();
        register_address = in.readString();
        response_person_tel = in.readString();
        response_person = in.readString();
        company_longitude = in.readString();
        company_latitude = in.readString();
        persons = in.createTypedArrayList(Person.CREATOR);
        users = in.createTypedArrayList(Person.CREATOR);
    }

    public static final Creator<Mission> CREATOR = new Creator<Mission>() {
        @Override
        public Mission createFromParcel(Parcel in) {
            return new Mission(in);
        }

        @Override
        public Mission[] newArray(int size) {
            return new Mission[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(patrol_id);
        dest.writeString(patrol_type);
        dest.writeString(patrol_date);
        dest.writeString(company_village);
        dest.writeString(companyname);
        dest.writeString(company_name);
        dest.writeString(company_code);
        dest.writeTypedList(contents);
        dest.writeString(legal_person);
        dest.writeString(legal_person_tel);
        dest.writeString(register_address);
        dest.writeString(response_person_tel);
        dest.writeString(response_person);
        dest.writeString(company_longitude);
        dest.writeString(company_latitude);
        dest.writeTypedList(persons);
        dest.writeTypedList(users);
    }
}

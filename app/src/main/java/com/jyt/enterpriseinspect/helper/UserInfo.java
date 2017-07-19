package com.jyt.enterpriseinspect.helper;

import com.orhanobut.hawk.Hawk;

/**
 * Created by chenweiqi on 2017/3/1.
 */

public class UserInfo {
    private static String ACCOUNT = "ACCOUNT";
    private static String PASSWORD = "PASSWORD";
    private static String ID = "ID";
    private static String REALNAME = "REALNAME";


    public static void setAccount(String account){
        Hawk.put(ACCOUNT,account);
    }

    public static String getAccount(){
        return Hawk.get(ACCOUNT);
    }

    public static String getPassword(){
        return Hawk.get(PASSWORD);
    }

    public static void setPassword(String password){
        Hawk.put(PASSWORD,password);
    }


    public static String getID() {
        return Hawk.get(ID);
    }

    public static void setID(String id) {
        Hawk.put(ID,id);
    }

    public static String getREALNAME() {
        return Hawk.get(REALNAME);
    }

    public static void setREALNAME(String realname) {
        Hawk.put(REALNAME,realname);
    }
}

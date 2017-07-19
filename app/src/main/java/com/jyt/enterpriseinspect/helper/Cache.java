package com.jyt.enterpriseinspect.helper;

import com.jyt.enterpriseinspect.entity.Content;
import com.jyt.enterpriseinspect.entity.Mission;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenweiqi on 2017/3/2.
 */

public class Cache {
    private static String MISSION_CONTENT="missionContent";
    private static String MISSION = "mission";
    private static String MISSION_LIST = "MISSION_LIST";


    public static void setMissionContent(String contentId, Content content){
        Hawk.put(UserInfo.getAccount()+"_"+MISSION_CONTENT+"_"+contentId,content);
    }

    public static Content getMissionContent(String contentId){
        return Hawk.get(UserInfo.getAccount()+"_"+MISSION_CONTENT+"_"+contentId);
    }

    public static void addMission(String  patrol_id_and_type_id){
        ArrayList<String> missions= Hawk.get(UserInfo.getAccount()+"_"+MISSION_LIST,new ArrayList<String>());
        int size = missions.size();
        for (int i=0;i<size;i++){
            if (missions.get(i).equals(patrol_id_and_type_id)){
                missions.remove(i);
                break;
            }
        }
        missions.add(0,patrol_id_and_type_id);
        Hawk.put(UserInfo.getAccount()+"_"+MISSION_LIST,missions);
    }

    public static void removeMission(){

    }


    public static ArrayList<Mission> getMissionList(){
        return Hawk.get(UserInfo.getAccount()+"_"+MISSION_LIST);
    }
}

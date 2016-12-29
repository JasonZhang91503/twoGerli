package com.mobileclass.handsomeboy.myapplication;

import java.util.ArrayList;

/**
 * Created by HandsomeBoy on 2016/12/7.
 */
public class SchedulePackage {
    public int[] idArr;
    public ArrayList<String> timeArr;
    public ArrayList<String> nameArr;

    public SchedulePackage(int[] id,ArrayList<String> time, ArrayList<String> name){
        idArr = id;
        timeArr = time;
        nameArr = name;
    }
}

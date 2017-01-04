package com.mobileclass.handsomeboy.myapplication;

import java.lang.ref.SoftReference;
import java.util.ArrayList;

/**
 * Created by HandsomeBoy on 2016/12/21.
 */

public class ClockPackage {

    public int[] idArr;
    public ArrayList<int[]> timeArr;
    public ArrayList<String> nameArr;
    public ArrayList<String> musicArr;
    public ArrayList<boolean[]> repeatListArr;
    public boolean[] shockArr;
    public boolean[] enableArr;

    public ClockPackage(int[] id,ArrayList<int[]> time,ArrayList<String> name
            ,ArrayList<String> music,ArrayList<boolean[]> repeatList,boolean[] shock,boolean[] enable){
        idArr = id;
        timeArr = time;
        nameArr = name;
        musicArr = music;
        repeatListArr = repeatList;
        shockArr = shock;
        enableArr = enable;
    }

}

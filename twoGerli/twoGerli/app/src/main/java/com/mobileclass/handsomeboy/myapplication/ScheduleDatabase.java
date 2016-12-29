package com.mobileclass.handsomeboy.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.PopupMenu;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by HandsomeBoy on 2016/12/7.
 */
public class ScheduleDatabase {
    private static final int VERSION = 1;
    private final String DatabaseName = "scheduleDatabase";

    private final String yearFilter = "substr(time,1,4)";   //年為單位的篩選
    private final String monthFilter = "substr(time,1,7)";  //月為單位的篩選
    private final String dayFilter = "substr(time,1,10)";   //日為單位的篩選

    //操作Database的內部成員
    private SQLiteDB sqLiteDB;
    private SQLiteDatabase db;

    public ScheduleDatabase(Context context){
        sqLiteDB = new SQLiteDB(context,DatabaseName,null,VERSION);
        db = sqLiteDB.getWritableDatabase();
    }


    //region getSchedule

    public SchedulePackage getScheduleByDay(int year, int month, int day){
        //取得當天資料表
        return getSchedule(getCursor_ScheduleByDay(CalendarManager.getDay(year,month,day)));
    }

    public SchedulePackage getScheduleByDay(int Syear, int Smonth, int Sday,int Eyear,int Emonth,int Eday){
        //取得從S到E天的資料表
        return getSchedule(getCursor_ScheduleByDay(
                CalendarManager.getDay(Syear,Smonth,Sday)
                ,CalendarManager.getDay(Eyear,Emonth,Eday)));
    }

    public SchedulePackage getScheduleByMonth(int year, int month){
        //取得當月資料表
        return getSchedule(getCursor_ScheduleByDay(CalendarManager.getMonth(year,month)));
    }

    public SchedulePackage getScheduleByYear(int year){
        //取得當年資料表
        return getSchedule(getCursor_ScheduleByDay(CalendarManager.getYear(year)));
    }

    public SchedulePackage getSchedule(Cursor cursor){
        //取得Cursor內部資料並包成SchedulePackage
        int row = cursor.getCount();
        int[] idArr = new int[row];
        ArrayList<String> timeArray = new ArrayList<String>();
        ArrayList<String> nameArray = new ArrayList<String>();
        cursor.moveToFirst();
        for(int i = 0; i < row; i++){
            int id = cursor.getInt(0);
            String time =  cursor.getString(1);
            String name =  cursor.getString(2);

            idArr[i] = id;
            timeArray.add(time);
            nameArray.add(name);
        }

        return new SchedulePackage(idArr,timeArray,nameArray);
    }

    //endregion

    //region insertSchedule

    public boolean insertSchedule(String Time,String Name){
        //防呆
        if(Time == null || Name == null){
            Log.d("DatabaseError","insertSchedule : Time or Name can't be null");
            return false;
        }

        //填入資料
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLiteDB.scd_Time,Time);
        contentValues.put(SQLiteDB.scd_Name,Name);

        //插入資料
        return db.insert(SQLiteDB.scheduleTable, null, contentValues) != -1;
    }

    //endregion

    //region updateSchedule

    public boolean updateSchedule(String time,String name,long id){
        ContentValues contentValues = new ContentValues();
        if(time != null){
            contentValues.put(SQLiteDB.scd_Time,time);
        }
        if(name != null){
            contentValues.put(SQLiteDB.scd_Name, name);
        }

        return update(Table.SCHEDULE,contentValues,id);
    }

    //endregion

    //region deleteSchedule

    public boolean deleteSchedule(long id){
        return delete(Table.SCHEDULE,id);
    }

    //endregion

    //region getClock

    public ClockPackage getClock(){
        return getClock(getCursor_Clock());
    }

    ClockPackage getClock(Cursor cursor){
        //取得Cursor內部資料並包成ClockPackage

        int row = cursor.getCount();
        cursor.moveToFirst();

        //宣告
        int[] idArr = new int[row];
        ArrayList<int[]> timeArr = new ArrayList<int[]>();
        ArrayList<String> nameArr = new ArrayList<String>();
        ArrayList<String> musicArr = new ArrayList<String>();
        ArrayList<boolean[]> repeatListArr = new ArrayList<boolean[]>();
        boolean[] shockArr = new boolean[row];
        boolean[] enableArr = new boolean[row];

        for(int i = 0; i < row;i++){

            //從cursor取欄位值
            int id = cursor.getInt(0);
            int[] time = CalendarManager.getClock(cursor.getString(1));
            String name = (cursor.isNull(2))? "":cursor.getString(2);
            String music = (cursor.isNull(3))? "":cursor.getString(3);
            boolean[] repeat = DecodeRepeatCode(cursor.getInt(4));
            boolean shock = (cursor.getInt(5) == 1);
            boolean enable = (cursor.getInt(6) == 1);

            //填入package欄位陣列
            idArr[i] = id;
            timeArr.add(time);
            nameArr.add(name);
            musicArr.add(music);
            repeatListArr.add(repeat);
            shockArr[i] = shock;
            enableArr[i] = enable;
        }
        return new ClockPackage(idArr,timeArr,nameArr,musicArr,repeatListArr,shockArr,enableArr);
    }

    //endregion

    //region insertClock

    public boolean insertClock(int[] Time,String Name,String Music,boolean[] RepeatList,boolean Shock,boolean Enable){
        //防呆
        if(Time == null){
            Log.d("DatabaseError","insertClock : Time can't be null");
            return false;
        }
        if( (Time[0] < 0 || 23 < Time[0] )||(Time[1] < 0 || 59 < Time[1])){
            Log.d("DatabaseError","insertClock : Time is not real");
            return false;
        }
        if(Time.length != 2){
            Log.d("DatabaseError","insertClock : Time's format incorrect");
            return false;
        }
        if(RepeatList != null){
            if(RepeatList.length != 7){
                Log.d("DatabaseError","insertClock : RepeatList's format incorrect");
                return false;
            }
        }

        //轉換儲存資料
        String con_Time,con_Name,con_Music;
        int con_Repeat,con_Shock,con_Enable;

        con_Time = CalendarManager.getClock(Time[0],Time[1]);
        con_Name = (Name == null) ? "" : Name;
        con_Music = (Music == null) ? "" : Music;
        con_Repeat = (RepeatList == null) ? 0 : EncodeRepeatCode(RepeatList);
        con_Shock = (Shock) ? 1 : 0;
        con_Enable = (Enable) ? 1 : 0;

        //填入資料
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLiteDB.alc_Time,con_Time);
        contentValues.put(SQLiteDB.alc_Name,con_Name);
        contentValues.put(SQLiteDB.alc_Music,con_Music);
        contentValues.put(SQLiteDB.alc_Repeat,con_Repeat);
        contentValues.put(SQLiteDB.alc_Shock,con_Shock);
        contentValues.put(SQLiteDB.alc_Enable,con_Enable);

        //插入資料
        return db.insert(SQLiteDB.alarmClockTable, null, contentValues) != -1;
    }

    //endregion

    //region updateClock

    boolean updateClock_shock(boolean shock,int id){
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLiteDB.alc_Shock,shock);

        return update(Table.ALARM_CLOCK,contentValues,id);
    }

    boolean updateClock_enable(boolean enable,int id){
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLiteDB.alc_Enable,enable);

        return update(Table.ALARM_CLOCK,contentValues,id);
    }

    boolean updateClock(int[] Time,String Name,String Music,boolean[] RepeatList,boolean Shock,boolean Enable,int id){
        //防呆
        if(Time != null){
            if( (Time[0] < 0 || 23 < Time[0] )||(Time[1] < 0 || 59 < Time[1])){
                Log.d("DatabaseError","insertClock : Time is not real");
                return false;
            }
            if(Time.length != 2){
                Log.d("DatabaseError","insertClock : Time's format incorrect");
                return false;
            }
        }
        if(RepeatList != null){
            if(RepeatList.length != 7){
                Log.d("DatabaseError","insertClock : RepeatList's format incorrect");
                return false;
            }
        }

        //填入資料
        ContentValues contentValues = new ContentValues();
        if(Time != null){
            String con_Time = CalendarManager.getClock(Time[0],Time[1]);
            contentValues.put(SQLiteDB.alc_Time,con_Time);
        }
        if(Name != null){
            contentValues.put(SQLiteDB.alc_Name,Name);
        }
        if(Music != null){
            contentValues.put(SQLiteDB.alc_Music,Music);
        }
        if(RepeatList != null){
            contentValues.put(SQLiteDB.alc_Repeat,EncodeRepeatCode(RepeatList));
        }
        contentValues.put(SQLiteDB.alc_Shock,Shock);
        contentValues.put(SQLiteDB.alc_Enable,Enable);


        //修改資料
        return update(Table.ALARM_CLOCK,contentValues,id);
    }

    //endregion

    //region deleteClock

    public boolean deleteClock(int id){
        return delete(Table.ALARM_CLOCK,id);
    }

    //endregion

    //region getCursor_Schedule

    public Cursor getCursor_ScheduleByDay(String day){
        return getCursor_Schedule(dayFilter,day);
    }

    public Cursor getCursor_ScheduleByDay(String start,String end){
        return getCursor_Schedule(dayFilter,start,end);
    }

    public Cursor getCursor_ScheduleByMonth(String Month){
        return getCursor_Schedule(monthFilter,Month);
    }

    public Cursor getCursor_ScheduleByMonth(String start,String end){
        return getCursor_Schedule(monthFilter,start,end);
    }

    public Cursor getCursor_ScheduleByYear(String Year){
        return getCursor_Schedule(yearFilter,Year);
    }

    public Cursor getCursor_ScheduleByYear(String start,String end){
        return getCursor_Schedule(yearFilter,start,end);
    }


    Cursor getCursor_Schedule(String filter,String time){
        String sqlTime = "'" + time + "'";
        return db.rawQuery("SELECT _id,Time,Name FROM " + SQLiteDB.scheduleTable +
                " WHERE "+filter+"=" + sqlTime +
                " ORDER BY Time DESC",null);
    }

    Cursor getCursor_Schedule(String filter,String start, String end){
        String sqlStart = "'" + start + "'";
        String sqlEnd = "'" + end + "'";
        return db.rawQuery("SELECT _id,Time,Name FROM " + SQLiteDB.scheduleTable +
                " WHERE "+filter+">="+sqlStart + " AND " +filter+"<="+sqlEnd +
                " ORDER BY Time DESC",null);
    }

    //endregion

    //region getCursor_Clock

    Cursor getCursor_Clock(){
        return db.rawQuery("SELECT * FROM " + SQLiteDB.alarmClockTable,null);
    }

    //endregion

    //region CRUD



    public boolean update(Table table,ContentValues values,long id){
        String tableStr = Table.getTableName(table);
        String id_str = Long.toString(id);
        if(tableStr == null){
            Log.d("DatabaseError","DataBase update : table name not found");
            return false;
        }
        int e = db.update(tableStr, values,"_id=" + id_str, null);

        if(e == 0){
            Log.d("DatabaseError","DataBase update : id is not match in table");
            return false;
        }
        return true;

    }

    public boolean delete(Table table,long id) {
        String tableStr = Table.getTableName(table);
        String id_str = Long.toString(id);
        int e;
        if(tableStr == null){
            Log.d("DatabaseError","DataBase delete : table name not found");
            return false;
        }

        if(id==-1){
            e = db.delete(tableStr,"1", null);
        }
        else{
            e = db.delete(tableStr, "_id=" + id_str, null);
        }

        if(e == 0){
            Log.d("DatabaseError","DataBase delete : id is not match in table");
            return false;
        }
        return true;
    }

    public boolean delete(Table table) {
        return delete(table,-1);
    }

    //endregion

    //region private method

    int EncodeRepeatCode(boolean[] repeat){
        int repeat_code = 0;
        for (int i = 0; i < repeat.length; i++){
            if(repeat[i]){
                repeat_code += (int)Math.pow(2,i);
            }
        }
        return repeat_code;
    }

    boolean[] DecodeRepeatCode(int code){
        boolean[] repeat = new boolean[7];
        for(int i = repeat.length - 1; i >= 0; i--){
            int cmp = code - (int)Math.pow(2,i);
            if(cmp >= 0){
                repeat[i] = true;
                code -= cmp;
            }
        }
        return repeat;
    }

    //endregion
}


class SQLiteDB extends SQLiteOpenHelper {
    public static final String scheduleTable = "scheduleTable";
    public static final String alarmClockTable = "alarmClockTable";

    public static final String row_index = "_id";

    //scheduleTable columns
    public static final String scd_Time = "Time";
    public static final String scd_Name = "Name";

    //scheduleTable columns
    public static final String alc_Time = "Time";
    public static final String alc_Name = "Name";
    public static final String alc_Music = "Music";
    public static final String alc_Repeat = "Repeat";
    public static final String alc_Shock = "Shock";
    public static final String alc_Enable = "Enable";


    public SQLiteDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        Log.d("DatabasePosition","DataBase constructor : finish");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DatabasePosition","DataBase Create : Begin");

        Log.d("DatabasePosition","DataBase Create : Create scheduleTable");
        final String createScheduleTable = "CREATE TABLE IF NOT EXISTS "
                + scheduleTable + "("
                + row_index + " INTEGER PRIMARY KEY, "
                + scd_Time + " TEXT NOT NULL,"
                + scd_Name + " TEXT NOT NULL "
                + ")";
        db.execSQL(createScheduleTable);

        Log.d("DatabasePosition","DataBase Create : Create alarmClockTable");
        final String createAlarmClockTable = "CREATE TABLE IF NOT EXISTS "
                + alarmClockTable + "("
                + row_index + " INTEGER PRIMARY KEY, "
                + alc_Time + " TEXT NOT NULL, "
                + alc_Name + " TEXT DEFAULT '', "
                + alc_Music + " TEXT DEFAULT '', "
                + alc_Repeat + " INTEGER NOT NULL DEFAULT 0, "
                + alc_Shock + " INTEGER NOT NULL DEFAULT 1, "
                + alc_Enable + " INTEGER NOT NULL DEFAULT 1 "
                + ")";
        db.execSQL(createAlarmClockTable);

        Log.d("DatabasePosition","DataBase Create : Finish");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("DatabasePosition","DataBase Upgrade : Begin");

        final String drop = "DROP TABLE " + scheduleTable;
        db.execSQL(drop);
        onCreate(db);

        Log.d("DatabasePosition","DataBase Upgrade : Finish");
    }
}
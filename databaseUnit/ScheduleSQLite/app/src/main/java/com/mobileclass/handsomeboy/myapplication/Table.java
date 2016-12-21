package com.mobileclass.handsomeboy.myapplication;

/**
 * Created by HandsomeBoy on 2016/12/7.
 */
public enum Table{
    SCHEDULE,ALARM_CLOCK;

    public static String getTableName(Table table) {
        String tableName;
        switch (table) {
            case SCHEDULE:
                tableName = "scheduleTable";
                break;
            case ALARM_CLOCK:
                tableName = "alarmClockTable";
                break;
            default:
                tableName = null;
                break;
        }
        return tableName;
    }
}

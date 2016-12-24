package com.example.keng.settime;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener
                                                                ,AdapterView.OnItemLongClickListener{

    Calendar calendar;
    ArrayList<String> arrayList;
    ArrayAdapter<String> tempAd;
    ListView listView;
    Intent intent;
    int hour,minute;
    long time_offset[];
    PendingIntent pendingIntent[];
    static final int ALARMCLOCK_NUM=10;
    int count=0;
    AlarmManager alarmManager[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intent=new Intent(this,MyBroadcastRecv.class);
        alarmManager=new AlarmManager[ALARMCLOCK_NUM];
        pendingIntent=new PendingIntent[ALARMCLOCK_NUM];
        listView=(ListView)findViewById(R.id.listview);
        arrayList = new ArrayList<String>();
        time_offset=new long[ALARMCLOCK_NUM];
        //dynamic array to insert data will time pick
        tempAd=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayList);
        listView.setOnItemLongClickListener(this);
    }
    public void Click(View view){

        calendar=Calendar.getInstance();
        int hr=calendar.get(Calendar.HOUR_OF_DAY);
        int min=calendar.get(Calendar.MINUTE);

        new TimePickerDialog(this,2,this,hr,min,false).show();//(context,theme,TimeSetListener,hour,min,is24?)

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minuteofHour) {

        String hour_str,minute_str;
        if(count<ALARMCLOCK_NUM){
            //set hour,minute through timepicker
            hour=hourOfDay;//hourofDay will return 24hr-format
            minute=minuteofHour;
            //convert to string
            hour_str=String.valueOf(hour);
            minute_str=String.valueOf(minute);
            //set comfortable look for minute or hour<10
            if(minute<10){
                minute_str="0"+minute_str;
            }
            else if(hour<10){
                hour_str="0"+hour_str;
            }
            //set Calender time
            calendar.set(Calendar.HOUR_OF_DAY,hour);
            calendar.set(Calendar.MINUTE,minute);
            calendar.set(Calendar.SECOND,0);
            //insert arrayList data into dynamic array
            arrayList.add(hour_str+":"+minute_str);
            listView.setAdapter(tempAd);

            intent.putExtra("id",String.valueOf(count+1));
            //use temp-liked variable to set offset time
            time_offset[count]=calendar.getTimeInMillis();
            //set Alarm_manager;
            pendingIntent[count]=PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager[count]=(AlarmManager)this.getSystemService(ALARM_SERVICE);
            alarmManager[count].set(AlarmManager.RTC_WAKEUP,time_offset[count],pendingIntent[count]);
            //call back
            Log.e("set_up","Alarm manager "+String.valueOf(count));
            Log.e("add clock:",hour_str+":"+minute_str);
            count++;
        }else{
            Toast.makeText(this,"Alarm clock is full",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        //convert click item to textview
        TextView selected_item=(TextView)view;
        //convert selected item to string
        String selected=selected_item.getText().toString();
        //remove from arraylist&temp_id&count
        arrayList.remove(selected);
        tempAd.remove(selected);
        alarmManager[position].cancel(pendingIntent[position]);
        if(count>=0){
            count--;
        }
        //Log_message
        Log.e("remove",selected);
        return  true;
    }
}

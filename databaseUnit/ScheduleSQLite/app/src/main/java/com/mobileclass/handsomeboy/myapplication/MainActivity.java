package com.mobileclass.handsomeboy.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    ScheduleDatabase db;

    Button btnInsert,btnUpdate,btnDelete,btnClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnInsert = (Button)findViewById(R.id.button);
        btnUpdate = (Button)findViewById(R.id.button2);
        btnDelete = (Button)findViewById(R.id.button3);
        btnClear = (Button)findViewById(R.id.button4);

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertItem();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateItem();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteItem();
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClearItem();
            }
        });

        db = new ScheduleDatabase(this);

        CalendarManager.getClock(16,57);
        //InsertItem();
        //UpdateItem();
        //DeleteItem();
        //ClearItem();
    }

    void InsertItem(){
        db.insertSchedule(CalendarManager.getTime(2016,12,5,8,0,0),"起床");
        db.insertSchedule(CalendarManager.getTime(2016,12,5,12,0,0),"上課");
        db.insertSchedule(CalendarManager.getTime(2016,12,5,22,0,0),"耍費");
        db.insertClock(new int[]{12,22},"ccc","music~~~"
                ,new boolean[]{true,false,true,false,true,false,true},true,true);
    }

    void UpdateItem(){
        db.updateSchedule(null,"不耍費了",3);
        db.updateClock(new int[]{6,2},"bingo","oh~~~"
                ,new boolean[]{false,false,false,false,false,false,false},false,false,1);
    }

    void DeleteItem(){
        db.delete(Table.SCHEDULE,2);
        db.delete(Table.ALARM_CLOCK,1);
    }

    void ClearItem(){
        db.delete(Table.SCHEDULE);
        db.delete(Table.ALARM_CLOCK);
    }
}

package info.devexchanges.navvp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.marcohc.robotocalendar.RobotoCalendarView;
import com.mobileclass.handsomeboy.myapplication.ScheduleDatabase;
import com.mobileclass.handsomeboy.myapplication.SchedulePackage;
import com.mobileclass.handsomeboy.myapplication.Table;

import java.util.ArrayList;
import java.util.Calendar;

public class Fragment1 extends Fragment  implements RobotoCalendarView.RobotoCalendarListener{
    private static final int SET_MPinput=1;//跳轉回傳到頁面，intent
    private RobotoCalendarView robotoCalendarView;
    int month=0,year=0,date=0;
    private ListView listView;
    private ArrayAdapter adapter;
    private View myView;
    private RobotoCalendarView.RobotoCalendarListener robotoCalendarListener;
    Button Input;
    ScheduleDatabase scheduleDatabase;
    int[] id;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView= inflater.inflate(R.layout.fragment1, container, false);
        setButton();
        // Gets the calendar from the view
        robotoCalendarView = (RobotoCalendarView) myView.findViewById(R.id.robotoCalendarPicker);

        // Set listener, in this case, the same activity
        robotoCalendarView.setRobotoCalendarListener(this);
        robotoCalendarView.setShortWeekDays(false);

        robotoCalendarView.showDateTitle(true);

        robotoCalendarView.updateView();

        scheduleDatabase=new ScheduleDatabase(getActivity());
     return  myView;
    }

    private void setButton() {
        Input = (Button) myView.findViewById(R.id.add);

        Input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getContext(),MPinputActivity.class);
                myIntent.putExtra("YEAR",year);
                myIntent.putExtra("MONTH",month);
                myIntent.putExtra("DATE",date);
                startActivityForResult(myIntent, SET_MPinput);
            }
        });
    }

    public void onDayClick(Calendar daySelectedCalendar) {
        year=daySelectedCalendar.get(Calendar.YEAR);
        month=daySelectedCalendar.get(Calendar.MONTH)+1;//月處理的方式
        date=daySelectedCalendar.get(Calendar.DAY_OF_MONTH);
        //robotoCalendarView.markCircleImage1(daySelectedCalendar);//產生點點的函式

        list_update();


        //Toast.makeText(getActivity(), "onDayClick: " + daySelectedCalendar.getTime(), Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onDayLongClick(Calendar daySelectedCalendar) {
        //Toast.makeText(this, "onDayLongClick: " + daySelectedCalendar.getTime(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRightButtonClick() {
        // Toast.makeText(this, "onRightButtonClick!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLeftButtonClick() {
        //Toast.makeText(this, "onLeftButtonClick!", Toast.LENGTH_SHORT).show();
    }

    void list_update(){

        //listview
        listView = (ListView) myView.findViewById(R.id.listView1);
        // 資料庫取得

        SchedulePackage schedulePackage = scheduleDatabase.getScheduleByDay(year,month,date);
        if(schedulePackage==null)
        {
            return;
        }
        id = schedulePackage.idArr;
        ArrayList<String> time = schedulePackage.timeArr;
        ArrayList<String> name = schedulePackage.nameArr;

        adapter = new ArrayAdapter(getContext(),
                android.R.layout.simple_list_item_1);
        if(schedulePackage != null){
            // 清單陣列
            for(int i=0;i<id.length;i++){

                char[] s =time.get(i).toCharArray();

                adapter.add(i+1+". "+name.get(i)+"   "+s[11]+s[12]+s[13]+s[14]+s[15]);
            }
        }

//        String s = time.get(0);
//        s.toCharArray(); s[0]+s[1]+s[2]+s[3]+s[4]+s[5]+s[6]+s[7]+s[8]+s[9]+s[10]+

        listView.setAdapter(adapter);
        //longclick
        listView.setLongClickable(true);
        listView.setOnItemLongClickListener( new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean  onItemLongClick(AdapterView arg0, View arg1, int arg2,
                                            long arg3) {
                // TODO Auto-generated method stub
                ListView listView = (ListView) arg0;// ID arg3  文字 arg2
                scheduleDatabase.delete(Table.SCHEDULE,id[arg2]);
//                manager.delete(Table.MONTH_PLAN,id[arg2]);
                list_update();
                /*
                Toast.makeText(
                        getContext(),
                        "ID：" + arg3 +
                                "   選單文字："+ listView.getItemAtPosition(arg2).toString()+"刪除",
                        Toast.LENGTH_LONG).show();
                */
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        Calendar calendar = Calendar.getInstance();

        if(year == 0){
            onDayClick(calendar);
        }else{
            calendar.set(Calendar.YEAR,year);
            calendar.set(Calendar.MONTH,month - 1);
            calendar.set(Calendar.DAY_OF_MONTH,date);
            onDayClick(calendar);
        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}

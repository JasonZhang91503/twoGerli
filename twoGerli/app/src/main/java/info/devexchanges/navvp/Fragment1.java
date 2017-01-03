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
    int[] id;
    String[] description;
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
        robotoCalendarView.markCircleImage1(daySelectedCalendar);//產生點點的函式

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


        adapter = new ArrayAdapter(getContext(),
                android.R.layout.simple_list_item_1);
//        if(planPackage != null){
//            id=planPackage.id;
//            description = planPackage.description;
//            // 清單陣列
//
//            for(int i=0;i<description.length;i++){
//                adapter.add(description[i]+id[i]);
//            }
//        }

        listView.setAdapter(adapter);
        //longclick
        listView.setLongClickable(true);
        listView.setOnItemLongClickListener( new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean  onItemLongClick(AdapterView arg0, View arg1, int arg2,
                                            long arg3) {
                // TODO Auto-generated method stub
                ListView listView = (ListView) arg0;// ID arg3  文字 arg2

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
        list_update();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}

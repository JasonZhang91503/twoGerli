package com.example.user.schedulelist;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.TypedArray;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.os.Handler;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import android.app.ListActivity;
import android.widget.LinearLayout.LayoutParams;


public class MainActivity extends ListActivity
        implements AbsListView.OnScrollListener{
    private ListView lsv_main;
    private ExpandableLayoutAdapter mExpandAdapter;
    private int mLastItem=0;
    private int mCount=3000;
    private LinearLayout mLoadLayout;
    private final Handler mHandler=new Handler();
    private final LinearLayout.LayoutParams mProgressBarLayoutParams=new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT);
    private final LinearLayout.LayoutParams mTipContentLayoutParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT);
    Calendar c=Calendar.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int Month;
        int Day;
        int Year;
        List<String> itemList=new ArrayList<String>();
        Month=c.get(Calendar.MONTH)+1;
        Day=c.get(Calendar.DAY_OF_MONTH);
        Year=c.get(Calendar.YEAR);
        for (int i=0;i<mCount;i++)
        {
            if(Day>monthcheck(Month))//換下個月
            {
                Day=0;
                if(Month==12)
                    Month=1;
                else
                    Month++;
            }
            else
            {
                if(Day==1)
                    if(Month==1){
                        ++Year;
                        itemList.add(Year+"/"+Month+"/"+Integer.toString(Day));
                    }
                    else
                        itemList.add(Year+"/"+Month+"/"+Integer.toString(Day));
                else
                    itemList.add(Month+"/"+Integer.toString(Day));
            }

            Day++;
        }
        mExpandAdapter=new ExpandableLayoutAdapter(this,itemList);
        lsv_main=getListView();
        lsv_main.setAdapter(mExpandAdapter);
         mLoadLayout=new LinearLayout(this);
           mLoadLayout.setMinimumHeight(60);
          mLoadLayout.setGravity(Gravity.CENTER);
          mLoadLayout.setOrientation(LinearLayout.HORIZONTAL);
        //進度條
          ProgressBar mProgressBar = new ProgressBar(this);
           mProgressBar.setPadding(0, 0, 15, 0);
          mLoadLayout.addView(mProgressBar, mProgressBarLayoutParams);

         TextView mTipContent = new TextView(this);
          mTipContent.setText("加載中......");
          mLoadLayout.addView(mTipContent, mTipContentLayoutParams);
        //取得listview物件

         lsv_main.addFooterView(mLoadLayout);
        lsv_main.setOnScrollListener(this);
        lsv_main.setOnItemClickListener(listViewOnItemClickListener);
    }
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mLastItem=firstVisibleItem+visibleItemCount-1;
        if(mExpandAdapter.count>mCount)
            lsv_main.removeFooterView(mLoadLayout);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(mLastItem==mExpandAdapter.count
                && scrollState== AbsListView.OnScrollListener.SCROLL_STATE_IDLE)
        {
            if(mExpandAdapter.count<=mCount)
            {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                            mExpandAdapter.count+=30;
                           mExpandAdapter.notifyDataSetChanged();
                           lsv_main.setSelection(mLastItem);
                    }
                },1000);
            }
        }

    }

    private AdapterView.OnItemClickListener listViewOnItemClickListener
            =new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {

        }
    };

    private int monthcheck(int month)
    {
        switch (month)
        {
            case 1:
                return 31;
            case 2:
                return 28;
            case 3:
                return 31;
            case 4:
                return 30;
            case 5:
                return 31;
            case 6:
                return 30;
            case 7:
                return 31;
            case 8:
                return 31;
            case 9:
                return 30;
            case 10:
                return 31;
            case 11:
                return 30;
            case 12:
                return 31;

        }
        return 0;
    }
}

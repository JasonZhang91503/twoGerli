package com.handsome.test.expandablelisttest;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class myFragment extends Fragment implements AbsListView.OnScrollListener {
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

    public myFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_my, container, false);

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

        mExpandAdapter=new ExpandableLayoutAdapter(getActivity(),itemList);
        lsv_main = (ListView) view.findViewById(R.id.list);
        lsv_main.setAdapter(mExpandAdapter);
        mLoadLayout=new LinearLayout(getActivity());
        mLoadLayout.setMinimumHeight(60);
        mLoadLayout.setGravity(Gravity.CENTER);
        mLoadLayout.setOrientation(LinearLayout.HORIZONTAL);
        //進度條
        ProgressBar mProgressBar = new ProgressBar(getActivity());
        mProgressBar.setPadding(0, 0, 15, 0);
        mLoadLayout.addView(mProgressBar, mProgressBarLayoutParams);

        TextView mTipContent = new TextView(getActivity());
        mTipContent.setText("加載中......");
        mLoadLayout.addView(mTipContent, mTipContentLayoutParams);
        //取得listview物件

        lsv_main.addFooterView(mLoadLayout);
        lsv_main.setOnScrollListener(this);
        lsv_main.setOnItemClickListener(listViewOnItemClickListener);

        return view;
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

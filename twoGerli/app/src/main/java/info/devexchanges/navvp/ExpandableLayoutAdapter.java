package info.devexchanges.navvp;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mobileclass.handsomeboy.myapplication.ScheduleDatabase;
import com.mobileclass.handsomeboy.myapplication.SchedulePackage;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;



 class ExpandableLayoutAdapter extends BaseAdapter {
     ScheduleDatabase scheduleDatabase;
    private List<Integer>Year,Month,Day;
    private List<String> arraylist;
    private Context context;
    int count=40;
     int id[];
     String thing;
     Calendar c=Calendar.getInstance();
     ExpandableLayoutAdapter(Context context, List<String> lists) {
        this.context = context;
        arraylist = lists;
    }
     ExpandableLayoutAdapter(Context context,List<String> lists,List<Integer> year,
                             List<Integer> month,List<Integer> day){
         this.context = context;
         arraylist = lists;
         Year=year;
         Month=month;
         Day=day;
     }
    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Object getItem(int position) {return position;}

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        scheduleDatabase=new ScheduleDatabase(context);
        final ViewHolder viewHolder;
        thing="";
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.expandable_layout, parent,false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        settext(position);
        viewHolder.itemTv.setText(arraylist.get(position));
        viewHolder.menuTv.setText(thing);
        viewHolder.menuLy.measure(0, 0);
        final int height = viewHolder.menuLy.getMeasuredHeight();
        viewHolder.itemTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewHolder.menuLy.getVisibility() == View.GONE){
                    show(viewHolder.menuLy,height);
                }else{
                    dismiss(viewHolder.menuLy,height);
                }
            }
        });
        return convertView;
    }
    void settext(int position){
        SchedulePackage schedulePackage = scheduleDatabase.getScheduleByDay(Year.get(position),Month.get(position),Day.get(position));
            if(schedulePackage==null)
                return;
            id = schedulePackage.idArr;
            ArrayList<String> time = schedulePackage.timeArr;
            ArrayList<String> name = schedulePackage.nameArr;
            if(schedulePackage!=null) {
                for(int i=0;i<id.length;i++){
                    char[] s =time.get(i).toCharArray();
                    thing+=(i+1+". "+name.get(i)+"   "+s[11]+s[12]+s[13]+s[14]+s[15]+"\n");
                }
            }
    }
    private void show(final View v,int height)//向下動畫
    {
        v.setVisibility(View.VISIBLE);
        ValueAnimator animator=ValueAnimator.ofInt(0,height);
        animator.setDuration(500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                v.getLayoutParams().height=(Integer)animation.getAnimatedValue();
                v.setLayoutParams(v.getLayoutParams());
            }
        });
        animator.start();
    }
    private void dismiss(final View v,int height)//向上動畫
    {
        ValueAnimator animator = ValueAnimator.ofInt(height,0);
        animator.setDuration(500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (Integer) animation.getAnimatedValue();
                if (value == 0) {
                    v.setVisibility(View.GONE);
                }
                v.getLayoutParams().height = value;
                v.setLayoutParams(v.getLayoutParams());
            }
        });
        animator.start();
    }
   private class ViewHolder {
        View rootView;
        TextView itemTv;
        TextView menuTv;
        FrameLayout menuLy;

         ViewHolder(View view) {
            rootView = view;
            itemTv = (TextView) view.findViewById(R.id.item_layout);
            menuTv = (TextView) view.findViewById(R.id.menu_tv);
            menuLy = (FrameLayout) view.findViewById(R.id.menu_layout);
        }
    }
}

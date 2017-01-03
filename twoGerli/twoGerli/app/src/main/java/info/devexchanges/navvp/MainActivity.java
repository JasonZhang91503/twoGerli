package info.devexchanges.navvp;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.mobileclass.handsomeboy.myapplication.CalendarManager;
import com.mobileclass.handsomeboy.myapplication.ScheduleDatabase;
import com.mobileclass.handsomeboy.myapplication.SchedulePackage;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.TimeZone;

import info.devexchanges.navvp.preferences.AlarmPreferencesActivity;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private ViewPager viewPager;
    private DrawerLayout drawer;
    private TabLayout tabLayout;
    private String[] pageTitle = {"Calendar", "Agenda", "Alarm Clock"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("1","15145");

        if(!ScheduleIntentService.isServiceRunning(this,"ScheduleIntentService")){
            ScheduleIntentService.startActionNotify(this);
        }

        viewPager = (ViewPager)findViewById(R.id.view_pager);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawerLayout);

        setSupportActionBar(toolbar);

        //create default navigation drawer toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //setting Tab layout (number of Tabs = number of ViewPager pages)
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        for (int i = 0; i < 3; i++) {
            tabLayout.addTab(tabLayout.newTab().setText(pageTitle[i]));
        }

        //set gravity for tab bar
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //handling navigation view item event
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);

        //set viewpager adapter
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        //change Tab selection when swipe ViewPager
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //change ViewPager page when tab selected
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        ScheduleDatabase scheduleDatabase = new ScheduleDatabase(this);
        scheduleDatabase.insertSchedule(CalendarManager.getTime(2016,12,30,5,7,20),"YEAH");
        scheduleDatabase.insertSchedule(CalendarManager.getTime(2016,12,30,5,7,25),"YEAH");
        scheduleDatabase.insertSchedule(CalendarManager.getTime(2016,12,30,5,7,28),"YEAH");


        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"));
        Timestamp timestamp = new Timestamp(calendar.getTimeInMillis());
        Log.d("TEST",timestamp.toString());
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.fr1) {
            viewPager.setCurrentItem(0);//跳到第一個fragment
        } else if (id == R.id.fr2) {
            viewPager.setCurrentItem(1);//跳到第2個fragment
        } else if (id == R.id.fr3) {
            viewPager.setCurrentItem(2);//跳到第3個fragment
        } else if (id == R.id.set) {
            Intent intent = new Intent(this, AlarmPreferencesActivity.class);
            intent.putExtra("string", "SET");
            startActivity(intent);
        } else if (id == R.id.close) {
            finish();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        assert drawer != null;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}

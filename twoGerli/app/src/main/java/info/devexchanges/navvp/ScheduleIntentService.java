package info.devexchanges.navvp;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import com.mobileclass.handsomeboy.myapplication.CalendarManager;
import com.mobileclass.handsomeboy.myapplication.ScheduleDatabase;
import com.mobileclass.handsomeboy.myapplication.SchedulePackage;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class ScheduleIntentService extends IntentService {
    private static final String ACTION_NOTIFICATION = "info.devexchanges.navvp.action.FOO";


    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "info.devexchanges.navvp.extra.PARAM1";


    public ScheduleIntentService() {
        super("ScheduleIntentService");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        flags = Service.START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionNotify(Context context) {
        Intent intent = new Intent(context, ScheduleIntentService.class);
        intent.setAction(ACTION_NOTIFICATION);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_NOTIFICATION.equals(action)) {
                handleActionNotification();
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionNotification() {
        ScheduleDatabase scheduleDatabase = new ScheduleDatabase(this);
        boolean isNotify = false;
        int[] notifiTime = scheduleDatabase.getLatestRecordTime();


        if(notifiTime == null){
            notifiTime = new int[6];
            notifiTime[0] = 1970;
            notifiTime[0] = 1;
            notifiTime[0] = 1;
            notifiTime[0] = 0;
            notifiTime[0] = 0;
            notifiTime[0] = 0;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(notifiTime[0],notifiTime[1]-1
                ,notifiTime[2],notifiTime[3],notifiTime[4],notifiTime[5]);
        calendar.set(Calendar.MILLISECOND,0);

        Log.d("ScheduleService","Service is start, notification time is = "
                + notifiTime[0] + "-" + notifiTime[1] + "-" +notifiTime[2] + " " +
                notifiTime[3] + ":"+ notifiTime[4] + ":" + notifiTime[5]);

        //重複詢問是否改過值
        while (true){
            if(ScheduleDatabase.getModify()){   //若改過就重新問提醒時間
                notifiTime = scheduleDatabase.getLatestRecordTime();  //會把modify變false

                Log.d("ScheduleService","Database is modified, notification time is = "
                        + notifiTime[0] + "-" + notifiTime[1] + "-" +notifiTime[2] + " " +
                        notifiTime[3] + ":"+ notifiTime[4] + ":" + notifiTime[5]);
                calendar.set(notifiTime[0],notifiTime[1]-1
                        ,notifiTime[2],notifiTime[3],notifiTime[4],notifiTime[5]);
                calendar.set(Calendar.MILLISECOND,0);

            }
            else{   //時間沒改動過

            }
            Timestamp t = new Timestamp(calendar.getTimeInMillis());
            //Log.d("Timestamp",t.toString());

            if(CalendarManager.getTime().equals(t.toString()) && !isNotify){
                Log.d("ScheduleService","Notification time~~~");
                final int notifyID = 1; // 通知的識別號碼
                final NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE); // 取得系統的通知服務
                final Notification notification = new Notification.Builder(getApplicationContext())
                        .setContentTitle("內容標題").setContentText(notifiTime[0] + "-" + notifiTime[1] + "-" +notifiTime[2] + " " +
                                notifiTime[3] + ":"+ notifiTime[4] + ":" + notifiTime[5]).setSmallIcon(R.drawable.glyphicons_049_star).getNotification(); // 建立通知
                notificationManager.notify(notifyID, notification); // 發送通知


                notifiTime = scheduleDatabase.getLatestRecordTime();  //會把modify變false

                if(notifiTime == null){
                    notifiTime = new int[6];
                    notifiTime[0] = 1970;
                    notifiTime[0] = 1;
                    notifiTime[0] = 1;
                    notifiTime[0] = 0;
                    notifiTime[0] = 0;
                    notifiTime[0] = 0;
                }

                Log.d("ScheduleService","Database is modified, notification time is = "
                        + notifiTime[0] + "-" + notifiTime[1] + "-" +notifiTime[2] + " " +
                        notifiTime[3] + ":"+ notifiTime[4] + ":" + notifiTime[5]);
                calendar.set(notifiTime[0],notifiTime[1]-1
                        ,notifiTime[2],notifiTime[3],notifiTime[4],notifiTime[5]);
                calendar.set(Calendar.MILLISECOND,0);
                isNotify = true;
            } else if(CalendarManager.getTime().equals(t.toString())){
                //時間剛好且通知過
            }
            else{
                isNotify = false;
            }

        }


    }

    public static boolean isServiceRunning(Context context, String serviceClassName){
        final ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningServiceInfo > services = activityManager.getRunningServices(Integer.MAX_VALUE);

        for (ActivityManager.RunningServiceInfo runningServiceInfo : services) {
            if (runningServiceInfo.service.getClassName().equals(serviceClassName)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void onDestroy() {
        Log.d("ScheduleService","Service destroy");
        super.onDestroy();
    }
}

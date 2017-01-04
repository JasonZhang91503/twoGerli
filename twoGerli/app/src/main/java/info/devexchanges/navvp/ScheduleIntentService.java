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
import com.mobileclass.handsomeboy.myapplication.NotifyPackage;
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
        Calendar calendar = Calendar.getInstance();
        boolean isNotify = false;
        String notifyInfo;

        //初次啟動需要檢查提醒時間
        notifyInfo = checkTime(scheduleDatabase,calendar);

        //重複偵測提醒時間是否改變
        while (true){
            if(ScheduleDatabase.getModify()){   //若改過，重新設定提醒時間
                notifyInfo = checkTime(scheduleDatabase,calendar);
            }

            Timestamp notifyTimestamp = new Timestamp(calendar.getTimeInMillis());  //提醒時間

            //比較現在系統時間與提醒的時間是否一樣，並且尚未進行此次提醒
            if(CalendarManager.getTime().equals(notifyTimestamp.toString()) && !isNotify){
                Log.d("ScheduleService","Notification time~~~");

                String notiTime = calendar.get(Calendar.YEAR) + "-" +
                        (calendar.get(Calendar.MONTH) + 1) + "-" +calendar.get(Calendar.DAY_OF_MONTH) + " " +
                        calendar.get(Calendar.HOUR_OF_DAY) + ":"+ calendar.get(Calendar.MINUTE) + ":" +
                        calendar.get(Calendar.SECOND);
                int notifyID = 1; // 通知的識別號碼

                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE); // 取得系統的通知服務
                Notification notification = new Notification.Builder(getApplicationContext())
                        .setContentTitle(notifyInfo).setContentText(notiTime).setSmallIcon(R.drawable.glyphicons_049_star).getNotification(); // 建立通知
                notificationManager.notify(notifyID, notification); // 發送通知

                //再次確認提醒時間
                notifyInfo = checkTime(scheduleDatabase,calendar);

                isNotify = true;
            } else if(CalendarManager.getTime().equals(notifyTimestamp.toString())){
                //是提醒時間，但是此次已經通知過了
            }
            else{   //時間超過後要設isNotify為false
                isNotify = false;
            }

        }
    }

    String checkTime(ScheduleDatabase database,Calendar calendar){
        NotifyPackage notifyPackage = database.getLatestRecordTime();
        int[] notifiTime = notifyPackage.idArr;

        if(notifiTime == null){
            notifiTime = new int[6];
            notifiTime[0] = 1970;
            notifiTime[1] = 1;
            notifiTime[2] = 1;
            notifiTime[3] = 0;
            notifiTime[4] = 0;
            notifiTime[5] = 0;
        }
        calendar.set(notifiTime[0],notifiTime[1]-1
                ,notifiTime[2],notifiTime[3],notifiTime[4],notifiTime[5]);
        calendar.set(Calendar.MILLISECOND,0);

        Log.d("ScheduleService","Service is start, notification time is = "
                + notifiTime[0] + "-" + notifiTime[1] + "-" +notifiTime[2] + " " +
                notifiTime[3] + ":"+ notifiTime[4] + ":" + notifiTime[5]);

        return notifyPackage.notifyInfo;
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

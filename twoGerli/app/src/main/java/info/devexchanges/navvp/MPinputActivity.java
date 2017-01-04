package info.devexchanges.navvp;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.mobileclass.handsomeboy.myapplication.CalendarManager;
import com.mobileclass.handsomeboy.myapplication.ScheduleDatabase;

public class MPinputActivity extends AppCompatActivity {
    private String output;
    int year,month,date;
    int hourOfDay1 , minute1; //選的時和分
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpinput);

        //取得傳遞資料
        Intent myIntent = this.getIntent();
        year = myIntent.getIntExtra("YEAR",1);
        month = myIntent.getIntExtra("MONTH",1);
        date = myIntent.getIntExtra("DATE",1);


    }

    public void button2_Click(View view) {
        switch (view.getId()){
            case R.id.button2:
                Intent replyIntent = new Intent();
                // 建立傳回值
                Bundle bundle = new Bundle();
                bundle.putString("MPINPUT", output);
                replyIntent.putExtras(bundle);  // 加上資料
                setResult(RESULT_OK, replyIntent); // 設定傳回
                finish(); // 結束活動
                break;
            case R.id.btnOK:
                //資料庫輸入
                ScheduleDatabase scheduleDatabase = new ScheduleDatabase(this);
                String time1 = CalendarManager.getTime(year,month,date,hourOfDay1,minute1,0);

                EditText edtx1=(EditText)findViewById(R.id.editText1);
                output = edtx1.getText().toString();
                 scheduleDatabase.insertSchedule(time1, output);
                finish();
                break;
        }
    }

    public void button3_Click(View view) {

        TimePickerDialogFragment dlg = TimePickerDialogFragment.newInstance();
        FragmentManager fm = getSupportFragmentManager();
        dlg.show(fm, "timepickerdialog");
    }

    public void getTime(int hourOfDay , int minute) {
        TextView output = (TextView) findViewById(R.id.lblOutput);
        output.setText(Integer.toString(hourOfDay) + ":" +
                Integer.toString(minute));
        hourOfDay1 = hourOfDay;
        minute1=minute;

    }
}

package info.devexchanges.navvp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MPinputActivity extends AppCompatActivity {
    private String output;
    int year,month,date;
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
//                GerliDatabaseManager manager = new GerliDatabaseManager(this);
                //manager.insertMonthPlan(2016,12,21,"柳隨雲好帥");//新增一筆月計畫

                EditText edtx1=(EditText)findViewById(R.id.editText1);
                output = edtx1.getText().toString();
//                manager.insertMonthPlan(year,month,date,output);
                finish();
                break;
        }
    }
}

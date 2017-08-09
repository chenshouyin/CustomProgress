package practice.csy.com.customprogress;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

import practice.csy.com.customprogress.view.CustomHorizontalProgres;

public class MainActivity extends AppCompatActivity {

    private CustomHorizontalProgres horizontalProgress1,horizontalProgress2,horizontalProgress3;
    private Timer timer,timer2,timer3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        horizontalProgress1 = (CustomHorizontalProgres) findViewById(R.id.horizontalProgress1);
        horizontalProgress2 = (CustomHorizontalProgres) findViewById(R.id.horizontalProgress2);
        horizontalProgress3 = (CustomHorizontalProgres) findViewById(R.id.horizontalProgress3);

        horizontalProgress1.setProgress(0);
        horizontalProgress1.setMax(100);

        horizontalProgress2.setProgress(0);
        horizontalProgress2.setMax(100);

        horizontalProgress3.setProgress(0);
        horizontalProgress3.setMax(200);

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //实时更新进度
                if (horizontalProgress1.getProgress() >= 100) {//指定时间取消
                    timer.cancel();
                }
                horizontalProgress1.setProgress(horizontalProgress1.getProgress()+1);

            }
        }, 50, 50);

        timer2 = new Timer();
        timer2.schedule(new TimerTask() {
            @Override
            public void run() {
                //实时更新进度
                if (horizontalProgress2.getProgress() >= 100) {//指定时间取消
                    timer2.cancel();
                }
                horizontalProgress2.setProgress(horizontalProgress2.getProgress()+1);

            }
        }, 40, 40);


        timer3 = new Timer();
        timer3.schedule(new TimerTask() {
            @Override
            public void run() {
                //实时更新进度
                if (horizontalProgress3.getProgress() >= horizontalProgress3.getMax()) {//指定时间取消
                    timer3.cancel();
                }
                horizontalProgress3.setProgress(horizontalProgress3.getProgress()+1);

            }
        }, 50, 50);
    }


}

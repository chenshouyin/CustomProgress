package practice.csy.com.customprogress;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

import practice.csy.com.customprogress.view.CustomHorizontalProgresNoNum;
import practice.csy.com.customprogress.view.CustomHorizontalProgresWithNum;
import practice.csy.com.customprogress.view.RoundlProgresWithNum;

public class MainActivity extends AppCompatActivity {
    private CustomHorizontalProgresNoNum horizontalProgress01;//水平不带进度
    private CustomHorizontalProgresWithNum horizontalProgress1,horizontalProgress2,horizontalProgress3;//水平带进度
    private RoundlProgresWithNum mRoundlProgresWithNum31;//自定义圆形进度条 不带数字进度
    private RoundlProgresWithNum mRoundlProgresWithNum32,mRoundlProgresWithNum33;//自定义圆形进度条 带数字进度

    private Timer timer,timer2,timer3;
    private Timer timer31,timer32,timer33;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        horizontalProgress01 = (CustomHorizontalProgresNoNum) findViewById(R.id.horizontalProgress01);


        horizontalProgress1 = (CustomHorizontalProgresWithNum) findViewById(R.id.horizontalProgress1);
        horizontalProgress2 = (CustomHorizontalProgresWithNum) findViewById(R.id.horizontalProgress2);
        horizontalProgress3 = (CustomHorizontalProgresWithNum) findViewById(R.id.horizontalProgress3);

        horizontalProgress1.setProgress(0);
        horizontalProgress1.setMax(100);

        horizontalProgress2.setProgress(0);
        horizontalProgress2.setMax(100);

        horizontalProgress3.setProgress(0);
        horizontalProgress3.setMax(200);

        //Circle progress with num
        mRoundlProgresWithNum31 = (RoundlProgresWithNum) findViewById(R.id.mRoundlProgresWithNum31);
        mRoundlProgresWithNum31.setProgress(0);
        mRoundlProgresWithNum31.setMax(100);

        //Circle progress no num
        mRoundlProgresWithNum32 = (RoundlProgresWithNum) findViewById(R.id.mRoundlProgresWithNum32);
        mRoundlProgresWithNum32.setProgress(0);
        mRoundlProgresWithNum32.setMax(100);

        mRoundlProgresWithNum33 = (RoundlProgresWithNum) findViewById(R.id.mRoundlProgresWithNum33);
        mRoundlProgresWithNum33.setProgress(0);
        mRoundlProgresWithNum33.setMax(100);

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //实时更新进度
                if (horizontalProgress1.getProgress() >= 100) {//指定时间取消
                    timer.cancel();
                }
                horizontalProgress1.setProgress(horizontalProgress1.getProgress()+1);
                horizontalProgress01.setProgress(horizontalProgress01.getProgress()+1);

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

        timer31 = new Timer();
        timer31.schedule(new TimerTask() {
            @Override
            public void run() {
                //实时更新进度
                if (mRoundlProgresWithNum31.getProgress() >= mRoundlProgresWithNum31.getMax()) {//指定时间取消
                    timer31.cancel();
                }
                mRoundlProgresWithNum31.setProgress(mRoundlProgresWithNum31.getProgress()+1);

            }
        }, 40, 40);

        timer32 = new Timer();
        timer32.schedule(new TimerTask() {
            @Override
            public void run() {
                //实时更新进度
                if (mRoundlProgresWithNum32.getProgress() >= mRoundlProgresWithNum32.getMax()) {//指定时间取消
                    timer32.cancel();
                }
                mRoundlProgresWithNum32.setProgress(mRoundlProgresWithNum32.getProgress()+1);

            }
        }, 30, 30);

        timer33 = new Timer();
        timer33.schedule(new TimerTask() {
            @Override
            public void run() {
                //实时更新进度
                if (mRoundlProgresWithNum33.getProgress() >= mRoundlProgresWithNum33.getMax()) {//指定时间取消
                    timer33.cancel();
                }
                mRoundlProgresWithNum33.setProgress(mRoundlProgresWithNum33.getProgress()+1);

            }
        }, 20, 20);


    }


}

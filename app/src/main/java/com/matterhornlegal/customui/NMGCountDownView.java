package com.matterhornlegal.customui;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.matterhornlegal.R;

import org.joda.time.Period;
import org.joda.time.PeriodType;

/**
 * Created by karan.kalsi on 10/9/2017.
 */

public class NMGCountDownView extends RelativeLayout {
    public NMGCountDownView(Context context) {
        super(context);
        init();

    }

    public NMGCountDownView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NMGCountDownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private TextView counterDaysT;
    private TextView counterDaysO;
    private TextView counterHoursT;
    private TextView counterHoursO;
    private TextView counterMinsT;
    private TextView counterMinsO;
    private TextView counterSecsT;
    private TextView counterSecsO;

    private void init() {
        inflate(getContext(), R.layout.count_down_view, this);

        counterDaysT = (TextView) findViewById(R.id.counter_days_t);
        counterDaysO = (TextView) findViewById(R.id.counter_days_o);
        counterHoursT = (TextView) findViewById(R.id.counter_hours_t);
        counterHoursO = (TextView) findViewById(R.id.counter_hours_o);
        counterMinsT = (TextView) findViewById(R.id.counter_mins_t);
        counterMinsO = (TextView) findViewById(R.id.counter_mins_o);
        counterSecsT = (TextView) findViewById(R.id.counter_secs_t);
        counterSecsO = (TextView) findViewById(R.id.counter_secs_o);
    }

    private  CountDownTimer countDownTimer=null;

    public void startTimer(final long time_left) {
        if (countDownTimer!=null){
            countDownTimer.cancel();
            countDownTimer=null;
        }
         countDownTimer = new CountDownTimer(time_left, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Period period = new Period(millisUntilFinished, PeriodType.dayTime());


                int hours = period.getHours();
                int days = hours / 24;
                hours = hours % 24;
                int minutes = period.getMinutes();
                int seconds = period.getSeconds();
                counterDaysO.setText(String.valueOf(days % 10));
                counterDaysT.setText(String.valueOf((days / 10) % 10));
                counterHoursO.setText(String.valueOf(hours % 10));
                counterHoursT.setText(String.valueOf((hours / 10) % 10));
                counterMinsO.setText(String.valueOf(minutes % 10));
                counterMinsT.setText(String.valueOf((minutes / 10) % 10));
                counterSecsO.setText(String.valueOf(seconds % 10));
                counterSecsT.setText(String.valueOf((seconds / 10) % 10));
            }

            @Override
            public void onFinish() {

            }
        };

        countDownTimer.start();
    }


}

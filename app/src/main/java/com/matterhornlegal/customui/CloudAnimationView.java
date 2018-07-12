package com.matterhornlegal.customui;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.animation.CycleInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;

import com.matterhornlegal.R;
import com.matterhornlegal.databinding.CloudAnimationViewBinding;

/**
 * Created by karan.kalsi on 11/2/2017.
 */



public class CloudAnimationView extends RelativeLayout {
private CloudAnimationViewBinding binding;
    public CloudAnimationView(Context context) {
        super(context);
        init();
    }

    public CloudAnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CloudAnimationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.cloud_animation_view, this);
        binding= DataBindingUtil.bind(getChildAt(0));
        binding.cloud1Back.post(new Runnable() {
            @Override
            public void run() {
            binding.cloud1Back.setY(binding.cloudContainer.getHeight()/2);
            }
        });
        binding.cloud1Back.post(new Runnable() {
            @Override
            public void run() {

               binding.cloud3Back.setX(binding.cloudContainer.getWidth());
                binding.cloud3Back.setY(0);

                binding.cloud1Back.setY(binding.cloudContainer.getHeight()/3);
                binding.cloud1Back.setX(-binding.cloud1Back.getWidth());
                ObjectAnimator animator1 = ObjectAnimator.ofFloat(binding.cloud1Back,TRANSLATION_X,binding.cloudContainer.getWidth());
                animator1.setDuration(1*60*1000);
                animator1.setRepeatMode(ValueAnimator.RESTART);
                animator1.setInterpolator(new LinearInterpolator());
                animator1.setRepeatCount(-1);
                animator1.start();




                binding.cloud2Front.setY(binding.cloudContainer.getHeight()/2);
                binding.cloud2Front.setX(binding.cloudContainer.getWidth());

                ObjectAnimator animator2 = ObjectAnimator.ofFloat(binding.cloud2Front,TRANSLATION_X,-binding.cloud2Front.getWidth());
                animator2.setDuration(30*1000);
                animator2.setRepeatMode(ValueAnimator.RESTART);
                animator2.setInterpolator(new LinearInterpolator());
                animator2.setRepeatCount(-1);
                animator2.start();



                binding.cloud3Back.setY(binding.cloudContainer.getHeight()/3);
                binding.cloud3Back.setX(-2.5f*binding.cloud3Back.getWidth());
                ObjectAnimator animator3 = ObjectAnimator.ofFloat(binding.cloud3Back,TRANSLATION_X,binding.cloudContainer.getWidth());
                animator3.setDuration(1*60*1000+(20*1000));
                animator3.setRepeatMode(ValueAnimator.RESTART);
                animator3.setInterpolator(new LinearInterpolator());
                animator3.setRepeatCount(-1);
                animator3.start();

                binding.cloud4Back.setY(binding.cloudContainer.getHeight()/3);
                binding.cloud4Back.setX(-2.5f*binding.cloud4Back.getWidth());
                ObjectAnimator animator4 = ObjectAnimator.ofFloat(binding.cloud4Back,TRANSLATION_X,binding.cloudContainer.getWidth());
                animator4.setDuration(1*60*1000+(20*1000));
                animator4.setRepeatMode(ValueAnimator.RESTART);
                animator4.setInterpolator(new LinearInterpolator());
                animator4.setRepeatCount(-1);

                animator4.setCurrentPlayTime(animator4.getDuration()*2/3);
                animator4.start();

                binding.cloud5Front.setY(binding.cloudContainer.getHeight()/2);
                binding.cloud5Front.setX(binding.cloudContainer.getWidth());

                ObjectAnimator animator5 = ObjectAnimator.ofFloat(binding.cloud5Front,TRANSLATION_X,-binding.cloud5Front.getWidth());
                animator5.setDuration(30*1000);
                animator5.setRepeatMode(ValueAnimator.RESTART);
                animator5.setInterpolator(new LinearInterpolator());
                animator5.setRepeatCount(-1);
                animator5.setCurrentPlayTime(animator5.getDuration()*1/2);
                animator5.start();

            }
        });
    }
}

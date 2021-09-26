package com.yoon.mystock;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.yoon.mystock.databinding.ActivityLandingBinding;

import timber.log.Timber;

public class ActivityLanding extends AppCompatActivity {
    private ActivityLanding This = this;
    private ActivityLandingBinding mBinding;
    private int mCnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(This, R.layout.activity_landing);
        Timber.plant(new Timber.DebugTree());
        Handler delayHandler = new Handler();

        delayHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 액티비티 여러 번 불러오는것 방지
                if (++mCnt > 2) {
                    Timber.tag("checkCheck").d("return");
                    return;
                }
                Timber.tag("checkCheck").d("mCnt : %s", mCnt);
                Timber.tag("checkCheck").d("goToActivityMain");
                goToActivityMain();
            }
        }, 2000);

        final AnimationDrawable mmAnimation = (AnimationDrawable) mBinding.frogImage.getBackground();
        mBinding.frogImage.post(new Runnable() {
            @Override
            public void run() {
                mmAnimation.start();
            }
        });

        // 종료
        if (getIntent().getBooleanExtra(Key.EVENT_APP_EXIT, false)) {
            this.finishAndRemoveTask();
            System.exit(0);
        }
    }

    private void goToActivityMain() {
        Intent mmIntent = new Intent(This, ActivityMain.class);
        startActivity(mmIntent);
    }
}
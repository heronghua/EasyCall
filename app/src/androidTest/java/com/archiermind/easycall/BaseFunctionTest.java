package com.archiermind.easycall;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiSelector;
import org.junit.Before;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class BaseFunctionTest {

    protected UiDevice mUiDevice;
    protected Instrumentation mInstrumentation;
    protected Context mTargetContext;
    protected Context mContext;

    @Before
    public void setUp(){
        mInstrumentation = InstrumentationRegistry.getInstrumentation();
        mTargetContext = mInstrumentation.getTargetContext();
        mUiDevice = UiDevice.getInstance(mInstrumentation);
        mContext = mInstrumentation.getContext();

        // 启动测试App
        Intent intent = mContext.getPackageManager().getLaunchIntentForPackage(mTargetContext.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mContext.startActivity(intent);
    }

    protected boolean findUiObjectDuring(UiSelector selector,int seconds){
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() < startTime + seconds * 1000){
            if (selector != null){
                return true;
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

}

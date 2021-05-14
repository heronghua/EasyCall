package com.archiermind.easycall;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RunWith(AndroidJUnit4.class)
public class BaseFunctionTest implements Thread.UncaughtExceptionHandler {

    protected static final boolean DEBUG = true;

    protected String TAG;

    protected UiDevice mUiDevice;
    protected Instrumentation mInstrumentation;
    protected Context mTargetContext;
    protected Context mContext;
    private static final String DUMP_FILE_PATH = "/sdcard/AT_DUMP/";

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

        TAG = this.getClass().getSimpleName();
        Thread.setDefaultUncaughtExceptionHandler(this);

    }

    private void dumpWindowHierarchy(){
        Log.i(TAG, "[dumpWindowHierarchy] +");
        File folder = new File(DUMP_FILE_PATH);
        if (!folder.exists()){
            folder.mkdirs();
        }
        String fileName = new SimpleDateFormat("yyyy-MM-DD-hh_mm_ss").format(new Date());
        File dumpFile = new File(folder.getAbsolutePath() + fileName +".hierachy");
        try {
            mUiDevice.dumpWindowHierarchy(dumpFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "[dumpWindowHierarchy] -");
    }

    /**
     *
     * @param selector  the object selector which you want to find on this page
     * @param seconds seconds during check
     * @return true if you find selector on current page
     */
    protected boolean findUiObjectDuring(UiSelector selector,int seconds){
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() <= startTime + seconds * 1000){
            if (mUiDevice.findObject(selector).exists()){
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

    @After
    public void tearDown(){
        mUiDevice.pressBack();
    }

    /** Provide an long press method for uiObject*/
	public void longPress(UiSelector selector) throws UiObjectNotFoundException {
        UiObject uiObject = mUiDevice.findObject(selector);
        Rect bounds = uiObject.getBounds();
        int x = bounds.centerX();
        int y = bounds.centerY();
        mUiDevice.swipe(x, y, x, y, 300);
    }


    @Override
    public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
        dumpWindowHierarchy();
    }
}

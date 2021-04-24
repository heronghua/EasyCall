package com.archiermind.easycall;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
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

@RunWith(AndroidJUnit4.class)
public class BaseFunctionTest {

    protected static final boolean DEBUG = true;

    protected String TAG;

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

        TAG = this.getClass().getSimpleName();
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

        // could not find object during #seconds , we should dump hierarchy to check
        try {
            mUiDevice.dumpWindowHierarchy(new File("/sdcard/AT_DUMP/"+selector.hashCode()));
        } catch (IOException e) {
            e.printStackTrace();
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


}

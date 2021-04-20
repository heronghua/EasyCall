package com.archiermind.easycall;

import android.app.Instrumentation;
import android.content.Context;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import androidx.test.uiautomator.UiDevice;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class BasicFunctionInstrumentedTest {

    private UiDevice mUiDevice;
    private Instrumentation mInstrumentation;
    private Context mTargetContext;

    @Before
    public void setUp(){
        mInstrumentation = InstrumentationRegistry.getInstrumentation();
        mTargetContext = mInstrumentation.getTargetContext();
        mUiDevice = UiDevice.getInstance(mInstrumentation);
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        assertEquals("com.archiermind.easycall", mTargetContext.getPackageName());
    }

    @Test
    public void testAddContact(){

        mUiDevice.pressHome();



    }

}

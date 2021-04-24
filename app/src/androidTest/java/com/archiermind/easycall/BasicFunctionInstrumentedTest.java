package com.archiermind.easycall;

import android.graphics.Rect;
import android.util.Log;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class BasicFunctionInstrumentedTest extends BaseFunctionTest{

    @Test
    public void testAddContact() throws IOException, UiObjectNotFoundException {
        UiObject grid = mUiDevice.findObject(new UiSelector().description("grid"));
        Rect bounds = grid.getBounds();
        mUiDevice.executeShellCommand("input tap " + bounds.right /2+ " " + bounds.bottom/2);
        mUiDevice.executeShellCommand("input tap " + bounds.right/2 + " " + bounds.bottom/2);
        Log.i(TAG,"[testAddContact] bounds.right/2 " + bounds.right/2);
        boolean enterNextPageSuccess = findUiObjectDuring(new UiSelector().text("获取照片"), 5);
        assertTrue(enterNextPageSuccess);

    }

    @Test
    public void testPhoneCall() throws UiObjectNotFoundException {
        UiObject grid = mUiDevice.findObject(new UiSelector().description("grid"));
        UiSelector phoneNumItemSelector = new UiSelector().index(0);
        UiObject itemUiObject = grid.getChild(phoneNumItemSelector);

        UiObject phoneNumTextView = itemUiObject.getChild(new UiSelector().description("phoneNum"));
        String phoneNum = phoneNumTextView.getText();
        assertNotNull(phoneNum);
        assertNotEquals("",phoneNum);

        itemUiObject.clickAndWaitForNewWindow();

        /*String currentPackageName = mUiDevice.getCurrentPackageName();
        Log.i(TAG,"testPhoneCall pkgName" + currentPackageName);
        assertEquals("com.android.incallui", currentPackageName);*/

        findUiObjectDuring(new UiSelector().text(phoneNum),0);

    }

}

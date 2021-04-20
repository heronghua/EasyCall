package com.archiermind.easycall;

import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class BasicFunctionInstrumentedTest extends BaseFunctionTest{

    @Test
    public void testAddContact() throws UiObjectNotFoundException {

        UiObject grid = mUiDevice.findObject(new UiSelector().description("grid"));
        //TODO how to perform double click
        grid.click();
        grid.clickAndWaitForNewWindow();

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

        assertTrue(findUiObjectDuring(new UiSelector().text(phoneNum),5));

    }

}

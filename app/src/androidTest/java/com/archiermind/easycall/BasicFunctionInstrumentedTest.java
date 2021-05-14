package com.archiermind.easycall;

import android.graphics.Rect;
import android.util.Log;
import androidx.test.uiautomator.*;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BasicFunctionInstrumentedTest extends BaseFunctionTest{

    /** The phone num you want to type for test .*/
    private static final String TEMP_PHONE_NUM1 = "987654321";
    private static final String TEMP_PHONE_NUM2 = "9876543219";
    private static final String CALLER_PKG_NAME = "com.android.incallui";;

    @Test(timeout = 20000)
    public void testAddContactByCamera() throws IOException, UiObjectNotFoundException {
        UiObject grid = mUiDevice.findObject(new UiSelector().description("grid"));
        Rect bounds = grid.getBounds();
        mUiDevice.executeShellCommand("input tap " + bounds.right /2+ " " + bounds.bottom/2);
        mUiDevice.executeShellCommand("input tap " + bounds.right/2 + " " + bounds.bottom/2);
        Log.i(TAG,"[testAddContact] bounds.right/2 " + bounds.right/2);
        boolean enterNextPageSuccess = findUiObjectDuring(new UiSelector().text("获取照片1"), 1);
        assertTrue(enterNextPageSuccess);

        //click take picture
        UiObject takePictureObject = mUiDevice.findObject(new UiSelector().text("拍照"));
        takePictureObject.clickAndWaitForNewWindow();

        mUiDevice.executeShellCommand("input keyevent KEYCODE_CAMERA");
        UiObject doneButtonUiObject = mUiDevice.findObject(new UiSelector().resourceIdMatches(".+done_button"));
        doneButtonUiObject.click();

        cropFlowCheck(TEMP_PHONE_NUM1);
    }

    @Test(timeout = 20000)
    public void testAddContactByPicture() throws IOException, UiObjectNotFoundException {
        UiObject grid = mUiDevice.findObject(new UiSelector().description("grid"));
        Rect bounds = grid.getBounds();
        mUiDevice.executeShellCommand("input tap " + bounds.right /2+ " " + bounds.bottom/2);
        mUiDevice.executeShellCommand("input tap " + bounds.right/2 + " " + bounds.bottom/2);
        Log.i(TAG,"[testAddContact] bounds.right/2 " + bounds.right/2);
        boolean enterNextPageSuccess = findUiObjectDuring(new UiSelector().text("获取照片"), 1);
        assertTrue(enterNextPageSuccess);

        UiObject choosePictureObject = mUiDevice.findObject(new UiSelector().text("相册"));
        choosePictureObject.clickAndWaitForNewWindow();

        UiObject cameraObj = mUiDevice.findObject(new UiSelector().text("相机"));
        cameraObj.clickAndWaitForNewWindow();

        UiObject firstJpg = mUiDevice.findObject(new UiSelector().classNameMatches(".+ImageView$"));
        firstJpg.clickAndWaitForNewWindow();

        UiObject selectRightButtonUiObject = mUiDevice.findObject(new UiSelector().resourceIdMatches(".+head_select_right"));
        selectRightButtonUiObject.clickAndWaitForNewWindow();

        cropFlowCheck(TEMP_PHONE_NUM2);

    }

    private void cropFlowCheck(String phoneNum) throws UiObjectNotFoundException {
        //check crop page is shown  com.android.gallery3d:id/head_select_right
        UiObject selectRightButtonUiObject = mUiDevice.findObject(new UiSelector().resourceIdMatches(".+head_select_right"));
        selectRightButtonUiObject.clickAndWaitForNewWindow();

        UiObject numInputUiObject = mUiDevice.findObject(new UiSelector().description("inputNum"));
        numInputUiObject.setText(phoneNum);

        UiObject confirmUiObject = mUiDevice.findObject(new UiSelector().text("确定"));
        confirmUiObject.clickAndWaitForNewWindow();

        assertTrue(findUiObjectDuring(new UiSelector().text(phoneNum),2));
    }

    @Test(timeout = 20000)
    public void testCall() throws UiObjectNotFoundException, IOException {
        UiObject grid = mUiDevice.findObject(new UiSelector().description("grid"));
        UiSelector phoneNumItemSelector = new UiSelector().index(0);
        UiObject itemUiObject = grid.getChild(phoneNumItemSelector);

        UiObject phoneNumTextView = itemUiObject.getChild(new UiSelector().description("phoneNum"));
        String phoneNum = phoneNumTextView.getText();
        assertNotNull(phoneNum);
        assertNotEquals("",phoneNum);

        itemUiObject.clickAndWaitForNewWindow();

        String currentPackageName = mUiDevice.getCurrentPackageName();
        Log.i(TAG,"testPhoneCall pkgName" + currentPackageName);

        assertEquals(CALLER_PKG_NAME, currentPackageName);
        mUiDevice.executeShellCommand("am force-stop " + currentPackageName);



    }

    @Test(timeout = 20000)
    public void testDeletePhoneNum() throws UiObjectNotFoundException {

        UiSelector phoneNumSelector = new UiSelector().text(TEMP_PHONE_NUM1);
        longPress(phoneNumSelector);

        UiObject confirmUiObject = mUiDevice.findObject(new UiSelector().text("确定"));
        confirmUiObject.clickAndWaitForNewWindow();

        assertFalse(findUiObjectDuring(phoneNumSelector,5));

    }

}

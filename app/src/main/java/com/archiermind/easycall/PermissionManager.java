package com.archiermind.easycall;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class PermissionManager {

    private static final String TAG = PermissionManager.class.getSimpleName();

    public static final String[] BASIC_PERMISSIONS = new String[]{
           // Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CALL_PHONE};

    public static final String[] OPTIONAL_PERMISSIONS = new String[]{Manifest.permission.CAMERA};

    public static boolean needRequestBasicPermission(Activity activity){
        return needRequestPermission(activity,BASIC_PERMISSIONS);
    }

    public static boolean needRequestOptionalPermission(Activity activity){
        return needRequestPermission(activity,OPTIONAL_PERMISSIONS);
    }

    private static boolean needRequestPermission(Activity activity,String[] permissions){
        Log.i(TAG, "[needRequestPermission] +");

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            Log.i(TAG, "[needRequestPermission] current platform version is "+Build.VERSION.SDK_INT);
            return false;
        }

        for (int i = 0; i < permissions.length; i++) {
            int hasPermission = ContextCompat.checkSelfPermission(activity, permissions[i]);
            Log.i(TAG, "[needRequestPermission] permission "+permissions[i] +" grant == "+hasPermission);
            if (hasPermission != PackageManager.PERMISSION_GRANTED){
                return true;
            }
        }
        Log.i(TAG, "[needRequestPermission] -");
        return false;

    }

    public static ArrayList<String> deniedPermissionArray(Activity activity,String[] permissions){
        Log.i(TAG, "[deniedPermissionArray] +");
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            Log.i(TAG, "[instance deniedPermissionArray] current platform version is "+Build.VERSION.SDK_INT
                +" no need to request permission");
            return null;
        }

        ArrayList<String> deniedPermissionArrays =new ArrayList<>() ;
        for (int i = 0; i < permissions.length; i++) {
            int hasPermission = ContextCompat.checkSelfPermission(activity, permissions[i]);
            if (hasPermission != PackageManager.PERMISSION_GRANTED){
                deniedPermissionArrays.add(permissions[i]);
            }
        }
        Log.i(TAG, "[deniedPermissionArray] -");
        return deniedPermissionArrays;
    }

    public static int requestPermissions(Activity activity,ArrayList<String> permissions){
        Log.i(TAG, "[requestPermissions] +");

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            Log.i(TAG, "[instance deniedPermissionArray] current platform version is "+Build.VERSION.SDK_INT
                    +" no need to request permission");
            return 0;
        }

        int requestCode = (int) (Math.random()*1000);
        activity.requestPermissions(permissions.toArray(new String[0]),requestCode);

        Log.i(TAG, "[requestPermissions] -");
        return requestCode;

    }



    public static class IntentSettingCompat{

        public static void guidToPermissionSetting(Activity activity){

            Log.i(TAG, "[guidToPermissionSetting] brand : "+Build.BRAND);
            if (Build.BRAND.contains("HUAWEI")){
                try {
                    Intent intent = new Intent(activity.getPackageName());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity");
                    intent.setComponent(comp);
                    activity.startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(activity, "permission setting guid fail", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                    goToApplicationDetailsInfoPage(activity);
                }
            }

            
        }

        public static void goToApplicationDetailsInfoPage(Activity activity) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
            intent.setData(uri);
            try {
                activity.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}

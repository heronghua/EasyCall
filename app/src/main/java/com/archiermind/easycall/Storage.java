package com.archiermind.easycall;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;

import java.io.File;
import java.io.IOException;

public class Storage {

    private static final boolean DEBUG = false;

    public static final String EXTERNAL_PRIVATE_CONTACTS_PATH ="contacts";

    public static String getExternalPrivateCachePath(Context context){
        File dir = context.getExternalCacheDir();
        return dir.getAbsolutePath();
    }

    public static String getInternalPrivateCachePath(Context context){
        File cacheDir = context.getCacheDir();
        return cacheDir.getAbsolutePath();
    }

    public static String getInternalPrivatePath(Context context){
        File cacheDir = context.getDir("contacts",Context.MODE_PRIVATE);
        return cacheDir.getAbsolutePath();
    }

    public static String getExternalPrivatePath(Context context){
        File dir = context.getExternalFilesDir(EXTERNAL_PRIVATE_CONTACTS_PATH);

        if (DEBUG){
            File file = new File(dir.getAbsolutePath(),"testFile.jpg");
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return dir.getAbsolutePath();
    }

    public static File createSharedTempFile(Context context){
        String imgNameOri = "Temp.tmp";
        return new File(getExternalPrivateCachePath(context),imgNameOri);
    }

    public static Uri createFinalImageFile(Context context){
        String imgNameOri = "Final.tmp";
        Uri uri =null;
        File image = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            image =new File( getExternalPrivateCachePath(context),imgNameOri);

            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DATA,image.getAbsolutePath());
            values.put(MediaStore.Images.Media.DISPLAY_NAME,imgNameOri);
            values.put(MediaStore.Images.Media.MIME_TYPE,"image/jpeg");

            uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        }else {
            image = new File(getExternalPrivateCachePath(context),imgNameOri);
            uri = Uri.fromFile(image);
        }

        return uri;
    }

    public static void renameTmpFileTo(Context context,Editable phoneNum, Handler mMainHandler) throws IllegalAccessException {
        File file = new File(getExternalPrivateCachePath(context),"Final.tmp");
        File phoneNumFile = new File(getInternalPrivatePath(context),phoneNum+".jpg");
        boolean success = file.renameTo(phoneNumFile);
        if (!success){
            throw new IllegalAccessException(" can not move file from " + file.getAbsolutePath() +" to " +phoneNumFile.getAbsolutePath());
        }

        mMainHandler.obtainMessage(0x25).sendToTarget();
    }
}

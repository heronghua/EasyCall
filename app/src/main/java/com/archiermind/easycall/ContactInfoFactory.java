package com.archiermind.easycall;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;

import java.io.File;
import java.util.ArrayList;

public class ContactInfoFactory {

    private static final String LOCK="ContactInfoFactoryLock";

    private static ContactInfoFactory instance;

    private Handler mParseHandler;

    private ContactInfoFactory(){
        HandlerThread parseThread = new HandlerThread("ParseThread");
        parseThread.start();
        mParseHandler = new Handler(parseThread.getLooper());
    }

    public static ContactInfoFactory getInstance() {
        if (instance == null){
            synchronized (LOCK){
                if (instance == null){
                    instance = new ContactInfoFactory();
                }
            }
        }
        return instance;
    }


    public void create(final Context context, final Handler mainHandler, final boolean refresh){

        mParseHandler.post(new Runnable() {
            @Override
            public void run() {
                ArrayList<ContactInfo> contactInfos = new ArrayList<>();

                String cachePath = Storage.getInternalPrivatePath(context);
                File dir = new File(cachePath);
                String[] files = dir.list();
                int len = files.length;
                for (int i = 0; i < len; i++) {
                    String fileName = files[i];
                    File fileI = new File(dir.getAbsolutePath(),fileName);

                    ContactInfo contactInfo = new ContactInfo();
                    String phoneNum = fileName.substring(0,fileName.indexOf('.'));
                    contactInfo.setPhoneNum(phoneNum);
                    contactInfo.setPhotoUri(Uri.fromFile(fileI).toString());
                    if (fileName.endsWith("jpg")){
                        contactInfos.add(contactInfo);
                    }
                }

                mainHandler.obtainMessage(0x123,contactInfos).sendToTarget();

            }
        });



    }


}

package com.archiermind.easycall;

import android.content.Intent;
import android.graphics.Bitmap;

public class CropIntent extends Intent {


    public CropIntent(String s) {
        super(s);
    }

    public static final CropIntent getDefault(){
        CropIntent cropIntent = new CropIntent("com.android.camera.action.CROP");

        cropIntent.putExtra("crop", "true");
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        cropIntent.putExtra("outputX", 320);
        cropIntent.putExtra("outputY", 320);
        cropIntent.putExtra("scale", true);
        cropIntent.putExtra("return-data", false);

        cropIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION|Intent.FLAG_GRANT_READ_URI_PERMISSION);

        // output format
        cropIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        return cropIntent;
    }

}

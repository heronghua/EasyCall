package com.archiermind.easycall;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.*;
import android.provider.MediaStore;
import android.text.Editable;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements Handler.Callback, View.OnTouchListener, AdapterView.OnItemClickListener, DialogInterface.OnClickListener, AdapterView.OnItemLongClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int REQUEST_CODE_CAMERA = 0X46;
    private static final int CAMERA_RESULT_CODE = 0x298;
    private static final int ALBUM_RESULT_CODE = 0x569;
    private static final int CROP_RESULT_CODE = 0x678;

    private GridView mGridView;
    private ContactAdapter mAdapter;
    private Handler mMainHandler, mWorkHandler;
    private GestureDetector mGestureDetector;
    private AlertDialog mChooseDialog,mSaveDialog;
    private int mBasicPermissionRequestCode;
    private int mOptionalPermissionRequestCode;
    private Uri mCaptureImageUri;
    private Uri mCropImageUri;
    private EditText mPhoneNumEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainHandler = new Handler(Looper.getMainLooper(),this);
        HandlerThread workThread = new HandlerThread("WorkThread");
        workThread.start();
        mWorkHandler = new Handler(workThread.getLooper());
        mGridView = new GridView(this);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mGridView.setLayoutParams(layoutParams);
        mGridView.setNumColumns(2);
        mAdapter = new ContactAdapter(this);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(this);
        setContentView(mGridView);

        doParseInBackground();

        mGestureDetector = new GestureDetector(this,new GestureMonitor());
        mGridView.setOnTouchListener(this);
        mGridView.setOnItemLongClickListener(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(R.string.gain_picture);
        builder.setItems(getResources().getStringArray(R.array.choices), this);
        builder.setNegativeButton(R.string.cancel,null);
        mChooseDialog = builder.create();

        AlertDialog.Builder savingBuilder = new AlertDialog.Builder(this);
        savingBuilder.setCancelable(true);
        savingBuilder.setTitle(R.string.set_num);
        savingBuilder.setPositiveButton(R.string.confirm,mSavingDialogListener);
        mPhoneNumEdit = (EditText) LayoutInflater.from(this).inflate(R.layout.item_editable, null, true);
        savingBuilder.setView(mPhoneNumEdit);
        mSaveDialog = savingBuilder.create();

    }

    private DialogInterface.OnClickListener mSavingDialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            final Editable phoneNum = mPhoneNumEdit.getText();
            mWorkHandler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        Storage.renameTmpFileTo(MainActivity.this, phoneNum,mMainHandler);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        boolean needRequestBasicPermission = PermissionManager.needRequestBasicPermission(this);
        if (needRequestBasicPermission){
            ArrayList<String> deniedPermissionArray = PermissionManager.deniedPermissionArray(this, PermissionManager.BASIC_PERMISSIONS);
            mBasicPermissionRequestCode= PermissionManager.requestPermissions(this, deniedPermissionArray);
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which==0){
            openAlbum();

        }else if (which==1){
            boolean needRequestOptionalPermission = PermissionManager.needRequestOptionalPermission(this);
            if (needRequestOptionalPermission){
                ArrayList<String> deniedPermissionArray = PermissionManager.deniedPermissionArray(this, PermissionManager.OPTIONAL_PERMISSIONS);
                mOptionalPermissionRequestCode= PermissionManager.requestPermissions(this, deniedPermissionArray);
                return;
            }

            capture();

        }

        mChooseDialog.dismiss();

    }

    private void doParseInBackground() {
        ContactInfoFactory.getInstance().create(this,mMainHandler,false);

    }


    @Override
    public boolean handleMessage(@NonNull Message msg) {
        switch (msg.what) {
            case 0x123:
                mAdapter.refresh((List<ContactInfo>) msg.obj);
                break;
            case 0x25:
                doParseInBackground();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ContactInfo item = (ContactInfo) parent.getAdapter().getItem(position);
        Uri phoneUri =Uri.parse( "tel:" + item.getPhoneNum().trim());
        Intent intent=new Intent(Intent.ACTION_CALL, phoneUri);
        startActivity(intent);
    }

    @Override
    public boolean onTouch(View v,MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.conirm_delet);
        builder.setNegativeButton(R.string.cancel,null);
        ContactInfo item = (ContactInfo) parent.getAdapter().getItem(position);
        Uri uri = Uri.parse(item.getPhotoUri());
        final File file = new File(uri.getPath());
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (file.exists()){
                    file.delete();
                    doParseInBackground();
                }
            }
        });

        builder.create().show();

        return true;
    }

    private class GestureMonitor extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            mChooseDialog.show();
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ArrayList<String> deniedPermissionArray = PermissionManager.deniedPermissionArray(this, permissions);

        if (mBasicPermissionRequestCode == requestCode) {
            if (deniedPermissionArray!=null&&deniedPermissionArray.size()>0) {
                Toast.makeText(this,"You must allow permission from system setting ,or you cannot use this app.",Toast.LENGTH_LONG).show();
                PermissionManager.IntentSettingCompat.goToApplicationDetailsInfoPage(this);
                finish();
            }

        }

        if (mOptionalPermissionRequestCode == requestCode){
            if (deniedPermissionArray!=null&&deniedPermissionArray.size()>0) {
                capture();
            }
        }
    }

    private void capture() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        cameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        File captureFile = Storage.createSharedTempFile(this);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            mCaptureImageUri = Uri.fromFile(captureFile);
        } else {
            mCaptureImageUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", captureFile);
        }
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCaptureImageUri);
        startActivityForResult(cameraIntent, CAMERA_RESULT_CODE);

    }

    private void openAlbum() {
        Intent albumIntent = new Intent(Intent.ACTION_PICK);
        albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(albumIntent, ALBUM_RESULT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, String.format("[onActivityResult] requestCode requestCode %d,resultCode %d,data %s",requestCode,resultCode,data));
        if (resultCode == Activity.RESULT_OK){
            switch (requestCode){

                case ALBUM_RESULT_CODE:
                    Uri uri = data.getData();
                    cropImage(uri);
                    break;

                case CAMERA_RESULT_CODE:
                    cropImage(mCaptureImageUri);
                    break;

                case CROP_RESULT_CODE:
                    mSaveDialog.show();
                    break;

                default:
                    break;
            }
        }
    }

    //If uri is null that means crop from camera ,use mCaptureImageFile
    private void cropImage(Uri uri) {
        CropIntent cropIntent = CropIntent.getDefault();
        cropIntent.setDataAndType(uri,"image/*");
        mCropImageUri = Storage.createFinalImageFile(this);
        cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCropImageUri);
        startActivityForResult(cropIntent, CROP_RESULT_CODE);
    }
}

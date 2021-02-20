package com.archiermind.easycall;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends BaseAdapter {

    private List<ContactInfo> mPhoneNums;
    private ImageLoader mImageLoader = null;

    public ContactAdapter(Context context) {
        mPhoneNums = new ArrayList<>();
        mImageLoader = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).build();
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(context)
                .defaultDisplayImageOptions(options)
                .build();
        mImageLoader.init(configuration);

    }


    @Override
    public int getCount() {
        return mPhoneNums.size();
    }

    @Override
    public ContactInfo getItem(int position) {
        return mPhoneNums.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact,null,false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }


        ContactInfo contactInfo = getItem(position);
        viewHolder.mPhone.setText(contactInfo.getPhoneNum());
        mImageLoader.displayImage(contactInfo.getPhotoUri(),viewHolder.mPhoto);

        return convertView;
    }

    private static class ViewHolder{
        ImageView mPhoto;
        TextView mPhone;

        public ViewHolder(View convertView) {
            mPhoto = convertView.findViewById(R.id.iv_photo);
            mPhone = convertView.findViewById(R.id.tv_phone);
        }
    }

    public void refresh(List<ContactInfo> data){
        this.mPhoneNums.clear();
        this.mPhoneNums .addAll(data);
        notifyDataSetChanged();
    }

}

package com.archiermind.easycall;

public class ContactInfo {

    private String photoUri;
    private String phoneNum;

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    @Override
    public String toString() {
        return "ContactInfo{" +
                "photoUri='" + photoUri + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                '}';
    }
}

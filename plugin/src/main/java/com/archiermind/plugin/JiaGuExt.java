package com.archiermind.plugin;

public class JiaGuExt {

    private String userName;

    private String userPwd;

    private String keyStorePath;

    private String keyStorePass;

    private String keyStoreAlias;

    private String getKeyStoreAliasPwd;

    private String jiaguToolPath;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getKeyStorePath() {
        return keyStorePath;
    }

    public void setKeyStorePath(String keyStorePath) {
        this.keyStorePath = keyStorePath;
    }

    public String getKeyStorePass() {
        return keyStorePass;
    }

    public void setKeyStorePass(String keyStorePass) {
        this.keyStorePass = keyStorePass;
    }

    public String getKeyStoreAlias() {
        return keyStoreAlias;
    }

    public void setKeyStoreAlias(String keyStoreAlias) {
        this.keyStoreAlias = keyStoreAlias;
    }

    public String getGetKeyStoreAliasPwd() {
        return getKeyStoreAliasPwd;
    }

    public void setGetKeyStoreAliasPwd(String getKeyStoreAliasPwd) {
        this.getKeyStoreAliasPwd = getKeyStoreAliasPwd;
    }

    public String getJiaguToolPath() {
        return jiaguToolPath;
    }

    public void setJiaguToolPath(String jiaguToolPath) {
        this.jiaguToolPath = jiaguToolPath;
    }

    @Override
    public String toString() {
        return "JiaGuExt{" +
                "userName='" + userName + '\'' +
                ", userPwd='" + userPwd + '\'' +
                ", keyStorePath='" + keyStorePath + '\'' +
                ", keyStorePass='" + keyStorePass + '\'' +
                ", keyStoreAlias='" + keyStoreAlias + '\'' +
                ", getKeyStoreAliasPwd='" + getKeyStoreAliasPwd + '\'' +
                ", jiaguToolPath='" + jiaguToolPath + '\'' +
                '}';
    }
}

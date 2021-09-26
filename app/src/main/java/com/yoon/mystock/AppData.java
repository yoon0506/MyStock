package com.yoon.mystock;

public class AppData {

    private static AppData mInstance = new AppData();
    public static AppData GetInstance(){
        return mInstance;
    }

    public DataModel mDataModel = null;
    // Room Database
    public LocalDatabase mDB;
    public int mDataCnt;

}

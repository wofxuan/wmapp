package com.mx.android.wmapp.alarmclock.model;

/**
 * Created by Administrator on 2016/3/4.
 */
public class AlarmModel {

    private int mID;
    private String mTitle;
    private String mTime;
    private String mRepeatType;
    private String mRepeatCode;
    private String mActive;
    private String mWakeType;
    private String mRing;
    private String mInterval;

    public AlarmModel(int ID, String Title, String Time, String WakeType,
                      String RepeatType, String RepeatCode, String ring, String Active, String Interval) {
        mID = ID;
        mTitle = Title;
        mTime = Time;
        mRepeatType = RepeatType;
        mRepeatCode = RepeatCode;
        mRing = ring;
        mActive = Active;
        mWakeType = WakeType;
        mInterval = Interval;
    }

    public AlarmModel(String Title, String Time, String WakeType, String RepeatType,
                      String RepeatCode, String ring, String Active, String Interval) {
        mTitle = Title;
        mTime = Time;
        mRepeatType = RepeatType;
        mRepeatCode = RepeatCode;
        mRing = ring;
        mActive = Active;
        mWakeType = WakeType;
        mInterval = Interval;
    }

    public AlarmModel() {
    }

    public String getInterval() { return mInterval; }

    public void setInterval(String mInterval) { this.mInterval = mInterval; }

    public int getID() {
        return mID;
    }

    public void setID(int mID) {
        this.mID = mID;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String mTime) {
        this.mTime = mTime;
    }

    public String getActive() {
        return mActive;
    }

    public void setActive(String mActive) {
        this.mActive = mActive;
    }

    public String getWakeType() {
        return mWakeType;
    }

    public void setWakeType(String mWakeType) {
        this.mWakeType = mWakeType;
    }


    public String getRepeatType() {
        return mRepeatType;
    }

    public void setRepeatType(String mRepeatType) {
        this.mRepeatType = mRepeatType;
    }

    public String getRepeatCode() {
        return mRepeatCode;
    }

    public void setRepeatCode(String mRepeatCode) {
        this.mRepeatCode = mRepeatCode;
    }

    public String getRing() {
        return mRing;
    }

    public void setRing(String ring) {
        mRing = ring;
    }
}

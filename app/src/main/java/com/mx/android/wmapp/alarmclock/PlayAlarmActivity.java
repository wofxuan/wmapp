package com.mx.android.wmapp.alarmclock;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.KeyEvent;

import com.android.mx.wmapp.R;
import com.mx.android.wmapp.alarmclock.data.MyAlarmDataBase;
import com.mx.android.wmapp.alarmclock.fragment.NormalFragment;
import com.mx.android.wmapp.alarmclock.fragment.QuestionFragment;
import com.mx.android.wmapp.alarmclock.model.AlarmModel;
import com.mx.android.wmapp.base.BaseActivity;
import com.mx.android.wmapp.entity.EventCenter;
import com.mx.android.wmapp.utils.ActivityManager;

public class PlayAlarmActivity extends BaseActivity {
    public static final String ALARM_ID = "id";

    private Vibrator vibrator;
    private String mWake;
    private NormalFragment normalFragment;
    private QuestionFragment questionFragment;
    private int mId;
    private String mRing;
    private MediaPlayer player;
    private AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.addActivity(this);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (maxVolume / 4) * 3,
                AudioManager.FLAG_PLAY_SOUND);

        mId = Integer.parseInt(getIntent().getStringExtra(ALARM_ID));
        setmId(mId);

        MyAlarmDataBase db = new MyAlarmDataBase(this);
        AlarmModel am = db.getAlarm(mId);

        mWake = am.getWakeType();
        mRing = am.getRing();
        normalFragment = new NormalFragment();
        questionFragment = new QuestionFragment();

        if (mWake.equals("常规")) {
            initFragment(0);

        } else {
            initFragment(1);
        }

        if (mRing.equals("震动")) {
            startVibrate();

        } else if (mRing.equals("响铃")) {
            SharedPreferences pf = getSharedPreferences("ringCode", MODE_PRIVATE);
            int ringCode = pf.getInt("key_ring", 1);

            startRing(ringCode);

        } else {
            SharedPreferences pf = getSharedPreferences("ringCode", MODE_PRIVATE);
            int ringCode = pf.getInt("key_ring", 1);
            startRing(ringCode);
            startVibrate();
        }

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_play_alarm;
    }

    @Override
    protected boolean isApplyButterKnife() {
        return true;
    }

    @Override
    protected boolean isApplyEventBus() {
        return true;
    }

    @Override
    protected void onEventComing(EventCenter eventCenter) {
    }

    private void startRing(int ringCode) {
        AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        int mode = audioManager.getRingerMode();
        if (mode == AudioManager.RINGER_MODE_SILENT) {
            //静音模式
            return;
        }
        switch (ringCode) {
            case 1:
                if (player != null && player.isPlaying()) {
                    player.stop();
                    player.release();
                    player = MediaPlayer.create(this, R.raw.ring01);
                } else {
                    player = MediaPlayer.create(this, R.raw.ring01);

                }
                break;
            case 2:
                if (player != null && player.isPlaying()) {
                    player.stop();
                    player.release();
                    player = MediaPlayer.create(this, R.raw.ring02);
                } else {
                    player = MediaPlayer.create(this, R.raw.ring02);

                }
                break;
            case 3:
                if (player != null && player.isPlaying()) {
                    player.stop();
                    player.release();
                    player = MediaPlayer.create(this, R.raw.ring03);
                } else {
                    player = MediaPlayer.create(this, R.raw.ring03);

                }
                break;
            case 4:
                if (player != null && player.isPlaying()) {
                    player.stop();
                    player.release();
                    player = MediaPlayer.create(this, R.raw.ring04);
                } else {
                    player = MediaPlayer.create(this, R.raw.ring04);

                }
                break;
            case 5:
                if (player != null && player.isPlaying()) {
                    player.stop();
                    player.release();
                    player = MediaPlayer.create(this, R.raw.ring05);
                } else {
                    player = MediaPlayer.create(this, R.raw.ring05);

                }
                break;
            case 6:
                if (player != null && player.isPlaying()) {
                    player.stop();
                    player.release();
                    player = MediaPlayer.create(this, R.raw.ring06);
                } else {
                    player = MediaPlayer.create(this, R.raw.ring06);

                }
                break;

        }
        player.start();
        player.setLooping(true);
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                player.start();
                player.setLooping(true);
            }
        });
    }

    private void startVibrate() {
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {2000, 2000, 2000, 2000, 2000, 2000, 2000, 2000, 2000, 2000, 2000, 2000};
        vibrator.vibrate(pattern, -1);

    }

    private void initFragment(int i) {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fm.beginTransaction();
        hideFragment(transaction);
        switch (i) {
            case 0:
                transaction.add(R.id.frag_content, normalFragment);
                transaction.show(normalFragment);
                break;
            case 1:
                transaction.add(R.id.frag_content, questionFragment);
                transaction.show(questionFragment);
                break;

        }

        transaction.commit();
    }

    private void hideFragment(android.support.v4.app.FragmentTransaction transaction) {
        if (normalFragment != null) {
            transaction.hide(normalFragment);
        }
        if (questionFragment != null) {
            transaction.hide(questionFragment);
        }
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    @Override
    protected void onDestroy() {
        if (player != null) {
            player.release();
        }
        if (vibrator != null) {
            vibrator.cancel();
        }
        ActivityManager.removeActivity(this);
        super.onDestroy();
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}

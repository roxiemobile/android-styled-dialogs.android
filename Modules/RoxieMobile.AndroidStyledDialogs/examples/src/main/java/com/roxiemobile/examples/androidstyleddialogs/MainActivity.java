package com.roxiemobile.examples.androidstyleddialogs;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity
{
// MARK: - Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final DialogFragmentManager dialogManager = new DialogFragmentManager(this);
        findViewById(R.id.show_dialog).setOnClickListener(v -> {

            final Timer timer = new Timer();
            mCount = 0;

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    mCount++;
                    dialogManager.showProgressDialog("" + mCount);

                    if (mCount >= 3) {
                        timer.cancel();
                        dialogManager.showAlertDialog("Done!", ""+ mCount);

                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                dialogManager.dismiss();
                            }
                        }, 3000);
                    }
                }
            }, 0, 1000);
        });
    }

// MARK: - Variables

    private int mCount = 0;
}

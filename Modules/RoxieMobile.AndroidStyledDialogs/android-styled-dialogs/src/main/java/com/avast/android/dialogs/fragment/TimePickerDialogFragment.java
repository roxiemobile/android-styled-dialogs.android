package com.avast.android.dialogs.fragment;

import android.content.Context;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.widget.TimePicker;

import com.avast.android.dialogs.core.BaseDialogFragment;
import com.roxiemobile.androidstyleddialogs.R;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Dialog with a time picker.
 * <p/>
 * Implement {@link com.avast.android.dialogs.iface.IDateDialogListener}
 * and/or {@link com.avast.android.dialogs.iface.ISimpleDialogCancelListener} to handle events.
 */
public class TimePickerDialogFragment extends DatePickerDialogFragment {

    TimePicker mTimePicker;
    Calendar mCalendar;


    public static SimpleDialogBuilder createBuilder(Context context, FragmentManager fragmentManager) {
        return new SimpleDialogBuilder(context, fragmentManager, TimePickerDialogFragment.class);
    }

    @Override
    @SuppressWarnings("deprecation")
    protected BaseDialogFragment.Builder build(BaseDialogFragment.Builder builder) {
        builder = super.build(builder);
        mTimePicker = (TimePicker) builder.getLayoutInflater().inflate(R.layout.sdl_timepicker, null);
        mTimePicker.setIs24HourView(getArguments().getBoolean(ARG_24H));
        builder.setView(mTimePicker);

        TimeZone zone = TimeZone.getTimeZone(getArguments().getString(ARG_ZONE));
        mCalendar = Calendar.getInstance(zone);
        mCalendar.setTimeInMillis(getArguments().getLong(ARG_DATE, System.currentTimeMillis()));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mTimePicker.setHour(mCalendar.get(Calendar.HOUR_OF_DAY));
            mTimePicker.setMinute(mCalendar.get(Calendar.MINUTE));
        }
        else {
            mTimePicker.setCurrentHour(mCalendar.get(Calendar.HOUR_OF_DAY));
            mTimePicker.setCurrentMinute(mCalendar.get(Calendar.MINUTE));
        }
        return builder;
    }

    @SuppressWarnings("deprecation")
    public Date getDate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mCalendar.set(Calendar.HOUR_OF_DAY, mTimePicker.getHour());
            mCalendar.set(Calendar.MINUTE, mTimePicker.getMinute());
        }
        else {
            mCalendar.set(Calendar.HOUR_OF_DAY, mTimePicker.getCurrentHour());
            mCalendar.set(Calendar.MINUTE, mTimePicker.getCurrentMinute());
        }
        return mCalendar.getTime();
    }
}

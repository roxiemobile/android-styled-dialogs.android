package com.roxiemobile.androidstyleddialogs.ui.dialog;

import com.avast.android.dialogs.iface.IDateDialogListener;

import java.util.Date;

public abstract class DateDialogListener implements IDateDialogListener
{
// MARK: - Construction

    public DateDialogListener() {
        mChainedListener = null;
    }

    public DateDialogListener(IDateDialogListener chainedListener) {
        mChainedListener = chainedListener;
    }

// MARK: - Methods

    @Override
    public void onPositiveButtonClicked(int requestCode, Date date) {
        if (mChainedListener != null) {
            mChainedListener.onPositiveButtonClicked(requestCode, date);
        }
    }

    @Override
    public void onNegativeButtonClicked(int requestCode, Date date) {
        if (mChainedListener != null) {
            mChainedListener.onNegativeButtonClicked(requestCode, date);
        }
    }

//    @Override
//    public void onDismiss(int requestCode, Date date) {
//        if (mChainedListener != null) {
//            mChainedListener.onDismiss(requestCode, date);
//        }
//    }

//    @Override
//    public void onCancel(int requestCode, Date date) {
//        if (mChainedListener != null) {
//            mChainedListener.onCancel(requestCode, date);
//        }
//    }

// MARK: - Variables

    private final IDateDialogListener mChainedListener;

}

package com.roxiemobile.androidstyleddialogs.ui.dialog;

import com.avast.android.dialogs.iface.ISimpleDialogListener;

public abstract class SimpleDialogListener implements ISimpleDialogListener
{
// MARK: - Construction

    public SimpleDialogListener() {
        mChainedListener = null;
    }

    public SimpleDialogListener(ISimpleDialogListener chainedListener) {
        mChainedListener = chainedListener;
    }

// MARK: - Methods

    @Override
    public void onPositiveButtonClicked(int requestCode) {
        if (mChainedListener != null) {
            mChainedListener.onPositiveButtonClicked(requestCode);
        }
    }

    @Override
    public void onNegativeButtonClicked(int requestCode) {
        if (mChainedListener != null) {
            mChainedListener.onNegativeButtonClicked(requestCode);
        }
    }

    @Override
    public void onNeutralButtonClicked(int requestCode) {
        if (mChainedListener != null) {
            mChainedListener.onNeutralButtonClicked(requestCode);
        }
    }

    @Override
    public void onDismiss(int requestCode) {
        if (mChainedListener != null) {
            mChainedListener.onDismiss(requestCode);
        }
    }

    @Override
    public void onCancel(int requestCode) {
        if (mChainedListener != null) {
            mChainedListener.onCancel(requestCode);
        }
    }

// MARK: - Variables

    private final ISimpleDialogListener mChainedListener;

}

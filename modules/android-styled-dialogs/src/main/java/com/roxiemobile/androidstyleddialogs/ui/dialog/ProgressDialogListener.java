package com.roxiemobile.androidstyleddialogs.ui.dialog;

import com.avast.android.dialogs.iface.IProgressDialogListener;

public abstract class ProgressDialogListener implements IProgressDialogListener
{
// MARK: - Construction

    public ProgressDialogListener() {
        mChainedListener = null;
    }

    public ProgressDialogListener(IProgressDialogListener chainedListener) {
        mChainedListener = chainedListener;
    }

// MARK: - Methods

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

    private final IProgressDialogListener mChainedListener;

}

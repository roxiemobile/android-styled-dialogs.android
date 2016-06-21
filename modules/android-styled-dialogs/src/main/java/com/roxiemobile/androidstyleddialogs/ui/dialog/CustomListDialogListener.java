package com.roxiemobile.androidstyleddialogs.ui.dialog;

import com.avast.android.dialogs.iface.ICustomListDialogListener;

public class CustomListDialogListener implements ICustomListDialogListener
{
// MARK: - Construction

    public CustomListDialogListener() {
        mChainedListener = null;
    }

    public CustomListDialogListener(ICustomListDialogListener chainedListener) {
        mChainedListener = chainedListener;
    }

// MARK: - Methods

    @Override
    public void onListItemSelected(CharSequence value, int number, int requestCode) {
        if (mChainedListener != null) {
            mChainedListener.onListItemSelected(value, number, requestCode);
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

    @Override
    public void onPositiveButtonClicked(int requestCode) {
        if (mChainedListener != null) {
            mChainedListener.onPositiveButtonClicked(requestCode);
        }
    }

// MARK: - Variables

    private final ICustomListDialogListener mChainedListener;

}

package com.roxiemobile.androidstyleddialogs.ui.dialog;

import com.avast.android.dialogs.iface.IListDialogListener;

public abstract class ListDialogListener implements IListDialogListener
{
// MARK: - Construction

    public ListDialogListener() {
        mChainedListener = null;
    }

    public ListDialogListener(IListDialogListener chainedListener) {
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

// MARK: - Variables

    private final IListDialogListener mChainedListener;

}

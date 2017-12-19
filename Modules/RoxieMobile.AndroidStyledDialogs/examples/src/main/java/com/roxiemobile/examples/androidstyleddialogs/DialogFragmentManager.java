package com.roxiemobile.examples.androidstyleddialogs;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.roxiemobile.androidstyleddialogs.core.manager.AbstractDialogFragmentManager;

public final class DialogFragmentManager extends AbstractDialogFragmentManager
{
// MARK: - Construction

    public DialogFragmentManager(@NonNull FragmentActivity activity) {
        super(activity);
    }
}

package com.roxiemobile.androidstyleddialogs.core.manager;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.avast.android.dialogs.fragment.ProgressDialogFragment;
import com.avast.android.dialogs.fragment.SimpleDialogFragment;
import com.roxiemobile.androidcommons.concurrent.ThreadUtils;
import com.roxiemobile.androidcommons.data.CommonKeys;
import com.roxiemobile.androidcommons.diagnostics.Guard;
import com.roxiemobile.androidcommons.util.StringUtils;
import com.roxiemobile.androidstyleddialogs.R;
import com.roxiemobile.androidstyleddialogs.ui.dialog.ProgressDialogListener;
import com.roxiemobile.androidstyleddialogs.ui.dialog.SimpleDialogListener;

import java.util.UUID;

public abstract class AbstractDialogFragmentManager
{
// MARK: - Construction

    public AbstractDialogFragmentManager(@NonNull FragmentActivity activity) {
        Guard.notNull(activity, "activity is null");

        // Init instance variables
        mActivity = activity;
    }

// MARK: - Methods

    public final void dismiss() {
        // Dismiss last known active Dialog
        dismissDialogOnUiThreadBlocking(mLastKnownUniqueTag);
    }

    @Deprecated
    public final void dismissActiveDialog() {
        dismiss();
    }

    protected @NonNull FragmentActivity getActivity() {
        return mActivity;
    }

// MARK: - Methods: AlertDialog

    public void showAlertDialog(int titleId, int messageId) {
        showAlertDialog(titleId, messageId, null);
    }

    public void showAlertDialog(CharSequence title, @NonNull CharSequence message) {
        showAlertDialog(title, message, null);
    }

    public void showAlertDialog(int titleId, int messageId, SimpleDialogListener listener) {
        showAlertDialog(titleId, messageId, true, listener);
    }

    public void showAlertDialog(int titleId, int messageId, boolean cancelable, SimpleDialogListener listener) {
        showAlertDialog(mActivity.getString(titleId), mActivity.getString(messageId), cancelable, listener);
    }

    public void showAlertDialog(CharSequence title, @NonNull CharSequence message, SimpleDialogListener listener) {
        showAlertDialog(title, message, true, listener);
    }

    public void showAlertDialog(CharSequence title, @NonNull CharSequence message, boolean cancelable, final SimpleDialogListener listener) {
        Guard.notNull(message, "message is null");

        final SimpleDialogFragment.SimpleDialogBuilder builder = SimpleDialogFragment.createBuilder(
                mActivity, mActivity.getSupportFragmentManager());

        // Init dialog builder
        builder .setCancelable(cancelable)
                .setTitle(title != null ? title.toString() : null)
                .setMessage(message.toString())
                .setPositiveButtonText(R.string.mdg__button_close)
                .setDialogListener(listener);

        // Replace last known Dialog
        replaceDialogOnUiThreadBlocking(builder.create());
    }

// MARK: - Methods: ErrorAlertDialog

    public void showErrorAlertDialog(int messageId) {
        showErrorAlertDialog(messageId, true);
    }

    public void showErrorAlertDialog(int messageId, boolean cancelable) {
        showAlertDialog(R.string.mdg__title_alert_error, messageId, cancelable, null);
    }

    public void showErrorAlertDialog(@NonNull CharSequence message) {
        showErrorAlertDialog(message, true);
    }

    public void showErrorAlertDialog(CharSequence message, boolean cancelable) {
        showAlertDialog(mActivity.getString(R.string.mdg__title_alert_error), message, cancelable, null);
    }

// MARK: - Methods: YesNoDialog

    public void showYesNoDialog(int titleId, int messageId, SimpleDialogListener listener) {
        showYesNoDialog(titleId, messageId, true, listener);
    }

    public void showYesNoDialog(int titleId, int messageId, boolean cancelable, SimpleDialogListener listener) {
        showYesNoDialog(mActivity.getString(titleId), mActivity.getString(messageId), cancelable, listener);
    }

    public void showYesNoDialog(CharSequence title, @NonNull CharSequence message, final SimpleDialogListener listener) {
        showYesNoDialog(title, message, true, listener);
    }

    public void showYesNoDialog(CharSequence title, @NonNull CharSequence message, boolean cancelable, final SimpleDialogListener listener) {
        Guard.notNull(message, "message is null");

        final SimpleDialogFragment.SimpleDialogBuilder builder = SimpleDialogFragment.createBuilder(
                mActivity, mActivity.getSupportFragmentManager());

        // Init dialog builder
        builder .setCancelable(cancelable)
                .setTitle(title != null ? title.toString() : null)
                .setMessage(message.toString())
                .setPositiveButtonText(R.string.mdg__button_yes)
                .setNegativeButtonText(R.string.mdg__button_no)
                .setDialogListener(listener);

        // Replace last known Dialog
        replaceDialogOnUiThreadBlocking(builder.create());
    }

// MARK: - Methods: ProgressDialog

    public void showProgressDialog(int textId) {
        showProgressDialog(textId, true);
    }

    public void showProgressDialog(int textId, boolean cancelable) {
        showProgressDialog(mActivity.getString(textId), cancelable, null);
    }

    public void showProgressDialog(@NonNull CharSequence message) {
        showProgressDialog(message, true);
    }

    public void showProgressDialog(@NonNull CharSequence message, boolean cancelable) {
        showProgressDialog(message, cancelable, null);
    }

    public void showProgressDialog(int textId, boolean cancelable, final ProgressDialogListener listener) {
        showProgressDialog(mActivity.getString(textId), cancelable, listener);
    }

    public void showProgressDialog(@NonNull CharSequence message, boolean cancelable, final ProgressDialogListener listener) {
        Guard.notNull(message, "message is null");

        final ProgressDialogFragment.ProgressDialogBuilder builder = ProgressDialogFragment.createBuilder(
                mActivity, mActivity.getSupportFragmentManager());

        // Init dialog builder
        builder .setCancelable(cancelable)
                .setCancelableOnTouchOutside(false)
                .setMessage(message.toString())
                .setDialogListener(listener);

        // Replace last known Dialog
        replaceDialogOnUiThreadBlocking(builder.create());
    }

// MARK: - Methods: CustomDialog

    public void showCustomDialog(@NonNull DialogFragment dialog) {
        Guard.notNull(dialog, "dialog is null");
        Guard.isNull(dialog.getTag(), "dialog.getTag() is not null");

        // Replace last known Dialog
        replaceDialogOnUiThreadBlocking(dialog);
    }

// MARK: - Private Methods

    private void replaceDialogOnUiThreadBlocking(final DialogFragment dialog) {
        ThreadUtils.runOnUiThreadBlocking(() -> {

            DialogFragment lastKnownDialog = findDialogByTag(mLastKnownUniqueTag);

            // Update existing ProgressDialog
            if ((lastKnownDialog instanceof ProgressDialogFragment) && (dialog instanceof ProgressDialogFragment)) {

                ProgressDialogFragment dialogOld = (ProgressDialogFragment) lastKnownDialog;
                ProgressDialogFragment dialogNew = (ProgressDialogFragment) dialog;

                // Replace message
                CharSequence message = dialogNew.getMessage();
                if (StringUtils.isNotBlank(message)) {
                    dialogOld.setMessage(message);
                }

                // Replace listener
                dialogOld.setDialogListener(dialogNew.getDialogListener());
            }
            // Replace/Dismiss last known Dialog
            else {

                if (dialog != null) {
                    mLastKnownUniqueTag = newUniqueTag();
                    dialog.showNow(mActivity.getSupportFragmentManager(), mLastKnownUniqueTag);
                }
                else {
                    mLastKnownUniqueTag = null;
                }

                // Dismiss last known Dialog without flashing of a background
                if (lastKnownDialog != null) {
                    mHandler.postDelayed(lastKnownDialog::dismiss, 100);
                }
            }
        });
    }

    private void dismissDialogOnUiThreadBlocking(final String tag) {
        ThreadUtils.runOnUiThreadBlocking(() -> {

            if (StringUtils.isBlank(tag)) {
                return;
            }

            // Dismiss active Dialog
            DialogFragment dialog = findDialogByTag(tag);
            if (dialog != null) {
                dialog.dismiss();
            }

            // Forget last known Dialog if tags is equal
            if (tag.equals(mLastKnownUniqueTag)) {
                mLastKnownUniqueTag = null;
            }
        });
    }

    private DialogFragment findDialogByTag(String tag) {
        DialogFragment activeDialog = null;

        if (StringUtils.emptyToNull(tag) != null) {
            Fragment fragment = mActivity.getSupportFragmentManager().findFragmentByTag(tag);

            if (fragment instanceof DialogFragment) {
                activeDialog = (DialogFragment) fragment;
            }
        }
        return activeDialog;
    }

    private static String newUniqueTag() {
        return CommonKeys.URN + ":fragment_tag." + UUID.randomUUID().toString();
    }

// MARK: - Variables

    private @NonNull FragmentActivity mActivity;

    private String mLastKnownUniqueTag;

    private @NonNull Handler mHandler = new Handler(ThreadUtils.getUiThreadLooper());
}

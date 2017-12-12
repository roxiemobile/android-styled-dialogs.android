package com.roxiemobile.androidstyleddialogs.core.manager;

import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.avast.android.dialogs.fragment.ProgressDialogFragment;
import com.avast.android.dialogs.fragment.SimpleDialogFragment;
import com.roxiemobile.androidcommons.concurrent.ThreadUtils;
import com.roxiemobile.androidcommons.data.CommonKeys;
import com.roxiemobile.androidcommons.util.StringUtils;
import com.roxiemobile.androidstyleddialogs.R;
import com.roxiemobile.androidstyleddialogs.ui.dialog.ProgressDialogListener;
import com.roxiemobile.androidstyleddialogs.ui.dialog.SimpleDialogListener;

import java.util.UUID;

import static com.roxiemobile.androidcommons.diagnostics.Require.requireTrue;
import static com.roxiemobile.androidcommons.diagnostics.Require.requireNotNull;
import static com.roxiemobile.androidcommons.diagnostics.Require.requireNull;

public abstract class AbstractDialogFragmentManager
{
// MARK: - Construction

    public AbstractDialogFragmentManager(@NonNull FragmentActivity activity) {
        requireNotNull(activity, "activity is null");

        // Init instance variables
        mActivity = activity;
    }

// MARK: - Methods

    public final void dismiss() {
        // Dismiss active Dialog on main thread
        ThreadUtils.runOnUiThreadBlocking(() -> replaceActiveDialog(null));
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
        requireNotNull(message, "message is null");

        final SimpleDialogFragment.SimpleDialogBuilder builder = SimpleDialogFragment.createBuilder(
                mActivity, mActivity.getSupportFragmentManager());

        // Init dialog builder
        builder .setCancelable(cancelable)
                .setTitle(title != null ? title.toString() : null)
                .setMessage(message.toString())
                .setPositiveButtonText(R.string.mdg__button_close)
                .setDialogListener(listener);

        // Replace active Dialog on main thread
        ThreadUtils.runOnUiThreadBlocking(() -> replaceActiveDialog(builder.create()));
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
        requireNotNull(message, "message is null");

        SimpleDialogFragment.SimpleDialogBuilder builder = SimpleDialogFragment.createBuilder(
                mActivity, mActivity.getSupportFragmentManager());

        // Init dialog builder
        builder .setCancelable(cancelable)
                .setTitle(title != null ? title.toString() : null)
                .setMessage(message.toString())
                .setPositiveButtonText(R.string.mdg__button_yes)
                .setNegativeButtonText(R.string.mdg__button_no)
                .setDialogListener(listener);

        // Replace active Dialog on main thread
        ThreadUtils.runOnUiThreadBlocking(() -> replaceActiveDialog(builder.create()));
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
        requireNotNull(message, "message is null");

        ProgressDialogFragment.ProgressDialogBuilder builder = ProgressDialogFragment.createBuilder(
                mActivity, mActivity.getSupportFragmentManager());

        // Init dialog builder
        builder .setCancelable(cancelable)
                .setCancelableOnTouchOutside(false)
                .setMessage(message.toString())
                .setDialogListener(listener);

        // Replace active Dialog on main thread
        ThreadUtils.runOnUiThreadBlocking(() -> replaceActiveDialog(builder.create()));
    }

// MARK: - Methods: CustomDialog

    public void showCustomDialog(@NonNull DialogFragment dialog) {
        requireNotNull(dialog, "dialog is null");
        requireNull(dialog.getTag(), "dialog.getTag() is not null");

        // Replace active Dialog on main thread
        ThreadUtils.runOnUiThreadBlocking(() -> replaceActiveDialog(dialog));
    }

// MARK: - Private Methods

    private void replaceActiveDialog(DialogFragment dialog) {
        requireTrue(ThreadUtils.runningOnUiThread(), "This method must execute on the main thread only!");

        final String uniqueTag = StringUtils.emptyToNull(mLastKnownUniqueTag);
        DialogFragment activeDialog = null;

        if (uniqueTag != null) {
            Fragment fragment = mActivity.getSupportFragmentManager().findFragmentByTag(uniqueTag);
            if (fragment instanceof DialogFragment) {
                activeDialog = (DialogFragment) fragment;
            }
        }

        // Update existing ProgressDialog
        if ((activeDialog instanceof ProgressDialogFragment) && (dialog instanceof ProgressDialogFragment)) {

            ProgressDialogFragment dialogOld = (ProgressDialogFragment) activeDialog;
            ProgressDialogFragment dialogNew = (ProgressDialogFragment) dialog;

            // Replace message
            CharSequence message = dialogNew.getMessage();
            if (StringUtils.isNotBlank(message)) {
                dialogOld.setMessage(message);
            }

            // Replace listener
            dialogOld.setDialogListener(dialogNew.getDialogListener());
        }
        // Replace active Dialog
        else if (dialog != null) {

            mLastKnownUniqueTag = newUniqueTag();
            dialog.show(mActivity.getSupportFragmentManager(), mLastKnownUniqueTag);
        }
        // Dismiss active Dialog
        else {

            mLastKnownUniqueTag = null;
            if (activeDialog != null) {
                activeDialog.dismiss();
            }
        }
    }

    private String newUniqueTag() {
        return CommonKeys.URN + ":fragment_tag." + UUID.randomUUID().toString();
    }

// MARK: - Variables

    private @NonNull FragmentActivity mActivity;

    private String mLastKnownUniqueTag;
}

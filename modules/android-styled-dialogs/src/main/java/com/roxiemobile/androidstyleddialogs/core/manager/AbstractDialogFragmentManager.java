package com.roxiemobile.androidstyleddialogs.core.manager;

import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.avast.android.dialogs.fragment.ProgressDialogFragment;
import com.avast.android.dialogs.fragment.SimpleDialogFragment;
import com.roxiemobile.androidcommons.concurrent.ThreadUtils;
import com.roxiemobile.androidstyleddialogs.R;
import com.roxiemobile.androidstyleddialogs.ui.dialog.ProgressDialogListener;
import com.roxiemobile.androidstyleddialogs.ui.dialog.SimpleDialogListener;

import static com.roxiemobile.androidcommons.diagnostics.Expect.expectNotNull;

public abstract class AbstractDialogFragmentManager
{
// MARK: - Construction

    public AbstractDialogFragmentManager(@NonNull FragmentActivity activity) {
        expectNotNull(activity, "activity is null");

        // Init instance variables
        mActivity = activity;
    }

// MARK: - Methods

    public void dismissActiveDialog() {
        showDialogOnUiThreadBlocking(null);
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
        expectNotNull(message, "message is null");

        SimpleDialogFragment.SimpleDialogBuilder builder = SimpleDialogFragment.createBuilder(
                mActivity, mActivity.getSupportFragmentManager());

        // Init dialog builder
        builder .setCancelable(cancelable)
                .setTitle(title != null ? title.toString() : null)
                .setMessage(message.toString())
                .setPositiveButtonText(R.string.mdg__button_close)
                .setDialogListener(new SimpleDialogListener(listener)
                {
                    @Override
                    public void onDismiss(int requestCode) {
                        super.onDismiss(requestCode);
                        clearActiveDialog();
                    }
                });

        // Build alert dialog
        DialogFragment fragment = builder.create();

        // Show dialog
        showDialogOnUiThreadBlocking(fragment);
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
        expectNotNull(message, "message is null");

        SimpleDialogFragment.SimpleDialogBuilder builder = SimpleDialogFragment.createBuilder(
                mActivity, mActivity.getSupportFragmentManager());

        // Init dialog builder
        builder .setCancelable(cancelable)
                .setTitle(title != null ? title.toString() : null)
                .setMessage(message.toString())
                .setPositiveButtonText(R.string.mdg__button_yes)
                .setNegativeButtonText(R.string.mdg__button_no)
                .setDialogListener(new SimpleDialogListener(listener)
                {
                    @Override
                    public void onDismiss(int requestCode) {
                        super.onDismiss(requestCode);
                        clearActiveDialog();
                    }
                });

        // Build alert dialog
        DialogFragment dialog = builder.create();

        // Show dialog
        showDialogOnUiThreadBlocking(dialog);
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
        expectNotNull(message, "message is null");

        ProgressDialogFragment.ProgressDialogBuilder builder = ProgressDialogFragment.createBuilder(
                mActivity, mActivity.getSupportFragmentManager());

        // Init dialog builder
        builder .setCancelable(cancelable)
                .setCancelableOnTouchOutside(false)
                .setMessage(message.toString())
                .setDialogListener(new ProgressDialogListener(listener)
                {
                    @Override
                    public void onDismiss(int requestCode) {
                        super.onDismiss(requestCode);
                        clearActiveDialog();
                    }
                });

        // Show dialog
        showDialogOnUiThreadBlocking(builder.create());
    }

// MARK: - Methods: CustomDialog

    public void showCustomDialog(@NonNull DialogFragment fragment) {
        expectNotNull(fragment, "fragment is null");
        showDialogOnUiThreadBlocking(fragment);
    }

// MARK: - Private Methods

    private void showDialogOnUiThreadBlocking(DialogFragment fragment) {
        ThreadUtils.runOnUiThreadBlocking(() -> replaceActiveDialog(fragment));
    }

    private void replaceActiveDialog(DialogFragment dialog) {
        synchronized (mLock) {

            // Update existing ProgressDialog
            if ((mActiveDialog instanceof ProgressDialogFragment) && (dialog instanceof ProgressDialogFragment)) {

                ProgressDialogFragment dialogOld = (ProgressDialogFragment) mActiveDialog;
                ProgressDialogFragment dialogNew = (ProgressDialogFragment) dialog;

                // Replace message
                CharSequence message = dialogNew.getMessage();
                if (!TextUtils.isEmpty(message)) {
                    dialogOld.setMessage(message);
                }

                // Replace listener
                dialogOld.setDialogListener(dialogNew.getDialogListener());
            }
            // Replace active Dialog
            else {

                if (mActiveDialog != null) {
                    mActiveDialog.dismiss();
                }

                mActiveDialog = dialog;

                if (dialog != null) {
                    dialog.show(mActivity.getSupportFragmentManager(), null);
                }
            }
        }
    }

    // dismissing a dialog manually in onDismiss() leads to bugs when trying to replace a dialog with a new one
    private void clearActiveDialog() {
        mActiveDialog = null;
    }

// MARK: - Variables

    private @NonNull FragmentActivity mActivity;

    private DialogFragment mActiveDialog;
    private final Object mLock = new Object();

}

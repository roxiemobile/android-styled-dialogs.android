package com.roxiemobile.androidstyleddialogs.core.manager;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.avast.android.dialogs.fragment.ProgressDialogFragment;
import com.avast.android.dialogs.fragment.SimpleDialogFragment;
import com.roxiemobile.androidcommons.concurrent.ThreadUtils;
import com.roxiemobile.androidcommons.util.StringUtils;
import com.roxiemobile.androidstyleddialogs.R;
import com.roxiemobile.androidstyleddialogs.ui.dialog.ProgressDialogListener;
import com.roxiemobile.androidstyleddialogs.ui.dialog.SimpleDialogListener;

import java.util.UUID;

import static com.roxiemobile.androidcommons.diagnostics.Expect.expectNotEmpty;
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
        dismissDialogWithTag(null);
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

        String tag = UUID.randomUUID().toString();

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
                        dismissDialogWithTag(tag);
                    }
                });

        // Build dialog
        DialogFragment dialog = builder.create();

        // Show dialog
        showDialogOnUiThreadBlocking(new DialogFragmentEntry(tag, dialog));
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

        String tag = UUID.randomUUID().toString();

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
                        dismissDialogWithTag(tag);
                    }
                });

        // Build dialog
        DialogFragment dialog = builder.create();

        // Show dialog
        showDialogOnUiThreadBlocking(new DialogFragmentEntry(tag, dialog));
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

        String tag = UUID.randomUUID().toString();

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
                        dismissDialogWithTag(tag);
                    }
                });

        // Build dialog
        DialogFragment dialog = builder.create();

        // Show dialog
        showDialogOnUiThreadBlocking(new DialogFragmentEntry(tag, dialog));
    }

// MARK: - Methods: CustomDialog

//    public void showCustomDialog(@NonNull DialogFragment fragment) {
//        expectNotNull(fragment, "fragment is null");
//        showDialogOnUiThreadBlocking(fragment, fragment.getTag());
//    }

// MARK: - Private Methods

    private void dismissDialogWithTag(@Nullable String tag) {
        ThreadUtils.runOnUiThreadBlocking(() -> {
            synchronized (mLock) {
                if (mActiveDialog != null) {
                    if (StringUtils.isEmpty(tag) || tag.equals(mActiveDialog.getTag())) {
                        mActiveDialog.getDialog().dismiss();
                        mActiveDialog = null;
                    }
                }
            }
        });
    }

    private void showDialogOnUiThreadBlocking(@NonNull DialogFragmentEntry fragment) {
        ThreadUtils.runOnUiThreadBlocking(() -> replaceActiveDialog(fragment));
    }

    private void replaceActiveDialog(@NonNull DialogFragmentEntry dialog) {
        synchronized (mLock) {

            // Update existing ProgressDialog
            if (mActiveDialog != null && (mActiveDialog.getDialog() instanceof ProgressDialogFragment)
                    && (dialog.getDialog() instanceof ProgressDialogFragment)) {

                ProgressDialogFragment dialogOld = (ProgressDialogFragment) mActiveDialog.getDialog();
                ProgressDialogFragment dialogNew = (ProgressDialogFragment) dialog.getDialog();

                // Replace message
                CharSequence message = dialogNew.getMessage();
                if (!TextUtils.isEmpty(message)) {
                    dialogOld.setMessage(message);
                }

                // Replace listener
                dialogOld.setDialogListener(dialogNew.getDialogListener());

                mActiveDialog = new DialogFragmentEntry(dialog.getTag(), dialogOld);
            }
            // Replace active Dialog
            else {

                if (mActiveDialog != null) {
                    mActiveDialog.getDialog().dismiss();
                }

                mActiveDialog = dialog;

                dialog.getDialog().show(mActivity.getSupportFragmentManager(), null);
            }
        }
    }

// MARK: - InnerTypes

    private class DialogFragmentEntry {

        public DialogFragmentEntry(@NonNull String tag, @NonNull DialogFragment dialogFragment) {
            expectNotEmpty(tag, "tag is empty");
            expectNotNull(dialogFragment, "dialogFragment is null");

            mTag = tag;
            mDialogFragment = dialogFragment;
        }

        public @NonNull String getTag() {
            return mTag;
        }

        public @NonNull DialogFragment getDialog() {
            return mDialogFragment;
        }

        private final String mTag;
        private final DialogFragment mDialogFragment;
    }

// MARK: - Variables

    private @NonNull FragmentActivity mActivity;

    private DialogFragmentEntry mActiveDialog;
    private final Object mLock = new Object();

}

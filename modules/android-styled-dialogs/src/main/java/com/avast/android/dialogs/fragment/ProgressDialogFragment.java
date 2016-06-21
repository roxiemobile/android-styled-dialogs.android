package com.avast.android.dialogs.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.avast.android.dialogs.core.BaseDialogBuilder;
import com.avast.android.dialogs.core.BaseDialogFragment;
import com.avast.android.dialogs.iface.IProgressDialogListener;
import com.roxiemobile.androidstyleddialogs.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple progress dialog that shows indeterminate progress bar together with message and dialog title (optional).<br/>
 * <p>
 * To show the dialog, start with {@link #createBuilder(android.content.Context, android.support.v4.app.FragmentManager)}.
 * </p>
 * <p>
 * Dialog can be cancelable - to listen to cancellation, activity or target fragment must implement {@link com.avast.android.dialogs.iface.ISimpleDialogCancelListener}
 * </p>
 *
 * @author Tomas Vondracek
 */
public class ProgressDialogFragment extends BaseDialogFragment {

    protected final static String ARG_MESSAGE = "message";
    protected final static String ARG_TITLE = "title";

    protected DialogHolder mDialogHolder = new DialogHolder();

    public static ProgressDialogBuilder createBuilder(Context context, FragmentManager fragmentManager) {
        return new ProgressDialogBuilder(context, fragmentManager);
    }

    @Override
    protected Builder build(Builder builder) {
        final LayoutInflater inflater = builder.getLayoutInflater();
        final View view = inflater.inflate(R.layout.sdl_progress, null, false);
        final TextView tvMessage = (TextView) view.findViewById(R.id.sdl_message);

        tvMessage.setText(getArguments().getCharSequence(ARG_MESSAGE));

        builder.setView(view);

        builder.setTitle(getArguments().getCharSequence(ARG_TITLE));

        return builder;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() == null) {
            throw new IllegalArgumentException("use ProgressDialogBuilder to construct this dialog");
        }
    }

    public void setMessage(CharSequence message)
    {
        if (getView() == null)
            return;

        TextView tvMessage = (TextView) getView().findViewById(R.id.sdl_message);
        if (tvMessage != null) {
            tvMessage.setText(message);
        }
    }

    public void setMessage(int messageResId)
    {
        if (getView() == null)
            return;

        TextView tvMessage = (TextView) getView().findViewById(R.id.sdl_message);
        if (tvMessage != null) {
            tvMessage.setText(messageResId);
        }
    }

    public CharSequence getMessage()
    {
        CharSequence message = null;

        if (getView() != null) {
            TextView tvMessage = (TextView) getView().findViewById(R.id.sdl_message);

            if (tvMessage != null) {
                message = tvMessage.getText();
            }
        }

        if (message == null) {
            message = getArguments().getString(ARG_MESSAGE);
        }

        return message;
    }

    public void setDialogListener(IProgressDialogListener listener) {
        mDialogHolder.mDialogListener = listener;
    }

    public IProgressDialogListener getDialogListener() {
        return mDialogHolder.mDialogListener;
    }

    protected void setDialogHolder(DialogHolder holder) {
        mDialogHolder = (holder == null) ? new DialogHolder() : holder;
    }

    // FIXME move to superclass
    @Override
    protected <T> List<T> getDialogListeners(Class<T> listenerInterface) {
        List<T> result = super.getDialogListeners(listenerInterface);
        if (mDialogHolder != null && mDialogHolder.mDialogListener != null)
        {
            if (listenerInterface.isInstance(mDialogHolder.mDialogListener))
            {
                result = new ArrayList<>(result);
                result.add(listenerInterface.cast(mDialogHolder.mDialogListener));
            }
        }
        return result;
    }

    protected static class DialogHolder {
        IProgressDialogListener mDialogListener;
    }

    public static class ProgressDialogBuilder extends BaseDialogBuilder<ProgressDialogBuilder> {

        private CharSequence mTitle;
        private CharSequence mMessage;

        private DialogHolder mDialogHolder = new DialogHolder();

        protected ProgressDialogBuilder(Context context, FragmentManager fragmentManager) {
            super(context, fragmentManager, ProgressDialogFragment.class);
        }

        @Override
        protected ProgressDialogBuilder self() {
            return this;
        }

        public ProgressDialogBuilder setTitle(int titleResourceId) {
            mTitle = mContext.getString(titleResourceId);
            return this;
        }


        public ProgressDialogBuilder setTitle(CharSequence title) {
            mTitle = title;
            return this;
        }

        public ProgressDialogBuilder setMessage(int messageResourceId) {
            mMessage = mContext.getString(messageResourceId);
            return this;
        }

        public ProgressDialogBuilder setMessage(CharSequence message) {
            mMessage = message;
            return this;
        }

        public ProgressDialogBuilder setDialogListener(IProgressDialogListener listener) {
            mDialogHolder.mDialogListener = listener;
            return this;
        }

        @Override
        protected Bundle prepareArguments() {
            Bundle args = new Bundle();
            args.putCharSequence(SimpleDialogFragment.ARG_MESSAGE, mMessage);
            args.putCharSequence(SimpleDialogFragment.ARG_TITLE, mTitle);

            return args;
        }

        @Override
        protected void prepareDialogFragment(BaseDialogFragment fragment) {
            super.prepareDialogFragment(fragment);

            if (fragment instanceof ProgressDialogFragment) {
                ((ProgressDialogFragment) fragment).setDialogHolder(mDialogHolder);
            }
        }
    }
}

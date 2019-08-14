package com.freak.printtool.hardware.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.TextView;

import com.freak.printtool.R;


/**
 * 显示Toast的对话框
 * @author Administrator
 */
public class ToastDialogFragment extends DialogFragment implements View.OnClickListener {

    private Dialog mDialog;
    private TextView mTextViewCommonTitle;
    private TextView mTextViewCommonContext;
    private TextView mTextViewCancel;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDialog = new Dialog(getActivity(), R.style.dialog);
        mDialog.setContentView(R.layout.fragment_dialog_toast);
        mDialog.setCancelable(true);

        mTextViewCommonTitle = (TextView) mDialog.findViewById(R.id.text_view_common_title);
        mTextViewCommonContext = (TextView) mDialog.findViewById(R.id.text_view_common_context);
        mTextViewCancel = (TextView) mDialog.findViewById(R.id.text_view_cancel);


        mTextViewCommonTitle.setText(getArguments().getString("title"));
        mTextViewCommonContext.setText(getArguments().getString("context"));
        mTextViewCancel.setText(getArguments().getString("cancel"));
        mTextViewCancel.setOnClickListener(this);
        return mDialog;
    }

    private OnTipsListener onTipsListener;

    public void setOnConfirmListener(OnTipsListener onTipsListener) {
        this.onTipsListener = onTipsListener;
    }

    public interface OnTipsListener {
        /**
         * 返回
         */
        void onCancel();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_view_cancel:
                mDialog.dismiss();
                break;
            default:
                break;
        }
    }
}


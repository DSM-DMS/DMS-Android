package com.dms.beinone.application.dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.dms.beinone.application.DMSService;
import com.dms.beinone.application.R;
import com.dms.beinone.application.activities.LoginActivity;
import com.dms.beinone.application.managers.AccountManager;
import com.dms.beinone.application.managers.HttpManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dms.beinone.application.DMSService.HTTP_CREATED;

/**
 * Created by BeINone on 2017-08-02.
 */

@SuppressLint("ValidFragment")
public class LogoutDialogFragment extends DialogFragment {

    private Context context;

    public LogoutDialogFragment (Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getContext())
                .setTitle(R.string.logout_dialog_message)
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
    }

    private void logout() {
        Toast.makeText(context,getString(R.string.logout_dialog_message), Toast.LENGTH_SHORT).show();
        AccountManager.setLogined(context,false);
        AccountManager.setToken(context,"JWT ");
        //getTargetFragment().getActivity().
    }
}

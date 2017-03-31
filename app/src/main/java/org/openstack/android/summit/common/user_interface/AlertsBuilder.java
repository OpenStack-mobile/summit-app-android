package org.openstack.android.summit.common.user_interface;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;

import org.openstack.android.summit.R;

/**
 * Created by smarcet on 31/03/17.
 */

public final class AlertsBuilder {


    public static AlertDialog buildAlert(Context context, String title, String message){
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);

        return builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.generic_error_message_ok,  (dialog, id) -> dialog.dismiss() )
                .create();
    }


    public static AlertDialog buildAlert(Context context, @StringRes int title, @StringRes int  message){
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);

        return builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.generic_error_message_ok,  (dialog, id) -> dialog.dismiss() )
                .create();
    }


    public static AlertDialog buildAlert(Context context, @StringRes int title, String message){
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);

        return builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.generic_error_message_ok,  (dialog, id) -> dialog.dismiss() )
                .create();
    }


    public static AlertDialog buildGenericError(Context context){
       return buildAlert(context, R.string.generic_error_title, R.string.generic_error_message);
    }


    public static AlertDialog buildValidationError(Context context, String message){
        return buildAlert(context, R.string.generic_validation_error_title, message);
    }

    public static AlertDialog buildError(Context context, @StringRes int  message){
       return buildAlert(context, R.string.generic_error_title, message);
    }


}

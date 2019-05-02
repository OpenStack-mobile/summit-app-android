package org.openstack.android.summit.common.user_interface;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import com.crashlytics.android.Crashlytics;
import org.openstack.android.summit.R;

import java.lang.ref.WeakReference;

/**
 * Created by smarcet on 31/03/17.
 */


public final class AlertsBuilder {

    public static AlertDialog buildAlert(Context context, String title, String message){
        try{
            androidx.appcompat.app.AlertDialog.Builder  builder = new androidx.appcompat.app.AlertDialog.Builder(context);

            return builder.setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(R.string.generic_error_message_ok,  (dialog, id) -> dialog.dismiss() )
                    .create();
        }
        catch (Exception ex){
            Crashlytics.logException(ex);
        }
        return null;
    }

    public static android.app.AlertDialog buildRawAlert(Context context, @StringRes int title, @StringRes int  message){
        WeakReference<Context> contentRef = new WeakReference<>(context);
        return new android.app.AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setNeutralButton("Close", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    if(contentRef.get() != null && contentRef.get() instanceof Activity){
                        ((Activity)contentRef.get()).finish();
                        contentRef.clear();
                    }
                })
                .create();
    }

    public static android.app.AlertDialog buildRawAlert(Context context, String message){
        WeakReference<Context> contentRef = new WeakReference<>(context);
        return new android.app.AlertDialog.Builder(context)
                .setTitle(R.string.generic_error_title)
                .setMessage(message)
                .setNeutralButton("Close", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    if(contentRef.get() != null && contentRef.get() instanceof Activity){
                        ((Activity)contentRef.get()).finish();
                        contentRef.clear();
                    }
                })
                .create();
    }

    public static AlertDialog buildAlert(Context context, @StringRes int title, @StringRes int  message){
        try {
            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);

            return builder.setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(R.string.generic_error_message_ok, (dialog, id) -> dialog.dismiss())
                    .create();
        }
        catch (Exception ex){
            Crashlytics.logException(ex);
        }
        return null;
    }

    public static AlertDialog buildConfirm(Context context,
                                           String title,
                                           String message,
                                           String positiveText,
                                           String negativeText,
                                           IAlertCommand positiveCommand


    ){
        try {
            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);

            return builder.setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(positiveText, (dialog, id) -> {
                        positiveCommand.execute();
                        dialog.dismiss();
                    })
                    .setNegativeButton(negativeText,  (dialog, id) -> dialog.dismiss())
                    .create();
        }
        catch (Exception ex){
            Crashlytics.logException(ex);
        }
        return null;
    }

    public static AlertDialog buildAlert(Context context, @StringRes int title, String message){
        try{
            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
            return builder.setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(R.string.generic_error_message_ok,  (dialog, id) -> dialog.dismiss() )
                    .create();
        }
        catch (Exception ex){
            Crashlytics.logException(ex);
        }
        return null;
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

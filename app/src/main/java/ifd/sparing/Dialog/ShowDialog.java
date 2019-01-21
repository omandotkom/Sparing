package ifd.sparing.Dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import ifd.sparing.MainActivity;
import ifd.sparing.R;

public class ShowDialog {
    public static void showInformation(Context context, String Message){
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Informasi");
        alertDialog.setMessage(Message);
        alertDialog.setIcon(R.drawable.information_icon);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();

    }
    }

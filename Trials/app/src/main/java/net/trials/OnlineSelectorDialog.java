package net.trials;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

/**
 * Created by pranavsethi on 16/04/15.
 */
public class OnlineSelectorDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("Online Resource: ")
                .setPositiveButton("Educomp Online", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent web = new Intent(Intent.ACTION_VIEW);
                        web.setData(Uri.parse("http://www.educomponline.com/signin.aspx"));
                        startActivity(web);
                    }
                })
                .setNeutralButton("Extra Marks", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent web = new Intent(Intent.ACTION_VIEW);
                        web.setData(Uri.parse("http://dpsrkp.extramarks.com/login/"));
                        startActivity(web);
                    }
                });

        return builder.create();

    }
}
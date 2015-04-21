package net.trials;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;


/**
 * Created by Avichal Rakesh on 4/14/2015.
 */
public class PrimarySelectorDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("Primary Schools: ")
                .setPositiveButton("Vasant Vihar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent VVIntent = new Intent(getActivity(), WebViewActivity.class);
                        VVIntent.putExtra("Title", "DPS Vasant Vihar");
                        VVIntent.putExtra("URL", Config.SCHOOL_RSS + "dpsvv");
                        startActivity(VVIntent);
                    }
                })
                .setNeutralButton("East of Kailash", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent EOKIntent = new Intent(getActivity(), WebViewActivity.class);
                        EOKIntent.putExtra("URL", Config.SCHOOL_RSS + "dpseok");
                        EOKIntent.putExtra("Title", "DPS East of Kailash");
                        startActivity(EOKIntent);
                    }
                });

        return builder.create();

    }


}

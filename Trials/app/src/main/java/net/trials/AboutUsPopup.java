package net.trials;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by Avichal Rakesh on 4/14/2015.
 */
public class AboutUsPopup extends DialogFragment {

    public int Chooser;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        if (Chooser == 0)
            builder.setView(inflater.inflate(R.layout.devs_popup, null));

        if (Chooser == 1)
            builder.setView(inflater.inflate(R.layout.off_details, null))
                    .setTitle("Officials");

        else if (Chooser == 2)
            builder.setView(inflater.inflate(R.layout.easter, null));

        builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return builder.create();
    }

    public void setChooser(int chooser) {
        Chooser = chooser;
    }

}

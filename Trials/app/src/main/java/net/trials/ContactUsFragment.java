package net.trials;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

/**
 * Created by Avichal Rakesh on 4/15/2015.
 */
public class ContactUsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.contact_us);

        Preference Rece = findPreference("reception");
        Preference TDept = findPreference("tdept");
        Preference AdminOff = findPreference("adminoff");
        Preference Clinic = findPreference("clinic");
        Preference Locate = findPreference("locate");
        Preference Email = findPreference("mail");

        Rece.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                Intent ReceCall = new Intent(Intent.ACTION_DIAL);
                ReceCall.setData(Uri.parse("tel:+911126171267"));
                startActivity(ReceCall);

                return true;
            }
        });

        TDept.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                Intent TDeptCall = new Intent(Intent.ACTION_DIAL);
                TDeptCall.setData(Uri.parse("tel:+9111261812927"));
                startActivity(TDeptCall);

                return true;
            }
        });

        AdminOff.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                Intent AdminOffCall = new Intent(Intent.ACTION_DIAL);
                AdminOffCall.setData(Uri.parse("tel:+911126166533"));
                startActivity(AdminOffCall);

                return true;
            }
        });

        Clinic.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                Intent ClinicCall = new Intent(Intent.ACTION_DIAL);
                ClinicCall.setData(Uri.parse("tel:+911126161893"));
                startActivity(ClinicCall);

                return true;
            }
        });

        Locate.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                Intent intent = new Intent(
                        android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://goo.gl/maps/GNRcC"));
                intent.setClassName("com.google.android.apps.maps",
                        "com.google.android.maps.MapsActivity");
                startActivity(intent);

                return true;
            }
        });

        Email.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                Intent i = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:principal@dpsrkp.net"));
                try {
                    startActivity(i);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getActivity(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }

                return true;
            }
        });

    }
}
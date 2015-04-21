package net.trials;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

public class AboutUsFragment extends PreferenceFragment {

    int Press = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.about_us);

        final Preference OffPref = findPreference("official_button");
        Preference DevPref = findPreference("developers_button");
        Preference Easter = findPreference("easter");
        Preference rateApp = findPreference("rate_app");
        Preference FeedBack = findPreference("feedback");

        DevPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                AboutUsPopup DevsP = new AboutUsPopup();
                DevsP.setChooser(0);
                DevsP.show(getActivity().getFragmentManager(), "Developers Popup");

                return true;
            }
        });

        OffPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {

                AboutUsPopup OffP = new AboutUsPopup();
                OffP.setChooser(1);
                OffP.show(getActivity().getFragmentManager(), "Official Popup");

                return true;
            }
        });


        Easter.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                if (Press < 5) {
                    if (Press > 1) {
                        String text = null;
                        text = "Press " + (5 - Press) + " more time";
                        if (Press != 4)
                            text = text + "s";
                        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
                    }
                    Press++;
                } else if (Press == 5) {
                    Press = 0;

                    AboutUsPopup EasterPopup = new AboutUsPopup();
                    EasterPopup.setChooser(2);
                    EasterPopup.show(getActivity().getFragmentManager(), "Easter");
                }

                return true;
            }
        });

        rateApp.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                final String appPackageName = getActivity().getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }

                return true;
            }
        });

        FeedBack.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                Intent i = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:devs.dpsrkp@gmail.com"));
                i.putExtra(Intent.EXTRA_SUBJECT, "Suggestions/Feedback/Complaints/Ways to Improve");
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
package net.trials;

import android.app.ActivityManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by Avichal Rakesh on 3/23/2015.
 */
public class ContactUs extends ActionBarActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_contact_common);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.outline_ic);
            ActivityManager.TaskDescription td = new ActivityManager.TaskDescription("DPS RKP", bm, 0xFF00810d);
            setTaskDescription(td);}

        Toolbar toolbar = (Toolbar) findViewById(R.id.commonTool);
        toolbar.setTitle("Contact Us");
        toolbar.setSubtitle("DPS RKP");
        toolbar.setBackgroundColor(0xFF009688);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getFragmentManager().beginTransaction()
                .replace(R.id.placeholder, new ContactUsFragment()
                ).commit();
    }
}
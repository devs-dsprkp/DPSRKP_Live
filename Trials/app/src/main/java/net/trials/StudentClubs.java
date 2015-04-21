package net.trials;

import android.app.ActivityManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ResourceBundle;

/**
 * Created by Avichal Rakesh on 4/15/2015.
 */
public class StudentClubs extends ActionBarActivity {

    private GridView gridView;
    Controller ctrl;
    String[] TileText = {"Exun Clan", "Digex Clan", "MatSoc", "AEROSS", "RoboKnights", "Business Studies", "Music Club", "Catalyst", "Pachtatva", "PhySoc","Frameworks","Psychology Club"};
    int[] TileImage = {R.drawable.exun, R.drawable.digex, R.drawable.math, R.drawable.aeross, R.drawable.robo, R.drawable.business, R.drawable.music, R.drawable.chem, R.drawable.panch, R.drawable.phy,R.drawable.frame,R.drawable.psy};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common);
        Toolbar toolbar = (Toolbar) findViewById(R.id.common_toolbar);
        toolbar.setTitle("Student Clubs");
        toolbar.setSubtitle("DPS RKP");
        toolbar.setBackgroundColor(0xFF3075da);

        ctrl = (Controller) getApplicationContext();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.outline_ic);
            ActivityManager.TaskDescription td = new ActivityManager.TaskDescription("DPS RKP", bm, 0xFF3075da);
            setTaskDescription(td);
        }

        gridView = (GridView) findViewById(R.id.common_grid);
        CustomGrid mGridAdapter = new CustomGrid(StudentClubs.this, TileText, TileImage);
        gridView.setAdapter(mGridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
//                Toast.makeText(StudentClubs.this, "Press Position: " + position, Toast.LENGTH_SHORT).show();

                switch(position)
                {
                    case 0:
                        if (ctrl.appInstalledOrNot("com.exunclan.exun")){
                            Intent exun = getPackageManager().getLaunchIntentForPackage("com.exunclan.exun");
                            startActivity(exun);
                        }else{
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.exunclan.exun")));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "com.exunclan.exun")));
                            }
                        }
                        break;
                    case 1:
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://digexclan.com")));
                        break;
                    case 2:
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://dpsrkp.net/crusade/")));
                        break;
                    case 3:
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://aeross.org")));
                        break;
                    case 4:
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://roboknights.in")));
                        break;
                    case 5:
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://dpsrkp.net/clubs.asp")));
                        break;
                    case 6:
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://dpsrkp.net/music_clubs.asp")));
                        break;
                    case 7:
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://catalystdpsrkp.tk/")));
                        break;
                    case 8:
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://dpsrkp.net/panchtatva/")));
                        break;
                    case 9:
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://physoc.in/")));
                        break;
                    case 10:
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.frameworksdpsrkp.org/")));
                        break;
                    case 11:
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://dpsrkp.net/clubs.asp")));
                        break;
                }

            }
        });

    }

}

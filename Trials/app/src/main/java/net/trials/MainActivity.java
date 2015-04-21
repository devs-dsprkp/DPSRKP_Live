package net.trials;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gcm.GCMRegistrar;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {
    private static final String NOTIFI_COUNT = "pending_counter";

    class NavItem {
        String mTitle;
        int mIcon;

        public NavItem(String title, int icon) {
            mTitle = title;
            mIcon = icon;
        }
    }

    Controller aController;
    AsyncTask<Void, Void, Void> mRegisterTask;

    public static String name;
    public static String email;
    public static String Class;
    public static String Sec;
    public static String identity;
    public static String type;
    public static Context contextOfApplication;

    ListView mDrawerList;
    RelativeLayout mDrawerPane;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter mAdapter;
    SlidingTabLayout tabs;
    int NumbOfTabs = 3;

    CharSequence[] tabLabels = {"Messages", "Main", "Notifications"};


    ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();


    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SharedPreferences settings = getSharedPreferences("APP_SETTINGS", MODE_PRIVATE);
        Boolean isFirstTime = true;
        isFirstTime = settings.getBoolean("firstRun", true);
        if (isFirstTime) {
            Intent firstRun = new Intent(MainActivity.this, InitializeParent.class);
            firstRun.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            firstRun.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(firstRun);
        }

        //Intent mainIntent = getIntent();
        //int setPage = mainIntent.getIntExtra("Tab", 1);

        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) this.getSystemService(ns);
        nMgr.cancel(0);
        SharedPreferences.Editor editor = getSharedPreferences(NOTIFI_COUNT, MODE_PRIVATE).edit();
        editor.putInt("idName", 0);
        editor.commit();

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setPageTransformer(false, new ZoomOutPageTranformer());
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager(), tabLabels, NumbOfTabs);
        //viewPager.requestDisallowInterceptTouchEvent(true);

        viewPager.setAdapter(mAdapter);
//        Toast.makeText(MainActivity.this, "setPage" + setPage, Toast.LENGTH_SHORT).show();
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        viewPager.setOnPageChangeListener(new MyPageChangeListener());

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(viewPager);

        //viewPager.setCurrentItem(0);
        viewPager.setCurrentItem(1);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.outline_ic);
            ActivityManager.TaskDescription td = new ActivityManager.TaskDescription("DPS RKP", bm, 0xFF00810d);
            setTaskDescription(td);
        }

        mNavItems.add(new NavItem("Schedules and Notices", R.drawable.notices));
        mNavItems.add(new NavItem("School Calendar", R.drawable.calendar));
        mNavItems.add(new NavItem("Achievements", R.drawable.achievements));
        mNavItems.add(new NavItem("Special Highlights", R.drawable.special));
        mNavItems.add(new NavItem("Activities in School", R.drawable.activity));
        mNavItems.add(new NavItem("Inter-School Activities", R.drawable.activity2));
        mNavItems.add(new NavItem("International Events", R.drawable.int_events));
        mNavItems.add(new NavItem("Sports", R.drawable.sports));
        mNavItems.add(new NavItem("Online Resources", R.drawable.online_resource));
        mNavItems.add(new NavItem("Student Clubs", R.drawable.school_clubs));
        mNavItems.add(new NavItem("Primary Schools", R.drawable.primary_school));
        mNavItems.add(new NavItem("Visit Website", R.drawable.main_site));
        mNavItems.add(new NavItem("Contact Us", R.drawable.contact_us));
        mNavItems.add(new NavItem("About App", R.drawable.about_us));


        // DrawerLayout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        // Populate the Navigtion Drawer with options
        mDrawerPane = (RelativeLayout) findViewById(R.id.left_drawer);
        mDrawerList = (ListView) findViewById(R.id.navlist);
        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setAdapter(adapter);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();

        // Drawer Item click listeners
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItemFromDrawer(position);

//                Toast.makeText(MainActivity.this, "Press Position: " + position,Toast.LENGTH_SHORT).show();
                mDrawerLayout.closeDrawer(mDrawerPane);
                switch (position) {
                    case 0:
                        Intent NoticesStart = new Intent(MainActivity.this, WebViewActivity.class);
                        NoticesStart.putExtra("URL", Config.SCHOOL_RSS + "notices-schedule");
                        NoticesStart.putExtra("Title", "Schedules and Notices");
                        startActivity(NoticesStart);
                        break;
                    case 1:
                        Intent calendar = new Intent(MainActivity.this, WebViewActivity.class);
                        calendar.putExtra("URL", "https://www.google.com/calendar/embed?title=Delhi+Public+School+R.K.Puram+Events+Schedule&showPrint=0&showCalendars=0&showTz=0&height=1000&wkst=2&hl=en_GB&bgcolor=%23ffffff&src=fs5nat910ju6ch9lbnjcthrbgk@group.calendar.google.com&color=%230D7813&ctz=Asia/Calcutta");
                        calendar.putExtra("Title", "School Schedule");
                        startActivity(calendar);
                        break;
                    case 2:
                        Intent achievements = new Intent(MainActivity.this, WebViewActivity.class);
                        achievements.putExtra("URL", Config.SCHOOL_RSS + "achievements");
                        achievements.putExtra("Title", "Achievements");
                        startActivity(achievements);
                        break;
                    case 3:
                        Intent higlights = new Intent(MainActivity.this, WebViewActivity.class);
                        higlights.putExtra("URL", Config.SCHOOL_RSS + "special-highlights");
                        higlights.putExtra("Title", "Special Highlights");
                        startActivity(higlights);
                        break;
                    case 4:
                        Intent inAct = new Intent(MainActivity.this, WebViewActivity.class);
                        inAct.putExtra("URL", Config.SCHOOL_RSS + "intra-activities");
                        inAct.putExtra("Title", "Activities");
                        startActivity(inAct);
                        break;
                    case 5:
                        Intent outAct = new Intent(MainActivity.this, WebViewActivity.class);
                        outAct.putExtra("URL", Config.SCHOOL_RSS + "inter-activities");
                        outAct.putExtra("Title", "Interschool Activities");
                        startActivity(outAct);
                        break;
                    case 6:
                        Intent inter = new Intent(MainActivity.this, WebViewActivity.class);
                        inter.putExtra("URL", Config.SCHOOL_RSS + "international-events");
                        inter.putExtra("Title", "International Events");
                        startActivity(inter);
                        break;
                    case 7:
                        Intent sports = new Intent(MainActivity.this, WebViewActivity.class);
                        sports.putExtra("URL", Config.SCHOOL_RSS + "sports");
                        sports.putExtra("Title", "Sports");
                        startActivity(sports);
                        break;
                    case 8:
                        OnlineSelectorDialog Dialog1 = new OnlineSelectorDialog();
                        Dialog1.show(getFragmentManager(), "Online Resource");
                        //online resource
                        break;
                    case 9:
                        Intent clubs = new Intent(MainActivity.this, StudentClubs.class);
                        startActivity(clubs);
                        //student clubs
                        break;
                    case 10:
                        PrimarySelectorDialog dialog = new PrimarySelectorDialog();
                        dialog.show(getFragmentManager(), "Primary School");
                        //primary schools
                        break;
                    case 11:
                        Intent web = new Intent(Intent.ACTION_VIEW);
                        web.setData(Uri.parse("http://dpsrkp.edu.in"));
                        startActivity(web);
                        break;
                    case 12:
                        Intent contact = new Intent(MainActivity.this, ContactUs.class);
                        startActivity(contact);
                        break;
                    case 13:
                        Intent about = new Intent(MainActivity.this, AboutUs.class);
                        startActivity(about);
                        break;
                }
            }
        });

        Boolean OKtoRegister = false;
        OKtoRegister = settings.getBoolean("goRegi", false);

        if (OKtoRegister) {
            Log.w("Regi", "Will Register");
            aController = (Controller) getApplicationContext();

            // Check if Internet present
            if (!aController.isConnectingToInternet()) {

                // Internet Connection is not present
                aController.showAlertDialog(MainActivity.this,
                        "Internet Connection Error",
                        "Please connect to Internet connection", false);
                // stop executing code by return
                return;
            }

            // Getting name, email from intent
            Intent i = getIntent();
            name = aController.getName();
            email = aController.getEmail();
            Class = "-";
            Sec = "-";
            identity = "-";
            type = "-";

            Boolean details = settings.getBoolean("detailsEntered", false);
            String TypeSaved = settings.getString("Type", "-");
            if (details) {
                name = settings.getString("Name", aController.getName());
                type = TypeSaved;
                identity = settings.getString("Identifier", "-");
                if (TypeSaved.equals("Student")) {
                    Class = settings.getString("Class", "-");
                    Sec = settings.getString("Sec", "-");
                }
            }

            // Make sure the device has the proper dependencies.
            GCMRegistrar.checkDevice(this);

            // Make sure the manifest permissions was properly set
            GCMRegistrar.checkManifest(this);

            registerReceiver(mHandleMessageReceiver, new IntentFilter(
                    Config.DISPLAY_MESSAGE_ACTION));

            // Get GCM registration id
            final String regId = GCMRegistrar.getRegistrationId(this);

            // Check if regid already presents
            if (regId.equals("")) {

                // Register with GCM
                GCMRegistrar.register(this, Config.GOOGLE_SENDER_ID);

            } else {

                // Device is already registered on GCM Server
                if (GCMRegistrar.isRegisteredOnServer(this)) {

                    // Skips registration.
//                Toast.makeText(getApplicationContext(), "Already registered with GCM Server", Toast.LENGTH_LONG).show();
                    Log.w("GCM Stat", "Already Registered");
                } else {

                    // Try to register again, but not in the UI thread.
                    // It's also necessary to cancel the thread onDestroy(),
                    // hence the use of AsyncTask instead of a raw thread.

                    final Context context = this;
                    mRegisterTask = new AsyncTask<Void, Void, Void>() {

                        @Override
                        protected Void doInBackground(Void... params) {

                            // Register on our server
                            // On server creates a new user
                            aController.register(context, name, email, Class, Sec, identity, regId, type);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void result) {
                            mRegisterTask = null;
                        }

                    };

                    // execute AsyncTask
                    mRegisterTask.execute(null, null, null);
                }
            }
        }
    }


    private int focusedPage = 0;

    private class MyPageChangeListener extends ViewPager.SimpleOnPageChangeListener {
        @Override
        public void onPageSelected(int position) {
            focusedPage = position;
        }
    }

    // Create a broadcast receiver to get message and show on screen
    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String newMessage = intent.getExtras().getString(Config.EXTRA_MESSAGE);

            // Waking up mobile if it is sleeping
            aController.acquireWakeLock(getApplicationContext());

//            Toast.makeText(getApplicationContext(), "Got Message: " + newMessage, Toast.LENGTH_LONG).show();

            // Releasing wake lock
            aController.releaseWakeLock();
        }
    };

    @Override
    protected void onDestroy() {
        // Cancel AsyncTask
        if (mRegisterTask != null) {
            mRegisterTask.cancel(true);
        }
        try {
            // Unregister Broadcast Receiver
            unregisterReceiver(mHandleMessageReceiver);

            //Clear internal resources.
            GCMRegistrar.onDestroy(this);

        } catch (Exception e) {
            Log.e("UnRegister Receiver Error", "> ");
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {

        if (viewPager.getCurrentItem() != 2 || !mAdapter.backConsumed())
            super.onBackPressed();
    }


    private void selectItemFromDrawer(int position) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.info_button:
                Intent about = new Intent(this, AboutUs.class);
                startActivity(about);
                break;
            case R.id.contact_us:
                Intent contact = new Intent(this, ContactUs.class);
                startActivity(contact);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    class DrawerListAdapter extends BaseAdapter {

        Context mContext;
        ArrayList<NavItem> mNavItems;

        public DrawerListAdapter(Context context, ArrayList<NavItem> navItems) {
            mContext = context;
            mNavItems = navItems;
        }

        @Override
        public int getCount() {
            return mNavItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mNavItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.drawer_item, null);
            } else {
                view = convertView;
            }

            TextView titleView = (TextView) view.findViewById(R.id.title);
            ImageView iconView = (ImageView) view.findViewById(R.id.icon);

            titleView.setText(mNavItems.get(position).mTitle);
            iconView.setImageResource(mNavItems.get(position).mIcon);

            return view;
        }
    }
}

package net.trials;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Avichal Rakesh on 3/23/2015.
 */
public class InitializePageAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created
    //WebViewFragment WebFragment = new WebViewFragment();

    // Build a Constructor and assign the passed Values to appropriate values in the class
    public InitializePageAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;

    }


    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {


        switch (position) {
            case 0:
                return new SplashScreen();

            case 1:
                return new Selector();


        }

        return null;
    }

    /*public boolean backConsumed()
    {
        return WebFragment.checkBackConsumed();
    }*/

    // This method return the titles for the Tabs in the Tab Strip

    /*@Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    // This method return the Number of tabs for the tabs Strip*/

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}

package net.trials;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

/**
 * Created by Avichal Rakesh on 3/23/2015.
 */
public class GridViewFragment extends Fragment {

    GridView MainGrid;

    String[] TileText = {"Notices", "Calendar", "Achievements", "Sports", "Assembly Gallery", "Activities"};
    int[] TileImage = {R.drawable.notices_main, R.drawable.cal_main, R.drawable.achievements_main, R.drawable.sports_main, R.drawable.assembly_main, R.drawable.activities_main};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.main_page_fragment, container, false);
        CustomGrid GridAdapter = new CustomGrid(view.getContext(), TileText, TileImage);
        MainGrid = (GridView) view.findViewById(R.id.grid);
        MainGrid.setAdapter(GridAdapter);
        MainGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                switch (position) {

                    case 0:
                        Intent NoticesStart = new Intent(getActivity(), WebViewActivity.class);
                        NoticesStart.putExtra("URL", Config.SCHOOL_RSS + "notices-schedule");
                        NoticesStart.putExtra("Title", "Schedules and Notices");
                        startActivity(NoticesStart);
                        break;
                    case 1:
                        Intent Calendar = new Intent(getActivity(), WebViewActivity.class);
                        Calendar.putExtra("URL", "http://goo.gl/nXqZw2");
                        Calendar.putExtra("Title", "School Schedule");
                        startActivity(Calendar);
                        break;
                    case 2:
                        Intent achievements = new Intent(getActivity(), WebViewActivity.class);
                        achievements.putExtra("URL", Config.SCHOOL_RSS + "achievements");
                        achievements.putExtra("Title", "Achievements");
                        startActivity(achievements);
                        break;
                    case 3:
                        Intent sports = new Intent(getActivity(), WebViewActivity.class);
                        sports.putExtra("URL", Config.SCHOOL_RSS + "sports");
                        sports.putExtra("Title", "Sports");
                        startActivity(sports);
                        break;
                    case 4:
                        Intent assembly = new Intent(getActivity(), WebViewActivity.class);
                        assembly.putExtra("URL", Config.SCHOOL_RSS + "assembly");
                        assembly.putExtra("Title", "Assembly");
                        startActivity(assembly);
                        break;
                    case 5:
                        Intent activities = new Intent(getActivity(), WebViewActivity.class);
                        activities.putExtra("URL", Config.SCHOOL_RSS + "inter-activities");
                        activities.putExtra("Title", "Interschool Activities");
                        startActivity(activities);
                        break;
                }
            }
        });


        return view;

    }

}

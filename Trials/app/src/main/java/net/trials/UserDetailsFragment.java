package net.trials;


import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.InputStream;

/**
 * Created by Avichal Rakesh on 4/20/2015.
 */
public class UserDetailsFragment extends Fragment {

    private SQLiteAdapter mySQLAdapter;
    private SimpleCursorAdapter myCursorAdapter;
    private ListView list;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.user_details, container, false);

        list = (ListView) v.findViewById(R.id.list_view);
        TextView invisibleText = (TextView) v.findViewById(R.id.invisible_text);
        TextView namePlaceholder = (TextView) v.findViewById(R.id.name_placeholder);
        TextView numberPlaceholder = (TextView) v.findViewById(R.id.number_placeholder);
        TextView classSecPlaceholder = (TextView) v.findViewById(R.id.class_sec_placeholder);
       // ImageButton addDetails = (ImageButton) v.findViewById(R.id.add_desc)

        final SharedPreferences userData;
        userData = getActivity().getSharedPreferences("APP_SETTINGS", getActivity().MODE_PRIVATE);
        //isFirstTime = settings.getBoolean("firstRun", true);
        String Type = userData.getString("Type", null);
        if ("Teacher".equals(userData.getString("Type", null))) {
            classSecPlaceholder.setVisibility(TextView.GONE);
            namePlaceholder.setText(userData.getString("Name", null));
            numberPlaceholder.setText(userData.getString("Identifier", null));

        } else if ("Student".equals(userData.getString("Type", null))) {
            DownloadImageTask task = new DownloadImageTask((ImageView) v.findViewById(R.id.user_image));
            task.execute(Config.IMAGE_PULL_BASE + userData.getString("Identifier", null) + ".jpg");

            classSecPlaceholder.setText(userData.getString("Class", null) + " '" + userData.getString("Sec", null) + "'");
            namePlaceholder.setText(userData.getString("Name", null));
            numberPlaceholder.setText(userData.getString("Identifier", null));
        } else {
            classSecPlaceholder.setVisibility(TextView.GONE);
            namePlaceholder.setText("No Details Shared");
            numberPlaceholder.setVisibility(TextView.GONE);
            invisibleText.setText("Please provide your details to receive priority notifications.");
        }

        displayListView();

        if (list.getAdapter().getCount() == 0 || userData.getString("Type", null) == null) {
            list.setVisibility(ListView.GONE);
            invisibleText.setVisibility(TextView.VISIBLE);
        }

        //

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView text = (TextView) view.findViewById(R.id.text);

                NotificationDialog dialog = new NotificationDialog();
                dialog.settext(text.getText().toString());
                dialog.show(getActivity().getFragmentManager(), "Notification");
            }
        });


        return v;
    }

    private void displayListView() {


        mySQLAdapter = new SQLiteAdapter(getActivity());
        mySQLAdapter.open();

        Cursor cursor = mySQLAdapter.fetchAllHistory();

        String[] source = new String[]{
                mySQLAdapter.KEY_CONTENT,
                mySQLAdapter.KEY_DATE};
        int[] destination = new int[]{R.id.text, R.id.date_text};

        myCursorAdapter = new SimpleCursorAdapter(getActivity(), R.layout.list_item, cursor, source, destination, 0);
        list.setAdapter(myCursorAdapter);

        mySQLAdapter.close();
    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap Avatar = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                Avatar = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return Avatar;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}



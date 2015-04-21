package net.trials;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by Avichal Rakesh on 4/20/2015.
 */
public class CustomCursorAdapter{

   /* LayoutInflater cursorInflater;

    // Default constructor
    public MyCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        cursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    public void bindView(View view, Context context, Cursor cursor) {
        TextView textViewTitle = (TextView) view.findViewById(R.id.text);
        String title = cursor.getString( cursor.getColumnIndex( MyTable.COLUMN_TITLE ) )
        textViewTitle.setText(title);
    }

    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // R.layout.list_row is your xml layout for each row
        return cursorInflater.inflate(R.layout.list_item, parent, false);
    }*/

}

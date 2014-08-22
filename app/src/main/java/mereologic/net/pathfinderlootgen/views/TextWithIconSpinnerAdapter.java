package mereologic.net.pathfinderlootgen.views;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.util.List;

import mereologic.net.pathfinderlootgen.R;

/**
 * Created by Doug on 22/08/2014.
 */
public class TextWithIconSpinnerAdapter extends ArrayAdapter<String> {

    private final List<Integer> icons;

    public TextWithIconSpinnerAdapter(Context context, List<String> items, List<Integer> icons) {
        super(context, R.layout.icon_and_text, R.id.right_text, items);
        this.icons = icons;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View iconAndTextView = super.getDropDownView(position, convertView, parent);
        ((ImageView) iconAndTextView.findViewById(R.id.left_icon)).setImageResource(icons.get(position));
        return iconAndTextView;
    }
}

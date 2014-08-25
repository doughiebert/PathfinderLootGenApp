package mereologic.net.pathfinderlootgen;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.SimpleAdapter;
import android.widget.SpinnerAdapter;

import java.util.Arrays;
import java.util.List;

import mereologic.net.pathfinderlootgen.views.TextWithIconSpinnerAdapter;

/**
 * Created by Doug on 22/08/2014.
 */
public class DropDownNavigation {

    public static final String START_OVER = "Start Over";
    public static final String ABOUT = "About";
    public static final String SETTINGS = "Settings";

    private static final List<String> DROP_DOWN_TEXT = Arrays.asList(START_OVER, ABOUT, SETTINGS);
    private static final List<Integer> DROP_DOWN_ICONS = Arrays.asList(R.drawable.icon_start_over, R.drawable.icon_about, android.R.drawable.ic_menu_preferences);

    private DropDownNavigation() {}

    public static void addDropDownNav(ActionBarActivity activity) {

        SpinnerAdapter adapter = new TextWithIconSpinnerAdapter(activity, DROP_DOWN_TEXT, DROP_DOWN_ICONS);
        ActionBar actionBar = activity.getSupportActionBar();

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setListNavigationCallbacks(adapter, new NavigationListener(activity));
    }

    private static class NavigationListener implements ActionBar.OnNavigationListener {

        private final ActionBarActivity activity;
        private boolean initialized = false;

        private NavigationListener(ActionBarActivity activity) {
            this.activity = activity;
        }

        @Override
        public boolean onNavigationItemSelected(int position, long noIdeaWhatThisId) {

            // for some reason Android calls onNavigationItemSelected when the action bar is created?
            if(!initialized) {
                initialized = true;
                return true;
            }

            Log.i("navitemsel", "picked " + position);
            switch(position) {
                case 0: // start over
                    this.activity.startActivity(new Intent(this.activity, ParameterizerActivity.class));
                    return true;

                case 1: // about
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setMessage(R.string.about);
                    builder.setPositiveButton("Great!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                    return true;
                case 2: // preferences
                    this.activity.startActivity(new Intent(this.activity, SettingsActivity.class));
                    return true;
            }
            return false;
        }
    }
}

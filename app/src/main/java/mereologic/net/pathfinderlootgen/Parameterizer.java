package mereologic.net.pathfinderlootgen;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Parameterizer extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameterizer);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new ParameterizerFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.parameterizer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void roll(View view) {
        Object level = ((Spinner) findViewById(R.id.level_spinner)).getSelectedItem();
        Logger.getAnonymousLogger().info("hello!");
        startActivity(new Intent(this, Roller.class)
                .putExtra(Roller.LEVEL, ((Integer) level).intValue()));
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class ParameterizerFragment extends Fragment {

        public ParameterizerFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_parameterizer, container, false);

            addLevelOptions(rootView);

            return rootView;
        }

        private void addLevelOptions(View rootView) {
            ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(rootView.getContext(), android.R.layout.simple_spinner_item, oneUpTo(20));
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            ((Spinner) rootView.findViewById(R.id.level_spinner)).setAdapter(adapter);
        }

        private List<Integer> oneUpTo(int max) {
            List<Integer> levels = new ArrayList<Integer>(max);
            for (int i = 0; i < max; i++) {
                levels.add(i + 1);
            }
            return levels;
        }
    }
}

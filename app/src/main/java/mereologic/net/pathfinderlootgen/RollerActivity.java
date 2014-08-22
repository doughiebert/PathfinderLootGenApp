package mereologic.net.pathfinderlootgen;

import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.logging.Logger;

import android.support.v7.app.ActionBarActivity;

import static android.support.v4.app.LoaderManager.LoaderCallbacks;


public class RollerActivity extends ActionBarActivity {

    private static final String TREASURE_PARCEL_KEY = "mereologic.net.roller.treasure";
    private static final int ROLLER_LOADER = 1;
    public static String LEVEL = "mereologic.net.roller.level";

    private GestureDetector gestureDetector;
    private ArrayList<Treasure> treasure;
    private ArrayAdapter<Treasure> treasureArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roller);
        showProgressSpinnerWhenRolling();
        if (savedInstanceState == null) {
            treasure = new ArrayList<Treasure>();
            rollTreasureAsync();
        } else {
            treasure = savedInstanceState.getParcelableArrayList(TREASURE_PARCEL_KEY);
            Logger.getAnonymousLogger().info("restored parcelled list of " + treasure.size() + " items.");
        }

        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                Logger.getAnonymousLogger().info("fling! vx: " + velocityX + "; vy: " + velocityY);
                if (velocityX < -50 /* dip/s */) {

                    return true;
                }
                return false;
            }
        });

        createTreasureListAdapter(treasure);
        enableRerollItemOnLongClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.roller, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_reroll:
                treasure.clear();
                treasureArrayAdapter.notifyDataSetChanged();
                rollTreasureAsync();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showProgressSpinnerWhenRolling() {
        // Create a progress bar to display while the list loads
        ProgressBar progressBar = new ProgressBar(this);
        progressBar.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER));
        progressBar.setIndeterminate(true);
        getListView().setEmptyView(progressBar);

        // Must add the progress bar to the root of the layout
        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        root.addView(progressBar);
    }

    private ListView getListView() {
        return (ListView) findViewById(R.id.loot_list);
    }

    private void enableRerollItemOnLongClick() {
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                treasure.set(position, treasure.get(position).reroll());
                treasureArrayAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    private void rollTreasureAsync() {
        Logger.getAnonymousLogger().info("restartLoader");
        getSupportLoaderManager().restartLoader(ROLLER_LOADER, getIntent().getExtras(), new LoaderCallbacks<ArrayList<Treasure>>() {

            @Override
            public Loader<ArrayList<Treasure>> onCreateLoader(int i, final Bundle bundle) {
                Logger.getAnonymousLogger().info("creating async loader");
                return new AsyncTaskLoader<ArrayList<Treasure>>(RollerActivity.this) {
                    @Override
                    public ArrayList<Treasure> loadInBackground() {
                        return LootGenerator.generate(bundle.getInt(LEVEL), LootGenerator.Progression.SLOW);
                    }
                };
            }

            @Override
            public void onLoadFinished(Loader<ArrayList<Treasure>> arrayListLoader, ArrayList<Treasure> treasures) {
                Logger.getAnonymousLogger().info("async load finished");
                treasure.clear();
                treasure.addAll(treasures);
                treasureArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLoaderReset(Loader<ArrayList<Treasure>> arrayListLoader) {
                Logger.getAnonymousLogger().info("loader is reset");
                treasure.clear();
                treasureArrayAdapter.notifyDataSetChanged();
            }
        }).forceLoad();
    }

    private void createTreasureListAdapter(final ArrayList<Treasure> treasures) {
        Logger.getAnonymousLogger().info("installing adapter");
        treasureArrayAdapter = new ArrayAdapter<Treasure>(this, android.R.layout.simple_list_item_2, treasures) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                Logger.getAnonymousLogger().info("rendering item at position " + position);
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(android.R.layout.simple_list_item_2, null);
                }
                TextView top = (TextView) convertView.findViewById(android.R.id.text1);
                TextView bottom = (TextView) convertView.findViewById(android.R.id.text2);

                top.setText(getItem(position).getName());
                bottom.setText(getItem(position).getValue().getName());

                return convertView;
            }


        };

        treasureArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
            supportInvalidateOptionsMenu();
            }
        });
        getListView().setAdapter(treasureArrayAdapter);
    }

    public Coins totalValue(ArrayList<? extends Treasure> treasure) {
        Coins total = Coins.Type.SILVER.coins(0);
        for (Treasure t : treasure) {
            total = total.add(t.getValue());
        }
        return total;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_value);
        Coins totalValue = totalValue(this.treasure);
        item.setTitle(getResources().getString(R.string.loot_value, totalValue.getQuantity(), totalValue.getType().toString()));
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Logger.getAnonymousLogger().info("onSaveInstanceState; parcelling " + treasure.size() + " entities.");
        outState.putParcelableArrayList(TREASURE_PARCEL_KEY, treasure);
    }
}

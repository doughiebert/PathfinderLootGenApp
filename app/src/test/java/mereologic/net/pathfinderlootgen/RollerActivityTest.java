package mereologic.net.pathfinderlootgen;

import android.app.Activity;
import android.content.Intent;
import android.widget.ListView;

import org.hamcrest.core.IsNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowListView;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

@Config(manifest = "/src/main/AndroidManifest.xml", emulateSdk = 16, reportSdk = 10)
@RunWith(RobolectricTestRunner.class)
public class RollerActivityTest {

    @Test
    public void testRollerActivity() throws Exception {
        Intent rollLevel1Intent = new Intent(Robolectric.getShadowApplication().getApplicationContext(), RollerActivity.class);
        rollLevel1Intent.putExtra(RollerActivity.LEVEL, 1);

        Activity activity = Robolectric.buildActivity(RollerActivity.class)
                .withIntent(rollLevel1Intent)
                .create()
                .get();

        // rolling occurs in a background task
        Robolectric.runUiThreadTasksIncludingDelayedTasks();
        Robolectric.runBackgroundTasks();

        assertThat(activity, notNullValue());
        ListView rawLootList = (ListView) activity.findViewById(R.id.loot_list);
        ShadowListView shadowLootList = Robolectric.shadowOf(rawLootList);
        assertThat(shadowLootList.getOnTouchListener(), IsNull.notNullValue());
        assertThat(rawLootList.getChildCount(), not(equalTo(0)));
    }
}

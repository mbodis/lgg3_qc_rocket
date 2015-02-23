package sk.svb.lgg3.svb_circlecase_rocket.activity;

import sk.svb.lgg3.svb_circlecase_rocket.R;
import sk.svb.lgg3.svb_circlecase_rocket.adapter.GameStatsAdapter;
import sk.svb.lgg3.svb_circlecase_rocket.logic.GameStats;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class FullScreenActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		setTitle(R.string.high_score);

		refreshList();
	}

	private void refreshList() {
		setListAdapter(new GameStatsAdapter(this, R.layout.adapter_gamestats,
				GameStats.find(GameStats.class, null, null, null, "score desc", "10")));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_delete) {
			GameStats.deleteAll(GameStats.class);
			Toast.makeText(getApplicationContext(),
					getString(R.string.data_removed), Toast.LENGTH_SHORT)
					.show();
			refreshList();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}

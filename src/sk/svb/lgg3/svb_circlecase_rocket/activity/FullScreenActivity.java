package sk.svb.lgg3.svb_circlecase_rocket.activity;

import java.util.ArrayList;
import java.util.List;

import sk.svb.lgg3.svb_circlecase_rocket.R;
import sk.svb.lgg3.svb_circlecase_rocket.adapter.GameStatsAdapter;
import sk.svb.lgg3.svb_circlecase_rocket.dialog.DeleteAllScores;
import sk.svb.lgg3.svb_circlecase_rocket.dialog.HighScoreNameFragmentDialog;
import sk.svb.lgg3.svb_circlecase_rocket.logic.GameStats;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Toast;

public class FullScreenActivity extends ListActivity {

	List<GameStats> list = new ArrayList<GameStats>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		setTitle(R.string.high_score);

		refreshList();
		getListView().setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {

				GameStats gs = (GameStats) parent.getItemAtPosition(position);
				HighScoreNameFragmentDialog f = HighScoreNameFragmentDialog
						.newInstance(gs.getName(), gs.getId());
				f.show(getFragmentManager(), HighScoreNameFragmentDialog.TAG);
				return false;
			}
		});
	}

	public void refreshList() {
		list = GameStats.find(GameStats.class, null, null, null, "score desc",
				"10");
		setListAdapter(new GameStatsAdapter(this, R.layout.adapter_gamestats,
				list));

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

			DeleteAllScores f = DeleteAllScores.newInstance();
			f.show(getFragmentManager(), DeleteAllScores.TAG);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}

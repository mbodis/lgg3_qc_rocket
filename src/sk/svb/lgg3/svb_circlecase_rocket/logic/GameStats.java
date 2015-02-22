package sk.svb.lgg3.svb_circlecase_rocket.logic;

import com.orm.SugarRecord;

public class GameStats extends SugarRecord<GameStats> {

	private int score = 0;
	private String date = "";

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}

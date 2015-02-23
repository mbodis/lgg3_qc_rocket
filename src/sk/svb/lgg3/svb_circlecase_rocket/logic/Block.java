package sk.svb.lgg3.svb_circlecase_rocket.logic;

import java.util.Random;

import android.content.Context;
import android.graphics.Rect;
import android.os.Vibrator;

public class Block {

	public static final int GATE_WIDTH = 200;
	public static final int GATE_HEIGTHT = 80;	
	
	public static final int MIN_SPEED = 45;
	public static final int MAX_SPEED = 75;
	
	int width, height;
	int speed = MIN_SPEED;
	int gate_1 = 400;
	int gate_2 = width - GATE_WIDTH - gate_1;
	int y = 0;
	Random rand;
	boolean visited = false;

	public Block(int width, int height) {
		this.width = width;
		this.height = height;
		rand = new Random();
	}

	public void update() {
		y = y + speed;
		if (y >= height + GATE_HEIGTHT * 3 / 2) {
			y = -80;
			visited = false;

			gate_1 = rand.nextInt(width - GATE_WIDTH - 100 * 2) + 100;
		}
	}

	public Rect getRect1() {
		return new Rect(0, y, gate_1, y + GATE_HEIGTHT);

	}

	public Rect getRect2() {
		return new Rect(gate_1 + GATE_WIDTH, y, width, y + GATE_HEIGTHT);
	}

	public boolean throughGate(int rx, int ry) {
		if (!visited) {
			if (rx > gate_1 && rx < gate_1 + GATE_WIDTH && ry > y
					&& ry < y + GATE_HEIGTHT) {
				visited = true;
				
				if (speed < MAX_SPEED){
					speed +=1;
				}
				
				return true;
			}
		}

		return false;
	}

	public boolean detectCollision(int rx, int ry) {
		if ((rx > 0 && rx < gate_1 && ry > y && ry < y + GATE_HEIGTHT)
				|| (rx > gate_1 + GATE_WIDTH && rx < width && ry > y && ry < y
						+ GATE_HEIGTHT)) {			
			return true;
		} else {
			return false;
		}
	}
	
}

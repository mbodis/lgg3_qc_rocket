package sk.svb.lgg3.svb_circlecase_rocket.view;

import sk.svb.lgg3.svb_circlecase_rocket.game.QcAccelerometerActivity;
import sk.svb.lgg3.svb_circlecase_rocket.logic.Block;
import sk.svb.lgg3.svb_circlecase_rocket.logic.Stars;
import sk.svb.lgg3.svb_circlecase_rocket.logic.Stars.Star;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class AccelerometerView extends SurfaceView implements Callback {

	private CanvasThread canvasThread;

	QcAccelerometerActivity act;
	Paint c_red, c_bl, c_wh, c_wht;
	int x = 0;
	int y = 0;

	// rocket size
	int R_SIZE_W = 30;
	int R_SIZE_H = 30;

	int W = 1046;
	int H = 1046;

	private Stars stars;
	private Block block;

	public int points = 0;

	public AccelerometerView(Context context) {
		super(context);
		// TODO Auto-generated method stub
	}

	public AccelerometerView(Context context, AttributeSet attrs) {
		super(context, attrs);

		init();
		this.getHolder().addCallback(this);
		this.canvasThread = new CanvasThread(getHolder());
		this.setFocusable(true);
		setWillNotDraw(false);
		stars = new Stars(W, H);
		block = new Block(W, H);

	}

	public AccelerometerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated method stub
	}

	private void init() {
		c_red = new Paint();
		c_red.setColor(Color.RED);

		c_bl = new Paint();
		c_bl.setColor(Color.BLACK);

		c_wh = new Paint();
		c_wh.setStrokeWidth(4);
		c_wh.setColor(Color.WHITE);

		c_wht = new Paint();
		c_wht.setColor(Color.WHITE);
	}

	public void setActivit(Activity act) {
		this.act = (QcAccelerometerActivity) act;
	}

	protected void myDraw(Canvas canvas) {

		canvas.drawARGB(255, 0, 0, 0);

		for (Star s : stars.getStarList()) {
			canvas.drawCircle(s.x, s.y, s.size, c_wht);
		}
		stars.updateStars();

		int xx = (int) ((double) canvas.getWidth() / 80 * x);
		int yy = (int) ((double) canvas.getHeight() / 80 * y);

		int rx = canvas.getWidth() / 2 + xx;
		int ry = canvas.getHeight() / 2 + yy;

		canvas.drawLine(rx, ry - R_SIZE_H, rx + R_SIZE_W, ry + R_SIZE_H, c_wh);
		canvas.drawLine(rx + R_SIZE_W, ry + R_SIZE_H, rx - R_SIZE_W, ry
				+ R_SIZE_H, c_wh);
		canvas.drawLine(rx - R_SIZE_W, ry + R_SIZE_H, rx, ry - R_SIZE_H, c_wh);

		canvas.drawRect(block.getRect1(), c_wht);
		canvas.drawRect(block.getRect2(), c_wht);
		block.update(points);
		if (block.throughGate(rx, ry)) {
			points++;
			updateScore();
			doVibrate(act, 20);
		}
		if (block.detectCollision(rx, ry)){
			doVibrate(act, 500);
			endGame();
		}

	}

	public void updateScore() {
		Intent i = new Intent("game_update");
		i.putExtra("score", true);
		act.sendBroadcast(i);
	}

	public void endGame() {
		Intent i = new Intent("game_update");
		i.putExtra("end", true);
		act.sendBroadcast(i);
	}

	public void setCoords(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
	}

	public void startDrawImage() {
		canvasThread.setRunning(true);
		canvasThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry = true;
		canvasThread.setRunning(false);
		while (retry) {
			try {
				canvasThread.join();
				retry = false;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private class CanvasThread extends Thread {
		private SurfaceHolder surfaceHolder;
		private boolean isRun = false;

		public CanvasThread(SurfaceHolder holder) {
			this.surfaceHolder = holder;
		}

		public void setRunning(boolean run) {
			this.isRun = run;
		}

		public boolean isRunning() {
			return this.isRun;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Canvas c;

			while (isRun) {
				c = null;
				try {
					c = this.surfaceHolder.lockCanvas(null);
					if (c != null) {
						synchronized (this.surfaceHolder) {
							AccelerometerView.this.myDraw(c);
						}
					}
				} finally {
					if (c != null)
						this.surfaceHolder.unlockCanvasAndPost(c);
				}

				try {
					sleep(50); // 20 redraw times a second
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public void doVibrate(Context ctx, long milis) {
		Vibrator v = (Vibrator) ctx.getSystemService(Context.VIBRATOR_SERVICE);
		v.vibrate(milis);
	}
}

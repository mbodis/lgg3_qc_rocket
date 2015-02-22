package sk.svb.lgg3.svb_circlecase_rocket.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;

public class AccelerometerView extends SurfaceView implements Callback {

	private CanvasThread canvasThread;

	Paint c_red, c_bl, c_wh;
	int x = 0;
	int y = 0;

	// rocket size
	int R_SIZE_W = 30;
	int R_SIZE_H = 30;

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
		
	}

	protected void myDraw(Canvas canvas) {

		canvas.drawARGB(255, 0, 0, 0);

		int xx = (int) ((double) canvas.getWidth() / 80 * x);
		int yy = (int) ((double) canvas.getHeight() / 80 * y);

		int cx = canvas.getWidth() / 2 + xx;
		int cy = canvas.getHeight() / 2 + yy;

		// canvas.drawCircle(cx , cy, 10, c_red);

		canvas.drawLine(cx, cy - R_SIZE_H, cx + R_SIZE_W, cy + R_SIZE_H, c_wh);
		canvas.drawLine(cx + R_SIZE_W, cy + R_SIZE_H, cx - R_SIZE_W, cy
				+ R_SIZE_H, c_wh);
		canvas.drawLine(cx - R_SIZE_W, cy + R_SIZE_H, cx, cy - R_SIZE_H, c_wh);

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
}

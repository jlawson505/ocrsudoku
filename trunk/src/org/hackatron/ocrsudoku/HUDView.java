package org.hackatron.ocrsudoku;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.SystemClock;
import android.view.View;

class HUDView extends View {

	private final static int SAMPLE_PERIOD_FRAMES = 12;
	private final static float SAMPLE_FACTOR = 1.0f / SAMPLE_PERIOD_FRAMES;

	private Paint _paint;

	private long _startTime;
	private int _frames;
	private int _msPerFrame;

	private int[] _sudukoArray = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 2, 3,
			4, 5, 6, 7, 8, 9, 1, 3, 4, 5, 6, 7, 8, 9, 1, 2, 4, 5, 6, 7, 8, 9,
			1, 2, 3, 5, 6, 7, 8, 9, 1, 2, 3, 4, 6, 7, 8, 9, 1, 2, 3, 4, 5, 7,
			8, 9, 1, 2, 3, 4, 5, 6, 8, 9, 1, 2, 3, 4, 5, 6, 7, 9, 1, 2, 3, 4,
			5, 6, 7, 8 };

	public HUDView(Context context) {
		super(context);

		_paint = new Paint();

		_paint.setColor(Color.WHITE);
		_paint.setTextSize(20);
		_paint.setAntiAlias(true);
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		drawSuduko(canvas);
		drawFrameRate(canvas);

		invalidate();
	}

	private void drawSuduko(Canvas canvas) {

		float sudukoWidth = (float) canvas.getWidth() / 1.5f;
		float sudukoHeight = (float) canvas.getHeight() / 1.5f;

		float length = 0.0f;
		if (sudukoWidth > sudukoHeight) {
			length = sudukoHeight;
		} else {
			length = sudukoWidth;
		}

		float xOffset = (canvas.getWidth() - length) / 2.0f;
		float yOffset = (canvas.getHeight() - length) / 2.0f;

		// TODO: Check if rendering numbers in groups are faster...
		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 9; y++) {
				int value = _sudukoArray[y * 9 + x];

				float xPosition = xOffset + (float) x / 9.0f * length;
				float yPosition = yOffset + (float) y / 9.0f * length;

				canvas.drawText(Integer.toString(value), xPosition, yPosition,
						_paint);
			}
		}
	}

	private void drawFrameRate(Canvas canvas) {
		long time = SystemClock.uptimeMillis();
		if (_startTime == 0) {
			_startTime = time;
		}
		if (_frames++ == SAMPLE_PERIOD_FRAMES) {
			_frames = 0;
			long delta = time - _startTime;
			_startTime = time;
			_msPerFrame = (int) (delta * SAMPLE_FACTOR);
		}
		if (_msPerFrame > 0) {
			canvas.drawText(Integer.toString(_frames) + "ms/frame", 5.0f, 25.0f, _paint);
		}
	}

}
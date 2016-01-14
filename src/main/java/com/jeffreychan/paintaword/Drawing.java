package com.jeffreychan.paintaword;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class Drawing extends View {

	private Paint currentPaint;
	Context context;
	private static final int MAX_PATHS = 100;
	ArrayList<Paint> mPaint = new ArrayList<>(MAX_PATHS);
	ArrayList<Path> pathList = new ArrayList<>(MAX_PATHS);

	private float mX, mY;
	private static final float TOLERANCE = 5;
	public static int index = 0;
	boolean undo = false;
	private Map<Path, Paint> colorsMap = new HashMap<>();
	private Map<String, Float> widthMap = new HashMap<>();
	public String myWidth = "Medium";

	public Drawing(Context c, AttributeSet attrs) {
		super(c, attrs);
		context = c;

		// Initialize all the paths and paints	
		for (int i = 0; i < MAX_PATHS; i++) {
			pathList.add(i, new Path());
			mPaint.add(i, new Paint());
			mPaint.get(i).setAntiAlias(true);
			mPaint.get(i).setColor(Color.BLACK);
			mPaint.get(i).setStyle(Paint.Style.STROKE);
			mPaint.get(i).setStrokeJoin(Paint.Join.ROUND);
			mPaint.get(i).setStrokeWidth(4f);
			colorsMap.put(pathList.get(i), mPaint.get(i));
		}

		widthMap.put("1", 1f);
		widthMap.put("2", 2f);
		widthMap.put("4", 4f);
		widthMap.put("8", 8f);
		widthMap.put("16", 16f);
		widthMap.put("32", 32f);
		widthMap.put("64", 64f);
		widthMap.put("128", 128f);
		widthMap.put("256", 128f);


		currentPaint = mPaint.get(0);

	}

	// override onSizeChanged
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		// your Canvas will draw onto the defined Bitmap
		Bitmap mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		new Canvas(mBitmap);
	}

	// override onDraw
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		for (int i = 0; i <= index; i++) {
			canvas.drawPath(pathList.get(i), colorsMap.get(pathList.get(i)));
		}
	}

	private void startTouch(float x, float y) {
		pathList.get(index).moveTo(x, y);
		mX = x;
		mY = y;
	}

	private void moveTouch(float x, float y) {
		float dx = Math.abs(x - mX);
		float dy = Math.abs(y - mY);
		if (dx >= TOLERANCE || dy >= TOLERANCE) {

			pathList.get(index).quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
			mX = x;
			mY = y;
		}
	}

	private void upTouch() {
		pathList.get(index).lineTo(mX, mY);
	}

	public void clearCanvas() {

		if (index >= 100) {
			pathList.subList(100, pathList.size()).clear();
			pathList.trimToSize();
			mPaint.subList(100, mPaint.size()).clear();
			mPaint.trimToSize();
		}


		index = 0;
		for (int i = 0; i < pathList.size(); i++) {
			pathList.get(i).rewind();
			pathList.get(i).moveTo(mX, mY);
			mPaint.get(i).setColor(currentPaint.getColor());
			mPaint.get(i).setStrokeWidth(widthMap.get(myWidth));
		}

		invalidate();
	}

	public void undoCanvas() {

		if (index >= 1) {

			index -= 1;

			pathList.get(index).rewind();
			pathList.get(index).moveTo(mX, mY);
			mPaint.get(index).setColor(currentPaint.getColor());

		}

		for (int i = index; i < mPaint.size(); i++) {
			mPaint.get(i).setStrokeWidth(widthMap.get(myWidth));
		}

		invalidate();

	}

	public void setColor(String str) {
		switch (str) {
			case "black":
				for (int i = index; i < mPaint.size(); i++) {
					mPaint.get(i).setColor(Color.BLACK);
				}
				invalidate();
				currentPaint = colorsMap.get(pathList.get(index));
				break;
			case "red":
				for (int i = index; i < mPaint.size(); i++) {
					mPaint.get(i).setColor(Color.RED);
				}
				invalidate();
				currentPaint = colorsMap.get(pathList.get(index));
				break;
			case "green":
				for (int i = index; i < mPaint.size(); i++) {
					mPaint.get(i).setColor(Color.GREEN);
				}
				invalidate();
				currentPaint = colorsMap.get(pathList.get(index));
				break;
			case "blue":
				for (int i = index; i < mPaint.size(); i++) {
					mPaint.get(i).setColor(Color.BLUE);
				}
				invalidate();
				currentPaint = colorsMap.get(pathList.get(index));
				break;

			case "orange":
				for (int i = index; i < mPaint.size(); i++) {
					mPaint.get(i).setColor(Color.rgb(255, 127, 0));
				}
				invalidate();
				currentPaint = colorsMap.get(pathList.get(index));
				break;
			case "purple":
				for (int i = index; i < mPaint.size(); i++) {
					mPaint.get(i).setColor(Color.rgb(102, 0, 153));
				}
				invalidate();
				currentPaint = colorsMap.get(pathList.get(index));
				break;
			case "yellow":
				for (int i = index; i < mPaint.size(); i++) {
					mPaint.get(i).setColor(Color.YELLOW);
				}
				invalidate();
				currentPaint = colorsMap.get(pathList.get(index));
				break;
			case "gray":
				for (int i = index; i < mPaint.size(); i++) {
					mPaint.get(i).setColor(Color.LTGRAY);
				}
				invalidate();
				currentPaint = colorsMap.get(pathList.get(index));
				break;
			case "white":
				for (int i = index; i < mPaint.size(); i++) {
					mPaint.get(i).setColor(Color.WHITE);
				}
				invalidate();
				currentPaint = colorsMap.get(pathList.get(index));
				break;
		}


	}

	public void setWidth(String str) {
		switch (str) {
			case "256":
				myWidth = "256";

				for (int i = index; i < mPaint.size(); i++) {
					mPaint.get(i).setStrokeWidth(256f);
				}
				invalidate();
				currentPaint = colorsMap.get(pathList.get(index));
				break;
			case "128":
				myWidth = "128";

				for (int i = index; i < mPaint.size(); i++) {
					mPaint.get(i).setStrokeWidth(128f);
				}
				invalidate();
				currentPaint = colorsMap.get(pathList.get(index));
				break;
			case "64":
				myWidth = "64";

				for (int i = index; i < mPaint.size(); i++) {
					mPaint.get(i).setStrokeWidth(64f);
				}
				invalidate();
				currentPaint = colorsMap.get(pathList.get(index));
				break;
			case "32":
				myWidth = "32";

				for (int i = index; i < mPaint.size(); i++) {
					mPaint.get(i).setStrokeWidth(32f);
				}
				invalidate();
				currentPaint = colorsMap.get(pathList.get(index));
				break;
			case "16":
				myWidth = "16";

				for (int i = index; i < mPaint.size(); i++) {
					mPaint.get(i).setStrokeWidth(16f);
				}
				invalidate();
				currentPaint = colorsMap.get(pathList.get(index));
				break;
			case "8":
				myWidth = "8";

				for (int i = index; i < mPaint.size(); i++) {
					mPaint.get(i).setStrokeWidth(8f);
				}
				invalidate();
				currentPaint = colorsMap.get(pathList.get(index));
				break;
			case "4":
				myWidth = "4";

				for (int i = index; i < mPaint.size(); i++) {
					mPaint.get(i).setStrokeWidth(4f);
				}
				invalidate();
				currentPaint = colorsMap.get(pathList.get(index));
				break;
			case "2":
				myWidth = "2";

				for (int i = index; i < mPaint.size(); i++) {
					mPaint.get(i).setStrokeWidth(2f);
				}
				invalidate();
				currentPaint = colorsMap.get(pathList.get(index));
				break;
			case "1":
				myWidth = "1";

				for (int i = index; i < mPaint.size(); i++) {
					mPaint.get(i).setStrokeWidth(1f);
				}
				invalidate();
				currentPaint = colorsMap.get(pathList.get(index));
				break;
		}

	}

	//override the onTouchEvent		
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		float x = event.getX();
		float y = event.getY();

		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				undo = false;

				startTouch(x, y);

				invalidate();

				break;

			case MotionEvent.ACTION_MOVE:

				moveTouch(x, y);
				invalidate();
				break;

			case MotionEvent.ACTION_UP:

				upTouch();
				index += 1;

				if (pathList.size() - index <= 10) {
					for (int i = 0; i < 50; i++) {
						pathList.add(new Path());
						mPaint.add(new Paint());
						mPaint.get(mPaint.size() - 1).setAntiAlias(true);
						mPaint.get(mPaint.size() - 1).setColor(currentPaint.getColor());
						mPaint.get(mPaint.size() - 1).setStyle(Paint.Style.STROKE);
						mPaint.get(mPaint.size() - 1).setStrokeJoin(Paint.Join.ROUND);
						mPaint.get(mPaint.size() - 1).setStrokeWidth(currentPaint.getStrokeWidth());
						colorsMap.put(pathList.get(mPaint.size() - 1), mPaint.get(mPaint.size() - 1));
					}
				}


				break;

		}
		return true;
	}

}
package hzh.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.RotateAnimation;

public class Needle extends View {

	private Bitmap needle;
	private Bitmap scaledNeedle;

	public Needle(Context context, AttributeSet attrs) {
		super(context, attrs);
		needle = BitmapFactory.decodeResource(getResources(), R.drawable.needle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = measureWidthOrHeight(widthMeasureSpec, true);
		int height = measureWidthOrHeight(heightMeasureSpec, false);
		setMeasuredDimension(width, height);
	}

	private int measureWidthOrHeight(int measureSpec, boolean isMeasureWidth) {
		int result = 0;
		int size = MeasureSpec.getSize(measureSpec);
		int mode = MeasureSpec.getMode(measureSpec);
		switch (mode) {
		case MeasureSpec.UNSPECIFIED:
			result=isMeasureWidth?needle.getWidth():needle.getHeight();
			break;
		case MeasureSpec.EXACTLY:
			result=size;
			break;
		case MeasureSpec.AT_MOST:
			result=Math.min(size, isMeasureWidth?needle.getWidth():needle.getHeight());
			break;

		default:
			break;
		}
		return result;
	}
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		scaledNeedle = Bitmap.createScaledBitmap(needle, w, h, true);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawBitmap(scaledNeedle, 0, 0, null);
	}
	public void rotate() {
		RotateAnimation ra=new RotateAnimation(280, 50, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		ra.setDuration(500);
		ra.setRepeatMode(RotateAnimation.REVERSE);
		ra.setRepeatCount(RotateAnimation.INFINITE);
		this.startAnimation(ra);
	}

}

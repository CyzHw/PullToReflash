package hzh.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

public class DashBoard extends View {

	private Bitmap dash_board;
	private Bitmap needle;
	private int scaleX;
	private int scaleY;
	private Bitmap scaledDash_board;
	private Bitmap scaledNeedle;
	private int x;
	private int y;

	public DashBoard(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		scaledDash_board = dash_board = BitmapFactory.decodeResource(
				getResources(), R.drawable.dash_board);
		scaledNeedle = needle = BitmapFactory.decodeResource(getResources(),
				R.drawable.needle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = myMeasureSpec(widthMeasureSpec, true);
		int height = myMeasureSpec(heightMeasureSpec, false);
		setMeasuredDimension(width, height);
	}

	/**
	 * 测量自己的宽高
	 * 
	 * @param measureSpec
	 * @param isWidthMeasureSpec
	 * @return
	 */
	private int myMeasureSpec(int measureSpec, boolean isWidthMeasureSpec) {
		int result = 0;

		int mode = MeasureSpec.getMode(measureSpec);
		int size = MeasureSpec.getSize(measureSpec);

		switch (mode) {
		case MeasureSpec.UNSPECIFIED:
			if (isWidthMeasureSpec) {
				result = dash_board.getWidth();
			} else {
				result = dash_board.getHeight();
			}
			break;
		case MeasureSpec.EXACTLY:
			result = size;
			break;

		// 当wrap_content时 ,使View高宽匹配自己,不会尽可能大.
		case MeasureSpec.AT_MOST:
			if (isWidthMeasureSpec) {
				result = Math.min(size, dash_board.getWidth());
			} else {
				result = Math.min(size, dash_board.getHeight());
			}
			break;

		default:
			break;
		}

		return result;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		x = w / 2;
		y = h / 2;
		scaledDash_board = Bitmap.createScaledBitmap(dash_board, w > 0 ? w : 1,
				h > 0 ? h : 1, true);
		scaledNeedle = Bitmap.createScaledBitmap(needle, w > 0 ? w : 1,
				h > 0 ? h : 1, true);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawBitmap(scaledDash_board, 0, 0, null);

		float degrees = progress * 2.8f;
		canvas.rotate(degrees, x, y);
		canvas.drawBitmap(scaledNeedle, 0, 0, null);
	}

	int progress;

	public void setProgress(int progress) {
		this.progress = progress;
		invalidate();
	}

}

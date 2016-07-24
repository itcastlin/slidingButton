package org.heima.slidingbutton.view;

import org.heima.slidingbutton.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class SlidingButton extends View {

	private Bitmap background;
	private Bitmap btn;
	private boolean ss_checked;
	private int width;
	private int height;
	private float downX;
	private int startOffset;
	private int maxOffset;
	private boolean isPastHalf;

	public SlidingButton(Context context) {
		this(context, null);
	}

	public SlidingButton(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SlidingButton(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		TypedArray ta = context.obtainStyledAttributes(attrs,
				R.styleable.SlidingSwicth);
		// 获取值
		int ss_bg = ta.getResourceId(R.styleable.SlidingSwicth_ss_bg, 0);
		int ss_btn_bg = ta
				.getResourceId(R.styleable.SlidingSwicth_ss_btn_bg, 0);
		ss_checked = ta.getBoolean(R.styleable.SlidingSwicth_ss_checked, false);
		background = BitmapFactory.decodeResource(getResources(), ss_bg);
		btn = BitmapFactory.decodeResource(getResources(), ss_btn_bg);
		width = background.getWidth();
		height = background.getHeight();
		maxOffset = width - btn.getWidth();

		if (ss_checked) {
			startOffset = maxOffset;
			isPastHalf = true;
		} else {
			startOffset = 0;
			isPastHalf = false;
		}
		ta.recycle();
	}

	// view：大小
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		Log.d("heima01", "onMeasure");
		// 调用这个方法可以设置控件大小
		// setMeasuredDimension(measuredWidth, measuredHeight);
		// java.lang.IllegalStateException: onMeasure() did not set the measured
		// dimension by calling setMeasuredDimension()
		// 必须设置这行：bug，一些属性，失效
		setMeasuredDimension(width, height);
	}

	// 绘制
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Log.d("heima01", "onDraw");
		Paint paint = new Paint();
		canvas.drawBitmap(background, 0, 0, paint);
		canvas.drawBitmap(btn, startOffset, 0, paint);
	}

	// 默认的情况：会调用
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			// Log.d("heimai01", "ACTION_DOWN");
			downX = event.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			// Log.d("heimai01", "ACTION_MOVE");
			float moveX = event.getX();
			int dx = (int) (moveX - downX + 0.5f);
			startOffset += dx;
			if (startOffset < 0) {// 左边界的判断
				startOffset = 0;
			}
			if (startOffset > maxOffset) {// 右边界的判断边界的判断
				startOffset = maxOffset;
			}

			invalidate();// in the future onDraw
			downX = moveX;
			break;
		case MotionEvent.ACTION_UP:
			boolean tempPastHalf = isPastHalf;
			// Log.d("heimai01", "ACTION_UP");
			// 超一半处理
			int halfMax = maxOffset / 2;
			if (startOffset > halfMax) {// 超一半处理
				startOffset = maxOffset;
				isPastHalf = true;
				if (!tempPastHalf) {
					if (onSwitchListener != null) {
						onSwitchListener.onSwitch(isPastHalf);
					}
				}

			} else {
				startOffset = 0;
				isPastHalf = false;

				if (tempPastHalf) {
					if (onSwitchListener != null) {
						onSwitchListener.onSwitch(isPastHalf);
					}
				}
			}
			invalidate();
			break;

		default:
			break;
		}
		return true;
	}

	private OnSwitchListener onSwitchListener;

	public void setOnSwitchListener(OnSwitchListener onSwitchListener) {
		this.onSwitchListener = onSwitchListener;
	}

	public interface OnSwitchListener {
		public void onSwitch(boolean isPastHalf);
	}
}

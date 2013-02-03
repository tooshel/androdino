package us.youfailed.analogcontrols;

import us.youfailed.util.Logf;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class AnalogControl extends View implements OnTouchListener{
	
	private static final int CENTERPOINT_RADIUS = 3;

	private static final int CURSOR_RADIUS = 10;

	private static final String TAG = "analogcontrol";
	
	Point currentPoint = new Point();
	Point centerPoint = new Point();
	Paint guidePaint = new Paint();
	Paint paint = new Paint();
	
	boolean fingerPressed = false;
	
	public AnalogControl(Context context) {
		super(context);
		setBackgroundColor(Color.GREEN);
		init();
	}

	private void init() {
		Log.i(TAG, "init");
		setFocusable(true);
		setFocusableInTouchMode(true);
		setOnTouchListener(this);
		guidePaint.setColor(getResources().getColor(R.color.guideline));
		guidePaint.setAntiAlias(true);
		
		paint.setColor(getResources().getColor(R.color.touchcursor));
	}

	public AnalogControl(Context context, AttributeSet attrs) {
		super(context, attrs);
		Log.i(TAG, String.format("contstructor  context:%s  attributes:%s ", context, attrs));
		init();
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(MotionEvent.ACTION_MOVE == event.getAction()) {
			currentPoint.x = event.getX();
			currentPoint.y = event.getY();
			fingerPressed = true;
		} else if (MotionEvent.ACTION_UP == event.getAction() ){
			fingerPressed = false;
		}
		invalidate();
		return true;
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		//draw the center guide
		canvas.drawCircle(centerPoint.x, centerPoint.y, CENTERPOINT_RADIUS, guidePaint);
		if(fingerPressed) {
			canvas.drawCircle(currentPoint.x, currentPoint.y, CURSOR_RADIUS, paint);
		}
	}
	
	
	public void reset() {
		centerPoint.x = getWidth()/2;
		centerPoint.y = getHeight()/2;
		currentPoint.copy(centerPoint);
		invalidate();
		Logf.i(TAG, "reset(): center is %s", centerPoint);
	}
	
    @Override
    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld){
            super.onSizeChanged(xNew, yNew, xOld, yOld);
            //viewWidth = xNew;
            //viewHeight = yNew;
            reset();
    }
	
	private class Point{
		float x, y;
		@Override
		public String toString() {
			return String.format("(%4s, %4s)", x, y);
		}
		
		public void copy(Point that) {
			this.x = that.x;
			this.y = that.y;
		}
	}
}

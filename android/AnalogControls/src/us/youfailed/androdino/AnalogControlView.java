package us.youfailed.androdino;

import us.youfailed.androdino.R;
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

public class AnalogControlView extends View implements OnTouchListener{
	
	private static final int CENTERPOINT_RADIUS = 3;

	private static final int CURSOR_RADIUS = 10;

	private static final String TAG = "analogcontrol";
	
	Point currentPoint = new Point();
	Point centerPoint = new Point();
	//value of this control,  from 0 to 1
	Point normal = new Point();
	
	Paint guidePaint = new Paint();
	Paint paint = new Paint();
	
	boolean invertX = false;
	boolean invertY = false;
	
	//sheldon's "punch it" button. round to either minvalue, 0, or maxvalue.
	private boolean isDigital = false;

	private final ValueListener defaultValueListener = new ValueListener() {
		@Override
		public void onValueChange(AnalogControlView control) {}
		
	};
	
	private ValueListener valueListener = defaultValueListener;
	
	boolean fingerPressed = false;
	
	public AnalogControlView(Context context) {
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

	public AnalogControlView(Context context, AttributeSet attrs) {
		super(context, attrs);
		Log.i(TAG, String.format("contstructor  context:%s  attributes:%s ", context, attrs));
		init();
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(MotionEvent.ACTION_MOVE == event.getAction()) {
			currentPoint.x = event.getX();
			currentPoint.y = event.getY();
			
			if(isDigital) {
				currentPoint.x = currentPoint.x > centerPoint.x ? (float)getWidth() : 0f;
				currentPoint.y = currentPoint.y > centerPoint.y ? (float)getHeight() : 0f;
			}
			
			fingerPressed = true;
		} else if (MotionEvent.ACTION_UP == event.getAction() ){
			fingerPressed = false;
		}
		updateValue();
		invalidate();
		return true;
	}
	
	private void updateValue() {
		if(!fingerPressed) {
			currentPoint.copy(centerPoint);
		}

		normal.x = currentPoint.x / (float)getWidth(); 
		//inherently invert Y since we want 0 to be bottom 
		normal.y = 1 - (currentPoint.y / (float)getHeight());
		
		if(invertX) normal.x  = 1 - normal.x;
		if(invertY) normal.y = 1 - normal.y;
			
		Log.i(TAG, "" + this);
		valueListener.onValueChange(this);
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
    
    public Point getValue() {
    	return normal;
    }
    @Override
    public String toString()  {
    	return String.format("AnalogControl id:%s   value%s", getId(), normal);
    }
    
    public void setValueListener(ValueListener listener) {
    	if(listener == null) {
    		valueListener = defaultValueListener;
    	} else {
    		valueListener = listener;
    	}
    }
    
    public boolean getInvertX() {
    	return invertX;
    }
    public void setInvertX(boolean inv) {
    	this.invertX = inv;
    }
    
    public boolean getInvertY() {
    	return invertY;
    }
    
    public void setInvertY(boolean inv) {
    	this.invertY = inv;
    }
    
    public void setInvert(boolean inv) {
    	invertX = invertY = inv;
    }

	public boolean isDigital() {
		return isDigital;
	}

	public void setDigital(boolean isDigital) {
		this.isDigital = isDigital;
	}
    
}

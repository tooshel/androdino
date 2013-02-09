package us.youfailed.androdino;

import us.youfailed.androdino.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

public class RemoteControlActivity extends Activity implements ValueListener{
	
	AnalogControlView speedControl;
	AnalogControlView steeringControl;
	ToggleButton invertSpeedToggle;
	ToggleButton invertSteeringToggle;
	ToggleButton lightsToggle;
	ToggleButton miscToggle;
	
	TextView debugText;
	
	CommPacket packet = new CommPacket();
	
	private static final String TAG = "mainactivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_control);
        
        speedControl = (AnalogControlView)findViewById(R.id.thumbstick1);
        steeringControl = (AnalogControlView)findViewById(R.id.thumbstick2);
        invertSpeedToggle = (ToggleButton)findViewById(R.id.toggleThumbstick1);
        invertSteeringToggle = (ToggleButton)findViewById(R.id.toggleThumbstick2);
        lightsToggle = (ToggleButton)findViewById(R.id.toggleButton1);
        miscToggle = (ToggleButton)findViewById(R.id.toggleButton2);
        debugText = (TextView)findViewById(R.id.debug_console);
        
        speedControl.setValueListener(this);
        steeringControl.setValueListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
//    @Override 
//    public void onResume() {
//    	super.onResume();
//    }
//    
//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//    	if(!hasFocus) return;
//    	AnalogControl control = (AnalogControl) findViewById(R.id.thumbstick1);
//    	Logf.i("mainactivity", "windowFocusChanged(%s)", hasFocus);
//    	control.reset();
//    }
//

    //for now just use same handler for all button clicks
    public void toggleClicked(View view) {
    	Log.i(TAG, "invert 1 clicked");
    	updatePacket();
    }
        
    
    private class CommPacket {
    	public float speed;
    	public float steer;
    	public boolean lights;
    	public boolean misc;
    	
    	private char[] buf = "s:1:9:1:9:1:1:1".toCharArray();
    	private static final String sep = ":"; 
    	
    	private String toChar(boolean bit)  {
    		return bit ? "1" : "0";
    	}

    	private String toChar(float normVal) {
    		if(normVal <= 0.01f ) return "0:0";
    		
    		//convert normVal to -9<= x <= 9
    		float val = ((normVal - 0.5f) * 2) * 9;
    		int absVal = (int)Math.abs(val);
    		String bit = toChar(val > 0);
    		if(absVal>9)  {
    			absVal = 9;
    			Log.wtf(TAG, "val should be 0-9, was:" + absVal);
    		}
    		String chr = Integer.toString(absVal);
    		return bit + ":" + chr;
    	}
    	
    	public String packetString() {
    		StringBuilder sb = new StringBuilder("s" + sep);
    		sb.append(toChar(speed)).append(sep);
    		sb.append(toChar(steer)).append(sep);
    		sb.append(toChar(lights)).append(sep);
    		sb.append(toChar(misc));
    		return sb.toString();
    	}
    }


	@Override
	public void onValueChange(AnalogControlView control) {
		updatePacket();
	}
	
	private void updatePacket() {
		
		packet.speed = speedControl.getValue().y;
		packet.steer = steeringControl.getValue().x;
		packet.lights = lightsToggle.isChecked();
		packet.misc = miscToggle.isChecked();
		
		debugText.setText(packet.packetString());
		
	}

    
    
}

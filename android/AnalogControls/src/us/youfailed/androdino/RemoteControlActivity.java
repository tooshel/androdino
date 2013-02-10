package us.youfailed.androdino;

import us.youfailed.util.CommPacket;
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
	ToggleButton miscToggle1;
	ToggleButton miscToggle2;
	
	
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
        miscToggle1 = (ToggleButton)findViewById(R.id.toggleButton2);
        miscToggle2 = (ToggleButton)findViewById(R.id.toggleButton3);
        debugText = (TextView)findViewById(R.id.debug_console);
        
        speedControl.setValueListener(this);
        steeringControl.setValueListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_remote_control, menu);
        return true;
    }
    
    //for now just use same handler for all button clicks
    public void toggleClicked(View view) {
    	Log.i(TAG, "invert 1 clicked");
    	updatePacket();
    	
    	speedControl.setInvertY(invertSpeedToggle.isChecked());
    	steeringControl.setInvertX(invertSteeringToggle.isChecked());
    }
        
    


	@Override
	public void onValueChange(AnalogControlView control) {
		updatePacket();
	}
	
	private void updatePacket() {
		
		packet.speed = speedControl.getValue().y;
		packet.steer = steeringControl.getValue().x;
		packet.lights = lightsToggle.isChecked();
		packet.misc1 = miscToggle1.isChecked();
		packet.misc2 = miscToggle2.isChecked();
		
		debugText.setText(packet.packetString());
		
	}

    
    
}

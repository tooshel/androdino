package us.youfailed.androdino;



import us.youfailed.util.ComService;
import us.youfailed.util.CommPacket;
import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class RemoteControlActivity extends Activity implements ValueListener{
	private static boolean D = true;
	
	AnalogControlView speedControl;
	AnalogControlView steeringControl;
	ToggleButton invertSpeedToggle;
	ToggleButton invertSteeringToggle;
	ToggleButton lightsToggle;
	ToggleButton miscToggle1;
	ToggleButton miscToggle2;
	ToggleButton tankModeToggle;
	

	private BluetoothAdapter mBluetoothAdapter;
	private ComService mComService;
    // Name of the connected device
    private String mConnectedDeviceName = null;
    
    private boolean isTankMode = false;

	TextView debugText;
	
	CommPacket packet = new CommPacket();
	private boolean bluetoothEnabled;
	
	private static final String TAG = "mainactivity";
	public  static final int REQUEST_BLUETOOTH_DEVICE_LIST = 0;
	private static final int REQUEST_ENABLE_BLUETOOTH = 1;
	
	
    // The Handler that gets information back from the ComService
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case ComService.MESSAGE_STATE_CHANGE:
                if(D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                switch (msg.arg1) {
                case ComService.STATE_CONNECTED:
                    setStatus(getString(R.string.title_connected_to) +  mConnectedDeviceName);
                    break;
                case ComService.STATE_CONNECTING:
                    setStatus(R.string.title_connecting);
                    break;
                case ComService.STATE_LISTEN:
                case ComService.STATE_NONE:
                    setStatus(R.string.title_not_connected);
                    break;
                }
                break;
            case ComService.MESSAGE_WRITE:
                //byte[] writeBuf = (byte[]) msg.obj;
                // construct a string from the buffer
                //String writeMessage = new String(writeBuf);
                //mConversationArrayAdapter.add("Me:  " + writeMessage);
                break;
            case ComService.MESSAGE_READ:
                //byte[] readBuf = (byte[]) msg.obj;
                // construct a string from the valid bytes in the buffer
                //String readMessage = new String(readBuf, 0, msg.arg1);
                //mConversationArrayAdapter.add(mConnectedDeviceName+":  " + readMessage);
                break;
            case ComService.MESSAGE_DEVICE_NAME:
                // save the connected device's name
                mConnectedDeviceName = msg.getData().getString(ComService.DEVICE_NAME);
                Toast.makeText(getApplicationContext(), "Connected to "
                               + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                break;
            case ComService.MESSAGE_TOAST:
                Toast.makeText(getApplicationContext(), msg.getData().getString(ComService.TOAST),
                               Toast.LENGTH_SHORT).show();
                break;
            }
        }
    };
	
	
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate:" + savedInstanceState);
        setContentView(R.layout.activity_remote_control);
        
        speedControl = (AnalogControlView)findViewById(R.id.thumbstick1);
        steeringControl = (AnalogControlView)findViewById(R.id.thumbstick2);
        invertSpeedToggle = (ToggleButton)findViewById(R.id.toggleThumbstick1);
        invertSteeringToggle = (ToggleButton)findViewById(R.id.toggleThumbstick2);
        lightsToggle = (ToggleButton)findViewById(R.id.toggleButton1);
        miscToggle1 = (ToggleButton)findViewById(R.id.toggleButton2);
        miscToggle2 = (ToggleButton)findViewById(R.id.toggleButton3);
        debugText = (TextView)findViewById(R.id.debug_console);
        tankModeToggle = (ToggleButton) findViewById(R.id.tankControlButton);
        
        speedControl.setValueListener(this);
        steeringControl.setValueListener(this);
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
        Log.d(TAG, "onResume");
    	testBluetoothAvailability();
    	
    	//on resume, set controls to correct state
    	//FIXME: i think this should be done in the view (e.g. somehow toggle buttons retain 'checked' value)
    	toggleClicked(null);
    	updatePacket();
    	
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_remote_control, menu);
        return true;
    }
    
    @Override 
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
    	case R.id.menuitem_device_list:
    		handleShowDevices();
    		return true;
    	case R.id.menuitem_disconnect:
    		handleDisconnect();
    		return true;
    	case R.id.menuitem_credits:
    		handleCredits();
    		return true;
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }
    
    private void handleCredits() {
		// TODO Auto-generated method stub
		
	}

	private void handleDisconnect() {
		// TODO Auto-generated method stub
		
	}

	//for now just use same handler for all button clicks
    public void toggleClicked(View view) {
    	Log.i(TAG, "invert 1 clicked");
    	updatePacket();
    	
    	speedControl.setInvertY(invertSpeedToggle.isChecked());
    	steeringControl.setInvertX(invertSteeringToggle.isChecked());
    	steeringControl.setDigital(miscToggle2.isChecked());
    	
    	isTankMode = tankModeToggle.isChecked();
    }
        

	@Override
	public void onValueChange(AnalogControlView control) {
		updatePacket();
	}
	
	private void updatePacket() {
		
		packet.speed = speedControl.getValue().y;
		if(isTankMode) {
			packet.steer = steeringControl.getValue().y;
		} else {
			packet.steer = steeringControl.getValue().x;
		}
		packet.lights = lightsToggle.isChecked();
		packet.misc1 = miscToggle1.isChecked();
		packet.misc2 = miscToggle2.isChecked();
		
		String str = packet.packetString();
		debugText.setText(str);
		
		//TODO: consider putting this elsewhere
		if(isConnected()) {
			sendMessage(str);
		}
	}

	private boolean isConnected() {
		if(mComService == null) return false;
		return mComService.getState() == ComService.STATE_CONNECTED;
	}
	
	public void handleShowDevices() {
		Intent intent = new Intent(this, DeviceListActivity.class);
		startActivityForResult(intent, REQUEST_BLUETOOTH_DEVICE_LIST);
	}
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode) {
		case REQUEST_BLUETOOTH_DEVICE_LIST:
			if(resultCode== RESULT_OK) {
				handleDevicePicked(data);
			}
			break;
		case REQUEST_ENABLE_BLUETOOTH:
			if(resultCode == RESULT_OK) {
				onBluetoothEnabled();
			}
			break;
		default:
			Log.w(TAG,"unhandled activity result");
		}
			
			
	}

	private void handleDevicePicked(Intent data) {
		//fixme: this should be set earlier on, 
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // Initialize the ComService to perform bluetooth connections
        mComService = new ComService(this, mHandler, 100L);
        // Get the device MAC address
        String address = data.getExtras()
            .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mComService.connect(device);
	}
    

	public void testBluetoothAvailability() {
		BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (bluetoothAdapter == null) {
			Log.i(TAG, "no bluetooth hardware");
			return;
		}		
		
		if(!bluetoothAdapter.isEnabled()) {
			Log.i(TAG, "bluetooth hardware present but not enabled");
		    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BLUETOOTH);
		    Log.i(TAG, "asking user to enable bluetooth");
		} else {
			onBluetoothEnabled();
		}
	}

	private void onBluetoothEnabled() {
		bluetoothEnabled = true;
		invalidateOptionsMenu();
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		menu.findItem(R.id.menuitem_device_list).setEnabled(bluetoothEnabled);
		return true;
	}

    private final void setStatus(int resId) {
        final ActionBar actionBar = getActionBar();
        actionBar.setSubtitle(resId);
    }

    private final void setStatus(CharSequence subTitle) {
        final ActionBar actionBar = getActionBar();
        actionBar.setSubtitle(subTitle);
    }
	
    /**
     * Sends a message.
     * @param message  A string of text to send.
     */
    private void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mComService.getState() != ComService.STATE_CONNECTED) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the ComService to write
            byte[] send = message.getBytes();
            mComService.write(send);
        }
    }

	
}

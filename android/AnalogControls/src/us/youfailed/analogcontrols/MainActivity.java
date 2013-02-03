package us.youfailed.analogcontrols;

import us.youfailed.util.Logf;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    
    
}

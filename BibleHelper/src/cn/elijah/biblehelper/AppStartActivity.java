package cn.elijah.biblehelper;

import cn.elijah.biblehelper.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;

public class AppStartActivity extends Activity {
    /** Called when the activity is first created. */

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);	
		
		setContentView(R.layout.main);

		
	new Handler().postDelayed(new Runnable(){
		@Override
		public void run(){
			Intent intent = new Intent (AppStartActivity.this,MainActivity.class);			
			startActivity(intent);			
			AppStartActivity.this.finish();
		}
	}, 1000);
   }
	
    //ÍË³ö°´¼ü
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {

	    return super.dispatchKeyEvent(event);
	}
	
}
package cn.elijah.biblehelper;


import cn.elijah.biblehelper.R;
import android.app.ActivityGroup;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;


public class Main_Tab_AD extends ActivityGroup {
	  

	//�������ǵĲ���
	public LinearLayout adViewLayout = null;
    
	//��дApplication����Ϊȫ�����������ļ�
	private Config _config;
	
	
	
	@Override
	   public void onCreate(Bundle savedInstanceState) {
	       super.onCreate(savedInstanceState);
	       setContentView(R.layout.main_tab_ad); 
			
	       //����Զ����Ӧ�ó���Config
	       _config = (Config)getApplication(); 
	       
	       //����ʵ��
	       _config.SetInstanceMain_Tab_AD(this);

	        //����������ҳ 
	        WebView webView = (WebView)findViewById(R.id.webViewAboutUs);
	        webView.getSettings().setSupportZoom(true);

	        webView.loadUrl("file:///android_asset/about.htm"); 

	 }

	 

		//���ñ������Ҳఴť
		public void btnmainright(View v) {  

	     }  	
	    
		//���ñ������м�����
		public void btnmaincenter(View v) {  

	     }  
	 
		//���ñ������Ҳఴť
		public void btnmainleft(View v) {  

	     } 
		
		//�˳�����-----------------------

		@Override  
		public boolean dispatchKeyEvent(KeyEvent event) {  

		    return super.dispatchKeyEvent(event);  
		} 

		
		@Override
		public void onConfigurationChanged(Configuration newConfig) {

			
			super.onConfigurationChanged(newConfig);
		}
		
}

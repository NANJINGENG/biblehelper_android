package cn.elijah.biblehelper;


import java.util.ArrayList;
import cn.elijah.biblehelper.R;
import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MainActivity extends ActivityGroup {

	
	public static MainActivity instance = null;
	

	
	private LinearLayout mBigTab1,mBigTab2,mBigTab3,mBigTab4;
	
	private LinearLayout mTabLayout;
	
	private ArrayList<View> mTabViews;
	
	private ImageView mTabImg;// ����ͼƬ
	private ImageView mTab1,mTab2,mTab3,mTab4;
	public int currIndex = 0;// ��ǰҳ�����
	private int one;//����ˮƽ����λ��
	//��дApplication����Ϊȫ�����������ļ�
	private Config _config;


   @Override
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);

       setContentView(R.layout.main_biblehelper);
       
       //����Զ����Ӧ�ó���Config
       _config = (Config)getApplication(); 
       
       //����ʵ��
       _config.SetMainActivity(this);
       
        //����activityʱ���Զ����������
       getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
       instance = this;
       
       //�ײ�4��ͼƬ��ť��ʵ��
       mTab1 = (ImageView) findViewById(R.id.img_weixin);
       mTab2 = (ImageView) findViewById(R.id.img_address);
       mTab3 = (ImageView) findViewById(R.id.img_friends);
       mTab4 = (ImageView) findViewById(R.id.img_settings);
       //���õ���¼�
       mTab1.setOnClickListener(new MyOnClickListener(0));
       mTab2.setOnClickListener(new MyOnClickListener(1));
       mTab3.setOnClickListener(new MyOnClickListener(2));
       mTab4.setOnClickListener(new MyOnClickListener(3));
       
       //���ֻ�ǵײ�4��ͼƬ��ť��������Ѳ�����Ϊ��ʹ�����Χ���󣬽��ⲿ�Ĳ���Ҳ������click�¼�
       mBigTab1 = (LinearLayout) findViewById(R.id.ll_tab1);
       mBigTab2 = (LinearLayout) findViewById(R.id.ll_tab2);
       mBigTab3 = (LinearLayout) findViewById(R.id.ll_tab3);
       mBigTab4 = (LinearLayout) findViewById(R.id.ll_tab4);
       //����¼�
       mBigTab1.setOnClickListener(new MyOnClickListener(0));
       mBigTab2.setOnClickListener(new MyOnClickListener(1));
       mBigTab3.setOnClickListener(new MyOnClickListener(2));
       mBigTab4.setOnClickListener(new MyOnClickListener(3));

       //�ײ�����Ч��ͼƬ��ʵ��
       mTabImg = (ImageView) findViewById(R.id.img_tab_now);
       
       
     //������ײ��ƶ����ͼƬ�Ŀ�ȣ�������Ч���ƶ��ľ���
       ReSizeImage();
       

		//���4��TAB����ҳ��ʵ��
		//�Ķ�ʥ��
       View view1 = getLocalActivityManager().startActivity(
               "Main_Tab_Readbible",
               new Intent(MainActivity.this, Main_Tab_Readbible.class))
               .getDecorView();
       
       //��ʷ��¼
       View view2 = getLocalActivityManager().startActivity(
               "Main_Tab_History",
               new Intent(MainActivity.this, Main_Tab_History.class))
               .getDecorView();
       
       //ϵͳ����
       View view3 = getLocalActivityManager().startActivity(
               "Main_Tab_Config",
               new Intent(MainActivity.this, Main_Tab_Config.class))
               .getDecorView();
       
       //�Ƽ�Ӧ��
       View view4 = getLocalActivityManager().startActivity(
               "Main_Tab_AD",
               new Intent(MainActivity.this, Main_Tab_AD.class))
               .getDecorView();

	   //��4��ҳ������б����л�ʹ��
	   mTabViews = new ArrayList<View>();
	   mTabViews.add(view1);
	   mTabViews.add(view2);
	   mTabViews.add(view3);
	   mTabViews.add(view4);
		
	   //���tab������ʵ��  			
	   mTabLayout = (LinearLayout)findViewById(R.id.tablayout);
	
	   //�л�����һҳ
	   ChangeTabLayout(0);
   }
   
   public void ChangePage(int index)
   {
		//�����ǰҳ���뽫Ҫ�л���ҳ����ȣ����л�
		if(currIndex == index)
		{
			return;
		}
		else
		{
			//�л�ҳ��
			ChangeTabLayout(index);
			//����Ч��
			SetTabAnimation(index);

			//��ǰҳ
			currIndex = index;
			
			if(currIndex == 1)
			{
				if(_config.GetMain_Tab_History() != null)
				{
					_config.GetMain_Tab_History().FillListView();
				}
					
			}
			
			if(currIndex == 2)
			{
				if(_config.GetMain_Tab_Config() != null)
				{
					_config.GetMain_Tab_Config().ReloadSetting();
				}
					
			}
			
			if(currIndex == 3)
			{
				if(_config.GetInstanceMain_Tab_AD() != null)
				{
					LinearLayout adViewLayout = _config.GetInstanceMain_Tab_AD().adViewLayout;

					if(adViewLayout != null)
					{
						   adViewLayout.removeAllViews();
					}
					
				}
						
	
			}
			
		}
		
   }
   
   @Override
   public void onConfigurationChanged(Configuration newConfig) {
     super.onConfigurationChanged(newConfig);

   //������ײ��ƶ����ͼƬ�Ŀ�ȣ�������Ч���ƶ��ľ���
     ReSizeImage();
 
     if(currIndex != 0)
     {
    	 int oldIndex = currIndex;
    	 
		//�л�ҳ��
		ChangeTabLayout(0);
		//����Ч��
		SetTabAnimation(0);

		currIndex = 0;
			
			
		//�л�ҳ��
		ChangeTabLayout(oldIndex);
		//����Ч��
		SetTabAnimation(oldIndex);
		
		currIndex = oldIndex;
			
     }
   }

   //������ײ��ƶ����ͼƬ�Ŀ�ȣ�������Ч���ƶ��ľ���
   private void ReSizeImage()
   {
	 //��ȡ��Ļ��ǰ�ֱ���
       Display currDisplay = getWindowManager().getDefaultDisplay();
       int displayWidth = currDisplay.getWidth();
       //int displayHeight = currDisplay.getHeight();
       
       //����ˮƽ����ƽ�ƴ�С
       one = displayWidth/4; 
       //����mTabImg�Ŀ�ȣ���Ӧ��ͬ�ֱ���
       mTabImg.setLayoutParams(new RelativeLayout.LayoutParams(
				one , RelativeLayout.LayoutParams.MATCH_PARENT));		
   }
   

/**
	 * ͷ��������
	 */
	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}
		@Override
		public void onClick(View v) {
			
			ChangePage(index);
		}
	};
   
	//�л�TABҳ
	public void ChangeTabLayout(int arg0)
	{
		mTabLayout.removeAllViews();
		mTabLayout.addView(mTabViews.get(arg0)); 
	}
	
	//�·��˵��Ķ���Ч��
	public void SetTabAnimation(int arg0)
	{
		switch (arg0) {
		case 0:
			mTab1.setImageDrawable(getResources().getDrawable(R.drawable.tab_weixin_pressed));
			if (currIndex == 1) {
				mTab2.setImageDrawable(getResources().getDrawable(R.drawable.tab_address_normal));
			} else if (currIndex == 2) {
				mTab3.setImageDrawable(getResources().getDrawable(R.drawable.tab_settings_normal));
			}
			else if (currIndex == 3) {
				mTab4.setImageDrawable(getResources().getDrawable(R.drawable.tab_find_frd_normal));
			}
			break;
		case 1:
			mTab2.setImageDrawable(getResources().getDrawable(R.drawable.tab_address_pressed));
			if (currIndex == 0) {
				mTab1.setImageDrawable(getResources().getDrawable(R.drawable.tab_weixin_normal));
			} else if (currIndex == 2) {
				mTab3.setImageDrawable(getResources().getDrawable(R.drawable.tab_settings_normal));
			}
			else if (currIndex == 3) {
				mTab4.setImageDrawable(getResources().getDrawable(R.drawable.tab_find_frd_normal));
			}
			break;
		case 2:
			mTab3.setImageDrawable(getResources().getDrawable(R.drawable.tab_settings_pressed));
			if (currIndex == 0) {
				mTab1.setImageDrawable(getResources().getDrawable(R.drawable.tab_weixin_normal));
			} else if (currIndex == 1) {
				mTab2.setImageDrawable(getResources().getDrawable(R.drawable.tab_address_normal));
			}
			else if (currIndex == 3) {
				mTab4.setImageDrawable(getResources().getDrawable(R.drawable.tab_find_frd_normal));
			}
			break;
		case 3:
			mTab4.setImageDrawable(getResources().getDrawable(R.drawable.tab_find_frd_pressed));
			if (currIndex == 0) {
				mTab1.setImageDrawable(getResources().getDrawable(R.drawable.tab_weixin_normal));
			} else if (currIndex == 1) {
				mTab2.setImageDrawable(getResources().getDrawable(R.drawable.tab_address_normal));
			}
			else if (currIndex == 2) {
				mTab3.setImageDrawable(getResources().getDrawable(R.drawable.tab_settings_normal));
			}
			break;
		}
		
		Animation animation = new TranslateAnimation(one*currIndex, one*arg0, 0, 0);
		
		animation.setFillAfter(true);// True:ͼƬͣ�ڶ�������λ��
		animation.setDuration(150);
		mTabImg.startAnimation(animation);
		
		animation = null;
		
	}
	

	//�˳�����-----------------------
	
	public void Exit()
	{

    		//ѯ���Ƿ��˳�
    		AlertDialog.Builder builder = new Builder(this);  
            builder.setMessage("�Ƿ��˳�����");  
            builder.setTitle("��ʾ");  
            builder.setPositiveButton("ȷ��",  
            new android.content.DialogInterface.OnClickListener() {
                @Override  
                public void onClick(DialogInterface dialog, int which) {
                	
                	//ֹͣ����
                	if(_config.GetVoicePanelActivity().mediaPlayer != null)
                	{
                		_config.GetVoicePanelActivity().mediaPlayer.stop();
                		//���ý�����
                		_config.GetVoicePanelActivity().seekBar.setProgress(0);
                	}
                	
                	dialog.dismiss();
                	
                	MainActivity.instance.finish();//�ر�Activity
                	
                	android.os.Process.killProcess(android.os.Process.myPid()); //��ȡPID

                	System.exit(0); //����java��c#�ı�׼�˳���������ֵΪ0���������˳�

                }  
            });  
            builder.setNegativeButton("ȡ��",  
            new android.content.DialogInterface.OnClickListener() {  
                @Override  
                public void onClick(DialogInterface dialog, int which) {  
                    dialog.dismiss();  
                }  
            });  
            builder.create().show();  

	}

	
    //�˳�����-----------------------
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
	    if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
	            && event.getAction() == KeyEvent.ACTION_DOWN 
	            && event.getRepeatCount() == 0) {
	        //����Ĳ������� 
		    Exit();	    	
	    }

	    return super.dispatchKeyEvent(event);
	}
	
}
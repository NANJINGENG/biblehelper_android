package cn.elijah.biblehelper;

import cn.elijah.biblehelper.R;
import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

public class Main_Tab_Config extends Activity {

 

    private RadioButton rbLectionFontSize1;  
    private RadioButton rbLectionFontSize2;  
    

    private RadioButton rbAutoDownload1;  
    private RadioButton rbAutoDownload2;  
    
    
	//��дApplication����Ϊȫ�����������ļ�
	private Config _config;

	
	
	@Override	 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_tab_config);

		//����Զ����Ӧ�ó���Config
		_config = (Config)getApplication();
		
		//����ʵ��
		_config.SetMain_Tab_Config(this);
	       
		//�ֺ�
		rbLectionFontSize1 = (RadioButton)findViewById(R.id.radioLectionFontSize1);
		rbLectionFontSize2 = (RadioButton)findViewById(R.id.radioLectionFontSize2);
		
		//�Զ�����
		rbAutoDownload1 = (RadioButton)findViewById(R.id.radioAutoDownload1);
		rbAutoDownload2 = (RadioButton)findViewById(R.id.radioAutoDownload2);
		
		//��ȡ���ò����õ�UI
		ReloadSetting();
		
	}
	 
	//���棬�����ݿ���д������
	public void onButtonSaveClick(View v)
	{
		//�ֺ�
		if(rbLectionFontSize1.isChecked())
		{
			//һ���ֺ�
			_config.SetLectionFontSize(20);
		}
		else
		{
			//���ֺ�
			_config.SetLectionFontSize(40);
		}
		
		
		//�Զ�����
		if(rbAutoDownload1.isChecked())
		{
			//�Զ�����
			_config.SetEnableAutoDownloadVoiceFile(true);
		}
		else
		{
			//��ֹ�Զ�����
			_config.SetEnableAutoDownloadVoiceFile(false);
		}

		//д�����ݿ�
		_config.WriteConfigToDatabase();
		
		//��ʾ���óɹ�
		Toast.makeText(getApplicationContext(), "����ɹ�", Toast.LENGTH_SHORT).show();
		
	}
	
	//ȡ������ȡ���ݿ�ֵ����ԭ����ѡ��
	public void onButtonCancelClick(View v)
	{
		ReloadSetting();
		
		
		//��ʾ���óɹ�
		Toast.makeText(getApplicationContext(), "�Ѿ�ȡ��", Toast.LENGTH_SHORT).show();
	}
	
	//��������config�ж�ȡ���õ�ֵ
	public void ReloadSetting()
	{

    	if(_config.GetLectionFontSize() == 20)
    	{
    		//�ֺţ���ͨ
    		rbLectionFontSize1.setChecked(true);
		}
		else
		{
    		//�ֺţ���
			rbLectionFontSize2.setChecked(true);
		}

    	//isAutoDownFile int=�Ƿ��Զ������ļ���0=���Զ����� 1=�Զ����أ�
	   	if(_config.GetEnableAutoDownloadVoiceFile())
	   	{
	   		rbAutoDownload1.setChecked(true);
	   	}
	   	else
	   	{
	   		//��ֹ�Զ�����
	   		rbAutoDownload2.setChecked(true);
	   	}
		
		
	}
	
	//�����ԭ
	public void onButtonRestoreClick(View v)
	{
		
	}
	
	//���ذ�ť
	public void onButtonBackClick(View v) {  
		this.finish();
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

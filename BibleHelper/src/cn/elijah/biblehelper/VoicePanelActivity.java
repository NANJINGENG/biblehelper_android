package cn.elijah.biblehelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

@SuppressLint({ "DefaultLocale", "HandlerLeak" })
public class VoicePanelActivity extends Activity {
	/** Called when the activity is first created. */

	public MediaPlayer mediaPlayer;
	private ImageButton button;
	public SeekBar seekBar;
	private String mp3Path;
	private Config _config;

	@SuppressLint("DefaultLocale")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_voice_panel);

		 //����Զ����Ӧ�ó���Config   
		_config = (Config)getApplication(); 


		mediaPlayer = new MediaPlayer();

		button = (ImageButton) findViewById(R.id.buttonplaycontrol);
		
		button.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_media_pause));

		seekBar = (SeekBar) findViewById(R.id.seekbarvoice);
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if (fromUser) {
					
					if(mediaPlayer.isPlaying())
					{
						mediaPlayer.seekTo(progress);
					}

				}
			}
		});
 
		
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mediaPlayer.isPlaying()) {
					mediaPlayer.pause();
				}
				else 
				{
					//����������0����ʾ���Լ�������
					if(seekBar.getProgress() > 0)
					{
						mediaPlayer.start();
					}
					else
					{
						AutoPlay();
					}

					//���²��Ű�ť��ͼ��״̬
					UpdatePlaybuttonIconStatus();
					
					new Thread(new runable()).start();
				}
				
				//���²��Ű�ť��ͼ��״̬
				UpdatePlaybuttonIconStatus();
				
			}
		});

		mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			@SuppressLint("DefaultLocale")
			@Override
			public void onCompletion(MediaPlayer mp) {
				//���Ž�����

				//���²��Ű�ť״̬
				UpdatePlaybuttonIconStatus();
				
				//���ý�����
				seekBar.setProgress(0);
				
				//�������һ��
				if(_config.GetIsHasNextChapter())
				{
		 		   //��ID����1
		 		   _config.setChapterID(_config.getChapterID() + 1);
		 		   //����б�
		 		   _config.GetInstanceMain_Tab_Readbible().FillListView();
		 	   }
		       else if(_config.getVolumeID() < 66)
		       {
		    	   //û����һ�£��ж��Ƿ�����һ�������У�����ת����һ���
		    	   //���ID��1
		    	  _config.setVolumeID(_config.getVolumeID() + 1);

				   //����Ϊ��1��
				   _config.setChapterID(1);
			
				   //����б�
				   _config.GetInstanceMain_Tab_Readbible().FillListView();
		       }
		       else
		       {
		    	   //���û����һ��Ҳû����һ����˳�����
		    	   return;
		       }
				
				//�������һ�»���һ���
				//�ж��Ƿ���MP3�ļ�
				String strVolumeID_00 = String.format("%02d",_config.getVolumeID());
				
				//�õ���ǰ�¶�Ӧ����ȷ����Ƶ�ļ�����
				String strCorrectMP3FileName = strVolumeID_00  + "-" + _config.getChapterID() + ".mp3";

				//MP3·��
				mp3Path = FileUtil.setMkdir(VoicePanelActivity.this, _config.GetVoiceFileStorageFolder()) + File.separator + strCorrectMP3FileName;

				//�ж�MP3�Ƿ����
				File mp3FileExists=new File(mp3Path);

				if(mp3FileExists.exists())
				{
				   //��ʼ����
				   AutoPlay();
				}
				else
				{
					//�����ڣ���ʾ����
					//����û��Ѿ�����Ϊ�����Զ����أ�����ʾ
					if(_config.GetEnableAutoDownloadVoiceFile())
					{
					   //��ʼ����
					   AutoPlay();
					}
					else
					{
						//���δ�����Զ����أ�����ʾ
						//��ʾ�Ƿ��Զ����ز�����һ�£����Զ�������һ�£����������ļ�
						AlertDialog.Builder builder = new Builder(_config.GetInstanceMain_Tab_Readbible());  
				        builder.setMessage("���²��Ž���������������һ�£����Ƿ�����ʥ�������Զ����ز�������һ�µ�������\r\nѡ��[ȷ��]�������Զ����ء�\r\nѡ��[ȡ��]��ֹͣ���š�\r\n��������ϵͳ�������������ѡ�");  
				        builder.setTitle("��ʾ");  
				        builder.setPositiveButton("ȷ��",  
				        new android.content.DialogInterface.OnClickListener() {
				            @Override  
				            public void onClick(DialogInterface dialog, int which) {
				            	//�Զ���ת����һ�£�����һ���

				            	//����Ϊ�����Զ�����
				            	_config.SetEnableAutoDownloadVoiceFile(true);
				            	
				            	//д�����ݿ�
				            	_config.WriteConfigToDatabase();
				            	
							   //��ʼ����
							   AutoPlay();
				            }  
				        });  
				        builder.setNegativeButton("ȡ��",  
				        new android.content.DialogInterface.OnClickListener() {  
				            @Override  
				            public void onClick(DialogInterface dialog, int which) {  
				            	//����Ϊ��ֹ�Զ�����
				            	_config.SetEnableAutoDownloadVoiceFile(false);


				            	//���²��Ű�ťͼ��״̬
				            	UpdatePlaybuttonIconStatus();
				            	
				                dialog.dismiss();  
				            }  
				        });  
				        builder.create().show();  
					}
				}

			}
		});
		
		//�����ܳ�0��
		seekBar.setMax(0);
		
		//�������ʵ�������������������жϲ��ŵ��½��Ƿ��������Ƶ�ļ��Ƿ���ڣ��Լ������Զ�������һ�µ�
		_config.SetVoicePanelActivity(this);
		
		//�ҵ����ذ�ť
		_buttonDownload = (Button) this.findViewById(R.id.buttondownload);
		
	}
	
	//���°�ťͼ��״̬
	public void UpdatePlaybuttonIconStatus()
	{
		if (mediaPlayer.isPlaying())
		{
			button.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_media_pause));
		}
		else 
		{
			button.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_media_play));
		}
	}

	
		
	/**
	 * ����ǰ�Ƿ��ǲ��ŵ�״̬��������򲥷ŵ�ǰ���µ���Ƶ��
		����ǣ����жϵ�ǰ���ŵ���Ƶ�Ƿ��뵱ǰ���������������򲻸ı䡣
		�粻�����򲥷��µ���Ƶ��
		
		�鿴�Ƿ������mp3�ļ�������еĻ���
		���Զ����ţ���ʾ����ʱ����������������ֶ����ڲ��Ž��ȡ�
		���û�����mp3�ļ���������ʾҪ��������������ѯ���Ƿ����أ�
		���ѡ���ǡ�����ʾ���ؽ���������ʼ���أ���������Զ��ر����ؽ��������Զ���ʼ���š�
		
		��ʼ���ź���ʾ�û���������������Ϻ󣬳����Զ���ת����һ�£��Ƿ���������Զ��������������½ڵ������ļ����������ʾֻ����һ�Σ������ڡ����á����������������á���
		���ѡ���ǣ���ʼ������һ�£�����ֻ��ǰ����һ�µġ��������һ���ĩβ����������һ����ĵ�һ�������ļ���
		�����������һ��ʱ���Զ���ת����һ����ĵ�һ�£��Զ����š�
	 * 
	 */
	@SuppressLint("DefaultLocale")
	public void AutoPlay()
	{
		//���������������ˣ��򲻲���
		//����������أ��򲻲���
		if(_config.GetInstanceMain_Tab_Readbible()._linearLayoutVoicePanel == null ||
				_config.GetInstanceMain_Tab_Readbible()._linearLayoutVoicePanel.getVisibility()!=0 ||
				_config.GetIsDownloading() == true)
		{
			if(mediaPlayer.isPlaying() == true)
			{
				mediaPlayer.stop();
			}
			seekBar.setProgress(0);
			UpdatePlaybuttonIconStatus();
			
			return;
		}

		//���ID�ַ���������2λ����01 �� 66
		String strVolumeID_00 = String.format("%02d",_config.getVolumeID());
		
		//�õ���ǰ�¶�Ӧ����ȷ����Ƶ�ļ�����
		String strCorrectMP3FileName = strVolumeID_00  + "-" + _config.getChapterID() + ".mp3";

		//�Ƿ����ڲ���
		if(mediaPlayer.isPlaying())
		{
			//�ж����ڲ��ŵ��뵱ǰ��ʾ�����Ƿ�������������Բ��ţ����أ���ȷ�ĵ�ǰ�µ�MP3

			//��ȷ�Ĳ���·�����ļ�����
			String strCorrectMP3FilePath = FileUtil.setMkdir(VoicePanelActivity.this, _config.GetVoiceFileStorageFolder()) + File.separator + strCorrectMP3FileName;
			//������������ǣ���������~
			if(strCorrectMP3FilePath.equalsIgnoreCase(mp3Path))
			{
			}
			else
			{
				//������������ֹͣ����
				if(mediaPlayer.isPlaying() == true)
				{
					mediaPlayer.stop();
				}
				
				seekBar.setProgress(0);
				
				//���²��Ű�ť��ͼ��״̬
				UpdatePlaybuttonIconStatus();

				//�������д˺���������ȷ��MP3
				AutoPlay();
			}
		}
		else
		{
			//��δ���ţ�����ֹͣ����ͣ��
			//�õ�����·��
			//MP3·��
			mp3Path = FileUtil.setMkdir(VoicePanelActivity.this, _config.GetVoiceFileStorageFolder()) + File.separator + strCorrectMP3FileName;


			//�ж�MP3�Ƿ����
			File mp3FileExists=new File(mp3Path);

			if(mp3FileExists.exists() == true)
			{
				//���ڣ�����
				//�ɹ�
				if(ResetMusic(mp3Path) == true)
				{
				}
				else
				{
					//ʧ��
					//���������һ���ļ������ڣ�����ʾ��Ҫ����
					ShowDialog_RequestToDownload();
					//�˳�����
					return;
				}

				//���ý��������ܳ���
				seekBar.setMax(mediaPlayer.getDuration());
				
				mediaPlayer.start();
				
				//���²��Ű�ť��ͼ��״̬
				UpdatePlaybuttonIconStatus();
				
				//��������seekbar���߳�
				new Thread(new runable()).start();

				//���ð�ťͼƬ
				button.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_media_pause));
			}
			else
			{
				//���������һ���ļ������ڣ�����ʾ��Ҫ����
				ShowDialog_RequestToDownload();
			}

		}
	}

	//��ʾ�Ƿ�����
	public void ShowDialog_RequestToDownload() 
	{ 
		//������������Զ�����
		if(_config.GetEnableAutoDownloadVoiceFile())
		{
        	//��ʼ����
        	StartDownloadThread();
		}
		else
		{
			AlertDialog.Builder builder = new Builder(_config.GetInstanceMain_Tab_Readbible());  
	        builder.setMessage("ʥ�����ֽ��Զ����ر��µ���Ƶ�ļ����⽫ʹ���������������ܲ���������ã�ȷ��Ҫ������");  
	        builder.setTitle("��ʾ");  
	        builder.setPositiveButton("ȷ��",  
	        new android.content.DialogInterface.OnClickListener() {
	            @Override  
	            public void onClick(DialogInterface dialog, int which) {
	            	//���²��Ű�ťͼ��״̬
	            	UpdatePlaybuttonIconStatus();
	            	
	            	//��ʼ����
	            	StartDownloadThread();
	            }  
	        });  
	        builder.setNegativeButton("ȡ��",  
	        new android.content.DialogInterface.OnClickListener() {  
	            @Override  
	            public void onClick(DialogInterface dialog, int which) {
	            	
	            	//���²��Ű�ťͼ��״̬
	            	UpdatePlaybuttonIconStatus();
	            	
	            	
	                dialog.dismiss();  
	            }  
	        });  
	        builder.create().show();  
		}
	}
	

	public boolean ResetMusic(String path) {

		//���óɹ�
		boolean bResult = false;
		
		mediaPlayer.reset();
		try {
			mediaPlayer.setDataSource(path);
			mediaPlayer.prepare();
			
			//�ɹ�
			bResult = true;
			
		} catch (IllegalArgumentException e) {
			
			e.printStackTrace();
		} catch (IllegalStateException e) {
			
			e.printStackTrace();
		} catch (IOException e) {

		}
		
		return bResult;
	}

	class runable implements Runnable {

		@Override
		public void run() {
			
			while (mediaPlayer != null && mediaPlayer.isPlaying()) 
			{
				try {
					Thread.sleep(1000);
					if (seekBar.getMax() > 0) {
						seekBar.setProgress(mediaPlayer.getCurrentPosition());
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	
	//����--------------------------------------------------
	
	private static final int DOWNLOAD_PREPARE = 0;
	private static final int DOWNLOAD_WORK = 1;
	private static final int DOWNLOAD_OK = 2;
	private static final int DOWNLOAD_ERROR =3;

	/**
	 * ��ť����¼�
	 */

	
	private Button _buttonDownload ;

	
	public void onButtonDownloadClick(View v) {
		
		//ֹͣ����
		if(mediaPlayer.isPlaying() == true)
		{
			mediaPlayer.stop();
		}

		seekBar.setProgress(0);
		
		//���²��Ű�ť��ͼ��״̬
		UpdatePlaybuttonIconStatus();
		
		downloadSize = 0;
		fileSize = 0;
		
		ShowDialog_RequestToDownload();
		
	}
	
	//���������߳�����
	public void StartDownloadThread()
	{
		if(_config.GetIsDownloading())
		{
			return;
		}

		_buttonDownload.setEnabled(false);
		
		//ֹͣ����
		if(mediaPlayer.isPlaying())
		{
			mediaPlayer.stop();
		}
		
		seekBar.setProgress(0);
	
		button.setEnabled(false);
		seekBar.setEnabled(false);

		
		Toast.makeText(VoicePanelActivity.this, "��ʼ���� �� " + _config.getChapterID() + " ��", Toast.LENGTH_SHORT).show();
		new Thread(){
			@Override
			public void run() {
				downloadFileInThread();
				super.run();
			}
		}.start();
	}
	
	
	/**
	 * �ļ�����
	 */
	private void downloadFileInThread()
	{
		//��������·��
		String strVolumeID_00 = String.format("%02d",_config.getVolumeID());
		String strMP3FileNameForDownload = strVolumeID_00  + "-" + _config.getChapterID() + ".mp3";
		
		
		//�õ���ǰ�¶�Ӧ����ȷ����Ƶ�ļ�����

		//�������
		String strVolumeName = _config.GetVolumeNameByID(_config.getVolumeID());
		String strCorrectMP3FileName = strVolumeName  + "��" + _config.getChapterID() + "��.mp3";
		String strDownloadURL_MP3 = _config.GetInternetMP3FolderPath() + File.separator + strCorrectMP3FileName;
		
		//ɾ�����е��ļ�����������
		mp3Path = FileUtil.setMkdir(VoicePanelActivity.this, _config.GetVoiceFileStorageFolder()) + File.separator + strMP3FileNameForDownload;
		
		//ɾ��mp3
		File mp3FileDelete = new File(mp3Path);

		mp3FileDelete.delete();
		
		//����mp3
		download_subfunction(strDownloadURL_MP3,strMP3FileNameForDownload);
		

	}
	
	private void download_subfunction(String strDownloadURL,String strFileName)
	{
		try {
			
			strDownloadURL = URLEncoder.encode(strDownloadURL,"utf-8");
			strDownloadURL = strDownloadURL.replaceAll("%3A", ":").replaceAll("%2F", "/");
			
			URL u = new URL(strDownloadURL);
						
			URLConnection conn = u.openConnection();
			conn.connect();
			InputStream is = conn.getInputStream();
			fileSize = conn.getContentLength();
			if(fileSize<1||is==null)
			{
				sendMessage(DOWNLOAD_ERROR);
			}
			else
			{
				sendMessage(DOWNLOAD_PREPARE);
				FileOutputStream fos = new FileOutputStream(getPath(strFileName));
				byte[] bytes = new byte[1024];
				int len = -1;
				
				
				while((len = is.read(bytes))!=-1 &&
						_config.GetInstanceMain_Tab_Readbible()._linearLayoutVoicePanel.getVisibility() == 0 
						)
				{
					fos.write(bytes, 0, len);
					downloadSize+=len;
					sendMessage(DOWNLOAD_WORK);
				}
				
				is.close();
				fos.close();
				
				sendMessage(DOWNLOAD_OK);
			}
		} catch (Exception e) {
			sendMessage(DOWNLOAD_ERROR);

		} 
	}
	
	/**
	 * �ļ�һ���Ĵ�С
	 */
	int fileSize = 0;
	/**
	 * �Ѿ����صĴ�С
	 */
	int downloadSize = 0;
	/**
	 * handler������Ϣ
	 */
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWNLOAD_PREPARE:
				//��������״̬
				_config.SetIsDownloading(true);

				break;
			case DOWNLOAD_WORK:

				
				int res = downloadSize*100/fileSize;
				_buttonDownload.setText(""+res+"%");
				break;
			case DOWNLOAD_OK:
				//�ж�Ŀ¼���Ƿ��ж�Ӧ��MP3�ļ�������У����Բ���
				
				downloadSize = 0;
				fileSize = 0;
				
				
				String strVolumeID_00 = String.format("%02d",_config.getVolumeID());
				
				//�õ���ǰ�¶�Ӧ����ȷ����Ƶ�ļ�����
				String strCorrectMP3FileName = strVolumeID_00  + "-" + _config.getChapterID() + ".mp3";
				
				mp3Path = FileUtil.setMkdir(VoicePanelActivity.this, _config.GetVoiceFileStorageFolder()) + File.separator + strCorrectMP3FileName;

				//�ж�MP3�Ƿ����
				File mp3FileExists = new File(mp3Path);

				if(mp3FileExists.exists())
				{
					//��������״̬
					_config.SetIsDownloading(false);
					
					Toast.makeText(VoicePanelActivity.this, "�� " + _config.getChapterID() + " �� �������", Toast.LENGTH_SHORT).show();
					
					//������ϣ����Բ���
					AutoPlay();
					
					_buttonDownload.setText("����");
					
					_buttonDownload.setEnabled(true);
					button.setEnabled(true);
					seekBar.setEnabled(true);
				}

				
				break;
			case DOWNLOAD_ERROR:
				//��������״̬
				_config.SetIsDownloading(false);
				
				Toast.makeText(VoicePanelActivity.this, "���س���������������״̬", Toast.LENGTH_SHORT).show();
				
				_buttonDownload.setEnabled(true);
				button.setEnabled(true);
				seekBar.setEnabled(true);
				
				break;
			}
			super.handleMessage(msg);
		}
	};
	/**
	 * �õ��ļ��ı���·��
	 * @return
	 * @throws IOException
	 */
	private String getPath(String strFileName) throws IOException
	{
		String path = FileUtil.setMkdir(this,_config.GetVoiceFileStorageFolder())+File.separator+strFileName;
		return path;
	}
	/**
	 * ��hand������Ϣ
	 * @param what
	 */
	private void sendMessage(int what)
	{
		Message m = new Message();
		m.what = what;
		handler.sendMessage(m);
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
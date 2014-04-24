package cn.elijah.biblehelper;


import java.util.Arrays;
import cn.elijah.biblehelper.R;
import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.text.ClipboardManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.HeaderViewListAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class Main_Tab_Readbible extends ActivityGroup {


	private Button mButtonCenter;
	
	//��дApplication����Ϊȫ�����������ļ�
	private Config _config;

	@Override
	   public void onCreate(Bundle savedInstanceState) {
	       super.onCreate(savedInstanceState);
	       setContentView(R.layout.main_tab_readbible);

	       //����Զ����Ӧ�ó���Config
	       _config = (Config)getApplication(); 
	       
	       
	       //���浱ǰʵ��
	       _config.SetInstanceMain_Tab_Readbible(this);
	       
	       
			//�õ��������
			_linearLayoutVoicePanel = (LinearLayout)findViewById(R.id.ll_voice_panel);
			

           //�м� ���� �µı��ⰴť
           mButtonCenter = (Button)findViewById(R.id.button_center);
	       
	       //��ʾ���ĵ�listview
	       ListView listViewBibleContent = (ListView)findViewById(R.id.listViewBibleContent);

	       
		   View loadMoreView = getLayoutInflater().inflate(R.layout.layout_next_chapter, null); 
		  	final Button loadMoreNextButton = (Button) loadMoreView.findViewById(R.id.ButtonNextChapter); 
		       loadMoreNextButton.setOnClickListener(new OnClickListener() {
		           @Override
		           public void onClick(View v) {

		        	   //��ת����һ��
		        	   //�õ���ǰ�İ�ť״̬�������ťΪ�ɼ���˵����������һ��	
		        	   //�������һ��
		        	   if(_config.GetIsHasNextChapter())
		        	   {
		        		   //��ID����1
		        		   _config.setChapterID(_config.getChapterID() + 1);
		        		   
		        		   FillListView();
		        		   
							//�������״̬�����Բ������½�
							if(_linearLayoutVoicePanel.getVisibility()==0)
							{								
								if(_config.GetVoicePanelActivity().seekBar != null)
								{
									_config.GetVoicePanelActivity().seekBar.setProgress(0);
									_config.GetVoicePanelActivity().seekBar.setMax(0);
								}
								
								_config.GetVoicePanelActivity().AutoPlay();
							}
							else
							{
								//ˢ�²������ͽ����״̬
								if(_config.GetVoicePanelActivity() != null)
								{
									if(_config.GetVoicePanelActivity().mediaPlayer != null &&
											_config.GetVoicePanelActivity().mediaPlayer.isPlaying() == true)
									{
										_config.GetVoicePanelActivity().mediaPlayer.stop();
									}
									
									if(_config.GetVoicePanelActivity().seekBar != null)
									{
										_config.GetVoicePanelActivity().seekBar.setProgress(0);
										_config.GetVoicePanelActivity().seekBar.setMax(0);
									}

									_config.GetVoicePanelActivity().UpdatePlaybuttonIconStatus();
								}
							}
		        	   }
		        	   
		       }
		   });

		       
		       final Button loadMorePrevButton = (Button) loadMoreView.findViewById(R.id.ButtonPrevChapter); 
		       loadMorePrevButton.setOnClickListener(new OnClickListener() {
		           @Override
		           public void onClick(View v) {

		        	   //��ת����һ��
		        	   //�õ���ǰ�İ�ť״̬�������ťΪ�ɼ���˵����������һ��		        	   

		        	   //�������һ��
		        	   if(_config.GetIsHasPrevChapter())
		        	   {
		        		   //��ID����1
		        		   _config.setChapterID(_config.getChapterID() - 1);
		        		   
		        		   FillListView();
		        		   
							//�������״̬�����Բ������½�
							if(_linearLayoutVoicePanel.getVisibility()==0)
							{
								
								if(_config.GetVoicePanelActivity().seekBar != null)
								{
									_config.GetVoicePanelActivity().seekBar.setProgress(0);
									_config.GetVoicePanelActivity().seekBar.setMax(0);
								}
								
								_config.GetVoicePanelActivity().AutoPlay();
							}
							else
							{
								//ˢ�²������ͽ����״̬
								if(_config.GetVoicePanelActivity() != null)
								{
									if(_config.GetVoicePanelActivity().mediaPlayer != null &&
											_config.GetVoicePanelActivity().mediaPlayer.isPlaying() == true)
									{
										_config.GetVoicePanelActivity().mediaPlayer.stop();
									}
									
									if(_config.GetVoicePanelActivity().seekBar != null)
									{
										_config.GetVoicePanelActivity().seekBar.setProgress(0);
										_config.GetVoicePanelActivity().seekBar.setMax(0);
									}

									_config.GetVoicePanelActivity().UpdatePlaybuttonIconStatus();
								}
							}
		        	   }

		       }
		   });

	       //������footerview����setadapter
	       listViewBibleContent.addFooterView(loadMoreView);

		       
	       //����ʾ���ĵ�listview�洢Ϊȫ�ֱ������ȴ�ѡ���ĺ�ˢ��
	       _config.SetListViewBibleContent(listViewBibleContent);

	       if(_config.GetListViewBibleContent() != null)
	    	{
	    	   //��ʼ����������
	    	   FillListView();

		       _config.GetListViewBibleContent().setOnItemLongClickListener(new OnItemLongClickListener() {
				@Override
				public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) 
				{
					//�����˵�
					showLongClickMenu(position);
					return true;
				}
	           });

	           //Ĭ��ѡ�е�һ�У��Ա��浱ǰ״̬�������������
		       SelectItemIndex(0);

	    	}
	       

	       //��������������壬�������Ű�ť�����Ž����������ؽ�����
	       LinearLayout linearLayoutVoicePanel = (LinearLayout)findViewById(R.id.ll_voice_panel);
	       
	       View control1 = getLocalActivityManager().startActivity(
	               "VoicePanelActivity",
	               new Intent(Main_Tab_Readbible.this, VoicePanelActivity.class))
	               .getDecorView();
	       
	       
	       linearLayoutVoicePanel.addView(control1);

	 }



	//���� ��ť
	public void btnmainright(View v) {

		Intent _intentSearch = new Intent (Main_Tab_Readbible.this,SearchBibleActivity.class);
		startActivity(_intentSearch);
     }  	
    
	//���ñ������м�����
	public void onButtonCenterClicked(View v) {
		Intent intent = new Intent (Main_Tab_Readbible.this,SelectVolumeAndChapter.class);			
		startActivity(intent);
     }  


	public LinearLayout _linearLayoutVoicePanel = null;
	
	//������ť
	public void onButtonVoiceClicked(View v)
	{
		//��ʾ������
	    if(_linearLayoutVoicePanel.getVisibility() == 0)
        {
	    	//����
	    	_linearLayoutVoicePanel.setVisibility(View.GONE);
	    	//��ȡ���Ž���ʵ��
	    	if(_config.GetVoicePanelActivity() != null )
	    	{
	    		//ֹͣ����
	    		if(_config.GetVoicePanelActivity().mediaPlayer != null && 
	    				_config.GetVoicePanelActivity().mediaPlayer.isPlaying())
	    		{
	    			_config.GetVoicePanelActivity().mediaPlayer.pause();
	    		}

		    	//���²��Ű�ťͼ��״̬
		    	_config.GetVoicePanelActivity().UpdatePlaybuttonIconStatus();
	    	}
        }
	    else
	    {
	    	//��ʾ
	    	_linearLayoutVoicePanel.setVisibility(View.VISIBLE);
	    	
	    	//��ȡ���Ž���ʵ��
	    	if(_config.GetVoicePanelActivity() != null )
	    	{
				if(_config.GetVoicePanelActivity().seekBar.getProgress() > 0)
				{
					_config.GetVoicePanelActivity().mediaPlayer.start();
				}
				else
				{
		    		//���Բ���
			    	_config.GetVoicePanelActivity().AutoPlay();
				}
				
				//���²��Ű�ťͼ��״̬
				_config.GetVoicePanelActivity().UpdatePlaybuttonIconStatus();
	    	}
	    }
	    
	    
	    
	}
	
	//�����ʷ��¼������Ϊ�Ѷ�+1
	public void MarkHistory(int VolumeID, int ChapterID)
	{
		DatabaseHelper dbHelper = new DatabaseHelper(Main_Tab_Readbible.this, _config.GetStrDatabaseNameForDeployment());

		// �õ�һ��SQLiteDatabase����  
        SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();  

        String sql = "SELECT [count] as ReadCount FROM biblestatistics where VOLUMESN=" + VolumeID +" and CHAPTERSN=" + ChapterID;
        Cursor cursor = sqliteDatabase.rawQuery(sql, null);

        //�м�¼
        if (cursor.moveToNext())
        {
        	int iReadCount = cursor.getInt(cursor.getColumnIndex("ReadCount"));
        	//����һ��
        	iReadCount++;
        	
        	String strSQL = "update biblestatistics set [count]=" + iReadCount + " where VOLUMESN=" + VolumeID +" and CHAPTERSN=" + ChapterID;
        	sqliteDatabase.execSQL(strSQL);
        }
        else
        {
        	//�޼�¼
        	String strSQL = "insert into biblestatistics (VOLUMESN,CHAPTERSN,[COUNT]) values (" +VolumeID+"," + ChapterID + ",1)";
        	sqliteDatabase.execSQL(strSQL);
        }
        
        
        //��������Ķ��������½�
        _config.WriteRecentlyReadingToDatabase();
        
	}
	
    /**
    * �ɾ�ID����ID�õ���������
    */
    public int GetLectionByVolumeIDandChapterID(int VolumeID, int ChapterID,String[] outStrsResult)
    {
    	//�������ݸ���
    	int iResult = 0;
    	//�������
    	Arrays.fill(outStrsResult, "");
    	
        //����DatabaseHelper����  
        DatabaseHelper dbHelper = new DatabaseHelper(Main_Tab_Readbible.this, _config.GetStrDatabaseNameForDeployment());  
        // �õ�һ��SQLiteDatabase����  
        SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();  

        String strSQL = "select [ID], [VolumeSN], [ChapterSN], [Lection] from [Bible]  where  VolumeSN="+ VolumeID +"  and ChapterSN="+ ChapterID +" order by [ID] asc"; 
        Cursor cursor = sqliteDatabase.rawQuery(strSQL,null);
        
        // ������ƶ�����һ�У��Ӷ��жϸý�����Ƿ�����һ�����ݣ�������򷵻�true��û���򷵻�false  
        while (cursor.moveToNext())
        {
        	outStrsResult[iResult] = cursor.getString(cursor.getColumnIndex("Lection"));  
            iResult++;
        }


        //�ж��Ƿ�ӡ���һ�¡���ť
        int iChapterCount = 0;
        
        // 
        strSQL = "select [SN], [ChapterNumber] from [BibleID] where [SN]=" +VolumeID +"  order by [SN] asc"; 
        
        cursor = sqliteDatabase.rawQuery(strSQL, null);
        
        // ������ƶ�����һ�У��Ӷ��жϸý�����Ƿ�����һ�����ݣ�������򷵻�true��û���򷵻�false  
        if (cursor.moveToNext())
        {
        	iChapterCount = cursor.getInt(cursor.getColumnIndex("ChapterNumber"));  
        }
        
       	Button buttonNextChapter = null;
    	buttonNextChapter = (Button)_config.GetListViewBibleContent().findViewById(R.id.ButtonNextChapter);
       
    	if(buttonNextChapter != null)
    	{
    	    if(iChapterCount == ChapterID)
            {
        		buttonNextChapter.setVisibility(View.GONE);
        		_config.SetIsHasNextChapter(false);
            }
    	    else
    	    {
        		buttonNextChapter.setVisibility(View.VISIBLE);
        		_config.SetIsHasNextChapter(true);
    	    }
    	}

    	Button ButtonPrevChapter = null;
    	ButtonPrevChapter = (Button)_config.GetListViewBibleContent().findViewById(R.id.ButtonPrevChapter);
       
    	if(ButtonPrevChapter != null)
    	{
    	    if(1 == ChapterID)
            {
    	    	ButtonPrevChapter.setVisibility(View.GONE);
        		_config.SetIsHasPrevChapter(false);
            }
    	    else
    	    {
    	    	ButtonPrevChapter.setVisibility(View.VISIBLE);
        		_config.SetIsHasPrevChapter(true);
    	    }
    	}
    	
    	
        
        return iResult;
    }
	
    

    
    //��侭�ĵ�listview
    public void FillListView()
    {

 	   //��������״̬
 	   _config.SetIsDownloading(false);
 	   
    	//������ID
        _config.setVerseID(1);
        _config.SetStrVolumeName(_config.GetVolumeNameByID(_config.getVolumeID()));


        //���±���
        StringBuilder sb = new StringBuilder();
        sb.append(_config.GetStrVolumeName());
		sb.append(" ��");
		sb.append(_config.getChapterID());
		sb.append("��");
        //���ñ�����
        mButtonCenter.setText(sb.toString());
		sb.delete(0, sb.length());
		sb = null;


		//���¾������ݣ���ˢ�¿ؼ�������Դ
        int iCount = _config.GetInstanceMain_Tab_Readbible().GetLectionByVolumeIDandChapterID(_config.getVolumeID(),_config.getChapterID(),_config.getStrsBibleContent());

        //�����һ�������о��ģ��������ʾ
        if(iCount > 0 && _config.getStrsBibleContent()[0].length()>0)
        {
	 	   //���û�ȡ����С����
	 	   _config.setVerseCount(iCount);

	 	   //ˢ�¾����б�
	 	   HeaderViewListAdapter listAdapter = (HeaderViewListAdapter) _config.GetListViewBibleContent().getAdapter();  //�����Ƚ�listViewǿ��ת��ΪHeaderViewListAdapter
		   
		   MyAdapterLection myAdapterLection = null;
		   
		   //����ListView
		   if(listAdapter == null)
		   {
			   myAdapterLection = new MyAdapterLection(Main_Tab_Readbible.this);
		   }
		   else
		   {
			   //ͨ��HeaderViewListAdapter ת��Ϊ�Զ����adapter
			   myAdapterLection = (MyAdapterLection)listAdapter.getWrappedAdapter();
		   }
	
		   _config.GetListViewBibleContent().setAdapter(myAdapterLection);
	
		   //ˢ��
		   myAdapterLection.notifyDataSetChanged();
	   
	       //Ĭ��ѡ�о����б�ĵ�һ��
	        this.SelectItemIndex(0);
	        
	        //������ʷ��¼
	        MarkHistory(_config.getVolumeID(),_config.getChapterID());
	        
        }
    

    }
    
    
    /**
     * ѡ�еڼ��У���0��ʼ
    */
    public void SelectItemIndex(int i)
    {
        //Ĭ��ѡ�е�һ�У��Ա��浱ǰ״̬�������������
        if(_config.GetListViewBibleContent().getCount()>0)
        {
     	   _config.GetListViewBibleContent().setSelection(i);
        }
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

	
	//��ʾ�����˵�����
	private void showLongClickMenu(final int selectedIndex)
	{
		String items[] = new String[]{"����С��","��������","������ǩ","������ǩ"};
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("��ݲ���");

		builder.setItems(items, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(final DialogInterface dialog, int which)
			{
				switch(which)
				{
				case 0:
					String strTitle = "";
					String strTextCopy = "";
					
					//����С��
					//����+�º�+�ں�
					int iVerseID = selectedIndex+1;
					 strTitle = _config.GetStrVolumeName() + " " + _config.getChapterID() + ":" + iVerseID;

					 strTextCopy = strTitle + " " +  _config.getStrsBibleContent()[selectedIndex];
					
					ClipboardManager clipboard1 = (ClipboardManager) getSystemService(Main_Tab_History.CLIPBOARD_SERVICE);
					clipboard1.setText(strTextCopy);

					Toast.makeText(Main_Tab_Readbible.this,"���Ƴɹ�", Toast.LENGTH_LONG).show();
					
					break;
				case 1:
					//��������
					StringBuilder sbTextCopy = new StringBuilder();
					
					sbTextCopy.append(_config.GetStrVolumeName());
					sbTextCopy.append(" �� ");
					sbTextCopy.append(_config.getChapterID());
					sbTextCopy.append(" ��\r\n");
					
					 for(int i=0;i<_config.getVerseCount();i++)
					 {
						 sbTextCopy.append(i+1);
						 sbTextCopy.append(" ");
						 sbTextCopy.append(_config.getStrsBibleContent()[i]);
						 sbTextCopy.append("\r\n");
					 }
					 
					ClipboardManager clipboard2 = (ClipboardManager) getSystemService(Main_Tab_History.CLIPBOARD_SERVICE);
					clipboard2.setText(sbTextCopy.toString());

					Toast.makeText(Main_Tab_Readbible.this,"���Ƴɹ�", Toast.LENGTH_LONG).show();
					
					sbTextCopy.delete(0, sbTextCopy.length());
					sbTextCopy = null;
					
					break;
				case 2:
					//������ǩ
					
			        int iVerseIDForBookmark = selectedIndex+1;
			        
					String strSQL ="insert into  [BibleLabels]   ([name],[volumesn],[chaptersn],[versesn]) values ('"+ _config.GetStrVolumeName() +"', "+ _config.getVolumeID() +", "+ _config.getChapterID() +", "+ iVerseIDForBookmark +") ";

				  	//�ɾ�ID�õ��Ѷ����µ��Ķ���������
					DatabaseHelper dbHelper = new DatabaseHelper(Main_Tab_Readbible.this, _config.GetStrDatabaseNameForDeployment());
			        SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();  
			        sqliteDatabase.execSQL(strSQL);

			        Toast.makeText(Main_Tab_Readbible.this,"�����ǩ�ɹ�", Toast.LENGTH_LONG).show();

					break;
				case 3:
					//������ǩ
					Intent intentBookmark = new Intent (Main_Tab_Readbible.this,BookmarkActivity.class);
					startActivity(intentBookmark);

					break;
				}

				//�رմ���
		        Handler handler = new Handler();
		        Runnable runnable = new Runnable()
		        {
		          @Override
		           public void run()
		           {
		               // ����AlertDialog���dismiss()�����رնԻ���Ҳ���Ե���cancel()������
		            	  dialog.dismiss();
		               }
		           };
		
		           handler.post(runnable);
		      }
		   });
		
		    builder.
		    setNegativeButton("ȡ ��", new DialogInterface.OnClickListener() { 
		    @Override 
		    public void onClick(DialogInterface dialog, int which) { 
		        	dialog.dismiss();
		        } 
		    });
		    
		
		    builder.create().show();
		}

	
 	
}

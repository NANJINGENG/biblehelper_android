package cn.elijah.biblehelper;



import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class BookmarkActivity extends Activity {

	//��дApplication����Ϊȫ�����������ļ�
	private Config _config;
	
	//��ǩ�б�
	private ListView _listViewBookmark;
	
	//������������ID
	private int[] _intsBookmarkVolumeID;
	
	//����������������
	private String[] _strsBookmarkVolumeName;
	
	//�����������ID
	private int[] _intsBookmarkChapterID;
	
	//�����������ID
	private int[] _intsBookmarkVerseID;
	
	//��ǩ�����ݿ��е�ID������ɾ��
	private int[] _intsBookmarkID_ForDelete;
	

	
	//���������listview����
	private MyAdapterVolume _adapterSearch;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bookmark_manage);

		//����Զ����Ӧ�ó���Config       
		_config = (Config)getApplication(); 


		//listviewʵ��
		_listViewBookmark = (ListView)findViewById(R.id.listViewBookmark);
		_listViewBookmark.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				

				
				int iVolumeID = _intsBookmarkVolumeID[position];
				int iChapterID = _intsBookmarkChapterID[position];
				int iVerseID = _intsBookmarkVerseID[position];
				String strVolumeName = _strsBookmarkVolumeName[position];

				//��ת������£���
				_config.setVolumeID(iVolumeID);
				_config.setChapterID(iChapterID);
				_config.setVerseID(iVerseID);
				_config.SetStrVolumeName(strVolumeName);


				//��侭�ĵ�listview
	     		_config.GetInstanceMain_Tab_Readbible().FillListView();
	
	     		//������ڲ��ţ����Բ������½�
	     		if(_config.GetVoicePanelActivity().mediaPlayer != null && 
	     				_config.GetVoicePanelActivity().mediaPlayer.isPlaying())
	     		{
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
						}

						_config.GetVoicePanelActivity().UpdatePlaybuttonIconStatus();
					}
	     		}

	     		//�ر�ѡ���µĴ���
	     		if(_config.GetMainActivity() != null)
	     		{
	     			_config.GetMainActivity().ChangePage(0);
	     		}

	 	       _config.GetInstanceMain_Tab_Readbible().SelectItemIndex(iVerseID - 1);

	 	       //�رմ���
	 	       finish();	
			}	
		});

		FillListView();

		//�������ʵ��
		_config.SetBookmarkActivity(this);
	}

	public void FillListView()
	{
		String strSQL ="select [id], [name], [volumesn], [chaptersn], [versesn]  from  [BibleLabels] order by [id] desc ";

		//��ѯ��ǩ
		DatabaseHelper dbHelper = new DatabaseHelper(BookmarkActivity.this, _config.GetStrDatabaseNameForDeployment());

		// �õ�һ��SQLiteDatabase���� 
        SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();  

        Cursor cursor = sqliteDatabase.rawQuery(strSQL, null);

        int iCount = cursor.getCount();
		if(iCount>0)
		{

			_intsBookmarkVolumeID = new int[iCount];
			_intsBookmarkChapterID = new int[iCount];
			_intsBookmarkVerseID = new int[iCount];
			_strsBookmarkVolumeName = new String[iCount];
			_intsBookmarkID_ForDelete = new int[iCount];
			
			
	        int i=0;

	        while(cursor.moveToNext())
	        {
	        	//�������
	        	_strsBookmarkVolumeName[i] = cursor.getString(cursor.getColumnIndex("name"));
	        	
	        	//���ID
	        	_intsBookmarkVolumeID[i] = cursor.getInt(cursor.getColumnIndex("volumesn"));
	        	
	        	//��ID
	        	_intsBookmarkChapterID[i] = cursor.getInt(cursor.getColumnIndex("chaptersn"));

	        	//��ID
	        	_intsBookmarkVerseID[i] = cursor.getInt(cursor.getColumnIndex("versesn"));

	        	//��ǩID������ɾ��
	        	_intsBookmarkID_ForDelete[i] = cursor.getInt(cursor.getColumnIndex("id"));
	        	
	        	i++;
	        }
	        
			
		}
		else
		{
			_intsBookmarkVolumeID = null;
			_intsBookmarkChapterID = null;
			_intsBookmarkVerseID = null;
			_strsBookmarkVolumeName = null;
			_intsBookmarkID_ForDelete = null;
		}


		_adapterSearch = new MyAdapterVolume(BookmarkActivity.this);
		_listViewBookmark.setAdapter(_adapterSearch);
		_adapterSearch.notifyDataSetChanged();
	}

	public void onButtonBackClick(View v) {  
		this.finish();
	}
	
	//Ϊѡ�����listviewʹ�õ�������
	private class MyAdapterVolume extends BaseAdapter 
	{
		public MyAdapterVolume(Context context) 
		{
			mContext = context;
		}

		 @Override 
        public int getCount()
        {      
			 if(_strsBookmarkVolumeName == null)
			 {
				 return 0;
			 }
			 else
			 {
				 return _strsBookmarkVolumeName.length;				 
			 }
        }

        @Override 
        public Object getItem(int arg0) { 
            return arg0; 
        }

        @Override
        public long getItemId(int position) {
            return position; 
        }

        @Override 
        public View getView(final int position, View convertView, ViewGroup parent) 
        { 
            Button buttonDelete = null;

            if (convertView == null) 
            {
            	convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_item_bookmark, null);
            }

            TextView mTextView1 = (TextView) convertView.findViewById(R.id.ItemViewBookmarkText);

            if(_strsBookmarkVolumeName[position] != null)
            {
	            String strBookmars = _strsBookmarkVolumeName[position] + " " + _intsBookmarkChapterID[position] + ":" + _intsBookmarkVerseID[position];
	            mTextView1.setText(strBookmars);
	            mTextView1.setTextColor(Color.BLACK);
	            mTextView1.setTextSize(20);
          
	            buttonDelete=(Button)convertView.findViewById(R.id.buttonDelete); 
	            //����¼�
	            buttonDelete.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						
	
						String strSQL ="delete from  [BibleLabels] where [id]=" + _intsBookmarkID_ForDelete[position] ;
	
					  	//�ɾ�ID�õ��Ѷ����µ��Ķ���������
						DatabaseHelper dbHelper = new DatabaseHelper(BookmarkActivity.this, _config.GetStrDatabaseNameForDeployment());
				        SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();  
				        sqliteDatabase.execSQL(strSQL);
	
				        Toast.makeText(BookmarkActivity.this,"ɾ����ǩ�ɹ�", Toast.LENGTH_LONG).show();
				        
				        //ˢ�½���
				        FillListView();
	
					}
				});
	            
            }
            

			return convertView;
        } 
        private Context mContext;
	 }


}

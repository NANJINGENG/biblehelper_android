package cn.elijah.biblehelper;


import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SearchBibleActivity extends Activity {


	//��дApplication����Ϊȫ�����������ļ�
	private Config _config;

	//������Χ
	private Spinner _spinnerSearchRange;

	//�������
	private ListView _listViewSearchResult;

	//���������listview����
	private MyAdapterVolume _adapterSearch;
	
	//��������ľ�������
	private String[] _strsSearchLection;
	
	//������������ID
	private int[] _intsSearchResultVolumeID;
	
	//����������������
	private String[] _strsSearchResultVolumeName;
	
	//�����������ID
	private int[] _intsSearchResultChapterID;
	
	//�����������ID
	private int[] _intsSearchResultVerseID;
	
	//�ؼ���textbox
	private EditText _editTextKeyword;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.search_bible);
	       
	       //����Զ����Ӧ�ó���Config
	       _config = (Config)getApplication(); 

	        _editTextKeyword = (EditText)findViewById(R.id.editTextKeyword);
	       //�����¼�
	       _editTextKeyword.setOnFocusChangeListener(new OnFocusChangeListener() {
	    	   @Override
				public void onFocusChange(View v, boolean hasFocus) {   

	    	   }
	       });
  
	       //�����б������
	       String[] strsSearchRange = GetSearchRange();

	       ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.simple_dropdown_item_1line,strsSearchRange);
  
	       //������Χ
	       _spinnerSearchRange =  (Spinner)findViewById(R.id.spinnerSearchRange);
	       
	       _spinnerSearchRange.setAdapter(adapter);
	       
	       _spinnerSearchRange.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}  
		});

	       //listviewʵ��
	       _listViewSearchResult = (ListView)findViewById(R.id.listViewSearchResult);
	       _listViewSearchResult.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {

				
				int iVolumeID = _intsSearchResultVolumeID[position];
				int iChapterID = _intsSearchResultChapterID[position];
				int iVerseID = _intsSearchResultVerseID[position];
				
				//��ת�������
				_config.setVolumeID(iVolumeID);
				_config.setChapterID(iChapterID);
				_config.SetStrVolumeName(_strsSearchResultVolumeName[position]);
				
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

	  }

	  //�õ�������Χ�ַ�������
	  private String[] GetSearchRange()
	  {
		  String[] strsResult = new String[69];
		  
		  strsResult[0] = "����ȫ��";
		  strsResult[1] = "��Լ";
		  strsResult[2] = "��Լ";

			//�ɾ�ID�õ��Ѷ����µ��Ķ���������
			DatabaseHelper dbHelper = new DatabaseHelper(SearchBibleActivity.this, _config.GetStrDatabaseNameForDeployment());

			// �õ�һ��SQLiteDatabase����  
	        SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();  

	        String sql = "select fullname from [bibleid] order by [SN]";
	        Cursor cursor = sqliteDatabase.rawQuery(sql, null);

	        int i = 3;
	        while(cursor.moveToNext())
	        {
	        	strsResult[i] = cursor.getString(cursor.getColumnIndex("FullName"));
	        	i++;
	        }


		  return strsResult;
	  }

	//���������ť
	public void onSearchButtonClicked(View v) 
	{
		if(_editTextKeyword.getText().toString().length()<=0)
		{
			Toast.makeText(getApplicationContext(), "������Ҫ����������", Toast.LENGTH_SHORT).show();
		}
		else
		{
			_strsSearchLection = GetSearchResult(_editTextKeyword.getText().toString(),_spinnerSearchRange.getSelectedItemPosition());
			
	       _adapterSearch = new MyAdapterVolume(SearchBibleActivity.this,_strsSearchLection);
	       
	       _listViewSearchResult.setAdapter(_adapterSearch);
		       
			_adapterSearch.notifyDataSetChanged();


			//�ر������
 	       ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(SearchBibleActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
 	       
 	       
		}
	}
	
	//�õ��������
	private String[] GetSearchResult(String strKeyword,int selectedRow)
	{
		String strSQL ="";
		
		String[] strsResult = null;
		switch (selectedRow) {
		case 0://ȫ��
			strSQL = "select FULLNAME,VOLUMESN,CHAPTERSN,VERSESN,LECTION from  BIBLE  inner join BIBLEID ON BIBLE.VOLUMESN=BIBLEID.SN where lection like   '%" + strKeyword + "%'  order by id";
			break;
		case 1://��Լ
			strSQL = "select FULLNAME,VOLUMESN,CHAPTERSN,VERSESN,LECTION from  BIBLE  inner join BIBLEID ON BIBLE.VOLUMESN=BIBLEID.SN where lection like '%" + strKeyword + "%'  and neworold=0 order by id";
			break;
		case 2://��Լ
			strSQL = "select FULLNAME,VOLUMESN,CHAPTERSN,VERSESN,LECTION from  BIBLE  inner join BIBLEID ON BIBLE.VOLUMESN=BIBLEID.SN where lection like '%" + strKeyword + "%'  and neworold=1 order by id";
			break;
		default:
			//�������������ID����
			int volumeID = selectedRow-3+1; 
			strSQL = "select FULLNAME,VOLUMESN,CHAPTERSN,VERSESN,LECTION from  BIBLE  inner join BIBLEID ON BIBLE.VOLUMESN=BIBLEID.SN where lection like '%" + strKeyword + "%'  and VOLUMESN=" + volumeID + "  order by id";
			break;
		}

	  	//�ɾ�ID�õ��Ѷ����µ��Ķ���������
		DatabaseHelper dbHelper = new DatabaseHelper(SearchBibleActivity.this, _config.GetStrDatabaseNameForDeployment());

		// �õ�һ��SQLiteDatabase����  
        SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();  

        Cursor cursor = sqliteDatabase.rawQuery(strSQL, null);
        
        int iCount = cursor.getCount();
		if(iCount>0)
		{
	        strsResult = new String[iCount];
	        
			_intsSearchResultVolumeID = new int[iCount];
			_intsSearchResultChapterID = new int[iCount];
			_intsSearchResultVerseID = new int[iCount];
			_strsSearchResultVolumeName = new String[iCount];
			
	        StringBuilder sbTemp = new StringBuilder();
	        
	        int i=0;
	
	        while(cursor.moveToNext())
	        {
	        	String strLection = cursor.getString(cursor.getColumnIndex("Lection"));
	        	strLection = strLection.replace(strKeyword, "</font><font color='red'>" + strKeyword + "</font><font color='black'>");
	
	        	//�������
	        	_strsSearchResultVolumeName[i] = cursor.getString(cursor.getColumnIndex("FullName"));
	        	
	        	//���ID
	        	_intsSearchResultVolumeID[i] = cursor.getInt(cursor.getColumnIndex("VolumeSN"));
	        	
	        	//��ID
	        	int iCHAPTERSN = cursor.getInt(cursor.getColumnIndex("ChapterSN"));
	        	_intsSearchResultChapterID[i] = iCHAPTERSN;

	        	//��ID
	        	int iVERSESN = cursor.getInt(cursor.getColumnIndex("VerseSN"));
	        	_intsSearchResultVerseID[i] = iVERSESN;
	        	
	        	//���һ��stringbuilder
	        	sbTemp.delete(0, sbTemp.length());
	        	sbTemp.append("<font color='green'>");
	        	sbTemp.append(cursor.getString(cursor.getColumnIndex("FullName")));
	        	sbTemp.append("</font>&nbsp;<font color='black'>");
	        	sbTemp.append(iCHAPTERSN);
	        	sbTemp.append(":");
	        	sbTemp.append(iVERSESN);
	        	sbTemp.append("&nbsp;");
	        	sbTemp.append(strLection);
	        	sbTemp.append("</font>");
	        	
	        	strsResult[i] = sbTemp.toString();
	        	
	        	i++;
	        }
		}
		return strsResult;
	}


	public void onButtonBackClick(View v) {  
		this.finish();
	}
		
	
	 //Ϊѡ�����listviewʹ�õ�������
		
	private class MyAdapterVolume extends BaseAdapter 
	{        
	    //���������
	    private String _mStrsLection[] = null;


		public MyAdapterVolume(Context context, String StrsLection[]) 
		{
			mContext = context;
			_mStrsLection = StrsLection;
		}

		 @Override 
        public int getCount()
        {      
			 if(_mStrsLection == null)
			 {
				 return 0;
			 }
			 else
			 {
				 return _mStrsLection.length;				 
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
        public View getView(int position, View convertView, ViewGroup parent) 
        { 
            // position����λ�ô�0��ʼ��convertView��Spinner,ListView��ÿһ��Ҫ��ʾ��view 
            // ͨ��return ��viewҲ����convertView 
            // parent���Ǹ������ˣ�Ҳ����Spinner,ListView,GridView��.

            if (convertView == null) 
            {
            	convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_item_lection, null);
            }
            
          	if(_mStrsLection != null)
          	{
	            TextView mTextView1 = (TextView) convertView.findViewById(R.id.ItemViewBibleContent);
	            mTextView1.setText(Html.fromHtml(_mStrsLection[position]));
	            mTextView1.setTextSize(_config.GetLectionFontSize());
          	}
          	
			return convertView;

        } 
        private Context mContext;
	 }
		
		
	
}

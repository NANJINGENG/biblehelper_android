package cn.elijah.biblehelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import cn.elijah.biblehelper.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


public class SelectVolumeAndChapter extends Activity {

	
    private ViewPager mPager;//ҳ������
    private List<View> mViewsList; // Tabҳ���б�

    private TextView t1, t2;// ҳ��ͷ��
    
    private LinearLayout mBigTab1,mBigTab2;
    

    //��Լ������ListView�ؼ�
    private ListView mListViewVolumeName_OT;
    
    //��Լ������ListView�ؼ�
    private ListView mListViewVolumeName_NT;
    

	//��дApplication����Ϊȫ�����������ļ�
	private Config _config;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.select_volume_and_chapter);
	       
	       //����Զ����Ӧ�ó���Config
	       _config = (Config)getApplication(); 
	       
	       //����ʵ��
	       _config.SetInstanceSelectVolumeAndChapter(this);
	       
	       
	       //��ͷ
	       InitTextView();
	       //��Լ����Լ�л�ҳ
	       InitViewPager();
	       
			//��Լ���������
			final String mStrsVolume_OT[] = new String[39];
			//��Լ����������
			final int mIntsChapterCount_OT[] = new int[39];
			GetVolumeNameArray(0,mStrsVolume_OT,mIntsChapterCount_OT); 
	       
			//��Լ���������
			final String mStrsVolume_NT[] = new String[27];
			//��Լ����������
			final int mIntsChapterCount_NT[] = new int[27];
			GetVolumeNameArray(1,mStrsVolume_NT,mIntsChapterCount_NT); 
	       
	       //Ϊ��Լlistview�������
	       mListViewVolumeName_OT = (ListView)mViewsList.get(0).findViewById(R.id.listViewVolume) ;
	       mListViewVolumeName_OT.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) 
			{
				
				//ʵ����AlertDialog����������ʾ������
				//ĳһ��������������������ѡ��
				String strsChapterIndex[] = new String[mIntsChapterCount_OT[position]]; 
				
				//������� ��������
				FillArrayChapterIndex(strsChapterIndex,strsChapterIndex.length);

				
				 //���þ�ID
	            _config.setVolumeID(position+1);

	            
				//��ʾ�Ի���˵�
				showListDialog(strsChapterIndex,mStrsVolume_OT[position]);

			}
           });

	       mListViewVolumeName_NT = (ListView)mViewsList.get(1).findViewById(R.id.listViewVolume) ;
	       mListViewVolumeName_NT.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) 
			{

				//ʵ����AlertDialog����������ʾ������
				//ĳһ��������������������ѡ��
				String strsChapterIndex[] = new String[mIntsChapterCount_NT[position]];
				//������� ��������
				FillArrayChapterIndex(strsChapterIndex,strsChapterIndex.length);
				
				 //���þ�ID
	            _config.setVolumeID(40 + position);
	            
				//��ʾ�Ի���˵�
				showListDialog(strsChapterIndex,mStrsVolume_NT[position]);
			}
           });
	       

	       //��ԼListView
	       MyAdapterVolume adapter_ot = new MyAdapterVolume(SelectVolumeAndChapter.this,mStrsVolume_OT,mIntsChapterCount_OT);
	       mListViewVolumeName_OT.setAdapter(adapter_ot);

	       //��ԼListView
	       MyAdapterVolume adapter_nt = new MyAdapterVolume(SelectVolumeAndChapter.this,mStrsVolume_NT,mIntsChapterCount_NT);
           mListViewVolumeName_NT.setAdapter(adapter_nt);

	       //������ʾ��Լҳ������tabҳ����ʽ
	       mPager.setCurrentItem(1);
	       mPager.setCurrentItem(0);

    }
    
    //������� ����
    private void FillArrayChapterIndex(String[] strs,int length)
    {
		//������� ����
		for(int i=0;i<length;i++)
		{
			StringBuilder sb = new StringBuilder();
			sb.append("�� ");
			sb.append(i + 1);
			sb.append(" ��");
			
			strs[i] = sb.toString();

			sb.delete(0, sb.length());
			sb = null;
		}

    }
    

    private void showListDialog(final String[] items, final String strVolumeName)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("��ת�� " + strVolumeName);
        /**
         * 1��public Builder setItems(int itemsId, final OnClickListener
         * listener) itemsId��ʾ�ַ����������ԴID������Դָ�����������ʾ���б��С� 2��public Builder
         * setItems(CharSequence[] items, final OnClickListener listener)
         * items��ʾ������ʾ���б��е��ַ�������
         */
        builder.setItems(items, new DialogInterface.OnClickListener()
        {
        	@Override
        	public void onClick(final DialogInterface dialog, int which)
        	{
        		//��侭�ĵ�listview
        		_config.setChapterID(which + 1);
        		_config.GetInstanceMain_Tab_Readbible().FillListView();

        		
        		//������ڲ��ţ����Բ������½�
        		if(_config.GetVoicePanelActivity().mediaPlayer != null && 
        				_config.GetVoicePanelActivity().mediaPlayer.isPlaying())
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

	            //�ر�ѡ���µĴ���
    	       if(_config.GetInstanceSelectVolumeAndChapter() != null)
    	       {
    	    	   _config.GetInstanceSelectVolumeAndChapter().finish();   
    	       }

    	       
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
    
	
    public int GetVolumeNameArray(int OldOrNew, String[] outStrsResult, int[] outIntsResult)
    {
    	//�������ݸ���
    	int iResult = 0;
    	//�������
    	Arrays.fill(outStrsResult, "");
    	Arrays.fill(outIntsResult, 0);
    	
        //����DatabaseHelper����  
        DatabaseHelper dbHelper = new DatabaseHelper(SelectVolumeAndChapter.this, _config.GetStrDatabaseNameForDeployment());
        // �õ�һ��SQLiteDatabase����  
        SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();  

        String strSQL = "select [SN], [ChapterNumber], [NewOrOld], [FullName]  from [BibleID] where  [NewOrOld]=" + OldOrNew + " order by [SN] asc ";
        Cursor cursor = sqliteDatabase.rawQuery(strSQL,null);
        // ������ƶ�����һ�У��Ӷ��жϸý�����Ƿ�����һ�����ݣ�������򷵻�true��û���򷵻�false  
        while (cursor.moveToNext())
        {
        	//���ȫ��
        	outStrsResult[iResult] = cursor.getString(cursor.getColumnIndex("FullName"));
        	//������������
        	outIntsResult[iResult] = cursor.getInt(cursor.getColumnIndex("ChapterNumber"));
            iResult++;
        }

        return iResult;
    }
	


    //Ϊѡ�����listviewʹ�õ�������
	private class MyAdapterVolume extends BaseAdapter 
	{        
	    //���������
	    private String _mStrsVolume[] = null;
	    //����������
	    private int _mIntsChapterCount[] = null;
	    

		 public MyAdapterVolume(Context context, String mStrsVolume[], int mIntsChapterCount[]) 
		 {
			 mContext = context;

			 _mStrsVolume = mStrsVolume;
			 _mIntsChapterCount = mIntsChapterCount;

		 }
		 
		 @Override 
        public int getCount()
        {      
			 return _mStrsVolume.length;
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
            	convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_item_volume, null);
            }

            //��Լ�������������
            TextView mTextView1 = (TextView) convertView.findViewById(R.id.ItemViewVolumeName);
			mTextView1.setTextColor(Color.BLACK);
			mTextView1.setText(_mStrsVolume[position]);

			//��Լ������а���������
			TextView mTextView2 = (TextView) convertView.findViewById(R.id.ItemViewChapterCount);
			mTextView2.setTextColor(Color.GRAY);

			StringBuilder sb = new StringBuilder();
			sb.append("��");
			sb.append(_mIntsChapterCount[position]);
			sb.append("��");
			
			mTextView2.setText(sb.toString());
			
			sb.delete(0, sb.length());
			sb = null;
			return convertView;

        } 
        private Context mContext;
	 }

	/*
     * ��ʼ��ͷ��
     */
    private void InitTextView() {
        t1 = (TextView) findViewById(R.id.tv1);
        t2 = (TextView) findViewById(R.id.tv2);

        t1.setOnClickListener(new MyOnClickListener(0));
        t2.setOnClickListener(new MyOnClickListener(1));

        //���ֻ�ǵײ�4��ͼƬ��ť��������Ѳ�����Ϊ��ʹ�����Χ���󣬽��ⲿ�Ĳ���Ҳ������click�¼�
        mBigTab1 = (LinearLayout) findViewById(R.id.ll_tab1);
        mBigTab2 = (LinearLayout) findViewById(R.id.ll_tab2);
        
        mBigTab1.setOnClickListener(new MyOnClickListener(0));
        mBigTab2.setOnClickListener(new MyOnClickListener(1));        
    }

	public void onButtonBackClick(View v) {  

		this.finish();
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
            mPager.setCurrentItem(index);
        }
    };
	
    /**
     * ��ʼ��ViewPager
*/
    private void InitViewPager() {
        mPager = (ViewPager) findViewById(R.id.tabpager);
        mViewsList = new ArrayList<View>();
        LayoutInflater mInflater = getLayoutInflater();
        mViewsList.add(mInflater.inflate(R.layout.select_tab_listview, null));
        mViewsList.add(mInflater.inflate(R.layout.select_tab_listview, null));

        PagerAdapter MyPagerAdapter = new PagerAdapter() {
			
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}
			
			@Override
			public int getCount() {
				return mViewsList.size();
			}

			@Override
			public void destroyItem(View container, int position, Object object) {
				((ViewPager)container).removeView(mViewsList.get(position));
			}

			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager)container).addView(mViewsList.get(position));
				return mViewsList.get(position);
			}
		};
		
        mPager.setAdapter(MyPagerAdapter);
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());
        
    }
    
    
    /**
     * ҳ���л�����
     */
    public class MyOnPageChangeListener implements OnPageChangeListener {
        @Override
        public void onPageSelected(int arg0) {
           
            //�л�����һҳ
            if(arg0 == 0)
            {
            	t1.setTextColor(SelectVolumeAndChapter.this.getResources().getColor(android.R.color.white));
            	t2.setTextColor(SelectVolumeAndChapter.this.getResources().getColor(android.R.color.black));
            	
            	mBigTab1.setBackgroundColor(SelectVolumeAndChapter.this.getResources().getColor(android.R.color.tab_indicator_text));
            	mBigTab2.setBackgroundColor(SelectVolumeAndChapter.this.getResources().getColor(android.R.color.darker_gray));
            }
            else
            {
            	//�л����ڶ�ҳ
            	t1.setTextColor(SelectVolumeAndChapter.this.getResources().getColor(android.R.color.black));
            	t2.setTextColor(SelectVolumeAndChapter.this.getResources().getColor(android.R.color.white));

            	mBigTab1.setBackgroundColor(SelectVolumeAndChapter.this.getResources().getColor(android.R.color.darker_gray));
            	mBigTab2.setBackgroundColor(SelectVolumeAndChapter.this.getResources().getColor(android.R.color.tab_indicator_text));
            }
            
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }
    
    //�˳�����-----------------------
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {

	    return super.dispatchKeyEvent(event);
	}
    
}

    
	


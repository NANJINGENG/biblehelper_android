package cn.elijah.biblehelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import android.annotation.SuppressLint;
import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.widget.ListView;

@SuppressLint("SdCardPath")
public class Config extends Application {

	//�洢�����������
	private String _strsVolumeName[] = null;

	 //ʥ����������
	 private String _strsBibleContent[] = null;

	 //��Ҫ��ʾ��С����
	 private int _iVerseCount = 0;
	 //��ID
	 private int _iVolumeID = 1;
	 //��ID
	 private int _iChapterID = 1;
	 //��ID������н�ID����λ�������
	 private int _iVerseID = 1;
	 //��ʾ�ı���
	 private String _strVolumeName = "";

	 //����ʹ�õ����ݿ������
	 private String _DatabaseNameForDeployment="";

	 //���ݿ����ֻ����·��
	 private String _DatabasePathForDeployment="";
	    
	 //��ʾ���ĵ�ListView
	 private ListView _ListViewBibleContent;
		
	 //��洰��ʵ��
	 private Main_Tab_AD _instanceMain_Tab_AD = null;
	 
	 //Main_Tab_Readbible����ʵ��
	 private Main_Tab_Readbible _instanceMain_Tab_Readbible = null;
	 
	 //SelectVolumeAndChapter����ʵ��
	 private SelectVolumeAndChapter _instanceSelectVolumeAndChapter = null;
	 
	 private MainActivity _MainActivity = null;
	 
	 //ʥ������MP3�ļ�·��
	 private String _InternetMP3FolderPath = "";
	 
	 //�Ƿ������Զ�������Ƶ�͸��
	 private boolean _EnableAutoDownloadVoiceFile = false;
	 
	 //�����͸���ļ��洢��Ŀ¼��ǰ�滹����SDCARD��������Ŀ¼
	 private String _VoiceFileStorageFolder = "";

	 
	 //�������ʵ��
	 private VoicePanelActivity _VoicePanelActivity = null;
	 
	 
	 //������ �Ƿ�����һ��
	 private boolean _IsHasNextChapter;
	 
	 //�������Ƿ�����һ��
	 private boolean _IsHasPrevChapter;
	 
	 //�Ƿ���������
	 private boolean _IsDownloading;
	 
	 //��ʷ��¼������
	 private Main_Tab_History _Main_Tab_History = null;
	 
	 
	 //�����ֺŴ�С
	 private int _LectionFontSize;
	 
	 //����Ķ����ID����ID
	 private String _recentlyReadingVolumeIDAndChapterID;
	 
	 
	 //ϵͳ����������
	 private Main_Tab_Config _Main_Tab_Config = null;
	 
	 //��ǩ����
	 private BookmarkActivity _BookmarkActivity = null;
	 
	 public BookmarkActivity GetBookmarkActivity()
	 {
		 return this._BookmarkActivity;
	 }
	 
	 public void SetBookmarkActivity(BookmarkActivity obj)
	 {
		 this._BookmarkActivity = obj;
	 }
	 
	 public Main_Tab_Config GetMain_Tab_Config()
	 {
		 return this._Main_Tab_Config;
	 }
	 
	 public void SetMain_Tab_Config(Main_Tab_Config obj)
	 {
		 this._Main_Tab_Config = obj;
	 }
	 

	    @Override
	    public void onCreate() {
	        
	        super.onCreate();
	        
	        //��ʼ��ȫ�ֱ���
	        //���ݿ�Ŀ¼
	        SetStrDatabasePathForDeployment("/data/data/cn.elijah.biblehelper/databases/");
	        //���ݿ�����
	        SetStrDatabaseNameForDeployment("bible_unv.db");

	        //������ݿ��Ƿ����
	        boolean dbExist = checkDataBase();
	        if(dbExist)
	        {
	        }
	        else
	        {
	     	   //�����ھͰ�raw������ݿ�д���ֻ�
	            try{
	                copyDataBase();
	            }catch(IOException e){
	                throw new Error("Error copying database");
	            }
	        }

	        //------------------------------------------------
	        
	        //�����176�ڣ����ﶨ��176���ȵ����飬����ʹ��
	        setStrsBibleContent(new String[176]);
	        
	        //С�� ��
	        setVerseCount(0);
	        
	        SetInternetMP3FolderPath("http://biblevoice.oss.aliyuncs.com/cuv");
	        
	        SetVoiceFileStorageFolder("BibleHelper");
	        
	        //�������أ���
	        SetIsDownloading(false);


	        //�����ݿ��ж�ȡ�������ݣ����õ�ȫ�ֱ�����
	        ReadConfigFromDatabase();
	        

	      //------------------------------------------------
	        //�洢������֣�Ϊ GetStrVolumeName() ����
	        _strsVolumeName = new String[66];

	        //���ݿ�ʵ��
			DatabaseHelper dbHelper = new DatabaseHelper(Config.this, this.GetStrDatabaseNameForDeployment());

			// �õ�һ��SQLiteDatabase����  
	        SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();  

	        String strSQL = "select [FullName]   from [BibleID]  order by [SN]";

	        Cursor cursor = sqliteDatabase.rawQuery(strSQL, null);
	        

	        int i=0;
	        while(cursor.moveToNext())
	        {
	        	_strsVolumeName[i] = cursor.getString(cursor.getColumnIndex("FullName"));
	        	i++;
	        }
	        
	        
	        //------------------------------------------------
	        //���Խ������͵�һ�µ�mp3�ļ�����������Ŀ¼��
	        try{
	        	copyMP3File();
	        }catch(IOException e){
	        	throw new Error("Error copying mp3 file");
	        }
	    }
	    
	    

	    //�����ݿ��ȡ����
	    public void ReadConfigFromDatabase()
	    {

	    	//���ݿ�ʵ��
			DatabaseHelper dbHelper = new DatabaseHelper(Config.this, this.GetStrDatabaseNameForDeployment());

			// �õ�һ��SQLiteDatabase����  
	        SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();  

	        String sql = "select [isBigFont], [isAutoDownfile], [selectBible]  from [settings]";

	        Cursor cursor = sqliteDatabase.rawQuery(sql, null);
	        if(cursor.moveToNext())
	        {

	        	// isBigFont=�Ƿ���ʾ���ֺţ�0=�����ֺ� 1=���ֺ� ��

	        	if(cursor.getInt(cursor.getColumnIndex("isBigFont")) == 1)
	        	{
	            	//�ֺţ���
	        		this.SetLectionFontSize(40);
	        	}
	        	else
	        	{
	        		//�ֺţ���ͨ
	        		this.SetLectionFontSize(20);
	        	}
	        	
	        	
	        	//isAutoDownFile int=�Ƿ��Զ������ļ���0=���Զ����� 1=�Զ����أ�
	        	if(cursor.getInt(cursor.getColumnIndex("isAutoDownfile")) == 1)
	        	{
	                this.SetEnableAutoDownloadVoiceFile(true);
	        	}
	        	else
	        	{
	        		//��ֹ����
	        		this.SetEnableAutoDownloadVoiceFile(false);
	        	}
	        	
	        	//selectBible=���ѡ��ľ�-�£�1-2��1������ID��2�����µ�ID��-����ָ�
	        	this.SetRecentlyReadingVolumeIDAndChapterID(cursor.getString(cursor.getColumnIndex("selectBible")));
	        	if(!this.GetRecentlyReadingVolumeIDAndChapterID().equalsIgnoreCase(""))
	        	{
	        		String strVolumeID = this.GetRecentlyReadingVolumeIDAndChapterID().substring(0,this.GetRecentlyReadingVolumeIDAndChapterID().indexOf('-'));
	        		String strChapterID = this.GetRecentlyReadingVolumeIDAndChapterID().substring(this.GetRecentlyReadingVolumeIDAndChapterID().indexOf('-') + 1);
	        		
	                setVolumeID(Integer.parseInt(strVolumeID));
	                setChapterID(Integer.parseInt(strChapterID));
	                
	                setVerseID(1);
	        	}


	        }
	    	
	    	
	    }
	    
	    
	    //д�����õ����ݿ�
	    public void WriteConfigToDatabase()
	    {
	    	// isBigFont=�Ƿ� ���ֺ�
	    	//��0=��ͨ��1=�� ��
	    	int isBigFont = 0;
	    	if(this.GetLectionFontSize() == 20)
	    	{
	    		//�ֺţ���ͨ
	    		isBigFont = 0;
			}
			else
			{
	    		//�ֺţ���
				isBigFont = 1;
			}

	    	//isAutoDownFile int=�Ƿ��Զ������ļ���0=���Զ����� 1=�Զ����أ�
	    	int isAutoDownFile = 0;
		   	if(this.GetEnableAutoDownloadVoiceFile())
		   	{
		   		isAutoDownFile = 1;
		   	}
		   	else
		   	{
		   		//��ֹ�Զ�����
		   		isAutoDownFile = 0;
		   	}


			//���ݿ�ʵ��
			DatabaseHelper dbHelper = new DatabaseHelper(Config.this, this.GetStrDatabaseNameForDeployment());

			// �õ�һ��SQLiteDatabase����  
			SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();
			String sql = "update [settings] set [isBigFont]="+isBigFont + ", " +
	       		"[isAutoDownFile]=" + isAutoDownFile + "  where [SN]=0";

			sqliteDatabase.execSQL(sql);

	   }
	    
	 
	 
	 public String GetRecentlyReadingVolumeIDAndChapterID()
	 {
		 return this._recentlyReadingVolumeIDAndChapterID;
	 }
	 
	 public void SetRecentlyReadingVolumeIDAndChapterID(String str)
	 {
		 this._recentlyReadingVolumeIDAndChapterID = str;
	 }
	 
	 public int GetLectionFontSize()
	 {
		 return this._LectionFontSize;
	 }
	 
	 public void SetLectionFontSize(int size)
	 {
		 this._LectionFontSize = size;
	 }
	 
	 public Main_Tab_History GetMain_Tab_History()
	 {
		 return this._Main_Tab_History;
	 }
	 
	 public void SetMain_Tab_History(Main_Tab_History obj)
	 {
		 this._Main_Tab_History = obj;
	 }
	 
	 
	 public MainActivity GetMainActivity()
	 {
		 return this._MainActivity;
	 }
	 
	 public void SetMainActivity(MainActivity obj)
	 {
		 this._MainActivity = obj;
	 }
	 
	 public boolean GetIsDownloading()
	 {
		 return this._IsDownloading;
	 }
	 
	 public void SetIsDownloading(boolean b)
	 {
		 this._IsDownloading = b;
	 }
	 
	 public boolean GetIsHasPrevChapter()
	 {
		 return this._IsHasPrevChapter;
	 }
	 
	 public void SetIsHasPrevChapter(boolean bIsHas)
	 {
		 this._IsHasPrevChapter = bIsHas;
	 }
	 
	 public boolean GetIsHasNextChapter()
	 {
		 return this._IsHasNextChapter;
	 }
	 
	 public void SetIsHasNextChapter(boolean bIsHas)
	 {
		 this._IsHasNextChapter = bIsHas;
	 }
	 
	 public VoicePanelActivity GetVoicePanelActivity()
	 {
		 return this._VoicePanelActivity;
	 }
	 
	 public void SetVoicePanelActivity(VoicePanelActivity obj)
	 {
		 this._VoicePanelActivity = obj;		 
	 }
	 
	 public String GetVoiceFileStorageFolder()
	 {
		 return this._VoiceFileStorageFolder;
	 }
	 
	 public void SetVoiceFileStorageFolder(String str)
	 {
		 this._VoiceFileStorageFolder = str;
	 }
	 
	 public boolean GetEnableAutoDownloadVoiceFile()
	 {
		 return this._EnableAutoDownloadVoiceFile;
	 }
	 
	 public void SetEnableAutoDownloadVoiceFile(boolean b)
	 {
		 this._EnableAutoDownloadVoiceFile = b;
	 }
	 
	 public String GetInternetMP3FolderPath()
	 {
		 return this._InternetMP3FolderPath;
	 }
	 
	 public void SetInternetMP3FolderPath(String str)
	 {
		 this._InternetMP3FolderPath = str;
	 }

	 
	 public SelectVolumeAndChapter GetInstanceSelectVolumeAndChapter()
	 {
		 return this._instanceSelectVolumeAndChapter;
	 }
	 
	 public void SetInstanceSelectVolumeAndChapter(SelectVolumeAndChapter instance)
	 {
		 this._instanceSelectVolumeAndChapter = instance;
	 }
	 
	 public void SetInstanceMain_Tab_AD(Main_Tab_AD instance)
	 {
		 _instanceMain_Tab_AD = instance;
	 }
	 
	 public Main_Tab_AD GetInstanceMain_Tab_AD()
	 {
		 return this._instanceMain_Tab_AD;
	 }
	 
	 public Main_Tab_Readbible GetInstanceMain_Tab_Readbible()
	 {
		 return this._instanceMain_Tab_Readbible;
	 }
	 
	 public void SetInstanceMain_Tab_Readbible(Main_Tab_Readbible instance)
	 {
		 this._instanceMain_Tab_Readbible = instance;
	 }
	 
	 public ListView GetListViewBibleContent()
	 {
		 return this._ListViewBibleContent;
	 }
	 
	 public void SetListViewBibleContent(ListView listview)
	 {
		 this._ListViewBibleContent = listview;
	 }


	public String GetStrDatabasePathForDeployment()
	{ 
		return this._DatabasePathForDeployment;
	}

	public void SetStrDatabasePathForDeployment(String str)
	{
		this._DatabasePathForDeployment = str;
	}
	 
    public String GetStrDatabaseNameForDeployment()
    {
    	return this._DatabaseNameForDeployment;
    }
    
    public void SetStrDatabaseNameForDeployment(String str)
    {
    	this._DatabaseNameForDeployment = str;
    }

    public String[] getStrsBibleContent(){
        return _strsBibleContent;
    }
    
    public void setStrsBibleContent(String[] strs){
        this._strsBibleContent = strs;
    }

    public int getVerseCount()
    {
    	return _iVerseCount;
    }
    
    public void setVerseCount(int i)
    {
    	this._iVerseCount = i;
    }
    
    public int getVolumeID()
    {
    	return _iVolumeID;
    }
    
    public void setVolumeID(int i)
    {
    	this._iVolumeID = i;
    }
    
    
    public int getChapterID()
    {
    	return _iChapterID;
    }
    
    public void setChapterID(int i)
    {
    	this._iChapterID = i;
    }
    
    public int getVerseID()
    {
    	return _iVerseID;
    }
    
    public void setVerseID(int i)
    {
    	this._iVerseID = i;
    }
    
    public String GetStrVolumeName()
    {
    	//���û�����������������ID�������ݿ��л�ȡ
    	if(this._strVolumeName.length()<=0)
    	{
    		this._strVolumeName = this.GetVolumeNameByID(this.getVolumeID());
    	}
    	
    	return this._strVolumeName;
    }
    
    public void SetStrVolumeName(String str)
    {
    	this._strVolumeName = str;
    }


    //�����ID�õ��������
    public String GetVolumeNameByID(int volumeID)
    {
    	return _strsVolumeName[volumeID-1];
    }
    
    
    //������Ķ�������д�����ݿ�
    public void WriteRecentlyReadingToDatabase()
    {
    	//selectBible=���ѡ��ľ�-�£�1-2��1������ID��2�����µ�ID��-����ָ�
    	String selectBible = this.getVolumeID() + "-" + this.getChapterID();
    	//���ݿ�ʵ��
		DatabaseHelper dbHelper = new DatabaseHelper(Config.this, this.GetStrDatabaseNameForDeployment());
	
		// �õ�һ��SQLiteDatabase����  
		SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();
		String sql = "update [settings] set  [selectBible]='" + selectBible + "'  where [SN]=0";
	
		sqliteDatabase.execSQL(sql);
    	
    }
    
    
  //------------------------------------------------
	
    /**
     * �ж����ݿ��Ƿ����
     * @return false or true
     */
    public boolean checkDataBase(){
    	SQLiteDatabase checkDB = null;
    	try{
    		String databaseFilename = this.GetStrDatabasePathForDeployment() + this.GetStrDatabaseNameForDeployment();
    		checkDB =SQLiteDatabase.openDatabase(databaseFilename, null,
    				SQLiteDatabase.OPEN_READONLY);
    	}catch(SQLiteException e){
    		
    	}
    	if(checkDB!=null){
    		checkDB.close();
    	}
    	return checkDB !=null?true:false;
    }
    /**
     * �������ݿ⵽�ֻ�ָ���ļ�����
     * @throws IOException
     */
    public void copyDataBase() throws IOException{
    	String databaseFilenames =this.GetStrDatabasePathForDeployment()+this.GetStrDatabaseNameForDeployment();
    	File dir = new File(this.GetStrDatabasePathForDeployment());
    	if(!dir.exists())//�ж��ļ����Ƿ���ڣ������ھ��½�һ��
    		dir.mkdir();
    	FileOutputStream os = null;
    	try{
    		os = new FileOutputStream(databaseFilenames);//�õ����ݿ��ļ���д����
    	}catch(FileNotFoundException e){
    		e.printStackTrace();
    	}
    	InputStream is = Config.this.getResources().openRawResource(R.raw.bible_unv);//�õ����ݿ��ļ���������
        byte[] buffer = new byte[8192];
        int count = 0;
        try{
        	while((count=is.read(buffer))>0){
        		os.write(buffer, 0, count);
        		os.flush();
        	}
        }catch(IOException e){
        	
        }
        try{
        	is.close();
        	os.close();
        }catch(IOException e){
        	e.printStackTrace();
        }
    }

    
    //����MP3�ļ�
    public void copyMP3File() throws IOException{
    	
    	//01-1.mp3
    	int originalFileID = R.raw.voice_file;
    	
    	//����mp3��Ŀ��Ŀ¼
    	String destinationFilePath = FileUtil.setMkdir(Config.this, GetVoiceFileStorageFolder()) + File.separator + "01-1.mp3";
    	
    	//�ж��Ƿ����
    	File fileObj = new File(destinationFilePath);
    	if(!fileObj.exists())
    	{
	    	FileOutputStream os = null;
	    	try{
	    		os = new FileOutputStream(destinationFilePath);//�õ�MP3�ļ���д����
	    	}catch(FileNotFoundException e){
	    		e.printStackTrace();
	    	}
	    	InputStream is = Config.this.getResources().openRawResource(originalFileID);//�õ����ݿ��ļ���������
	        byte[] buffer = new byte[8192];
	        int count = 0;
	        try{
	        	while((count=is.read(buffer))>0){
	        		os.write(buffer, 0, count);
	        		os.flush();
	        	}
	        }catch(IOException e){
	        	
	        }
	        try{
	        	is.close();
	        	os.close();
	        }catch(IOException e){
	        	e.printStackTrace();
	        }
    	}
    }
    
}

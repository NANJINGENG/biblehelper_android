package cn.elijah.biblehelper;

import java.io.File;

import android.content.Context;
import android.os.Environment;


public class FileUtil {

	
	//����Ƿ�װ��sd��
	public static boolean checkSDCard()
	{
		if(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
		{
			return true;
		}else{
			return false;
		}
	}

	//����Ŀ¼
	public static String setMkdir(Context context, String strDownloadFolder)
	{
		String filePath;
		if(checkSDCard())
		{
			filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+strDownloadFolder;
		}else{
			filePath = context.getCacheDir().getAbsolutePath()+File.separator+strDownloadFolder;
		}
		File file = new File(filePath);
		if(!file.exists())
		{
			file.mkdirs();
		}else{
		}
		return filePath;
	}
}

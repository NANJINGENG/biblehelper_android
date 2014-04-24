package cn.elijah.biblehelper;

import java.io.File;

import android.content.Context;
import android.os.Environment;


public class FileUtil {

	
	//检查是否安装了sd卡
	public static boolean checkSDCard()
	{
		if(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
		{
			return true;
		}else{
			return false;
		}
	}

	//创建目录
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

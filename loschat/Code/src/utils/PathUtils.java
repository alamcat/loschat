package utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PathUtils {
	public static String getDataPath(String prefix) {
		SimpleDateFormat sdf = new SimpleDateFormat("/yyyy/MM/dd/");
		String datePath = sdf.format(new Date());
		String wholePath = prefix + datePath;
		File file = new File(wholePath);
		if(!file.exists()) {
			file.mkdirs();
		}
		return datePath; 
	}

	public static String getReceivedData() {
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyPattern("HH:mm:ss");
		Date date = new Date();
		return sdf.format(date);
	}
}

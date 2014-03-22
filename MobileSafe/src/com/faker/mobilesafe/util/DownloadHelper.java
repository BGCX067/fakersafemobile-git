package com.faker.mobilesafe.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.ProgressDialog;

/**
 * 单线程下载工具类
 * 
 * @author Administrator
 * 
 */
public class DownloadHelper {

	/**
	 * 通过url地址，下载文件并返回该文件的File对象
	 * 
	 * @param url
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public static File getServerFile(String url,String dirPath,ProgressDialog pd) throws Exception {
		String fileName = url.substring(url.lastIndexOf("/") + 1, url.length());
		URL apkUrl = new URL(url);
		File dir = new File(dirPath);
		dir.mkdirs();
		File file = null;
		if (dir.exists()) {
			file = new File(dir, fileName);
			file.createNewFile();
			HttpURLConnection conn = (HttpURLConnection) apkUrl
					.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5000);
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				InputStream is = conn.getInputStream();
				pd.setMax(conn.getContentLength());
				FileOutputStream os = new FileOutputStream(file);
				int len = -1;
				int pos = 0;
				byte[] buffer = new byte[1024];
				while ((len = is.read(buffer)) != -1) {
					os.write(buffer, 0, len);
					pos += len;
					pd.setProgress(pos);
					Thread.sleep(30);
				}
				os.flush();
				os.close();
				is.close();
			}
		}

		return file;
	}

}

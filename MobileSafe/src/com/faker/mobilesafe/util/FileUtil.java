package com.faker.mobilesafe.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;

public class FileUtil {

	/**
	 * 合并文件
	 * 
	 * @param c
	 * @param partFileList
	 *            小文件名集合
	 * @param dst
	 *            目标文件路径
	 * @throws IOException
	 * 
	 */
	public static void mergeFile(Context c, String[] partFileList, File dir,
			String filename) throws IOException {
		if (dir.exists()) {
			File dst = new File(dir, filename);
			dst.createNewFile();
			OutputStream out = new FileOutputStream(dst);
			byte[] buffer = new byte[1024];
			InputStream in;
			int readLen = 0;
			for (int i = 0; i < partFileList.length - 3; i++) {
				// 获得输入流 ,注意文件的路径
				in = c.getAssets().open(partFileList[i]);
				while ((readLen = in.read(buffer)) != -1) {
					out.write(buffer, 0, readLen);
				}
				out.flush();
				in.close();
			}
			// 把所有小文件都进行写操作后才关闭输出流，这样就会合并为一个文件了
			out.close();
		}
	}

}

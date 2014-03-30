package com.faker.mobilesafe.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import android.content.Context;
import android.util.Log;

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
			Log.i("lichfaker", partFileList.length + "");
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

	@SuppressWarnings("unchecked")
	public static void mergeZipFile(Context c, String[] partFileList, File dir,
			File unzipDir, String filename) throws IOException {
		if (dir.exists()) {
			File dst = new File(dir, filename + ".zip");
			dst.createNewFile();
			OutputStream out = new FileOutputStream(dst);
			byte[] buffer = new byte[1024];
			InputStream in;
			int readLen = 0;
			Log.i("lichfaker", partFileList.length + "");
			for (int i = 0; i < partFileList.length; i++) {
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

			// 解压
			ZipFile zipFile = new ZipFile(dst); // 创建zip文件对象
			// 得到zip文件条目枚举对象
			Enumeration<ZipEntry> zipEnumeration = (Enumeration<ZipEntry>) zipFile
					.entries();
			while (zipEnumeration.hasMoreElements()) {
				// 得到当前条目
				ZipEntry entry = zipEnumeration.nextElement();
				String entryName = new String(entry.getName().getBytes(
						"ISO8859_1"));
				// 若当前条目为目录则创建
				if (entry.isDirectory()) {
					new File(unzipDir, entryName).mkdir();
				} else {
					in = zipFile.getInputStream(entry);
					out = new FileOutputStream(new File(unzipDir, entryName));
					byte[] buffer1 = new byte[1024];
					int len = -1;
					while ((len = in.read(buffer1)) != -1) {
						out.write(buffer1, 0, len);
					}
					in.close();
					out.flush();
					out.close();
				}
			}
			// 删除zip文件
			dst.delete();
		}
	}

	public static void moveFile(InputStream is, File dirTo, String filename)
			throws Exception {
		File outFile = new File(dirTo, filename);
		if (!outFile.exists()) {
			FileOutputStream fos = new FileOutputStream(outFile);
			byte[] buffer = new byte[1024];
			int len = -1;
			while ((len = is.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
			}
			is.close();
			fos.flush();
			fos.close();
		}
	}
}

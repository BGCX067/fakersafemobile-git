package com.faker.mobilesafe.util;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import com.faker.mobilesafe.bean.DownloadInfo;
import com.faker.mobilesafe.dao.DownloadDao;

import android.content.Context;

/**
 * 多线程下载器
 * 
 * @author Administrator
 * 
 */
public class MulDownloadHelper {

	// 下载的线程数
	private final static int threadsize = 6;
	private DownloadDao serivce;
	private Context context;

	private boolean isDownload = true;

	public boolean isDownload() {
		return isDownload;
	}

	public void setDownload(boolean isDownload) {
		this.isDownload = isDownload;
	}

	public MulDownloadHelper(Context context) {
		this.context = context;
		serivce = new DownloadDao(context);
	}

	public boolean download(String path, File dir, ProgressBarListener listener)
			throws Exception {
		// 使用http协议
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("GET");
		if (conn.getResponseCode() == 200) {
			int filesize = conn.getContentLength();

			// 设置进度条的最大值
			listener.getMax(filesize);

			// 生成一个和服务器大小一样的文件
			String fileName = path.substring(path.lastIndexOf("/") + 1, path.length());
			File file = new File(dir, fileName);
			file.createNewFile();
			RandomAccessFile acf = new RandomAccessFile(file, "rwd");
			acf.close();

			// 判断是否存在下载记录
			boolean isExist = serivce.isExist(path);
			if (isExist) {
				// 查询下载的总数据量
				int size = serivce.getDownloadSize(path);
				listener.getDownload(size, 1);
			} else {
				// 插入下载记录
				for (int i = 0; i < threadsize; i++) {
					serivce.save(new DownloadInfo(i, path, 0));
				}
			}

			// 每条线程的下载量
			int block = filesize % threadsize == 0 ? filesize / threadsize
					: filesize / threadsize + 1;

			// 开启线程进行下载操作
			for (int i = 0; i < threadsize; i++) {
				new DownloadThread(i, block, path, file, listener, context,
						this).start();
			}

			return true;
		} else {
			return false;
		}
	}


	private class DownloadThread extends Thread {

		// 下载线程的id
		private int threadid;
		// 每条线程的下载量
		private int block;
		// 文件下载的路径
		private String path;
		// 保存的文件
		private File file;
		// 下载的监听器
		private ProgressBarListener listener;

		// 下载的范围
		private int startposition;
		private int endposition;

		private DownloadDao service;
		private MulDownloadHelper manager;

		public DownloadThread(int threadid, int block, String path, File file,
				ProgressBarListener listener, Context context,
				MulDownloadHelper manager) {
			super();
			this.threadid = threadid;
			this.block = block;
			this.path = path;
			this.file = file;
			this.listener = listener;
			this.manager = manager;

			startposition = threadid * block;
			endposition = (threadid + 1) * block - 1;

			service = new DownloadDao(context);
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();

			try {

				// 查询线程已经下载的数据量
				int size = service.getDownloadSize(new DownloadInfo(threadid,
						path, 0));
				startposition = startposition + size;

				RandomAccessFile acf = new RandomAccessFile(file, "rwd");
				acf.seek(startposition);

				// 执行下载操作 http
				URL url = new URL(path);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setConnectTimeout(5000);
				conn.setRequestMethod("GET");
				// 给定下载范围
				conn.setRequestProperty("Range", "bytes=" + startposition + "-"
						+ endposition);

				// 不需要对请求码进行再次判断
				InputStream is = conn.getInputStream();
				byte[] buffer = new byte[1024];
				int len = 0;
				int count = size;
				DownloadInfo info = new DownloadInfo();
				info.setPath(path);
				info.setThreadid(threadid);
				while ((len = is.read(buffer)) != -1) {
					if (!manager.isDownload()) {
						return;
					}
					listener.getDownload(len, 0);
					// 写入数据
					acf.write(buffer, 0, len);
					count = count + len;
					info.setDownloadlength(count);
					service.update(info);
				}
				is.close();
				acf.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	public interface ProgressBarListener {

		// 得到文件的长度
		public void getMax(int size);

		// 每次下载的数据量
		public void getDownload(int size, int flag);// flag 如果是0 就表示是线程的下载更新 ，
													// 如果是1就表示 之前已经下载的更新
	}
}

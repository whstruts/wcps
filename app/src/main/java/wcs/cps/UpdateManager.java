package wcs.cps;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 *@author coolszy
 *@date 2012-4-26
 *@blog http://blog.92coding.com
 */

public class UpdateManager
{
	/* 涓嬭浇涓�*/
	private static final int DOWNLOAD = 1;
	/* 涓嬭浇缁撴潫 */
	private static final int DOWNLOAD_FINISH = 2;
	
	private static final int DOWNLOAD_INTERNET = 3;
	/* 淇濆瓨瑙ｆ瀽鐨刋ML淇℃伅 */
	HashMap<String, String> mHashMap;
	/* 涓嬭浇淇濆瓨璺緞 */
	private String mSavePath;
	/* 璁板綍杩涘害鏉℃暟閲�*/
	private int progress;
	/* 鏄惁鍙栨秷鏇存柊 */
	private boolean cancelUpdate = false;
	
	private boolean interCon = false;
	
	private int serviceCode ;
	private int versionCode;

	private Context mContext;
	/* 鏇存柊杩涘害鏉�*/
	private ProgressBar mProgress;
	private Dialog mDownloadDialog;

	private Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
			// 姝ｅ湪涓嬭浇
			case DOWNLOAD:
				// 璁剧疆杩涘害鏉′綅缃�
				mProgress.setProgress(progress);
				break;
			case DOWNLOAD_FINISH:
				// 瀹夎鏂囦欢
				installApk();
				break;
			case DOWNLOAD_INTERNET:
				// 瀹夎鏂囦欢
				if (serviceCode > versionCode)
				{
					showNoticeDialog();
				}else
				{
					Toast.makeText(mContext, R.string.soft_update_no, Toast.LENGTH_LONG).show();
				}
				break;
			default:
				break;
			}
		};
	};

	public UpdateManager(Context context)
	{
		this.mContext = context;
	}

	/**
	 * 妫�祴杞欢鏇存柊
	 */
	public void checkUpdate()
	{
		InterNetXml();
/*
		if (isUpdate())
		{
			// 鏄剧ず鎻愮ず瀵硅瘽妗�
			showNoticeDialog();
		} else
		{
			Toast.makeText(mContext, R.string.soft_update_no, Toast.LENGTH_LONG).show();
		}*/
	}

	/**
	 * 妫�煡杞欢鏄惁鏈夋洿鏂扮増鏈�
	 * 
	 * @return
	 */
	private boolean isUpdate()
	{/*
		// 鑾峰彇褰撳墠杞欢鐗堟湰
		versionCode = getVersionCode(mContext);
		// 鎶妚ersion.xml鏀惧埌缃戠粶涓婏紝鐒跺悗鑾峰彇鏂囦欢淇℃伅
		InputStream inStream = ParseXmlService.class.getClassLoader().getResourceAsStream("version.xml");
		// 瑙ｆ瀽XML鏂囦欢銆�鐢变簬XML鏂囦欢姣旇緝灏忥紝鍥犳浣跨敤DOM鏂瑰紡杩涜瑙ｆ瀽
		ParseXmlService service = new ParseXmlService();
		try
		{
			mHashMap = service.parseXml(inStream);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		if (null != mHashMap)
		{
			serviceCode = Integer.valueOf(mHashMap.get("version"));
			// 鐗堟湰鍒ゆ柇
			if (serviceCode > versionCode)
			{
				return true;
			}
		}*/
		
		/*
		versionCode = getVersionCode(mContext); //获取当前软件版本 
		URL url;//定义网络中version.xml的连接
		try { //一个测试
		url = new URL("http://"+ clsSocketCln.m_SvrIp +"/version.xml");//创建version.xml的连接地址。
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");  
		InputStream inStream = connection.getInputStream();//从输入流获取数据
		//InputStream inStream = ParseXmlService.class.getClassLoader().getSystemResource(connection) ;//getResourceAsStream("version.xml");
		ParseXmlService service = new ParseXmlService();//将数据通过ParseXmlService这个类解析
		mHashMap =service.parseXml(inStream);//得到解析信息 
		
		if (null != mHashMap)
		{
			serviceCode = Integer.valueOf(mHashMap.get("version"));
			// 鐗堟湰鍒ゆ柇
			if (serviceCode > versionCode)
			{
				return true;
			}
		}
		}
		catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();//测试失败
		}*/
		
	//	InterNetXml();
		
        //包装成url的对象    

		return false;
	}

	/**
	 * 鑾峰彇杞欢鐗堟湰鍙�
	 * 
	 * @param context
	 * @return
	 */
	private int getVersionCode(Context context)
	{
		int verCode = 0;
		try
		{
			// 鑾峰彇杞欢鐗堟湰鍙凤紝瀵瑰簲AndroidManifest.xml涓媋ndroid:versionCode
			verCode = context.getPackageManager().getPackageInfo("wcs.cps", 0).versionCode;
		} catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
		return verCode;
	}

	/**
	 * 鏄剧ず杞欢鏇存柊瀵硅瘽妗�
	 */
	private void showNoticeDialog()
	{
		// 鏋勯�瀵硅瘽妗�
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle(R.string.soft_update_title);
		builder.setMessage(R.string.soft_update_info);
		// 鏇存柊
		builder.setPositiveButton(R.string.soft_update_updatebtn, new OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
				// 鏄剧ず涓嬭浇瀵硅瘽妗�
				showDownloadDialog();
			}
		});
		// 绋嶅悗鏇存柊
		builder.setNegativeButton(R.string.soft_update_later, new OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});
		Dialog noticeDialog = builder.create();
		noticeDialog.show();
	}

	/**
	 * 鏄剧ず杞欢涓嬭浇瀵硅瘽妗�
	 */
	private void showDownloadDialog()
	{
		// 鏋勯�杞欢涓嬭浇瀵硅瘽妗�
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle(R.string.soft_updating);
		// 缁欎笅杞藉璇濇澧炲姞杩涘害鏉�
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.softupdate_progress, null);
		mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
		builder.setView(v);
		// 鍙栨秷鏇存柊
		builder.setNegativeButton(R.string.soft_update_cancel, new OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
				// 璁剧疆鍙栨秷鐘舵�
				cancelUpdate = true;
			}
		});
		mDownloadDialog = builder.create();
		mDownloadDialog.show();
		// 鐜板湪鏂囦欢
		downloadApk();
	}

	/**
	 * 涓嬭浇apk鏂囦欢
	 */
	private void downloadApk()
	{
		// 鍚姩鏂扮嚎绋嬩笅杞借蒋浠�
		new downloadApkThread().start();
	}
	
	private void InterNetXml()
	{
		// 鍚姩鏂扮嚎绋嬩笅杞借蒋浠�
		new InterNetThread().start();
	}
	
	private class InterNetThread extends Thread
	{
		@Override
		public void run()
		{
			try
			{
				versionCode = getVersionCode(mContext); //获取当前软件版本 
				URL url;//定义网络中version.xml的连接
				 //一个测试
				url = new URL("http://"+ clsSocketCln.m_SvrIp +"/version.xml");//创建version.xml的连接地址。
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("GET");  
				InputStream inStream = connection.getInputStream();//从输入流获取数据
				//InputStream inStream = ParseXmlService.class.getClassLoader().getSystemResource(connection) ;//getResourceAsStream("version.xml");
				ParseXmlService service = new ParseXmlService();//将数据通过ParseXmlService这个类解析
				mHashMap =service.parseXml(inStream);//得到解析信息 
				
				if (null != mHashMap)
				{
					serviceCode = Integer.valueOf(mHashMap.get("version"));
					// 鐗堟湰鍒ゆ柇
					mHandler.sendEmptyMessage(DOWNLOAD_INTERNET);
				}
			}catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	};

	/**
	 * 涓嬭浇鏂囦欢绾跨▼
	 * 
	 * @author coolszy
	 *@date 2012-4-26
	 *@blog http://blog.92coding.com
	 */
	private class downloadApkThread extends Thread
	{
		@Override
		public void run()
		{
			try
			{
				// 鍒ゆ柇SD鍗℃槸鍚﹀瓨鍦紝骞朵笖鏄惁鍏锋湁璇诲啓鏉冮檺
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
				{
					// 鑾峰緱瀛樺偍鍗＄殑璺緞
					String sdpath = Environment.getExternalStorageDirectory() + "/";
					mSavePath = sdpath + "download";
					//URL url = new URL(mHashMap.get("url"));
					URL url;
					url = new URL("http://"+ clsSocketCln.m_SvrIp +"/WcpsActivity.apk");//创建version.xml的连接地址。
					// 鍒涘缓杩炴帴
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.connect();
					// 鑾峰彇鏂囦欢澶у皬
					int length = conn.getContentLength();
					// 鍒涘缓杈撳叆娴�
					InputStream is = conn.getInputStream();

					File file = new File(mSavePath);
					// 鍒ゆ柇鏂囦欢鐩綍鏄惁瀛樺湪
					if (!file.exists())
					{
						file.mkdir();
					}
					File apkFile = new File(mSavePath, mHashMap.get("name"));
					FileOutputStream fos = new FileOutputStream(apkFile);
					int count = 0;
					// 缂撳瓨
					byte buf[] = new byte[1024];
					// 鍐欏叆鍒版枃浠朵腑
					do
					{
						int numread = is.read(buf);
						count += numread;
						// 璁＄畻杩涘害鏉′綅缃�
						progress = (int) (((float) count / length) * 100);
						// 鏇存柊杩涘害
						mHandler.sendEmptyMessage(DOWNLOAD);
						if (numread <= 0)
						{
							// 涓嬭浇瀹屾垚
							mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
							break;
						}
						// 鍐欏叆鏂囦欢
						fos.write(buf, 0, numread);
					} while (!cancelUpdate);// 鐐瑰嚮鍙栨秷灏卞仠姝笅杞�
					fos.close();
					is.close();
				}
			} catch (MalformedURLException e)
			{
				e.printStackTrace();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			// 鍙栨秷涓嬭浇瀵硅瘽妗嗘樉绀�
			mDownloadDialog.dismiss();
		}
	};

	/**
	 * 瀹夎APK鏂囦欢
	 */
	private void installApk()
	{
		File apkfile = new File(mSavePath, mHashMap.get("name"));
		if (!apkfile.exists())
		{
			return;
		}
		// 閫氳繃Intent瀹夎APK鏂囦欢
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
		mContext.startActivity(i);
	}
}

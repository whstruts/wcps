
package wcs.cps;


import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.graphics.Color;

public class actBlueMgr extends Activity
{
	private BluetoothAdapter mBluetoothAdapter = null;
	private ArrayAdapter<String> mPairedDevicesArrayAdapter;
	private ArrayAdapter<String> mBlueSacnDataAdapter;
	private static clsBluetoothMgr m_BluetoothMgr=null;
	private String m_sClsName="actBlueMgr";
	
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;	
	
	private  Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
			case clsBluetoothMgr.STATE_DIS_MESS:
				String sMess=(String)msg.obj;
				mViewMess(sMess);
				break;
				
			case clsBluetoothMgr.STATE_SEND_DATA:
				byte[] readBuf = (byte[]) msg.obj;
				String readMessage = new String(readBuf, 0, msg.arg1);
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date curDate = new Date(System.currentTimeMillis());
				String str = formatter.format(curDate);
				mBlueSacnDataAdapter.insert(str + "  " +readMessage, 0);
				break;
			 default:
				 break;
			}
			return;
		}
	};
	
	
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actbluemgr);
		this.setTitleColor(Color.BLUE);
        this.setTitle("WCPS-无线拣选小车系统 【蓝牙管理】 【" +clsMyPublic.g_WorkNo +" "+ clsMyPublic.g_WorkName +"】");
        mSetSysData();
        if (m_BluetoothMgr==null)
        	m_BluetoothMgr=new clsBluetoothMgr();
        //m_BluetoothMgr.m_CurHandler=mHandler;
        clsBluetoothMgr.m_CurHandler=mHandler;
        clsMyPublic.m_hdlSendMess=mHandler;
    }
	
	private void mViewMess(String _Mess)
	{
		TextView tv=(TextView)findViewById(R.id.tv_ts);
		tv.setText(_Mess);
	}
	
	public boolean onKeyDown(int keyCode,KeyEvent event){
		switch(keyCode){
		case KeyEvent.KEYCODE_HOME:return true;
		case KeyEvent.KEYCODE_BACK:return true;
		case KeyEvent.KEYCODE_CALL:return true;
		case KeyEvent.KEYCODE_SYM: return true;
		case KeyEvent.KEYCODE_VOLUME_DOWN: return true;
		case KeyEvent.KEYCODE_VOLUME_UP: return true;
		case KeyEvent.KEYCODE_STAR: return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	private void mSetSysData()
	{
    	Button bt_Return=(Button)findViewById(R.id.bt_Return);
    	Button bt_QueyBlue=(Button)findViewById(R.id.bt_blueCheck);
    	//返回事件
    	bt_Return.setOnClickListener
    	(
			new OnClickListener()
		    {
				public void onClick(View v) 
				{
					clsSocketMgr.m_iCntState=0;
					finish();
				}
		    }
        );

    	//搜索蓝牙
    	bt_QueyBlue.setOnClickListener
    	(
    			new OnClickListener()
    		    {
    				public void onClick(View v) 
    				{
    					clsSocketMgr.m_iCntState=0;
    					mQueryCurBluetooth();
    				}
    		    }	
    	);
		//蓝牙扫描测试数据
		mBlueSacnDataAdapter=new ArrayAdapter<String>(this,R.layout.device_name);
		ListView lv_blueScanData=(ListView)this.findViewById(R.id.listView2);
		lv_blueScanData.setAdapter(mBlueSacnDataAdapter);
	}
	private int mCheckBluetooth()
	{
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null)
		{
			Toast.makeText(null, "当前设备不支持蓝牙.",Toast.LENGTH_LONG).show();
		}
		return 1;
	}
	public int mQueryCurBluetooth()
	{
		try
		{
			mCheckBluetooth();
			ListView lv_detail=(ListView)this.findViewById(R.id.listView1);//拿到ListView的引用
			mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this,R.layout.device_name);
			lv_detail.setOnItemClickListener(mDeviceClickListener);
			lv_detail.setAdapter(mPairedDevicesArrayAdapter);
			mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	
			Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
			if (pairedDevices.size() > 0)
			{
				for (BluetoothDevice device : pairedDevices)
				{
					mPairedDevicesArrayAdapter.add(device.getName() + device.getAddress());
				}
			}
			return 1;
		}
		catch (Exception e)
		{
			String sErrLog="";
			sErrLog=m_sClsName +";mQueryCurBluetooth;" + e.toString();
	        LogDBHelper.gInsetLogData(LogDBHelper.Logs.LOGS_TABLE_NAME,m_sClsName,clsMyPublic.g_WorkName,sErrLog);
	        mShowMessage(sErrLog);
	        return -1;
	    }
	}
	
	private  void mShowMessage(String _sMess)
	{
		try
		{
			new AlertDialog.Builder(actBlueMgr.this)
	        .setTitle("温馨提示")
	        .setIcon(android.R.drawable.alert_dark_frame)
	        .setMessage(_sMess)
	        .setNegativeButton("确定", new DialogInterface.OnClickListener()
	 	    {
	 		   public void onClick(DialogInterface dialog, int which) 
	 		   {
	 			  
	 		   }
	 		 }).show();
		}
		catch (Exception e)
		{
			String sErrLog="";
			sErrLog=m_sClsName +";mShowMessage;" + e.toString();
	        LogDBHelper.gInsetLogData(LogDBHelper.Logs.LOGS_TABLE_NAME,m_sClsName,clsMyPublic.g_WorkName,sErrLog);
		}
	}
	private OnItemClickListener mDeviceClickListener = new OnItemClickListener()
	{
		public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3)
		{
			try
			{
				mBluetoothAdapter.cancelDiscovery();
				String info = ((TextView) v).getText().toString();
				String address = info.substring(info.length() - 17);
				BluetoothDevice device = mBluetoothAdapter
						.getRemoteDevice(address);			
				m_BluetoothMgr.connect(device);
			}
			catch (Exception e)
			{
				String sErrLog="";
				sErrLog=m_sClsName +";onItemClick;" + e.toString();
		        LogDBHelper.gInsetLogData(LogDBHelper.Logs.LOGS_TABLE_NAME,m_sClsName,clsMyPublic.g_WorkName,sErrLog);
		        mShowMessage(sErrLog);
		    }
		}
	};
	

}

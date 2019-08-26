package wcs.cps;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;

public class clsBluetoothMgr 
{	
	private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	private final BluetoothAdapter mAdapter;
	public static final int STATE_NONE = 0;
	public static final int STATE_LISTEN = 1; 												
	public static final int STATE_CONNECTING = 2;													
	public static final int STATE_CONNECTED = 3;	
	public static final int STATE_DIS_MESS=4;
	public static final int STATE_SEND_DATA=5;
	public static  Handler m_CurHandler; 
	
	private ConnectThread mConnectThread;
	private ConnectedThread mConnectedThread;
	
	
	public clsBluetoothMgr()
	{
		mAdapter = BluetoothAdapter.getDefaultAdapter();
	}
	private class ConnectThread extends Thread
	{
		private final BluetoothSocket mmSocket;
		private final BluetoothDevice mmDevice;

		public ConnectThread(BluetoothDevice device)
		{
			mmDevice = device;
			BluetoothSocket tmp = null;

			try
			{
				tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
			}
			catch (IOException e)
			{
				//Log.e(TAG, "create() failed", e);
			}
			mmSocket = tmp;
		}

		public void run()
		{
			setName("ConnectThread");

			mAdapter.cancelDiscovery();

			try
			{
				m_CurHandler.obtainMessage(STATE_DIS_MESS, 14,-1, "正在连接蓝牙扫描设备，请稍等...").sendToTarget();
				mmSocket.connect();
				m_CurHandler.obtainMessage(STATE_DIS_MESS, 100,-1, "连接蓝牙扫描设备成功，通信中...").sendToTarget();
			}
			catch (IOException e)
			{
				try
				{
					mmSocket.close();
				}
				catch (IOException e2)
				{
					m_CurHandler.obtainMessage(STATE_DIS_MESS, 100,-1, "连接蓝牙扫描设备失败，请联系系统管理员...").sendToTarget();
				}
			}

			synchronized (clsBluetoothMgr.this)
			{
				mConnectThread = null;
			}
			connected(mmSocket, mmDevice);
			return;
		}

		public void cancel()
		{
			try
			{
				mmSocket.close();
			}
			catch (IOException e)
			{
				
			}
		}
	}
	
	private class ConnectedThread extends Thread
	{
		private final BluetoothSocket mmSocket;
		private final InputStream mmInStream;
		private final OutputStream mmOutStream;

		public ConnectedThread(BluetoothSocket socket)
		{
			mmSocket = socket;
			InputStream tmpIn = null;
			OutputStream tmpOut = null;

			try
			{
				tmpIn = socket.getInputStream();
				tmpOut = socket.getOutputStream();
			}
			catch (IOException e)
			{
				//Log.e(TAG, "temp sockets not created", e);
			}

			mmInStream = tmpIn;
			mmOutStream = tmpOut;
			
		}

		public void run()
		{
			byte[] buffer = new byte[1024];
			int bytes;

			while (true)
			{
				try
				{
					bytes = mmInStream.read(buffer);
					m_CurHandler.obtainMessage(STATE_SEND_DATA, bytes,-1, buffer).sendToTarget();
				}
				catch (IOException e)
				{
					m_CurHandler.obtainMessage(STATE_DIS_MESS, 100,-1, "蓝牙扫描设备失去联系，请联系系统管理员...").sendToTarget();
					break;
				}
			}
		}
		public void write(byte[] buffer)
		{
			try
			{
				mmOutStream.write(buffer);

				//mHandler.obtainMessage(BluetoothChat.MESSAGE_WRITE, -1, -1,buffer).sendToTarget();
			}
			catch (IOException e)
			{
				//Log.e(TAG, "Exception during write", e);
			}
		}

		public void cancel()
		{
			try
			{
				mmSocket.close();
			}
			catch (IOException e)
			{
				//Log.e(TAG, "close() of connect socket failed", e);
			}
		}
	}
	public synchronized void start()
	{


		if (mConnectThread != null)
		{
			mConnectThread.cancel();
			mConnectThread = null;
		}

		if (mConnectedThread != null)
		{
			mConnectedThread.cancel();
			mConnectedThread = null;
		}

	}
	public synchronized void connect(BluetoothDevice device)
	{

		if (mConnectThread != null)
		{
			mConnectThread.cancel();
			mConnectThread = null;
		}
		if (mConnectedThread != null)
		{
			mConnectedThread.cancel();
			mConnectedThread = null;
		}

		mConnectThread = new ConnectThread(device);
		mConnectThread.start();
		
	}

	public synchronized void connected(BluetoothSocket socket,
			BluetoothDevice device)
	{
		if (mConnectThread != null)
		{
			mConnectThread.cancel();
			mConnectThread = null;
		}

		if (mConnectedThread != null)
		{
			mConnectedThread.cancel();
			mConnectedThread = null;
		}

		mConnectedThread = new ConnectedThread(socket);
		mConnectedThread.start();
	}
	public synchronized void stop()
	{
		if (mConnectThread != null)
		{
			mConnectThread.cancel();
			mConnectThread = null;
		}
		if (mConnectedThread != null)
		{
			mConnectedThread.cancel();
			mConnectedThread = null;
		}
	}

	public void write(byte[] out)
	{
		ConnectedThread r;
		synchronized (this)
		{
			r = mConnectedThread;
		}
		r.write(out);
	}
}

package wcs.cps;


import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.AlertDialog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class actLogin extends Activity
{	
	private Button bt_Login=null;
	private Button bt_Exit=null;
	private Button bt_Cnnt=null;
	private Button bt_Update=null;
	private TextView tv_MessTs=null;
	private TextView tv_SerIp=null;
	private TextView tv_Port=null;
	private TextView tv_wrkno=null;
	private TextView tv_pwd=null;
    private TextView tv_Cur=null;
	private int m_iCurWorkIndex=-1;
	public  Handler hd=new Handler()//声明消息处理器
	{
			@Override
			public void handleMessage(Message msg)//重写方法
        	{	
				switch(msg.what)
				{
				case clsMyPublic.MsTypeDisMess:
					mDoDisMessage(msg);
					break;
				case clsMyPublic.MsTypeReceMess:
					mDoReturnData(msg);
				}
        	}
	};
	
	
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
        setContentView(R.layout.actlogin); 
        mSetSysData();
        /*
        if (clsSocketMgr.m_iCntState==0)
        	tv_MessTs.setText("正在连接服务端,服务端IP地址:"+ 
                              clsSocketMgr.m_sktSrvIp +";端口号:" + String.valueOf(clsSocketMgr.m_sktSrvPort) +",请稍等....");
        else if(clsSocketMgr.m_iCntState==1)
        {
        	tv_MessTs.setText("连接服务端成功,服务端IP地址:"+ clsSocketMgr.m_sktSrvIp +";端口号:" + 
        			          String.valueOf(clsSocketMgr.m_sktSrvPort) +".");
        }
        */      
        
        
        //clsMyPublic.m_hdlSendMess=hd;
        clsMyPublic.g_LocalIp=clsSocketCln.GetLocalIPAddress();
        
      //  StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
       // StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
        
        UpdateManager manager = new UpdateManager(actLogin.this);

		manager.checkUpdate();

    }
	public void mSendData(int _Type)
	{
		String _sendData="";
		clsSocketCln.m_CurHandler=hd;
		if (_Type==1)
		{
			_sendData= clsMyPublic.g_SktParamLogin +";PDALOGIN;'" +
		               clsMyPublic.g_WorkNo +"','" +
					   clsMyPublic.g_WorkPass +"','" +
		               clsMyPublic.g_LocalIp +"';"+
		               clsMyPublic.g_LocalIp +";";
					 
			bt_Login.setEnabled(false);
		}
		else if(_Type==2)
		{
			_sendData=clsMyPublic.g_SktParamExit +";PDALOGOUT;'"+
					  clsMyPublic.g_WorkName +"','"+
					  clsMyPublic.g_LocalIp+"';";
			bt_Exit.setEnabled(false);
		}
	    //clsSocketMgr.hd.obtainMessage(99, 100,-1, _sendData).sendToTarget();	
	    
	    //clsSocketCln.m_SendData=clsSocketCln.g_SktParamLogin+";"+clsSocketCln.g_GsNo+";"+
        //clsSocketCln.g_WorkNo +";"+clsSocketCln.g_WorkPass;
	    clsSocketCln.m_SendData=_sendData;
		clsSocketCln _SktCln=new clsSocketCln();
		_SktCln.mStart();
			    
	    
	}
	public void mSetSysData()
	{
    	bt_Login=(Button)findViewById(R.id.bt_login);
    	bt_Exit=(Button)findViewById(R.id.bt_Exit);
    	bt_Cnnt=(Button)findViewById(R.id.bt_Cnnt);
    	bt_Update=(Button)findViewById(R.id.bt_Update);
    	tv_MessTs=(TextView)findViewById(R.id.tv_ts);
    	tv_SerIp=(TextView)findViewById(R.id.tv_SerIp);
    	tv_Port=(TextView)findViewById(R.id.tv_Port);
    	tv_wrkno=(TextView)findViewById(R.id.tv_wrkno);
    	tv_pwd=(TextView)findViewById(R.id.tv_pwd);
    		
    	clsMyPublic.g_WorkNo=LogDBHelper.gGetColValue("WORKNO");
    	
    	tv_wrkno.setText(clsMyPublic.g_WorkNo);
    	tv_SerIp.setText(clsSocketCln.m_SvrIp);
    	tv_Port.setText(String.valueOf(clsSocketCln.m_SvrPort));
    	
    	bt_Update.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				UpdateManager manager = new UpdateManager(actLogin.this);

				manager.checkUpdate();
			}
		});
    	
    	bt_Cnnt.setOnClickListener
    	(
    			new OnClickListener()
    		    {
    				public void onClick(View v) 
    				{
    					clsSocketCln.m_SvrIp=tv_SerIp.getText().toString();
    					clsSocketCln.m_SvrPort=Integer.parseInt(tv_Port.getText().toString());
    			    	if (clsSocketCln.m_SvrIp.length()==0||clsSocketCln.m_SvrPort==0 )
    			    	{   
    			    		Toast.makeText(actLogin.this, "请输入服务端IP或者服务端端口",
    								Toast.LENGTH_SHORT).show();
    			    		return;
    			    	}   			    	
    					mSaveLoginData();
    					//v.setEnabled(false);
    					//v.setBackgroundResource(R.drawable.clrunenabled);
    				}
    		    }	
    	);    	
    	//登陆事件
    	bt_Login.setOnClickListener
    	(
    			new OnClickListener()
    		    {
    				public void onClick(View v) 
    				{   
    					clsMyPublic.g_WorkNo=tv_wrkno.getText().toString();
    					clsMyPublic.g_WorkPass=tv_pwd.getText().toString();
    					if (clsMyPublic.g_WorkNo.length()==0 || clsMyPublic.g_WorkPass.length()==0)
    					{
    						Toast.makeText(actLogin.this, "请输入员工编号或者员工密码",
    								       Toast.LENGTH_SHORT).show();
    						return;
    					}
    					LogDBHelper.gUpdateColValue(0, "WORKNO", clsMyPublic.g_WorkNo);
    					mSendData(1);
    				    v.setEnabled(false);
    				    v.setBackgroundResource(R.drawable.clrunenabled);	
    				}
    		    }	
    	);
    	//退出事件
    	bt_Exit.setOnClickListener
    	(
    			new OnClickListener()
    		    {
    				public void onClick(View v) 
    				{
    					Intent intent=new Intent();
    					intent.putExtra("UINO", "MainUI");
    					setResult(3,intent);
    					finish();   
    					
    				}
    		    }	
    	);
    	tv_SerIp.setOnClickListener
    	(
    			new OnClickListener()
    		    {
    				public void onClick(View v) 
    				{
    					m_iCurWorkIndex=1;
    					mCharKeys();   										
    				}
    		    }	
    	);
    	tv_Port.setOnClickListener
    	(
    			new OnClickListener()
    		    {
    				public void onClick(View v) 
    				{
    					m_iCurWorkIndex=2;
    					mCharKeys();   										
    				}
    		    }	
    	);
    	tv_wrkno.setOnClickListener
    	(
    			new OnClickListener()
    		    {
    				public void onClick(View v) 
    				{
    					m_iCurWorkIndex=3;
    					mCharKeys(); 
    					v.setFocusable(true);
    					v.setFocusableInTouchMode(true);
    				}
    		    }	
    	);
    	tv_pwd.setOnClickListener
    	(
    			new OnClickListener()
    		    {
    				public void onClick(View v) 
    				{
    					m_iCurWorkIndex=4;
    					mCharKeys(); 
    					v.setFocusable(true);
    					v.setFocusableInTouchMode(true);
    				}
    		    }	
    	);

    	
	}
	
	private void mDoReturnData(Message msg)
	{	
		String sReceData="",sFunNo="",sRet1="";
		sReceData=(String)msg.obj;
		sFunNo=clsMyPublic.GetStrInOfStr(sReceData, 0);
		if (sFunNo.compareTo(clsMyPublic.g_SktParamLogin)==0)
		{
			sRet1=clsMyPublic.GetStrInOfStr(sReceData, 1);
			if (sRet1.compareTo("1")==0)
			{
				clsMyPublic.g_WorkName=clsMyPublic.GetStrInOfStr(sReceData,3);
				clsMyPublic.g_WorkAreaNo=clsMyPublic.GetStrInOfStr(sReceData,4);
				setResult(2,null);
				finish();
			}
			else if (sRet1.compareTo("-1")==0)
			{	
				tv_MessTs.setText(clsMyPublic.GetStrInOfStr(sReceData,2));
				bt_Login.setEnabled(true);
				bt_Login.setBackgroundResource(R.drawable.clsbtnselect);
			}
			else if (sRet1.compareTo("-2")==0)
			{	
				tv_MessTs.setText(clsMyPublic.GetStrInOfStr(sReceData,2));
			}
		}
	}
	private void mDoDisMessage(Message msg)
	{
		String sMess=(String)msg.obj;
		tv_MessTs.setText(sMess);
	}
	private void mSaveLoginData()
	{

		LogDBHelper.gUpdateColValue(0, "SERIP", clsSocketCln.m_SvrIp);
		LogDBHelper.gUpdateColValue(0, "SERPORT", String.valueOf(clsSocketCln.m_SvrPort));
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
	    switch (keyCode)
	    {
	        case KeyEvent.KEYCODE_BACK:
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	private void mCharKeys()
	{
		LinearLayout loginLayout = (LinearLayout) getLayoutInflater().inflate(
				R.layout.actcharkeys, null);
		final AlertDialog adNumKey=
		new AlertDialog.Builder(this)
				.setView(loginLayout).create();//.setTitle("键盘")
		Window win=adNumKey.getWindow();
		WindowManager.LayoutParams lp=win.getAttributes();
		lp.x=0;
		lp.y=200;
		win.setAttributes(lp);
		clsMyPublic.m_sKeys="";
		if (m_iCurWorkIndex==1)
			tv_Cur=tv_SerIp;
		else if (m_iCurWorkIndex==2)
			tv_Cur=tv_Port;
		else if (m_iCurWorkIndex==3)
			tv_Cur=tv_wrkno;
		else if (m_iCurWorkIndex==4)
			tv_Cur=tv_pwd;
		//clsMyPublic.m_sKeys=tv_Cur.getText().toString();
		for (int i = 0; i < loginLayout.getChildCount(); i++)
		{  
			View v=loginLayout.getChildAt(i);
			if (v instanceof RelativeLayout)
			{
				RelativeLayout KeyLayout=(RelativeLayout)v;
				for(int k=0;k<KeyLayout.getChildCount();k++)
				{
					View x=KeyLayout.getChildAt(k);
					x.setOnClickListener
			    	(
		    			new OnClickListener()
		    		    {
		    				public void onClick(View v) 
		    				{
		    					CharSequence sTmp="";
		    					String sKey="";
		    					Button btn=(Button)v;
		    					sTmp=btn.getText();
		    					sKey=(String)sTmp;
		    					if (sKey.compareTo("回车")==0)
		    					{
		    						adNumKey.cancel();
		    						return;
		    					}
		    					else if (sKey.compareTo("删除")==0)
		    					{
		    						int iLen=-1;
		        					iLen=clsMyPublic.m_sKeys.length();
		        					if (iLen>0)
		        					{
		        						clsMyPublic.m_sKeys=clsMyPublic.m_sKeys.substring(0,iLen-1);
		        						tv_Cur.setText(clsMyPublic.m_sKeys);
		        					}
		    						return;
		    					}
		    					else if (sKey.compareTo("清除")==0)
		    					{
		    						clsMyPublic.m_sKeys="";
		    						tv_Cur.setText("");
		    						return;
		    					}
		    					else if(sKey.compareTo("取消")==0)
		    					{
		    						adNumKey.cancel();
		    						return;
		    					}
		    					clsMyPublic.m_sKeys+=sKey;
		    					tv_Cur.setText(clsMyPublic.m_sKeys);

		    				}
		    		    }
			    	);
				}
			}
	    }
		
		adNumKey.show();		
	}
}

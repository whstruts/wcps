package wcs.cps;




import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class actSupplyActive extends Activity
{
	private RadioButton[] rbline=null;  
	private RadioGroup radioGroup;  

	private Button bt_Return=null;
	private Button bt_Refresh=null;
	private Button bt_Dwcl=null;
	
	private ListView lv_mainTask=null;
	private TextView tv_MessTs=null;
	private TextView tv_Cur=null;
	private TextView txt_bhtm=null;
	
	private EditText edtInput=null;
	
	
	private String[][] m_receMainTask;
	private int m_iCurReceIndex=-1;
	private int m_iSumReceCount=-1;
	
	private String m_sBarCodeData="";
	private String m_sAreaNo="";
	
	private String m_sClsName="actSupplyActive";
	
	String[][]msg=null;
	
	public  Handler hd=new Handler()//声明消息处理器
	{
		public void handleMessage(Message msg)//重写方法
		{			
			switch(msg.what)
			{
			case clsMyPublic.MsTypeDisMess:
				mDoDisMessage(msg);
				break;
			case clsMyPublic.MsTypeReceMess:
				mDoReturnData(msg);
				break;
		    case clsBluetoothMgr.STATE_SEND_DATA:
		    	mViewBlueData(msg);
		    	break;
			}
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.actsupplyactive);
		this.setTitleColor(Color.BLUE);
        this.setTitle("WCPS-无线拣选小车系统 【补货标签激活】 【" +clsMyPublic.g_WorkNo +" "+ clsMyPublic.g_WorkName +"】");
		clsMyPublic.m_hdlSendMess=hd;
		clsBluetoothMgr.m_CurHandler=hd;
		mSetButtonIni();
		m_sAreaNo=clsMyPublic.g_WorkAreaNo;
	}
	private void mViewBlueData(Message msg)
	{
		byte[] readBuf = (byte[]) msg.obj;
		String readMessage = new String(readBuf, 0, msg.arg1);
		readMessage=readMessage.trim();
		m_sBarCodeData=readMessage;
		mSendData(2);
	}
	private void mDoDisMessage(Message msg)
	{
		String sMess=(String)msg.obj;
		tv_MessTs.setText(sMess);
	}
	private void mSetButtonIni()
	{
		rbline=new RadioButton[5];
		rbline[0]=(RadioButton)findViewById(R.id.radio0);
		rbline[1]=(RadioButton)findViewById(R.id.radio1);
		rbline[2]=(RadioButton)findViewById(R.id.radio2);
		rbline[3]=(RadioButton)findViewById(R.id.radio3);
		rbline[4]=(RadioButton)findViewById(R.id.radio4);
		
		radioGroup = (RadioGroup)findViewById(R.id.rdGrp);  
		radioGroup.setOnCheckedChangeListener(new RadioGroupListener()); 
		
		bt_Return=(Button)findViewById(R.id.bt_return);
		bt_Refresh=(Button)findViewById(R.id.bt_Refresh);
		bt_Dwcl=(Button)findViewById(R.id.bt_Dwcl);
		
		tv_MessTs=(TextView)findViewById(R.id.tv_ts);
		txt_bhtm=(TextView)findViewById(R.id.txt_bhtm);
		lv_mainTask=(ListView)findViewById(R.id.lst_bhData);
		
		
		edtInput=(EditText)findViewById(R.id.edtInput);
		edtInput.setInputType(InputType.TYPE_NULL);

		edtInput.setOnKeyListener
    	(
    		new View.OnKeyListener()
	    	{
	    		public boolean onKey(View v, int keyCode, KeyEvent event)
	    		{
	    			if (keyCode==KeyEvent.KEYCODE_ENTER)
	    			{	
	    				String s="";
	    				s=edtInput.getText().toString();
	    				s=s.trim();
	    				if (s.length()==0) 
	    				{
	    					edtInput.setText("");
	    					return true;
	    				}	  
	    				m_sBarCodeData=edtInput.getText().toString();
	    				edtInput.setText("");  
	    				mSendData(2);
	    			}
	    			return false;
	    		}
	    	}
    	);
		
		bt_Dwcl.setOnClickListener
    	(
			new OnClickListener()
		    {
				public void onClick(View v) 
				{
					mCharKeys();
					return;
				}
		    }
        );
		
		//返回事件
    	bt_Return.setOnClickListener
    	(
			new OnClickListener()
		    {
				public void onClick(View v) 
				{
					finish();
				}
		    }
        );

    	//刷新事务事件
    	bt_Refresh.setOnClickListener
    	(
			new OnClickListener()
		    {
				public void onClick(View v) 
				{
					m_receMainTask=null;
					mSendData(1);
				    v.setEnabled(false);
				    v.setBackgroundResource(R.drawable.clrunenabled);	
				}
		    }
        );
		
	}
	class RadioGroupListener implements RadioGroup.OnCheckedChangeListener
	{   
		public void onCheckedChanged(RadioGroup group, int checkedId)
		{  
			if(checkedId == rbline[0].getId())
			{  
				m_sAreaNo="01";
			}
		    else if(checkedId == rbline[1].getId())
		    {  
		    	m_sAreaNo="02";
		    }  
		    else if(checkedId == rbline[2].getId())
		    {  
		    	m_sAreaNo="03";
		    }  
		    else if(checkedId == rbline[3].getId())
		    {  
		    	m_sAreaNo="04";
		    }  
		    else if(checkedId == rbline[4].getId())
		    {  
		    	m_sAreaNo="05";
		    }  			
			
			m_receMainTask=null;
			lv_mainTask.removeAllViewsInLayout();

			mSendData(1);

			
			bt_Refresh.setEnabled(false);
			bt_Refresh.setBackgroundResource(R.drawable.clrunenabled);	
			
			rbline[0].setEnabled(false);
			rbline[1].setEnabled(false);
			rbline[2].setEnabled(false);
			rbline[3].setEnabled(false);
			rbline[4].setEnabled(false);
		}  
        
    }  
	
	public void mSendData(int _Type)
	{
		String _sendData="";
		clsSocketCln.m_CurHandler=hd;
		if (_Type==1)
		{
			_sendData=clsMyPublic.g_sktParamBhActiv +";" +
					  m_sAreaNo +";";
		}
		else if (_Type==2)
		{			
			_sendData=clsMyPublic.g_sktParamBhDanwei +";" +
					  m_sBarCodeData +";";
		}
		clsSocketCln.m_SendData=_sendData;
		clsSocketCln _SktCln=new clsSocketCln();
		_SktCln.mStart();
	}
	
	private void mFillMainTaskData(String[][] _sData)
	{
		msg=_sData;
        BaseAdapter ba_detail=new BaseAdapter()//新建适配器
        {
			public int getCount() 
			{
				return msg[0].length;//得到列表的长度
			}
			public Object getItem(int arg0){return null;}
			public long getItemId(int arg0){return 0;}
			public View getView(int arg0, View arg1, ViewGroup arg2)//为每一项添加内容
			{
				LinearLayout ll_detail=new LinearLayout(actSupplyActive.this);
				ll_detail.setOrientation(LinearLayout.HORIZONTAL);		//设置朝向	
				ll_detail.setPadding(5,5,5,5);//四周留白

				for(int i=0;i<msg.length;i++)//为每一行设置显示的数据
				{					    
					TextView s= new TextView(actSupplyActive.this);
					s.setText(msg[i][arg0]+" ");//TextView中显示的文字
					s.setTextSize(20);//字体大小
					s.setTextColor(Color.RED);
					s.setPadding(1,2,2,1);//四周留白
					if (i==0)
						s.setWidth(400);//宽度
					else if (i==1)
						s.setWidth(300);
					else
						s.setWidth(300);
					
				    ll_detail.addView(s);//放入LinearLayout
				}
				return ll_detail;//将此LinearLayout返回
			}        	
        };        
        lv_mainTask.setAdapter(ba_detail);
        
        
        
        lv_mainTask.setOnItemClickListener//为列表添加监听
        (
           new OnItemClickListener()
           {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) //arg2为点击的第几项
			{
				m_sBarCodeData=msg[0][arg2];
				mShowMessageII("请确定补货商品已经到补货区域，您确定要激活吗?");
			}        	   
           }
        );
	}
	

	private void mDoReturnData(Message msg)
	{	
		try
		{
			String sReceData="",sFunNo="",sRet1="";
			sReceData=(String)msg.obj;
			sFunNo=clsMyPublic.GetStrInOfStr(sReceData, 0);
			if (sFunNo.compareTo(clsMyPublic.g_sktParamBhActiv)==0)
			{			
				sRet1=clsMyPublic.GetStrInOfStr(sReceData, 1);
				if (sRet1.compareTo("-1")==0||sRet1.compareTo("0")==0)
				{
					tv_MessTs.setText(sReceData);
					bt_Refresh.setBackgroundResource(R.drawable.clsbtnselect);
					bt_Refresh.setEnabled(true);
					
					if (m_receMainTask==null)
					{

						m_receMainTask=new String[4][1];				
						for(int i=0;i<4;i++)
						{
							m_receMainTask[i]=new String[1];
						}
					}
					
					for(int i=0;i<4;i++)
					{
						m_receMainTask[i][0]="";
					}
						
					mFillMainTaskData(m_receMainTask);	
					
					rbline[0].setEnabled(true);
					rbline[1].setEnabled(true);
					rbline[2].setEnabled(true);
					rbline[3].setEnabled(true);
					rbline[4].setEnabled(true);
					
					return;
				}
				else if (sRet1.compareTo("-2")==0)
				{	
					tv_MessTs.setText(sReceData);
				}
				else
				{				
					tv_MessTs.setText(sReceData);
					if (m_receMainTask==null)
					{
						m_iSumReceCount=Integer.parseInt(sRet1);
						m_receMainTask=new String[4][m_iSumReceCount];				
						for(int i=0;i<4;i++)
						{
							m_receMainTask[i]=new String[m_iSumReceCount];
						}
						m_iCurReceIndex=-1;
					}
					m_iCurReceIndex++;
					for(int i=0;i<4;i++)
					{
						m_receMainTask[i][m_iCurReceIndex]=clsMyPublic.GetStrInOfStr(sReceData, i+2);
					}
						
					mFillMainTaskData(m_receMainTask);		
					
					if (m_iSumReceCount==(m_iCurReceIndex+1))
					{
						//保证一次查询借宿后，才开始下次查询
						bt_Refresh.setBackgroundResource(R.drawable.clsbtnselect);
						bt_Refresh.setEnabled(true);
						
						rbline[0].setEnabled(true);
						rbline[1].setEnabled(true);
						rbline[2].setEnabled(true);
						rbline[3].setEnabled(true);
						rbline[4].setEnabled(true);
					}
				}
				
			}
			else
			{
				tv_MessTs.setText(sReceData);
			}
		}
		catch (Exception e)
		{
			String sErrLog="";
			sErrLog=m_sClsName +";mDoReturnData;" + e.toString();
	        LogDBHelper.gInsetLogData(LogDBHelper.Logs.LOGS_TABLE_NAME,m_sClsName,clsMyPublic.g_WorkName,sErrLog);
	        mShowMessage(sErrLog);
	    }
		catch(UnknownError e)
		{
			String sErrLog="";
			sErrLog=m_sClsName +";mDoReturnData;" + e.toString();
	        LogDBHelper.gInsetLogData(LogDBHelper.Logs.LOGS_TABLE_NAME,m_sClsName,clsMyPublic.g_WorkName,sErrLog);
		}

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
	
	private  void mShowMessage(String _sMess)
	{
		try
		{
			new AlertDialog.Builder(actSupplyActive.this)
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
	
	public  void mShowMessageII(String _sMess)
	{
		new AlertDialog.Builder(actSupplyActive.this)
        .setTitle("温馨提示")
        .setIcon(android.R.drawable.alert_dark_frame)
        .setMessage(_sMess+"\n确定要退出本界面吗?")
        .setNegativeButton("取消", new DialogInterface.OnClickListener()
		   {
			   public void onClick(DialogInterface dialog, int which)
			   {
				   
			   }
		   }
		   ).setPositiveButton("确定", new DialogInterface.OnClickListener()
 	    {
 		   public void onClick(DialogInterface dialog, int which) 
 		   {
 			  mSendData(2);
 		   }
 		 }).show();
	}
	
	private void mCharKeys()
	{
		LinearLayout loginLayout = (LinearLayout) getLayoutInflater().inflate(
				R.layout.actcharkeys, null);
		final AlertDialog adNumKey=
		new AlertDialog.Builder(this)
				.setTitle("键盘").setView(loginLayout).create();
		Window win=adNumKey.getWindow();
		WindowManager.LayoutParams lp=win.getAttributes();
		lp.x=-500;
		lp.y=500;
		win.setAttributes(lp);
		clsMyPublic.m_sKeys="";
		tv_Cur=txt_bhtm;
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
		    						m_sBarCodeData=clsMyPublic.m_sKeys;
		    						mSendData(2);
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

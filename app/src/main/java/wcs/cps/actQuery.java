package wcs.cps;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.Gravity;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class actQuery extends Activity
{
	
	private Button bt_Return=null;
	private Button bt_Refresh=null;
	private Button bt_Up=null;
	private Button bt_Down=null;
	private ListView lv_mainTask=null;
	private ListView lv_HzTask=null;
	private TextView tv_MessTs=null;
	private TextView tv_Cur=null;	
	private TextView tv_cxtj=null;
	
	private String[][] m_receMainTask;
	private String[][] m_HzMainTask;
	
	private int m_iCurReceIndex=-1;
	private int m_iSumReceCount=-1;
	
	public static String m_sFpdNo="";
	public static String m_sAreaNo="";
	public static String m_sBoxNo="";
	public static int m_iCurBoxIndex=0;
	public static String[] m_sQueryBoxNo;
	public static int m_iMaxBoxIndex=0;
	
	private int[] m_selectColor = new int[50];
	
	private EditText edtInput=null;
	
	private String m_sClsName="actQuery";
	
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
        setContentView(R.layout.actquery);
        this.setTitleColor(Color.BLUE);
        this.setTitle("WCPS-无线拣选小车系统 【拣货复查】 【" +clsMyPublic.g_WorkNo +" "+ clsMyPublic.g_WorkName +"】");
        clsMyPublic.m_hdlSendMess=hd;   
        mSetSysData();
        mSetButtonEanble();
        mSendData(1);
    }
	
	public void mSendData(int _Type)
	{
		String _sendData="";
		clsSocketCln.m_CurHandler=hd;
		if (_Type==1)
		{
			_sendData=clsMyPublic.g_SktParamCXPick +";" +
					  m_sFpdNo +";" +
					  m_sAreaNo +";";
		    bt_Refresh.setEnabled(false);
		    bt_Refresh.setBackgroundResource(R.drawable.clrunenabled);	
		}
		clsSocketCln.m_SendData=_sendData;
		clsSocketCln _SktCln=new clsSocketCln();
		_SktCln.mStart();
	}
	
	private void mSetButtonEanble()
	{
		
		//向上翻
		if (m_iCurBoxIndex<=0)
		{
			bt_Up.setEnabled(false);
			bt_Up.setBackgroundResource(R.drawable.clrunenabled);	
			bt_Down.setEnabled(false);
			bt_Down.setBackgroundResource(R.drawable.clrunenabled);
		}
		else
		{
			bt_Up.setEnabled(true);
			bt_Up.setBackgroundResource(R.drawable.clsbtnselect);
			
			bt_Down.setEnabled(false);
			bt_Down.setBackgroundResource(R.drawable.clrunenabled);
		}
		
		if(m_iMaxBoxIndex==m_iCurBoxIndex)
		{			
			bt_Down.setEnabled(false);
			bt_Down.setBackgroundResource(R.drawable.clrunenabled);
		}
		else
		{
			bt_Down.setEnabled(true);
			bt_Down.setBackgroundResource(R.drawable.clsbtnselect);
		}
	}
	private void mDoDisMessage(Message msg)
	{
		String sMess=(String)msg.obj;
		tv_MessTs.setText(sMess);
	}
	private void mSetSysData()
	{
		bt_Return=(Button)findViewById(R.id.bt_return);
		bt_Refresh=(Button)findViewById(R.id.bt_Refresh);
		bt_Up=(Button)findViewById(R.id.bt_Up);
		bt_Down=(Button)findViewById(R.id.bt_Down);
		
		tv_cxtj=(TextView)findViewById(R.id.tv_cxtj);
		
    	tv_MessTs=(TextView)findViewById(R.id.tv_ts);
		lv_mainTask=(ListView)findViewById(R.id.listView1);
		lv_HzTask=(ListView)findViewById(R.id.listView2);
		
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
	    				tv_cxtj.setText(s);
	    				m_sFpdNo=tv_cxtj.getText().toString();
	    				
	    				m_receMainTask=null;
	    				m_iCurBoxIndex=0;
	    				
						mSendData(1);
						
	    				edtInput.setText("");  				
	    			}
	    			return false;
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
					m_sFpdNo="";
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
					m_sFpdNo=tv_cxtj.getText().toString();
    				m_iCurBoxIndex=0;
    				
					mSendData(1);
				    v.setEnabled(false);
				    v.setBackgroundResource(R.drawable.clrunenabled);	
				}
		    }
        );
    	tv_cxtj.setOnClickListener
    	(
			new OnClickListener()
		    {
				public void onClick(View v) 
				{
					mNumKeys(); 
				}
		    }
        );
    	
    	bt_Up.setOnClickListener
    	(
			new OnClickListener()
		    {
				public void onClick(View v) 
				{
					if(m_iCurBoxIndex>0)
					{
						m_iCurBoxIndex--;
						m_sFpdNo=m_sQueryBoxNo[m_iCurBoxIndex];
						m_receMainTask=null;
						mSetButtonEanble(); 
						mSendData(1);
					}
				}
		    }
        );
    	bt_Down.setOnClickListener
    	(
			new OnClickListener()
		    {
				public void onClick(View v) 
				{
					if(m_iCurBoxIndex<m_iSumReceCount)
					{
						
						m_sFpdNo=m_sQueryBoxNo[m_iCurBoxIndex];
						m_receMainTask=null;
						mSetButtonEanble(); 
						mSendData(1);
						m_iCurBoxIndex++;
					}
				}
		    }
        );
	}
	
	private void mFillMainTaskData(String[][] _sData)
	{
		final String[][]msg=_sData;
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
				LinearLayout ll_detail=new LinearLayout(actQuery.this);
				ll_detail.setOrientation(LinearLayout.HORIZONTAL);		//设置朝向	
				ll_detail.setPadding(5,5,5,5);//四周留白
				
				
				
				
				if(m_selectColor[arg0]==1)
				{
					ll_detail.setBackgroundColor(Color.TRANSPARENT);	
					ll_detail.setBackgroundColor(Color.RED);	
				}
				else
				{
					ll_detail.setBackgroundColor(Color.TRANSPARENT);	
					
					if (msg[10][arg0]!=null)
					{
						if (msg[10][arg0].compareTo("索取")!=0)
							ll_detail.setBackgroundColor(Color.GREEN);
					}
				}
				
				for(int i=0;i<msg.length;i++)//为每一行设置显示的数据
				{	
															
					TextView s= new TextView(actQuery.this);
				
					s.setText(msg[i][arg0]+" ");//TextView中显示的文字
					s.setTextSize(20);//字体大小
					s.setTextColor(Color.BLUE);
					s.setPadding(1,2,2,1);//四周留白
										
					if (i==0 || i==4 || i==5|| i==6 || i==7|| i==11 || i==12)
						//s.setWidth(120);//宽度
					s.setWidth(192);
					else if (i==1||i==3)
						//s.setWidth(180);//宽度
					s.setWidth(288);
					else if (i==2 || i==8|| i==10)
						//s.setWidth(300);
					s.setWidth(480);
					else if(i==9)
						//s.setWidth(500);
					s.setWidth(800);
					s.setHeight(40);
					if (i==4|| i==5 || i==6|| i==7)
						s.setGravity(Gravity.LEFT);
					else if (i==8)
						s.setGravity(Gravity.LEFT);
					else
						s.setGravity(Gravity.LEFT);					
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
					
					if(m_selectColor[arg2]!=1)
					{
						m_selectColor[arg2]=1;
						arg1.setBackgroundColor(Color.RED);
					}
					else
					{
						m_selectColor[arg2]=0;
						if (msg[10][arg2].compareTo("索取")!=0)
						{
							arg1.setBackgroundColor(Color.GREEN);
						}
						else
						{
							arg1.setBackgroundColor(Color.TRANSPARENT);	
						}
					}
					 
					//mShowMessage(sMess);
				}        	   
           }
        );
        
        lv_mainTask.deferNotifyDataSetChanged();
	}
	
	private void mFillHzTaskData(String[][] _sData)
	{
		final String[][]msg=_sData;
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
				LinearLayout ll_detail=new LinearLayout(actQuery.this);
				ll_detail.setOrientation(LinearLayout.HORIZONTAL);		//设置朝向	
				ll_detail.setPadding(5,5,5,5);//四周留白				
				for(int i=0;i<msg.length;i++)//为每一行设置显示的数据
				{					    
					TextView s= new TextView(actQuery.this);
					s.setText(msg[i][arg0]+" ");//TextView中显示的文字
					s.setTextSize(20);//字体大小
					s.setTextColor(Color.RED);
					s.setPadding(1,2,2,1);//四周留白
					if (i==0 || i==1 || i==3 || i==5 || i==7)
						s.setWidth(100); 
					else 
					{
						s.setWidth(150);
						s.setGravity(Gravity.LEFT);
					}
				    ll_detail.addView(s);//放入LinearLayout
				}
				return ll_detail;//将此LinearLayout返回
			}   	
        }; 
        lv_HzTask.setAdapter(ba_detail);
	}
	
	private void mDoReturnData(Message msg)
	{	
		try
		{
			String sReceData="",sFunNo="",sRet1="";
			sReceData=(String)msg.obj;
			sFunNo=clsMyPublic.GetStrInOfStr(sReceData, 0);
			if (sFunNo.compareTo(clsMyPublic.g_SktParamCXPick)==0)
			{			
				sRet1=clsMyPublic.GetStrInOfStr(sReceData, 1);
				if (sRet1.compareTo("-1")==0||sRet1.compareTo("0")==0)
				{
					//mShowMessage(clsMyPublic.GetStrInOfStr(sReceData, 2));
					bt_Refresh.setBackgroundResource(R.drawable.clsbtnselect);
					bt_Refresh.setEnabled(true);
					mSetButtonEanble();
					return;
				}
				else if (sRet1.compareTo("-2")==0)
				{	
					tv_MessTs.setText(clsMyPublic.GetStrInOfStr(sReceData,2));
				}
				else
				{	
					tv_MessTs.setText(sReceData);
					if (m_receMainTask==null)
					{
						m_iSumReceCount=Integer.parseInt(sRet1);
						m_iMaxBoxIndex = m_iSumReceCount;
						m_receMainTask=new String[13][m_iSumReceCount];				
						for(int i=0;i<13;i++)
						{
							m_receMainTask[i]=new String[m_iSumReceCount];
						}
						
						m_HzMainTask=new String[3][1];
						m_HzMainTask[0]=new String[1];
						m_HzMainTask[0][0]="合计:";					
						m_HzMainTask[1]=new String[1];
						m_HzMainTask[1][0]="品规数:";
						m_HzMainTask[2]=new String[1];					
						m_HzMainTask[2][0]=String.valueOf(m_iSumReceCount);
						mFillHzTaskData(m_HzMainTask);
						m_iCurReceIndex=-1;
					}
					m_iCurReceIndex++;
									
					if (m_iCurReceIndex==m_iSumReceCount)
					{
						mShowMessage("接受数据有异常，请重新尝试...或者联系系统管理员");
						return;
					}
					else if (m_iCurReceIndex>m_iSumReceCount)
					{
						return;
					}
					
					for(int i=0;i<13;i++)
					{
						m_receMainTask[i][m_iCurReceIndex]=clsMyPublic.GetStrInOfStr(sReceData, i+2);
					}					
					mFillMainTaskData(m_receMainTask);
					if (m_iSumReceCount==(m_iCurReceIndex+1))
					{
						//保证一次查询借宿后，才开始下次查询
						bt_Refresh.setBackgroundResource(R.drawable.clsbtnselect);
						bt_Refresh.setEnabled(true);
						mSetButtonEanble();
					}
				}

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
	
	private void mViewBlueData(Message msg)
	{
		byte[] readBuf = (byte[]) msg.obj;
		String readMessage = new String(readBuf, 0, msg.arg1);
		tv_cxtj.setText(readMessage);			
	}
	
	private  void mShowMessage(String _sMess)
	{
		try
		{
			new AlertDialog.Builder(actQuery.this)
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
	
	private void mNumKeys()
	{
		LinearLayout loginLayout = (LinearLayout) getLayoutInflater().inflate(
				R.layout.actnumkeys, null);
		final AlertDialog adNumKey=
		new AlertDialog.Builder(this)
				.setTitle("键盘").setView(loginLayout).create();
		Window win=adNumKey.getWindow();
		WindowManager.LayoutParams lp=win.getAttributes();
		lp.x=200;
		lp.y=200;
		win.setAttributes(lp);
		clsMyPublic.m_sKeys="";
		
		tv_Cur=tv_cxtj;
		
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

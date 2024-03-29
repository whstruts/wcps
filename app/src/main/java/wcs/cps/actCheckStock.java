package wcs.cps;

import java.util.ArrayList;

import wcs.cps.actPickWork.clsPickTask;
import wcs.cps.actSupply.clsSupTask;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class actCheckStock extends Activity 
{
	private TextView[] m_WorkBt;
	private int m_iPickTaskCount=0;
	private int m_iSumPickBtCount=10;
	private int m_iCurMaxIndex=-1;
	private int m_iCurMinIndex=-1;
	private int m_iCurReceCount=0;
	private int m_iCurWorkIndex=-1;
	private int m_iCurBtIndex=-1;
	private int m_iUnWorkCount=0;   //未作业的任务数
	
	private Button bt_save=null;
	private Button bt_Return=null;
	private Button bt_scan=null;
	private Button bt_hwtz=null;
	private Button btUp=null;
	private Button btDown=null;
	private TextView tv_MessTs=null;
	
	
	private String[][] m_receMainTask;
	private int m_iCurReceIndex=-1;
	private int m_iSumReceCount=-1;
	private String m_sQuery="";
	private String m_RealNum="";
	private EditText edtInput=null;
	private String m_sClsName="actCheckStock";
	
	private Intent m_acthwtz=null;
	
	private TextView tv_hwcode=null;
	private TextView tv_chinaname = null;// 中文名
	private TextView tv_sl=null;//库存数量
	private TextView tv_realsl=null;//实际数量
	private TextView tv_Manu = null;// 制造商信息
	private TextView tv_bzdw = null;// 包装单位
	private TextView tv_ypgg = null;// 药品规格
	private TextView tv_spph = null;// 批号
	private TextView tv_yxDate = null;// 有效期
	
	private static ArrayList<clsSupTask> m_SupTaskList=null;
	
	public class clsSupTask 
	{
		String _sDanjNo="";
		String _sHangHao="";
		String _sBoxNo="";
		String _sPickHw="";
		String _sArtName;
		String _sArtGg;
		String _sArtZbz;
		String _sArtBzdw;
		String _sArtPh;
		String _sArtCj;
		String _sJhsl;
		String _sSjsl;
		String _sZylb;
		String _sFpdno;
		String _sBarCode;
		String _sArtDate;   //有效日期
		int   _iDoStatus=0;  // 0 初始化 2 索取成功 3 上架确认
	}
	
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
        setContentView(R.layout.actcheckstock);
        this.setTitleColor(Color.BLUE);
        this.setTitle("WCPS-无线拣选小车系统 【货位盘查】 【" +clsMyPublic.g_WorkNo +" "+ clsMyPublic.g_WorkName +"】");
        clsMyPublic.m_hdlSendMess=hd;
        clsBluetoothMgr.m_CurHandler=hd;
        m_SupTaskList=new ArrayList<clsSupTask>();
        
        mSetSysData();
        mSetButtonIni();
        
    }
	
	private void mSetButtonIni()
	{
		for(int i=0;i<m_iSumPickBtCount;i++)
		{
			m_WorkBt[i].setEnabled(false);
			m_WorkBt[i].setBackgroundResource(R.drawable.clrunenabled);
		}
		
		tv_hwcode.setText("");
		tv_chinaname.setText("");
		tv_sl.setText("");
		tv_realsl.setText("");
		tv_Manu.setText("");
		tv_bzdw.setText("");
		tv_ypgg.setText("");
		tv_spph.setText("");
		tv_yxDate.setText("");
	}
	
	public void mSendData(int _Type)
	{
		String _sendData="";
		clsSocketCln.m_CurHandler=hd;
	

		
		
		if (_Type==1)
		{
			m_SupTaskList.clear();
			m_iCurReceCount=0;
			m_iPickTaskCount=0;
			m_iCurMaxIndex=-1;
			m_iCurMinIndex=-1;
			//m_iUnWorkCount=0;
			m_iCurBtIndex=-1;
			_sendData=clsMyPublic.g_sktParamHwDp +";" +
					  m_sQuery +";";
		}
		if(_Type==2)
		{
			_sendData=clsMyPublic.g_sktParamDpQr+";AIOPDCONFIRM;'"+
					m_sQuery+"',"+
					m_RealNum+",'"+
					clsMyPublic.g_WorkName+"';";


		}
		clsSocketCln.m_SendData=_sendData;
		clsSocketCln _SktCln=new clsSocketCln();
		_SktCln.mStart();
	}
	private void mDoDisMessage(Message msg)
	{
		String sMess=(String)msg.obj;
		tv_MessTs.setText(sMess);
	}
	private void mSetSysData()
	{
		bt_Return=(Button)findViewById(R.id.bt_return);
		bt_scan=(Button)findViewById(R.id.bt_scan);
		bt_hwtz=(Button)findViewById(R.id.bt_hwtz);
		bt_save=(Button)findViewById(R.id.bt_save);
		btUp=(Button)findViewById(R.id.bt_up);
		btDown=(Button)findViewById(R.id.bt_down);
		btUp.setEnabled(false);
		btDown.setEnabled(false);
		
    	tv_MessTs=(TextView)findViewById(R.id.tv_ts);
		
    	tv_hwcode=(TextView)findViewById(R.id.tv_hwcode);
    	tv_chinaname=(TextView)findViewById(R.id.tv_chinaname);
		tv_sl=(TextView)findViewById(R.id.tv_sl);
		tv_realsl=(TextView)findViewById(R.id.tv_sl_real);
		tv_Manu=(TextView)findViewById(R.id.tv_manu_v);
		tv_bzdw=(TextView)findViewById(R.id.tv_bzdw);
		tv_ypgg=(TextView)findViewById(R.id.tv_ypgg);
		tv_spph=(TextView)findViewById(R.id.tv_spph);
		tv_yxDate=(TextView)findViewById(R.id.tv_yxDate);
		
		edtInput=(EditText)findViewById(R.id.edt_ScnData);
		edtInput.setInputType(InputType.TYPE_NULL);
		
		m_WorkBt=new TextView[m_iSumPickBtCount];
    	m_WorkBt[0]=(TextView)findViewById(R.id.tv_hw01);
    	m_WorkBt[1]=(TextView)findViewById(R.id.tv_hw02);
    	m_WorkBt[2]=(TextView)findViewById(R.id.tv_hw03);
    	m_WorkBt[3]=(TextView)findViewById(R.id.tv_hw04);
    	m_WorkBt[4]=(TextView)findViewById(R.id.tv_hw05);
    	m_WorkBt[5]=(TextView)findViewById(R.id.tv_hw06);
    	m_WorkBt[6]=(TextView)findViewById(R.id.tv_hw07);
    	m_WorkBt[7]=(TextView)findViewById(R.id.tv_hw08);
    	m_WorkBt[8]=(TextView)findViewById(R.id.tv_hw09);
    	m_WorkBt[9]=(TextView)findViewById(R.id.tv_hw10);
    	
    	for(int i=0;i<10;i++)
    	{
    		m_WorkBt[i].setVisibility(View.INVISIBLE);
    	}
    	
    	for(int i=0;i<this.m_iSumPickBtCount;i++)
		{
    		m_WorkBt[i].setEnabled(false);
    		m_WorkBt[i].setOnClickListener
			(
				new OnClickListener()
			    {
					public void onClick(View v) 
					{
						tv_hwcode.setText("");
						mViewSupData(v);
						
					}
			    }
			);
        }

		//向下翻页事件
		btDown.setOnClickListener
				(
						new OnClickListener()
						{
							public void onClick(View v)
							{
								mChangePickData(1);
							}
						}
				);
		//向上翻页事件
		btUp.setOnClickListener
				(
						new OnClickListener()
						{
							public void onClick(View v)
							{
								mChangePickData(-1);
							}
						}
				);

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
	    				tv_hwcode.setText(s);
	    				edtInput.setText("");  	
	    				//查询数据
	    				m_receMainTask=null;
						m_sQuery=tv_hwcode.getText().toString();
						
						tv_hwcode.setEnabled(false);
						tv_hwcode.setBackgroundResource(R.drawable.clrunenabled);
						
						bt_scan.setEnabled(false);
						bt_scan.setBackgroundResource(R.drawable.clrunenabled);
						
						mSendData(1);
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
					finish();
				}
		    }
        );
    	
    	bt_hwtz.setOnClickListener
    	(
			new OnClickListener()
		    {
				public void onClick(View v) 
				{
					mViewUi(1);
				}
		    }
        );
    	
    	bt_scan.setOnClickListener
    	(
			new OnClickListener()
		    {
				public void onClick(View v) 
				{
					m_receMainTask=null;
					m_sQuery=tv_hwcode.getText().toString();
					if (m_sQuery==null) return;
					if (m_sQuery.length()==0) return;
					
					bt_scan.setEnabled(false);
					bt_scan.setBackgroundResource(R.drawable.clrunenabled);	
					
					mSendData(1);
				}
		    }
        );
    	
    	tv_hwcode.setOnClickListener
    	(
			new OnClickListener()
		    {
				public void onClick(View v) 
				{
					mCharKeys();
				}
		    }
        );

		tv_realsl.setOnClickListener
				(
						new OnClickListener()
						{
							public void onClick(View v)
							{
								numCharKeys();
							}
						}
				);
    	
    	bt_save.setOnClickListener
    	(
			new OnClickListener()
		    {
				public void onClick(View v) 
				{
					if(tv_realsl.getText().toString().length()==0)
					{
						ShowWarningMessage("请输入该货位商品实际数量");
						return;
					}
					m_RealNum=tv_realsl.getText().toString();
					m_sQuery=tv_hwcode.getText().toString();
					mSendData(2);
					int iCurbtIndex=m_iCurBtIndex;
			    	int iCurWrIndex=m_iCurWorkIndex;
			    	m_iCurBtIndex=-1;
			    	m_iUnWorkCount--;
			    	
			    	if (m_iUnWorkCount<=0)
					{
			    		mShowMessage("盘点作业全部，请继续其他作业");
			    		return;
					}
					
					mAutoMoveNextHw(iCurbtIndex,iCurWrIndex);
				}
		    }
        );
	}

	//向下翻页事件


	private void mViewUi(int _UiNo)
	{
		switch(_UiNo)
		{
		case 1:
			m_acthwtz = new Intent(this,actPositionMove.class);
			startActivityForResult(m_acthwtz,4);
		}
	}
	
	private void mAutoMoveNextHw(int _CurBtIndex,int _CurWrIndex)
	{		
		//删除已经做完的任务
		m_SupTaskList.remove(_CurWrIndex);
		tv_realsl.setText("");
		m_iCurReceCount=0;
	    m_iPickTaskCount=0;
		m_iUnWorkCount=0;
		m_iCurBtIndex=-1;
		
		m_iPickTaskCount=m_SupTaskList.size();
		m_iUnWorkCount=m_iPickTaskCount;
		
		if(m_iCurMinIndex>=m_iPickTaskCount)
		{
			m_iCurMinIndex=m_iCurMinIndex-10;
		}
				
		clsSupTask _PickTask=new clsSupTask();
		int iCurIndex=m_iCurMinIndex;
		for(int i=0;i<m_iSumPickBtCount;i++)
		{
			if (iCurIndex<m_iPickTaskCount)
			{
				_PickTask=m_SupTaskList.get(iCurIndex);
				m_WorkBt[i].setVisibility(View.VISIBLE);
				this.m_WorkBt[i].setText(_PickTask._sPickHw);
				this.m_WorkBt[i].setTag(iCurIndex);
				m_WorkBt[i].setEnabled(true);
				if (_PickTask._iDoStatus==0)
				{
					mSetPickColor(i,iCurIndex);
				}
				else
					m_WorkBt[i].setBackgroundResource(R.drawable.clrendboxback);
				this.m_iCurMaxIndex=iCurIndex;
				iCurIndex++;
			}
			else
			{
				m_WorkBt[i].setVisibility(View.INVISIBLE);
			}
		}
			

		if (m_iCurWorkIndex>m_iCurMaxIndex)
		{
			m_iCurWorkIndex--;
			if (_CurBtIndex==0)
				_CurBtIndex=9;
			else
				_CurBtIndex--;
		}
		
		mViewSupData(m_WorkBt[_CurBtIndex]);
						
	}
	
	private void mSetPickColor(int _iIndex,int _iTaskIndex)
    {
    	if (m_iCurWorkIndex==_iTaskIndex)
    		m_WorkBt[_iIndex].setBackgroundResource(R.drawable.clrcurworkhw);
    	else
    		m_WorkBt[_iIndex].setBackgroundResource(R.drawable.clrhwselect);
    	
    }
	
	private void mViewSupData(View v)
	{
		if (m_iPickTaskCount==0) return;
    	
		clsSupTask _SupTask=new clsSupTask();
    	int tag = (Integer) v.getTag();
    	_SupTask=m_SupTaskList.get(tag);
    	
    	tv_hwcode.setText(_SupTask._sPickHw);
    	tv_sl.setText(_SupTask._sJhsl);
		tv_chinaname.setText(_SupTask._sArtName); //商品名称		
		tv_Manu.setText(_SupTask._sArtCj);  //生产厂家		
		tv_ypgg.setText(_SupTask._sArtGg);  //药品规格		
		tv_spph.setText(_SupTask._sArtPh);  //药品批号		
		tv_bzdw.setText(_SupTask._sArtBzdw);   //药品包装单位数量		
		tv_yxDate.setText(_SupTask._sArtDate); //有效期
		
		if (m_iCurBtIndex>-1)
    	{
    		m_WorkBt[m_iCurBtIndex].setBackgroundResource(R.drawable.clrhwselect);
    	}
		
    	if (_SupTask._iDoStatus==0)
    	{
    		v.setBackgroundResource(R.drawable.clrcurworkhw);
    		m_iCurBtIndex=tag%m_iSumPickBtCount;
    		m_iCurWorkIndex=tag;
    	}
    	else
    	{
    		v.setBackgroundResource(R.drawable.clrendboxback);
    	}
	}
	
	private void mVewPickData()
	{
		int iCurIndex=0;
		clsSupTask _PickTask=new clsSupTask();

		iCurIndex=0;
		this.m_iCurMaxIndex=-1;
		this.m_iCurMinIndex=0;

		for(int i=0;i<m_iSumPickBtCount;i++)
		{
			if (iCurIndex<this.m_iPickTaskCount)
			{
				_PickTask=m_SupTaskList.get(iCurIndex);
				m_WorkBt[i].setVisibility(View.VISIBLE);
				this.m_WorkBt[i].setText(_PickTask._sPickHw);
				this.m_WorkBt[i].setTag(iCurIndex);
				m_WorkBt[i].setEnabled(true);
				if (_PickTask._iDoStatus==0)
    				mSetPickColor(i,iCurIndex);
				else
					m_WorkBt[i].setBackgroundResource(R.drawable.clrendboxback);
				iCurIndex++;
				this.m_iCurMaxIndex++;
			}
			else
			{
				m_WorkBt[i].setVisibility(View.INVISIBLE);
			}
		}
	}
	private void mChangePickData(int _Type)
	{
		int iCurIndex=0;
		clsSupTask _PickTask=new clsSupTask();
		if (_Type==0)
		{
			iCurIndex=0;
			this.m_iCurMaxIndex=-1;
			this.m_iCurMinIndex=0;
			btDown.setEnabled(false);
			btUp.setEnabled(false);
			btDown.setBackgroundResource(R.drawable.clrunenabled);
			btUp.setBackgroundResource(R.drawable.clrunenabled);
			for(int i=0;i<m_iSumPickBtCount;i++)
			{
				if (iCurIndex<this.m_iPickTaskCount)
				{
					_PickTask=m_SupTaskList.get(iCurIndex);
					m_WorkBt[i].setVisibility(View.VISIBLE);
					this.m_WorkBt[i].setText(_PickTask._sPickHw);
					this.m_WorkBt[i].setTag(iCurIndex);
					m_WorkBt[i].setEnabled(true);
					if (_PickTask._iDoStatus==0)
						mSetPickColor(i,iCurIndex);
					else
						m_WorkBt[i].setBackgroundResource(R.drawable.clrendboxback);
					iCurIndex++;
					this.m_iCurMaxIndex++;
				}
				else
				{
					m_WorkBt[i].setVisibility(View.INVISIBLE);
				}
			}
			if ((m_iPickTaskCount-1)>m_iCurMaxIndex)
			{
				btDown.setEnabled(true);
				btDown.setBackgroundResource(R.drawable.clsbtnselect);
			}
		}
		else if(_Type==1)
		{
			//向下翻页
			if(m_iCurMaxIndex==(m_iPickTaskCount-1)) return;

			m_iCurMinIndex=m_iCurMaxIndex+1;
			iCurIndex=m_iCurMinIndex;
			for(int i=0;i<m_iSumPickBtCount;i++)
			{
				if (iCurIndex<this.m_iPickTaskCount)
				{
					_PickTask=m_SupTaskList.get(iCurIndex);
					m_WorkBt[i].setVisibility(View.VISIBLE);
					this.m_WorkBt[i].setText(_PickTask._sPickHw);
					this.m_WorkBt[i].setTag(iCurIndex);
					m_WorkBt[i].setEnabled(true);
					if (_PickTask._iDoStatus==0)
						mSetPickColor(i,iCurIndex);
					else
						m_WorkBt[i].setBackgroundResource(R.drawable.clrendboxback);
					iCurIndex++;
					this.m_iCurMaxIndex++;
				}
				else
				{
					m_WorkBt[i].setVisibility(View.INVISIBLE);
				}
			}
			if ((m_iPickTaskCount-1)>m_iCurMaxIndex)
			{
				btDown.setEnabled(true);
				btDown.setBackgroundResource(R.drawable.clsbtnselect);
			}
			else
			{
				btDown.setEnabled(false);
				btDown.setBackgroundResource(R.drawable.clrunenabled);
			}
			btUp.setEnabled(true);
			btUp.setBackgroundResource(R.drawable.clsbtnselect);
		}
		else if (_Type==-1)
		{
			if(m_iCurMinIndex==0) return;
			m_iCurMaxIndex=m_iCurMinIndex-1;
			iCurIndex=m_iCurMaxIndex;
			for(int i=(m_iSumPickBtCount-1);i>=0;i--)
			{
				_PickTask=m_SupTaskList.get(iCurIndex);
				m_WorkBt[i].setVisibility(View.VISIBLE);
				this.m_WorkBt[i].setText(_PickTask._sPickHw);
				this.m_WorkBt[i].setTag(iCurIndex);
				m_WorkBt[i].setEnabled(true);
				if (_PickTask._iDoStatus==0)
				{
					mSetPickColor(i,iCurIndex);
				}
				else
					m_WorkBt[i].setBackgroundResource(R.drawable.clrendboxback);
				this.m_iCurMinIndex=iCurIndex;
				iCurIndex--;
			}
			if (m_iCurMinIndex==0)
			{
				btUp.setEnabled(false);
				btUp.setBackgroundResource(R.drawable.clrunenabled);
			}
			else
			{
				btUp.setEnabled(true);
				btUp.setBackgroundResource(R.drawable.clsbtnselect);
			}
			if ((m_iPickTaskCount-1)>m_iCurMaxIndex)
			{
				btDown.setEnabled(true);
				btDown.setBackgroundResource(R.drawable.clsbtnselect);
			}
			else
			{
				btDown.setEnabled(false);
				btDown.setBackgroundResource(R.drawable.clrunenabled);
			}
		}
	}
	
	private void mDoReturnData(Message msg)
	{	
		try
		{
			String sReceData="",sFunNo="",sRet1="";
			sReceData=(String)msg.obj;
			sFunNo=clsMyPublic.GetStrInOfStr(sReceData, 0);
			if (sFunNo.compareTo(clsMyPublic.g_sktParamHwDp)==0)
			{			
				sRet1=clsMyPublic.GetStrInOfStr(sReceData, 1);
				if (sRet1.compareTo("-1")==0||sRet1.compareTo("0")==0)
				{
					tv_MessTs.setText(clsMyPublic.GetStrInOfStr(sReceData,2));
					bt_scan.setBackgroundResource(R.drawable.clsbtnselect);
					bt_scan.setEnabled(true);
					mSetButtonIni();
					return;
				}
				else if (sRet1.compareTo("-2")==0)
				{	
					tv_MessTs.setText(clsMyPublic.GetStrInOfStr(sReceData,2));
				}
				else
				{	
					tv_MessTs.setText(sReceData);
					
					m_iPickTaskCount=Integer.parseInt(sRet1);
					m_iUnWorkCount=m_iPickTaskCount;
					clsSupTask _SupTask=new clsSupTask();
					
					_SupTask._sPickHw=clsMyPublic.GetStrInOfStr(sReceData,2);
					_SupTask._sArtName=clsMyPublic.GetStrInOfStr(sReceData,4);
					_SupTask._sArtGg=clsMyPublic.GetStrInOfStr(sReceData,5);
					_SupTask._sArtBzdw=clsMyPublic.GetStrInOfStr(sReceData,6);
					_SupTask._sArtCj=clsMyPublic.GetStrInOfStr(sReceData, 8);
					_SupTask._sArtPh=clsMyPublic.GetStrInOfStr(sReceData,9);
					_SupTask._sJhsl=clsMyPublic.GetStrInOfStr(sReceData, 10);
					_SupTask._sArtDate=clsMyPublic.GetStrInOfStr(sReceData,11)+" ~ "+clsMyPublic.GetStrInOfStr(sReceData,12);
					
					
					m_SupTaskList.add(_SupTask);
					btUp.setEnabled(true);
					btDown.setEnabled(true);
					m_iCurReceCount++;	
					
					if (m_iCurReceCount==m_iPickTaskCount)
					{					
						mVewPickData();
						mViewSupData(m_WorkBt[0]);   //指哪扫哪 被动补货默认第一个箱子
						
						bt_scan.setBackgroundResource(R.drawable.clsbtnselect);
						bt_scan.setEnabled(true);
					}			
				}						
			}
			else if(sFunNo.compareTo(clsMyPublic.g_sktParamDpQr)==0)
			{
				sRet1=clsMyPublic.GetStrInOfStr(sReceData, 1);
				if (sRet1.compareTo("-1")==0||sRet1.compareTo("0")==0)
				{
					tv_MessTs.setText(clsMyPublic.GetStrInOfStr(sReceData,2));
					bt_scan.setBackgroundResource(R.drawable.clsbtnselect);
					bt_scan.setEnabled(true);
					mSetButtonIni();
					return;
				}
				else if(sRet1.compareTo("-2")==0)
				{
					tv_MessTs.setText(clsMyPublic.GetStrInOfStr(sReceData,2));
				}
				else if(sRet1.compareTo("1")==0)
				{
					tv_MessTs.setText("该货位实际数量已录入");
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
	
	
	private  void mShowMessage(String _sMess)
	{
		try
		{
			new AlertDialog.Builder(actCheckStock.this)
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
		readMessage=readMessage.trim();
		tv_hwcode.setText(readMessage);
		m_sQuery=readMessage;
		m_receMainTask=null;
		mSendData(1);
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
		clsMyPublic.m_sKeys=tv_hwcode.getText().toString();
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
		    						m_receMainTask=null;
		    						m_sQuery=tv_hwcode.getText().toString();
		    						if (m_sQuery==null) return;
		    						if (m_sQuery.length()==0) return;
		    						mSendData(1);
		    						bt_scan.setEnabled(false);
		    						bt_scan.setBackgroundResource(R.drawable.clrunenabled);	
		    					    
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
		        						tv_hwcode.setText(clsMyPublic.m_sKeys);
		        					}
		    						return;
		    					}
		    					else if (sKey.compareTo("清除")==0)
		    					{
		    						clsMyPublic.m_sKeys="";
		    						tv_hwcode.setText("");
		    						return;
		    					}
		    					else if(sKey.compareTo("取消")==0)
		    					{
		    						adNumKey.cancel();
		    						return;
		    					}
		    					clsMyPublic.m_sKeys+=sKey.toUpperCase();
		    					tv_hwcode.setText(clsMyPublic.m_sKeys);
		    				}
		    		    }
			    	);
				}
			}
	    }		
		adNumKey.show();		
	}
	private void numCharKeys()
	{
		LinearLayout loginLayout = (LinearLayout) getLayoutInflater().inflate(
				R.layout.actnumkeys, null);
		final AlertDialog adNumKey=
				new AlertDialog.Builder(this)
						.setView(loginLayout).create();//.setTitle("键盘")
		Window win=adNumKey.getWindow();
		WindowManager.LayoutParams lp=win.getAttributes();
		lp.x=0;
		lp.y=200;
		win.setAttributes(lp);
		clsMyPublic.m_sKeys="";
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
													tv_realsl.setText(clsMyPublic.m_sKeys);
												}
												return;
											}
											else if (sKey.compareTo("清除")==0)
											{
												clsMyPublic.m_sKeys="";
												tv_realsl.setText("");
												return;
											}
											else if(sKey.compareTo("取消")==0)
											{
												adNumKey.cancel();
												return;
											}
											clsMyPublic.m_sKeys+=sKey;
											tv_realsl.setText(clsMyPublic.m_sKeys);

										}
									}
							);
				}
			}
		}

		adNumKey.show();
	}
	public  void ShowWarningMessage(String _sMess)
	{
		new AlertDialog.Builder(actCheckStock.this)
				.setTitle("温馨提示")
				.setIcon(android.R.drawable.alert_dark_frame)
				//.setMessage(_sMess+"\n确定要退出本界面吗?")
				.setMessage(_sMess)
				//.setNegativeButton("取消", new DialogInterface.OnClickListener()
				//		{
				//			public void onClick(DialogInterface dialog, int which)
				//			{

				//			}
				//		}
				//).setPositiveButton("确定", new DialogInterface.OnClickListener()
				.setPositiveButton("确定", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				//finish();
			}
		}).show();
	}
}

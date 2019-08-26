using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Net;
using System.Net.Sockets;
using System.Threading;
using System.Windows.Forms;
using System.Data;

namespace WServer
{
    //定义类型
    public struct _tSocketCln
    {
        public int _SocketIndex;    //Socket 索引编号
        public string _ClnIP;       //客户端IP地址
        public string _FunType;     //功能类型
        public string _FunMark;     //功能说明
        public int _CnntStatus;     //连接状态 0 未连接 1连接成功
        public string _IPNO;        //注册表中的IP编号
        public int _SelfIndex;      //自身在数组的位置
    }


    public struct _socketThread
    {
        public Thread _clnThread;
        public Socket _clnSocket;
        public int _DoStatus;
        public string _IpAddress;
        public string _sbLx;
        public string _sbName;
        public string _sbNo;
    }                                

    //Socket发送数据
    public struct _tDoSktData
    {
        public string _DoData; //发送数据
        public int    _ClnIndex; //客户端索引编号
    }
    //打印机设置
    public struct _tSetPrnData
    {
        public string _sAreaNo;
        public string _IPNO;   //
    }
    class clsServerSocket
    {
        //定义客户端
        public static Socket m_ServerSocket = null;
        private string m_sClsName = "clsServerSocket";
        //线程定义
        Thread mThreadServerLsn = null;  //服务端监听线程

        //获取本地IP地址
        //定义事件
        public delegate void DisCmdMess(int _Type, string _Data);
        public event DisCmdMess OnDisCmdMess;


        private static _socketThread[] m_clnSocket;
        private static int m_iCurSocketIndex = -1;            


        private string mGetLocalIP(string _IP)
        {
            string sMess = null;
            try
            {
                string hostName = System.Net.Dns.GetHostName();
                System.Net.IPHostEntry ipEntry = System.Net.Dns.GetHostEntry(hostName);        
                //ip地址列表
                System.Net.IPAddress[] addr = ipEntry.AddressList;
                for (int i = 0; i < addr.GetLength(0); i++)
                {
                    string IPAddress = addr[i].ToString();
                    if (IPAddress == _IP) 
                        return IPAddress;
                }
                sMess = "物理IP与系统设置IP不匹配，请联系系统管理员";
                MyPublic.gWriteLog(MyPublic.g_LogErr, m_sClsName, "mGetLocalIP", sMess);
                OnDisCmdMess(1,sMess);
                return null;
            }
            catch (System.Exception ex)
            {
                sMess = ex.Message.ToString();
                MyPublic.gWriteLog(MyPublic.g_LogErr, m_sClsName, "mGetLocalIP", sMess);
                return null;
            }
        }

        //初始化数据
        public int mIniSocketData()
        {
            string sMess = null;
            try
            {
                m_clnSocket = new _socketThread[MyPublic.m_SysTreadCount];
                for (int i = 0; i < MyPublic.m_SysTreadCount; i++)
                {
                    m_clnSocket[i]._clnThread = null;
                    m_clnSocket[i]._clnSocket = null;
                    m_clnSocket[i]._DoStatus = 0;
                }

                mThreadServerLsn = new Thread(new ThreadStart(mCreateServerSocketListen));
                mThreadServerLsn.Start();
             return 0;
            }
            catch (System.Exception ex)
            {
                sMess = ex.Message.ToString();
                MyPublic.gWriteLog(MyPublic.g_LogErr, m_sClsName, "mIniSocketData", sMess);
                return -1;
            }         
        }
        //查找可用的Socket
        private int mFindNullSocket()
        {
            string sMess = null;
            try
            {
                for (int i = 0; i < m_clnSocket.GetLength(0); i++)
                {
                    if (m_clnSocket[i]._DoStatus == 0)
                    {
                        m_clnSocket[i]._clnSocket = null;
                        m_clnSocket[i]._clnThread = null;
                        return i;
                    }
                }
                return -1;
            }
            catch (System.Exception ex)
            {
                sMess = ex.Message.ToString();
                MyPublic.gWriteLog(MyPublic.g_LogErr, m_sClsName, "mFindNullSocket", sMess);
                return -1;
            }
        }
        //创建Socket服务端监听
        private void mCreateServerSocketListen()
        {
            string sMess = null;
            try
            {
                IPAddress ip = IPAddress.Parse(MyPublic.m_LocalIP);
                IPEndPoint ipe_ctrl = new IPEndPoint(ip, MyPublic.m_LocalPort);
                m_ServerSocket = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
                m_ServerSocket.Bind(ipe_ctrl);
                m_ServerSocket.Listen(MyPublic.m_SysTreadCount);
                sMess = "Socket服务监听创建成功;本地;IP【" + MyPublic.m_LocalIP + "】;端口号【" + MyPublic.m_LocalPort.ToString() + "】";
                OnDisCmdMess(MyPublic.g_LogStatus,sMess);
                MyPublic.gWriteLog(MyPublic.g_LogSkt, "Socket通信", "Socket通信", sMess);
                while(1==1)
                {
                    m_iCurSocketIndex = mFindNullSocket();
                    if (m_iCurSocketIndex == -1) 
                    {
                        sMess = "没有可用的连接Socket,请联系系统管理员.";
                        MyPublic.gWriteLog(MyPublic.g_LogErr, m_sClsName, "mCreateServerSocketListen", sMess);
                        MyPublic.gWriteLog(MyPublic.g_LogSkt, "Socket通信", "Socket通信", sMess);
                        OnDisCmdMess(MyPublic.g_LogErr, sMess);
                        Thread.Sleep(1000);
                        continue;
                    }
                    m_clnSocket[m_iCurSocketIndex]._clnSocket = m_ServerSocket.Accept();

                    //获取客户端的IP地址
                    string sClnIP = null;
                    sClnIP = ((System.Net.IPEndPoint)m_clnSocket[m_iCurSocketIndex]._clnSocket.RemoteEndPoint).Address.ToString();
                    //启动客户端线程
                    m_clnSocket[m_iCurSocketIndex]._DoStatus = 1;
                    m_clnSocket[m_iCurSocketIndex]._IpAddress = sClnIP;
                    m_clnSocket[m_iCurSocketIndex]._clnThread = new Thread(new ThreadStart(mCreateConnectClientData));
                    m_clnSocket[m_iCurSocketIndex]._clnThread.Start();

                    sMess = string.Format("客户端：{0};线程【{1}】;连接成功;", sClnIP, m_iCurSocketIndex);
                    OnDisCmdMess(MyPublic.g_LogStatus,sMess);
                    Thread.Sleep(100);
                }
            }
            catch (System.Exception ex)
            {
                sMess = ex.Message.ToString();
                sMess = "严重的系统问题;Socket服务监听创建失败或者运行中出错;" + ex.Message;
                OnDisCmdMess(MyPublic.g_LogErr,sMess);
                MyPublic.gWriteLog(MyPublic.g_LogErr, m_sClsName, "mCreateServerSocketListen", sMess);

            }
        }
        //创建Socket服务端监听
        private void mCreateConnectClientData()
        {
            string sMess = "";
            int _iCurIndex = m_iCurSocketIndex;
            try
            {
                int iRet = -1;
                string _sMainReceData = "";
                m_clnSocket[_iCurIndex]._clnSocket.ReceiveTimeout = 10000;
                while(1==1)
                {
                    byte[] _receData = new byte[40960];
                    byte[] _sendData = null;
                    int iReceLength = 0;
                    int _iStartPos = -1;
                    string _sDoWorkData = "";
                    try
                    {
                        iReceLength = m_clnSocket[_iCurIndex]._clnSocket.Receive(_receData);
                        if (iReceLength == 0)
                        {
                            sMess = string.Format("客户端：{0};线程【{1}】;结束工作;接受为空;", m_clnSocket[_iCurIndex]._IpAddress, m_iCurSocketIndex);
                            OnDisCmdMess(MyPublic.g_LogStatus, sMess);
                            Thread.Sleep(100);
                            m_clnSocket[_iCurIndex]._DoStatus = 0;
                            m_clnSocket[_iCurIndex]._clnSocket.Close();
                            return;
                        }

                        string sReceData = null;
                        sReceData = Encoding.Default.GetString(_receData, 0, iReceLength);
                        int iDelPos = -1;
                        iDelPos = sReceData.IndexOf(Convert.ToChar(10)); //删除/n
                        if (iDelPos >= 0)
                            sReceData = sReceData.Remove(iDelPos, 1);

                        sMess = string.Format("客户端：{0};接受数据：{1}", m_clnSocket[_iCurIndex]._IpAddress, sReceData);
                        OnDisCmdMess(MyPublic.g_LogSkt, sMess);
                        MyPublic.gWriteLog(MyPublic.g_LogSkt, "Socket通信", "Socket通信", sMess);
                        _sMainReceData += sReceData;

                        _iStartPos = _sMainReceData.IndexOf(MyPublic.g_sktDataEndChar);
                        if (_iStartPos > 0)
                        {
                            _sDoWorkData = _sMainReceData.Substring(0, _iStartPos + 3);
                            clsOutput _clsWCps = new clsOutput();
                            string[] _sResult = null;
                            _sResult = _clsWCps.mDoCPSWorkData(_sDoWorkData);
                            for (int i = 0; i < _sResult.GetLength(0); i++)
                            {
                                _sResult[i] += MyPublic.g_sktDataEndChar;
                                _sendData = Encoding.Default.GetBytes(_sResult[i]);
                                iRet = m_clnSocket[_iCurIndex]._clnSocket.Send(_sendData, _sendData.Length, 0);
                                sMess = string.Format("客户端：{0};发送状态：{1};数据:{2}", m_clnSocket[_iCurIndex]._IpAddress, iRet,_sResult[i]);
                                OnDisCmdMess(MyPublic.g_LogSkt, sMess);
                                MyPublic.gWriteLog(MyPublic.g_LogSkt, "Socket通信", "Socket通信", sMess);
                                Thread.Sleep(50);
                            }
                        }
                        sMess = string.Format("客户端：{0};线程【{1}】;结束工作;正常结束;", m_clnSocket[_iCurIndex]._IpAddress, m_iCurSocketIndex);
                        OnDisCmdMess(MyPublic.g_LogStatus, sMess);

                        m_clnSocket[_iCurIndex]._DoStatus = 0;
                        m_clnSocket[_iCurIndex]._clnSocket.Close();
                        return;

                    }
                    catch (System.Exception ex)
                    {
                        sMess = string.Format("客户端：{0};线程【{1}】;结束工作;接受超时;", m_clnSocket[_iCurIndex]._IpAddress, m_iCurSocketIndex);
                        OnDisCmdMess(MyPublic.g_LogStatus, sMess);
                        m_clnSocket[_iCurIndex]._DoStatus = 0;
                        m_clnSocket[_iCurIndex]._clnSocket.Close();
                        sMess = string.Format("客户端：{0};{1}", m_clnSocket[_iCurIndex]._IpAddress, ex.Message.ToString());
                        OnDisCmdMess(MyPublic.g_LogErr, sMess);
                        return;
                    }
                }
            }
            catch (System.Exception ex)
            {
                sMess = string.Format("客户端：{0};线程【{1}】;结束工作;异常退出;", m_clnSocket[_iCurIndex]._IpAddress, m_iCurSocketIndex);
                OnDisCmdMess(MyPublic.g_LogStatus, sMess);
                sMess = ex.Message.ToString();
                MyPublic.gWriteLog(MyPublic.g_LogErr, m_sClsName, "mCreateConnectClientData", sMess);
                OnDisCmdMess(MyPublic.g_LogErr, sMess);
            }
        }
        private void clsServerDisDoDataCmdMess(int _Type, string _Data)
        {
            OnDisCmdMess(_Type, _Data);
        }
        
       
        //关闭线程
        public void mCloseThread()
        {
            string sMess = null;
            try
            {
                mThreadServerLsn.Abort();
                m_ServerSocket.Shutdown(0);
                m_ServerSocket.Close();
            }
            catch (System.Exception ex)
            {
                sMess = ex.Message.ToString();
                MyPublic.gWriteLog(MyPublic.g_LogErr, m_sClsName, "mCloseThread", sMess);
            }
        }

    }
}

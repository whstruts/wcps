using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Drawing;
using System.Data;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Runtime.InteropServices;
using System.Security.Cryptography;
using System.IO;

namespace WServer
{

    class MyPublic
    {

        public static string m_AppExePath = null;
        public static int m_AppRunSucess = 0;  //默认系统启动失败
        //***************************************************************************************//
        public static string m_IniPath = null;
        //***************************************************************************************
        public static string m_DbWMSUserID = null;   //数据库用户编号
        public static string m_DbWMSUserPWD = null;  //数据库用户密码
        public static string m_DbWMSServer = null;   //数据库服务
        public static string m_DbWCSUserID = null;   //数据库用户编号
        public static string m_DbWCSUserPWD = null;  //数据库用户密码
        public static string m_DbWCSServer = null;   //数据库服务
        public static string m_DbWCSCnntStr = null;  //数据库连接字符串
        public static string m_DbLMISCnntStr = null;  //数据库连接字符串



        //***************************************************************************************///
        public static string m_SysUserID = null;   //系统用户
        public static string m_SysUserPwd = null;  //系统用户密码
        public static string m_SysUserSave = null; //系统用户信息是否保存
        //***************************************************************************************//
        public static string m_SysGroupNo = null;  //服务端分组编号
        public static int m_SysTreadCount = 0;     //处理数据线程数量
        public static string m_LocalIP = null;     //服务端本地IP地址
        public static int m_LocalPort = 0;      //服务端本地端口号
        public static string g_SysCation = "";

        public static int m_LoginSuccess = 0;      //0 登录失败或者未登录 1 登录成功
        //********************
        const string KEY_64 = "98765432";//注意了，是8个字符，64位    
        const string IV_64 = "23456789";

        private static string m_sClsName = "MyPublic";
        public static string g_sktDataEndChar = "@^@";

        public static string g_SktParamLogin = "PBS0001";  //登陆
        public static string g_SktParamExit = "PBS0002";   //退出
        public static string g_SktParamMainTask = "PBS0003";     //查看任务
        public static string g_SktParamPickTask = "PBS0004";     //发起拣货作业任务
        public static string g_SktParamQRPick = "PBS0005";       //拣货确认作业
        public static string g_SktParamSQPick = "PBS0006";       //索取拣货作业任务
        public static string g_SktParamMatch = "PBS0007";        //关联周转箱作业任务
        public static string g_SktParamSQZDBh = "PBS0008";       //索取主动补货作业任务
        public static string g_SktParamQRZDBH = "PBS0009";       //确认主动补货作业任务
        public static string g_SktParamCXPick = "PBS0010";       //查询拣货作业明细
        public static string g_sktParamSQWrkType = "PSB0011";    //索取作业类型
        public static string g_sktParamSFWGL = "PSB0012";        //判断是否有未关联的任务
        public static string g_sktParamCXWork = "PSB0013";       //查询作业记录
        public static string g_sktParamCXStock = "PSB0014";      //查询库存
        public static string g_sktParamFpdGroup = "PSB0015";   //分配单组
        public static string g_sktParamBhActiv = "PSB0016";      //补货条码激活
        public static string g_sktParamBhDanwei = "PSB0017";   //补货到位激活

        public static string g_sktParamHwPc = "PSB0018";      //货位盘查
        public static string g_sktParamHwTz = "PSB0019";      //货位调整
        public static string g_sktParamHwDp = "PSB0020";      //货位动盘
        public static string g_sktParamDpQr = "PSB0021";      //货位动盘

        public static string g_sktParamPrnData = "PSB9999";    //测试打印
        public static string g_sktParamPrnTest = "PSB9998";    //测试打印
        public static string g_sktParamLsZtData = "PSB9001";   //绿色自提打印
        public static string g_sktParamCnntState = "CPS0000";    //判断连线是否正常

        //********************************
        public const int g_LogStatus = 1;
        public const int g_LogDps = 2;
        public const int g_LogMcs = 3;
        public const int g_LogEcs = 4;
        public const int g_LogPda = 5;
        public const int g_LogCps = 6;
        public const int g_LogPrn = 7;
        public const int g_LogSkt = 8;
        public const int g_LogErr = 9;
        public const int g_LogTime = 10;


        [DllImport("kernel32")]
        private static extern long WritePrivateProfileString(string section, string key, string val, string filePath);
        [DllImport("kernel32")]
        private static extern int GetPrivateProfileString(string section, string key, string def, StringBuilder retVal, int size, string filePath);

        //初始化系统参数
        public static void gIniSysParam()
        {
            string sMess = null;
            try
            {
                m_AppExePath = Application.StartupPath;

                //读取ini文件路径
                m_LoginSuccess = 0;
                m_IniPath = Application.StartupPath + @"\WCSServer.ini";

                StringBuilder stmp = new StringBuilder(255);
                //数据库信息
                //WMS数据库
                GetPrivateProfileString("WMS_DB", "DB_SERVER_WMS", "", stmp, 255, m_IniPath);
                m_DbWMSServer = stmp.ToString();
                //WMS用户
                GetPrivateProfileString("WMS_DB", "DB_USERID_WMS", "", stmp, 255, m_IniPath);
                m_DbWMSUserID = stmp.ToString();
                GetPrivateProfileString("WMS_DB", "DB_USERPWD_WMS", "", stmp, 255, m_IniPath);
                m_DbWMSUserPWD = Decode(stmp.ToString());
                //WCS数据库
                GetPrivateProfileString("WCS_DB", "DB_SERVER_WCS", "", stmp, 255, m_IniPath);
                m_DbWCSServer = stmp.ToString();
                //WCS用户
                GetPrivateProfileString("WCS_DB", "DB_USERID_WCS", "", stmp, 255, m_IniPath);
                m_DbWCSUserID = stmp.ToString();
                GetPrivateProfileString("WCS_DB", "DB_USERPWD_WCS", "", stmp, 255, m_IniPath);
                m_DbWCSUserPWD = Decode(stmp.ToString());

                GetPrivateProfileString("SysSet", "SysGroup", "", stmp, 255, m_IniPath);
                m_SysGroupNo = stmp.ToString();
                GetPrivateProfileString("SysSet", "ThreadCount", "", stmp, 255, m_IniPath);
                m_SysTreadCount = int.Parse(stmp.ToString());


                GetPrivateProfileString("SysSet", "SvrIp", "", stmp, 255, m_IniPath);
                m_LocalIP = stmp.ToString();

                GetPrivateProfileString("SysSet", "SvrPort", "", stmp, 255, m_IniPath);
                m_LocalPort = int.Parse(stmp.ToString());

                //系统用户
                GetPrivateProfileString("SysSet", "UserID", "", stmp, 255, m_IniPath);
                m_SysUserID = stmp.ToString();
                GetPrivateProfileString("SysSet", "UserPwd", "", stmp, 255, m_IniPath);
                m_SysUserPwd = Decode(stmp.ToString());
                GetPrivateProfileString("SysSet", "SysCaption", "", stmp, 255, m_IniPath);
                g_SysCation = stmp.ToString();
            }
            catch (System.Exception ex)
            {
                sMess = ex.Message.ToString();
                MessageBox.Show(sMess);
            }
        }

        public static void gUpdateIniData()
        {
            string sMess = null;
            try
            {
                WritePrivateProfileString("WMS_DB", "DB_SERVER_WMS", m_DbWMSServer, m_IniPath);
                WritePrivateProfileString("WMS_DB", "DB_USERID_WMS", m_DbWMSUserID, m_IniPath);
                WritePrivateProfileString("WMS_DB", "DB_USERPWD_WMS", Encode(m_DbWMSUserPWD), m_IniPath);
                WritePrivateProfileString("SysSet", "SvrIp", m_LocalIP, m_IniPath);
                WritePrivateProfileString("SysSet", "SvrPort", m_LocalPort.ToString(), m_IniPath);
                WritePrivateProfileString("SysSet", "ThreadCount", m_SysTreadCount.ToString(), m_IniPath);
                WritePrivateProfileString("SysSet", "UserID", m_SysUserID, m_IniPath);
                WritePrivateProfileString("SysSet", "UserPwd", Encode(m_SysUserPwd), m_IniPath);
            }
            catch (System.Exception ex)
            {
                sMess = ex.Message.ToString();
                gWriteLog(g_LogErr, m_sClsName, "gUpdateIniData", sMess);
            }

        }

        public static void gSpreadString(string _str1, string _spr, int _len, string[] _Data)
        {
            string sMess = null;
            try
            {
                int iPos = -1;
                int i = 0;
                string sData = null;
                iPos = _str1.IndexOf(_spr, 0);
                while (iPos > 0)
                {
                    sData = _str1.Substring(0, iPos);
                    _Data[i] = sData;
                    _str1 = _str1.Remove(0, iPos + 1);
                    iPos = _str1.IndexOf(_spr, 0);
                    i++;
                    if (i >= _len) return;
                }
            }
            catch (System.Exception ex)
            {
                sMess = ex.Message.ToString();
                gWriteLog(g_LogErr, m_sClsName, "gSpreadString", sMess);
            }

        }
        public static string mGetStrInOfStr(string _str1, int _startIndex)
        {
            string sMess = "";
            try
            {
                int iPos = -1;
                int iCurIndex = -1;
                string stmpStr = _str1;
                string sRet = "";
                while (stmpStr.IndexOf(";") >= 0)
                {
                    iPos = stmpStr.IndexOf(";");
                    iCurIndex++;
                    if (iCurIndex == _startIndex)
                    {
                        sRet = stmpStr.Substring(0, iPos);
                        break;
                    }
                    stmpStr = stmpStr.Substring(iPos + 1);
                }
                return sRet;
            }
            catch (System.Exception ex)
            {
                sMess = ex.Message.ToString();
                gWriteLog(g_LogErr, m_sClsName, "mGetStrInOfStr", sMess);
                return "";
            }
        }
        public static void gWriteLog(int _LogType, string _ClsName, string _FunName, string _LogData)
        {
            string sMess = null;
            try
            {
                //******************
                //创建文件夹 按照年 年月 年月日创建
                DateTime dt = DateTime.Now;
                string sLogPath = null;
                //创建Log文件夹
                sLogPath = m_AppExePath + "//Log";
                if (!Directory.Exists(sLogPath))
                    Directory.CreateDirectory(sLogPath);
                //创建年文件夹
                sLogPath += ("//" + dt.Year.ToString());
                if (!Directory.Exists(sLogPath))
                    Directory.CreateDirectory(sLogPath);
                //创建月文件夹
                sLogPath += ("//" + dt.GetDateTimeFormats('y')[0].ToString().ToString());
                if (!Directory.Exists(sLogPath))
                    Directory.CreateDirectory(sLogPath);
                //创建月日文件
                sLogPath += ("//" + dt.GetDateTimeFormats('D')[0].ToString().ToString());
                if (!Directory.Exists(sLogPath))
                    Directory.CreateDirectory(sLogPath);
                //*******************************
                if (_LogType == g_LogErr)
                    //系统报错日志
                    sLogPath += "//sysError.txt";
                else if (_LogType == g_LogSkt)
                    sLogPath += "//sysSktData.txt";
                else if (_LogType == g_LogTime)
                    sLogPath += "//sysComfig.txt";
                else
                    sLogPath += "//sysError.txt";

                FileInfo f = new FileInfo(sLogPath);
                StreamWriter w = null;
                if (f.Exists == false)
                    w = f.CreateText();
                else
                    w = f.AppendText();
                sMess = dt.TimeOfDay.ToString();  //时间；
                sMess += ";";
                sMess += _ClsName;                //对象名称
                sMess += ";";
                sMess += _FunName;                //函数名称
                sMess += ";";
                sMess += _LogData;                //日志内容
                sMess += ";";
                w.WriteLine(sMess);
                w.Close();

            }
            catch (System.Exception ex)
            {
                sMess = ex.Message.ToString();
            }
        }
        //加密算法
        static public string Encode(string data)
        {
            byte[] byKey = System.Text.ASCIIEncoding.ASCII.GetBytes(KEY_64);
            byte[] byIV = System.Text.ASCIIEncoding.ASCII.GetBytes(IV_64);
            DESCryptoServiceProvider cryptoProvider = new DESCryptoServiceProvider();
            int i = cryptoProvider.KeySize;
            MemoryStream ms = new MemoryStream();
            CryptoStream cst = new CryptoStream(ms, cryptoProvider.CreateEncryptor(byKey, byIV), CryptoStreamMode.Write);
            StreamWriter sw = new StreamWriter(cst);
            sw.Write(data);
            sw.Flush();
            cst.FlushFinalBlock();
            sw.Flush();
            return Convert.ToBase64String(ms.GetBuffer(), 0, (int)ms.Length);
        }
        //解密算法
        static public string Decode(string data)
        {
            byte[] byKey = System.Text.ASCIIEncoding.ASCII.GetBytes(KEY_64);
            byte[] byIV = System.Text.ASCIIEncoding.ASCII.GetBytes(IV_64);
            byte[] byEnc;
            try
            {
                byEnc = Convert.FromBase64String(data);
            }
            catch
            {
                return null;
            }
            DESCryptoServiceProvider cryptoProvider = new DESCryptoServiceProvider();
            MemoryStream ms = new MemoryStream(byEnc);
            CryptoStream cst = new CryptoStream(ms, cryptoProvider.CreateDecryptor(byKey, byIV), CryptoStreamMode.Read);
            StreamReader sr = new StreamReader(cst);
            return sr.ReadToEnd();
        }

    }
}

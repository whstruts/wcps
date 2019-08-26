using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace WServer
{
    public partial class FrmLogin : Form
    {
        private int m_iSetLoginStatus = 0; //0 不要设置  1 正在设置
        private string m_sClsName = "FrmLogin";

        public static  clsMyAccDB m_LmisDB = new clsMyAccDB();

        public FrmLogin()
        {
            InitializeComponent();
        }

        private void FrmLogin_Load(object sender, EventArgs e)
        {
            mIniLogin();
        }
        private int mIniLogin()
        {
            string sMess = null;

            try
            {
                //初始界面大小
                this.Height = 206;
                this.Width = 366;
                //初始化数据库设置
                txt_wmsDbName.Text= MyPublic.m_DbWMSServer;
                txt_WMSDBUserID.Text = MyPublic.m_DbWMSUserID;
                txt_WMSDBUserPWD.Text = MyPublic.m_DbWMSUserPWD;
                txt_SvrIP.Text = MyPublic.m_LocalIP;
                txt_SvrPort.Text = MyPublic.m_LocalPort.ToString();
                txt_KeyCnntCount.Text = MyPublic.m_SysTreadCount.ToString();

                txt_SysUserID.Text = MyPublic.m_SysUserID;
                txt_SysUserPWD.Text = MyPublic.m_SysUserPwd;
                return 0;
            }
            catch (System.Exception ex)
            {
                sMess = ex.Message.ToString();
                MyPublic.gWriteLog(MyPublic.g_LogErr, m_sClsName, "mIniLogin", sMess);
                return 1;
            }
        }

        private void bt_Set_Click(object sender, EventArgs e)
        {
            if (m_iSetLoginStatus == 0)
            {
                this.Height = 404;
                this.Width = 376;
                m_iSetLoginStatus = 1;
                txt_wmsDbName.Focus();
            }
            else
            {
                this.Height = 208;
                this.Width = 366;
                m_iSetLoginStatus = 0;
                txt_SysUserID.Focus();
            }
        }

        private void bt_Exit_Click(object sender, EventArgs e)
        {

            this.Close();
        }

        public void bt_Ok_Click(object sender, EventArgs e)
        {
            string sMess = null;
            try
            {

                


                int iRet = 0;
                bt_Ok.Enabled = false;
                //WMS数据库信息
                txt_WMSDBUserID.Text = txt_WMSDBUserID.Text.Trim();
                txt_WMSDBUserPWD.Text = txt_WMSDBUserPWD.Text.Trim();
                txt_wmsDbName.Text=txt_wmsDbName.Text.Trim();

                if (txt_WMSDBUserPWD.Text.Length == 0 || txt_WMSDBUserID.Text.Length == 0 || txt_wmsDbName.Text.Length == 0 ||
                    txt_wmsDbName.Text.Length == 0 || txt_SvrPort.Text.Length == 0 || txt_KeyCnntCount.Text.Length == 0)
                {
                    lbl_Mess.Text = "WMS数据库信息输入不完整，请核对!";
                    txt_WMSDBUserID.Focus();
                    bt_Ok.Enabled = true;
                    return;
                }
                //WCS数据库信息
                txt_SvrIP.Text = txt_SvrIP.Text.Trim();
                txt_SvrPort.Text = txt_SvrPort.Text.Trim();
                txt_KeyCnntCount.Text = txt_KeyCnntCount.Text.Trim();
                if ( txt_wmsDbName.Text.Length == 0 || txt_SvrPort.Text.Length == 0 || txt_KeyCnntCount.Text.Length == 0)
                {
                    lbl_Mess.Text = "配置信息输入不完整，请核对!";
                    txt_SvrIP.Focus();
                    bt_Ok.Enabled = true;
                    return;
                }


                //系统用户信息
                txt_SysUserID.Text = txt_SysUserID.Text.Trim();
                txt_SysUserPWD.Text = txt_SysUserPWD.Text.Trim();


                if (txt_SysUserID.Text.Length == 0 ||
                    txt_SysUserPWD.Text.Length == 0)
                {
                    lbl_Mess.Text = "系统用户信息输入不完整，请核对!";
                    txt_WMSDBUserID.Focus();
                    bt_Ok.Enabled = true;
                    return;
                }

                //连接数据库wcs/wcs6mose@lwcs_2b
                lbl_Mess.Text = "系统正在连接数据库,请稍等...";

                MyPublic.m_LocalIP= txt_SvrIP.Text;
                MyPublic.m_LocalPort=int.Parse(txt_SvrPort.Text);
                MyPublic.m_SysTreadCount=int.Parse(txt_KeyCnntCount.Text);

                MyPublic.m_DbWMSUserID = txt_WMSDBUserID.Text;
                MyPublic.m_DbWMSServer = txt_wmsDbName.Text;
                MyPublic.m_DbWMSUserPWD = txt_WMSDBUserPWD.Text;
                MyPublic.m_DbLMISCnntStr = " user id=" + MyPublic.m_DbWMSUserID +
                                           ";data source=" + MyPublic.m_DbWMSServer +
                                           ";password=" + MyPublic.m_DbWMSUserPWD ;
                m_LmisDB.m_DbCnntStr = MyPublic.m_DbLMISCnntStr;
                iRet = m_LmisDB.mConnectDB(ref sMess);
                if (iRet != 0)
                {
                    lbl_Mess.Text = "系统连接LMIS数据库失败,请核对数据库信息!";
                    txt_WMSDBUserID.Focus();
                    bt_Ok.Enabled = true;
                    return;
                }
                lbl_Mess.Text = "系统连接LMIS数据库成功,正在验证用户信息...";
                //系统用户信息
                MyPublic.m_SysUserID = txt_SysUserID.Text;
                MyPublic.m_SysUserPwd = txt_SysUserPWD.Text;

                MyPublic.gUpdateIniData();

                //用户校验成功
                MyPublic.m_LoginSuccess = 1;
                this.Close();
            }
            catch (System.Exception ex)
            {
                sMess = ex.Message.ToString();
                MyPublic.gWriteLog(MyPublic.g_LogErr, m_sClsName, "bt_Ok_Click", sMess);
            }
        }

        private void gb_SetLogin_Enter(object sender, EventArgs e)
        {

        }
    }
}

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
    public partial class FrmMain : Form
    {
        private static string m_sClsName = "FrmMain";
        private clsServerSocket m_ServerSocket=null;
        delegate void mDoMessTask(int _Type, string _Data);
        private int m_iStart = 0;
        public FrmMain()
        {
            InitializeComponent();
        }

        //初始化List控件数据
        private void mIniListControlData()
        {
            string sMess = null;
            try
            {
                int iDateLen = 80;
                int iTimeLen = 120;
                int iDataLen = 600;
                //显示模式
                lst_Status.View = View.Details;
                lst_Status.FullRowSelect = true;
                lst_Status.MultiSelect = false;
                //创建标题
                lst_Status.Columns.Add("日期", iDateLen);
                lst_Status.Columns.Add("时间", iTimeLen);
                lst_Status.Columns.Add("描述", iDataLen);

                //显示模式
                lst_Dps.View = View.Details;
                lst_Dps.FullRowSelect = true;
                lst_Dps.MultiSelect = false;
                //创建标题
                lst_Dps.Columns.Add("日期", iDateLen);
                lst_Dps.Columns.Add("时间", iTimeLen);
                lst_Dps.Columns.Add("描述", iDataLen);


                //显示模式
                lst_Mcs.View = View.Details;
                lst_Mcs.FullRowSelect = true;
                lst_Mcs.MultiSelect = false;
                //创建标题
                lst_Mcs.Columns.Add("日期", iDateLen);
                lst_Mcs.Columns.Add("时间", iTimeLen);
                lst_Mcs.Columns.Add("描述", iDataLen);


                //显示模式
                lst_Ecs.View = View.Details;
                lst_Ecs.FullRowSelect = true;
                lst_Ecs.MultiSelect = false;
                //创建标题
                lst_Ecs.Columns.Add("日期", iDateLen);
                lst_Ecs.Columns.Add("时间", iTimeLen);
                lst_Ecs.Columns.Add("描述", iDataLen);



                //显示模式
                lst_Pda.View = View.Details;
                lst_Pda.FullRowSelect = true;
                lst_Pda.MultiSelect = false;
                //创建标题
                lst_Pda.Columns.Add("日期", iDateLen);
                lst_Pda.Columns.Add("时间", iTimeLen);
                lst_Pda.Columns.Add("描述", iDataLen);


                //显示模式
                lst_Prn.View = View.Details;
                lst_Prn.FullRowSelect = true;
                lst_Prn.MultiSelect = false;
                //创建标题
                lst_Prn.Columns.Add("日期", iDateLen);
                lst_Prn.Columns.Add("时间", iTimeLen);
                lst_Prn.Columns.Add("描述", iDataLen);


                //显示模式
                lst_cps.View = View.Details;
                lst_cps.FullRowSelect = true;
                lst_cps.MultiSelect = false;
                //创建标题
                lst_cps.Columns.Add("日期", iDateLen);
                lst_cps.Columns.Add("时间", iTimeLen);
                lst_cps.Columns.Add("描述", iDataLen);

                //显示模式
                lst_Skt.View = View.Details;
                lst_Skt.FullRowSelect = true;
                lst_Skt.MultiSelect = false;
                //创建标题
                lst_Skt.Columns.Add("日期", iDateLen);
                lst_Skt.Columns.Add("时间", iTimeLen);
                lst_Skt.Columns.Add("描述", 1024);

                //显示模式
                lst_Err.View = View.Details;
                lst_Err.FullRowSelect = true;
                lst_Err.MultiSelect = false;
                //创建标题
                lst_Err.Columns.Add("日期", iDateLen);
                lst_Err.Columns.Add("时间", iTimeLen);
                lst_Err.Columns.Add("描述", iDataLen);
            }
            catch (System.Exception ex)
            {
                sMess = ex.Message.ToString();
                MyPublic.gWriteLog(-1, m_sClsName, "mIniListControlData", sMess);
            }
        }
        //添加List控件显示数据
        private void mAddListData(int _lstIndex,string _Data)
        {
            string sMess = null;
            try
            {
                string sDate = null;
                string sTime = null;
                DateTime dt = DateTime.Now;
                sDate = dt.ToShortDateString().ToString();
                sTime = dt.TimeOfDay.ToString();
                ListViewItem lvItem;
                ListViewItem.ListViewSubItem lvSubItem;
                lvItem = new ListViewItem();
                lvItem.Text = sDate;
                lvSubItem = new ListViewItem.ListViewSubItem();
                lvSubItem.Text = sTime;
                lvItem.SubItems.Add(lvSubItem);
                lvSubItem = new ListViewItem.ListViewSubItem();
                lvSubItem.Text = _Data;
                lvItem.SubItems.Add(lvSubItem);
                switch (_lstIndex)
                {
                    case MyPublic.g_LogStatus:
                        //运行状态
                        lst_Status.Items.Insert(0,lvItem);
                        lst_Status.Items[0].Selected = true;
                        if (lst_Status.Items.Count==200)
                            lst_Status.Items.RemoveAt(199);
                        break;
                    case MyPublic.g_LogDps:
                        //DPS日志
                        lst_Dps.Items.Insert(0,lvItem);
                        lst_Dps.Items[0].Selected = true;
                        if (lst_Dps.Items.Count == 200)
                            lst_Dps.Items.RemoveAt(199);
                        break;
                    case MyPublic.g_LogMcs:
                        lst_Mcs.Items.Insert(0,lvItem);
                        lst_Mcs.Items[0].Selected = true;
                        if (lst_Mcs.Items.Count == 200)
                            lst_Mcs.Items.RemoveAt(199);
                        break;
                    case MyPublic.g_LogEcs:
                        lst_Ecs.Items.Insert(0,lvItem);
                        lst_Ecs.Items[0].Selected = true;
                        if (lst_Ecs.Items.Count == 200)
                            lst_Ecs.Items.RemoveAt(199);
                        break;
                    case MyPublic.g_LogPda:
                        lst_Pda.Items.Insert(0,lvItem);
                        lst_Pda.Items[0].Selected = true;
                        if (lst_Pda.Items.Count == 200)
                            lst_Pda.Items.RemoveAt(199);
                        break;
                    case MyPublic.g_LogCps:
                        lst_cps.Items.Insert(0,lvItem);
                        lst_cps.Items[0].Selected = true;
                        if (lst_cps.Items.Count == 200)
                            lst_cps.Items.RemoveAt(199);
                        break;
                    case MyPublic.g_LogPrn:
                        lst_Prn.Items.Insert(0,lvItem);
                        lst_Prn.Items[0].Selected = true;
                        if (lst_Prn.Items.Count == 200)
                            lst_Prn.Items.RemoveAt(199);
                        break;
                    case MyPublic.g_LogSkt:
                        lst_Skt.Items.Insert(0,lvItem);
                        lst_Skt.Items[0].Selected = true;
                        if (lst_Skt.Items.Count == 200)
                            lst_Skt.Items.RemoveAt(199);
                        break;
                    case MyPublic.g_LogErr:
                        lst_Err.Items.Insert(0,lvItem);
                        lst_Err.Items[0].Selected = true;
                        if (lst_Err.Items.Count == 200)
                            lst_Err.Items.RemoveAt(199);                       
                        break;
                }


            }
            catch (System.Exception ex)
            {
                sMess = ex.Message.ToString();
                MyPublic.gWriteLog(-1, m_sClsName, "mAddListData", sMess);
            }
        }
        //初始化树控件数据
        private void mIniTreeViewData()
        {
            string sMess = "";
            try
            {
                string[] sFun = new string[9];
                sFun[0] = "【运行状态】";
                sFun[1] = "【DPS 日志】";
                sFun[2] = "【MCS 日志】";
                sFun[3] = "【ECS 日志】";
                sFun[4] = "【PDA 日志】";
                sFun[5] = "【CPS 日志】";
                sFun[6] = "【打印日志】";
                sFun[7] = "【通信日志】";
                sFun[8] = "【异常日志】";

                TreeNode rootNode = new TreeNode("【全部功能】");
                Tv_Fun.Nodes.Add(rootNode);
                for (int i = 0; i < sFun.GetLength(0); i++)
                {
                    TreeNode MyNode2 = new TreeNode(sFun[i]);
                    rootNode.Nodes.Add(MyNode2);
                }
                rootNode.ExpandAll();
            }
            catch (System.Exception ex)
            {
                sMess = ex.Message.ToString();
                MyPublic.gWriteLog(-1, m_sClsName, "mIniTreeViewData", sMess);
            }
        }
        private void FrmMain_Load(object sender, EventArgs e)
        {
            string sMess = null;
            try
            {
                this.Text = MyPublic.g_SysCation;
                mIniListControlData();
                sMess = "系统需要加载数据，请确保硬件环境到位,然后点击【启动】";
                mAddListData(MyPublic.g_LogStatus, sMess);
                mIniTreeViewData();
                lst_Status.Visible = true;
                lst_Dps.Visible = false;
                lst_Mcs.Visible = false;
                lst_Ecs.Visible = false;
                lst_Pda.Visible = false;
                lst_cps.Visible = false;
                lst_Prn.Visible = false;
                lst_Skt.Visible = false;
                lst_Err.Visible = false;
            }
            catch (System.Exception ex)
            {
                sMess = ex.Message.ToString();
                MyPublic.gWriteLog(-1, m_sClsName, "FrmMain_Load", sMess);
            }
            
        }


        private void ts_Start_Click(object sender, EventArgs e)
        {
            string sMess = null;
            try
            {

                //clsOutput _clsWCps = new clsOutput();
                //string[] _sResult = null;
                //string _sDoWorkData = "";
                //_sResult = _clsWCps.mDoCPSWorkData(_sDoWorkData);


                m_ServerSocket = new clsServerSocket();
                m_ServerSocket.OnDisCmdMess += new clsServerSocket.DisCmdMess(FrmMain_OnDisCmdMess);
                m_ServerSocket.mIniSocketData();
                ts_Start.Enabled = false;
                m_iStart = 1;
            }
            catch (System.Exception ex)
            {
                sMess = ex.Message.ToString();
                MyPublic.gWriteLog(-1, m_sClsName, "bt_Start_Click", sMess);
            }
        }

        private void ts_Reg_Click(object sender, EventArgs e)
        {
            //FrmServer mFrmServer = new FrmServer();
            //mFrmServer.ShowDialog();
            //mFrmServer.Dispose();
            //mFrmServer = null;
        }

        private void ts_Exit_Click(object sender, EventArgs e)
        {
            if (m_iStart == 1)
            {
                m_ServerSocket.mCloseThread();
            }
            this.Close();
        }

        private void FrmMain_OnDisCmdMess(int _Type, string _Data)
        {
            Invoke(new mDoMessTask(mAddListData), new object[] { _Type, _Data });
        }

        private void Tv_Fun_AfterSelect(object sender, TreeViewEventArgs e)
        {
            string sCurNoteText = "";
            sCurNoteText=Tv_Fun.SelectedNode.Text;
            if (sCurNoteText=="【运行状态】")
            {             
                lst_Status.Visible = true;
                lst_Dps.Visible=false;
                lst_Mcs.Visible = false;
                lst_Ecs.Visible = false;
                lst_Pda.Visible = false;
                lst_cps.Visible = false;
                lst_Prn.Visible = false;
                lst_Skt.Visible = false;
                lst_Err.Visible = false;
            }
            else if (sCurNoteText=="【DPS 日志】")
            {
                lst_Status.Visible = false;
                lst_Dps.Visible = true;
                lst_Mcs.Visible = false;
                lst_Ecs.Visible = false;
                lst_Pda.Visible = false;
                lst_cps.Visible = false;
                lst_Prn.Visible = false;
                lst_Skt.Visible = false;
                lst_Err.Visible = false;
            }
            else if (sCurNoteText == "【MCS 日志】")
            {
                lst_Status.Visible = false;
                lst_Dps.Visible = false;
                lst_Mcs.Visible = true;
                lst_Ecs.Visible = false;
                lst_Pda.Visible = false;
                lst_cps.Visible = false;
                lst_Prn.Visible = false;
                lst_Skt.Visible = false;
                lst_Err.Visible = false;
            }
            else if (sCurNoteText == "【ECS 日志】")
            {
                lst_Status.Visible = false;
                lst_Dps.Visible = false;
                lst_Mcs.Visible = false;
                lst_Ecs.Visible = true;
                lst_Pda.Visible = false;
                lst_cps.Visible = false;
                lst_Prn.Visible = false;
                lst_Skt.Visible = false;
                lst_Err.Visible = false;
            }
            else if (sCurNoteText == "【CPS 日志】")
            {
                lst_Status.Visible = false;
                lst_Dps.Visible = false;
                lst_Mcs.Visible = false;
                lst_Ecs.Visible = false;
                lst_Pda.Visible = false;
                lst_cps.Visible = true;
                lst_Prn.Visible = false;
                lst_Skt.Visible = false;
                lst_Err.Visible = false;
            }
            else if (sCurNoteText == "【PDA 日志】")
            {
                lst_Status.Visible = false;
                lst_Dps.Visible = false;
                lst_Mcs.Visible = false;
                lst_Ecs.Visible = false;
                lst_Pda.Visible = true;
                lst_cps.Visible = false;
                lst_Prn.Visible = false;
                lst_Skt.Visible = false;
                lst_Err.Visible = false;
            }
            else if (sCurNoteText == "【打印日志】")
            {
                lst_Status.Visible = false;
                lst_Dps.Visible = false;
                lst_Mcs.Visible = false;
                lst_Ecs.Visible = false;
                lst_Pda.Visible = false;
                lst_cps.Visible = false;
                lst_Prn.Visible = true;
                lst_Skt.Visible = false;
                lst_Err.Visible = false;
            }
            else if (sCurNoteText == "【通信日志】")
            {
                lst_Status.Visible = false;
                lst_Dps.Visible = false;
                lst_Mcs.Visible = false;
                lst_Ecs.Visible = false;
                lst_Pda.Visible = false;
                lst_cps.Visible = false;
                lst_Prn.Visible = false;
                lst_Skt.Visible = true;
                lst_Err.Visible = false;
            }
            else if (sCurNoteText == "【异常日志】")
            {
                lst_Status.Visible = false;
                lst_Dps.Visible = false;
                lst_Mcs.Visible = false;
                lst_Ecs.Visible = false;
                lst_Pda.Visible = false;
                lst_cps.Visible = false;
                lst_Prn.Visible = false;
                lst_Skt.Visible = false;
                lst_Err.Visible = true;
            }


        }

        private void ts_Prn_Click(object sender, EventArgs e)
        {
        }
        private void FrmMain_FormClosing(object sender, FormClosingEventArgs e)
        {
            if (MessageBox.Show("您确定要退出系统吗，\r请确认是否影响其他系统的正常运行?", "温馨提示",
                                MessageBoxButtons.OKCancel, MessageBoxIcon.Question, MessageBoxDefaultButton.Button2)
                             == DialogResult.Cancel)
            {
                e.Cancel = true;
            }
        }
    }
}

using System;
using System.Collections.Generic;
using System.Linq;
using System.Windows.Forms;
using System.Diagnostics;

namespace WServer
{
    static class Program
    {
        /// <summary>
        /// 应用程序的主入口点。
        /// </summary>
        [STAThread]
        static void Main()
        {
            MyPublic.gIniSysParam();
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);
            FrmLogin mFrmLogin = new FrmLogin();
            mFrmLogin.ShowDialog();
            mFrmLogin.Dispose();
            mFrmLogin = null;
            if (MyPublic.m_LoginSuccess == 0) return;
            Application.Run(new FrmMain());
            Process.GetCurrentProcess().Kill();
        }
    }
}

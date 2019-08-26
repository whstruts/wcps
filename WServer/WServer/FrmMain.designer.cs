namespace WServer
{
    partial class FrmMain
    {
        /// <summary>
        /// 必需的设计器变量。
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// 清理所有正在使用的资源。
        /// </summary>
        /// <param name="disposing">如果应释放托管资源，为 true；否则为 false。</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows 窗体设计器生成的代码

        /// <summary>
        /// 设计器支持所需的方法 - 不要
        /// 使用代码编辑器修改此方法的内容。
        /// </summary>
        private void InitializeComponent()
        {
            this.components = new System.ComponentModel.Container();
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(FrmMain));
            this.Tv_Fun = new System.Windows.Forms.TreeView();
            this.imageList1 = new System.Windows.Forms.ImageList(this.components);
            this.sta_Bar = new System.Windows.Forms.StatusStrip();
            this.lst_Status = new System.Windows.Forms.ListView();
            this.toolStrip1 = new System.Windows.Forms.ToolStrip();
            this.ts_Start = new System.Windows.Forms.ToolStripButton();
            this.ts_Reg = new System.Windows.Forms.ToolStripButton();
            this.ts_Prn = new System.Windows.Forms.ToolStripButton();
            this.ts_Exit = new System.Windows.Forms.ToolStripButton();
            this.lst_cps = new System.Windows.Forms.ListView();
            this.lst_Dps = new System.Windows.Forms.ListView();
            this.lst_Mcs = new System.Windows.Forms.ListView();
            this.lst_Ecs = new System.Windows.Forms.ListView();
            this.lst_Prn = new System.Windows.Forms.ListView();
            this.lst_Err = new System.Windows.Forms.ListView();
            this.lst_Skt = new System.Windows.Forms.ListView();
            this.lst_Pda = new System.Windows.Forms.ListView();
            this.toolStrip1.SuspendLayout();
            this.SuspendLayout();
            // 
            // Tv_Fun
            // 
            this.Tv_Fun.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(192)))), ((int)(((byte)(192)))), ((int)(((byte)(255)))));
            this.Tv_Fun.ImageIndex = 0;
            this.Tv_Fun.ImageList = this.imageList1;
            this.Tv_Fun.Location = new System.Drawing.Point(1, 61);
            this.Tv_Fun.Name = "Tv_Fun";
            this.Tv_Fun.SelectedImageIndex = 1;
            this.Tv_Fun.Size = new System.Drawing.Size(178, 432);
            this.Tv_Fun.TabIndex = 5;
            this.Tv_Fun.AfterSelect += new System.Windows.Forms.TreeViewEventHandler(this.Tv_Fun_AfterSelect);
            // 
            // imageList1
            // 
            this.imageList1.ImageStream = ((System.Windows.Forms.ImageListStreamer)(resources.GetObject("imageList1.ImageStream")));
            this.imageList1.TransparentColor = System.Drawing.Color.Transparent;
            this.imageList1.Images.SetKeyName(0, "FOLDER.ICO");
            this.imageList1.Images.SetKeyName(1, "folder open.ico");
            // 
            // sta_Bar
            // 
            this.sta_Bar.Location = new System.Drawing.Point(0, 515);
            this.sta_Bar.Name = "sta_Bar";
            this.sta_Bar.Size = new System.Drawing.Size(990, 22);
            this.sta_Bar.TabIndex = 6;
            this.sta_Bar.Text = "statusStrip1";
            // 
            // lst_Status
            // 
            this.lst_Status.BackColor = System.Drawing.Color.White;
            this.lst_Status.ForeColor = System.Drawing.Color.Blue;
            this.lst_Status.Location = new System.Drawing.Point(182, 60);
            this.lst_Status.Name = "lst_Status";
            this.lst_Status.Size = new System.Drawing.Size(808, 432);
            this.lst_Status.TabIndex = 7;
            this.lst_Status.UseCompatibleStateImageBehavior = false;
            // 
            // toolStrip1
            // 
            this.toolStrip1.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(192)))), ((int)(((byte)(192)))), ((int)(((byte)(255)))));
            this.toolStrip1.ImageScalingSize = new System.Drawing.Size(32, 32);
            this.toolStrip1.Items.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.ts_Start,
            this.ts_Reg,
            this.ts_Prn,
            this.ts_Exit});
            this.toolStrip1.Location = new System.Drawing.Point(0, 0);
            this.toolStrip1.Name = "toolStrip1";
            this.toolStrip1.Size = new System.Drawing.Size(990, 56);
            this.toolStrip1.TabIndex = 8;
            this.toolStrip1.Text = "toolStrip1";
            // 
            // ts_Start
            // 
            this.ts_Start.Image = ((System.Drawing.Image)(resources.GetObject("ts_Start.Image")));
            this.ts_Start.ImageTransparentColor = System.Drawing.Color.Magenta;
            this.ts_Start.Name = "ts_Start";
            this.ts_Start.Size = new System.Drawing.Size(51, 53);
            this.ts_Start.Text = "启动[&S]";
            this.ts_Start.TextImageRelation = System.Windows.Forms.TextImageRelation.ImageAboveText;
            this.ts_Start.ToolTipText = "启动[&S]";
            this.ts_Start.Click += new System.EventHandler(this.ts_Start_Click);
            // 
            // ts_Reg
            // 
            this.ts_Reg.Image = ((System.Drawing.Image)(resources.GetObject("ts_Reg.Image")));
            this.ts_Reg.ImageTransparentColor = System.Drawing.Color.Magenta;
            this.ts_Reg.Name = "ts_Reg";
            this.ts_Reg.Size = new System.Drawing.Size(52, 53);
            this.ts_Reg.Text = "注册[&R]";
            this.ts_Reg.TextImageRelation = System.Windows.Forms.TextImageRelation.ImageAboveText;
            this.ts_Reg.ToolTipText = "注册";
            this.ts_Reg.Visible = false;
            this.ts_Reg.Click += new System.EventHandler(this.ts_Reg_Click);
            // 
            // ts_Prn
            // 
            this.ts_Prn.Image = ((System.Drawing.Image)(resources.GetObject("ts_Prn.Image")));
            this.ts_Prn.ImageTransparentColor = System.Drawing.Color.Magenta;
            this.ts_Prn.Name = "ts_Prn";
            this.ts_Prn.Size = new System.Drawing.Size(52, 53);
            this.ts_Prn.Text = "打印[&R]";
            this.ts_Prn.TextImageRelation = System.Windows.Forms.TextImageRelation.ImageAboveText;
            this.ts_Prn.ToolTipText = "注册";
            this.ts_Prn.Visible = false;
            this.ts_Prn.Click += new System.EventHandler(this.ts_Prn_Click);
            // 
            // ts_Exit
            // 
            this.ts_Exit.Image = ((System.Drawing.Image)(resources.GetObject("ts_Exit.Image")));
            this.ts_Exit.ImageTransparentColor = System.Drawing.Color.Magenta;
            this.ts_Exit.Name = "ts_Exit";
            this.ts_Exit.Size = new System.Drawing.Size(51, 53);
            this.ts_Exit.Text = "退出[&T]";
            this.ts_Exit.TextImageRelation = System.Windows.Forms.TextImageRelation.ImageAboveText;
            this.ts_Exit.Click += new System.EventHandler(this.ts_Exit_Click);
            // 
            // lst_cps
            // 
            this.lst_cps.BackColor = System.Drawing.Color.White;
            this.lst_cps.ForeColor = System.Drawing.Color.Blue;
            this.lst_cps.Location = new System.Drawing.Point(182, 60);
            this.lst_cps.Name = "lst_cps";
            this.lst_cps.Size = new System.Drawing.Size(808, 432);
            this.lst_cps.TabIndex = 9;
            this.lst_cps.UseCompatibleStateImageBehavior = false;
            // 
            // lst_Dps
            // 
            this.lst_Dps.BackColor = System.Drawing.Color.White;
            this.lst_Dps.ForeColor = System.Drawing.Color.Blue;
            this.lst_Dps.Location = new System.Drawing.Point(182, 60);
            this.lst_Dps.Name = "lst_Dps";
            this.lst_Dps.Size = new System.Drawing.Size(808, 432);
            this.lst_Dps.TabIndex = 10;
            this.lst_Dps.UseCompatibleStateImageBehavior = false;
            // 
            // lst_Mcs
            // 
            this.lst_Mcs.BackColor = System.Drawing.Color.White;
            this.lst_Mcs.ForeColor = System.Drawing.Color.Blue;
            this.lst_Mcs.Location = new System.Drawing.Point(182, 60);
            this.lst_Mcs.Name = "lst_Mcs";
            this.lst_Mcs.Size = new System.Drawing.Size(808, 432);
            this.lst_Mcs.TabIndex = 11;
            this.lst_Mcs.UseCompatibleStateImageBehavior = false;
            // 
            // lst_Ecs
            // 
            this.lst_Ecs.BackColor = System.Drawing.Color.White;
            this.lst_Ecs.ForeColor = System.Drawing.Color.Blue;
            this.lst_Ecs.Location = new System.Drawing.Point(182, 60);
            this.lst_Ecs.Name = "lst_Ecs";
            this.lst_Ecs.Size = new System.Drawing.Size(808, 432);
            this.lst_Ecs.TabIndex = 12;
            this.lst_Ecs.UseCompatibleStateImageBehavior = false;
            // 
            // lst_Prn
            // 
            this.lst_Prn.BackColor = System.Drawing.Color.White;
            this.lst_Prn.ForeColor = System.Drawing.Color.Blue;
            this.lst_Prn.Location = new System.Drawing.Point(182, 60);
            this.lst_Prn.Name = "lst_Prn";
            this.lst_Prn.Size = new System.Drawing.Size(808, 432);
            this.lst_Prn.TabIndex = 13;
            this.lst_Prn.UseCompatibleStateImageBehavior = false;
            // 
            // lst_Err
            // 
            this.lst_Err.BackColor = System.Drawing.Color.White;
            this.lst_Err.ForeColor = System.Drawing.Color.Blue;
            this.lst_Err.Location = new System.Drawing.Point(182, 60);
            this.lst_Err.Name = "lst_Err";
            this.lst_Err.Size = new System.Drawing.Size(808, 432);
            this.lst_Err.TabIndex = 14;
            this.lst_Err.UseCompatibleStateImageBehavior = false;
            // 
            // lst_Skt
            // 
            this.lst_Skt.BackColor = System.Drawing.Color.White;
            this.lst_Skt.ForeColor = System.Drawing.Color.Blue;
            this.lst_Skt.Location = new System.Drawing.Point(182, 60);
            this.lst_Skt.Name = "lst_Skt";
            this.lst_Skt.Size = new System.Drawing.Size(808, 432);
            this.lst_Skt.TabIndex = 15;
            this.lst_Skt.UseCompatibleStateImageBehavior = false;
            // 
            // lst_Pda
            // 
            this.lst_Pda.BackColor = System.Drawing.Color.White;
            this.lst_Pda.ForeColor = System.Drawing.Color.Blue;
            this.lst_Pda.Location = new System.Drawing.Point(182, 60);
            this.lst_Pda.Name = "lst_Pda";
            this.lst_Pda.Size = new System.Drawing.Size(808, 432);
            this.lst_Pda.TabIndex = 16;
            this.lst_Pda.UseCompatibleStateImageBehavior = false;
            // 
            // FrmMain
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 12F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(192)))), ((int)(((byte)(192)))), ((int)(((byte)(255)))));
            this.ClientSize = new System.Drawing.Size(990, 537);
            this.Controls.Add(this.lst_Pda);
            this.Controls.Add(this.lst_Skt);
            this.Controls.Add(this.lst_Err);
            this.Controls.Add(this.lst_Prn);
            this.Controls.Add(this.lst_Ecs);
            this.Controls.Add(this.lst_Mcs);
            this.Controls.Add(this.lst_Dps);
            this.Controls.Add(this.lst_cps);
            this.Controls.Add(this.toolStrip1);
            this.Controls.Add(this.lst_Status);
            this.Controls.Add(this.sta_Bar);
            this.Controls.Add(this.Tv_Fun);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedSingle;
            this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
            this.MaximizeBox = false;
            this.Name = "FrmMain";
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "WCS系统服务端";
            this.Load += new System.EventHandler(this.FrmMain_Load);
            this.FormClosing += new System.Windows.Forms.FormClosingEventHandler(this.FrmMain_FormClosing);
            this.toolStrip1.ResumeLayout(false);
            this.toolStrip1.PerformLayout();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.TreeView Tv_Fun;
        private System.Windows.Forms.StatusStrip sta_Bar;
        private System.Windows.Forms.ListView lst_Status;
        private System.Windows.Forms.ImageList imageList1;
        private System.Windows.Forms.ToolStrip toolStrip1;
        private System.Windows.Forms.ToolStripButton ts_Start;
        private System.Windows.Forms.ToolStripButton ts_Reg;
        private System.Windows.Forms.ToolStripButton ts_Exit;
        private System.Windows.Forms.ListView lst_cps;
        private System.Windows.Forms.ListView lst_Dps;
        private System.Windows.Forms.ListView lst_Mcs;
        private System.Windows.Forms.ListView lst_Ecs;
        private System.Windows.Forms.ListView lst_Prn;
        private System.Windows.Forms.ListView lst_Err;
        private System.Windows.Forms.ListView lst_Skt;
        private System.Windows.Forms.ListView lst_Pda;
        private System.Windows.Forms.ToolStripButton ts_Prn;
    }
}


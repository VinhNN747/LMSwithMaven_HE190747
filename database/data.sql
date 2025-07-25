
USE [LeaveManagementAssignment]
GO
/****** Object:  Table [dbo].[Division]    Script Date: 17/06/2025 01:53:41 CH ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Division](
	[DivisionID] [int] IDENTITY(1,1) NOT NULL,
	[DivisionName] [nvarchar](50) NOT NULL,
	[DivisionHead] [varchar](10) NULL,
 CONSTRAINT [PK_Division] PRIMARY KEY CLUSTERED 
(
	[DivisionID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[LeaveRequest]    Script Date: 17/06/2025 01:53:41 CH ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[LeaveRequest](
	[LeaveRqID] [varchar](20) NOT NULL,
	[SenderID] [varchar](10) NOT NULL,
	[ApproverID] [varchar](10) NOT NULL,
	[Reason] [nvarchar](max) NULL,
	[Status] [varchar](20) NOT NULL,
 CONSTRAINT [PK_LeaveRequest] PRIMARY KEY CLUSTERED 
(
	[LeaveRqID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[User]    Script Date: 17/06/2025 01:53:41 CH ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[User](
	[UserID] [varchar](10) NOT NULL,
	[FullName] [nvarchar](100) NOT NULL,
	[Username] [varchar](50) NOT NULL,
	[Email] [varchar](100) NOT NULL,
	[Gender] [varchar](1) NULL,
	[DivisionID] [int] NULL,
	[Role] [nvarchar](100) NOT NULL,
	[IsActive] [bit] NOT NULL,
	[ManagerID] [varchar](10) NULL,
 CONSTRAINT [PK_User] PRIMARY KEY CLUSTERED 
(
	[UserID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
SET IDENTITY_INSERT [dbo].[Division] ON 

INSERT [dbo].[Division] ([DivisionID], [DivisionName], [DivisionHead]) VALUES (2, N'IT', N'LG001')
INSERT [dbo].[Division] ([DivisionID], [DivisionName], [DivisionHead]) VALUES (3, N'MKT', N'CP001')
INSERT [dbo].[Division] ([DivisionID], [DivisionName], [DivisionHead]) VALUES (4, N'Sales', NULL)
INSERT [dbo].[Division] ([DivisionID], [DivisionName], [DivisionHead]) VALUES (5, N'Accountant', N'AG001')
INSERT [dbo].[Division] ([DivisionID], [DivisionName], [DivisionHead]) VALUES (6, N'HR', N'TS001')
INSERT [dbo].[Division] ([DivisionID], [DivisionName], [DivisionHead]) VALUES (7, N'Customer Service', NULL)
INSERT [dbo].[Division] ([DivisionID], [DivisionName], [DivisionHead]) VALUES (8, N'Operations', NULL)
INSERT [dbo].[Division] ([DivisionID], [DivisionName], [DivisionHead]) VALUES (9, N'Legal', NULL)
INSERT [dbo].[Division] ([DivisionID], [DivisionName], [DivisionHead]) VALUES (10, N'Quality Assurance ', NULL)
INSERT [dbo].[Division] ([DivisionID], [DivisionName], [DivisionHead]) VALUES (11, N'Security', NULL)
INSERT [dbo].[Division] ([DivisionID], [DivisionName], [DivisionHead]) VALUES (12, N'Business Development', NULL)
SET IDENTITY_INSERT [dbo].[Division] OFF
GO
INSERT [dbo].[User] ([UserID], [FullName], [Username], [Email], [Gender], [DivisionID], [Role], [IsActive], [ManagerID]) VALUES (N'AG001', N'Ariana Grande', N'ari_g', N'ari_g.pop@gmail.com', N'F', 5, N'Head', 1, NULL)
INSERT [dbo].[User] ([UserID], [FullName], [Username], [Email], [Gender], [DivisionID], [Role], [IsActive], [ManagerID]) VALUES (N'BE001', N'Billie Eilish', N'billz', N'billz.altpop@gmail.com', N'F', 5, N'Employee', 1, N'AG001')
INSERT [dbo].[User] ([UserID], [FullName], [Username], [Email], [Gender], [DivisionID], [Role], [IsActive], [ManagerID]) VALUES (N'BM001', N'Bruno Mars', N'bruno123', N'brunomars123@gmail.com', N'M', 5, N'Employee', 1, N'AG001')
INSERT [dbo].[User] ([UserID], [FullName], [Username], [Email], [Gender], [DivisionID], [Role], [IsActive], [ManagerID]) VALUES (N'CP001', N'Charlie Puth', N'charlie123', N'charlieputh123@gmail.com', N'M', 3, N'Head', 1, NULL)
INSERT [dbo].[User] ([UserID], [FullName], [Username], [Email], [Gender], [DivisionID], [Role], [IsActive], [ManagerID]) VALUES (N'ES001', N'Ed Sheeran', N'edsheeran123', N'ed321@gmail.com', N'M', 3, N'Employee', 1, N'CP001')
INSERT [dbo].[User] ([UserID], [FullName], [Username], [Email], [Gender], [DivisionID], [Role], [IsActive], [ManagerID]) VALUES (N'JB001', N'Justin Bieber', N'jbiebs', N'jbiebs.official@gmail.com', N'M', 3, N'Employee', 1, N'CP001')
INSERT [dbo].[User] ([UserID], [FullName], [Username], [Email], [Gender], [DivisionID], [Role], [IsActive], [ManagerID]) VALUES (N'LG001', N'Lady Gaga', N'ladygaga123', N'ladygaga@gmail.com', N'F', 2, N'Head', 1, NULL)
INSERT [dbo].[User] ([UserID], [FullName], [Username], [Email], [Gender], [DivisionID], [Role], [IsActive], [ManagerID]) VALUES (N'MJ001', N'Michael Jackson', N'mj75', N'mj345@gmail.com', N'M', 2, N'Employee', 1, N'LG001')
INSERT [dbo].[User] ([UserID], [FullName], [Username], [Email], [Gender], [DivisionID], [Role], [IsActive], [ManagerID]) VALUES (N'SG001', N'Selena Gomez', N'selly', N'selly.beats@gmail.com', N'F', 2, N'Employee', 1, N'LG001')
INSERT [dbo].[User] ([UserID], [FullName], [Username], [Email], [Gender], [DivisionID], [Role], [IsActive], [ManagerID]) VALUES (N'TS001', N'Taylor Swift', N'taytay', N'taytay.music@gmail.com', N'F', 6, N'Head', 1, NULL)
INSERT [dbo].[User] ([UserID], [FullName], [Username], [Email], [Gender], [DivisionID], [Role], [IsActive], [ManagerID]) VALUES (N'TW001', N'The Weeknd', N'weeky', N'weeky.wave@gmail.com', N'M', 6, N'Employee', 1, N'TS001')
INSERT [dbo].[User] ([UserID], [FullName], [Username], [Email], [Gender], [DivisionID], [Role], [IsActive], [ManagerID]) VALUES (N'VN001', N'Vinh Nguyen', N'vinh', N'vinhngoc172005@gmail.com', N'M', 6, N'Employee', 1, N'TS001')
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [UK_User_Email]    Script Date: 17/06/2025 01:53:41 CH ******/
ALTER TABLE [dbo].[User] ADD  CONSTRAINT [UK_User_Email] UNIQUE NONCLUSTERED 
(
	[Email] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [UK_User_Username]    Script Date: 17/06/2025 01:53:41 CH ******/
ALTER TABLE [dbo].[User] ADD  CONSTRAINT [UK_User_Username] UNIQUE NONCLUSTERED 
(
	[Username] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
ALTER TABLE [dbo].[LeaveRequest] ADD  DEFAULT ('Inprogress') FOR [Status]
GO
ALTER TABLE [dbo].[User] ADD  DEFAULT ('Employee') FOR [Role]
GO
ALTER TABLE [dbo].[User] ADD  DEFAULT ((1)) FOR [IsActive]
GO
ALTER TABLE [dbo].[Division]  WITH CHECK ADD  CONSTRAINT [FK_Division_Head] FOREIGN KEY([DivisionHead])
REFERENCES [dbo].[User] ([UserID])
GO
ALTER TABLE [dbo].[Division] CHECK CONSTRAINT [FK_Division_Head]
GO
ALTER TABLE [dbo].[LeaveRequest]  WITH CHECK ADD  CONSTRAINT [FK_LeaveRequest_Approver] FOREIGN KEY([ApproverID])
REFERENCES [dbo].[User] ([UserID])
GO
ALTER TABLE [dbo].[LeaveRequest] CHECK CONSTRAINT [FK_LeaveRequest_Approver]
GO
ALTER TABLE [dbo].[LeaveRequest]  WITH CHECK ADD  CONSTRAINT [FK_LeaveRequest_Sender] FOREIGN KEY([SenderID])
REFERENCES [dbo].[User] ([UserID])
GO
ALTER TABLE [dbo].[LeaveRequest] CHECK CONSTRAINT [FK_LeaveRequest_Sender]
GO
ALTER TABLE [dbo].[User]  WITH CHECK ADD  CONSTRAINT [FK_User_Division] FOREIGN KEY([DivisionID])
REFERENCES [dbo].[Division] ([DivisionID])
GO
ALTER TABLE [dbo].[User] CHECK CONSTRAINT [FK_User_Division]
GO
ALTER TABLE [dbo].[User]  WITH CHECK ADD  CONSTRAINT [FK_User_Manager] FOREIGN KEY([ManagerID])
REFERENCES [dbo].[User] ([UserID])
GO
ALTER TABLE [dbo].[User] CHECK CONSTRAINT [FK_User_Manager]
GO
ALTER TABLE [dbo].[Division]  WITH CHECK ADD  CONSTRAINT [CHK_DivisionName] CHECK  (([DivisionName]<>''))
GO
ALTER TABLE [dbo].[Division] CHECK CONSTRAINT [CHK_DivisionName]
GO
ALTER TABLE [dbo].[LeaveRequest]  WITH CHECK ADD  CONSTRAINT [CHK_Status] CHECK  (([Status]='Rejected' OR [Status]='Approved' OR [Status]='Inprogress'))
GO
ALTER TABLE [dbo].[LeaveRequest] CHECK CONSTRAINT [CHK_Status]
GO
ALTER TABLE [dbo].[User]  WITH CHECK ADD  CONSTRAINT [CHK_Gender] CHECK  (([Gender]='F' OR [Gender]='M'))
GO
ALTER TABLE [dbo].[User] CHECK CONSTRAINT [CHK_Gender]
GO
ALTER TABLE [dbo].[User]  WITH CHECK ADD  CONSTRAINT [CHK_Role] CHECK  (([Role]='Head' OR [Role]='Lead' OR [Role]='Employee'))
GO
ALTER TABLE [dbo].[User] CHECK CONSTRAINT [CHK_Role]
GO

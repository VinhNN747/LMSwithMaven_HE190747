USE [master]
GO
/****** Object:  Database [LeaveManagementAssignment]    Script Date: 30/06/2025 12:13:27 SA ******/
CREATE DATABASE [LeaveManagementAssignment]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'LeaveManagementAssignment', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL16.MSSQLSERVER\MSSQL\DATA\LeaveManagementAssignment.mdf' , SIZE = 8192KB , MAXSIZE = UNLIMITED, FILEGROWTH = 65536KB )
 LOG ON 
( NAME = N'LeaveManagementAssignment_log', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL16.MSSQLSERVER\MSSQL\DATA\LeaveManagementAssignment_log.ldf' , SIZE = 8192KB , MAXSIZE = 2048GB , FILEGROWTH = 65536KB )
 WITH CATALOG_COLLATION = DATABASE_DEFAULT, LEDGER = OFF
GO
ALTER DATABASE [LeaveManagementAssignment] SET COMPATIBILITY_LEVEL = 160
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [LeaveManagementAssignment].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [LeaveManagementAssignment] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [LeaveManagementAssignment] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [LeaveManagementAssignment] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [LeaveManagementAssignment] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [LeaveManagementAssignment] SET ARITHABORT OFF 
GO
ALTER DATABASE [LeaveManagementAssignment] SET AUTO_CLOSE OFF 
GO
ALTER DATABASE [LeaveManagementAssignment] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [LeaveManagementAssignment] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [LeaveManagementAssignment] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [LeaveManagementAssignment] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [LeaveManagementAssignment] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [LeaveManagementAssignment] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [LeaveManagementAssignment] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [LeaveManagementAssignment] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [LeaveManagementAssignment] SET  ENABLE_BROKER 
GO
ALTER DATABASE [LeaveManagementAssignment] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [LeaveManagementAssignment] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [LeaveManagementAssignment] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [LeaveManagementAssignment] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [LeaveManagementAssignment] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [LeaveManagementAssignment] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [LeaveManagementAssignment] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [LeaveManagementAssignment] SET RECOVERY FULL 
GO
ALTER DATABASE [LeaveManagementAssignment] SET  MULTI_USER 
GO
ALTER DATABASE [LeaveManagementAssignment] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [LeaveManagementAssignment] SET DB_CHAINING OFF 
GO
ALTER DATABASE [LeaveManagementAssignment] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [LeaveManagementAssignment] SET TARGET_RECOVERY_TIME = 60 SECONDS 
GO
ALTER DATABASE [LeaveManagementAssignment] SET DELAYED_DURABILITY = DISABLED 
GO
ALTER DATABASE [LeaveManagementAssignment] SET ACCELERATED_DATABASE_RECOVERY = OFF  
GO
EXEC sys.sp_db_vardecimal_storage_format N'LeaveManagementAssignment', N'ON'
GO
ALTER DATABASE [LeaveManagementAssignment] SET QUERY_STORE = ON
GO
ALTER DATABASE [LeaveManagementAssignment] SET QUERY_STORE (OPERATION_MODE = READ_WRITE, CLEANUP_POLICY = (STALE_QUERY_THRESHOLD_DAYS = 30), DATA_FLUSH_INTERVAL_SECONDS = 900, INTERVAL_LENGTH_MINUTES = 60, MAX_STORAGE_SIZE_MB = 1000, QUERY_CAPTURE_MODE = AUTO, SIZE_BASED_CLEANUP_MODE = AUTO, MAX_PLANS_PER_QUERY = 200, WAIT_STATS_CAPTURE_MODE = ON)
GO
USE [LeaveManagementAssignment]
GO
/****** Object:  Table [dbo].[Division]    Script Date: 30/06/2025 12:13:27 SA ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Division](
	[DivisionID] [int] IDENTITY(1,1) NOT NULL,
	[DivisionName] [nvarchar](50) NULL,
	[DivisionHead] [int] NULL,
 CONSTRAINT [PK_Division] PRIMARY KEY CLUSTERED 
(
	[DivisionID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Feature]    Script Date: 30/06/2025 12:13:27 SA ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Feature](
	[FeatureID] [int] IDENTITY(1,1) NOT NULL,
	[FeatureName] [varchar](max) NULL,
	[Endpoint] [varchar](max) NULL,
 CONSTRAINT [PK_Feature] PRIMARY KEY CLUSTERED 
(
	[FeatureID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[LeaveRequest]    Script Date: 30/06/2025 12:13:27 SA ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[LeaveRequest](
	[LeaveRequestID] [int] IDENTITY(1,1) NOT NULL,
	[SenderID] [int] NOT NULL,
	[Status] [varchar](50) NULL,
	[Reason] [nvarchar](max) NULL,
	[StartDate] [date] NOT NULL,
	[EndDate] [date] NOT NULL,
	[Title] [nvarchar](max) NULL,
	[ReviewerID] [int] NULL,
 CONSTRAINT [PK_LeaveRequest] PRIMARY KEY CLUSTERED 
(
	[LeaveRequestID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Role]    Script Date: 30/06/2025 12:13:27 SA ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Role](
	[RoleID] [int] IDENTITY(1,1) NOT NULL,
	[RoleName] [nvarchar](50) NOT NULL,
	[RoleDescription] [varchar](50) NULL,
	[RoleLevel] [int] NOT NULL,
 CONSTRAINT [PK_Role] PRIMARY KEY CLUSTERED 
(
	[RoleID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Role_Feature]    Script Date: 30/06/2025 12:13:27 SA ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Role_Feature](
	[RoleID] [int] NOT NULL,
	[FeatureID] [int] NOT NULL,
 CONSTRAINT [PK_Role_Feature] PRIMARY KEY CLUSTERED 
(
	[RoleID] ASC,
	[FeatureID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[User]    Script Date: 30/06/2025 12:13:27 SA ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[User](
	[UserID] [int] IDENTITY(1,1) NOT NULL,
	[Fullname] [nvarchar](50) NULL,
	[Username] [varchar](50) NULL,
	[Password] [varchar](max) NULL,
	[Email] [varchar](max) NULL,
	[DivisionID] [int] NULL,
	[Gender] [bit] NULL,
	[ManagerID] [int] NULL,
 CONSTRAINT [PK_User] PRIMARY KEY CLUSTERED 
(
	[UserID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[User_Role]    Script Date: 30/06/2025 12:13:27 SA ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[User_Role](
	[UserID] [int] NOT NULL,
	[RoleID] [int] NOT NULL,
 CONSTRAINT [PK_User_Role] PRIMARY KEY CLUSTERED 
(
	[UserID] ASC,
	[RoleID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
SET IDENTITY_INSERT [dbo].[Division] ON 

INSERT [dbo].[Division] ([DivisionID], [DivisionName], [DivisionHead]) VALUES (1, N'IT', 11)
INSERT [dbo].[Division] ([DivisionID], [DivisionName], [DivisionHead]) VALUES (2, N'Accountant', 15)
INSERT [dbo].[Division] ([DivisionID], [DivisionName], [DivisionHead]) VALUES (3, N'HR', NULL)
INSERT [dbo].[Division] ([DivisionID], [DivisionName], [DivisionHead]) VALUES (4, N'MKT', NULL)
INSERT [dbo].[Division] ([DivisionID], [DivisionName], [DivisionHead]) VALUES (5, N'Sales', NULL)
INSERT [dbo].[Division] ([DivisionID], [DivisionName], [DivisionHead]) VALUES (6, N'Security', NULL)
SET IDENTITY_INSERT [dbo].[Division] OFF
GO
SET IDENTITY_INSERT [dbo].[Feature] ON 

INSERT [dbo].[Feature] ([FeatureID], [FeatureName], [Endpoint]) VALUES (1, NULL, N'/user/create')
INSERT [dbo].[Feature] ([FeatureID], [FeatureName], [Endpoint]) VALUES (2, NULL, N'/user/edit')
INSERT [dbo].[Feature] ([FeatureID], [FeatureName], [Endpoint]) VALUES (3, NULL, N'/division/create')
INSERT [dbo].[Feature] ([FeatureID], [FeatureName], [Endpoint]) VALUES (4, NULL, N'/division/edit')
INSERT [dbo].[Feature] ([FeatureID], [FeatureName], [Endpoint]) VALUES (5, NULL, N'/example/authorized')
INSERT [dbo].[Feature] ([FeatureID], [FeatureName], [Endpoint]) VALUES (6, NULL, N'/user/list')
INSERT [dbo].[Feature] ([FeatureID], [FeatureName], [Endpoint]) VALUES (7, NULL, N'/division/list')
INSERT [dbo].[Feature] ([FeatureID], [FeatureName], [Endpoint]) VALUES (8, NULL, N'/user/delete')
INSERT [dbo].[Feature] ([FeatureID], [FeatureName], [Endpoint]) VALUES (9, NULL, N'/division/delete')
INSERT [dbo].[Feature] ([FeatureID], [FeatureName], [Endpoint]) VALUES (10, NULL, N'/user/changedivision')
INSERT [dbo].[Feature] ([FeatureID], [FeatureName], [Endpoint]) VALUES (11, NULL, N'/leaverequest/create')
INSERT [dbo].[Feature] ([FeatureID], [FeatureName], [Endpoint]) VALUES (12, NULL, N'/leaverequest/myrequests')
INSERT [dbo].[Feature] ([FeatureID], [FeatureName], [Endpoint]) VALUES (13, NULL, N'/leaverequest/subs')
INSERT [dbo].[Feature] ([FeatureID], [FeatureName], [Endpoint]) VALUES (15, NULL, N'/leaverequest/list')
INSERT [dbo].[Feature] ([FeatureID], [FeatureName], [Endpoint]) VALUES (16, NULL, N'/role/list')
INSERT [dbo].[Feature] ([FeatureID], [FeatureName], [Endpoint]) VALUES (17, NULL, N'/role/create')
INSERT [dbo].[Feature] ([FeatureID], [FeatureName], [Endpoint]) VALUES (18, NULL, N'/role/delete')
INSERT [dbo].[Feature] ([FeatureID], [FeatureName], [Endpoint]) VALUES (19, NULL, N'/role/edit')
INSERT [dbo].[Feature] ([FeatureID], [FeatureName], [Endpoint]) VALUES (20, NULL, N'/role/assignFeatures')
INSERT [dbo].[Feature] ([FeatureID], [FeatureName], [Endpoint]) VALUES (21, NULL, N'/user/role')
INSERT [dbo].[Feature] ([FeatureID], [FeatureName], [Endpoint]) VALUES (22, NULL, N'/leaverequest/review')
SET IDENTITY_INSERT [dbo].[Feature] OFF
GO
SET IDENTITY_INSERT [dbo].[LeaveRequest] ON 

INSERT [dbo].[LeaveRequest] ([LeaveRequestID], [SenderID], [Status], [Reason], [StartDate], [EndDate], [Title], [ReviewerID]) VALUES (2010, 17, N'Approved', N'di choi', CAST(N'2025-06-01' AS Date), CAST(N'2025-06-04' AS Date), N'nghi di choi', 16)
INSERT [dbo].[LeaveRequest] ([LeaveRequestID], [SenderID], [Status], [Reason], [StartDate], [EndDate], [Title], [ReviewerID]) VALUES (2011, 16, N'Rejected', N'kham benh', CAST(N'2025-06-01' AS Date), CAST(N'2025-06-02' AS Date), N'nghi di kham benh', 15)
SET IDENTITY_INSERT [dbo].[LeaveRequest] OFF
GO
SET IDENTITY_INSERT [dbo].[Role] ON 

INSERT [dbo].[Role] ([RoleID], [RoleName], [RoleDescription], [RoleLevel]) VALUES (5, N'Admin', NULL, 100)
INSERT [dbo].[Role] ([RoleID], [RoleName], [RoleDescription], [RoleLevel]) VALUES (6, N'Employee', N'', 1)
INSERT [dbo].[Role] ([RoleID], [RoleName], [RoleDescription], [RoleLevel]) VALUES (8, N'Intern', N'', 1)
INSERT [dbo].[Role] ([RoleID], [RoleName], [RoleDescription], [RoleLevel]) VALUES (10, N'Manager', N'', 5)
INSERT [dbo].[Role] ([RoleID], [RoleName], [RoleDescription], [RoleLevel]) VALUES (12, N'Division Head', N'', 99)
INSERT [dbo].[Role] ([RoleID], [RoleName], [RoleDescription], [RoleLevel]) VALUES (14, N'Lead', N'', 5)
SET IDENTITY_INSERT [dbo].[Role] OFF
GO
INSERT [dbo].[Role_Feature] ([RoleID], [FeatureID]) VALUES (5, 1)
INSERT [dbo].[Role_Feature] ([RoleID], [FeatureID]) VALUES (5, 2)
INSERT [dbo].[Role_Feature] ([RoleID], [FeatureID]) VALUES (5, 3)
INSERT [dbo].[Role_Feature] ([RoleID], [FeatureID]) VALUES (5, 4)
INSERT [dbo].[Role_Feature] ([RoleID], [FeatureID]) VALUES (5, 6)
INSERT [dbo].[Role_Feature] ([RoleID], [FeatureID]) VALUES (5, 7)
INSERT [dbo].[Role_Feature] ([RoleID], [FeatureID]) VALUES (5, 8)
INSERT [dbo].[Role_Feature] ([RoleID], [FeatureID]) VALUES (5, 9)
INSERT [dbo].[Role_Feature] ([RoleID], [FeatureID]) VALUES (5, 10)
INSERT [dbo].[Role_Feature] ([RoleID], [FeatureID]) VALUES (5, 16)
INSERT [dbo].[Role_Feature] ([RoleID], [FeatureID]) VALUES (5, 17)
INSERT [dbo].[Role_Feature] ([RoleID], [FeatureID]) VALUES (5, 18)
INSERT [dbo].[Role_Feature] ([RoleID], [FeatureID]) VALUES (5, 19)
INSERT [dbo].[Role_Feature] ([RoleID], [FeatureID]) VALUES (5, 20)
INSERT [dbo].[Role_Feature] ([RoleID], [FeatureID]) VALUES (5, 21)
INSERT [dbo].[Role_Feature] ([RoleID], [FeatureID]) VALUES (6, 11)
INSERT [dbo].[Role_Feature] ([RoleID], [FeatureID]) VALUES (6, 12)
INSERT [dbo].[Role_Feature] ([RoleID], [FeatureID]) VALUES (8, 11)
INSERT [dbo].[Role_Feature] ([RoleID], [FeatureID]) VALUES (8, 12)
INSERT [dbo].[Role_Feature] ([RoleID], [FeatureID]) VALUES (10, 11)
INSERT [dbo].[Role_Feature] ([RoleID], [FeatureID]) VALUES (10, 12)
INSERT [dbo].[Role_Feature] ([RoleID], [FeatureID]) VALUES (10, 13)
INSERT [dbo].[Role_Feature] ([RoleID], [FeatureID]) VALUES (10, 22)
INSERT [dbo].[Role_Feature] ([RoleID], [FeatureID]) VALUES (12, 6)
INSERT [dbo].[Role_Feature] ([RoleID], [FeatureID]) VALUES (12, 11)
INSERT [dbo].[Role_Feature] ([RoleID], [FeatureID]) VALUES (12, 12)
INSERT [dbo].[Role_Feature] ([RoleID], [FeatureID]) VALUES (12, 13)
INSERT [dbo].[Role_Feature] ([RoleID], [FeatureID]) VALUES (12, 22)
INSERT [dbo].[Role_Feature] ([RoleID], [FeatureID]) VALUES (14, 11)
INSERT [dbo].[Role_Feature] ([RoleID], [FeatureID]) VALUES (14, 12)
INSERT [dbo].[Role_Feature] ([RoleID], [FeatureID]) VALUES (14, 13)
INSERT [dbo].[Role_Feature] ([RoleID], [FeatureID]) VALUES (14, 22)
GO
SET IDENTITY_INSERT [dbo].[User] ON 

INSERT [dbo].[User] ([UserID], [Fullname], [Username], [Password], [Email], [DivisionID], [Gender], [ManagerID]) VALUES (6, N'Vinh', N'ad', N'ad', N'ad@gmail.com', 6, 1, NULL)
INSERT [dbo].[User] ([UserID], [Fullname], [Username], [Password], [Email], [DivisionID], [Gender], [ManagerID]) VALUES (11, N'Bruno Mars', N'bruno123', N'123', N'brunomars123@gmail.com', 1, 1, NULL)
INSERT [dbo].[User] ([UserID], [Fullname], [Username], [Password], [Email], [DivisionID], [Gender], [ManagerID]) VALUES (12, N'Justin Bieber', N'justinbb', N'123', N'jbiebs.official@gmail.com', 1, 1, 11)
INSERT [dbo].[User] ([UserID], [Fullname], [Username], [Password], [Email], [DivisionID], [Gender], [ManagerID]) VALUES (15, N'Ariana Grande', N'ariana1', N'123', N'ari_g.pop@gmail.com', 2, 0, NULL)
INSERT [dbo].[User] ([UserID], [Fullname], [Username], [Password], [Email], [DivisionID], [Gender], [ManagerID]) VALUES (16, N'Lady Gaga', N'ladygaga123', N'123', N'ladygaga@gmail.com', 2, 0, 15)
INSERT [dbo].[User] ([UserID], [Fullname], [Username], [Password], [Email], [DivisionID], [Gender], [ManagerID]) VALUES (17, N'Billie Eilish', N'billie123', N'123', N'billie@gmail.com', 2, 0, 16)
INSERT [dbo].[User] ([UserID], [Fullname], [Username], [Password], [Email], [DivisionID], [Gender], [ManagerID]) VALUES (18, N'Michael Jackson', N'mj75', N'123', N'mj345@gmail.com', 2, 1, 15)
INSERT [dbo].[User] ([UserID], [Fullname], [Username], [Password], [Email], [DivisionID], [Gender], [ManagerID]) VALUES (19, N'Ed Sheeran', N'edsheeran123', N'123', N'ed321@gmail.com', 2, 1, 18)
INSERT [dbo].[User] ([UserID], [Fullname], [Username], [Password], [Email], [DivisionID], [Gender], [ManagerID]) VALUES (20, N'Taylor Swift', N'taytay', N'123', N'taytay.music@gmail.com', 2, 0, 16)
INSERT [dbo].[User] ([UserID], [Fullname], [Username], [Password], [Email], [DivisionID], [Gender], [ManagerID]) VALUES (21, N'The Weeknd', N'weeky', N'123', N'weeky.wave@gmail.com', 5, 1, NULL)
SET IDENTITY_INSERT [dbo].[User] OFF
GO
INSERT [dbo].[User_Role] ([UserID], [RoleID]) VALUES (6, 5)
INSERT [dbo].[User_Role] ([UserID], [RoleID]) VALUES (11, 12)
INSERT [dbo].[User_Role] ([UserID], [RoleID]) VALUES (12, 10)
INSERT [dbo].[User_Role] ([UserID], [RoleID]) VALUES (15, 12)
INSERT [dbo].[User_Role] ([UserID], [RoleID]) VALUES (16, 14)
INSERT [dbo].[User_Role] ([UserID], [RoleID]) VALUES (17, 8)
INSERT [dbo].[User_Role] ([UserID], [RoleID]) VALUES (18, 10)
INSERT [dbo].[User_Role] ([UserID], [RoleID]) VALUES (19, 6)
INSERT [dbo].[User_Role] ([UserID], [RoleID]) VALUES (20, 8)
INSERT [dbo].[User_Role] ([UserID], [RoleID]) VALUES (21, 6)
GO
ALTER TABLE [dbo].[Division]  WITH CHECK ADD  CONSTRAINT [FK_Division_User] FOREIGN KEY([DivisionHead])
REFERENCES [dbo].[User] ([UserID])
GO
ALTER TABLE [dbo].[Division] CHECK CONSTRAINT [FK_Division_User]
GO
ALTER TABLE [dbo].[LeaveRequest]  WITH CHECK ADD  CONSTRAINT [FK_LeaveRequest_User2] FOREIGN KEY([ReviewerID])
REFERENCES [dbo].[User] ([UserID])
GO
ALTER TABLE [dbo].[LeaveRequest] CHECK CONSTRAINT [FK_LeaveRequest_User2]
GO
ALTER TABLE [dbo].[LeaveRequest]  WITH CHECK ADD  CONSTRAINT [FK_LeaveRequest_User3] FOREIGN KEY([SenderID])
REFERENCES [dbo].[User] ([UserID])
GO
ALTER TABLE [dbo].[LeaveRequest] CHECK CONSTRAINT [FK_LeaveRequest_User3]
GO
ALTER TABLE [dbo].[Role_Feature]  WITH CHECK ADD  CONSTRAINT [FK_Role_Feature_Feature] FOREIGN KEY([FeatureID])
REFERENCES [dbo].[Feature] ([FeatureID])
GO
ALTER TABLE [dbo].[Role_Feature] CHECK CONSTRAINT [FK_Role_Feature_Feature]
GO
ALTER TABLE [dbo].[Role_Feature]  WITH CHECK ADD  CONSTRAINT [FK_Role_Feature_Role] FOREIGN KEY([RoleID])
REFERENCES [dbo].[Role] ([RoleID])
GO
ALTER TABLE [dbo].[Role_Feature] CHECK CONSTRAINT [FK_Role_Feature_Role]
GO
ALTER TABLE [dbo].[User]  WITH CHECK ADD  CONSTRAINT [FK_User_Division] FOREIGN KEY([DivisionID])
REFERENCES [dbo].[Division] ([DivisionID])
GO
ALTER TABLE [dbo].[User] CHECK CONSTRAINT [FK_User_Division]
GO
ALTER TABLE [dbo].[User]  WITH CHECK ADD  CONSTRAINT [FK_User_User] FOREIGN KEY([ManagerID])
REFERENCES [dbo].[User] ([UserID])
GO
ALTER TABLE [dbo].[User] CHECK CONSTRAINT [FK_User_User]
GO
ALTER TABLE [dbo].[User_Role]  WITH CHECK ADD  CONSTRAINT [FK_User_Role_Role] FOREIGN KEY([RoleID])
REFERENCES [dbo].[Role] ([RoleID])
GO
ALTER TABLE [dbo].[User_Role] CHECK CONSTRAINT [FK_User_Role_Role]
GO
ALTER TABLE [dbo].[User_Role]  WITH CHECK ADD  CONSTRAINT [FK_User_Role_User] FOREIGN KEY([UserID])
REFERENCES [dbo].[User] ([UserID])
GO
ALTER TABLE [dbo].[User_Role] CHECK CONSTRAINT [FK_User_Role_User]
GO
USE [master]
GO
ALTER DATABASE [LeaveManagementAssignment] SET  READ_WRITE 
GO

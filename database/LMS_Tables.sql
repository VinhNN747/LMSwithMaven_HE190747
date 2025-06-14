USE LeaveManagementAssignment
-- Drop Division table if it exists
IF OBJECT_ID('Division', 'U') IS NOT NULL
    DROP TABLE Division;
GO

CREATE TABLE Division (
    DivisionID INT NOT NULL IDENTITY(1,1),
    DivisionName NVARCHAR(50) NOT NULL,
    DivisionDirector VARCHAR(10) NULL,
    CONSTRAINT PK_Division PRIMARY KEY (DivisionID),
    CONSTRAINT CHK_DivisionName CHECK (DivisionName <> '')
);
GO

-- Drop User table if it exists
IF OBJECT_ID('[User]', 'U') IS NOT NULL
    DROP TABLE [User];
GO

CREATE TABLE [User] (
    UserID VARCHAR(10) NOT NULL,
    FullName NVARCHAR(100) NOT NULL,
    Username VARCHAR(50) NOT NULL,
    Email VARCHAR(100) NOT NULL,
    Gender CHAR(1) NULL,
    DivisionID INT NULL,
    [Role] NVARCHAR(100) NOT NULL DEFAULT 'Employee',
    IsActive BIT NOT NULL DEFAULT 1,
    ManagerID VARCHAR(10) NULL,
    CONSTRAINT PK_User PRIMARY KEY (UserID),
    CONSTRAINT UK_User_Username UNIQUE (Username),
    CONSTRAINT UK_User_Email UNIQUE (Email),
    CONSTRAINT CHK_Gender CHECK (Gender IN ('M', 'F')),
    CONSTRAINT CHK_Role CHECK ([Role] IN ('Employee', 'Manager', 'Director')),
    CONSTRAINT FK_User_Division FOREIGN KEY (DivisionID) REFERENCES Division(DivisionID),
    CONSTRAINT FK_User_Manager FOREIGN KEY (ManagerID) REFERENCES [User](UserID)
);
GO

-- Add foreign key constraint to Division for DivisionDirector
ALTER TABLE Division
ADD CONSTRAINT FK_Division_Director
    FOREIGN KEY (DivisionDirector) REFERENCES [User](UserID);
GO

-- Drop LeaveRequest table if it exists
IF OBJECT_ID('LeaveRequest', 'U') IS NOT NULL
    DROP TABLE LeaveRequest;
GO

CREATE TABLE LeaveRequest (
    LeaveRqID VARCHAR(20) NOT NULL,
    SenderID VARCHAR(10) NOT NULL,
    ApproverID VARCHAR(10) NOT NULL,
    Reason NVARCHAR(MAX) NULL,
    [Status] VARCHAR(20) NOT NULL DEFAULT 'Pending',
    CONSTRAINT PK_LeaveRequest PRIMARY KEY (LeaveRqID),
    CONSTRAINT CHK_Status CHECK ([Status] IN ('Pending', 'Approved', 'Canceled')),
    CONSTRAINT FK_LeaveRequest_Sender FOREIGN KEY (SenderID) REFERENCES [User](UserID),
    CONSTRAINT FK_LeaveRequest_Approver FOREIGN KEY (ApproverID) REFERENCES [User](UserID)
);
GO
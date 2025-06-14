USE LeaveManagementAssignment

IF OBJECT_ID('trg_HandleInsertLeaveRequest', 'TR') IS NOT NULL
DROP TRIGGER trg_HandleInsertLeaveRequest;
GO

CREATE TRIGGER trg_HandleInsertLeaveRequest
ON LeaveRequest
INSTEAD OF INSERT
AS
BEGIN
SET NOCOUNT ON;

    DECLARE @SenderID VARCHAR(10), 
            @ApproverID VARCHAR(10), 
            @Reason NVARCHAR(MAX), 
            @Status VARCHAR(20),
            @Index INT, 
            @NewID VARCHAR(20);

    DECLARE cur CURSOR FOR
SELECT
	i.SenderID
   ,u.ManagerID
   ,i.Reason
   ,i.[Status]
FROM inserted i
LEFT JOIN [User] u
	ON i.SenderID = u.UserID;

OPEN cur;
FETCH NEXT FROM cur INTO @SenderID, @ApproverID, @Reason, @Status;

WHILE @@FETCH_STATUS = 0
BEGIN
SELECT
	@Index = ISNULL(MAX(CAST(RIGHT(LeaveRqID, 3) AS INT)), 0)
FROM LeaveRequest
WHERE LEFT(LeaveRqID, LEN(@SenderID)) = @SenderID;

SET @Index += 1;
SET @NewID = @SenderID + RIGHT('000' + CAST(@Index AS VARCHAR), 3);

INSERT INTO LeaveRequest (LeaveRqID, SenderID, ApproverID, Reason, [Status])
	VALUES (@NewID, @SenderID, @ApproverID, @Reason, @Status);

FETCH NEXT FROM cur INTO @SenderID, @ApproverID, @Reason, @Status;
END

CLOSE cur;
DEALLOCATE cur;
END;
GO



IF OBJECT_ID('trg_InsertUserID', 'TR') IS NOT NULL
DROP TRIGGER trg_InsertUserID;
GO

CREATE TRIGGER trg_InsertUserID
ON [User]
INSTEAD OF INSERT
AS
BEGIN
SET NOCOUNT ON;

    DECLARE 
        @FullName NVARCHAR(100),
        @Username VARCHAR(50),
        @Email VARCHAR(100),
        @Gender CHAR(1),
        @DivisionID INT,
        @Role NVARCHAR(100),
        @IsActive BIT,
        @ManagerID VARCHAR(10),
        @Acronym VARCHAR(10),
        @Index INT,
        @NewID VARCHAR(10);

    DECLARE cur CURSOR FOR
SELECT
	FullName
   ,Username
   ,Email
   ,Gender
   ,DivisionID
   ,[Role]
   ,IsActive
   ,ManagerID
FROM inserted;

OPEN cur;
FETCH NEXT FROM cur INTO
@FullName, @Username, @Email, @Gender, @DivisionID, @Role, @IsActive, @ManagerID;

WHILE @@FETCH_STATUS = 0
BEGIN
-- Build acronym from FullName (e.g. John Doe -> JD)
SET @Acronym = (SELECT
		STRING_AGG(LEFT(value, 1), '')
	FROM STRING_SPLIT(@FullName, ' '));

-- Get next index
SELECT
	@Index = ISNULL(MAX(CAST(RIGHT(UserID, 3) AS INT)), 0)
FROM [User]
WHERE LEFT(UserID, LEN(@Acronym)) = @Acronym;

SET @Index += 1;

-- Format ID like JD001
SET @NewID = @Acronym + RIGHT('000' + CAST(@Index AS VARCHAR), 3);

        -- Handle NULL Role (default to Employee)
        IF @Role IS NULL
SET @Role = 'Employee';

-- Final insert
INSERT INTO [User] (UserID, FullName, Username, Email, Gender, DivisionID, [Role], IsActive, ManagerID)
	VALUES (@NewID, @FullName, @Username, @Email, @Gender, @DivisionID, @Role, @IsActive, @ManagerID);

FETCH NEXT FROM cur INTO
@FullName, @Username, @Email, @Gender, @DivisionID, @Role, @IsActive, @ManagerID;
END

CLOSE cur;
DEALLOCATE cur;
END;
GO



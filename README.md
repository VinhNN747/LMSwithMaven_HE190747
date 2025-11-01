# Leave Management System (LMS)

Hệ thống quản lý nghỉ phép sử dụng Java, Jakarta EE 10, Hibernate và SQL Server.

## 🚀 Cài đặt nhanh

### Yêu cầu
- Java 11+
- Maven 3.6+
- SQL Server
- Tomcat 10+

### Cấu hình
1. Tạo database `LeaveManagementAssignment` trong SQL Server
2. Chạy script `database/data.sql`
3. Cập nhật thông tin DB trong `persistence.xml`
4. Build: `mvn clean package`
5. Deploy file WAR lên Tomcat

## 📋 Tính năng

- ✅ Quản lý người dùng và phòng ban
- ✅ Phân quyền theo vai trò
- ✅ Tạo và phê duyệt yêu cầu nghỉ phép
- ✅ Luồng phê duyệt phân cấp
- ✅ Lịch công tác (Agenda)

## 🔐 Tài khoản mẫu

| Username | Password | Vai trò | Phòng ban |
|----------|----------|---------|-----------|
| ari_g | ari_g | Head | Accountant |
| billz | billz | Employee | Accountant |
| charlie123 | charlie123 | Head | MKT |
| edsheeran123 | edsheeran123 | Employee | MKT |
| ladygaga123 | ladygaga123 | Head | IT |
| taytay | taytay | Head | HR |
| vinh | vinh | Employee | HR |

## 🛠 Công nghệ

- **Backend**: Jakarta EE 10, Hibernate 6.4, Servlet
- **Frontend**: JSP, Bootstrap 5.3
- **Database**: SQL Server
- **Build**: Maven

## 📁 Cấu trúc
LMS_withMaven/
├── src/main/java/com/
│ ├── controller/ # Servlets
│ ├── dao/ # Data Access Objects
│ └── entity/ # JPA Entities
├── src/main/webapp/
│ ├── css/ # Stylesheets
│ ├── view/ # JSP Views
│ └── WEB-INF/ # Config files
├── database/data.sql # Database schema
└── pom.xml # Maven config

## 🌐 Truy cập

Sau khi deploy: `http://localhost:8080/LMS_withMaven/`

## 📞 Liên hệ

**Tác giả**: Vinh Nguyen  
**Email**: vinhngoc172005@gmail.com

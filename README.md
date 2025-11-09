# 🧾ALHORIZON | A/L Paper Management System | DEA Assignment

A comprehensive **Spring Boot web application** designed to manage Advanced Level past papers, notes, and related academic resources.  
The system allows administrators and teachers to upload, organize, and manage subject-wise papers, while students can browse and download papers easily.  

---

## 🧩 Project Description

The **AL Paper Management System** provides an online platform for managing and sharing educational materials (PDFs) in a structured, secure, and user-friendly manner.  
It supports both **web-based user interfaces (Thymeleaf views)** and **RESTful JSON APIs** for integration with other applications or Postman testing.

---

## 🚀 Main Features

### 🎓 Admin Features
- Upload new papers (PDFs) with metadata (title, year, subject, type)
- Manage subjects and view categorized papers
- Delete old or duplicate papers
- Secure admin dashboard with authentication

### 📚 Student Features
- View and filter available papers by subject and year
- Download PDFs directly from the portal
- Browse categorized content streams (e.g., Technology, Science)

### ⚙️ System Features
- File uploads stored locally (`uploads/papers/`)
- Pagination and filtering
- RESTful API for external integrations
- Role-based access (Admin, Teacher, Student)
- CSRF-protected web forms, but API-friendly CSRF exclusions
- Clean modular structure: `auth`, `papers`, `subjects`, `config`

---

## 🧠 Technologies Used

| Layer | Technology |
|-------|-------------|
| **Backend Framework** | Spring Boot 3.x |
| **Template Engine** | Thymeleaf |
| **Database** | MySQL (JPA / Hibernate) |
| **Security** | Spring Security (Role-based + BCrypt) |
| **Frontend Styling** | HTML5, CSS3, Bootstrap |
| **File Handling** | Multipart File Upload |
| **API Testing** | Postman |
| **Build Tool** | Maven |
| **Java Version** | JDK 17+ |

---

## ⚙️ Setup Instructions

### 🧰 Prerequisites
- Install **Java 17+**
- Install **Maven 3.8+**
- MySQL running locally (e.g., `localhost:3306`)
- (Optional) Postman for API testing

### 🪜 Steps

1. **Clone the project**
   ```bash
   git clone https://github.com/your-username/al-paper-management.git
   cd al-paper-management

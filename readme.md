# 📚 Library Management System (Console Based)

A fully functional **Java Console-Based Library Management System** built using layered architecture and file-based persistence (Serialization).

This project simulates real-world backend logic including authentication, role-based dashboards, book issuing/returning, and member management.

---

## 🚀 Features

### 🔐 Authentication System
- admin account auto-creation (if not exists)
- Role-based login (admin / member)
- Credential verification using serialized storage

---

### 🛠 admin Dashboard
- Create new members
- Delete members
- View all members
- View specific member details
- Add new books
- Remove books
- View all books (with stock details)
- Logout functionality

---

### 👤 member Dashboard
- View available books
- Search books by:
    - Book ID
    - Title
    - Author
- Issue book (with duplicate prevention)
- Return book
- View issued books
- View profile
- Update password
- Logout

---

## 🧠 Core Concepts Used

- Object-Oriented Programming (OOP)
- Layered Architecture (UI → Operations → Repository → Model)
- Java Serialization (`.ser` file storage)
- Collections (`ArrayList`, `ListIterator`)
- Encapsulation & Proper Model Design
- Separation of Concerns
- Input Validation & Edge Case Handling

---

## 📸 Application Screenshots

### 🔐 Login Screen
![Login Screen](assets/screenshots/login.png)

---

### 🛠 admin Dashboard
![admin Dashboard](assets/screenshots/adminDashboard.png)

---

### 👤 member Dashboard
![member Dashboard](assets/screenshots/memberDashboard.png)

---

### 📚 Book Add Flow
![Issue Book](assets/screenshots/bookAdd.png)

---

### 📚 Book Issue Flow
![Issue Book](assets/screenshots/bookissue.png)

---

### 📘 Book Return Flow
![Return Book](assets/screenshots/bookReturn.png)

## 📂 Project Structure

```
LibraryManagementSystem/
│
├── LandingPage.java
│
├── login/
│   ├── LoginDashboard.java
│   └── login.java
│
├── admin/
│   └── AdminDashboard.java
│
├── member/
│   ├── member.java
│   ├── MemberDashboard.java
│   ├── MemberOperations.java
│   └── MemberRepo.java
│
├── books/
│   ├── Book.java
│   ├── BookOperations.java
│   └── BookRepo.java
│
└── auth/
    ├── admin.java
    └── CredRepo.java
```


---

## 💾 Data Persistence

This system uses Java Serialization to store:

- admin credentials → `Credentials.ser`
- Members data → `MemberData.ser`
- books data → `BooksData.ser`

All data persists between application runs.

---

## 🔄 Book Issuing Logic

- Prevents issuing the same book twice to the same member
- Prevents issuing when stock is unavailable
- Automatically updates available quantity
- Keeps member and book records in sync

---

## 🏗 Architecture Overview
```
Dashboard (UI Layer)
↓
Operations (Business Logic Layer)
↓
Repository (Persistence Layer)
↓
Model (Data Layer)
```

## 🛠 Technologies Used

- Java
- Java IO (File Streams)
- Object Serialization
- OOP Principles

---

## 📌 Future Improvements (Will be done soon)

- Replace serialization with database (MySQL / PostgreSQL)
- Convert to Spring Boot REST API

---
 
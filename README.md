# **Quiz System**

This project is a simple Quiz Management System designed to demonstrate CRUD operations using Java and MySQL. It includes a backend application that interacts with a MySQL database to manage users, quizzes, questions, and results.

---

## **Features**
- **User Management**: Add and authenticate users with roles (Admin/User).  
- **Quiz Management**: Add, view, and manage quiz questions.  
- **Results Tracking**: Record and view quiz results for users.  
- **CRUD Operations**:
  - **Create**: Add users, quizzes, questions, and results.
  - **Read**: Retrieve data like quiz questions and user results.
  - **Update**: Modify user information or quiz data.
  - **Delete**: Remove quizzes or user data.

---

## Sustainable Development Goal (SDG) Integration  
### Chosen SDG: **Quality Education (SDG 4)**  
This project aligns with SDG 4 by promoting:  
- Accessible, engaging, and organized quiz systems to enhance the learning experience.  
- A platform for users to improve knowledge retention through interactive assessments.  
- Secure and transparent scoring to motivate continuous learning.  

---

## **Technologies Used**
- **Programming Language**: Java  
- **Database**: MySQL  
- **Tools**:  
  - **JDBC** for database connectivity.  
  - **dbdiagram.io** for database schema visualization.  
  - **GitHub** for version control.

---

## **Database Schema**
The database contains the following tables:

1. **Users**:  
   - Fields: `id`, `username`, `password`, `role`.  
   - Description: Stores user login information and roles.

2. **Quiz**:  
   - Fields: `id`, `title`, `description`.  
   - Description: Stores quiz details.

3. **Questions**:  
   - Fields: `id`, `quiz_id`, `question`, `answer`.  
   - Description: Stores quiz questions and their answers.

4. **Results**:  
   - Fields: `id`, `user_id`, `quiz_id`, `score`, `date_taken`.  
   - Description: Tracks users’ quiz performance.

Refer to `schema.png` for a diagram of the database schema.

## Contributor
 
| Name                         | Role       | Email                         |
|------------------------------|------------|-------------------------------|
| [Rivera, Irish](https://github.com/kelleeerrrr)      | Developer  | 23-00679@g.batstate-u.edu.ph  |

<%@ include file="common/header.jsp" %>

<h2>Welcome to Struts Demo Application</h2>

<p>This is a sample Struts 2 application demonstrating:</p>
<ul>
    <li><strong>Struts 2 Framework</strong> - MVC pattern implementation</li>
    <li><strong>JSP Pages</strong> - View layer with Struts tags</li>
    <li><strong>Action Classes</strong> - Controller layer handling business logic</li>
    <li><strong>In-Memory H2 Database</strong> - Data persistence layer</li>
</ul>

<div style="margin: 30px 0;">
    <h3>Features</h3>
    <ul>
        <li>User management (Create, Read, Update, Delete)</li>
        <li>Form validation</li>
        <li>Database operations with H2</li>
        <li>Responsive web design</li>
        <li>Error handling and messaging</li>
    </ul>
</div>

<div style="margin: 30px 0;">
    <h3>Quick Start</h3>
    <p>Click on "User List" to view all users or "Add User" to create a new user.</p>
    <a href="<s:url action='userList'/>" class="btn">View Users</a>
    <a href="<s:url action='userForm'/>" class="btn">Add New User</a>
</div>

<div style="margin: 30px 0; padding: 15px; background-color: #e7f3ff; border-left: 4px solid #2196F3;">
    <h4>Database Information</h4>
    <p>This application uses an H2 in-memory database that is automatically initialized with sample data when the application starts. The database includes a "users" table with sample records.</p>
</div>

<%@ include file="common/footer.jsp" %>
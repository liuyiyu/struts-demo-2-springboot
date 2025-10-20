<%@ include file="common/header.jsp" %>

<h2>User Management</h2>

<div style="margin-bottom: 20px;">
    <a href="<s:url action='userForm'/>" class="btn">Add New User</a>
</div>

<s:if test="users != null && users.size() > 0">
    <table class="table">
        <thead>
            <tr>
                <th>ID</th>
                <th>First Name</th>
                <th>Last Name</th>
                <th>Email</th>
                <th>Phone</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <s:iterator value="users" var="user">
                <tr>
                    <td><s:property value="#user.id"/></td>
                    <td><s:property value="#user.firstName"/></td>
                    <td><s:property value="#user.lastName"/></td>
                    <td><s:property value="#user.email"/></td>
                    <td><s:property value="#user.phone"/></td>
                    <td>
                        <a href="<s:url action='editUser'><s:param name='userId' value='#user.id'/></s:url>" 
                           class="btn btn-warning">Edit</a>
                        <a href="<s:url action='deleteUser'><s:param name='userId' value='#user.id'/></s:url>" 
                           class="btn btn-danger"
                           onclick="return confirm('Are you sure you want to delete this user?')">Delete</a>
                    </td>
                </tr>
            </s:iterator>
        </tbody>
    </table>
</s:if>
<s:else>
    <div style="padding: 20px; text-align: center; color: #666;">
        <p>No users found. <a href="<s:url action='userForm'/>">Add the first user</a>.</p>
    </div>
</s:else>

<%@ include file="common/footer.jsp" %>
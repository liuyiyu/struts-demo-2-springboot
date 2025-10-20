<%@ include file="common/header.jsp" %>

<h2>
    <s:if test="user.id != null">
        Edit User
    </s:if>
    <s:else>
        Add New User
    </s:else>
</h2>

<s:form action="saveUser" method="post">
    <s:hidden name="user.id" value="%{user.id}" />
    
    <div class="form-group">
        <s:textfield name="user.firstName" 
                     label="First Name" 
                     value="%{user.firstName}" 
                     placeholder="Enter first name"
                     required="true" />
        <s:fielderror fieldName="user.firstName" cssClass="error" />
    </div>
    
    <div class="form-group">
        <s:textfield name="user.lastName" 
                     label="Last Name" 
                     value="%{user.lastName}" 
                     placeholder="Enter last name"
                     required="true" />
        <s:fielderror fieldName="user.lastName" cssClass="error" />
    </div>
    
    <div class="form-group">
        <s:textfield name="user.email" 
                     label="Email" 
                     value="%{user.email}" 
                     placeholder="Enter email address"
                     type="email"
                     required="true" />
        <s:fielderror fieldName="user.email" cssClass="error" />
    </div>
    
    <div class="form-group">
        <s:textfield name="user.phone" 
                     label="Phone" 
                     value="%{user.phone}" 
                     placeholder="Enter phone number" />
        <s:fielderror fieldName="user.phone" cssClass="error" />
    </div>
    
    <div style="margin-top: 20px;">
        <s:submit value="Save User" cssClass="btn" />
        <a href="<s:url action='userList'/>" class="btn" style="background-color: #6c757d; margin-left: 10px;">Cancel</a>
    </div>
</s:form>

<%@ include file="common/footer.jsp" %>
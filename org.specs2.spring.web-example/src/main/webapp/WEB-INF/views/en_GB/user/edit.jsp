<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
	<link rel="stylesheet" href="/css/jquery-ui-1.8.5.custom.css" type="text/css"/>
	<link rel="stylesheet" href="/css/main.css" type="text/css"/>
	<title>Users</title>
</head>
<body>
<div class="body">
	<sf:form action="/users.html" method="post" commandName="user">
		<sf:hidden path="id"/>

		<div class="ui-widget">
			<sf:label path="username">Username</sf:label> <sf:input path="username"/><sf:errors path="username"/>
		</div>
		<div class="ui-widget">
			<sf:label path="fullName">First Name</sf:label> <sf:input path="fullName"/><sf:errors path="fullName"/>
		</div>

		<input type="submit"/>
	</sf:form>
</div>
</body>
</html>
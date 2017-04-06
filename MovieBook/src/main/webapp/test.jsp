<%@ page import="com.moviebook.user.User"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Test Page</title>

<!-- CSS Includes -->
<link rel="stylesheet" href="resources/css/styles.css" />

<!-- Javascript Includes -->
<script type="text/javascript" src="resources/js/jquery-3.1.1.js"></script>
<script type="text/javascript" src="resources/js/main.js"></script>

</head>
<body>
	<!-- Test login form  -->
	<form id="loginForm" style="display: none">
		<table>
			<tr>
				<td>Username:</td>
				<td><input name="loginUserName" type="text" /></td>
			</tr>
			<tr>
				<td>Password:</td>
				<td><input name="loginPassword" type="password" /></td>
			</tr>
			<tr>
				<td colspan="2">
					<button type="button" id="loginButton" onclick="loginAction()">Login</button>
				</td>
			</tr>
		</table>
	</form>
	<div class="loginInfo" style="display: none; float: clear;">
		<img src="" alt="Profile picture" class="userDisplayPic" />
		<p class="userDetails"></p>
		<button class="logout" style="float: right;" >Logout</button>
	</div>
	<div class="logger"></div>

	<!-- Hidden fields -->
	<%
		if (session.getAttribute("currentUserBean") != null) {
	%>
	<div id="currentUser" style="display: none;">
		<%=((User) session.getAttribute("currentUserBean")).toJson()%>
	</div>
	<%
		}
	%>


</body>
</html>
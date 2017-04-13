<%@ page import="com.moviebook.bean.user.UserBean"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Test Page</title>

<!-- CSS Includes -->
<link rel="stylesheet" href="test_resources/css/styles.css" />

<!-- Javascript Includes -->
<script type="text/javascript" src="test/js/jquery-3.1.1.js"></script>
<script type="text/javascript" src="test/js/main.js"></script>

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
	<div class="loginInfo" style="display: none; clear: both">
		<img src="" alt="Profile picture" class="userDisplayPic" />
		<p class="userDetails"></p>
		<button class="logout" style="float: right;">Logout</button>
	</div>

	<div class="friendsList" style="clear: both; display: none">
		<h1>Friends</h1>
	</div>

	<div class="recommendMoviesList" style="clear: both; display: none">
		<h1>Recommended Movies</h1>
	</div>

	<!-- Hidden fields -->
	<%
		if (session.getAttribute("currentUserBean") != null) {
	%>
	<div id="currentUser" style="display: none;">
		<%=((UserBean) session.getAttribute("currentUserBean")).toJson()%>
	</div>
	<%
		}
	%>


</body>
</html>
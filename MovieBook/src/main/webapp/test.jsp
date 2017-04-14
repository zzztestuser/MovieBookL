<%@ page import="com.moviebook.bean.UserBean"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Test Page</title>

<!-- CSS Includes -->
<link rel="stylesheet" href="test/css/styles.css" />

<!-- Javascript Includes -->
<script type="text/javascript" src="test/js/jquery-3.1.1.js"></script>
<script type="text/javascript" src="test/js/test.js"></script>

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

	<div class="friendsList" style="clear: both; display: none"></div>

	<div class="recommendMoviesList" style="display: none"></div>


	<div class="movieSearch" style="display: none">
		<hr />
		<h1>Search for movie</h1>
		<div class="searchTerms">
			<strong>Title</strong><input name="movieSearch" type="text"
				style="margin: 0px 5px 0px 5px" />
			<button type="button" id="movieSearchButton"
				onclick="movieSearchAction()">Search</button>
		</div>
		<div class="searchResults">
			<table class="searchResultsTable" style="display: none">
				<tr class="searchResultsRow">
					<td class="searchResultDetail"><span
						style="font-weight: bold;">Title:</span> <span
						class="searchResultTitle"></span> <br /> <span
						style="font-weight: bold;">Genre(s):</span> <span
						class="searchResultGenres"></span> <br /> <span
						class="searchResultDescription"></span></td>
					<td class="searchResultActions">
						<div class="interestedFriends">
							<strong>Interested Friends</strong>
							<div class="interestedFriendsList"></div>
						</div>
						<hr />
						<div class="movieScreenings">
							<strong>Screenings</strong>
							<div class="movieScreeningsList"></div>
						</div>
						<div class="inviteActions">
						</div>
					</td>
				</tr>
			</table>
		</div>
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
// Initializer
$(document).ready(function() {
	pageInit();
});

var movieBook = {};

function pageInit() {

	// Check for existence of hidden field and initalize appropriately

	var userInfo = $("#currentUser")

	if (userInfo.length == 0) {
		// Not logged in
		initLoggedOut();
	} else {
		// Logged in.
		var user = JSON.parse(userInfo.text());
		movieBook.currentUser = user;
		initLoggedIn();
	}

	attachEventHandlers();

}

function initLoggedOut() {
	// Show login form
	$("form#loginForm").show();
	$("div.loginInfo").hide();
	$("div.recommendMoviesList").hide();

}

function initLoggedIn() {

	updateUserDetails();
	updateUserFriends();
	updateRecommendedMovies();

}

function updateRecommendedMovies() {
	$.ajax({

		url : "api/movies/recommended",

		type : "GET",

		dataType : "json",

		cache : false,

	}).done(
			function(movies) {

				var recElement = $("div.recommendMoviesList");

				$.each(movies, function(key, movie) {
					// Build the <div> object to house this
					var newMovie = $("<table>");
					var movieRow = newMovie.append($("<tr>"));
					movieRow.append($("<td>").text("Title").css(
							"font-weight", "bold"));
					movieRow.append($("<td>").text(movie.title));

					recElement.append(newMovie);
				})
				
				recElement.show();
				
			});
}

function attachEventHandlers() {
	$("button.logout").click(logoutAction);
}

function logoutAction() {
	// Destroy everything and force a reload of page
	movieBook = {};
	$.ajax({

		url : "api/session",

		type : "DELETE",

		cache : false,

	}).always(function() {

		window.location.href = "test.jsp";

	});
}

function loginAction() {

	var loginData = {
		'username' : $("input[name='loginUserName']").val(),
		'password' : $("input[name='loginPassword']").val()
	};

	$.ajax({

		url : "api/session",

		type : "POST",

		data : loginData,

		dataType : "json",

		cache : false,

	}).done(function(userJson) {

		movieBook.currentUser = userJson;
		initLoggedIn();

	});

};

function updateUserDetails() {

	var user = movieBook.currentUser;

	$("div.loginInfo > img.userDisplayPic").attr("src", user.profilePhotoPath);
	$("div.loginInfo > p.userDetails").text(user.name);
	$("form#loginForm").hide();
	$("div.loginInfo").show();
}

function updateUserFriends() {

	var user = movieBook.currentUser;

	$.ajax({
		url : "api/users/" + user.id + "/friends",
		type : "GET",
		dataType : "json",
		cache : false,

	}).done(function(friendsList) {
		var friendsListDiv = $("div.friendsList");
		var table = $("<table></table>");
		for ( var i in friendsList) {

			var tr = $("<tr></tr>");
			tr.append($("<td>Name</td>").css("font-weight", "bold"));
			tr.append($("<td>" + friendsList[i].name + "</td>"));
			tr.append($("<td>Email</td>").css("font-weight", "bold"));
			tr.append($("<td>" + friendsList[i].email + "</td>"));
			table.append(tr);

		}
		friendsListDiv.append(table);
		friendsListDiv.show();
	});

}

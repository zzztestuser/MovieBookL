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
	$("div.movieSearch").hide();

}

function initLoggedIn() {

	updateUserDetails();
	updateUserFriends();
	updateRecommendedMovies();

	$("div.movieSearch").show();

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
				recElement.empty();
				recElement.append($("<hr />"));
				recElement.append($("<h1>").text("Recommended Movies"));

				$.each(movies, function(key, movie) {
					// Build the <div> object to house this
					var newMovie = $("<table>");
					var movieRow = newMovie.append($("<tr>"));
					movieRow.append($("<td>").text("Title").css("font-weight",
							"bold"));
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
		"username" : $("input[name='loginUserName']").val(),
		"password" : $("input[name='loginPassword']").val()
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
		url : "api/users/friends",
		data : {
			"user" : user.id,
			"inviteStatus" : "accepted"
		},
		type : "GET",
		dataType : "json",
		cache : false,

	}).done(function(friendsList) {
		var friendsListDiv = $("div.friendsList");
		friendsListDiv.empty();
		friendsListDiv.append($("<hr />"));
		friendsListDiv.append($("<h1>").text("Friends List"));
		var table = $("<table></table>");
		for ( var i in friendsList) {

			var tr = $("<tr></tr>");
			tr.append($("<td>Name</td>").css("font-weight", "bold"));
			tr.append($("<td>" + friendsList[i].name + "</td>"));
			tr.append($("<td>Email</td>").css("font-weight", "bold"));
			tr.append($("<td>" + friendsList[i].email + "</td>"));
			tr.append($("<td>Invite status</td>").css("font-weight", "bold"));
			tr.append($("<td>" + friendsList[i].status + "</td>"));
			table.append(tr);

		}
		friendsListDiv.append(table);
		friendsListDiv.show();
	});

}

function movieSearchAction() {

	var searchTerms = {
		"title" : $("input[name='movieSearch']").val()
	};

	$.ajax(
			{
				url : "api/movies/search",

				type : "GET",

				data : searchTerms,

				dataType : "json",

				cache : false,

				statusCode : {
					404 : function() {
						// No results found
						$(".searchResultsTable").hide();
						$(".searchResultsTable").after(
								$("<div>").addClass("noResultsFound").css(
										"color", "red")
										.text("No results found"));
					}
				}

			}).done(function(results) {

		// Remove results not found if any
		if ($(".searchResults > .noResultsFound").length)
			$(".searchResults > .noResultsFound").remove();

		// Some results found
		var resultRow = $(".searchResultsTable .searchResultsRow:first-child");
		resultRow.detach();
		var table = $(".searchResultsTable");
		table.hide();
		table.empty();

		$.each(results, function(index, movie) {
			var newRow = resultRow.clone();
			updateMovieSearchResultDetails(newRow, table, movie);
			updateMovieSearchResultActions(newRow, table, movie);
		})

		table.show();

	});

}

function updateMovieSearchResultDetails(newRow, table, movie) {
	newRow.find(".searchResultTitle").text(movie.title);

	newRow.find(".searchResultGenres").text($(movie.genre).map(function() {
		return (this);
	}).get().join(", ")).css("font-style", "italic");
	newRow.find(".searchResultDescription").text(movie.description);
	table.append(newRow);

}

function updateMovieSearchResultActions(newRow, table, movie) {

	// Get interested friends
	var friendsAJAX = $.ajax({
		url : "api/movies/recommended/friends",
		data : {
			"movie" : movie.id
		},
		type : "GET",
		cache : false,
		dataType : "json"
	});

	friendsAJAX.done(function(friends, textStatus, jqxHR) {
		var friendsList = newRow.find(".interestedFriendsList");
		friendsList.empty();
		
		if (jqxHR.status == 204) {
			// No Friends found
			friendsList.append($("<span>").css("color","red").text("No interested friends."));
		}
		
		// Setup friends
		$.each(friends, function(index, friend) {
			var selectOption = $("<input>", {
				"type" : "checkbox",
				"name" : "inviteFriendOption",
				"value" : friend.id
			});
			friendsList.append(selectOption);
			friendsList.append($("<span>").text(friend.name));
			friendsList.append($("<br />"));
		})
	});

	var screeningsAJAX = $.ajax({
		url : "api/movies/screening",
		data : {
			"movie" : movie.id
		},
		type : "GET",
		cache : false,
		dataType : "json"
	});

	screeningsAJAX.done(function(screenings, textStatus, jqxHR) {
		var screeningsList = newRow.find(".movieScreeningsList");
		screeningsList.empty();

		if (jqxHR.status == 204) {
			// No elements found!
			screeningsList.append($("<span>").css("color","red").text("No screenings found"));
			return;
		} 
		
		var screeningsList = newRow.find(".movieScreeningsList");
		screeningsList.empty();
		$.each(screenings, function(index, screening) {
			var screeningOption = $("<input>", {
				"type" : "radio",
				"name" : "inviteScreeningOption",
				"value" : screening.id
			});
			screeningsList.append(screeningOption);
			screeningsList.append($("<span>").text(screening.theatreName + " - " + screening.theatreLocation));
			screeningsList.append($("<br />"));
		});
	});
	
	screeningsAJAX.fail(function(jqXHR, textStatus, errorThrown) {
		if (jqXHR.status == 404) {
			// No screening
			var screeningsList = newRow.find(".movieScreeningsList");
			screeningsList.empty();
			screeningsList.append($("<span>").css("color","red").text("No screenings found"));
		}
		
	});

	$.when(friendsAJAX, screeningsAJAX).done(function() {
		// Show invite button only if both succeeded;
		newRow.find(".searchResultActions").append("<hr />");

	}).fail(function(jqXHR, textStatus, errorThrown) {
		// Don't show any button if either failed
	});

}

// Initializer
$(document).ready(function () {
    pageInit();
});

var movieBook = {};

function pageInit() {

    // Check for existence of hidden field and initialize appropriately

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

    //attachEventHandlers();

}

function initLoggedIn() {

    updateUserDetails();
    updateUserFriends();
    updateRecommendedMovies();

    $("div.movieSearch").show();

}

 function logoutAction() {
 // Destroy everything and force a reload of page
 movieBook = {};
 $.ajax({
	 url : "api/session",
	 type : "DELETE",
	 cache : false,
 }).done(function() {
	 window.location.href = "login.jsp";
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

	}).done(function(userDetails) {
		window.location.href = 'index.jsp'
	})

}

function updateUserDetails() {

    var user = movieBook.currentUser;

    $("img.userDisplayPic").attr("src", user.profilePhotoPath);
    $("div.userDetails").text("Welcome " + user.name + "!");
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

    }).done(function (friendsList) {
        var friendsListDiv = $("div.friendsList");
        friendsListDiv.empty();
        friendsListDiv.append($("<hr />"));
        friendsListDiv.append($("<h3>").text("Friends List"));
        var table = $("<table></table>");

        for (var i in friendsList) {
    	    var tr = $("<tr></tr>");
            tr.append($("<td>Name:&nbsp;&nbsp;&nbsp;&nbsp;</td>").css("font-weight", "bold"));
            tr.append($("<td>" + friendsList[i].name + "</td>"));
            tr.append($("<td>&nbsp;&nbsp;&nbsp;&nbsp;Email:&nbsp;&nbsp;&nbsp;&nbsp;</td>").css("font-weight", "bold"));
            tr.append($("<td>" + friendsList[i].email + "</td>"));
            tr.append($("<td>&nbsp;&nbsp;&nbsp;&nbsp;Invite status:&nbsp;&nbsp;&nbsp;&nbsp;</td>").css("font-weight", "bold"));
            tr.append($("<td>" + friendsList[i].status + "</td>"));	            
            tr.append($("<tr></tr>"));
            table.append(tr);
        }
        
        friendsListDiv.append(table);
        //friendsListDiv.show();
        
    });

}

function updateRecommendedMovies() {
    $.ajax({

        url : "api/movies/recommended",

        type : "GET",

        dataType : "json",

        cache : false,

    }).done(function (movies) {

        var recElement = $("div.recommendMoviesList");
        recElement.empty();
        recElement.append($("<hr />"));
        recElement.append($("<h3>").text("Recommended Movies"));

        $.each(movies, function (key, movie) {
            // Build the <div> object to house this
            var newMovie = $("<table>");
            var movieRow = newMovie.append($("<tr>"));
            movieRow.append($("<img>").attr("src", movie.posterSmallPath));
            movieRow.append(newMovie.append($("<tr>")));
            movieRow.append($("<td>").text("Title: ").css("font-weight", "bold"));
            movieRow.append($("<td>").text(movie.title));
            movieRow.append(newMovie.append($("<tr>")));
            movieRow.append($("<td>").text("Description: ").css("font-weight", "bold"));
            movieRow.append($("<td>").text(movie.description));
            movieRow.append(newMovie.append($("<tr>")));
            movieRow.append($("<td>").text("Genre: ").css("font-weight", "bold"));
            movieRow.append($("<td>").text(movie.genre));
            movieRow.append(newMovie.append($("<tr>")));
            movieRow.append($("<td>").text("Duration: ").css("font-weight", "bold"));
            movieRow.append($("<td>").text(movie.duration));
            movieRow.append(newMovie.append($("<tr>")));
            movieRow.append($("<td>").text("Language: ").css("font-weight", "bold"));
            movieRow.append($("<td>").text(movie.language));
            movieRow.append(newMovie.append($("<tr>")));
            movieRow.append($("<img>").attr("src", "photos\\home_separator.png"));

            recElement.append(newMovie);
        })

        recElement.show();

    });
}

function movieSearchAction() {

    var searchTerms = {
        "title" : $("input[name='movieSearch']").val()
    };

    $.ajax({
        url : "api/movies/search",

        type : "GET",

        data : searchTerms,

        dataType : "json",

        cache : false,

        statusCode : {
            404 : function () {
                // No results found
                $(".searchResultsTable").hide();
                $(".searchResultsTable").after($("<div>").addClass("noResultsFound").css("color", "red").text("No results found"));
            }
        }

    }).done(function (results) {

        // Remove results not found if any
        if ($(".searchResults > .noResultsFound").length)
            $(".searchResults > .noResultsFound").remove();

        // Some results found
        var resultRow = $(".searchResultsTable .searchResultsRow:first-child");
        resultRow.detach();
        var table = $(".searchResultsTable");
        table.hide();
        table.empty();

        $.each(results, function (index, movie) {
            var newRow = resultRow.clone();
            updateMovieSearchResultDetails(newRow, table, movie);
            updateMovieSearchResultActions(newRow, table, movie);
        })

        table.show();

    });

}

function updateMovieSearchResultDetails(newRow, table, movie) {
    newRow.find(".searchResultTitle").text(movie.title);

    newRow.find(".searchResultGenres").text($(movie.genre).map(function () {
        return (this);
    }).get().join(", ")).css("font-style", "italic");
    newRow.find(".searchResultDescription").text(movie.description);
    table.append(newRow);

}

function updateMovieSearchResultActions(newRow, table, movie) {

    var notFound = false;

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

    friendsAJAX.done(function (friends, textStatus, jqxHR) {
        var friendsList = newRow.find(".interestedFriendsList");
        friendsList.empty();

        if (jqxHR.status == 204) {
            // No Friends found
            friendsList.append($("<span>").css("color", "red").text("No interested friends."));
            notFound = true;
            return;
        }

        // Setup friends
        $.each(friends, function (index, friend) {
            var selectOption = $("<input>", {
                "type" : "checkbox",
                "name" : "inviteFriendOption" + movie.id,
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

    screeningsAJAX.done(function (screenings, textStatus, jqxHR) {
        var screeningsList = newRow.find(".movieScreeningsList");
        screeningsList.empty();

        if (jqxHR.status == 204) {
            // No elements found!
            screeningsList.append($("<span>").css("color", "red").text("No screenings found"));
            notFound = true;
            return;
        }

        var screeningsList = newRow.find(".movieScreeningsList");
        screeningsList.empty();
        $.each(screenings, function (index, screening) {
            var screeningOption = $("<input>", {
                "type" : "radio",
                "name" : "inviteScreeningOption" + movie.id,
                "value" : screening.id
            });
            screeningsList.append(screeningOption);
            screeningsList.append($("<span>").text(screening.theatreName + " - " + screening.theatreLocation));
            screeningsList.append($("<br />"));
            screeningsList.append($("<span>").text(convertLocalDateTimeToString(screening.screeningDateTime)));
            screeningsList.append($("<br />"));

        });
    });

    screeningsAJAX.fail(function (jqXHR, textStatus, errorThrown) {
        if (jqXHR.status == 404) {
            // No screening
            var screeningsList = newRow.find(".movieScreeningsList");
            screeningsList.empty();
            screeningsList.append($("<span>").css("color", "red").text("No screenings found"));
        }

    });

    $.when(friendsAJAX, screeningsAJAX).done(function () {
        // Show invite button only if both succeeded;
        if (!notFound) {
            // Create the invite button
            var inviteButton = $("<button>", {
                "type" : "button",
                "text" : "Invite"
            });
            inviteButton.data("movie", movie.id);

            inviteButton.click(handleInviteAction);

            var attachPoint = newRow.find(".inviteActions");
            attachPoint.empty();

            attachPoint.append($("<hr />"));
            attachPoint.append(inviteButton);

        }

    }).fail(function (jqXHR, textStatus, errorThrown) {
        // Don't show any button if either failed
        var attachPoint = newRow.find(".inviteActions");
        attachPoint.empty();
    });

}

function convertLocalDateTimeToString(dateTime) {
    return dateTime.date.day.toString().padStart(2, "0") + "/" + dateTime.date.month.toString().padStart(2, "0") + "/" + dateTime.date.year.toString() + " "
            + dateTime.time.hour.toString().padStart(2, "0") + ":" + dateTime.time.minute.toString().padStart(2, "0");
}

function handleInviteAction() {

    idT = $(this).data("movie");
    var inviteData = {};

    var clearStatusMessage = function (attachPoint) {
        console.log("clearStatusMessage firing!");
        console.log("clear html:\n" + attachPoint.html());
        attachPoint.find(".inviteActionMessage").remove();
    }

    var addStatusMessage = function (message, color, attachPoint) {
        console.log("addStatusMessage firing!");
        console.log("message is " + message);
        console.log("color is " + color);
        console.log("attach html:\n" + attachPoint.html());
        attachPoint.append($("<div>").addClass("inviteActionMessage").css("color", color).text(message));
    }

    /* Handle friends */
    var selectedUserElements = $(this).parent().parent().find("input[name=inviteFriendOption" + idT + "]:checked");

    
    if (selectedUserElements.length == 0) {
        // No elements selected!
        clearStatusMessage($(this).parent());
        addStatusMessage("No friends selected! Please select a friend.", "red", $(this).parent());
        return;
    }

    selectedUserElements.each(function (index, element) {
        if (!inviteData.hasOwnProperty("friend")) {
            inviteData["friend"] = $(element).val();
        } else {
            inviteData["friend"] = inviteData["friend"] + "," + $(element).val();
        }

    });

    /* Handle screenings */
    var selectedScreeningElements = $(this).parent().parent().find("input[name=inviteScreeningOption" + idT + "]:checked");

    if (selectedScreeningElements.length == 0) {
        // No elements selected!
        clearStatusMessage($(this).parent());
        addStatusMessage("No screening selected! Please select a screening.", "red", $(this).parent());
        return;
    } else {
        inviteData["screening"] = $(selectedScreeningElements).val();
    }

    console.log(JSON.stringify(inviteData));
    var attachPoint = $(this).parent();
    console.log(attachPoint.html());

    // Make the AJAX call
    $.ajax({
        url : "api/events/invite",
        data : inviteData,
        type : "POST",
        dataType : "text",
        cache : false
    }).done(function () {
        console.log("Success call for events invite");
        clearStatusMessage(attachPoint);
        addStatusMessage("Invite successfully sent.", "green", attachPoint);

    }).fail(function () {
        clearStatusMessage(attachPoint);
        addStatusMessage("Error when sending invite. Please contact system administrators", "red", attachPoint);

    });

}
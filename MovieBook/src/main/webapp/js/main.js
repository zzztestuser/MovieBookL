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

    $("div.loginInfo > img.userDisplayPic").attr("src", user.profilePhotoPath);
    $("div.loginInfo > p.userDetails").text(user.name);
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
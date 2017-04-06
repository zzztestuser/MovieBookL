// Initializer
$(document).ready(function() {
	pageInit();
});

function pageInit() {

	// Check for existence of hidden field and initalize appropriately

	var userInfo = $("#currentUser")

	if (userInfo.length == 0) {
		// Show login form
		$("form#loginForm").show();
		$("div.loginInfo").hide();
	} else {
		// Logged in. Retrieve user details first then populate
		var user = JSON.parse(userInfo.text());
		sessionStorage.setItem("currentUser", JSON.stringify(user));
		updateUserDetails();
	}

	attachEventHandlers();

}

function attachEventHandlers() {
	$("button.logout").click(logoutAction);
}

function logoutAction() {
	// Destroy everything and force a reload of page
	sessionStorage.clear();
	$.ajax({

		url : "login/user",

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

		url : "login/user",

		type : "POST",

		data : loginData,

		dataType : "json",

		cache : false,

	}).done(function(userJson) {

		sessionStorage.setItem("currentUser", JSON.stringify(userJson));
		updateUserDetails();

	});

};

function updateUserDetails() {

	var user = JSON.parse(sessionStorage.getItem("currentUser"));

	$("div.loginInfo > img.userDisplayPic").attr("src", user.profilePhotoPath);
	$("div.loginInfo > p.userDetails").text(user.name);
	$("form#loginForm").hide();
	$("div.loginInfo").show();
}

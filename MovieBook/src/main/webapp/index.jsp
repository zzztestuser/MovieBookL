<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" import="com.moviebook.bean.user.UserBean"%>

<!DOCTYPE html>
<html>
<head>
<title>Home</title>
<meta charset="utf-8">
<meta name = "format-detection" content = "telephone=no" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="icon" href="images/favicon.ico">
<link rel="shortcut icon" href="images/favicon.ico" />
<link rel="stylesheet" href="css/touchTouch.css">
<link rel="stylesheet" href="css/camera.css">
<link rel="stylesheet" href="css/style.css">
<link rel="stylesheet" href="css/font-awesome-min-moviebook.css">
<script src="js/jquery.js"></script>
<script src="js/jquery-migrate-1.1.1.js"></script>
<script src="js/jquery.easing.1.3.js"></script>
<script src="js/script.js"></script>
<script src="js/superfish.js"></script>
<script src="js/jquery.equalheights.js"></script>
<script src="js/jquery.mobilemenu.js"></script>
<script src="js/tmStickUp.js"></script>
<script src="js/jquery.ui.totop.js"></script>
<script src="js/touchTouch.jquery.js"></script>
<script src="js/camera.js"></script>
<!--[if (gt IE 9)|!(IE)]><!-->
<script src="js/jquery.mobile.customized.min.js"></script>
<!--<![endif]-->
<script>
 $(window).load(function(){
  $().UItoTop({ easingType: 'easeOutQuart' });
  $('#camera_wrap').camera({
    loader: false,
    pagination: false ,
    minHeight: '400',
    thumbnails: true,
    height: '46.32478632478632%',
    caption: false,
    navigation: false,
    fx: 'mosaic'
  });
  $('.gallery .gall_item').touchTouch();
 });
</script>



<script>
 $( "#emailbutton" ).button().on( "click", function() {
     //for (i=0; i<rows; i++) {
         //do something
     //}
 //});
 } );
</script>

<style>
#bg {
  position: fixed; 
  top: -50%; 
  left: -50%; 
  width: 200%; 
  height: 200%;
}
#bg img {
  position: absolute; 
  top: 0; 
  left: 0; 
  right: 0; 
  bottom: 0; 
  margin: auto; 
  min-width: 50%;
  min-height: 50%;
  opacity: 1.0;
}

.emailbutton {
    background-color: #4CAF50; /* Green background */
    border: none; /* Remove borders */
    color: white; /* White text */
    padding: 12px 24px; /* Some padding */
    border-radius: 10px;
    font-size: 16px; /* Set a font-size */
    text-align: center;
}

.fa {
  padding: 10px;
  font-size: 25px;
  width: 25px;
  text-align: center;
  text-decoration: none;
  margin: 5px 2px;
  border-radius: 50%;
}

.fa:hover {
    opacity: 0.7;
}

.fa-facebook {
  background: #3B5998;
  color: white;
}

.fa-twitter {
  background: #55ACEE;
  color: white;
}

.fa-google {
  background: #dd4b39;
  color: white;
}

/* Add a right margin to each icon */
.fal {
    margin-left: -12px;
    margin-right: 8px;
}

/* The Modal (background) */
.modal {
    display: none; /* Hidden by default */
    position: fixed; /* Stay in place */
    z-index: 1; /* Sit on top */
    padding-top: 100px; /* Location of the box */
    left: 0;
    top: 0;
    width: 100%; /* Full width */
    height: 100%; /* Full height */
    overflow: auto; /* Enable scroll if needed */
    background-color: rgb(0,0,0); /* Fallback color */
    background-color: rgba(0,0,0,0.4); /* Black w/ opacity */
}

/* Modal Content */
.modal-content {
    position: relative;
    background-color: #fefefe;
    margin: auto;
    padding: 0;
    border: 1px solid #888;
    width: 50%;
    box-shadow: 0 4px 8px 0 rgba(0,0,0,0.2),0 6px 20px 0 rgba(0,0,0,0.19);
    -webkit-animation-name: animatetop;
    -webkit-animation-duration: 0.4s;
    animation-name: animatetop;
    animation-duration: 0.4s
}

/* Add Animation */
@-webkit-keyframes animatetop {
    from {top:-300px; opacity:0} 
    to {top:0; opacity:1}
}

@keyframes animatetop {
    from {top:-300px; opacity:0}
    to {top:0; opacity:1}
}

/* The Close Button */
.close {
    color: white;
    float: right;
    font-size: 28px;
    font-weight: bold;
}

.close:hover,
.close:focus {
    color: #000;
    text-decoration: none;
    cursor: pointer;
}

/* The Close2 Button */
.close2 {
    color: white;
    float: right;
    font-size: 28px;
    font-weight: bold;
}

.close2:hover,
.close2:focus {
    color: #000;
    text-decoration: none;
    cursor: pointer;
}

.modal-header {
    padding: 2px 16px;
    background-color: #f3ebac;
    color: black;
}

.modal-body {padding: 2px 16px;}

.modal-footer {
    padding: 2px 16px;
    background-color: #f3ebac;
    color: black;
}

/* 100% Image Width on Smaller Screens */
@media only screen and (max-width: 700px){
    .modal-content {
        width: 600px;
        height: 500px;
    }
}
</style>

</head>

<body class="page1" id="top">
<%! public int cont; %>
<div id="bg">
  <img src="images/background-page.jpg" alt="">
</div>
<div class="loginInfo">
		<img src="" alt="Profile picture" class="userDisplayPic" />
		<p class="userDetails">User Details</p>		
	</div>
<!--==============================
              header
=================================-->
<div class="main">
<header>
  <div class="container_12">
    <div class="grid_12">
      <h1 class="logo"><b>Movie Book</b><span>Movie!Up with your friends </span> </h1>      
      <!-- <table width="335" border="1">-->
      <table border="1">
        <tr>
          <td width="378" height="28"></td>          
          <td width="46">search:</td>
          <td width="189"><form name="form1" method="post" action="">
            <label for="search"></label>
            <input type="text" name="search" id="search">
          </form></td>
          <td width="60"><button type="button" id="logoutButton" onclick="logoutAction()">Logout</button></td>
        </tr>
      </table>
    </div>
    <div class="clear"></div>
  </div>
  <section id="stuck_container">
  <!--==============================
              Stuck menu
  =================================-->
    <div class="container_12">
        <div class="grid_12">
          <div class="navigation ">
            <nav>
              <ul class="sf-menu">
               <li class="current"><a href="index.html">Home</a></li>
               <li><a href="about.html">About</a></li>
               <li><a href="friends.html">Friends</a></li>
               <li><a href="invitations.html">Invitations</a></li>
               <li><a href="contacts.html">Contacts</a></li>
             </ul>
            </nav>
            <div class="clear"></div>
          </div>
         <div class="clear"></div>
     </div>
     <div class="clear"></div>
    </div>
  </section>
<section class="slider_wrapper">
  <div class="container_12">
    <div class="grid_12">
      <div id="camera_wrap">
        <div data-src="images/12 Years a Slave_L.jpg"data-thumb="images/12 Years a Slave_S.jpg"></div>
        <div data-src="images/Shawshank_L.jpg" ></div>
        <div data-src="images/Frozen_L.jpg"></div>
      </div>
    </div>
    <div class="clear"></div>
  </div>
</section>
</header>
</div>
<!--=====================
          Content
======================-->
<div class="recommendation">
<div class="container_12">
    <div class="grid_12">
 <h3 style='color:#FF00FF'>Recommendations for you:</h3>
 <br>
</div></div></div>
 <br>
 <br>
  <div class="container_12">
    <div class="grid_12">    
<!--<a href="#none" onclick="window.open('invite.html','popup', 'width=600, height=500, scrollbars=auto'); return false;">	<img src="images/Divergent_M.jpg" id="diver1" alt="" class="fleft"></a>-->
<table border="1">
	<tr>
	<td rowspan=4>
        <img src="images/Divergent_M.jpg" id="diver1" alt="" class="fleft">
        </td>
        <td><h2>Divergent</h2></td>
        </tr>
        <tr><td>PG13 | 2h 19min | Adventure, Mystery, Sci-Fi</td>
</tr>
<tr><td>Director: Neil Burger; Writer: Evan Daugherty, Vanessa Taylor, Veronica Roth; Stars: Shailene Woodley, Theo James, Ashley Judd<br>
In a world divided by factions based on virtues, Tris learns she's Divergent and won't fit in. When she discovers a plot to destroy Divergents, Tris and the mysterious Four must find out what makes Divergents dangerous before it's too late.</td></tr>
<tr>
<td>
    F1, F2, F3
    </td>
</tr>  
</table>
<br>

<table border="1">
	<tr>
	<td rowspan=4>
        <img src="images/Frozen_M.jpg" alt="" class="fleft" id="frozen1">
        </td>
        <td><h2>Frozen</h2></td>
        </tr>
        <tr><td>G | 1h 42min | Animation, Adventure, Comedy</td>
</tr>
<tr><td>When the newly-crowned Queen Elsa accidentally uses her power to turn things into ice to curse her home in infinite winter, her sister, Anna, teams up with a mountain man, his playful reindeer, and a snowman to change the weather condition.</td></tr>
<tr>
<td>
    F1, F2, F3
    </td>
</tr>  
</table>      
<br>    

<table border="1">
	<tr>
	<td rowspan=4>
        <img src="images/Her_M.jpg" alt="" class="fleft" id="her1">
        </td>
        <td><h2>Her</h2></td>
        </tr>
        <tr><td>G | 1h 42min | Animation, Adventure, Comedy</td>
</tr>
<tr><td>Director: Spike Jonze; Writer: Spike Jonze; Stars: Joaquin Phoenix, Amy Adams. <br>
A lonely writer develops an unlikely relationship with an operating system designed to meet his every need.</td></tr>
<tr>
<td>
    F1, F2, F3
    </td>
</tr>  
</table>
<br>
 <img align="middle" src="images/home_separator.png" alt="---------------------"> 
 <br>
 <h3 style='color:#FF00FF'>Your friends also like...</h3> 
 <br>
    <table border="1">
	<tr>
	<td rowspan=4>
        <img src="images/Edge of Tomorrow_M.jpg" alt="" class="fleft" id="edge1">
        </td>
        <td><h2>Edge of Tomorrow</h2></td>
        </tr>
        <tr><td>G | 1h 53min | Action, Adventure, Sci-Fi </td>
</tr>
<tr><td>Director: Doug Liman; Writers: Christopher McQuarrie (screenplay), Jez Butterworth (screenplay); Stars: Tom Cruise, Emily Blunt, Bill Paxton<br>
A soldier fighting aliens gets to relive the same day over and over again, the day restarting every time he dies.</td></tr>
<tr>
<td>
    F1, F2, F3
    </td>
</tr>  
</table>
<br>
<table border="1">
	<tr>
	<td rowspan=4>
        <img src="images/Ender Game_M.jpg" alt="" class="fleft" id="end1">
        </td>
        <td><h2>Ender's Game</h2></td>
        </tr>
        <tr><td>G | 1h 54min | Action, Sci-Fi </td>
</tr>
<tr><td>Director: Gavin Hood; Writers: Gavin Hood (screenplay), Orson Scott Card (based on the book Ender's Game); Stars: Harrison Ford, Asa Butterfield, Hailee Steinfeld<br>
Young Ender Wiggin is recruited by the International Military to lead the fight against the Formics, a genocidal alien race which nearly annihilated the human race in a previous invasion.</td></tr>
<tr>
<td>
    F1, F2, F3
    </td>
</tr>  
</table> 
<br>
<table border="1">
	<tr>
	<td rowspan=4>
        <img id="name1" src="images/Your Name_M.jpg" width="182" height="268" alt="" class="fleft">
        </td>
        <td><h2>Your Name</h2></td>
        </tr>
        <tr><td>G | 1h 46min | Animation, Drama, Fantasy </td>
</tr>
<tr><td>Director: Makoto Shinkai; Writers: Clark Cheng (english script), Makoto Shinkai; Stars: Ryûnosuke Kamiki, Mone Kamishiraishi<br>
Two strangers find themselves linked in a bizarre way. When a connection forms, will distance be the only thing to keep them apart?</td></tr>
<tr>
<td>
    F1, F2, F3
    </td>
</tr>  
</table>
    </div>
      </div>
    <br>
<div class="grid_5 prefix_1 gallery"> </div>
    <div class="clear"></div>
<!--==============================
              footer_top
=================================-->
<div class="footer-top">
  <div class="container_12">
    <div class="grid_12">
    <br>
      <div class="sep-1"></div>      
    </div>
    <div class="grid_4">
      <address class="address-1"> <span class="fa fa-home"></span>Nanyang Technological University, WKWSCI  <br>
CI6206 Internet Programming (Assignment 2)</address>
    </div>
    <div class="grid_3">
      <a href="MovieBook@ilovemovie.ntu.sg" class="mail-1"><span class="fa fa-envelope"></span>MovieBook@ilovemovie.ntu.sg</a>
    </div>
    <div class="grid_4 fright">
        <div class="socials">
            <a href="#">facebook</a>
            <a href="#">twitter</a>
            <a href="#">google+</a>
        </div>
    </div>
    <div class="clear"></div>
  </div>
</div>
<!--==============================
       Invite_Friends_Window
=================================-->
<div id="myModal" class="modal">

    <!-- Modal content -->
    <div class="modal-content">
        <div class="modal-header">
            <span class="close">&times;</span>
            <h4 style="color:black">Movie Details</h4>
        </div>
        <div class="modal-body" id="modalb">
       <table border="1">
	<tr>
	<td rowspan=4>
        <img id="name1" src="images/Your Name_M.jpg" width="182" height="268" alt="" class="fleft">
        </td>
        <td><h2>Your Name</h2></td>
        </tr>
        <tr><td>G | 1h 46min | Animation, Drama, Fantasy </td>
</tr>
<tr><td>Director: Makoto Shinkai; Writers: Clark Cheng (english script), Makoto Shinkai; Stars: Ryûnosuke Kamiki, Mone Kamishiraishi<br>
Two strangers find themselves linked in a bizarre way. When a connection forms, will distance be the only thing to keep them apart?</td></tr>
<tr>
<td>
    F1, F2, F3
    </td>
</tr>  
</table>
        </div>
        <div class="modal-footer">
            <p>&nbsp;</p>            
            <p>
            <button class="emailbutton" id="like" style="float: left; background-color: #0000FF">Like</button>
                <button class="emailbutton" id="email" style="float: right;">E-mail Invitation</button>
            </p>
            <div style="clear: both;"></div>
        </div>
    </div>
</div>

<!--==============================
       Confirm_Invite_Window
=================================-->
<div id="myModal2" class="modal">

    <!-- Modal content -->
    <div class="modal-content">
        <div class="modal-header">
            <span class="close2">&times;</span>
            <h4 style="color:black">Confirm Invitation</h4>
        </div>
        <div class="modal-body">
            <p>Details of Invitation:</p>
            <p>Some text...</p>
            <p>&nbsp;</p>
            <p>&nbsp;</p>
        </div>
        <div class="modal-footer">
            <p>&nbsp;</p>            
            <p>
                <button class="emailbutton" style="float: right;">Confirm</button>
            </p>
            <div style="clear: both;"></div>
        </div>
    </div>

</div>

<script>
// Get the modal
var modal = document.getElementById('myModal');
var modal2 = document.getElementById('myModal2');

// Get the image and insert it inside the modal - use its "alt" text as a caption
var img1 = document.getElementById('diver1');
img1.onclick = function () {
    cont = 1;
    modal.style.display = "block";
}

var img2 = document.getElementById('frozen1');
img2.onclick = function () {
    cont = 2;
    modal.style.display = "block";
}

var img3 = document.getElementById('her1');
img3.onclick = function () {
    cont = 3;
    modal.style.display = "block";
}

// Get the image and insert it inside the modal - use its "alt" text as a caption
var img4 = document.getElementById('edge1');
img4.onclick = function () {
    cont = 4;
    modal.style.display = "block";
}

var img5 = document.getElementById('end1');
img5.onclick = function () {
    cont = 5;
    modal.style.display = "block";
}

var img6 = document.getElementById('name1');
img6.onclick = function () {
    cont = 6;
    modal.style.display = "block";
}

email.onclick = function () {    
    modal2.style.display = "block";
}

// Get the <span> element that closes the modal
var span = document.getElementsByClassName("close")[0];
var span2 = document.getElementsByClassName("close2")[0];

// When the user clicks on <span> (x), close the modal
span.onclick = function() {
    modal.style.display = "none";
}

span2.onclick = function() {
    modal2.style.display = "none";
}

// When the user clicks anywhere outside of the modal, close it
window.onclick = function(event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
	if (event.target == modal2) {
        modal2.style.display = "none";
		modal.style.display = "none";
    }
}
</script>

<!--==============================
              footer
=================================-->
<footer id="footer">
  <div class="container_12">
    <div class="grid_12" >
      <div class="sub-copy" style='color:#708090'>MovieBook &copy; <span id="copyright-year"></span> | <a href="#">Privacy Policy</a> <br> Website designed by <a href="http://www.MovieBook.com/" rel="nofollow">MovieBook.com</a></div>
    </div>
    <div class="clear"></div>
  </div>
</footer>
<a href="#" id="toTop" class="fa fa-angle-up"></a>
</body>
</html>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Bengaluru Police</title>

    <!-- Bootstrap Core CSS -->
    <link href="css/bootstrap.min.css" rel="stylesheet">

    <!-- MetisMenu CSS -->
    <link href="css/metisMenu.min.css" rel="stylesheet">

    <!-- Timeline CSS -->
    <link href="css/timeline.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="css/startmin.css" rel="stylesheet">

    <!-- Morris Charts CSS -->
    <link href="css/morris.css" rel="stylesheet">

    <!-- Custom Fonts -->
    <link href="css/font-awesome.min.css" rel="stylesheet" type="text/css">
	
	<!-- Other Css-->
	<link href="css/index.css" rel="stylesheet" type="text/css">
<script src="https://www.gstatic.com/firebasejs/4.10.1/firebase.js"></script>
<script type="text/javascript">
var uid,crimeID,htmlText="",i=0;
function displayCrimes()
{
	var config = {
    apiKey: "AIzaSyAnv_hl0J-kXjngPRhaRQekjZRuGE9yOuY",
    authDomain: "smarttraumarelieverflawsome.firebaseapp.com",
    databaseURL: "https://smarttraumarelieverflawsome.firebaseio.com/"
  };
  firebase.initializeApp(config);
	var query = firebase.database().ref("users").orderByKey();
	i=0;
query.once("value")
  .then(function(snapshot) {
    snapshot.forEach(function(childSnapshot) {
     
      var uid = childSnapshot.key;
      var snap = childSnapshot.toJSON();
	  var name = snap.name;
	  var phone = snap.phone;
	  var crimeSnap = snap.crimes;
	  if(crimeSnap!=null || crimeSnap!=undefined)
	  {
	  var crimeIDs= Object.keys(crimeSnap);
	 
	  var crimeID,x;
		for(x=0;x<crimeIDs.length;x++)
		{
			crimeID=crimeIDs[x];
			
	  var desc = crimeSnap[crimeID].description;
	  var date= crimeSnap[crimeID].reportedTimestamp.date;
	  var month= crimeSnap[crimeID].reportedTimestamp.month;
	  var hour= crimeSnap[crimeID].reportedTimestamp.hours;
	  var minutes= crimeSnap[crimeID].reportedTimestamp.minutes;
	  var sec= crimeSnap[crimeID].reportedTimestamp.seconds;


	  var solvedTF= crimeSnap[crimeID].solvedDetails.solved;
	  var solvedTime= crimeSnap[crimeID].solvedDetails.solvedTimestamp;
		htmlText="<div><div class='crimes'><div class='crimeID'>Crime ID:"+crimeID+"</div>"+
				"<div class='reportedBy'>Reported By: "+name+"</div>"+
				"<div class='contact'>Contact: "+phone+"</div>"+
				"<div class='reportedTime'>Time of Reporting: "+hour+":"+minutes+":"+sec+" , "+date+"/"+month+"</div>"+
				"<div class='description'>Description of Crime: "+desc+"</div>"+
				"<div id='"+crimeID+"' class='solve'>Solved?  "+
					"<input type='radio' id='yes"+crimeID+"' name='solved"+crimeID+"' value='yes' onchange='databaseUpdate("+crimeID+");'> Yes</input>"+				
					"<input type='radio' id='no"+crimeID+"' name='solved"+crimeID+"' value='no' onchange='databaseUpdate("+crimeID+");'> No</input>"+
				"</div></div></div></br>";		
				var divs=document.createElement('div');
				divs.innerHTML=htmlText;
				document.getElementById('allcrimes').appendChild(divs);
		}
	  }		
	});
});
}
function databaseUpdate(i)
{
	
	var elem= false;
	if(document.getElementById('yes'+i).checked)
		elem = true;
	else
		elem = false;
 
  // Get a reference to the database service
  
  var database = firebase.database();
   
 
 var timestamp= Math.floor(Date.now() / 1000);
 var query = firebase.database().ref("users").orderByKey();
query.once("value")
  .then(function(snapshot) {
    snapshot.forEach(function(childSnapshot) {
     
      var uid = childSnapshot.key;
      var snap = childSnapshot.toJSON();
	  var name = snap.name;
	  var phone = snap.phone;
	  var crimeSnap = snap.crimes;
	  if(crimeSnap!=null || crimeSnap!=undefined)
	  {
	  var crimeIDs= Object.keys(crimeSnap);
	  var crimeID,x;
		for(x=0;x<crimeIDs.length;x++)
		{
			crimeID=crimeIDs[x];
	  if(crimeID == i)
	  {
	  var desc = crimeSnap[crimeID].description;
	  var reportedTime= crimeSnap[crimeID].reportedTimestamp;
	  var solvedTF= crimeSnap[crimeID].solvedDetails.solved;
	  var solvedTime= crimeSnap[crimeID].solvedDetails.solvedTimestamp;
	  database.ref('users/' + uid + "/crimes/"+crimeID+"/solvedDetails").set({
		solved:elem,
		solvedTimestamp:timestamp
	  
	  
  });
	  }
	  }
	  }
	});
  });
  
} 
</script>
   
</head>
<body onload=displayCrimes();>

<div id="wrapper">

    <!-- Navigation -->
    <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
        <div class="navbar-header">
            <a class="navbar-brand" href="#">Bengaluru Police</a>
        </div>

        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
        </button>

        <!-- Top Navigation: Left Menu -->
        <ul class="nav navbar-nav navbar-left navbar-top-links">
            <li><a href="#"><i class="fa fa-home fa-fw"></i> Website</a></li>
        </ul>

        <!-- Top Navigation: Right Menu -->
        <ul class="nav navbar-right navbar-top-links">
            <li class="dropdown navbar-inverse">
                <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                    <i class="fa fa-bell fa-fw"></i> <b class="caret"></b>
                </a>
                
            </li>
            <li class="dropdown">
                <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                    <i class="fa fa-user fa-fw"></i> secondtruth <b class="caret"></b>
                </a>
                <ul class="dropdown-menu dropdown-user">
                    <li><a href="#"><i class="fa fa-user fa-fw"></i> User Profile</a>
                    </li>
                    <li><a href="#"><i class="fa fa-gear fa-fw"></i> Settings</a>
                    </li>
                    <li class="divider"></li>
                    <li><a href="#"><i class="fa fa-sign-out fa-fw"></i> Logout</a>
                    </li>
                </ul>
            </li>
        </ul>

        <!-- Sidebar -->
        <div class="navbar-default sidebar" role="navigation">
            <div class="sidebar-nav navbar-collapse">

                <ul class="nav" id="side-menu">
                    <li class="sidebar-search">
                        <div class="input-group custom-search-form">
                            <input type="text" class="form-control" placeholder="Search...">
                                <span class="input-group-btn">
                                    <button class="btn btn-primary" type="button">
                                        <i class="fa fa-search"></i>
                                    </button>
                                </span>
                        </div>
                    </li>
                    <li>
                        <a href="#" class="active"><i class="fa fa-dashboard fa-fw"></i> Dashboard</a>
                    </li>
                    
                </ul>

            </div>
        </div>
    </nav>

    <!-- Page Content -->
    <div id="page-wrapper">
        <div class="container-fluid">

            <div class="row">
                <div class="col-lg-12">
                    <h1 class="page-header">Bengaluru Police - Crime Dashboard</h1>
                </div>
            </div>
			<div id="CrimeTitle">All Crimes</div>
				<div id="allcrimes"></div>
        </div>
    </div>

</div>

<!-- jQuery -->
<script src="js/jquery.min.js"></script>

<!-- Bootstrap Core JavaScript -->
<script src="js/bootstrap.min.js"></script>

<!-- Metis Menu Plugin JavaScript -->
<script src="js/metisMenu.min.js"></script>

<!-- Custom Theme JavaScript -->
<script src="js/startmin.js"></script>

</body>
</html>

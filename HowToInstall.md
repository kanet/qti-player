## Download and Check demo ##
  1. Download latest distributions from **Downloads** tab
  1. Unzip archive to _yourdir_
  1. open _yourdir_/demo.html in web browser. You should be able to work with demo content.

## Include player in your web page ##
  1. copy _yourdir_/qtiplayer folder to you website
  1. Include it in you web page
  1. copy defaultplayer.css or create new CSS and include it in your webpage
  1. Upload content (QTI files)
  1. create tag for player in web page with id
  1. Declare qpOnAppLoaded() function and create player there
  1. If you want to send result to the server then declare onAssessmentFinished() function

## Sample web page ##
```
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <link type="text/css" rel="stylesheet" href="defaultplayer.css">
    <title>QTI Player demo</title>
    
    <script type="text/javascript" language="javascript" src="qtiplayer/qtiplayer.nocache.js"></script>
    <script language="javascript">
        // Create player and load content
    	function qpOnAppLoaded(){
    	  var player = qpCreatePlayer('myplayer');
    	  player.load('content/test.xml');
    	}

 	// Optional function to handle assessment result
 	player.onAssessmentFinished = function(result){
          alert('Score: ' + result.score + ' out of ' + result.max);
    	}

    </script>

  </head>
  <body>

    <div id="myplayer"/>
	
  </body>
</html>

```
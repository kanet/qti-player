**IMPORTANT**

**PLEASE NOTE THE CHANGE IN THE NAME OF THE CALLBACK FUNCTION CALLED WHEN THE ASSESSMENT IS FINISHED onAssessmentFinished -> onAssessmentSessionFinished**

## Introduction ##

SCORM is [ADL standard](http://www.adlnet.gov/Technologies/scorm/default.aspx) for storing test results on the server (usually it is LMS).

To add SCORM support:
  1. Upload scorm folder to you browser and include APIWrapper.js file
  1. Define **onAssessmentSessionFinished**() function and send results using SCOMR protocol.

## Sample HTML fragment with SCORM support: ##
```
<script type="text/javascript" language="javascript" src="scorm/APIWrapper.js"></script>
<script language="javascript">
  function qpOnAppLoaded(){
    // Load assessment
    var player = qpCreatePlayer('player');
    player.load('content/test.xml');

    // Send results via SCORM
    player.onAssessmentSessionFinished = function(){
      doLMSInitialize();
      doLMSSetScore(player.getResult().getScore(), player.getResult().getMaxScore());
      doLMSCommit();
      doLMSFinish();
  }

</script>
```
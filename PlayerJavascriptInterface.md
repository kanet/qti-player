## Introduction ##

This article explains the usage of javascript interface of the _qtiplayer_ object.


## Java Script interface features ##
The description of crucial issues in the qtiplayer JS interface.

### + Loading XML contents ###

XML contents may be loaded into application in three ways:
  * through JavaScript API, whole _assessmentTest_ XML
  * through JavaScript API, a single _assessmentItem_ XML
  * through GWT API, see [details](http://code.google.com/p/qti-player/wiki/PlayerGWTInterface)

While qtiplayer application is initialized, content file may be loaded using `load` function. This function takes one argument - the URL to the XML file.

The mentioned XML file should contain either `<assessmentTest>` node or `<assessmentItem>` node. The contents will be automatically recognized and qtiplayer will display either whole Assessment or single item. Don't forget to set proper Flow Options in order to obtain single Item view.

### + Navigation ###
Navigation is the control over the Assessment Session and Assessment Item Session flows. Can be done when the _Engine Mode_ is _RUNNING_.

### + Engine Mode ###
Mode control provides the information about the player operation. It could carry the String values as follows:

_NONE_ – player does not function,

_ASSESSMENT\_LOADING_ – delivery engine is loading the assessment file,

_ASSESSMENT\_LOADED_ – the Assessment file has been loaded, history initialized, time counting started,

_ITEM\_LOADING_ – the delivery engine is loading the Assessment Item (first or subsequent),

_ITEM\_LOADED_ – the Assessment Item file has been loaded, the state for the Item has been loaded,

_RUNNING_ – the Assessment is running, awaiting for the user action; this is the engine mode that enables the navigation through the assessment items (as well as _PREVIEW_).

_PREVIEW_ – the Assessment is in preview mode;

_FINISHED_ – the Assessment has been finished; if the engine mode is _FINISHED_ user can navigate through assessment items in preview mode (so, when visiting a page, the state is _PREVIEW_ and all modules are locked)

### + Lesson Status ###
Provides the information about the status of the current lesson. Possible values are:

_INCOMPLETE_ – all pages have not been visited yet,

_COMPLETED_ – all pages have been visited and the maximum score for the assessment is 0,

_PASSED_ – all pages have been visited and the user score is equal to or higher than the mastery score,

_FAILED_ – all pages have been visited and the user score is lower than the mastery score,

### + Assessment Session State ###
Assessment session state control concerns the management over the content introduced by the user. The state **cannot** be controlled if the _Engine Mode_ is _NONE or_ASSESSMENT\_LOADING_._

### + Assessment Session Report ###
Provides the information about the current Assessment and Item Sessions features.

### + Flow Options ###
Flow options are parameters that control the flow of the content in the player. There are 3 variables in the flow options:
  * showToC (true/false) - show the Table of Contents page
  * showSummary (true/false) - show the Summary page
  * itemsDisplayMode("ONE"/"ALL") - determines if only one item per page or all items per page are shown (the "ALL" mode is dedicated for enabling the print feature)
  * showCheck (true/false) - show or hide the "Check" button
Flow option should be initialized by `setFlowOptions` function **before** loading the contents!

### + JavaScript Events ###
JavaScript events are callback functions that could be defined by the user and while defined will be called by the player in crucial lifecycle moments. These functions are:
  * onAssessmentLoaded - called after the contents are loaded and player is initialized, before player view is displayed (initial page index can be set here)
  * onAssessmentSessionBegin - called after the view is displayed and qtiplayer session has begun (show answers commang can be placed here)
  * onAssessmentSessionFinished - called when Summary page is displayed
  * onTestPageSwitching - called before the test page is quitted
  * onTestPageSwitching - called after the test page is opened

### + State management ###
The state data is represented by String value and includes:
  * items' data inpt by the user
  * items' results
  * items' stats (mistakes & checks count, time)
  * current page (the page that were visited while the state was obtained)

When the state is loaded (`player.setStateString(state)` function, player is updated using all these data, including move to the current page.

It is recommended that previously saved state is loaded in _onAssessmentLoaded_ event. In order to override the default behaviour of moving to the current page, navigation function should be called in the _onAssessmentLoaded_ event.

The state may be updated in any moment of the qtiplayer lifecycle.

## HTML/javascript usage example: ##
```
<script type="text/javascript" language="javascript" src="scorm/APIWrapper.js"></script>
<script language="javascript">
  function qpOnAppLoaded(){
    // Load assessment
    var player = qpCreatePlayer('player');
    player.setFlowOptions({showToC: true, showSummary: true, itemsDisplayMode: "ONE", showCheck: true});
    player.load('content/test.xml');

    // implement this functions in order to catch 
    // session events
    
    player.onAssessmentLoaded = function(){
      // called after the contents are loaded, before view is displayed
      // load state
      player.setStateString(state);
      // go to selected page (overrides current page stored in state var)
      player.navigateGotoItem(3);
      // or
      // player.navigateGotoTOC();
      // or
      // player.navigateGotoSummary();
    } 

    player.onAssessmentSessionBegin = function(){
      // called after the Asssessment Session is begun
      alert("Assessment Session begin");
    }

    //
    // note the change onAssessmentFinished -> onAssessmentSessionFinished
    //
    player.onAssessmentSessionFinished = function(){
      // called after the Assessment Session is finished
      alert("Assessment Session finished");
    }

    player.onTestPageSwitching = function(){
      // called before the page is switched
      // called only for pages of type "TEST" (it means that if user is exiting TOC or SUMMARY page, the function won't trigger)
      alert("Page is to be switched");
    }

    player.onTestPageSwitched = function(){
      // called after the page has been switched
      // called only for pages of type "TEST" (it means that if user is exiting TOC or SUMMARY page, the function won't trigger)
      alert("Page has been switched");

      // if you plan to check item results and stats just after page item has been completed,
      // call the instructions here; ex.
      getItemsSessionData();
    }
  }

  function managePlayer(){

    // move to the next item
    player.navigateNextItem();

    // move to the previous item
    player.navigatePreviousItem();

    // move to the selected item (first item index is 0)
    player.navigateGotoItem(4);

    // move to the TOC
    player.navigateGotoTOC();

    // reset the answers and results for current item (and hide feedback)
    player.navigateResetItem();

    // hide answers marking and feedback, does not reset the answers
    player.navigateContinueItem();


    // get player engine mode
    var engineMode = player.getEngineMode().toString();
    if (engineMode == "RUNNING"){
      alert("Engine is running");
    }

    // finish current Item Session (and show feedback)
    player.navigateFinishItem();

    // finish the Assessment Session
    player.navigateFinishAssessment();

    // go to the Assessment Summary page
    player.navigateSummaryAssessment();


    // implement this to get or set the Assessment Session state
    // in string value
    // note: player.getState() and player.setState() are depreciated
    var state = player.getStateString();
    player.setStateString(state);


    // set the mastery score
    player.setMasteryScore(50);

  
    // get the Assessment Session Report
    var report = player.getAssessmentSessionReport();
    // use this to get Assessment Session time (in seconds)
    var time = report.getTime();
    // get the current user score
    var score = report.getScore();
    // get the maxium user score
    var max = report.getScoreMax();
    // get the index of the item being currently displayed (for the first item index returns 1)
    var currentItemIndex = report.getItemIndex();
    // get the items count in the assessment
    var itemsCount = report.getItemsCount();

    // get the lesson status 
    var lessonStatus = report.getLessonStatus();
    if (lessonStatus == "PASSED")
      alert("Lesson passed");

    // get total mistakes (value for the current page is refreshed after the page is switched)
    var totalMistakes = report.getTotalMistakes()
    // get total checks (value for the current page is refreshed after the page is switched)
    var totalChecks = report.getTotalChecks()

  }

function getItemsSessionData(){

    var report = player.getAssessmentSessionReport();

    // get session data for the selected item
    // values for the current page are refreshed after the page is switched

    for (var itemIndex = 0 ; itemIndex < report.getItemsCount() ; itemIndex ++){
        var itemTime = report.getItemTime(itemIndex);
        var itemScore = report.getItemScore(itemIndex);
        var itemScoreMax = report.getItemScoreMax(itemIndex);
        var itemMistakes = report.getItemMistakes(itemIndex);
        var itemChecks = report.getItemChecks(itemIndex);
    }
}

</script>
```
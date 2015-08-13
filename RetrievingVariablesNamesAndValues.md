# Introduction #

The qtiplayer JavaScript API allows to get the values of the response variables during playing the test. The values of Ressponse Variables contain the answers given by the user. Using this interface this answers may be stored or processes outside qtiplayer.


# Example #

The following example explains how to display an alert with the answers given by the user (to be precise, only the answer of variable named "RESPONSE"). The alert is displayed every time before the page is swiched (while exiting the current page). To obtain such functionality, callback function `onPageSwitching` is used.

```
      var player1;
      function qpOnAppLoaded(){
        // Load assessment
        player1 = qpCreatePlayer('player');
        player1.load('content/demo.xml');
		
	player1.onTestPageSwitching = function(){
		// get current item index (first page has index of 1)
		var s = "page: " + player1.getAssessmentSessionReport().getItemIndex(); 
		// get the array of current item response variables names
		s += "\nResponse variables: " + player1.getCurrentItemResponseVariables();
		// get value (user answer) of variable "RESPONSE"
		s += "\nRESPONSE value: " + player1.getCurrentItemResponseVariableValue("RESPONSE");
		// get cardinality of variable "RESPONSE"
		s += "\nRESPONSE cardinality: " + player1.getCurrentItemResponseVariableCardinality("RESPONSE");
		// get base type of variable "RESPONSE"
		s += "\nRESPONSE base type: " + player1.getCurrentItemResponseVariableBaseType("RESPONSE");

		alert(s); // display the values or use them in any different way
	}
		
```
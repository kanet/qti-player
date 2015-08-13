## Introduction ##
Qti player uses CSS file for styling player. To start creating you own skin check [defaultplayer.css](http://qti-player.googlecode.com/svn/trunk/qtiplayer/war/defaultplayer.css)

The following pictures should help you map CSS classes to visual elements:



## Player CSS map ##
![http://qti-player.googlecode.com/svn/trunk/qtiplayer/doc/out/player-css-map.png](http://qti-player.googlecode.com/svn/trunk/qtiplayer/doc/out/player-css-map.png)



## Modal feedback CSS map ##
![http://qti-player.googlecode.com/svn/trunk/qtiplayer/doc/out/feedback-modal-css-map.png](http://qti-player.googlecode.com/svn/trunk/qtiplayer/doc/out/feedback-modal-css-map.png)



## Results page CSS map ##
![http://qti-player.googlecode.com/svn/trunk/qtiplayer/doc/out/resultpage-css-map.png](http://qti-player.googlecode.com/svn/trunk/qtiplayer/doc/out/resultpage-css-map.png)



## Choice interaction CSS map ##
![http://qti-player.googlecode.com/svn/trunk/qtiplayer/doc/out/choice-css-map.png](http://qti-player.googlecode.com/svn/trunk/qtiplayer/doc/out/choice-css-map.png)
<a href='Hidden comment: 
When the answer is selected, the inline feeback may be shown for each answer:
* *qp-choice-feedback* - inline feedback (hidden)
* *qp-choice-feedback-correct* - inline feedback shown for a correct answer
* *qp-choice-feedback-wrong* - inline feedback shown for a wrong answer
'></a>
In work mode:
  * **qp-choice-button** - every button

In check mode button can have additional style:
  * **qp-choice-selected-correct** - correct answer
  * **qp-choice-selected-wrong** - wrong answer
  * **qp-choice-notselected-wrong** - wrong but not selected answer (correct one)
  * **qp-choice-notselected-correct** - correct but not selected answer (error)



## Order interaction CSS map ##
![http://qti-player.googlecode.com/svn/trunk/qtiplayer/doc/out/order-css-map.png](http://qti-player.googlecode.com/svn/trunk/qtiplayer/doc/out/order-css-map.png)
  * **qp-order-cover** is created to disable text selection in the lower layers - there should be a transparent gif or png for the background-image



## Match interaction CSS map ##
![http://qti-player.googlecode.com/svn/trunk/qtiplayer/doc/out/match-css-map.png](http://qti-player.googlecode.com/svn/trunk/qtiplayer/doc/out/match-css-map.png)
Remarks:
  * for **qp-match-container-layout** only width should be specified, height will be calculated on the basis of **qp-match-element-left-container** and **qp-match-element-right-container** elements heights and count
  * for **qp-match-element-left-container** and **qp-match-element-right-container** width and height are required
  * all elements' classes ending with **-cover** or **-land** should specify a transparent gif or png image as the backgound-image
  * for IE the lines drawn in the area are described by classes that end with **-vml**, for the rest of the browsers (FF, Opera, Chrome, Safari) those classes names' end with **-svg**


## Selection interaction CSS map ##
![http://qti-player.googlecode.com/svn/trunk/qtiplayer/doc/out/selection-css-map.png](http://qti-player.googlecode.com/svn/trunk/qtiplayer/doc/out/selection-css-map.png)


## Identification interaction CSS map ##
![http://qti-player.googlecode.com/svn/trunk/qtiplayer/doc/out/identification-css-map.png](http://qti-player.googlecode.com/svn/trunk/qtiplayer/doc/out/identification-css-map.png)


## Drag-drop interaction CSS map ##
![http://qti-player.googlecode.com/svn/trunk/qtiplayer/doc/out/dragdrop-css-map.png](http://qti-player.googlecode.com/svn/trunk/qtiplayer/doc/out/dragdrop-css-map.png)


## Vocabox CSS map ##
![http://qti-player.googlecode.com/svn/trunk/qtiplayer/doc/out/vocabox-css-map.png](http://qti-player.googlecode.com/svn/trunk/qtiplayer/doc/out/vocabox-css-map.png)


## Text Entry Multiple CSS map ##
![http://qti-player.googlecode.com/svn/trunk/qtiplayer/doc/out/textentrymultiple-css-map.png](http://qti-player.googlecode.com/svn/trunk/qtiplayer/doc/out/textentrymultiple-css-map.png)


### inlineChoiceInteraction ###

In standard mode (not check mode):
  * **qp-text-choice** - every button

In check mode:
  * **qp-text-choice-correct** - correct answer
  * **qp-text-choice-wrong** - wrong answer

### textEntryInteraction ###

In standard mode (not check mode):
  * **qp-text-textentry** - every button

In check mode:
  * **qp-text-textentry-correct** - correct answer
  * **qp-text-textentry-wrong** - wrong answer


## Audio Player ##
The audio player button takes the two style classes
  * **qp-audioplayer-button** - when stopped
  * **qp-audioplayer-button-playing** - when playing

## Operation messages CSS map ##
![http://qti-player.googlecode.com/svn/trunk/qtiplayer/doc/out/message-css-map.png](http://qti-player.googlecode.com/svn/trunk/qtiplayer/doc/out/message-css-map.png)
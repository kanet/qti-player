# Introduction #

This page describes the extensions to QTI specification that are implemented in qtiplayer.


# Response processing #

### math\_correct\_multiple template ###

In order to enable the placement of more than one module per item (per page), an additional **Response Processing template** has been created.

The mentioned template matches the answers' correctenes for each module in a page. It's like it executes _match\_correct_ template for each response declaration and then summarizes the results of the comparisons. The result is put into outcome variable _SCORE_. The identifiers for the response declarations are free to choose.

The name for the template is:
**_match\_correct\_multiple_**

The address for the template is:
**_http://www.ydp.eu/qti/rptemplates/match\_correct\_multiple_**

So if you plan to use it, please append in the assessment item definition the response processing template information as follows
```
<responseProcessing template="http://www.ydp.eu/qti/rptemplates/match_correct_multiple"/>
```

The template _match\_correct\_multiple_ is made a default template in case no template is specified in the assessment item definition.

The implementation example of multiple modules per page and _match\_correct\_multiple_ template is shown here: http://qti-player.googlecode.com/svn/trunk/qtiplayer/war/content/demo/inline_choice.xml


### Special Outcome Variables in math\_correct\_multiple template ###

For the template `math_correct_multiple` special macros in _response processing_ have been defined. These macros define the values that are assigned to the special _Outcome Variables_.

Currently there are 6 special _Outcome Variables_ available:
  * `identifier="SCORE"` - response processing assignes the current **total** score for whole page
  * `identifier="SCOREHISTORY"` - response processing assignes the history of **total** score; the subsequent score values are separated with semicolon ex. `0;0;1;0;2`
  * `identifier="SCORECHANGES"` - response processing assignes the changes of the **total** score; the subsequent score values are separated with semicolon ex. `0;0;1;-1;2`
  * `identifier="%MODULEIDENTIFIER%-SCORE"` - response processing assignes the current score **for the module** with `%MODULEIDENTIFIER%` identifier
  * `identifier="%MODULEIDENTIFIER%-SCOREHISTORY"` - response processing assignes the history of score **for the module** with `%MODULEIDENTIFIER%` identifier; ex. if first two answer attempts for the module were wrong, the third is correctand the fourth is wrong this _Outcome Variable_ will contain the value `0;0;1;0` etc.
  * `identifier="%MODULEIDENTIFIER%-SCOREHISTORY"` - response processing assignes the changes of the score **for the module** with `%MODULEIDENTIFIER%` identifier; ex. if first two answer attempts for the module were wrong, the third is correct and the fourth is wrong this _Outcome Variable_ will contain the value `0;0;1;-1` etc.
  * `identifier="%MODULEIDENTIFIER%-PREVIOUS` - contains the previous (not current) user answer for the module
  * `identifier="%MODULEIDENTIFIER%-LASTCHANGE` - contains the last small change of the answer for the module - the difference between the current and the previous answer. If an answer option has been added it is stored with the "`+`" prefix, if removed with "`-`" prefix. The possible values could be ex. "`+ChoiceA`" (selected choice), "`+A B`" (a pair), "`-ChoiceA`" (unselected choice), "`+ChoiceA;-ChoiceB`" (for single choice - selected `A` instead of `B`). **Requires the declaration of `-PREVIOUS` variable**.

Special _Outcome Variables_ are always defined for an existing module (for an existing _Response Identifier_). If these special _Outcome Variables_ are not defined, their values will not be calculated during _response processing_.

The example of the special _Outcome Variables_ implementation is placed below:

```
<assessmentItem>
  <responseDeclaration identifier="RESPONSE" ... />
  <outcomeDeclaration identifier="SCORE" ... />
  <outcomeDeclaration identifier="SCOREHISTORY" ... />
  <outcomeDeclaration identifier="SCORECHANGES" ... />
  <outcomeDeclaration identifier="RESPONSE-SCORE" ... />
  <outcomeDeclaration identifier="RESPONSE-SCOREHISTORY" ... />
  <outcomeDeclaration identifier="RESPONSE-SCORECHANGES" ... />
  <outcomeDeclaration identifier="RESPONSE-PREVIOUS" ... />
  <outcomeDeclaration identifier="RESPONSE-LASTCHANGE" ... />
  <itemBody>
    ...
    <choiceInteraction responseIdentifier="RESPONSE" ... >
    </choiceInteraction>
    ...
  </itemBody>
  <responseProcessing template="http://www.ydp.eu/qti/rptemplates/match_correct_multiple"/>
</assessmentItem>
```

For the real-life example see this documents:

http://qti-player.googlecode.com/svn/trunk/qtiplayer/war/content/test/choice_multiple.xml

http://qti-player.googlecode.com/svn/trunk/qtiplayer/war/content/demo/inline_choice.xml
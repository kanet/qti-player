# Introduction #

In this document the implementation of inline, modal and assessment feedbacks is explained.



# Feedback processing #

## Feedback implementation ##

Modal feedback is defined by including the declaration of `< modalFeedback />` node in Assessment Item document. The inline feedback is defined by `< feedbackInline />` node. As follows in the example:
```
<assessmentItem>
  <responseDeclaration identifier="RESPONSE"... />
  <outcomeDeclaration identifier="IDENTIFIER" ... />
  <itemBody>
    ...
    <choiceInteraction responseIdentifier="RESPONSE" ... >
      <feedbackInline outcomeIdentifier="IDENTIFIER" identifier="VALUE" senderIdentifier="^RESPONSE$"showHide="show" align="TOP_CENTER" mark="CORRECT" fadeEffect="500">Inline feedback.</feedbackInline>
    </choiceInteraction>
    ...
  </itemBody>
  <responseProcessing ... />
  <modalFeedback outcomeIdentifier="IDENTIFIER" identifier="VALUE" senderIdentifier="^RESPONSE$" sound="beep.mp3" showHide="show" >Feedback.</modalFeedback>
</assessmentItem>
```
Here are the attrributes explained:
  * **outcomeIdentifier** - the **identifier** of the variable that will be user in the processing; could be identifier of Response Variable or Outcome Variable
  * **identifier** - the value to which the current value of the variable will be compared;
  * **senderIdentifier** - the identifier of the module that triggered the Feedback Processing
  * **sound** - url to the audio file that will be played upon the feedback activation (only for `modalFeedback`)
  * **showHide** - (show/hide) - if the result of the processing is positive the feedback will only be shown (or played) id the `showHide` value is `show`. If the result is negative, feedback will not be shown unless the `showHide` value is `hide`

### Feedback processing ###

The variable given by the identifier defined by `outcomeIdentifier` attribute is under consideration. For this variable a comparison of its value  and the value defined by `identifier` attribute is performed. The value in `identifier` attribute could be of three types:
  1. **numerical value** - the value of the given variable and the value defined in `identifier` attribute are compared on the mathematical basis. Three comapators are available for the definition
    * == - equals
    * >= - greater or equals
    * <= - less or equals
The appliation of these comparators is shown in the example below:
```
<modalFeedback outcomeIdentifier="SCORE" identifier=">=1" showHide="show"/>
<modalFeedback outcomeIdentifier="SCORE" identifier="<=5" showHide="show"/>
<modalFeedback outcomeIdentifier="SCORE" identifier="==0" showHide="show"/>
```
If `showHide` set to `show` the feedback will be shown when the condition of the compatrison is met.
> 2. **regular expression** - the feedback processing checks whether the value of the variable under consideration matches the given regular expression
```
<modalFeedback outcomeIdentifier="RESPONSE" identifier=".*A1.*" showHide="show">A1 is selected</modalFeedback>
<modalFeedback outcomeIdentifier="RESPONSE" identifier="(H)|(He)" showHide="show">H or He is selected</modalFeedback>
<modalFeedback outcomeIdentifier="RESPONSE" identifier=".*" showHide="show">Whatever.</modalFeedback>
```
> 3. **standard expression** - when neither comparator expression nor regular expression is defined, the processing will check whether the answers separated by semicolon (`;`) are reflecting the current value of the variable
```
<modalFeedback outcomeIdentifier="RESPONSE" identifier="A1" showHide="show">A1 is selected</modalFeedback>
<modalFeedback outcomeIdentifier="RESPONSE" identifier="A1;A2" showHide="show">A1 and A2 are selected</modalFeedback>
<modalFeedback outcomeIdentifier="RESPONSE" identifier="A1;A2;A3" showHide="show">A1, A2 and A3 are selected</modalFeedback>
```

In every example case it could be either `< modalFeedback />` or `< feedbackInline />`.

Please note that `identifier="(H;He)|(He;H)"` has the identical translation as `identifier="H;He"`. The first one is processed as **regular expression**, the latter as **standard expression**.

When the `identifier` attribute value is compared to the variable value it is crucial to know how these values are stored. For the purpose of the comparison process the values are stored as string. When the value consists of multiple subvalues, they are separated with semicolon. Here are some examples:
  * cadrinality:simple - `A1`
  * cadrinality:multiple & ordered - `A1;A2;A3`
  * baseType:pair & directedPair - `A1 A2;A3 A4;A5 A6`


For more examples on how to implement feedbacks see:

http://qti-player.googlecode.com/svn/trunk/qtiplayer/war/content/test/choice_multiple.xml

http://qti-player.googlecode.com/svn/trunk/qtiplayer/war/content/demo/order.xml

http://qti-player.googlecode.com/svn/trunk/qtiplayer/war/content/demo/match.xml

http://qti-player.googlecode.com/svn/trunk/qtiplayer/war/content/demo/text_entry.xml

http://qti-player.googlecode.com/svn/trunk/qtiplayer/war/content/demo/inline_choice.xml

http://qti-player.googlecode.com/svn/trunk/qtiplayer/war/content/demo/selection.xml

http://qti-player.googlecode.com/svn/trunk/qtiplayer/war/content/demo/identification.xml

### Sound in feedback - 'sound' attribute ###

In order to achieve sound playback as an feedback action, the attribute `sound` of the `modalFeedback` node must be defined. It could be done as follows:
```
<modalFeedback outcomeIdentifier="IDENTIFIER" identifier="value" showHide="show" sound="click.mp3"/>
```
the HTML content could also be included:
```
<modalFeedback outcomeIdentifier="IDENTIFIER" identifier="value" showHide="show" sound="http://rlab.pl/resources/click.mp3">Playing sound!</modalFeedback>
```
Currently the supported audio file type is **mp3**. The value of the `sound` attribute points to the file placed relatively to the location of Assessment Item document (so in the first example _click.mp3_ is placed in the same directory together with exercise.xml). If the path starts from `http` the relative path is not combined, and the location is treated as absolute (second example).

For inline feedback sound attribute can also be defined.


### Feedback sender - 'senderIdentifier' attribute ###

Showing of each feedback could be also described by the identifier of a sender. The sender is the module that triggered the response processing and the change in score. If the sender-based behaviour is desired the _senderIdentifier_ attribute of _modalFeedback_ needs to be filled. This attribute is filled with **regular expression** that is matched to the value of _responseIdentifier_ of each module.

Values of _indetifier_ and _senderIdentifier_ attributes are checked in the conjunction. If both match - the feedback is shown (provided that `showHide="show"`).

Usage as below:

```
  <itemBody>
    ...
    <choiceInteraction responseIdentifier="RESPONSE" ... >
    </choiceInteraction>
    ...
  </itemBody>
<modalFeedback outcomeIdentifier="IDENTIFIER" identifier="value" showHide="show" senderIdentifier="^RESPONSE$"/>
```

Example:
http://qti-player.googlecode.com/svn/trunk/qtiplayer/war/content/demo/inline_choice.xml


### Inline feedback alignment - 'align' attribute ###

The inline feedback cloud can be positioned around the mounting element in a few different ways. This is applied using `aling` attribute of `<inlineFeedback>` node. The default value for this attribute is `TOP_LEFT`. The example implementation of the inline feeback position looks like this.

```
<feedbackInline outcomeIdentifier="IDENTIFIER" identifier="VALUE" senderIdentifier="^RESPONSE$"showHide="show" align="TOP_CENTER">Inline feedback.</feedbackInline>
<feedbackInline outcomeIdentifier="IDENTIFIER" identifier="VALUE" senderIdentifier="^RESPONSE$"showHide="show" align="BOTTOM_RIGHT">Inline feedback.</feedbackInline>
```

The picture below explains the details of the feedback positioning.

![http://qti-player.googlecode.com/svn/trunk/qtiplayer/doc/out/inline-feedback-align.png](http://qti-player.googlecode.com/svn/trunk/qtiplayer/doc/out/inline-feedback-align.png)

For more examples on implementing inline feedback alignment see

http://qti-player.googlecode.com/svn/trunk/qtiplayer/war/content/demo/inline_choice.xml


### Feedback for correct/wrong - styling ###

The feature of setting different CSS style classes for feedback that with the corresponding answer evaluable as correct or wrong has been implemented. This behaviour can be achieved through manipulating the value of the `mark` atrtibute of the `<inlineFeedback>` node. The example syntax using `mark` attribute is shown below:

```
<feedbackInline outcomeIdentifier="IDENTIFIER" identifier="VALUE" senderIdentifier="^RESPONSE$"showHide="show" align="TOP_CENTER" mark="CORRECT">Inline feedback.</feedbackInline>
```


According to the value of the mentioned attribute the outer `<div>` containing the inline feedback will be styled with different classes. The table below explains this feature:

| `mark` attribute | style class | comment |
|:-----------------|:------------|:--------|
| mark="NONE"      | class="qp-feedback-inline" | -       |
| mark="CORRECT"   | class="qp-feedback-inline-correct" | -       |
| mark="WRONG"     | class="qp-feedback-inline-wrong" | -       |
| mark="AUTO"      | class="qp-feedback-inline-(correct/wrong)" | Qti-player will try to automatically determine the evaluation basing on the correct answer value of the considered Response Variable |
| not present      | class="qp-feedback-inline" | treated as mark="NONE" |


### Inline feedback fade-in and fade-out effects ###


To enable the fade-in and fade-out effect please add the `fadeEffect` attribute to the `<feedbackInline>` node. The value of the attribute represents the amount of time in ms (miliseconds) that will be spent on showing or hidin the feedback cloud. The typical implementation of the node would look as follows:

```
<feedbackInline outcomeIdentifier="IDENTIFIER" identifier="VALUE" showHide="show" fadeEffect="500">Inline feedback.</feedbackInline>
```

If the specified value is `fadeEffect="0"` or there is no `fadeEffect` attribute the fade effects will be disabled.

## Assessment feedback ##

Feedback could be displayed on the results summary page that is shown after the Assessment Session is finished. Assessment feedback is a HTML content that is displayed when the condition of the percentage score result is met. The `<assessmentFeedback>` tag could contain one or more `<assessmentFeedbackCase>` tags. Every `<assessmentFeedbackCase>` tag defines a feedback contents thwt will be shown when the condition defined is met.

**The conditions are checked from first to last and the feedback for only the first matching feedback case will be shown**. Analyzing the upcoming example, if the percentage score for the test is **65%**:
  * the condition for **75%** is not met,
  * the condition for **50%** is met, so the text "Feedback for 50%" will be displayed
  * the rest of the conditions will not be checked.

The example below presents how to define the cases for Assessment Feedback:

```
<assessmentTest ...>
  <testPart>
    <assessmentSection ...>
      <assessmentItemRef .../>
      <assessmentItemRef .../>

      <assessmentFeedback>
        <assessmentFeedbackCase percentageScore="75">Feedback for 75%</assessmentFeedbackCase>
        <assessmentFeedbackCase percentageScore="50">Feedback for 50%</assessmentFeedbackCase>
        <assessmentFeedbackCase percentageScore="25">Feedback for 25%</assessmentFeedbackCase>
      </assessmentFeedback>
    </assessmentSection>
  </testPart>
</assessmentTest>
```

The example implementation:

http://qti-player.googlecode.com/svn/trunk/qtiplayer/war/content/demo.xml
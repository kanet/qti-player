# Introduction #

The Assessment declaration or Assessment Item declaration document could contain the reference to the external CSS files.

The desired links should be placed in the nodes structure, as follows:
```
<styleDeclaration>
  <link href="firefox.css" userAgent=".*Mozilla.*"/>
  <link href="ie.css" userAgent=".*Opera.*"/>
</styleDeclaration>
```

Summary for _< link />_ attributes:
> - _href_ - path to the CSS file, relatively to the location of xml declaration file (or abosulte path that starts from _http://..._)

> - _userAgent_ - regular expression that would be matched to the user-agent data obtained from the browser

For a more detailed (and working) code example, look up here:

http://qti-player.googlecode.com/svn/trunk/qtiplayer/war/content/


# Style reference in the Assessment declaration #
This is how to include the link to the css file in the Assessment declaration:
```
<assessmentTest>

  <styleDeclaration>
    <link href="assessment_style.css" userAgent=".*"/>
  </styleDeclaration>

  <testPart>
    <assessmentSection identifier="sectionA" title="Section A" visible="true">
      <assessmentItemRef identifier="order" href="demo/order.xml"/>
    </assessmentSection>
  </testPart>
</assessmentTest>
```



# Style reference in the Assessment Item declaration #
This is how to include the link to the css file in the Assessment Item declaration:
```
<assessmentItem>

  <styleDeclaration>
    <link href="item_style.css" userAgent=".*"/>
  </styleDeclaration>

  <itemBody>
    <p>Some contents, blah blah blah</p>
  </itemBody>
</assessmentItem>
```
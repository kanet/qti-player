# Introduction #

The system of content tagging has been introduced in order to decide which fragments of itemBody contents should be displayed. The application user could decide to ex. display only pictures, modules or text nodes.

# Implementation #

The implementation of the tags in the content body could be done through `<qy:tag name="xxx">` nodes. The `name` attribute is crucial as it contains the name of the tag.

Visit the link presented below to acquire more examples of tags implementation:

http://qti-player.googlecode.com/svn/trunk/qtiplayer/war/content/demo/identification.xml

# Usage #

The information which tags should be ignored is passed to the player through Display Options. This data is stored in a JavaScript array containing the list of nodes' names. The mentioned array is passed to the player object through `setDisplayOptions` method. The live code is shown below.

```
function qpOnAppLoaded(){
	// Load assessment
	player1 = qpCreatePlayer('player');
	// set Display Options - ignore all "media" tags & all untagged fragments
	player1.setDisplayOptions(["media", "untagged"]);
	// load contents
        player1.load('content/demo.xml');	
}
```

For the real example see the link below:

http://qti-player.googlecode.com/svn/trunk/qtiplayer/war/demo.html

# More information on tags #

  * The `untagged` keyword covers all the fragments of the contents that are not marked with any other tag.

  * Tags cannot be nested - if a tag would be inserted as a direct or indirect child of a tag node it would not function
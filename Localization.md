# Introduction #

The player localization could be adapted by creating the locale definition. This is defined in the form of javascript (_.js_) file. The structure of the file is shown below:

```
if (window.QtiPlayer == null)
	window.QtiPlayer = {};

window.QtiPlayer.locale = {
	SUMMARY_PAGE: "Page", 
};
```

The object `window.QtiPlayer.locale` should contain all the fields that can be defined for the player. This file ought to be included into the html file that owns the player.

The example can be found here:

http://qti-player.googlecode.com/svn/trunk/qtiplayer/war/locale_en.js

# Locale definition variables #

The list of the variables that should be defined:
  * `SUMMARY_PAGE` - "Page" text on the summary page
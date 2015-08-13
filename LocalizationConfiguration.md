# Localization #

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


# Configuration #

Qti-player is configured through so called _flow options_. These options can be read from .js configuration file or set in JavaScript code while starting the application. The .js configuration file should be included into HTML document.

Firstly options stored in the .js configuration file (if defined) are read. If both config file and JavaScript API command setting _flow options_ are set, **JavaScript code command overrides those stored in config file**.

The example of config.js file:

```
if (window.QtiPlayer == null)
	window.QtiPlayer = {};
	
window.QtiPlayer.flowoptions = {
	SHOW_TOC: true,
	SHOW_SUMMARY: true,
	ITEMS_DISPLAY_MODE: "ONE",
	SHOW_CHECK: false
}
```

The example can be found here:

http://qti-player.googlecode.com/svn/trunk/qtiplayer/war/config.js
# Introduction #

This page explains how to use _qtiplayer_ in the GWT enviroment.

# Details #

The assessment content could be loaded by using `load()` method. The method is executed with two parameters:
  * `XMLData assessmentData` - `XMLData` object representing the assessment data source
  * `XMLData[] itemDatas` - the array of `XMLData` objects representing the data sources of the items

Sample code:

```
public void createPlayer(){
    XMLData assessmentData;
    XMLData[] itemDatas;

    // loading xml documents into above variables

    // mount player in html element of id "root"
    Player p = new Player("root");
    // optionally - flow options could be set
    p.setFlowOptions(new FlowOptions(false, false, PageItemsDisplayMode.ONE, true));
    // load data source xml documents
    p.load(assessmentData, itemDatas);
    // after performing all load routines, player is working :)
}
```

# Example #

The live example of implementing the Player object is presented here:

http://qti-player.googlecode.com/svn/trunk/qtiplayer/src/com/qtitools/player/client/DirectLoadInterfaceTest.java
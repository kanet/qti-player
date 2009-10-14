package com.klangner.qtiplayer.client;

import java.util.Vector;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.RootPanel;
import com.klangner.qtiplayer.client.model.Assessment;
import com.klangner.qtiplayer.client.model.AssessmentItem;
import com.klangner.qtiplayer.client.model.IDocumentLoaded;
import com.klangner.qtiplayer.client.model.Result;

/**
 * Main class with player API
 * @author Krzysztof Langner
 */
public class Player {

  /** player node id */
  private String              id;
  /** Javascript object representing this java object */
  private JavaScriptObject    jsObject;
  /** Assessment played by this player*/
  private Assessment          assessment;
  /** Player view */
  private PlayerView          playerView;
  /** Current item index. 0 based */
  private int                 currentItemIndex;
  /** current item object */
  private AssessmentItem      currentItem;
  /** Item results */
  private Vector<Result>      results = new Vector<Result>();
  
  
  /**
   * Event send when assessment is done
   * @param msg
   */
  private static native void onAssessmentFinished(
      JavaScriptObject player, int score, int max) /*-{
    
    if(typeof player.onAssessmentFinished == 'function') {
      var result = new Array();
      result.score = score;
      result.max = max;
      player.onAssessmentFinished(result);
    }
  }-*/;
  
    
  
  /**
   * constructor
   * @param id
   */
  public Player(String id){
  
    this.id = id;
    this.jsObject = JavaScriptObject.createFunction();
  }
  
  public JavaScriptObject getJavaScriptObject(){
    return jsObject;
  }
  
  /**
   * Load assessment file from server
   */
  public void loadAssessment(String url){
    
    assessment = new Assessment();
    assessment.load(GWT.getHostPageBaseURL() + url, new IDocumentLoaded(){

      public void finishedLoading() {
        initialize();
      }
    });
  }
  
  /**
   * Check score
   * @param index
   */
  private void checkItemScore(){

    playerView.getCheckButton().setVisible(false);
    
    if(currentItemIndex+1 < assessment.getItemCount()){
      playerView.getNextButton().setVisible(true);
    }
    else{
      playerView.getFinishButton().setVisible(true);
    }

    playerView.markErrors();
    results.add(currentItem.getResponseProcesing().getResult());
    playerView.showFeedback(currentItem.getResponseProcesing().getFeedback());
  }

  
  /**
   * Create user interface
   */
  private void initialize() {
    
    RootPanel rootPanel = RootPanel.get(id); 
    Element element = rootPanel.getElement();
    Node node = element.getFirstChild();
    if(node != null)
      element.removeChild(node);
    
    playerView = new PlayerView(assessment);
    rootPanel.add(playerView.getView());
    
    playerView.getCheckButton().addClickHandler(new ClickHandler(){
      public void onClick(ClickEvent event) {
        checkItemScore();
      }
    });

    playerView.getNextButton().addClickHandler(new ClickHandler(){
      public void onClick(ClickEvent event) {
        loadAssessmentItem(currentItemIndex+1);
      }
    });
    
    playerView.getFinishButton().addClickHandler(new ClickHandler(){
      public void onClick(ClickEvent event) {
        showAssessmentResult();
      }
    });


    // Switch to first item
    loadAssessmentItem(0);
  }
  
  
  /**
   * Show assessment item in body part of player
   * @param index
   */
  private void loadAssessmentItem(int index){
    String  url = assessment.getItemRef(index);

    currentItem = new AssessmentItem();
    currentItemIndex = index;

    currentItem.load(url, new IDocumentLoaded(){

      public void finishedLoading() {
        showCurrentItem();
      }
    });

  }
  
  /**
   * Show assessment item in body part of player
   * @param index
   */
  private void showAssessmentResult(){
    int score = 0;
    int max = 0;
    
    for(int i = 0; i < results.size(); i++){
      Result result = results.elementAt(i);
      score += result.getScore();
      max += result.getMaxPoints();
    }
    
    playerView.getCheckButton().setVisible(false);
    playerView.getNextButton().setVisible(false);
    playerView.getFinishButton().setVisible(false);
    playerView.showResultPage("Your score is: " + (int)((score * 100)/max) + "%");
    onAssessmentFinished(jsObject, score, max);
  }
   
  /**
   * create view for assessment item
   */
  private void showCurrentItem(){
    
    playerView.showAssessmentItem(currentItem, currentItemIndex+1);
    
    playerView.getCheckButton().setVisible(true);
    playerView.getNextButton().setVisible(false);
    playerView.getFinishButton().setVisible(false);
    
  }
  

}

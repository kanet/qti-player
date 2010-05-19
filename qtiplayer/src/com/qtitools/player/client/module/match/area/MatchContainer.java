package com.qtitools.player.client.module.match.area;

import java.util.Vector;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;
import com.qtitools.player.client.model.variables.response.Response;
import com.qtitools.player.client.module.IInteractionModule;
import com.qtitools.player.client.module.IModuleEventsListener;
import com.qtitools.player.client.module.IStateChangedListener;
import com.qtitools.player.client.module.ITouchEventsListener;
import com.qtitools.player.client.util.RandomizedSet;

public class MatchContainer extends FlowPanel{

	/** module state changed listener */
	private IStateChangedListener stateListener;
	/** module state changed listener */
	private ITouchEventsListener touchEventsListener;
	/** response processing interface */
	private Response response;
	private IInteractionModule moduleReference;
	
	public MatchArea area;
	public Vector<MatchConnection> connections;
	public Vector<MatchElement> elements;
	public Vector<String> linesIds;
	public Vector<Boolean> linesIdsUsed;
	
	private AbsolutePanel layoutPanel;
	private VerticalPanel leftPanel;
	private VerticalPanel rightPanel;
	public AbsolutePanel areaPanel;
	
	private MatchDragManager dragManager;
	private int lastDragX;
	private int lastDragY;

	/** Shuffle? */
	private boolean shuffle = false;
	public int maxAssociations = 4;
	
	public MatchContainer(NodeList nodes, boolean _shuffle, int _maxAssociations, Response _response, 
			IModuleEventsListener moduleEventsListener, IInteractionModule _moduleReference){

		stateListener = (IStateChangedListener)moduleEventsListener;
		touchEventsListener = (ITouchEventsListener)moduleEventsListener;
		response = _response;
		moduleReference = _moduleReference;
		
		shuffle = _shuffle;
		maxAssociations = _maxAssociations;
		
		dragManager = new MatchDragManager();
		
		connections = new Vector<MatchConnection>();
		elements = new Vector<MatchElement>();
		
		for (int n = 0 ; n < nodes.getLength()  &&  n < 2 ; n ++){
			NodeList choiceNodes = ((Element)(nodes.item(n))).getElementsByTagName("simpleAssociableChoice");
			for (int nn = 0 ; nn < choiceNodes.getLength() ; nn++){
				elements.add(new MatchElement((Element)choiceNodes.item(nn), (n == 0)? MatchSide.LEFT : MatchSide.RIGHT));
			}
		}
		
		// ids
		linesIds = new Vector<String>();
		linesIdsUsed = new Vector<Boolean>();
		int linesCount = (maxAssociations > 0) ? maxAssociations : getElementsCount(MatchSide.LEFT)*getElementsCount(MatchSide.RIGHT);
		for (int i = 0 ; i < linesCount ; i ++ ){
			linesIds.add(Document.get().createUniqueId());
			linesIdsUsed.add(false);
		}
		
		leftPanel = new VerticalPanel();
		leftPanel.setStylePrimaryName("qp-match-left-container");
		rightPanel = new VerticalPanel();
		rightPanel.setStylePrimaryName("qp-match-right-container");
		areaPanel = new AbsolutePanel();
		areaPanel.setStylePrimaryName("qp-match-area-container");
		
		layoutPanel = new AbsolutePanel();
		layoutPanel.setStylePrimaryName("qp-match-container-layout");
		layoutPanel.add(leftPanel, 0, 0);
		layoutPanel.add(rightPanel, 0, 0);
		layoutPanel.add(areaPanel, 0, 0);
		
		
		area = new MatchArea(50, 50, touchEventsListener);
		
		insertElements();
		
		add(layoutPanel);
		
	}
	
	public void init(){
		int leftPanelMargin = 0;
		int rightPanelMargin = 0;
		int hL = 0;
		int hR = 0;

		for (int e = 0 ; e < elements.size() ; e ++ ){
			if (elements.get(e).side == MatchSide.LEFT){
				hL += elements.get(e).getView().getOffsetHeight();
				if (leftPanelMargin == 0)
					leftPanelMargin = elements.get(e).getView().getOffsetWidth();
			}else if (elements.get(e).side == MatchSide.RIGHT){
				hR += elements.get(e).getView().getOffsetHeight();
				if (rightPanelMargin == 0)
					rightPanelMargin = elements.get(e).getView().getOffsetWidth();
			}
		}

		int areaWidth = layoutPanel.getOffsetWidth() - leftPanelMargin - rightPanelMargin;
		int rightPanelLeft = areaWidth + leftPanelMargin;
		int areaHeight = (hR > hL) ? hR : hL;
		int heightPerElement = (hL+hR)/elements.size();
		
		// slot size
		
		layoutPanel.setWidgetPosition(rightPanel, rightPanelLeft, 0);

		area.setCanvasSize(layoutPanel.getOffsetWidth(), areaHeight);
		areaPanel.add(area.getView(), 0, 0);
		areaPanel.setHeight(String.valueOf(areaHeight));
		layoutPanel.setHeight(String.valueOf(areaHeight));
		
		updateSlotsAnchors(areaWidth, heightPerElement, leftPanelMargin, rightPanelMargin);
		
	}
	
	private void insertElements(){
		leftPanel.clear();
		rightPanel.clear();
		Vector<Integer> elementsOrder = new Vector<Integer>();
		
		if (shuffle){
			RandomizedSet<Integer> randomizedIndexes = new RandomizedSet<Integer>();
			for (int e = 0 ; e < elements.size() ; e ++ ){
				randomizedIndexes.push(e);
			}
			while (randomizedIndexes.hasMore()){
				elementsOrder.add(randomizedIndexes.pull());
			}
		} else {
			for (int e = 0 ; e < elements.size() ; e ++ ){
				elementsOrder.add(e);
			}
		}

		for (Integer e:elementsOrder){
			if (elements.get(e).side == MatchSide.LEFT)
				leftPanel.add(elements.get(e).getView());
			else if (elements.get(e).side == MatchSide.RIGHT)
				rightPanel.add(elements.get(e).getView());
		}
	}

	
	private void updateSlotsAnchors(int areaWidth, int heightPerElement, int leftMargin, int rightMargin){
		
		int sX, sY;
		int slotWidth;
		
		for (int e = 0 ; e < elements.size() ; e ++ ){
			slotWidth = elements.get(e).slot.getOffsetWidth();
			if (elements.get(e).side == MatchSide.LEFT){
				sX = leftMargin-slotWidth/2;
				sY = heightPerElement * leftPanel.getWidgetIndex(elements.get(e).getView()) + heightPerElement/2;
			} else {
				sX = leftMargin + areaWidth + slotWidth/2;
				sY = heightPerElement * rightPanel.getWidgetIndex(elements.get(e).getView()) + heightPerElement/2;
			}
			elements.get(e).setSlotAnchor(sX, sY);
		}
	}

	private int getElementsCount(MatchSide s){
		int count = 0;
		for (int e = 0 ; e < elements.size() ; e ++ ){
			if (elements.get(e).side == s)
				count++;
		}
		return count;
	}

	private String useLineId(){
		for (int u = 0 ; u < linesIdsUsed.size() ; u ++){
			if (linesIdsUsed.get(u) == false){
				linesIdsUsed.set(u, true);
				return linesIds.get(u);
			}
		}
		return "";
	}
	private void freeLineId(String ofId){
		for (int i = 0 ; i < linesIds.size() ; i ++ ){
			if (linesIds.get(i).compareTo(ofId) == 0){
				linesIdsUsed.set(i, false);
				return;
			}
		}
		
	}
	
	public void startDrag(String tagId, int x, int y){
		x = x - areaPanel.getAbsoluteLeft();
		y = y - areaPanel.getAbsoluteTop();

		lastDragX = x;
		lastDragY = y;
		
		if (dragManager.isDragging()){
			//endDrag(tagId,x,y);
			return;
		}
		
		if (connections.size() >= maxAssociations  &&  maxAssociations != 0){
			return;
		}
		
		int fromIndex = -1;
		
		for (int e = 0 ; e < elements.size() ; e ++ ){
			if (elements.get(e).isBelongingLocation(x, y, areaPanel.getAbsoluteLeft(), areaPanel.getAbsoluteTop())){
				fromIndex = e;
				break;
			}
		}
		if (fromIndex != -1){
			if (getMachCountForElement(fromIndex) < elements.get(fromIndex).matchMax  ||  elements.get(fromIndex).matchMax == 0)
				dragManager.startDrag(fromIndex);
		}
		if (dragManager.isDragging()){
			drawDragLine(x, y);
			area.showDragLine();
		}
	}
	
	public void endDrag(String tagId, int x, int y){
		if (x == 0  &&  y == 0){
			x = lastDragX;
			y = lastDragY; 
		} else {
			x = x - areaPanel.getAbsoluteLeft();
			y = y - areaPanel.getAbsoluteTop();
		}

		area.resetLand();
		
		if (!dragManager.isDragging()){
			removeLineAt(x, y);
			return;
		}
		
		int endElementIndex = -1;
		for (int e = 0 ; e < elements.size() ; e ++ ){
			if (elements.get(e).isBelongingLocation(x, y, areaPanel.getAbsoluteLeft(), areaPanel.getAbsoluteTop())){
				endElementIndex = e;
				break;
			}
		}
		
		if (endElementIndex != -1){
			
			boolean validated = true;
			if ((elements.get(dragManager.getSourceIndex()).side == elements.get(endElementIndex).side)){
				validated = false;
			} else if (getMachCountForElement(endElementIndex) >= elements.get(endElementIndex).matchMax  &&  elements.get(endElementIndex).matchMax != 0){
				validated = false;
			}else {
				for (MatchConnection mc:connections){
					if (mc.compare(elements.get(dragManager.getSourceIndex()), elements.get(endElementIndex))){
						validated = false;
						break;
					}
				}
			}
			if (validated){
				addLine(endElementIndex);
				updateResponse();
			}
		}

		area.hideDragLine();
		dragManager.endDrag();
	}
	
	
	public void processDrag(int x, int y){
		
		x = x - areaPanel.getAbsoluteLeft();
		y = y - areaPanel.getAbsoluteTop();

		if (!dragManager.isDragging()){
			return;
		}
		drawDragLine(x, y);
		
		lastDragX = x;
		lastDragY = y;
	}
	
	public void drawDragLine(int x , int y){

		area.processDragLine(x+2, y+2, 
			elements.get(dragManager.getSourceIndex()).getSlotAnchorX(), 
			elements.get(dragManager.getSourceIndex()).getSlotAnchorY());
			
		area.moveLand(x, y);
	}
	
	public void removeLineAt(int x, int y){
		int MAX_DIST = 6;
		double a, b, d;

		for (int c = 0 ; c < connections.size() ; c ++ ){
			if ((connections.get(c).line.getX1() < connections.get(c).line.getX2()  &&
					(x < connections.get(c).line.getX1()-MAX_DIST  || x > connections.get(c).line.getX2()+MAX_DIST))
					||
				(connections.get(c).line.getX2() < connections.get(c).line.getX1()  &&
					(x < connections.get(c).line.getX2()-MAX_DIST  || x > connections.get(c).line.getX1()+MAX_DIST))
					||
				(connections.get(c).line.getY1() < connections.get(c).line.getY2()  &&
					(x < connections.get(c).line.getY1()-MAX_DIST  ||  y > connections.get(c).line.getY2()+MAX_DIST))
					||
				(connections.get(c).line.getY2() < connections.get(c).line.getY1()  &&
					(x < connections.get(c).line.getY2()-MAX_DIST  ||  y > connections.get(c).line.getY1()+MAX_DIST))
				){
				continue;
			}
				
			a = Double.valueOf(connections.get(c).line.getY1()-connections.get(c).line.getY2()) / Double.valueOf(connections.get(c).line.getX1()-connections.get(c).line.getX2());
			b = connections.get(c).line.getY1() - a * connections.get(c).line.getX1();
			d = Math.abs( (a * x - y + b) / Math.sqrt(a*a+1) );
			if (d <= MAX_DIST){
				removeLine(connections.get(c).lineId);
				return;
			}
				
		}
	}
	
	//public static native void alert(String s)/*-{
	//	alert(s);
	//}-*/;
	
	public boolean isLineId(String tagId){

		for (int i = 0 ; i < linesIds.size() ; i ++ ){
			if (linesIds.get(i).compareTo(tagId) == 0){
				return true;
			}
		}
		
		return false;
	}
	
	public void addLine(int endElementIndex){
		MatchConnection newMC = new MatchConnection(elements.get(dragManager.getSourceIndex()), elements.get(endElementIndex) , useLineId());
		addLine(newMC);
	}
	
	public void addLine(String from, String to){
		MatchConnection newMC = new MatchConnection(getElementByIdentifier(from), getElementByIdentifier(to), useLineId());
		addLine(newMC);
	}
	
	private void addLine(MatchConnection newMC){
		connections.add(newMC);
		area.addLine(newMC.line);
		
	}
	
	public void removeLine(String tagId){
		int lineIndex = -1;
		
		for (int i = 0 ; i < connections.size() ; i ++ ){
			if (connections.get(i).lineId.compareTo(tagId) == 0){
				lineIndex = i;
				break;
			}
		}
		if (lineIndex != -1){
			freeLineId(tagId);
			removeConnection(lineIndex);
			updateResponse();
		}
		
	}
	
	private void removeConnection(int connectionIndex){
		area.removeLine(connections.get(connectionIndex).line);
		connections.remove(connectionIndex);
		
	}
	
	public void removeAllLines(){
		for (int i = 0 ; i < linesIdsUsed.size() ; i ++ ){
			if (linesIdsUsed.get(i) == true){
				freeLineId(linesIds.get(i));
			}
		}
		while (connections.size() > 0){
			removeConnection(connections.size()-1);
		}
	}
	
	public void reset(){
		removeAllLines();
		area.clear();
		insertElements();
		init();
	}
	

	
	private MatchElement getElementByIdentifier(String identifier){
		for (int e = 0 ; e < elements.size() ; e ++ ){
			if (elements.get(e).identifier.compareTo(identifier) == 0){
				return elements.get(e);
			}
		}
		return null;
	}
	
	private int getMachCountForElement(int index){
		int count = 0;
		String elementIdentifier = elements.get(index).identifier;
		for (MatchConnection conn:connections){
			if (elementIdentifier.compareTo(conn.from) == 0)
				count++;
			else if (elementIdentifier.compareTo(conn.to) == 0)
				count++;
		}
		return count;
	}
	
	public void markAnswers(){

		Vector<String> correctAnswers = response.correctAnswers;
		
		String currAnswer;
		boolean answerFound;
		
		for (int c = 0 ; c < connections.size() ; c ++){
			currAnswer = connections.get(c).from + " " + connections.get(c).to;
			answerFound = false;
			for (String currCorrectAnswer:correctAnswers){
				if (currCorrectAnswer.compareTo(currAnswer) == 0){
					answerFound = true;
					break;
				}
			}
			if (answerFound)
				connections.get(c).markCorrect();
			else
				connections.get(c).markWrong();
			
		}
	}
	
	public void unmark(){
		for (int c = 0 ; c < connections.size() ; c ++){
			connections.get(c).unmark();
		}
	}

	
	private void updateResponse(){
		
		Vector<String> currResponseValues = new Vector<String>();
		
		for (MatchConnection mc : connections){
			currResponseValues.add(mc.getAsDirectedPair());
		}
		
		response.set(currResponseValues);
		stateListener.onStateChanged(moduleReference);
	}
}

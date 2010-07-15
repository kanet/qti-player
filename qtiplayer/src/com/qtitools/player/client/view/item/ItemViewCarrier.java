package com.qtitools.player.client.view.item;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class ItemViewCarrier {

	public ItemViewCarrier(String title, Widget _contentView, Widget _feedbackView, Widget _scoreView){
		titleView = new Label(title);
		titleView.setStyleName("qp-item-title");
		contentView = _contentView;
		feedbackView = _feedbackView;
		scoreView = _scoreView;
	}
	
	private Label titleView;
	private Widget contentView;
	private Widget feedbackView;
	private Widget scoreView;

	public Widget getTitleView(){
		return titleView;
	}
	public Widget getContentView(){
		return contentView;
	}
	public Widget getFeedbackView(){
		return feedbackView;
	}
	public Widget getScoreView(){
		return scoreView;
	}
}

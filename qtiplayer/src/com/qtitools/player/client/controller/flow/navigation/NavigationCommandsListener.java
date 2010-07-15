package com.qtitools.player.client.controller.flow.navigation;

public interface NavigationCommandsListener {

	public void nextPage();
	public void previousPage();
	public void gotoPage(int index);
	public void gotoFirstPage();
	public void gotoLastPage();
	public void gotoToc();
	public void gotoSummary();
	public void gotoTest();
	public void checkPage();
	public void continuePage();
	public void resetPage();
	public void preview();
}

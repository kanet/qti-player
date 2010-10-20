package com.qtitools.player.client;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;
import com.qtitools.player.client.controller.data.DataSourceManager;
import com.qtitools.player.client.controller.data.StyleDataSourceManager;
import com.qtitools.player.client.style.StyleSocket;
import com.qtitools.player.client.view.player.PlayerContentView;
import com.qtitools.player.client.view.player.PlayerViewSocket;

public class PlayerGinModule extends AbstractGinModule {

	@Override
	protected void configure() {
		bind(StyleSocket.class).to(StyleDataSourceManager.class).in(
				Singleton.class);
		bind(StyleDataSourceManager.class).in(Singleton.class);
		bind(PlayerViewSocket.class).to(PlayerContentView.class).in(
				Singleton.class);
		bind(PlayerContentView.class).in(Singleton.class);

		// this is unnecessary, but left for clarity - if GIN can't find a
		// binding for a class, it falls back to calling GWT.create() on that
		// class
		bind(DataSourceManager.class);
	}

}

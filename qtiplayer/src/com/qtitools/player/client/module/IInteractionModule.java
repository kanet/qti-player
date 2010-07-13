package com.qtitools.player.client.module;

public interface IInteractionModule extends IActivity, IStateful, IBrowserEventHandler, IUnattachedComponent {
	public String getIdentifier();
}

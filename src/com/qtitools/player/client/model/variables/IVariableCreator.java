package com.qtitools.player.client.model.variables;

import com.google.gwt.xml.client.Node;

public interface IVariableCreator<V> {

	public V createVariable(Node node);
}

package com.klangner.qtiplayer.client.model;

import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.klangner.qtiplayer.client.module.IModuleSocket;

public interface IModuleFactory {

  /**
   * Create module for given XML node
   * @param node - XML node
   * @param moduleSocket - Interface to module socket
   * 
   * @return new module or null if can't create module for given node
   */
  public Widget createModule(Element node, IModuleSocket moduleSocket);  
}

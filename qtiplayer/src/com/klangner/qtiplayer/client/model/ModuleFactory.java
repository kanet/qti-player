package com.klangner.qtiplayer.client.model;

import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.klangner.qtiplayer.client.module.IModuleSocket;
import com.klangner.qtiplayer.client.module.choice.ChoiceModule;
import com.klangner.qtiplayer.client.module.debug.DebugWidget;
import com.klangner.qtiplayer.client.module.text.TextModule;

public class ModuleFactory {

  /**
   * Create module for given XML node
   * @param node - XML node
   * @param moduleSocket - Interface to module socket
   * 
   * @return new module or null if can't create module for given node
   */
  public Widget createModule(Element node, IModuleSocket moduleSocket) {

    String nodeName = node.getNodeName();
    
    if(nodeName.compareTo("p") == 0 || nodeName.compareTo("blockquote") == 0)
      return new TextModule(node, moduleSocket);
    else if(nodeName.compareTo("choiceInteraction") == 0)
      return new ChoiceModule(node, moduleSocket);
    else if(node.getNodeType() == Node.ELEMENT_NODE)
      return new DebugWidget(node);
    else
      return null;
  }
  
}

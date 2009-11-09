package com.klangner.qtieditor.client.model;

import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.klangner.qtieditor.client.model.modules.ChoiceEditor;
import com.klangner.qtiplayer.client.model.IModuleFactory;
import com.klangner.qtiplayer.client.module.IModuleSocket;
import com.klangner.qtiplayer.client.module.debug.DebugModule;
import com.klangner.qtiplayer.client.module.object.ObjectModule;
import com.klangner.qtiplayer.client.module.text.TextModule;

public class EditableModuleFactory implements IModuleFactory{

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
      return new ChoiceEditor(node);
    else if(nodeName.compareTo("object") == 0)
      return new ObjectModule(node, moduleSocket);
    else if(node.getNodeType() == Node.ELEMENT_NODE)
      return new DebugModule(node);
    else
      return null;
  }
  
}

package com.klangner.qtiplayer.client.module;

import java.io.Serializable;

public interface IStateful {

  /**
   * Get state
   * @return state object
   */
  public Serializable getState();
  
  /**
   * set new state 
   * @param newState state object created with getState() function
   */
  public void setState(Serializable newState);
}

package org.jbpm.examples.eventlistener;

import java.util.ArrayList;
import java.util.List;

import org.jbpm.api.listener.EventListener;
import org.jbpm.api.listener.EventListenerExecution;


public class LogListener implements EventListener {

  private static final long serialVersionUID = 1L;
  
  // value gets injected from process definition
  String msg;
  
  public void notify(EventListenerExecution execution) {
    List<String> logs = (List<String>) execution.getVariable("logs");
    if (logs==null) {
      logs = new ArrayList<String>();
      execution.setVariable("logs", logs);
    }
    
    logs.add(msg);
    
    execution.setVariable("logs", logs);
  }
}

package org.jbpm.examples.eventlistener;

import java.util.ArrayList;
import java.util.List;

import org.jbpm.api.Execution;
import org.jbpm.api.ProcessInstance;
import org.jbpm.test.JbpmTestCase;


public class EventListenerTest extends JbpmTestCase {
  
  String deploymentId;
  
  protected void setUp() throws Exception {
    super.setUp();
    
    deploymentId = repositoryService.createDeployment()
        .addResourceFromClasspath("org/jbpm/examples/eventlistener/process.jpdl.xml")
        .deploy();
  }

  protected void tearDown() throws Exception {
    repositoryService.deleteDeploymentCascade(deploymentId);
    
    super.tearDown();
  }

  public void testEventListener() {
    ProcessInstance processInstance = executionService.startProcessInstanceByKey("EventListener");
    
    Execution execution = processInstance.findActiveExecutionIn("wait");
    executionService.signalExecutionById(execution.getId());
    
    List<String> expectedLogs = new ArrayList<String>();
    expectedLogs.add("start on process definition");
    expectedLogs.add("start on activity wait");
    expectedLogs.add("end on activity wait");
    expectedLogs.add("take transition");
    
    List<String> logs = (List<String>) executionService.getVariable(processInstance.getId(), "logs");
    
    assertEquals(expectedLogs, logs);
  }
}

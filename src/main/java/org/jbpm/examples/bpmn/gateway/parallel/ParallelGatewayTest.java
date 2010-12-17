package org.jbpm.examples.bpmn.gateway.parallel;

import java.util.List;

import org.jbpm.api.NewDeployment;
import org.jbpm.api.ProcessInstance;
import org.jbpm.api.task.Task;
import org.jbpm.test.JbpmTestCase;


public class ParallelGatewayTest extends JbpmTestCase {
  
  @Override
  protected void setUp() throws Exception {
    super.setUp();
    NewDeployment deployment = repositoryService.createDeployment();
    deployment.addResourceFromClasspath("org/jbpm/examples/bpmn/gateway/parallel/parallel_gateway.bpmn.xml");
    registerDeployment(deployment.deploy());
  }
  
  public void testParallelGateway() {
    ProcessInstance processInstance = executionService.startProcessInstanceByKey("parallelGateway");
    
    List<Task> tasks = taskService.createTaskQuery().list();
    assertEquals(2, tasks.size());
    
    // Complete first task -> 1 still open
    taskService.completeTask(tasks.get(0).getId());
    assertProcessInstanceActive(processInstance);
    Task secondTask = taskService.createTaskQuery().uniqueResult();
    assertNotNull(secondTask);
    assertTrue(secondTask.getName().equals("Prepare shipment") || secondTask.getName().equals("Bill customer"));
    
    // Complete second task -> process ended
    taskService.completeTask(secondTask.getId());
    assertProcessInstanceEnded(processInstance);
  }

}

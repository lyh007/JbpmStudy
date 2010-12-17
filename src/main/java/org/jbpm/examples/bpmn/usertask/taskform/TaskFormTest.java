package org.jbpm.examples.bpmn.usertask.taskform;

import java.util.HashMap;
import java.util.Map;

import org.jbpm.api.NewDeployment;
import org.jbpm.api.ProcessInstance;
import org.jbpm.api.TaskQuery;
import org.jbpm.api.history.HistoryActivityInstance;
import org.jbpm.api.task.Task;
import org.jbpm.test.JbpmTestCase;


public class TaskFormTest extends JbpmTestCase {
  
  protected void setUp() throws Exception {
    super.setUp();
    
    NewDeployment deployment = repositoryService.createDeployment();
    deployment.addResourceFromClasspath("org/jbpm/examples/bpmn/task/usertask/taskform/vacationrequest.bpmn.xml");
    deployment.addResourceFromClasspath("org/jbpm/examples/bpmn/task/usertask/taskform/request_vacation.ftl");
    deployment.addResourceFromClasspath("org/jbpm/examples/bpmn/task/usertask/taskform/verify_request.ftl");
    String deployId = deployment.deploy();
    registerDeployment(deployId);
    
    // test users
    identityService.createUser("peter", "Peter", "Pan");
    identityService.createGroup("user");
    identityService.createMembership("peter", "user");
    identityService.createUser("alex", "Alex", "Ander");
    identityService.createGroup("manager");
    identityService.createMembership("alex", "manager");
    
  }
  
  protected void tearDown() throws Exception {
    identityService.deleteUser("peter");
    identityService.deleteGroup("user");
    identityService.deleteUser("alex");
    identityService.deleteGroup("manager");
    super.tearDown();
  }
  
  public void testRequestAccepted() {
    
    ProcessInstance pi = executionService.startProcessInstanceByKey("vacationRequestProcess");
    
    // After process start, peter should be a candidate for the task
    Task requestTasktask = taskService.createTaskQuery().candidate("peter").uniqueResult();
    assertNotNull(requestTasktask);
    assertEquals("org/jbpm/examples/bpmn/task/usertask/taskform/request_vacation.ftl", requestTasktask.getFormResourceName());
    
    // After this task is completed, a manager should be able to see the 'verify' task
    TaskQuery verifyTaskQuery = taskService.createTaskQuery().candidate("alex");
    assertNull(verifyTaskQuery.uniqueResult());
    taskService.completeTask(requestTasktask.getId());
    
    Task verifyTask = verifyTaskQuery.uniqueResult();
    assertEquals("org/jbpm/examples/bpmn/task/usertask/taskform/verify_request.ftl", verifyTask.getFormResourceName());
    assertNotNull(verifyTask);
    
    // When completing the verification task, we also need to store the result of the decision 
    // (will be done through the taskform normally)
    Map<String, Object> vars = new HashMap<String, Object>();
    vars.put("verificationResult", "OK");
    taskService.completeTask(verifyTask.getId(), vars);
    assertProcessInstanceEnded(pi);
  }
  
  public void testRequestRefused() {
    ProcessInstance pi = executionService.startProcessInstanceByKey("vacationRequestProcess");
    
    Task requestTasktask = taskService.createTaskQuery().candidate("peter").uniqueResult();
    taskService.completeTask(requestTasktask.getId());
    
    Task verifyTask = taskService.createTaskQuery().candidate("alex").uniqueResult();
    
    // We now complete the task, but we give a negative result as process var
    Map<String, Object> vars = new HashMap<String, Object>();
    vars.put("verificationResult", "Not OK");
    taskService.completeTask(verifyTask.getId(), vars);
    
    // Process should now be ended, but the last activity should be the 'sendRejectionMessage' task
    assertProcessInstanceEnded(pi);
    HistoryActivityInstance hai = historyService.createHistoryActivityInstanceQuery()
                                         .activityName("sendRejectionMessage").uniqueResult();
     assertNotNull(hai);
  }

}

package org.jbpm.examples.services;

import junit.framework.TestCase;

import org.jbpm.api.Configuration;
import org.jbpm.api.ExecutionService;
import org.jbpm.api.HistoryService;
import org.jbpm.api.ManagementService;
import org.jbpm.api.ProcessEngine;
import org.jbpm.api.RepositoryService;
import org.jbpm.api.TaskService;

/** shows explicitely the part of the API that is  
 * provided by JbpmTestCase as a convenience.
 * 
 * @author Koen Aers, Tom Baeyens
 */
public class ServicesTest extends TestCase {

  public void testObtainServicesAndDeployProcess() {
    // create a configuration
    Configuration configuration = new Configuration();
    // build a process engine from a configuration
    ProcessEngine processEngine = configuration.buildProcessEngine();

    // Obtain the services from the process engine
    // ProcessEngine and Services are to be used as singletons.  (ie they are threadsafe)
    RepositoryService repositoryService = processEngine.getRepositoryService();
    ExecutionService executionService = processEngine.getExecutionService();
    TaskService taskService = processEngine.getTaskService();
    HistoryService historyService = processEngine.getHistoryService();
    ManagementService managementService = processEngine.getManagementService();
    
    assertNotNull(repositoryService);
    assertNotNull(executionService);
    assertNotNull(taskService);
    assertNotNull(historyService);
    assertNotNull(managementService);
    
    // Deploying a process
    String deploymentId = repositoryService.createDeployment()
        .addResourceFromClasspath("org/jbpm/examples/services/Order.jpdl.xml")
        .deploy();

    repositoryService.deleteDeployment(deploymentId);
  }
}

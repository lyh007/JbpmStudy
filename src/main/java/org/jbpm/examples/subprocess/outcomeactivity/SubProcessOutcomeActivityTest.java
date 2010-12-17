/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jbpm.examples.subprocess.outcomeactivity;

import java.util.List;

import org.jbpm.api.ProcessInstance;
import org.jbpm.api.task.Task;
import org.jbpm.test.JbpmTestCase;


/**
 * @author Tom Baeyens
 */
public class SubProcessOutcomeActivityTest extends JbpmTestCase {

  String subProcessReviewDeploymentId;
  String subProcessDocumentDeploymentId;
  
  protected void setUp() throws Exception {
    super.setUp();
    
    subProcessReviewDeploymentId = repositoryService.createDeployment()
        .addResourceFromClasspath("org/jbpm/examples/subprocess/outcomeactivity/SubProcessReview.jpdl.xml")
        .deploy();

    subProcessDocumentDeploymentId = repositoryService.createDeployment()
        .addResourceFromClasspath("org/jbpm/examples/subprocess/outcomeactivity/SubProcessDocument.jpdl.xml")
        .deploy();
  }

  protected void tearDown() throws Exception {
    repositoryService.deleteDeploymentCascade(subProcessReviewDeploymentId);
    repositoryService.deleteDeploymentCascade(subProcessDocumentDeploymentId);
    
    super.tearDown();
  }

  public void testSubProcessResultOk() {
    ProcessInstance processInstance = executionService
        .startProcessInstanceByKey("SubProcessDocument");
    
    assertNotNull(processInstance.findActiveExecutionIn("review"));

    List<Task> taskList = taskService.findPersonalTasks("johndoe");
    Task task = taskList.get(0);
    
    // the task in the sub process instance is completed 
    taskService.completeTask(task.getId(), "ok");

    // we check that the process instance has moved to the next step 
    processInstance = executionService.findProcessInstanceById(processInstance.getId());
    assertNotNull(processInstance.findActiveExecutionIn("next step"));
  }

  public void testSubProcessResultNok() {
    ProcessInstance processInstance = executionService
        .startProcessInstanceByKey("SubProcessDocument");
    
    assertNotNull(processInstance.findActiveExecutionIn("review"));

    List<Task> taskList = taskService.findPersonalTasks("johndoe");
    Task task = taskList.get(0);
    
    // the task in the sub process instance is completed 
    taskService.completeTask(task.getId(), "nok");

    // we check that the process instance has moved to update
    processInstance = executionService.findProcessInstanceById(processInstance.getId());
    assertNotNull(processInstance.findActiveExecutionIn("update"));
  }

  public void testSubProcessResultReject() {
    ProcessInstance processInstance = executionService
        .startProcessInstanceByKey("SubProcessDocument");
    
    assertNotNull(processInstance.findActiveExecutionIn("review"));

    List<Task> taskList = taskService.findPersonalTasks("johndoe");
    Task task = taskList.get(0);
    
    // the task in the sub process instance is completed 
    taskService.completeTask(task.getId(), "reject");

    // we check that the process instance has moved to close
    processInstance = executionService.findProcessInstanceById(processInstance.getId());
    assertNotNull(processInstance.findActiveExecutionIn("close"));
  }
}

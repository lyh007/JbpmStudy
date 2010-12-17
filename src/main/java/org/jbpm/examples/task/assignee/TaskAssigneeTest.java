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
package org.jbpm.examples.task.assignee;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jbpm.api.ProcessInstance;
import org.jbpm.api.task.Task;
import org.jbpm.test.JbpmTestCase;


/**
 * @author Tom Baeyens
 */
public class TaskAssigneeTest extends JbpmTestCase {

  String deploymentId;
  
  protected void setUp() throws Exception {
    super.setUp();
    
    deploymentId = repositoryService.createDeployment()
        .addResourceFromClasspath("org/jbpm/examples/task/assignee/process.jpdl.xml")
        .deploy();
  }

  protected void tearDown() throws Exception {
    repositoryService.deleteDeploymentCascade(deploymentId);
    
    super.tearDown();
  }

  public void testTaskAssignee() {
    Map<String, Object> variables = new HashMap<String, Object>(); 
    variables.put("order", new Order("johndoe"));
    ProcessInstance processInstance = executionService.startProcessInstanceByKey("TaskAssignee", variables);
    String pid = processInstance.getId();

    List<Task> taskList = taskService.findPersonalTasks("johndoe");
    assertEquals(1, taskList.size());
    Task task = taskList.get(0);
    assertEquals("review", task.getName());
    assertEquals("johndoe", task.getAssignee());

    // submit the task
    taskService.completeTask(task.getId());
    
    // verify that the task list is now empty
    taskList = taskService.findPersonalTasks("johndoe");
    assertEquals(0, taskList.size());

    // verify that process moved to the next state
    processInstance = executionService.findProcessInstanceById(pid);
    assertTrue(processInstance.isActive("wait"));
  }
}

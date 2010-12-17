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
package org.jbpm.examples.task.candidates;

import java.util.List;

import org.jbpm.api.ProcessInstance;
import org.jbpm.api.task.Task;
import org.jbpm.test.JbpmTestCase;


/**
 * @author Tom Baeyens
 */
public class TaskCandidatesTest extends JbpmTestCase {

  String deploymentId;
  String dept;

  protected void setUp() throws Exception {
    super.setUp();

    // create identities
    dept = identityService.createGroup("sales-dept");

    identityService.createUser("johndoe", "John", "Doe");
    identityService.createMembership("johndoe", dept, "developer");

    identityService.createUser("joesmoe", "Joe", "Smoe");
    identityService.createMembership("joesmoe", dept, "developer");

    // deploy process
    deploymentId = repositoryService.createDeployment()
        .addResourceFromClasspath("org/jbpm/examples/task/candidates/process.jpdl.xml")
        .deploy();
  }

  protected void tearDown() throws Exception {
    // delete process deployment
    repositoryService.deleteDeploymentCascade(deploymentId);

    // delete identities
    identityService.deleteGroup(dept);
    identityService.deleteUser("johndoe");
    identityService.deleteUser("joesmoe");

    super.tearDown();
  }

  public void testGroupCandidateAssignment() {
    ProcessInstance processInstance = executionService.startProcessInstanceByKey("TaskCandidates");
    String pid = processInstance.getId();

    // both johndoe and joesmoe will see the task in their *group* task list 
    List<Task> taskList = taskService.findGroupTasks("johndoe");
    assertEquals("Expected a single task in johndoe's task list", 1, taskList.size());
    Task task = taskList.get(0);
    assertEquals("review", task.getName());

    taskList = taskService.findGroupTasks("joesmoe");
    assertEquals("Expected a single task in joesmoe's group task list", 1, taskList.size());
    task = taskList.get(0);
    assertEquals("review", task.getName());

    // johndoe and joesmoe will NOT see the task in their *assigned* task list 

    taskList = taskService.findPersonalTasks("johndoe");
    assertEquals(0, taskList.size());

    taskList = taskService.findPersonalTasks("joesmoe");
    assertEquals(0, taskList.size());

    // lets assume that johndoe takes the task
    taskService.takeTask(task.getId(), "johndoe");

    // johndoe's and joesmoe's group task list is now empty  
    taskList = taskService.findGroupTasks("johndoe");
    assertEquals(0, taskList.size());

    taskList = taskService.findGroupTasks("joesmoe");
    assertEquals(0, taskList.size());

    // johndoe's assigned task list has the task in it
    taskList = taskService.findPersonalTasks("johndoe");
    assertEquals("Expected a single task being created", 1, taskList.size());
    task = taskList.get(0);
    assertEquals("review", task.getName());
    assertEquals("johndoe", task.getAssignee());

    // joesmoe will not see the task in the assigned task list
    taskList = taskService.findPersonalTasks("joesmoe");
    assertEquals(0, taskList.size());

    // now johndoe completes the task
    taskService.completeTask(task.getId());

    // verify that the task list is now empty
    taskList = taskService.findPersonalTasks("johndoe");
    assertEquals(0, taskList.size());

    // verify that process moved to the next state
    processInstance = executionService.findProcessInstanceById(pid);
    assertTrue(processInstance.isActive("wait"));
  }
}

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
package org.jbpm.examples.task.swimlane;

import java.util.List;

import org.jbpm.api.task.Task;
import org.jbpm.test.JbpmTestCase;


/**
 * @author Tom Baeyens
 */
public class TaskSwimlaneTest extends JbpmTestCase {

  String deploymentId;
  
  String dept;
  

  protected void setUp() throws Exception {
    super.setUp();
    
    // create identities
    dept = identityService.createGroup("sales-dept");

    identityService.createUser("johndoe", "John", "Doe");
    identityService.createMembership("johndoe", dept, "developer");
    
    // deploy process
    deploymentId = repositoryService.createDeployment()
        .addResourceFromClasspath("org/jbpm/examples/task/swimlane/process.jpdl.xml")
        .deploy();
    
  }

  protected void tearDown() throws Exception {
    // delete process deployment
    repositoryService.deleteDeploymentCascade(deploymentId);

    // delete identities
    identityService.deleteGroup(dept);
    identityService.deleteUser("johndoe");

    super.tearDown();
  }

  public void testTaskSwimlane() {    
    executionService.startProcessInstanceByKey("TaskSwimlane");

    List<Task> taskList = taskService.findGroupTasks("johndoe");
    assertEquals("Expected a single task in johndoe's task list", 1, taskList.size());
    Task task = taskList.get(0);
    String taskId = task.getId();
    assertEquals("enter order data", task.getName());
    assertNull(task.getAssignee());

    assertEquals(0, taskService.findPersonalTasks("johndoe").size());

    // lets assume that johndoe takes the task
    taskService.takeTask(taskId, "johndoe");

    // the next task will be created and assigned directly to johndoe
    // this is because johndoe was the person that took the previous task 
    // in the salesRepresentative swimlane.  so that person is most likely 
    // to know the context of this case

    // we'll check that the group task lists for johndoe and joesmoe are empty
    assertEquals(0, taskService.findGroupTasks("johndoe").size());

    // and that the task is directly assigned to johndoe
    taskList = taskService.findPersonalTasks("johndoe");
    assertEquals(1, taskList.size());
    task = taskList.get(0);
    assertEquals("enter order data", task.getName());
    assertEquals("johndoe", task.getAssignee());

    // submit the task
    taskService.completeTask(taskId);

    taskList = taskService.findPersonalTasks("johndoe");
    assertEquals(1, taskList.size());
    task = taskList.get(0);
    assertEquals("calculate quote", task.getName());
    assertEquals("johndoe", task.getAssignee());
  }
}

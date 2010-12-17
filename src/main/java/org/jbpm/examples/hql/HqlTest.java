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
package org.jbpm.examples.hql;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.jbpm.api.ProcessInstance;
import org.jbpm.api.task.Task;
import org.jbpm.test.JbpmTestCase;


/**
 * @author Tom Baeyens
 */
public class HqlTest extends JbpmTestCase {

  String deploymentId;

  String taskLaundryId;
  String taskDishesId;
  String taskIronId;
  
  protected void setUp() throws Exception {
    super.setUp();
    
    deploymentId = repositoryService.createDeployment()
        .addResourceFromClasspath("org/jbpm/examples/hql/process.jpdl.xml")
        .deploy();
    
    // add task laundry
    Task task = taskService.newTask();
    task.setName("laundry");
    taskLaundryId = taskService.saveTask(task);
    
    // add task dishes
    task = taskService.newTask();
    task.setName("dishes");
    taskDishesId = taskService.saveTask(task);
    
    // add task iron
    task = taskService.newTask();
    task.setName("iron");
    taskIronId = taskService.saveTask(task);
  }

  protected void tearDown() throws Exception {
    repositoryService.deleteDeploymentCascade(deploymentId);
    
    taskService.deleteTaskCascade(taskLaundryId);
    taskService.deleteTaskCascade(taskDishesId);
    taskService.deleteTaskCascade(taskIronId);
    
    super.tearDown();
  }

  public void testHql() {
    ProcessInstance processInstance = executionService.startProcessInstanceByKey("Hql");
    String processInstanceId = processInstance.getId();

    Set<String> expectedTaskNames = new HashSet<String>();
    expectedTaskNames.add("dishes");
    expectedTaskNames.add("iron");
    Collection<String> taskNames = (Collection<String>) executionService.getVariable(processInstanceId, "tasknames with i");
    taskNames = new HashSet<String>(taskNames);
    assertEquals(expectedTaskNames, taskNames);

    Object activities = executionService.getVariable(processInstanceId, "tasks");
    assertEquals("3", activities.toString());
  }
}

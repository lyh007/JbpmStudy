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
package org.jbpm.examples.task.variables;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jbpm.api.Execution;
import org.jbpm.api.task.Task;
import org.jbpm.test.JbpmTestCase;


/**
 * @author Tom Baeyens
 */
public class TaskVariablesTest extends JbpmTestCase {

  String deploymentId;
  
  protected void setUp() throws Exception {
    super.setUp();
    
    deploymentId = repositoryService.createDeployment()
        .addResourceFromClasspath("org/jbpm/examples/task/variables/process.jpdl.xml")
        .deploy();
  }

  protected void tearDown() throws Exception {
    repositoryService.deleteDeploymentCascade(deploymentId);
    
    super.tearDown();
  }

  public void testTaskAssignee() { 
    Map<String, Object> variables = new HashMap<String, Object>();
    variables.put("category", "big");
    variables.put("dollars", 100000);
    Execution execution = executionService.startProcessInstanceByKey("TaskVariables", variables);

    List<Task> taskList = taskService.findPersonalTasks("johndoe");
    assertFalse("Task list for assignee was empty", taskList.isEmpty());
    Task task = taskList.get(0);
    String taskId = task.getId();

    Set<String> variableNames = taskService.getVariableNames(taskId);

    Set<String> expectedVariableNames = new HashSet<String>();
    expectedVariableNames.add("category");
    expectedVariableNames.add("dollars");

    assertEquals(expectedVariableNames, variableNames);

    Map<String, Object> expectedVariables = variables;

    variables = taskService.getVariables(taskId, variableNames);

    assertEquals(expectedVariables, variables);

    variables = new HashMap<String, Object>();
    variables.put("category", "small");
    variables.put("lires", 923874893);
    taskService.setVariables(taskId, variables);

    expectedVariables = new HashMap<String, Object>();
    expectedVariables.put("category", "small");
    expectedVariables.put("lires", 923874893);
    expectedVariables.put("dollars", 100000);

    // The default java.util.HashSet#KeySet is not serializable
    Set<String> names = new HashSet(expectedVariables.keySet());
    
    variables = executionService.getVariables(execution.getId(), names);

    assertEquals(expectedVariables, variables);
  }
}

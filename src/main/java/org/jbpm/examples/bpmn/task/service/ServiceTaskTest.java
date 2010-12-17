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
package org.jbpm.examples.bpmn.task.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jbpm.api.NewDeployment;
import org.jbpm.api.ProcessInstance;
import org.jbpm.api.TaskQuery;
import org.jbpm.api.task.Task;
import org.jbpm.test.JbpmTestCase;

/**
 * @author Bernd Ruecker (bernd.ruecker@camunda.com)
 * @author Joram Barrez
 */
public class ServiceTaskTest extends JbpmTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		NewDeployment deployment = repositoryService.createDeployment();
		deployment.addResourceFromClasspath("org/jbpm/examples/bpmn/task/service/service_task_java.bpmn.xml");
		registerDeployment(deployment.deploy());
	}

	public void testJavaServiceTaskCall() {
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("var1", "value");

		ProcessInstance pi = executionService.startProcessInstanceByKey(
				"ServiceTaskJavaProcess", variables);

		assertNotNull(pi.getId());
		assertEquals("myMethod with arg1: value", executionService.getVariable(
				pi.getId(), "returnVar"));

		TaskQuery taskQuery = taskService.createTaskQuery();
		List<Task> allTasks = taskQuery.list();
		assertEquals(1, allTasks.size());

		taskService.completeTask(allTasks.get(0).getId());
		assertProcessInstanceEnded(pi);
	}

}

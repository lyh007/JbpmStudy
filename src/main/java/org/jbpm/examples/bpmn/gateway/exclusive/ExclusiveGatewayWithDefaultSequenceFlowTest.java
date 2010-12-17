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
package org.jbpm.examples.bpmn.gateway.exclusive;

import java.util.HashMap;
import java.util.Map;

import org.jbpm.api.NewDeployment;
import org.jbpm.api.ProcessInstance;
import org.jbpm.api.history.HistoryProcessInstance;
import org.jbpm.test.JbpmTestCase;

/**
 * 
 * @author Joram Barrez
 */
public class ExclusiveGatewayWithDefaultSequenceFlowTest extends JbpmTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		NewDeployment deployment = repositoryService.createDeployment();
		deployment.addResourceFromClasspath("org/jbpm/examples/bpmn/gateway/exclusive/exclusive_gateway_default_seq_flow.bpmn.xml");
		registerDeployment(deployment.deploy());
	}
	
	public void testWithLargeDeposit() {
		startAndVerifyProcess(9999, "localBank", "largeDeposit");
	}
	
	public void testWithForeignBankt() {
		startAndVerifyProcess(9999, "foreign", "foreignBank");
	}
	
	public void testDefaultSequenceFlow() {
		startAndVerifyProcess(1, "local", "standard");
	}
	
	private void startAndVerifyProcess(int amount, String bankType, String endId) {
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("amount", amount);
		vars.put("bankType", bankType);
		ProcessInstance processInstance = executionService.startProcessInstanceByKey("exclusiveGatewayDefaultSeqFlow", vars);
		assertProcessInstanceEnded(processInstance);
		
		HistoryProcessInstance historyProcessInstance = 
			historyService.createHistoryProcessInstanceQuery()
			 	          .processInstanceId(processInstance.getId())
			 	          .uniqueResult();
		assertEquals(endId, historyProcessInstance.getEndActivityName());
	}
	

}

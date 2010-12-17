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
package org.jbpm.examples.task.reminder;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.jbpm.api.ProcessInstance;
import org.jbpm.api.job.Job;
import org.jbpm.api.task.Task;
import org.jbpm.examples.task.assignee.Order;
import org.jbpm.test.JbpmTestCase;
import org.subethamail.wiser.Wiser;
import org.subethamail.wiser.WiserMessage;

/**
 * @author Alejandro Guizar
 */
public class TaskReminderTest extends JbpmTestCase {
  
  Wiser wiser = new Wiser();

  protected void setUp() throws Exception {
    super.setUp();

    // deploy process
    String deploymentId = repositoryService.createDeployment()
        .addResourceFromClasspath("org/jbpm/examples/task/reminder/process.jpdl.xml")
        .deploy();
    registerDeployment(deploymentId);

    // create actors
    identityService.createUser("johndoe", "John", "Doe", "john@doe");

    // start mail server
    wiser.setPort(2525);
    wiser.start();
  }

  protected void tearDown() throws Exception {
    // stop mail server
    wiser.stop();

    // delete actors
    identityService.deleteUser("johndoe");

    super.tearDown();
  }

  public void testTaskReminder() throws MessagingException, IOException {
    Map<String, Order> variables = Collections.singletonMap("order", new Order("johndoe"));
    ProcessInstance processInstance = executionService.startProcessInstanceByKey("TaskReminder", variables);

    Job job = managementService.createJobQuery()
        .processInstanceId(processInstance.getId())
        .uniqueResult();

    // timer was produced, no messages yet
    assertNotNull("expected job to be non-null", job);
    assertEquals(0, wiser.getMessages().size());

    managementService.executeJob(job.getId());

    // examine produced messages
    List<WiserMessage> wiserMessages = wiser.getMessages();
    assertEquals(1, wiserMessages.size());

    // only one message
    WiserMessage wiserMessage = wiserMessages.get(0);
    MimeMessage message = wiserMessage.getMimeMessage();

    // from
    Address[] from = message.getFrom();
    assertEquals(1, from.length);
    assertEquals("noreply@jbpm.org", from[0].toString());

    // to
    Address[] expectedTo = InternetAddress.parse("john@doe");
    Address[] to = message.getRecipients(RecipientType.TO);
    assert Arrays.equals(expectedTo, to) : Arrays.asList(to);

    // subject
    assertEquals("review", message.getSubject());

    // text
    assertTrue(((String) message.getContent()).contains("Do not forget about task \"review\""));

    // get task list
    List<Task> taskList = taskService.findPersonalTasks("johndoe");
    assertEquals(1, taskList.size());

    // only one task
    Task task = taskList.get(0);
    assertEquals("review", task.getName());
    assertEquals("johndoe", task.getAssignee());

    // submit task
    taskService.completeTask(task.getId());

    // timer should be gone
    job = managementService.createJobQuery()
        .processInstanceId(processInstance.getId())
        .uniqueResult();
    assertNull("expected timer to be null", job);
  }
}

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
package org.jbpm.examples.mail.template;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;

import org.jbpm.test.JbpmTestCase;
import org.subethamail.wiser.Wiser;
import org.subethamail.wiser.WiserMessage;

/**
 * @author Alejandro Guizar
 */
public class TemplateMailTest extends JbpmTestCase {

  Wiser wiser = null;
  
  String group1;
  
  String group2;

  protected void setUp() throws Exception {
    super.setUp();

    // deploy process
    String deploymentId = repositoryService.createDeployment()
        .addResourceFromClasspath("org/jbpm/examples/mail/template/process.jpdl.xml")
        .deploy();
    registerDeployment(deploymentId);

    // create actors
    identityService.createUser("bb", "Big Brother", null, "bb@oceania");
    identityService.createUser("obrien", null, "O'Brien", "obrien@miniluv");
    identityService.createUser("charr", null, "Charrington", "charr@miniluv");
    group1 = identityService.createGroup("thinkpol");
    group2 = identityService.createGroup("innerparty");
    identityService.createMembership("obrien", group2);
    identityService.createMembership("charr", group1);
    identityService.createMembership("obrien", group1);
    
    // start mail server
    wiser = new Wiser();
    wiser.setPort(2525);
    wiser.start();
  }

  protected void tearDown() throws Exception {
    wiser.stop();
    
    // delete actors
    identityService.deleteGroup(group1);
    identityService.deleteGroup(group2);
    identityService.deleteUser("bb");
    identityService.deleteUser("obrien");
    identityService.deleteUser("charr");

    super.tearDown();
  }

  public void testTemplateMail() throws MessagingException, IOException {
    // prepare dynamic values
    String addressee = "winston@minitrue";
    String newspaper = "times";
    Calendar calendar = Calendar.getInstance();
    calendar.clear();
    calendar.set(1983, Calendar.DECEMBER, 3);
    Date date = calendar.getTime();
    String details = "reporting bb dayorder doubleplusungood refs unpersons rewrite "
        + "fullwise upsub antefiling";
    // assemble variables
    Map<String, Object> variables = new HashMap<String, Object>();
    variables.put("addressee", addressee);
    variables.put("newspaper", newspaper);
    variables.put("date", date);
    variables.put("details", details);
    // start process instance
    executionService.startProcessInstanceByKey("TemplateMail", variables);

    // examine produced messages
    List<WiserMessage> wisMessages = wiser.getMessages();
    // winston, bb, innerparty(obrien), thinkpol(charr, obrien)
    assertEquals(5, wisMessages.size());

    for (WiserMessage wisMessage : wisMessages) {
      Message message = wisMessage.getMimeMessage();
      // from
      Address[] from = message.getFrom();
      assertEquals(1, from.length);
      assertEquals("noreply@jbpm.org", from[0].toString());
      // to
      Address[] expectedTo = InternetAddress.parse(addressee);
      Address[] to = message.getRecipients(RecipientType.TO);
      assert Arrays.equals(expectedTo, to) : Arrays.asList(to);
      // cc
      Address[] expectedCc = InternetAddress.parse("bb@oceania, obrien@miniluv");
      Address[] cc = message.getRecipients(RecipientType.CC);
      assert Arrays.equals(expectedCc, cc) : Arrays.asList(cc);
      // bcc - recipients undisclosed
      assertNull(message.getRecipients(RecipientType.BCC));
      // subject
      assertEquals("rectify " + newspaper, message.getSubject());
      // text
      assertEquals(newspaper + ' ' + date + ' ' + details, (String) message.getContent());
    }
  }
}

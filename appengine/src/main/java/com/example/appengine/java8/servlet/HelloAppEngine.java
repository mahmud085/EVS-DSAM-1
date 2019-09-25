/*
 * Copyright 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.appengine.java8.servlet;

// [START example]
import com.google.appengine.api.NamespaceManager;
import com.google.appengine.api.datastore.*;
import com.google.appengine.api.utils.SystemProperty;
import com.google.appengine.api.datastore.Query.SortDirection;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


// With @WebServlet annotation the webapp/WEB-INF/web.xml is no longer required.
@SuppressWarnings("ALL")
@WebServlet(name = "HelloAppEngine", value = "/admin/home")
public class HelloAppEngine extends HttpServlet {

  private static ArrayList<String> firstnames;

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    Properties properties = System.getProperties();

    response.setContentType("text/plain");
    response.getWriter().println("Hello App Engine - Standard using "
            + SystemProperty.version.get() + " Java "
            + properties.get("java.specification.version"));
  }

  public static String getInfo() {
    try {
      DatastoreService ds= DatastoreServiceFactory.getDatastoreService();
  /*    // Key will be assigned once written
      Entity incBookEntity = new Entity("Task");
      // Create the Entity
      incBookEntity.setProperty("firstname","newname");
      incBookEntity.setProperty("surname", "surnamenew") ;          // Add Property ("author", book.getAuthor())
      incBookEntity.setProperty("faculty", "newfaculty");
      ds.put(incBookEntity);
      */
      Query q;
      q = new Query("candidate").addSort("firstname",
              SortDirection.DESCENDING);

      PreparedQuery pq = ds.prepare(q);
      List<Entity> entities = pq.asList(FetchOptions.Builder.withLimit(5));
     firstnames=new ArrayList<>();
      for(Entity entity:entities){
        firstnames.add((String)entity.getProperty("firstname"));
      }

      Properties props = new Properties();
      Session session = Session.getDefaultInstance(props, null);

      try {
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress("oormila131@gmail.com"));
        msg.addRecipient(Message.RecipientType.TO,
                new InternetAddress("oormila131@gmail.com"));
        msg.setSubject("Your Example.com account has been activated");
        msg.setText("This is a test");
        Transport.send(msg);
        System.out.print("Email sent");
      } catch (AddressException e) {
        e.printStackTrace();
      } catch (MessagingException e) {
        e.printStackTrace();
      }
    }catch(Exception e){
      e.printStackTrace();
    }


    return "Version: " + System.getProperty("java.version")
          + " OS: " + System.getProperty("os.name")
          + " User: " + System.getProperty("user.name")+"firstnames are  : "+firstnames.toString();
  }

}
// [END example]

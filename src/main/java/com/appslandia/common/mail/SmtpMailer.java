// The MIT License (MIT)
// Copyright © 2015 AppsLandia. All rights reserved.

// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:

// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

package com.appslandia.common.mail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

import com.appslandia.common.base.InitializeObject;
import com.appslandia.common.crypto.SecureConfig;
import com.appslandia.common.utils.Asserts;
import com.appslandia.common.utils.CollectionUtils;
import com.appslandia.common.utils.ExceptionUtils;

import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.MimeMessage;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class SmtpMailer extends InitializeObject {

  protected SecureConfig config;
  protected Session session;

  @Override
  protected void init() throws Exception {
    Asserts.notNull(this.config, "config is required.");

    this.session = Session.getInstance(createMailProps());
  }

  protected Properties createMailProps() {
    Properties props = new Properties();
    Iterator<String> iter = this.config.getKeys();

    while (iter.hasNext()) {
      String key = iter.next();

      if (key.startsWith("mail.")) {
        props.put(key, this.config.getString(key));
      }
    }
    return props;
  }

  public void send(MailerMessage message) throws MessagingException {
    send(CollectionUtils.toList(new ArrayList<>(1), message));
  }

  public void send(List<MailerMessage> messages) throws MessagingException {
    initialize();
    Asserts.notNull(messages);

    try (Transport transport = this.session.getTransport("smtp")) {

      String user = this.config.getString("mail.smtp.user");
      String password = this.config.getString("mail.smtp.password");
      transport.connect(user, password);

      String toEmails = this.config.getString("mail.to_emails");

      for (MailerMessage mailerMessage : messages) {
        MimeMessage message = mailerMessage.toMimeMessage(this, toEmails);
        message.saveChanges();

        transport.sendMessage(message, message.getAllRecipients());
      }
    }
  }

  public void sendAsync(MailerMessage message, Executor executor, Consumer<Exception> errorHandler) {
    sendAsync(Arrays.asList(message), executor, errorHandler);
  }

  public void sendAsync(List<MailerMessage> messages, Executor executor, Consumer<Exception> errorHandler) {
    initialize();
    Asserts.notNull(messages);
    Asserts.notNull(executor);

    executor.execute(new Runnable() {

      @Override
      public void run() {
        try {
          send(messages);

        } catch (Exception ex) {
          if (errorHandler != null) {
            errorHandler.accept(ex);
          }
          throw ExceptionUtils.toUncheckedException(ex);
        }
      }
    });
  }

  /**
   * <ul>
   * <li>mail.smtp.host=smtp.example.com</li>
   * <li>mail.smtp.port=587</li>
   * <li>mail.smtp.user=your-email@example.com</li>
   * <li>mail.smtp.password=your-email-password</li>
   * <li>mail.smtp.auth=true</li>
   * <li>mail.smtp.starttls.enable=true</li>
   * <li>mail.smtp.ssl.trust=smtp.example.com</li>
   * <li>mail.from=no-reply@example.com</li>
   * <li>mail.debug=false</li>
   * <li>mail.to_emails=to-email@example.com</li>
   * </ul>
   * 
   * @param config
   * @return
   */
  public SmtpMailer setConfig(SecureConfig config) {
    assertNotInitialized();
    this.config = config;
    return this;
  }
}

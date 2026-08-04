// Licensed under the MIT License.
// See LICENSE file in the project root for details.

package com.appslandia.common.mail;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

import com.appslandia.common.base.InitializingObject;
import com.appslandia.common.crypto.SecureConfig;
import com.appslandia.common.utils.Arguments;
import com.appslandia.common.utils.ExceptionUtils;

import jakarta.mail.MessagingException;
import jakarta.mail.Session;

/**
 *
 * @author Loc Ha
 *
 */
public class SmtpMailer extends InitializingObject {

  protected SecureConfig config;
  protected Session session;

  @Override
  protected void init() throws Exception {
    Arguments.notNull(config, "config is required.");

    session = Session.getInstance(config.toProperties(key -> key.startsWith("mail.")));
  }

  public void send(MailerMessage message) throws MessagingException {
    send(List.of(message));
  }

  public void send(List<MailerMessage> messages) throws MessagingException {
    initialize();
    Arguments.notNull(messages);

    try (var transport = session.getTransport("smtp")) {

      var user = config.getString("mail.smtp.user");
      var password = config.getString("mail.smtp.password");
      transport.connect(user, password);

      var toEmails = config.getString("mail.to_emails");

      for (MailerMessage mailerMessage : messages) {
        var message = mailerMessage.toMimeMessage(this, toEmails);
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
    Arguments.notNull(messages);
    Arguments.notNull(executor);

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

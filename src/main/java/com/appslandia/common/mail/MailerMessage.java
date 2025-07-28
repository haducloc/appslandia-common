// The MIT License (MIT)
// Copyright © 2015 Loc Ha

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

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import com.appslandia.common.utils.Arguments;
import com.appslandia.common.utils.EmailUtils;
import com.appslandia.common.utils.MimeTypes;

import jakarta.mail.Address;
import jakarta.mail.Message.RecipientType;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

/**
 *
 * @author Loc Ha
 *
 */
public class MailerMessage {

  private Address sender;
  private List<Address> from;
  private List<Address> replyTo;

  private List<Address> to;
  private List<Address> cc;
  private List<Address> bcc;

  private String subject;
  private Object content;
  private String contentType;

  private Date sentDate;
  private Consumer<MimeMessage> msgInit;

  public MailerMessage clearFrom() {
    if (this.from != null) {
      this.from.clear();
    }
    return this;
  }

  public MailerMessage sender(String email) throws AddressException {
    this.sender = new InternetAddress(email);
    return this;
  }

  public MailerMessage sender(String email, String person) throws AddressException {
    this.sender = EmailUtils.toAddressEmail(email, person);
    return this;
  }

  public MailerMessage from(String email) throws AddressException {
    getFrom().add(new InternetAddress(email));
    return this;
  }

  public MailerMessage from(String email, String person) throws AddressException {
    getFrom().add(EmailUtils.toAddressEmail(email, person));
    return this;
  }

  public MailerMessage replyTo(String email) throws AddressException {
    getReplyTo().add(new InternetAddress(email));
    return this;
  }

  public MailerMessage replyTo(String email, String person) throws AddressException {
    getReplyTo().add(EmailUtils.toAddressEmail(email, person));
    return this;
  }

  public MailerMessage to(String email) throws AddressException {
    getTo().add(new InternetAddress(email));
    return this;
  }

  public MailerMessage to(String email, String person) throws AddressException {
    getTo().add(EmailUtils.toAddressEmail(email, person));
    return this;
  }

  public MailerMessage cc(String email) throws AddressException {
    getCc().add(new InternetAddress(email));
    return this;
  }

  public MailerMessage cc(String email, String person) throws AddressException {
    getCc().add(EmailUtils.toAddressEmail(email, person));
    return this;
  }

  public MailerMessage bcc(String email) throws AddressException {
    getBcc().add(new InternetAddress(email));
    return this;
  }

  public MailerMessage bcc(String email, String person) throws AddressException {
    getBcc().add(EmailUtils.toAddressEmail(email, person));
    return this;
  }

  public MailerMessage subject(String subject) {
    this.subject = subject;
    return this;
  }

  public MailerMessage content(Multipart content) {
    this.content = Arguments.notNull(content);
    return this;
  }

  public MailerMessage content(Object content, String type) {
    this.content = Arguments.notNull(content);
    this.contentType = Arguments.notNull(type);
    return this;
  }

  public MailerMessage htmlContent(String content) {
    return content(content, MimeTypes.TEXT_HTML_UTF8);
  }

  public MailerMessage textContent(String content) {
    return content(content, MimeTypes.TEXT_PLAIN_UTF8);
  }

  public MailerMessage sentDate(Date sentDate) {
    this.sentDate = sentDate;
    return this;
  }

  public MailerMessage msgInit(Consumer<MimeMessage> msgInit) {
    this.msgInit = msgInit;
    return this;
  }

  public void send(SmtpMailer mailer) throws MessagingException {
    mailer.send(this);
  }

  public MimeMessage toMimeMessage(SmtpMailer mailer, String toEmails) throws MessagingException {
    var msg = new MimeMessage(mailer.session);

    // Sender
    // Notes: msg.setSender(this.sender); throws Exception in
    // jakarta.mail-1.6.5.jar

    if (this.sender != null) {
      msg.setSender(this.sender);
    } else {
      msg.removeHeader("Sender");
    }

    // From
    if (this.from != null) {
      msg.addFrom(this.from.toArray(new Address[this.from.size()]));
    }

    // Reply To
    if (this.replyTo != null) {
      msg.setReplyTo(this.replyTo.toArray(new Address[this.replyTo.size()]));
    }

    // toEmails?
    if (toEmails != null) {
      msg.addRecipients(RecipientType.TO, InternetAddress.parse(toEmails));
    } else {
      // Recipients
      if (this.to != null) {
        msg.addRecipients(RecipientType.TO, this.to.toArray(new Address[this.to.size()]));
      }
      if (this.cc != null) {
        msg.addRecipients(RecipientType.CC, this.cc.toArray(new Address[this.cc.size()]));
      }
      if (this.bcc != null) {
        msg.addRecipients(RecipientType.BCC, this.bcc.toArray(new Address[this.bcc.size()]));
      }
    }

    // Subject
    msg.setSubject(this.subject, StandardCharsets.UTF_8.name());

    // Content
    if (this.content != null) {
      msg.setContent(this.content, this.contentType);
    }

    // sentDate
    if (this.sentDate != null) {
      msg.setSentDate(this.sentDate);
    }

    // Others
    if (this.msgInit != null) {
      this.msgInit.accept(msg);
    }
    return msg;
  }

  protected List<Address> getFrom() {
    if (this.from == null) {
      this.from = new ArrayList<>();
    }
    return this.from;
  }

  protected List<Address> getReplyTo() {
    if (this.replyTo == null) {
      this.replyTo = new ArrayList<>();
    }
    return this.replyTo;
  }

  protected List<Address> getTo() {
    if (this.to == null) {
      this.to = new ArrayList<>();
    }
    return this.to;
  }

  protected List<Address> getCc() {
    if (this.cc == null) {
      this.cc = new ArrayList<>();
    }
    return this.cc;
  }

  protected List<Address> getBcc() {
    if (this.bcc == null) {
      this.bcc = new ArrayList<>();
    }
    return this.bcc;
  }
}

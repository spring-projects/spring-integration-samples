This example demonstrates the following aspects of the Mail support available with Spring Integration:
1. IMAP IDLE Channel Adapter (with sample GMail configuration)

Simply change configuration file (src/main/resources/META-INF/spring/integration/mail-imap-idle-config.xml) 
to point to your existing GMail account (replace userid and password) and run the GmailImapIdleAdapterTest.
Once started start sending emails to your account. You'll see that adapter will receive messages and will
publish them to a channel specified by 'channel' attribute.

Make sure you enable your GMail account for IMAP/POP3 access.

You should see the output similar to this:

DEBUG: org.springframework.integration.mail.ImapMailReceiver - connecting to store [imaps://<your_user_name>:*****@imap.gmail.com:993/inbox]
DEBUG: org.springframework.integration.mail.ImapMailReceiver - opening folder [imaps://<your_user_name>:*****@imap.gmail.com:993/inbox]
INFO : org.springframework.integration.mail.ImapMailReceiver - attempting to receive mail from folder [INBOX]

Send a Message and you will see:

DEBUG: org.springframework.integration.mail.ImapMailReceiver - found 2 new messages
DEBUG: org.springframework.integration.mail.ImapIdleChannelAdapter - received 2 mail messages
INFO : org.springframework.integration.samples.mail.imapidle.GmailImapIdleAdapterTest - Message: [Payload=javax.mail.internet.MimeMessage@1f00aff5][Headers={$timestamp=1284745370451, $id=9dfd4b30-2d15-4905-9301-f39aa4073ea0}]
INFO : org.springframework.integration.samples.mail.imapidle.GmailImapIdleAdapterTest - Message: [Payload=javax.mail.internet.MimeMessage@66200db9][Headers={$timestamp=1284745370452, $id=4b8322ba-f469-4afc-af4f-1332a94b735b}]
DEBUG: org.springframework.integration.mail.ImapIdleChannelAdapter - waiting for mail
DEBUG: org.springframework.integration.mail.ImapMailReceiver - opening folder [imaps://<your_user_name>:*****@imap.gmail.com:993/inbox]

You can experiment with various attributes of the adapter (e.g., should-delete-messages, should-mark-messages-as-read etc...)

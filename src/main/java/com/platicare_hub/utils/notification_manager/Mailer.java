// package com.platicare_hub.utils.notification_manager;

// import java.io.File;
// import java.io.IOException;
// import java.nio.file.Files;
// import java.nio.file.Paths;
// import java.util.Properties;

// import com.platicare_hub.utils.Constants;

// import jakarta.mail.Authenticator;
// import jakarta.mail.Message;
// import jakarta.mail.MessagingException;
// import jakarta.mail.PasswordAuthentication;
// import jakarta.mail.Session;
// import jakarta.mail.Transport;
// import jakarta.mail.internet.InternetAddress;
// import jakarta.mail.internet.MimeMessage;


// public class Mailer
// {
//     private static String host = Constants.SMTP_SERVER;
//     private static String port = Constants.SMTP_SERVER_PORT;
//     private static String username = Constants.SMTP_SERVER_USERNAME;
//     private static String password = Constants.SMTP_SERVER_PASSWORD;
//     private static Properties props = new Properties();

//     static
//     {
//         props.put("mail.smtp.host", host);
//         props.put("mail.smtp.port", port);
//         props.put("mail.smtp.auth", "true");
//         props.put("mail.smtp.starttls.enable", "true");
//         props.put("mail.transport.protocol", "smtp");
//         props.put("mail.smtp.ssl.protocols", "TLSv1.2");
//     }

//     public static String getHtmlContent(String templateName)
//     {
//         String htmlTemplate = null;
//         try
//         {
//             htmlTemplate = new String(Files.readAllBytes(Paths.get("email-templates" + File.separator + templateName)));
//         }
//         catch (IOException e)
//         {
//             e.printStackTrace();
//         }
//         return htmlTemplate;
//     }


//     public static void sendRegistrationUrl(String templateName, String recipientEmail, String recipientName, String registrationUrl)
//     {
//         Session session = Session.getInstance(props, new Authenticator() {
//             protected PasswordAuthentication getPasswordAuthentication()
//             {
//                 return new PasswordAuthentication(username, password);
//             }
//         });

//         try
//         {
//             Message message = new MimeMessage(session);
//             message.setFrom(new InternetAddress(username));
//             message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));

//             String subject = templateName.replace(".html", "").replace("-template", "").replace("-", " ");
//             message.setSubject(subject);

//             String htmlContent = String.format(getHtmlContent(templateName), recipientName, registrationUrl);
//             message.setContent(htmlContent, "text/html; charset=utf-8");

//             Transport.send(message);
//         }
//         catch (MessagingException e)
//         {
//             e.printStackTrace();
//         }
//     }

// }


package com.platicare_hub.utils.notification_manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.stream.Collectors;

import com.platicare_hub.utils.Constants;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class Mailer
{
    private static final String host = Constants.SMTP_SERVER;
    private static final String port = Constants.SMTP_SERVER_PORT;
    private static final String username = Constants.SMTP_SERVER_USERNAME;
    private static final String password = Constants.SMTP_SERVER_PASSWORD;

    private static Properties getMailProperties()
    {
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.debug", "true");
        return props;
    }

    public static String getHtmlContent(String templateName)
    {
        String resourcePath = "/email-templates/" + templateName;
        try (InputStream is = Mailer.class.getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new IOException("Template not found: " + resourcePath);
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                return reader.lines().collect(Collectors.joining(System.lineSeparator()));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void sendRegistrationUrl(String templateName, String recipientEmail, String recipientName, String registrationUrl)
    {
        Session session = Session.getInstance(getMailProperties(), new Authenticator()
        {
            @Override
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(username, password);
            }
        });

        try
        {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));

            String subject = templateName.replace(".html", "").replace("-template", "").replace("-", " ");
            message.setSubject(subject);

            String rawContent = getHtmlContent(templateName);
            String htmlContent = rawContent.replace("%s1", recipientName).replace("%s2", registrationUrl);
            
            message.setContent(htmlContent, "text/html; charset=utf-8");

            Transport.send(message);
        }
        catch (MessagingException e)
        {
            // throw new RuntimeException("Failed to send email: " + e.getMessage(), e);
        }
    }
}
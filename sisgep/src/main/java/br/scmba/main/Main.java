package br.scmba.main;

import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Main {

    public class MyAuthenticator extends Authenticator {
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication("santacasa@santacasaba.org.br", "Office365Scmba");
        }
    }

    public static void bypassSSL() {
        final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            @Override
            public void checkClientTrusted(final java.security.cert.X509Certificate[] certs, final String authType) {
            }

            @Override
            public void checkServerTrusted(final java.security.cert.X509Certificate[] certs, final String authType) {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        } };
        try {
            final SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            SSLContext.setDefault(sc);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(final String[] args) throws Exception {
        // System.setProperty("mail.smtps.auth", "true");
        // System.setProperty("mail.smtps.host", "smtp.teste.com.br");
        new Main().sender();
    }

    public void sender() throws Exception {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT-3:00"));

        System.out.println("Iniciando envio...");

        final Properties props = new Properties();

        final String smtp = "smtp.office365.com";

        props.put("mail.smtp.host", smtp);
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtps.auth", "true");
        props.put("mail.debug", "true");

        props.put("mail.smtp.port", "587");


        final Session session = Session.getInstance(props, new MyAuthenticator());

        // criando a mensagem
        final MimeMessage message = new MimeMessage(session);

        final Address from = new InternetAddress("santacasa@santacasaba.org.br");
        final Address to = new InternetAddress("fernanda.peixoto@santacasaba.org.br");
        message.setFrom(from);
        message.addRecipient(RecipientType.TO, to);
        message.setSentDate(new Date());
        message.setSubject("teste");
        message.setText("Texto da mensagem!");
        bypassSSL();
        Transport.send(message);

        System.out.println("Enviado!");
    }
}
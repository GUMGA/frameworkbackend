package io.gumga.amazon.ses;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.handlers.AsyncHandler;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceAsync;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceAsyncClientBuilder;
import com.amazonaws.services.simpleemail.model.*;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Properties;

/**
 * Serviço para gerenciamento de e-mails
 */
@Service
public class GumgaAmazonSes {

    public static String REGION = "us-east-1";

    private AmazonSimpleEmailServiceAsyncClientBuilder asesacb;
    private AmazonSimpleEmailServiceAsync client;

    /**
     * Método que inicia o serviço
     */
    @PostConstruct
    public void init() {
        System.out.println("-----> INIT " + this.getClass().getSimpleName());
        asesacb = AmazonSimpleEmailServiceAsyncClientBuilder.standard();
        asesacb.setRegion(REGION);
        client = asesacb.build();
    }

    /**
     * Método executado no fim do serviço
     */
    @PreDestroy
    public void destroy() {
        System.out.println("-----> Destroy " + this.getClass().getSimpleName());
        client.shutdown();
        client = null;
    }

    /**
     * Envia e-mail assíncrono
     * @param listener Interface que contém métodos de status da execução da chamada assíncrona do método
     * @param HTMLBODY Texto HTML
     * @param TEXTBODY Texto Simples
     * @param SUBJECT Assunto
     * @param FROM De
     * @param TO Para
     * @throws IOException Exceção no envio de e-mail
     */
    public void sendAsysncEmail(GumgaAWSAsyncListener listener, String HTMLBODY, String TEXTBODY, String SUBJECT, String FROM, String... TO) throws IOException {
        SendEmailRequest request = getSendEmailRequest(HTMLBODY, TEXTBODY, SUBJECT, FROM, TO);

        request.setGeneralProgressListener(new ProgressListener() {
            @Override
            public void progressChanged(ProgressEvent pe) {
                listener.progressChanged(pe.toString());
            }
        });
        client.sendEmailAsync(request, new AsyncHandler<SendEmailRequest, SendEmailResult>() {
            @Override
            public void onError(Exception ex) {
                listener.onError(ex);
            }

            @Override
            public void onSuccess(SendEmailRequest rqst, SendEmailResult result) {
                listener.onSuccess(rqst, result);
            }
        });
    }

    /**
     * Envia e-mail síncrono
     * @param HTMLBODY Texto HTML
     * @param TEXTBODY Texto Simples
     * @param SUBJECT Assunto
     * @param FROM De
     * @param TO Para
     * @return Resultado de envio do E-mail
     */
    public SendEmailResult sendEmail(String HTMLBODY, String TEXTBODY, String SUBJECT, String FROM, String... TO) {
        SendEmailRequest request = getSendEmailRequest(HTMLBODY, TEXTBODY, SUBJECT, FROM, TO);
        return client.sendEmail(request);
    }

    /**
     * Modelo de requisição do e-mail
     * @param HTMLBODY Texto HTML
     * @param TEXTBODY Texto Simples
     * @param SUBJECT Assunto
     * @param FROM De
     * @param TO Para
     * @return
     */
    public SendEmailRequest getSendEmailRequest(String HTMLBODY, String TEXTBODY, String SUBJECT, String FROM, String[] TO) {
        return new SendEmailRequest()
                .withDestination(new Destination().withToAddresses(TO))
                .withMessage(new Message()
                        .withBody(new Body().withHtml(new Content().withCharset("UTF-8").withData(HTMLBODY))
                                .withText(new Content().withCharset("UTF-8").withData(TEXTBODY))).withSubject(new Content()
                                .withCharset("UTF-8").withData(SUBJECT))).withSource(FROM);
    }

    /**
     * Envia e-mail com anexo de forma assíncrona
     * @param listener Interface que contém métodos de status da execução da chamada assíncrona do método
     * @param HTMLBODY Texto HTML
     * @param SUBJECT Assunto
     * @param FROM De
     * @param raw Binário dos arquivos
     * @param TO Para
     * @throws IOException Exceção
     * @throws MessagingException Exceção
     */
    public void sendAsysncEmail(GumgaAWSAsyncListener listener, String HTMLBODY, String SUBJECT, String FROM, ByteArrayOutputStream raw, String... TO) throws IOException, MessagingException {
        SendRawEmailRequest request = getSendRawEmailRequest(HTMLBODY, SUBJECT, FROM, raw, TO);

        request.setGeneralProgressListener(new ProgressListener() {
            @Override
            public void progressChanged(ProgressEvent pe) {
                listener.progressChanged(pe.toString());
            }
        });
        client.sendRawEmailAsync(request, new AsyncHandler<SendRawEmailRequest, SendRawEmailResult>() {
            @Override
            public void onError(Exception ex) {
                listener.onError(ex);
            }

            @Override
            public void onSuccess(SendRawEmailRequest request, SendRawEmailResult sendRawEmailResult) {

            }
        });
    }

    /**
     * Envia e-mail com anexo de forma síncrona
     * @param HTMLBODY Texto HTML
     * @param SUBJECT Assunto
     * @param FROM De
     * @param raw Binário dos arquivos
     * @param TO Para
     * @return Objeto
     * @throws IOException Exceção
     * @throws MessagingException Exceção
     */
    public SendRawEmailResult sendEmail(String HTMLBODY, String SUBJECT, String FROM, ByteArrayOutputStream raw, String... TO) throws IOException, MessagingException {
        SendRawEmailRequest request = getSendRawEmailRequest(HTMLBODY, SUBJECT, FROM, raw, TO);
        return client.sendRawEmail(request);
    }

    /**
     * Objeto de envio de anexos
     * @param HTMLBODY Texto HTML
     * @param SUBJECT Assunto
     * @param FROM De
     * @param raw Binário dos arquivos
     * @param TO Para
     * @return Objeto
     * @throws MessagingException Exceção
     * @throws IOException Exceção
     */
    public SendRawEmailRequest getSendRawEmailRequest(String HTMLBODY, String SUBJECT, String FROM, ByteArrayOutputStream raw, String[] TO) throws MessagingException, IOException {
        Properties props = new Properties();

        ProfileCredentialsProvider profileCredentialsProvider = new ProfileCredentialsProvider();
        AWSCredentials credentials = profileCredentialsProvider.getCredentials();

        props.setProperty("mail.transport.protocol", "aws");
        props.setProperty("mail.aws.user", credentials.getAWSAccessKeyId());
        props.setProperty("mail.aws.password", credentials.getAWSSecretKey());
        Session session = Session.getDefaultInstance(new Properties());
        MimeMessage message = new MimeMessage(session);
        message.setSubject(SUBJECT, "UTF-8");
        message.setText(HTMLBODY);

        message.setFrom(new InternetAddress(FROM));

        for (String t : TO) {
            message.addRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(t));
        }

        PrintStream out = System.out;
        message.writeTo(out);
        message.writeTo(raw);

        RawMessage rawMessage = new RawMessage(ByteBuffer.wrap(raw.toByteArray()));

        return new SendRawEmailRequest()
                .withDestinations(TO)
                .withRawMessage(rawMessage);
    }

    /**
     * Envia e-mail com anexo de forma síncrona
     * @param subject Assunto
     * @param message Mensagem
     * @param attachement Arquivo
     * @param fileName Nome do arquivo
     * @param contentType Tipo de arquivo
     * @param from De
     * @param to Para
     */
    public void sendAsysncEmail(String subject, String message, byte[] attachement, String fileName, String contentType, String from, String... to) {
        try {
            // JavaMail representation of the message
            Session s = Session.getInstance(new Properties(), null);
            MimeMessage mimeMessage = new MimeMessage(s);

            // Sender and recipient
            mimeMessage.setFrom(new InternetAddress(from));
            for (String toMail : to) {
                mimeMessage.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(toMail));
            }

            // Subject
            mimeMessage.setSubject(subject);

            // Add a MIME part to the message
            MimeMultipart mimeBodyPart = new MimeMultipart();
            BodyPart part = new MimeBodyPart();
            part.setContent(message, String.valueOf(MediaType.TEXT_HTML));
            mimeBodyPart.addBodyPart(part);

            // Add a attachement to the message
            part = new MimeBodyPart();
            DataSource source = new ByteArrayDataSource(attachement, contentType);
            part.setDataHandler(new DataHandler(source));
            part.setFileName(fileName);
            mimeBodyPart.addBodyPart(part);

            mimeMessage.setContent(mimeBodyPart);

            // Create Raw message
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            mimeMessage.writeTo(outputStream);
            RawMessage rawMessage = new RawMessage(ByteBuffer.wrap(outputStream.toByteArray()));

            // Send Mail
            SendRawEmailRequest rawEmailRequest = new SendRawEmailRequest(rawMessage);
            rawEmailRequest.setDestinations(Arrays.asList(to));
            rawEmailRequest.setSource(from);
            client.sendRawEmail(rawEmailRequest);

        } catch (IOException | MessagingException e) {
            // your Exception
            e.printStackTrace();
        }
    }
}

package io.gumga.amazon;

import com.amazonaws.services.simpleemail.model.SendEmailResult;
import com.amazonaws.services.simpleemail.model.SendRawEmailResult;
import com.amazonaws.services.sqs.model.ListQueuesResult;
import com.amazonaws.services.sqs.model.Message;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.gumga.amazon.ses.GumgaAmazonSes;
import io.gumga.amazon.ses.GumgaAmazonSqs;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.mail.MessagingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by willian on 05/10/17.
 * https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/auth/profile/package-summary.html
 * Estes testes estão desativados para não ficar enviando e-mails.
 */
public class GumgaAmazonSesTest extends AbstractTest {

    @Autowired
    private GumgaAmazonSes gumgaAmazonSes;

    @Autowired
    private GumgaAmazonSqs gumgaAmazonSqs;

    @Test
    public void sendEmail() throws IOException {
        // Este teste está comentado para não ficar enviando e-mail.
//        SendEmailResult sendEmailResult = gumgaAmazonSes.sendEmail("<!DOCTYPE html><html><body>ola</body></html>", "asdasd", "qweqwasdasdasdasde@gmail.com", "no-reply@gileadesistemas.com.br", "williawsdasdasdasdasdasda@gmail.com");
//        try {
//            GumgaAmazonSes ses = new GumgaAmazonSes();
//            ses.init();
//            SendRawEmailResult sendEmailResult = ses.sendEmail("<!DOCTYPE html><html><body>ola</body></html>", "qweqwasdasdasdasde@gmail.com", "no-reply@gileadesistemas.com.br", new ByteArrayOutputStream(), "williawsdasdasdasdasdasda@gmail.com");
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        }
        //
//
//        ListQueuesResult listQueuesResult = gumgaAmazonSqs.listQueues();
//        for (String url : listQueuesResult.getQueueUrls()) {
//            List<Message> receiveMessagesOfQueue = gumgaAmazonSqs.getReceiveMessagesOfQueue(url);
//            for (Message message : receiveMessagesOfQueue) {
//
//                ObjectMapper mapper = new ObjectMapper();
//                JsonNode actualObj = mapper.readTree(message.getBody());
//
//                JsonNode msg = mapper.readTree(actualObj.get("Message").asText());
//
//                System.out.println(sendEmailResult.getMessageId() + " \n----------- \n" + message);
//                System.out.println("------------>");
//            }
//        }


        assertEquals(1, 1);
    }
}

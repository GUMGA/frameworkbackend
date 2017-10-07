package io.gumga.amazon.ses;

import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.handlers.AsyncHandler;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceAsync;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceAsyncClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendEmailResult;
import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.stereotype.Service;

@Service
public class GumgaAmazonSes {

    public static String REGION = "us-east-1";

    private AmazonSimpleEmailServiceAsyncClientBuilder asesacb;
    private AmazonSimpleEmailServiceAsync client;

    @PostConstruct
    public void init() {
        System.out.println("-----> INIT " + this.getClass().getSimpleName());
        asesacb = AmazonSimpleEmailServiceAsyncClientBuilder.standard();
        asesacb.setRegion(REGION);
        client = asesacb.build();
    }
    
    @PreDestroy
    public void destroy(){
        System.out.println("-----> Destroy " + this.getClass().getSimpleName());
        client.shutdown();
        client=null;
    }

    public void sendAsysncEmail(GumgaAWSAsyncListener listener, String HTMLBODY, String TEXTBODY, String SUBJECT, String FROM, String... TO) throws IOException {
        SendEmailRequest request = new SendEmailRequest()
                .withDestination(new Destination().withToAddresses(TO))
                .withMessage(new Message()
                        .withBody(new Body().withHtml(new Content().withCharset("UTF-8").withData(HTMLBODY))
                                .withText(new Content().withCharset("UTF-8").withData(TEXTBODY))).withSubject(new Content()
                        .withCharset("UTF-8").withData(SUBJECT))).withSource(FROM);

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
}

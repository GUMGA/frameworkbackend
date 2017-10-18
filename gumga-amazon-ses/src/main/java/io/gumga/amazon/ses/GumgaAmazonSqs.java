package io.gumga.amazon.ses;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.handlers.AsyncHandler;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceAsyncClientBuilder;
import com.amazonaws.services.sqs.*;
import com.amazonaws.services.sqs.model.ListQueuesRequest;
import com.amazonaws.services.sqs.model.ListQueuesResult;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.Future;

@Service
public class GumgaAmazonSqs {

    public static String REGION = "us-east-1";
    private AmazonSQSClientBuilder amazonSQSClientBuilder;
    private AmazonSQS sqs;

    private AmazonSQSAsyncClientBuilder amazonSQSAsyncClientBuilder;
    private AmazonSQSAsync sqsAsync;


    @PostConstruct
    public void init() {
        System.out.println("-----> INIT " + this.getClass().getSimpleName());
        sqs = AmazonSQSClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
        sqsAsync = AmazonSQSAsyncClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
    }

    public ListQueuesResult listQueues() {
        ListQueuesResult listQueuesResult = sqs.listQueues();
        return listQueuesResult;
    }

    public Future<ListQueuesResult> listAsyncQueues(GumgaAWSAsyncListener listener) {
         return sqsAsync.listQueuesAsync(new AsyncHandler<ListQueuesRequest, ListQueuesResult>() {
            @Override
            public void onError(Exception e) {
                listener.onError(e);
            }

            @Override
            public void onSuccess(ListQueuesRequest request, ListQueuesResult listQueuesResult) {
                listener.onSuccess(request, listQueuesResult);
            }
        });
    }

    public List<Message> getReceiveMessagesOfQueue(String myQueueUrl) {
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(myQueueUrl);
        List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
        return messages;
    }

    @PreDestroy
    public void destroy() {
        System.out.println("-----> Destroy " + this.getClass().getSimpleName());
        sqs.shutdown();
        sqs = null;
    }

}

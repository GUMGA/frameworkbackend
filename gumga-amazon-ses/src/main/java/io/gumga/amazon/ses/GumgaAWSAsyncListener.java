package io.gumga.amazon.ses;

import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.AmazonWebServiceResult;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendEmailResult;

/**
 *
 * @author munif
 */
public interface GumgaAWSAsyncListener {

    void progressChanged(String progress);

    void onError(Exception ex);

    void onSuccess(AmazonWebServiceRequest rqst, AmazonWebServiceResult result);

}

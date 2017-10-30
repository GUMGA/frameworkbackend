package io.gumga.amazon.ses;

import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.AmazonWebServiceResult;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendEmailResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author munif
 */
public class GumgaDefaultAWSAsyncListener implements GumgaAWSAsyncListener {

    private static final Logger logger = LoggerFactory.getLogger(GumgaDefaultAWSAsyncListener.class);

    public void progressChanged(String progress) {
        logger.info(progress.toString());
    }

    public void onError(Exception ex) {
        logger.error("On error ", ex);
    }

    public void onSuccess(AmazonWebServiceRequest rqst, AmazonWebServiceResult result) {
        logger.info(rqst.toString() + " ---> " + result.toString());
    }

}

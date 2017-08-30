package io.gumga.amazon.ses;

import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendEmailResult;

/**
 *
 * @author munif
 */
public interface GumgaAWSAsyncListener {

    void progressChanged(String progress);

    void onError(Exception ex);

    void onSuccess(SendEmailRequest rqst, SendEmailResult result);

}

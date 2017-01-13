/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gumga.presentation.exceptionhandler;

/**
 * Factory interface for creating {@link ErrorMessage} based on a specific
 * {@code Exception}.
 *
 * @param <T> The specific exception type.
 */
public interface ErrorMessageFactory<T extends Exception> {

    /**
     * Gets the exception class used for this factory.
     *
     * @return An exception class.
     */
    Class<T> getExceptionClass();

    /**
     * Creates an {@link ErrorMessage} from an exception.
     *
     * @param ex The exception to get data from.
     * @return An error message.
     */
    ErrorMessage getErrorMessage(T ex);

    /**
     * Gets the HTTP status response code that will be written to the response
     * when the message occurs.
     *
     * @return An HTTP status response.
     */
    int getResponseCode();
}

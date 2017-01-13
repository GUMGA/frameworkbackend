
package io.gumga.presentation;

import io.gumga.application.GumgaLoggerService;
import io.gumga.core.exception.*;
import io.gumga.presentation.validation.ErrorResource;
import io.gumga.presentation.validation.FieldErrorResource;
import io.gumga.validation.exception.InvalidEntityException;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.WebUtils;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    
    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Autowired
    private GumgaLoggerService gumgaLoggerService;

    @ExceptionHandler(InvalidEntityException.class)
    public ResponseEntity<Object> handleCustomException(InvalidEntityException ex, WebRequest request) {
        ErrorResource error = new ErrorResource("InvalidRequest", ex.getMessage());
        List<FieldError> fieldErrors = ex.getErrors().getFieldErrors();

        for (FieldError fieldError : fieldErrors) {
            FieldErrorResource fieldErrorResource = new FieldErrorResource();
            fieldErrorResource.setResource(fieldError.getObjectName());
            fieldErrorResource.setField(fieldError.getField());
            fieldErrorResource.setCode(fieldError.getCode());
            fieldErrorResource.setMessage(fieldError.getDefaultMessage());

            error.addFieldError(fieldErrorResource);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        gumgaLoggerService.logToFile(ex.toString(), 4);
        logger.info("InvalidEntity", ex);
        return handleExceptionInternal(ex, error, headers, HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public @ResponseBody
    ErrorResource unprocessableEntity(HttpServletRequest req, Exception ex) {
        gumgaLoggerService.logToFile(ex.toString(), 4);
        logger.warn("Unprocessable Entity", ex);
        return new ErrorResource("Unprocessable Entity", "Unprocessable Entity", ex.getMessage());
    }

//    @ExceptionHandler(IllegalArgumentException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public @ResponseBody
//    ErrorResource illegalArgument(HttpServletRequest req, Exception ex) {
//        gumgaLoggerService.logToFile(ex.toString(), 4);
//        logger.warn("IllegalArgument", ex);
//        return new ErrorResource("IllegalArgument", "Invalid request", ex.getMessage());
//    }
    
    
    @ExceptionHandler({ConstraintViolationException.class, DataIntegrityViolationException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public @ResponseBody
    ErrorResource constraintViolation(HttpServletRequest req, Exception ex) {
        gumgaLoggerService.logToFile(ex.toString(), 4);
        logger.warn("Error on operation", ex);
        ErrorResource errorResource = new ErrorResource("ConstraintViolation", "Error on operation", ex.getMessage());
        if (ex.getCause() instanceof ConstraintViolationException){
            Map<String,String> data=new HashMap<>();
            ConstraintViolationException cve=(ConstraintViolationException) ex.getCause();
            data.put("ConstraintName",cve.getConstraintName());
            data.put("Message",cve.getMessage());
            data.put("SQL",cve.getSQL());
            data.put("SQLState",cve.getSQLState());
            data.put("ErrorCode",""+cve.getErrorCode());
            errorResource.setData(data);
        }
        return errorResource;
    }

    /*
     * Tratamento das exceções padrão
     */
    @ExceptionHandler({EntityNotFoundException.class, NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody
    ErrorResource notFound(HttpServletRequest req, Exception ex) {
        gumgaLoggerService.logToFile(ex.toString(), 4);
        logger.warn("ResourceNotFound", ex);
        return new ErrorResource("ResourceNotFound", "Entity not found", ex.getMessage());
    }

    @ExceptionHandler(JpaObjectRetrievalFailureException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody
    ErrorResource notFound(HttpServletRequest req, JpaObjectRetrievalFailureException ex) {
        gumgaLoggerService.logToFile(ex.toString(), 4);
        logger.warn("ResourceNotFound", ex);
        return new ErrorResource("ResourceNotFound", "Entity not found", ex.getCause().getMessage());
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public @ResponseBody
    ErrorResource objectOptimisticLockingFailureException(HttpServletRequest req, Exception ex) {
        gumgaLoggerService.logToFile(ex.toString(), 4);
        logger.error("Object Already updated", ex);
        return new ErrorResource("Object Already updated", "Object Already updated", ex.getCause().getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody
    ErrorResource badRequest(HttpServletRequest req, Exception ex) {
        gumgaLoggerService.logToFile(ex.toString(), 4);
        logger.warn("BadRequest", ex);
        return new ErrorResource("BadRequest", "Invalid request", ex.getMessage());
    }

    @ExceptionHandler({ConflictException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public @ResponseBody
    ErrorResource conflict(HttpServletRequest req, Exception ex) {
        gumgaLoggerService.logToFile(ex.toString(), 4);
        logger.warn("Conflict", ex);
        return new ErrorResource("Conflict", "Error on operation", ex.getMessage());
    }

    @ExceptionHandler({ForbiddenException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public @ResponseBody
    ErrorResource forbidden(HttpServletRequest req, Exception ex) {
        gumgaLoggerService.logToFile(ex.toString(), 4);
        logger.warn("Forbidden", ex);
        return new ErrorResource("Forbidden", "Error on operation", ex.getMessage());
    }

    @ExceptionHandler({UnauthorizedException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public @ResponseBody
    ErrorResource unauthorized(HttpServletRequest req, Exception ex) {
        gumgaLoggerService.logToFile(ex.toString(), 4);
        logger.warn("Unauthorized", ex);
        return new ErrorResource("Unauthorized", "Error on operation", ex.getMessage());
    }

    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody
    ErrorResource dataAccessException(HttpServletRequest req, Exception ex) {
        gumgaLoggerService.logToFile(ex.toString(), 4);
        logger.error("Error on operation", ex);
        return new ErrorResource(ex.getClass().getSimpleName(), "Error on operation", ex.getCause().getMessage());
    }

    /**
     * Todo erro não tratado irá lançar um "INTERNAL SERVER ERROR", que
     * corresponde ao status 500
     *
     * @param req
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody
    ErrorResource exception(HttpServletRequest req, Exception ex) {
        gumgaLoggerService.logToFile(ex.toString(), 4);
        logger.error("Error on operation", ex);
        return new ErrorResource(ex.getClass().getSimpleName(), "Error on operation", ex.getMessage());
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
        }
        return new ResponseEntity<>(new ErrorResource("BAD Request", ex.getClass().getSimpleName(), ex.getMessage()), headers, status);
    }

}

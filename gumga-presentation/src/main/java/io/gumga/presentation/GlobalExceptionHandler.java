
package io.gumga.presentation;

import io.gumga.application.GumgaLoggerService;
import io.gumga.core.exception.*;
import io.gumga.presentation.exceptionhandler.GumgaRunTimeException;
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
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.WebUtils;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe global para manipulação de erros e excessões
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    
    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Autowired
    private GumgaLoggerService gumgaLoggerService;

    /**
     * Manipulador de Excessão
     * Método para o tratamento de excessão de entidade inválida
     * Um registro de log é executado sempre que o método é invocado
     * @param ex Objeto InvalidEntityException - Excessão de Entidade Inválida
     * @param request Objeto WebRequest - Requisição de origem
     * @return Objeto handleExceptionInternal - Manipulador de excessão interna contendo a resposta do servidor
     */
    @ExceptionHandler({InvalidEntityException.class})
    public ResponseEntity<Object> handleCustomException(InvalidEntityException ex, WebRequest request) {
        ErrorResource error = new ErrorResource("InvalidRequest", ex.getMessage());
        List<FieldError> fieldErrors = ex.getErrors().getFieldErrors();

        addFieldErro(error, fieldErrors);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        gumgaLoggerService.logToFile(ex.toString(), 4);
        logger.info("InvalidEntity", ex);
        return handleExceptionInternal(ex, error, headers, HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

    /**
     * Manipulador de Excessão
     * Método para o tratamento de excessão de entidade improcessável
     * {@code 422 Unprocessable Entity}.
     * @see <a href="http://tools.ietf.org/html/rfc4918#section-11.2">WebDAV</a>
     * O servidor entende o tipo do conteúdo da entidade, e a sintaxe da requisição está correta,
     * mas é incapaz de processar as instruções contidas
     * Um registro de log é executado sempre que o método é invocado
     * @param req Objeto HttpServletRequest contendo a requisição de origem
     * @param ex Objeto Exception
     * @return Objeto ErrorResource contendo a resposta do servidor
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public @ResponseBody
    ErrorResource unprocessableEntity(HttpServletRequest req, Exception ex) {
        gumgaLoggerService.logToFile(ex.toString(), 4);
        logger.warn("Unprocessable Entity", ex);
        return new ErrorResource("Unprocessable Entity", "Unprocessable Entity", ex.getMessage());
    }
    /**
     * Manipulador de Excessão
     * Método para o tratamento de excessão de entidade improcessável
     * {@code 409 Conflict}.
     * @see <a href="http://tools.ietf.org/html/rfc7231#section-6.5.8">HTTP/1.1: Semantics and Content, section 6.5.8</a>
     * A requisição não pôde ser concluída devido a um conflito com o estado
     * atual do recurso de destino.
     * Esta excessão é usada em situações em que o usuário poderá resolver
     * o conflito e reeviar a solicitação
     * @param req Objeto HttpServletRequest contendo a requisição de origem
     * @param ex Objeto Exception
     * @return Objeto ErrorResource contendo a resposta do servidor
     */
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
    /**
     * Manipulador de Excessão
     * Método para o tratamento de excessão de entidade não encontrada
     * {@code 404 Not Found}.
     * @see <a href="http://tools.ietf.org/html/rfc7231#section-6.5.4">HTTP/1.1: Semantics and Content, section 6.5.4</a>
     * O servidor não encontrou uma representação atual para o recurso requisitado
     * ou o mesmo não está disponível
     * @param req Objeto HttpServletRequest contendo a requisição de origem
     * @param ex Objeto Exception
     * @return Objeto ErrorResource contendo a resposta do servidor
     */
    @ExceptionHandler({EntityNotFoundException.class, NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody
    ErrorResource notFound(HttpServletRequest req, Exception ex) {
        gumgaLoggerService.logToFile(ex.toString(), 4);
        logger.warn("ResourceNotFound", ex);
        return new ErrorResource("ResourceNotFound", "Entity not found", ex.getMessage());
    }

    /**
     * Manipulador de Excessão
     * Método para o tratamento de excessão de entidade não encontrada
     * {@code 404 Not Found}.
     * @see <a href="http://tools.ietf.org/html/rfc7231#section-6.5.4">HTTP/1.1: Semantics and Content, section 6.5.4</a>
     * O servidor não encontrou uma representação atual para o recurso requisitado
     * ou o mesmo não está disponível
     * @param req Objeto HttpServletRequest contendo a requisição de origem
     * @param ex Objeto JpaObjectRetrievalFailureException
     * @return Objeto ErrorResource contendo a resposta do servidor
     */
    @ExceptionHandler(JpaObjectRetrievalFailureException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody
    ErrorResource notFound(HttpServletRequest req, JpaObjectRetrievalFailureException ex) {
        gumgaLoggerService.logToFile(ex.toString(), 4);
        logger.warn("ResourceNotFound", ex);
        return new ErrorResource("ResourceNotFound", "Entity not found", ex.getCause().getMessage());
    }

    /**
     *
     * @param req
     * @param ex
     * @return
     */
    /**
     * Manipulador de Excessão
     * Método para o tratamento de excessão de entidade já modificada
     * {@code 409 Conflict}.
     * @see <a href="http://tools.ietf.org/html/rfc7231#section-6.5.8">HTTP/1.1: Semantics and Content, section 6.5.8</a>
     * A requisição não pôde ser concluída devido a um conflito com o estado
     * atual do recurso de destino.
     * Esta excessão é usada em situações em que o usuário poderá resolver
     * o conflito e reeviar a solicitação
     * @param req Objeto HttpServletRequest contendo a requisição de origem
     * @param ex Objeto Exception
     * @return Objeto ErrorResource contendo a resposta do servidor
     */
    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public @ResponseBody
    ErrorResource objectOptimisticLockingFailureException(HttpServletRequest req, Exception ex) {
        gumgaLoggerService.logToFile(ex.toString(), 4);
        logger.error("Object Already updated", ex);
        return new ErrorResource("Object Already updated", "Object Already updated", ex.getCause().getMessage());
    }

    /**
     * Manipulador de Excessão
     * Método para o tratamento de excessão de entidade já modificada
     * {@code 400 Bad Request}.
     * @see <a href="http://tools.ietf.org/html/rfc7231#section-6.5.1">HTTP/1.1: Semantics and Content, section 6.5.1</a>
     * O servidor não consegue ou rejeitou o processamento da requisição devido
     * a algum erro perceptivel do cliente
     * @param req bjeto HttpServletRequest contendo a requisição de origem
     * @param ex Objeto Exception
     * @return Objeto ErrorResource contendo a resposta do servidor
     */
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody
    ErrorResource badRequest(HttpServletRequest req, Exception ex) {
        gumgaLoggerService.logToFile(ex.toString(), 4);
        logger.warn("BadRequest", ex);
        return new ErrorResource("BadRequest", "Invalid request", ex.getMessage());
    }
    /**
     * Manipulador de Excessão
     * Método para o tratamento de excessão de entidade em conflito
     * {@code 409 Conflict}.
     * @see <a href="http://tools.ietf.org/html/rfc7231#section-6.5.8">HTTP/1.1: Semantics and Content, section 6.5.8</a>
     * A requisição não pôde ser concluída devido a um conflito com o estado
     * atual do recurso de destino.
     * Esta excessão é usada em situações em que o usuário poderá resolver
     * o conflito e reeviar a solicitação
     * @param req Objeto HttpServletRequest contendo a requisição de origem
     * @param ex Objeto Exception
     * @return Objeto ErrorResource contendo a resposta do servidor
     */
    @ExceptionHandler({ConflictException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public @ResponseBody
    ErrorResource conflict(HttpServletRequest req, Exception ex) {
        gumgaLoggerService.logToFile(ex.toString(), 4);
        logger.warn("Conflict", ex);
        return new ErrorResource("Conflict", "Error on operation", ex.getMessage());
    }

    /**
     * Manipulador de Excessão
     * Método para o tratamento de excessão de entidade sem permissão
     * O servidor entendeu a requisição, mas recusa seu processamento
     * {@code 403 Forbidden}.
     * @see <a href="http://tools.ietf.org/html/rfc7231#section-6.5.3">HTTP/1.1: Semantics and Content, section 6.5.3</a>
     * @param req Objeto HttpServletRequest contendo a requisição de origem
     * @param ex Objeto Exception
     * @return Objeto ErrorResource contendo a resposta do servidor
     */
    @ExceptionHandler({ForbiddenException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public @ResponseBody
    ErrorResource forbidden(HttpServletRequest req, Exception ex) {
        gumgaLoggerService.logToFile(ex.toString(), 4);
        logger.warn("Forbidden", ex);
        return new ErrorResource("Forbidden", "Error on operation", ex.getMessage());
    }

    /**
     * Manipulador de Excessão
     * Método para o tratamento de excessão de entidade não autorizada
     * {@code 401 Unauthorized}.
     * @see <a href="http://tools.ietf.org/html/rfc7235#section-3.1">HTTP/1.1: Authentication, section 3.1</a>
     * A requisição não foi aplicada porque não foi encontrada uma credencial válida
     * o recurso de destino
     * @param req Objeto HttpServletRequest contendo a requisição de origem
     * @param ex Objeto Exception
     * @return Objeto ErrorResource contendo a resposta do servidor
     */

    @ExceptionHandler({UnauthorizedException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public @ResponseBody
    ErrorResource unauthorized(HttpServletRequest req, Exception ex) {
        gumgaLoggerService.logToFile(ex.toString(), 4);
        logger.warn("Unauthorized", ex);
        return new ErrorResource("Unauthorized", "Error on operation", ex.getMessage());
    }

    /**
     * Manipulador de Excessão
     * Erro no acesso a um dado
     * O servidor encontrou uma condição inesperada que impediu o cumprimento do pedido
     * {@code 500 Internal Server Error}.
     * @see <a href="http://tools.ietf.org/html/rfc7231#section-6.6.1">HTTP/1.1: Semantics and Content, section 6.6.1</a>
     * @param req Objeto HttpServletRequest contendo a requisição de origem
     * @param ex Objeto Exception
     * @return Objeto ErrorResource contendo a resposta do servidor
     */
    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody
    ErrorResource dataAccessException(HttpServletRequest req, Exception ex) {
        gumgaLoggerService.logToFile(ex.toString(), 4);
        logger.error("Error on operation", ex);
        return new ErrorResource(ex.getClass().getSimpleName(), "Error on operation", ex.getCause().getMessage());
    }

    /**
     * Todos erros não tratados irão lançar este erro
     *O servidor encontrou uma condição inesperada que impediu o cumprimento do pedido
     * {@code 500 Internal Server Error}.
     * @see <a href="http://tools.ietf.org/html/rfc7231#section-6.6.1">HTTP/1.1: Semantics and Content, section 6.6.1</a>
     * @param req Objeto HttpServletRequest contendo a requisição de origem
     * @param ex Objeto Exception
     * @return Objeto ErrorResource contendo a resposta do servidor
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody ErrorResource exception(HttpServletRequest req, Exception ex) {
        gumgaLoggerService.logToFile(ex.toString(), 4);
        logger.error("Error on operation", ex);
        return new ErrorResource(ex.getClass().getSimpleName(), "Error on operation", ex.getMessage());
    }

    /**
     * Manipulador de Excessão
     * Erro em tempo de execução
     * @param response Objeto HttpServletResponse contendo a resposta do servidor
     * @param ex Objeto GumgaRunTimeException
     * @return Objeto ErrorResource contendo a resposta do servidor
     */
    @ExceptionHandler(GumgaRunTimeException.class)
    @ResponseBody
    public ErrorResource gumgaRunTimeException(HttpServletResponse response, GumgaRunTimeException ex) {
        gumgaLoggerService.logToFile(ex.toString(), 4);
        logger.error("Error on operation", ex);
        response.setStatus(ex.getHttpStatus().value());
        ErrorResource errorResource = new ErrorResource(ex.getClass().getSimpleName(), "Error on operation", ex.getMessage());
        errorResource.setFieldErrors(ex.getFieldErrors());
        return errorResource;
    }

    /**
     * Manipulador de Excessão
     * Gera erro interno baseado em objetos de uma requisiçao HTTP
     * @param ex Objeto Exception
     * @param body Objeto contendo o corpo da requisição
     * @param headers Objeto HttpHeaders contendo o cabeçalho da requisição
     * @param status Objeto HttpStatus
     * @param request Objeto WebRequest
     * @return Objeto ResponseEntity
     */
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
        }
        if(body instanceof ErrorResource) {
            return new ResponseEntity<>(body, headers, status);
        }
        return new ResponseEntity<>(new ErrorResource("BAD Request", ex.getClass().getSimpleName(), ex.getMessage()), headers, status);
    }

    /**
     * Manipulador de Excessão
     * Gera erro para argumento de método inválido
     * @param ex Objeto MethodArgumentNotValidException
     * @param headers Objeto HttpHeaders contendo o cabeçalho da requisição HTTP
     * @param status Objeto HttpStatus
     * @param request Objeto WebRequest
     * @return Objeto handleExceptionInternal
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ErrorResource error = new ErrorResource("InvalidRequest", ex.getMessage());
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        addFieldErro(error, fieldErrors);

        HttpHeaders newheaders = new HttpHeaders();
        newheaders.setContentType(MediaType.APPLICATION_JSON);
        gumgaLoggerService.logToFile(ex.toString(), 4);
        logger.info("InvalidEntity", ex);
        return handleExceptionInternal(ex, error, newheaders, HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

    /**
     * Manipulador de Excessão
     * Manipula um erro de campo
     * @param error Objeto ErrorResource contendo o campo
     * @param fieldErrors Lista de erros do campo
     */
    private void addFieldErro(ErrorResource error, List<FieldError> fieldErrors) {
        for (FieldError fieldError : fieldErrors) {
            FieldErrorResource fieldErrorResource = new FieldErrorResource();
            fieldErrorResource.setResource(fieldError.getObjectName());
            fieldErrorResource.setField(fieldError.getField());
            fieldErrorResource.setCode(fieldError.getCode());
            fieldErrorResource.setMessage(fieldError.getDefaultMessage());

            error.addFieldError(fieldErrorResource);
        }
    }

}

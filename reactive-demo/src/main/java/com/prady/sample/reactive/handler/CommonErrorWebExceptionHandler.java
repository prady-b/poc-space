/**
 *
 */

package com.prady.sample.reactive.handler;

import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.prady.sample.reactive.exception.ItemAlreadyExistsException;
import com.prady.sample.reactive.exception.ItemNotFoundException;
import com.prady.sample.reactive.response.ErrorResponse;
import com.prady.sample.reactive.response.ErrorResponse.ErrorCode;
import com.prady.sample.reactive.response.ValidationErrorResponse;

import reactor.core.publisher.Mono;

/**
 * @author Prady
 */
@Component
@Order(-2)
public class CommonErrorWebExceptionHandler extends AbstractErrorWebExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(CommonErrorWebExceptionHandler.class);

    public CommonErrorWebExceptionHandler(ErrorAttributes errorAttributes, ApplicationContext applicationContext,
            ServerCodecConfigurer serverCodecConfigurer) {
        super(errorAttributes, new ResourceProperties(), applicationContext);
        super.setMessageWriters(serverCodecConfigurer.getWriters());
        super.setMessageReaders(serverCodecConfigurer.getReaders());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::errorResponse);
    }

    private Mono<ServerResponse> errorResponse(final ServerRequest request) {
        Throwable exception = getError(request);
        log.error("Error in Common handler ", ex);
        if (exception instanceof ItemNotFoundException) {
            return ServerResponse.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON)
                    .syncBody(new ErrorResponse(ErrorCode.NOT_FOUND, ex.getMessage()));
        } else if (exception instanceof ItemAlreadyExistsException) {
            return ServerResponse.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
                    .syncBody(new ErrorResponse(ErrorCode.ALREADY_EXISTS, ex.getMessage()));
        } else if (exception instanceof ConstraintViolationException) {
            return ServerResponse.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
                    .syncBody(new ValidationErrorResponse(
                            ValidationErrorResponse.getValidationMessages(((ConstraintViolationException) ex).getConstraintViolations())));
        }
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
                .syncBody(new ErrorResponse(ErrorCode.GENERAL, ex.getMessage()));
    }
}

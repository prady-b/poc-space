/**
 *
 */
package com.prady.sample.tx.handler;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;

import com.prady.sample.tx.exception.InsufficientResourcesException;
import com.prady.sample.tx.exception.ItemAlreadyExistsException;
import com.prady.sample.tx.exception.ItemNotFoundException;
import com.prady.sample.tx.response.ErrorResponse;
import com.prady.sample.tx.response.ErrorResponse.ErrorCode;
import com.prady.sample.tx.response.ValidationErrorResponse;

/**
 * @author Prady
 *
 */
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class CommonErrorController extends AbstractErrorController {

    private static final Logger log = LoggerFactory.getLogger(CommonErrorController.class);

    private final ErrorProperties errorProperties;
    private final ErrorAttributes errorAttributes;

    public CommonErrorController() {
        this(new DefaultErrorAttributes(), new ErrorProperties());
    }

    /**
     * @param errorAttributes the error attributes
     * @param errorProperties configuration properties
     */
    public CommonErrorController(ErrorAttributes errorAttributes, ErrorProperties errorProperties) {
        this(errorAttributes, errorProperties, Collections.emptyList());
    }

    /**
     *
     * @param errorAttributes the error attributes
     * @param errorProperties configuration properties
     * @param errorViewResolvers error view resolvers
     */
    public CommonErrorController(ErrorAttributes errorAttributes, ErrorProperties errorProperties,
            List<ErrorViewResolver> errorViewResolvers) {
        super(errorAttributes, errorViewResolvers);
        Assert.notNull(errorProperties, "ErrorProperties must not be null");
        this.errorProperties = errorProperties;
        this.errorAttributes = errorAttributes;
    }

    @Override
    public String getErrorPath() {
        return errorProperties.getPath();
    }

    /**
     * @return
     */
    @GetMapping
    @ResponseBody
    public ResponseEntity<ErrorResponse> errorGet(HttpServletRequest request) {
        return getErrorResponseResponseEntity(request);
    }

    /**
     * @return
     */
    @PostMapping
    @ResponseBody
    public ResponseEntity<ErrorResponse> errorPost(HttpServletRequest request) {
        return getErrorResponseResponseEntity(request);
    }

    /**
     * @return
     */
    @PutMapping
    @ResponseBody
    public ResponseEntity<ErrorResponse> errorPut(HttpServletRequest request) {
        return getErrorResponseResponseEntity(request);
    }

    /**
     * @return
     */
    @DeleteMapping
    @ResponseBody
    public ResponseEntity<ErrorResponse> errorDelete(HttpServletRequest request) {
        return getErrorResponseResponseEntity(request);
    }

    /**
     * @param request
     * @return
     */
    private ResponseEntity<ErrorResponse> getErrorResponseResponseEntity(HttpServletRequest request) {
        Throwable ex = errorAttributes.getError(new ServletWebRequest(request));
        log.error("Error in Common handler {} ", ex.getMessage());

        if (ex instanceof ItemNotFoundException) {
            return new ResponseEntity<>(new ErrorResponse(ErrorCode.NOT_FOUND, ex.getMessage()), HttpStatus.NOT_FOUND);
        } else if (ex instanceof ItemAlreadyExistsException) {
            return new ResponseEntity<>(new ErrorResponse(ErrorCode.ALREADY_EXISTS, ex.getMessage()), HttpStatus.BAD_REQUEST);
        } else if (ex instanceof InsufficientResourcesException) {
            return new ResponseEntity<>(new ErrorResponse(ErrorCode.INSUFFICIENT_RESOURCES, ex.getMessage()), HttpStatus.BAD_REQUEST);
        } else if (ex instanceof ConstraintViolationException) {
            return new ResponseEntity<>(new ValidationErrorResponse(
                    ValidationErrorResponse.getValidationMessages(((ConstraintViolationException) ex).getConstraintViolations())),
                    HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ErrorResponse(ErrorCode.GENERAL, ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

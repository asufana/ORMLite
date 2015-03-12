package com.github.asufana.orm.exceptions;

import lombok.*;

import org.slf4j.*;

@Getter
public class ORMLiteException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(ORMLiteException.class);
    
    private final Exception exception;
    private final String message;
    private final String sql;
    
    public ORMLiteException(final String message) {
        this(null, message, null);
    }
    
    public ORMLiteException(final Exception exception) {
        this(exception, String.format("%s: %s",
                                      exception.getClass().getSimpleName(),
                                      exception.getMessage()), null);
    }
    
    public ORMLiteException(final Exception exception, final String sql) {
        this(exception, String.format("%s: %s",
                                      exception.getClass().getSimpleName(),
                                      exception.getMessage()), sql);
    }
    
    public ORMLiteException(final Exception exception,
            final String message,
            final String sql) {
        super();
        this.exception = exception;
        this.message = message;
        this.sql = sql;
        
        logger.error("ORMLiteException: {}", message);
    }
}

package pl.patrykbober.soa.exception;

import lombok.NoArgsConstructor;

import javax.ejb.ApplicationException;

@NoArgsConstructor
@ApplicationException
public class RestException extends RuntimeException {

    public RestException(String message) {
        super(message);
    }
}

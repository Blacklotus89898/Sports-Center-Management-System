package ca.mcgill.ecse321.scs.exception;

import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;

public class SCSException extends RuntimeException {
    @NonNull
    private HttpStatus status;

    public SCSException(@NonNull HttpStatus status, @NonNull String message) {
        super(message);
        this.status = status;
    }

    @NonNull
    public HttpStatus getStatus() {
        return status;
    }
}

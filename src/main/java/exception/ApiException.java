package exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
	private static final long serialVersionUID = 1647315650756981670L;
	
	private final String errorCode;
    private final Object errorData;

    public ApiException(String errorCode, Object errorData) {
        super(errorCode); 
        this.errorCode = errorCode;
        this.errorData = errorData;
    }
}
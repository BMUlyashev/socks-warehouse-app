package pro.sky.sockswarehouse.exception;

public class SocksException extends RuntimeException {
    private final String message;

    public SocksException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

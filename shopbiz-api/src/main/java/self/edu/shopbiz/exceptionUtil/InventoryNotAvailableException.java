package self.edu.shopbiz.exceptionUtil;

public class InventoryNotAvailableException extends RuntimeException {

    public InventoryNotAvailableException() {
        super();
    }

    public InventoryNotAvailableException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public InventoryNotAvailableException(final Long id) {
        super(String.format("Inventory %s not sufficient for order", id));
    }

    public InventoryNotAvailableException(final String message) {
        super(message);
    }
}

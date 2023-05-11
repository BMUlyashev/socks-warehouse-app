package pro.sky.sockswarehouse.model;

public enum Operations {
    LESS_THEN("lessThan"),
    EQUAL("equal"),
    MORE_THEN("moreThan"),
    BAD_OPERATIONS("badOperations");

    private final String nameOperation;

    Operations(String stringOperation) {
        this.nameOperation = stringOperation;
    }


    public static Operations getNameOperation(String operator) {
        for (Operations operations : values()) {
            if (operations.nameOperation.equals(operator)) {
                return operations;
            }
        }
        return BAD_OPERATIONS;
    }
}

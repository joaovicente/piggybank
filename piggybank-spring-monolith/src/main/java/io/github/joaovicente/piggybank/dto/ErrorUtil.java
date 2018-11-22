package io.github.joaovicente.piggybank.dto;

public class ErrorUtil {
    public enum ErrorCode {
        RESOURCE_NOT_FOUND("RESOURCE_NOT_FOUND"),
        INVALID_INPUT("INVALID_INPUT");
        private final String string;

        ErrorCode(String string) {
            this.string = string;
        }
        public String value()   {
            return this.string;
        }
    }
    public enum ErrorMessage {
        NOT_FOUND("not found"),
        IS_INVALID("is invalid");
        private final String message;

        ErrorMessage(String message) {
            this.message = message;
        }
        public String prefix(String prefixString)  {
           return prefixString + " " + this.message;
        }
        public String value()  {
            return this.message;
        }
    }
}

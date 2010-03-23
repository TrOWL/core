package eu.trowl.query.sparql.endpoint;

/**
 *
 * @author ed
 */
public class InvalidParameterException extends Exception {

    /**
     *
     */
    public InvalidParameterException() {
		// TODO Auto-generated constructor stub
	}

        /**
         *
         * @param message
         */
        public InvalidParameterException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

        /**
         *
         * @param cause
         */
        public InvalidParameterException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

        /**
         *
         * @param message
         * @param cause
         */
        public InvalidParameterException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}

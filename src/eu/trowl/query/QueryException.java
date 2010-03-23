package eu.trowl.query;

/**
 *
 * @author ed
 */
public class QueryException extends Exception {

    /**
     *
     */
    public QueryException() {
		// TODO Auto-generated constructor stub
	}

    /**
     *
     * @param message
     */
    public QueryException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

        /**
         *
         * @param cause
         */
        public QueryException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

        /**
         *
         * @param message
         * @param cause
         */
        public QueryException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}

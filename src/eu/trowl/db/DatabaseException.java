package eu.trowl.db;

/**
 *
 * @author ed
 */
public class DatabaseException extends Exception {

    /**
     *
     */
    public DatabaseException() {
		// TODO Auto-generated constructor stub
	}

        /**
         *
         * @param message
         */
        public DatabaseException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

        /**
         *
         * @param cause
         */
        public DatabaseException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

        /**
         *
         * @param message
         * @param cause
         */
        public DatabaseException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}

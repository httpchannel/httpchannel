/**
 * 
 */
package com.captchatrader.exception;

/**
 * @author <a href="http://www.rogiel.com">Rogiel</a>
 * 
 */
public class UserNotValidatedException extends CaptchaTraderException {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public UserNotValidatedException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public UserNotValidatedException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public UserNotValidatedException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public UserNotValidatedException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public UserNotValidatedException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
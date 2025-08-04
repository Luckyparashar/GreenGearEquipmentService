package com.greengear.equipment.Exception;

@SuppressWarnings("serial")
public class InvalidInputException extends RuntimeException {
	public InvalidInputException(String mesg) {
		super(mesg);
	}
}

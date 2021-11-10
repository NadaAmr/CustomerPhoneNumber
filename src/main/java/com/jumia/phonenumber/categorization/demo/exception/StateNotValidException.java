package com.jumia.phonenumber.categorization.demo.exception;

public class StateNotValidException extends RuntimeException
{

	 public StateNotValidException(String  state) {

	        super(String.format("state %s not valid", state));
	    }
}

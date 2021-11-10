package com.jumia.phonenumber.categorization.demo.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jumia.phonenumber.categorization.demo.enums.PhoneState;
import com.jumia.phonenumber.categorization.demo.exception.StateNotValidException;
import com.jumia.phonenumber.categorization.demo.model.Country;

import io.micrometer.core.instrument.util.StringUtils;

public class PhoneValidationUtils {

	private PhoneValidationUtils() {
		throw new IllegalStateException("Utility class");
	}

	public static boolean isValidPhone(Country phoneCountry, String customerPhoneValue) {
		boolean validPhone = false;

		Pattern countryPattern = Pattern.compile(phoneCountry.getCountryRegex());
		Matcher countryMatcher = countryPattern.matcher(customerPhoneValue);

		if (countryMatcher.find()) {
			validPhone = true;
		}
		return validPhone;
	}

	public static boolean isValidState(String phoneState, boolean validPhone) {

		boolean validState = true;

		if (phoneState.equalsIgnoreCase(PhoneState.VALID.name()) && !validPhone
				|| phoneState.equalsIgnoreCase(PhoneState.NOT_VALID.name()) && validPhone) {
			validState = false;
		}

		return validState;
	}
	
	public static void isValidStateValue(String phoneState) {
		
		if(!StringUtils.isBlank(phoneState) && !(phoneState.equalsIgnoreCase(PhoneState.VALID.name()) 
				|| phoneState.equalsIgnoreCase(PhoneState.NOT_VALID.name()))) {
			throw new StateNotValidException(phoneState);
		}
	}

}

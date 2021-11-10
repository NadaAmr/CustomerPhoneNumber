package com.jumia.phonenumber.categorization.demo.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.jumia.phonenumber.categorization.demo.enums.PhoneState;
import com.jumia.phonenumber.categorization.demo.exception.StateNotValidException;
import com.jumia.phonenumber.categorization.demo.helper.CustomerPhoneHelper;
import com.jumia.phonenumber.categorization.demo.model.Country;

//@ExtendWith(Spring)
public class PhoneValidationUtilsTest {

	private static List<Country> countriesList;
	private static Map<String, Country> countryRegexMap = new HashMap<>();

	@BeforeAll
	private static void setupData() {

		countriesList = CustomerPhoneHelper.setupCountryData();
		countryRegexMap = countriesList.stream()
				.collect(Collectors.toMap(Country::getCountryCode, Function.identity()));

	}

	@ParameterizedTest(name = "Run {index} : name {0}")
	@MethodSource("isValidPhoneTestData")
	void isValidPhoneTest(String testcase, String phoneNumber, String countryCode, boolean expectedResult) {

		// given
		Country phone = countryRegexMap.get(countryCode);
		// when
		boolean valid = PhoneValidationUtils.isValidPhone(phone, phoneNumber);

		// then
		assertThat(valid).isEqualTo(expectedResult);

	}

	static Stream<Arguments> isValidPhoneTestData() {
		return Stream.of(Arguments.of("When phone number is valid ", "(212) 633963130", "212", true),
				Arguments.of("When phone number is not valid ", "(212) 6007989253", "212", false));
	}

	@ParameterizedTest(name = "Run {index} : name {0}")
	@MethodSource("isValidPhoneStateTestData")
	void isValidPhoneStateTest(String testcase, boolean validPhone, String phoneState, boolean expectedResult) {

		// when
		boolean valid = PhoneValidationUtils.isValidState(phoneState, validPhone);

		// then
		assertThat(valid).isEqualTo(expectedResult);

	}

	static Stream<Arguments> isValidPhoneStateTestData() {
		return Stream.of(
				Arguments.of("When phone number is valid and filter state is valid ", true, PhoneState.VALID.name(),
						true),
				Arguments.of("When phone number is  not valid and filter State is valid ", false,
						PhoneState.VALID.name(), false),
				Arguments.of("When phone number is valid and filter state is not valid ", true,
						PhoneState.NOT_VALID.name(), false),
				Arguments.of("When phone number is  not valid and filter State is not valid ", false,
						PhoneState.NOT_VALID.name(), true),
				Arguments.of("When phone number is valid and filter state is null ", true, PhoneState.BOTH.name(),
						true),
				Arguments.of("When phone number is  not valid and filter State is null ", false, PhoneState.BOTH.name(),
						true));
	}

	@Test
	void isValidStateValueTest() {
		
		Assertions.assertThrows(StateNotValidException.class, () -> {
			PhoneValidationUtils.isValidStateValue("Test");
		});
	}
}

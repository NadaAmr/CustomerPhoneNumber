package com.jumia.phonenumber.categorization.demo.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jumia.phonenumber.categorization.demo.dtos.CustomerPhoneDto;
import com.jumia.phonenumber.categorization.demo.dtos.CustomerPhonePagesDto;
import com.jumia.phonenumber.categorization.demo.enums.PhoneState;
import com.jumia.phonenumber.categorization.demo.model.Country;
import com.jumia.phonenumber.categorization.demo.model.Customer;
import com.jumia.phonenumber.categorization.demo.repositories.CountryRepository;
import com.jumia.phonenumber.categorization.demo.repositories.CustomerRepository;
import com.jumia.phonenumber.categorization.demo.service.CustomerPhoneNumberService;
import com.jumia.phonenumber.categorization.demo.utils.PhoneValidationUtils;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CustomerPhoneNumberServiceImpl implements CustomerPhoneNumberService {

	private final CustomerRepository customerRepository;

	private final CountryRepository countryRepository;

	private static final String COUNTRY_CODE_PATTERN = "\\(\\d+\\)";

	@Override
	public CustomerPhonePagesDto retrievePhoneNumbers(String countryName, String phoneState) {

		Map<String, Country> countryRegexMap = new HashMap<>();

		if (StringUtils.isBlank(phoneState)) {
			phoneState = PhoneState.BOTH.name();
		}

		if (!StringUtils.isBlank(countryName)) {

			Optional<Country> countryOptional = countryRepository.findByCountryNameIgnoreCase(countryName);

			if (countryOptional.isPresent()) {
				Country country = countryOptional.get();
				countryRegexMap.put(country.getCountryCode(), country);
				return retrieveCustomerPhoneSpecificCountry(country.getCountryCode(), countryRegexMap, phoneState);

			}

			return CustomerPhonePagesDto.builder().customerPhoneList(new ArrayList<>()).totalNumberOfElements(0)
					.build();

		}

		List<Country> countries = countryRepository.findAll();

		countryRegexMap = countries.stream().collect(Collectors.toMap(Country::getCountryCode, Function.identity()));

		return retrieveCustomerPhoneForAllCustomers(countryRegexMap, phoneState);

	}

	private List<CustomerPhoneDto> filterCustomerPhoneNumbers(List<Customer> customerPhoneNumbers,
			Map<String, Country> countryRegexMap, String phoneState) {
		List<CustomerPhoneDto> customerPhoneDtos = new ArrayList<>();

		for (Customer customer : customerPhoneNumbers) {

			String customerPhoneValue = customer.getPhone();
			String countryCode = getCountryCodeFromPhone(customerPhoneValue);

			if (!StringUtils.isBlank(countryCode)
					&& Optional.ofNullable(countryRegexMap.get(countryCode)).isPresent()) {

				Country phoneCountry = countryRegexMap.get(countryCode);

				boolean validPhone = PhoneValidationUtils.isValidPhone(phoneCountry, customerPhoneValue);

				CustomerPhoneDto customerPhoneDto = CustomerPhoneDto.builder().phoneNumber(customerPhoneValue)
						.countryCode(countryCode).country(phoneCountry.getCountryName())
						.state(validPhone ? PhoneState.VALID : PhoneState.NOT_VALID).build();

				boolean validState = PhoneValidationUtils.isValidState(phoneState, validPhone);

				if (validState) {
					customerPhoneDtos.add(customerPhoneDto);
				}
			}
		}
		return customerPhoneDtos;

	}

	private String getCountryCodeFromPhone(String customerPhoneValue) {

		String countryCode = null;
		Pattern pattern = Pattern.compile(COUNTRY_CODE_PATTERN);
		Matcher matcher = pattern.matcher(customerPhoneValue);

		if (matcher.find()) {
			countryCode = matcher.group(0).replaceAll("[\\(-\\)]", "");
		}
		return countryCode;
	}

	private CustomerPhonePagesDto retrieveCustomerPhoneSpecificCountry(String countryCode,
			Map<String, Country> countryRegexMap, String phoneState) {

		List<Customer> customerPhoneNumbersList = customerRepository.findByPhoneStartsWith("(" + countryCode + ")");

		if (!customerPhoneNumbersList.isEmpty()) {
			List<CustomerPhoneDto> customerPhone = filterCustomerPhoneNumbers(customerPhoneNumbersList, countryRegexMap,
					phoneState);

			return CustomerPhonePagesDto.builder().customerPhoneList(customerPhone)
					.totalNumberOfElements(customerPhone.size()).build();
		}
		return CustomerPhonePagesDto.builder().customerPhoneList(new ArrayList<>()).totalNumberOfElements(0).build();

	}

	private CustomerPhonePagesDto retrieveCustomerPhoneForAllCustomers(Map<String, Country> countryRegexMap,
			String phoneState) {

		List<Customer> customerPhoneNumbersList = customerRepository.findAll();

		if (!customerPhoneNumbersList.isEmpty()) {
			List<CustomerPhoneDto> customerPhone = filterCustomerPhoneNumbers(customerPhoneNumbersList, countryRegexMap,
					phoneState);

			return CustomerPhonePagesDto.builder().customerPhoneList(customerPhone)
					.totalNumberOfElements(customerPhone.size()).build();
		}

		return CustomerPhonePagesDto.builder().customerPhoneList(new ArrayList<>()).totalNumberOfElements(0).build();

	}

}

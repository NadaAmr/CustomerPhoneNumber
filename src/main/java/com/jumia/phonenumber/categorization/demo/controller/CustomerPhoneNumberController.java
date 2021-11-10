package com.jumia.phonenumber.categorization.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jumia.phonenumber.categorization.demo.dtos.CustomerPhonePagesDto;
import com.jumia.phonenumber.categorization.demo.service.CustomerPhoneNumberService;
import com.jumia.phonenumber.categorization.demo.utils.PhoneValidationUtils;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class CustomerPhoneNumberController {

	private final CustomerPhoneNumberService customerPhoneService;

	@GetMapping(path = "customer/phone")
	public ResponseEntity<CustomerPhonePagesDto> retrievePhoneNumbers(
			@RequestParam(required = false, name = "country") String country,
			@RequestParam(required = false, name = "state") String state) {

		PhoneValidationUtils.isValidStateValue(state);

		CustomerPhonePagesDto validPhoneNumbers = customerPhoneService.retrievePhoneNumbers(country, state);
		return ResponseEntity.ok(validPhoneNumbers);

	}

}

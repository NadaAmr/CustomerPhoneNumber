package com.jumia.phonenumber.categorization.demo.service;

import com.jumia.phonenumber.categorization.demo.dtos.CustomerPhonePagesDto;

public interface CustomerPhoneNumberService {

	public CustomerPhonePagesDto retrievePhoneNumbers(String countryName, String phoneState);
}

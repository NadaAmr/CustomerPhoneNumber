package com.jumia.phonenumber.categorization.demo.dtos;

import com.jumia.phonenumber.categorization.demo.enums.PhoneState;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder=true)
public class CustomerPhoneDto {
	
	private String phoneNumber;
	private String country;
	private PhoneState state;
	private String countryCode;
	
	

}

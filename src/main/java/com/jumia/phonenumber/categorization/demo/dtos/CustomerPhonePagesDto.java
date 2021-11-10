package com.jumia.phonenumber.categorization.demo.dtos;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data 
@Builder
public class CustomerPhonePagesDto {

	private List<CustomerPhoneDto> customerPhoneList;
	private Integer totalNumberOfPages;
	private Integer totalNumberOfElements;
}

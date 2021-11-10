package com.jumia.phonenumber.categorization.demo.helper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.jumia.phonenumber.categorization.demo.model.Country;
import com.jumia.phonenumber.categorization.demo.model.Customer;

public abstract class CustomerPhoneHelper {

	public static List<Country> setupCountryData() {

		Country morroco = Country.builder().id(1l).countryName("Morocco").countryCode("212")
				.countryRegex("\\(212\\)\\ ?[5-9]\\d{8}$").build();

		Country cameroon = Country.builder().id(2l).countryName("Cameroon").countryCode("237")
				.countryRegex("\\(237\\)\\ ?[2368]\\d{7,8}$").build();

		Country ethiopia = Country.builder().id(3l).countryName("Ethiopia").countryCode("251")
				.countryRegex("\\(251\\)\\ ?[1-59]\\d{8}$").build();
		Country uganda = Country.builder().id(4l).countryName("Uganda").countryCode("256").countryRegex("\\(256\\)\\ ?\\d{9}")
				.build();
		Country mozambique = Country.builder().id(5l).countryName("Mozambique").countryCode("258")
				.countryRegex("\\(258\\)\\ ?[28]\\d{7,8}$").build();

		return (List<Country>) Arrays.asList(morroco, cameroon, ethiopia, uganda, mozambique);
	}

	public static List<Customer> setupCustomerData() {

		Customer customer1 = Customer.builder().id(1l).phone("(212) 6007989253").build();
		Customer customer2 = Customer.builder().id(2l).phone("(212) 633963130").build();
		Customer customer3 = Customer.builder().id(3l).phone("(251) 911203317").build();
		Customer customer4 = Customer.builder().id(4l).phone("(237) 673122155").build();
		Customer customer5 = Customer.builder().id(5l).phone("(237) 6A0311634").build();
		Customer customer6 = Customer.builder().id(6l).phone("(258) 84330678235").build();
		Customer customer7 = Customer.builder().id(7l).phone("(256) 775069443").build();
		Customer customer8 = Customer.builder().id(8l).phone("(256) 7503O6263").build();
		Customer customer9 = Customer.builder().id(9l).phone("(258) 823747618").build();
		Customer customer10 = Customer.builder().id(10l).phone("(251) 911203317660").build();
		Customer customer11 = Customer.builder().id(11l).phone("(212) 6007989254").build();

		return Arrays.asList(customer1, customer2, customer3, customer4, customer5, customer6, customer7, customer8,
				customer9, customer10, customer11);
	}

	public static List<Customer> getCountryCustomers(List<Customer> allCustomer, String countryCode) {
		return allCustomer.stream().filter(customer -> customer.getPhone().startsWith("(" + countryCode + ")"))
				.collect(Collectors.toList());
	}

}

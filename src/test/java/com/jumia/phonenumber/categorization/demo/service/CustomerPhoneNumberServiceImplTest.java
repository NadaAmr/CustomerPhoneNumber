package com.jumia.phonenumber.categorization.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.jumia.phonenumber.categorization.demo.dtos.CustomerPhoneDto;
import com.jumia.phonenumber.categorization.demo.dtos.CustomerPhonePagesDto;
import com.jumia.phonenumber.categorization.demo.enums.CountryEnum;
import com.jumia.phonenumber.categorization.demo.enums.PhoneState;
import com.jumia.phonenumber.categorization.demo.helper.CustomerPhoneHelper;
import com.jumia.phonenumber.categorization.demo.model.Country;
import com.jumia.phonenumber.categorization.demo.model.Customer;
import com.jumia.phonenumber.categorization.demo.repositories.CountryRepository;
import com.jumia.phonenumber.categorization.demo.repositories.CustomerRepository;
import com.jumia.phonenumber.categorization.demo.service.impl.CustomerPhoneNumberServiceImpl;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CustomerPhoneNumberServiceImplTest {

	@InjectMocks
	CustomerPhoneNumberServiceImpl customerPhoneNumberService;

	@Mock
	CustomerRepository customerRepository;

	@Mock
	CountryRepository countryRepository;
	

	private static List<Country> countries;

	private static List<Customer> customer;

	@BeforeAll
	static void setupData() {
		countries = CustomerPhoneHelper.setupCountryData();
		customer = CustomerPhoneHelper.setupCustomerData();
	}

	@ParameterizedTest(name = "Run {index} : case {0}")
	@MethodSource("retrieveCustomerPhoneTestData")
	void retriveAllCustomerPhoneTest(String testCase, String countryName,
			String phoneState, List<CustomerPhoneDto> expectedcustomerPhoneList) {

		// when
		doReturn(countries).when(countryRepository).findAll();

		doReturn(customer).when(customerRepository).findAll();

		CustomerPhonePagesDto actualList = customerPhoneNumberService.retrievePhoneNumbers(countryName,
				phoneState);

		// then
		assertThat(actualList.getCustomerPhoneList()).isEqualTo(expectedcustomerPhoneList);

	}

	static Stream<Arguments> retrieveCustomerPhoneTestData() {
		CustomerPhoneDto customer1 = CustomerPhoneDto.builder().phoneNumber("(212) 6007989253").country("Morocco")
				.countryCode("212").state(PhoneState.NOT_VALID).build();

		CustomerPhoneDto customer2 = CustomerPhoneDto.builder().phoneNumber("(212) 633963130").country("Morocco")
				.countryCode("212").state(PhoneState.VALID).build();

		CustomerPhoneDto customer3 = CustomerPhoneDto.builder().phoneNumber("(251) 911203317").country("Ethiopia")
				.countryCode("251").state(PhoneState.VALID).build();

		CustomerPhoneDto customer4 = CustomerPhoneDto.builder().phoneNumber("(237) 673122155").country("Cameroon")
				.countryCode("237").state(PhoneState.VALID).build();

		CustomerPhoneDto customer5 = CustomerPhoneDto.builder().phoneNumber("(237) 6A0311634").country("Cameroon")
				.countryCode("237").state(PhoneState.NOT_VALID).build();

		CustomerPhoneDto customer6 = CustomerPhoneDto.builder().phoneNumber("(258) 84330678235").country("Mozambique")
				.countryCode("258").state(PhoneState.NOT_VALID).build();

		CustomerPhoneDto customer7 = CustomerPhoneDto.builder().phoneNumber("(256) 775069443").country("Uganda")
				.countryCode("256").state(PhoneState.VALID).build();

		CustomerPhoneDto customer8 = CustomerPhoneDto.builder().phoneNumber("(256) 7503O6263").country("Uganda")
				.countryCode("256").state(PhoneState.NOT_VALID).build();

		CustomerPhoneDto customer9 = CustomerPhoneDto.builder().phoneNumber("(258) 823747618").country("Mozambique")
				.countryCode("258").state(PhoneState.VALID).build();

		CustomerPhoneDto customer10 = CustomerPhoneDto.builder().phoneNumber("(251) 911203317660").country("Ethiopia")
				.countryCode("251").state(PhoneState.NOT_VALID).build();

		CustomerPhoneDto customer11 = CustomerPhoneDto.builder().phoneNumber("(212) 6007989254").country("Morocco")
				.countryCode("212").state(PhoneState.NOT_VALID).build();

		List<CustomerPhoneDto> expectedAllCustomerPhoneList = new ArrayList<>();
		expectedAllCustomerPhoneList.add(customer1);
		expectedAllCustomerPhoneList.add(customer2);
		expectedAllCustomerPhoneList.add(customer3);
		expectedAllCustomerPhoneList.add(customer4);
		expectedAllCustomerPhoneList.add(customer5);
		expectedAllCustomerPhoneList.add(customer6);
		expectedAllCustomerPhoneList.add(customer7);
		expectedAllCustomerPhoneList.add(customer8);
		expectedAllCustomerPhoneList.add(customer9);
		expectedAllCustomerPhoneList.add(customer10);
		expectedAllCustomerPhoneList.add(customer11);


		List<CustomerPhoneDto> expectedValidCustomerPhoneList = new ArrayList<>();
		expectedValidCustomerPhoneList.add(customer2);
		expectedValidCustomerPhoneList.add(customer3);
		expectedValidCustomerPhoneList.add(customer4);
		expectedValidCustomerPhoneList.add(customer7);
		expectedValidCustomerPhoneList.add(customer9);

		List<CustomerPhoneDto> expectedNotValidCustomerPhoneList = new ArrayList<>();
		expectedNotValidCustomerPhoneList.add(customer1);
		expectedNotValidCustomerPhoneList.add(customer5);
		expectedNotValidCustomerPhoneList.add(customer6);
		expectedNotValidCustomerPhoneList.add(customer8);
		expectedNotValidCustomerPhoneList.add(customer10);
		expectedNotValidCustomerPhoneList.add(customer11);


		List<CustomerPhoneDto> expectedCustomerPhoneLastPageList = new ArrayList<>();
		expectedCustomerPhoneLastPageList.add(customer11);

		return Stream.of(
				Arguments.of("When retrieve all customer phone number  no state", null,
						null, expectedAllCustomerPhoneList),
				Arguments.of("When retrieve all customer phone number with  valid state", null,
						PhoneState.VALID.name(), expectedValidCustomerPhoneList),
				Arguments.of("When retrieve all customer phone number with  not valid state",
						null, PhoneState.NOT_VALID.name(), expectedNotValidCustomerPhoneList));
	}

	@ParameterizedTest(name = "Run {index} : case {0}")
	@MethodSource("retrieveCustomerPhoneForSpecificCountryTestData")
	void retrieveCustomerPhoneForSpecificCountryTest(String testCase, String countryName,
			String phoneState, List<CustomerPhoneDto> expectedcustomerPhoneList, Country country,
			List<Customer> customer) {

		// when
		doReturn(Optional.ofNullable(country)).when(countryRepository).findByCountryNameIgnoreCase(Mockito.any(String.class));

		doReturn(customer).when(customerRepository).findByPhoneStartsWith(Mockito.any(String.class));

		CustomerPhonePagesDto actualList = customerPhoneNumberService.retrievePhoneNumbers( countryName,
				phoneState);

		// then
		assertThat(actualList.getCustomerPhoneList()).isEqualTo(expectedcustomerPhoneList);

	}

	static Stream<Arguments> retrieveCustomerPhoneForSpecificCountryTestData() {

		CustomerPhoneDto customer1 = CustomerPhoneDto.builder().phoneNumber("(212) 6007989253").country("Morocco")
				.countryCode("212").state(PhoneState.NOT_VALID).build();

		CustomerPhoneDto customer2 = CustomerPhoneDto.builder().phoneNumber("(212) 633963130").country("Morocco")
				.countryCode("212").state(PhoneState.VALID).build();

		CustomerPhoneDto customer3 = CustomerPhoneDto.builder().phoneNumber("(251) 911203317").country("Ethiopia")
				.countryCode("251").state(PhoneState.VALID).build();

		CustomerPhoneDto customer7 = CustomerPhoneDto.builder().phoneNumber("(256) 775069443").country("Uganda")
				.countryCode("256").state(PhoneState.VALID).build();

		CustomerPhoneDto customer8 = CustomerPhoneDto.builder().phoneNumber("(256) 7503O6263").country("Uganda")
				.countryCode("256").state(PhoneState.NOT_VALID).build();

		CustomerPhoneDto customer10 = CustomerPhoneDto.builder().phoneNumber("(251) 911203317660").country("Ethiopia")
				.countryCode("251").state(PhoneState.NOT_VALID).build();

		CustomerPhoneDto customer11 = CustomerPhoneDto.builder().phoneNumber("(212) 6007989254").country("Morocco")
				.countryCode("212").state(PhoneState.NOT_VALID).build();

		/* case get phone for morocco country with no filters */
		List<CustomerPhoneDto> expectedMorccoCustomerPhoneList = new ArrayList<>();
		expectedMorccoCustomerPhoneList.add(customer1);
		expectedMorccoCustomerPhoneList.add(customer2);
		expectedMorccoCustomerPhoneList.add(customer11);

		List<Customer> moroccoCustomer = CustomerPhoneHelper.getCountryCustomers(customer, "212");

		/* case get phone for morocco country with filters for valid state only */

		List<CustomerPhoneDto> expectedMorccoCustomerPhoneWithValidStateList = new ArrayList<>();
		expectedMorccoCustomerPhoneWithValidStateList.add(customer2);

		/* case get phone for morocco country with filters for not valid state only */

		List<CustomerPhoneDto> expectedMorccoCustomerPhoneWithNotValidStateList = new ArrayList<>();
		expectedMorccoCustomerPhoneWithNotValidStateList.add(customer1);
		expectedMorccoCustomerPhoneWithNotValidStateList.add(customer11);

		/* case get phone for uganda country with no filters */

		List<CustomerPhoneDto> expectedUgandaCustomerPhoneList = new ArrayList<>();
		expectedUgandaCustomerPhoneList.add(customer7);
		expectedUgandaCustomerPhoneList.add(customer8);

		List<Customer> ugandaCustomer = CustomerPhoneHelper.getCountryCustomers(customer, "256");

		/* case get phone for uganda country with filters for valid state only */

		List<CustomerPhoneDto> expectedUgandaCustomerPhoneWithValidStateList = new ArrayList<>();
		expectedUgandaCustomerPhoneWithValidStateList.add(customer7);

		/* case get phone for uganda country with filters for not valid state only */

		List<CustomerPhoneDto> expectedUgandaCustomerPhoneWithNotValidStateList = new ArrayList<>();
		expectedUgandaCustomerPhoneWithNotValidStateList.add(customer8);

		/* case get phone for Ethiopia country with no filters */

		List<CustomerPhoneDto> expectedEthiopiaCustomerPhoneList = new ArrayList<>();
		expectedEthiopiaCustomerPhoneList.add(customer3);
		expectedEthiopiaCustomerPhoneList.add(customer10);

		List<Customer> ethiopiaCustomer = CustomerPhoneHelper.getCountryCustomers(customer, "251");

		/* case get phone for Ethiopia country with filters for valid state only */

		List<CustomerPhoneDto> expectedEthiopiaCustomerPhoneWithValidStateList = new ArrayList<>();
		expectedEthiopiaCustomerPhoneWithValidStateList.add(customer3);

		/* case get phone for Ethiopia country with filters for not valid state only */

		List<CustomerPhoneDto> expectedEthiopiaCustomerPhoneWithNotValidStateList = new ArrayList<>();
		expectedEthiopiaCustomerPhoneWithNotValidStateList.add(customer10);
		
		
		/* case get phone for Camerooon country with no filters */

		List<CustomerPhoneDto> expectedCameroonCustomerPhoneList = new ArrayList<>();


		return Stream.of(
				Arguments.of("When retrieve Morocco customer phone number with valid page Number and no state", 
						CountryEnum.MOROCCO.name(), null, expectedMorccoCustomerPhoneList, countries.get(0),
						moroccoCustomer),
				Arguments.of("When retrieve Morocco customer phone number with valid page Number and valid state ", 
						CountryEnum.MOROCCO.name(), PhoneState.VALID.name(),
						expectedMorccoCustomerPhoneWithValidStateList, countries.get(0), moroccoCustomer),
				Arguments.of("When retrieve Morocco customer phone number with valid page Number and not valid state ",
				           CountryEnum.MOROCCO.name(), PhoneState.NOT_VALID.name(),
						expectedMorccoCustomerPhoneWithNotValidStateList, countries.get(0), moroccoCustomer),
				Arguments.of("When retrieve Uganda customer phone number with valid page Number and no state", 
						CountryEnum.UGANDA.name(), null, expectedUgandaCustomerPhoneList, countries.get(3),
						ugandaCustomer),
				Arguments.of("When retrieve Uganda customer phone number with valid page Number and valid state ", 
						CountryEnum.UGANDA.name(), PhoneState.VALID.name(),
						expectedUgandaCustomerPhoneWithValidStateList, countries.get(3), ugandaCustomer),
				Arguments.of("When retrieve Uganda customer phone number with valid page Number and not valid state ",
						 CountryEnum.UGANDA.name(), PhoneState.NOT_VALID.name(),
						expectedUgandaCustomerPhoneWithNotValidStateList, countries.get(3), ugandaCustomer),
				Arguments.of("When retrieve Ethiopia customer phone number with valid page Number and no state", 
						CountryEnum.ETHIOPIA.name(), null, expectedEthiopiaCustomerPhoneList, countries.get(2),
						ethiopiaCustomer),
				Arguments.of("When retrieve Ethiopia customer phone number with valid page Number and valid state ", 
						CountryEnum.ETHIOPIA.name(), PhoneState.VALID.name(),
						expectedEthiopiaCustomerPhoneWithValidStateList, countries.get(2), ethiopiaCustomer),
				Arguments.of("When retrieve Ethiopia customer phone number with valid page Number and not valid state ",
						 CountryEnum.ETHIOPIA.name(), PhoneState.NOT_VALID.name(),
						expectedEthiopiaCustomerPhoneWithNotValidStateList, countries.get(2), ethiopiaCustomer),
				Arguments.of("When retrieve Egypt customer phone number with  no  state ",
						"Egypt", null, new ArrayList<>(), null, new ArrayList<Customer>()),
				Arguments.of("When retrieve Cameroon customer phone number with no  state ",
						CountryEnum.CAMEROON.name(), null, expectedCameroonCustomerPhoneList, null, new ArrayList<Customer>()));
	}

}

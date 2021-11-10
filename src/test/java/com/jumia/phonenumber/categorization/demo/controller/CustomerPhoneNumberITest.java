package com.jumia.phonenumber.categorization.demo.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;

import com.jumia.phonenumber.categorization.demo.dtos.CustomerPhoneDto;
import com.jumia.phonenumber.categorization.demo.dtos.CustomerPhonePagesDto;
import com.jumia.phonenumber.categorization.demo.enums.CountryEnum;
import com.jumia.phonenumber.categorization.demo.enums.PhoneState;
import com.jumia.phonenumber.categorization.demo.helper.CustomerPhoneHelper;
import com.jumia.phonenumber.categorization.demo.model.Country;
import com.jumia.phonenumber.categorization.demo.model.Customer;
import com.jumia.phonenumber.categorization.demo.repositories.CountryRepository;
import com.jumia.phonenumber.categorization.demo.repositories.CustomerRepository;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@ActiveProfiles("test")
public class CustomerPhoneNumberITest {

	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private CountryRepository countryRepository;

	@Autowired
	private MockMvc mockMvc;

	private List<Country> countryList;
	private List<Customer> customerList;

	@BeforeEach
	private void setupData() {
		customerList = CustomerPhoneHelper.setupCustomerData();
		countryList = CustomerPhoneHelper.setupCountryData();

		customerRepository.saveAll(customerList);
		countryRepository.saveAll(countryList);

	}

	@ParameterizedTest(name = "Run {index} case {0}")
	@MethodSource("whenGetCustomerData_thenSucessTestData")
	public void whenGetCustomerData_thenSucess(String testcase, String countryParam, String stateParam,
			CustomerPhonePagesDto expectedResult) throws Exception {

		LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
		requestParams.add("country", countryParam);
		requestParams.add("state", stateParam);

		ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.get("/customer/phone").params(requestParams))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.totalNumberOfElements")
						.value(is(expectedResult.getTotalNumberOfElements())))
				.andExpect(MockMvcResultMatchers.jsonPath("$.customerPhoneList",
						hasSize(expectedResult.getCustomerPhoneList().size())))
				.andDo(MockMvcResultHandlers.print());

		for (int i = 0; i < expectedResult.getCustomerPhoneList().size(); i++) {

			result.andExpect(MockMvcResultMatchers.jsonPath("$.customerPhoneList[" + i + "].phoneNumber",
					is(expectedResult.getCustomerPhoneList().get(i).getPhoneNumber())))
					.andExpect(MockMvcResultMatchers.jsonPath("$.customerPhoneList[" + i + "].country",
							is(expectedResult.getCustomerPhoneList().get(i).getCountry())))
					.andExpect(MockMvcResultMatchers.jsonPath("$.customerPhoneList[" + i + "].countryCode",
							is(expectedResult.getCustomerPhoneList().get(i).getCountryCode())))
					.andExpect(MockMvcResultMatchers.jsonPath("$.customerPhoneList[" + i + "].state",
							is(expectedResult.getCustomerPhoneList().get(i).getState().name())))
					.andDo(MockMvcResultHandlers.print()).andReturn();
		}

	}

	static Stream<Arguments> whenGetCustomerData_thenSucessTestData() {
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

		CustomerPhonePagesDto expectedAllCustomerPhoneDto = CustomerPhonePagesDto.builder()
				.customerPhoneList(expectedAllCustomerPhoneList)
				.totalNumberOfElements(expectedAllCustomerPhoneList.size()).build();

		List<CustomerPhoneDto> expectedCustomerPhoneListCase2 = new ArrayList<>();
		expectedCustomerPhoneListCase2.add(customer1);
		expectedCustomerPhoneListCase2.add(customer2);
		expectedCustomerPhoneListCase2.add(customer11);

		CustomerPhonePagesDto expectedAllCustomerPhoneDtoCase2 = CustomerPhonePagesDto.builder()
				.customerPhoneList(expectedCustomerPhoneListCase2)
				.totalNumberOfElements(expectedCustomerPhoneListCase2.size()).build();

		List<CustomerPhoneDto> expectedCustomerPhoneListCase3 = new ArrayList<>();
		expectedCustomerPhoneListCase3.add(customer2);
		expectedCustomerPhoneListCase3.add(customer3);
		expectedCustomerPhoneListCase3.add(customer4);
		expectedCustomerPhoneListCase3.add(customer7);
		expectedCustomerPhoneListCase3.add(customer9);

		CustomerPhonePagesDto expectedAllCustomerPhoneDtoCase3 = CustomerPhonePagesDto.builder()
				.customerPhoneList(expectedCustomerPhoneListCase3)
				.totalNumberOfElements(expectedCustomerPhoneListCase3.size()).build();

		List<CustomerPhoneDto> expectedCustomerPhoneListCase4 = new ArrayList<>();
		expectedCustomerPhoneListCase4.add(customer1);
		expectedCustomerPhoneListCase4.add(customer5);
		expectedCustomerPhoneListCase4.add(customer6);
		expectedCustomerPhoneListCase4.add(customer8);
		expectedCustomerPhoneListCase4.add(customer10);
		expectedCustomerPhoneListCase4.add(customer11);

		CustomerPhonePagesDto expectedAllCustomerPhoneDtoCase4 = CustomerPhonePagesDto.builder()
				.customerPhoneList(expectedCustomerPhoneListCase4)
				.totalNumberOfElements(expectedCustomerPhoneListCase4.size()).build();

		List<CustomerPhoneDto> expectedCustomerPhoneListCase5 = new ArrayList<>();
		expectedCustomerPhoneListCase5.add(customer1);
		expectedCustomerPhoneListCase5.add(customer11);

		CustomerPhonePagesDto expectedAllCustomerPhoneDtoCase5 = CustomerPhonePagesDto.builder()
				.customerPhoneList(expectedCustomerPhoneListCase5)
				.totalNumberOfElements(expectedCustomerPhoneListCase5.size()).build();

		return Stream.of(Arguments.of("When get phone with no param", null, null, expectedAllCustomerPhoneDto),
				Arguments.of("When get phone with country param only", CountryEnum.MOROCCO.name(), null,
						expectedAllCustomerPhoneDtoCase2),
				Arguments.of("When get phone with valid state param only", null, PhoneState.VALID.name(),
						expectedAllCustomerPhoneDtoCase3),
				Arguments.of("When get phone with not valid state param only", null, PhoneState.NOT_VALID.name(),
						expectedAllCustomerPhoneDtoCase4),
				Arguments.of("When get phone with country param and not valid state param ", CountryEnum.MOROCCO.name(),
						PhoneState.NOT_VALID.name(), expectedAllCustomerPhoneDtoCase5)

		);
	}

	
	@Test
	public void whenGetCustomerData_thenFaliure() throws Exception {

		LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
		requestParams.add("country", CountryEnum.MOROCCO.name());
		requestParams.add("state", "Test");

		this.mockMvc.perform(MockMvcRequestBuilders.get("/customer/phone").params(requestParams))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message")
						.value(is("state Test not valid")))
				.andDo(MockMvcResultHandlers.print());

	}
}

package com.jumia.phonenumber.categorization.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jumia.phonenumber.categorization.demo.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
	
	List <Customer> findAll();
	
	List<Customer> findByPhoneStartsWith( String countrycode);
	
	
}

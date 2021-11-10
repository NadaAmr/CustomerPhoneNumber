package com.jumia.phonenumber.categorization.demo.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jumia.phonenumber.categorization.demo.model.Country;

public interface CountryRepository extends JpaRepository<Country,Long> {
	
	public List<Country> findAll();
	
	Optional<Country> findByCountryNameIgnoreCase(String countryName);

}

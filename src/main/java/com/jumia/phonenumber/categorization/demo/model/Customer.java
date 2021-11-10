package com.jumia.phonenumber.categorization.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name="Customer")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

	@Id
	private Long id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="phone")
	private String phone;
	
	
}

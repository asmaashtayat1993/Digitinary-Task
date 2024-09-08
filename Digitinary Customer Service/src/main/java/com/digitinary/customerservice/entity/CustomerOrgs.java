package com.digitinary.customerservice.entity;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "customer_orgs")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerOrgs {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Schema(description = "Customer Organization Id- Auto Genarated", example = "1")
	private Long orgId;

	@NotEmpty
	@Schema(description = " Organization Registration number", example = "DIG01478")
	private String registrationNo;

	@NotEmpty
	@Schema(description = "Organization Legal Name", example = "Digitinary")
	private String legalName;

	@Schema(description = "Organization Trademark Name", example = "Digitinary Organization")
	private String trademarkName;

	@OneToMany(mappedBy = "customerOrgs",cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference("org-addresses")
	@Schema(description = "Organization Address")
	private List<Addresses> addresses;
	
	@OneToOne
	@JoinColumn(name="customerId")
	@JsonBackReference("customer-org")
	private Customers customer;

}

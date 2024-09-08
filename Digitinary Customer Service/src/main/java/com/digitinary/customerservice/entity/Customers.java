package com.digitinary.customerservice.entity;

import java.util.List;

import com.digitinary.customerservice.enums.CustomerType;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "customers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Customers {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Schema(description = "Customer Id - Auto Genarated", example = "1")
	private Long id;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Schema(description = "Customer Type", example = "INDIVIDUAL", allowableValues = { "INDIVIDUAL", "ORGANIZATION" })
	private CustomerType type;

	@NotEmpty
	@Size(max = 250)
	@Schema(description = "Customer Name", example = "Hamzah Ali")
	private String name;

	@Size(max = 14)
	@Schema(description = "Customer MobileNumber", example = "00962780880950")
	private String mobileNo;

	
	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference("customer-addresses")
	@Schema(description = "Customer Address's List")
	private List<Addresses> addresses;

	@OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference("customer-org")
	@Schema(description = "Customer Orgnaization Information")
	private CustomerOrgs customerOrg;

}

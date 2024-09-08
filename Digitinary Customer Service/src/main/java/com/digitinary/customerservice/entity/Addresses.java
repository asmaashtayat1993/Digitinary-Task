package com.digitinary.customerservice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "addresses")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Addresses {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Address Id-Auto Genarated", example = "1")
    private Long id;

    @NotEmpty
    @Schema(description = "Address- LineOne", example = "Amman- Mecca Street")
    private String lineOne;

    @Schema(description = "Address- LineTwo", example = "Amman- Almadena Street")
    private String lineTwo;

    @NotEmpty
    @Schema(description = "Postal code for the address", example = "74125")
    private String postalCode;

    @NotEmpty
    @Schema(description = "Address-City", example = "Amman")
    private String city;

    @NotEmpty
    @Schema(description = "Address-Country", example = "Jordan")
    private String country;
    
    @ManyToOne
    @JoinColumn(name = "customerId")
    @JsonBackReference("customer-addresses")
    @Schema(description = "Customer to which this address belongs")
    private Customers customer;
    
    @ManyToOne
    @JoinColumn(name = "orgId")
    @JsonBackReference("org-addresses")
    @Schema(description = "Customer to which this address belongs")
    private CustomerOrgs customerOrgs;
}

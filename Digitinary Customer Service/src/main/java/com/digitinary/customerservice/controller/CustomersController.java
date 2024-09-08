package com.digitinary.customerservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.digitinary.customerservice.entity.Customers;
import com.digitinary.customerservice.service.CustomersService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/customers")
@Validated
public class CustomersController {
	@Autowired
	private CustomersService customerService;

	@Operation(summary = "Create a new customer", description = "Create a new customer")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Customer created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input provided"),
        @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
        @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
        @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")

    })
	@PostMapping("/create")
	public ResponseEntity<Customers> createCustomer(@Valid @RequestBody Customers customer) {
		ResponseEntity<Customers> createdCustomer= customerService.createCustomer(customer);
		return new ResponseEntity<>(createdCustomer.getBody(), HttpStatus.CREATED);
	}

	
    @Operation(summary = "Update  customer", description = "Update customer details for the given CustomerID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Customer updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input provided"),
        @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
        @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
        @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")
    })

	@PutMapping("/update/{id}")
	public ResponseEntity<Customers> updateCustomer(@PathVariable("id")  Long id, @Valid @RequestBody Customers customer) {
		Customers updatedCustomer = customerService.updateCustomer(id, customer);
		return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
	}
    
    
    @Operation(summary = "Delete a customer", description = "Delete acustomer by ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Customer deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Customer not found"),
        @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
        @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
        @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")

    })

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteCustomer(@PathVariable("id")  Long id) {
		customerService.deleteCustomer(id);
		 return new ResponseEntity<>("Customer Deleted Successfully", HttpStatus.OK);	
		 }
	
    
    @Operation(summary = "Get a customer by ID", description = "Retrieve customer by ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Customer retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Customer not found"),
        @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
        @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
        @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")

    })

	@GetMapping("/get/{id}")
    public ResponseEntity<Customers> getCustomerById(@PathVariable("id")  Long id) {
        Customers customer = customerService.getCustomerById(id);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }
}

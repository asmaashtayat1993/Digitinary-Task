package com.digitinary.customerservice;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import com.digitinary.customerservice.controller.CustomersController;
import com.digitinary.customerservice.entity.Customers;
import com.digitinary.customerservice.enums.CustomerType;
import com.digitinary.customerservice.exception.CustomerNotFoundException;
import com.digitinary.customerservice.exception.CustomerValidationException;
import com.digitinary.customerservice.service.CustomersService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@WebMvcTest(CustomersController.class)
@Slf4j
public class CustomersControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CustomersService customersService;

	@InjectMocks
	private CustomersController customersController;

	@Autowired
	private ObjectMapper objectMapper;

	private Customers customer;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		customer = new Customers();
		customer.setId(1L);
		customer.setMobileNo("00962780880950");
		customer.setName("ALi");
		customer.setType(CustomerType.ORGANIZATION);
	}

	@Test
	void testSuccessCreateCustomer() throws Exception {
	    Customers newCustomer = new Customers();
	    newCustomer.setId(1L);
	    newCustomer.setName("Asmaa");
	    newCustomer.setMobileNo("1234567890");
	    newCustomer.setType(CustomerType.INDIVIDUAL);
	    
	    when(customersService.createCustomer(any(Customers.class))).thenReturn(
	            new ResponseEntity<>(newCustomer, HttpStatus.CREATED));

	    mockMvc.perform(post("/customers/create")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(objectMapper.writeValueAsString(newCustomer)))
	            .andExpect(status().isCreated())
	            .andExpect(content().json(objectMapper.writeValueAsString(newCustomer)));
	}
/*	@Test
	void testFailedCreateCustomer() throws Exception {
		    Customers invalidCustomer = new Customers();
		    invalidCustomer.setId(1L);
		    invalidCustomer.setMobileNo("12345");
		    invalidCustomer.setName(""); 
		    invalidCustomer.setType(CustomerType.ORGANIZATION);

		    when(customersService.createCustomer(any(Customers.class)))
		            .thenThrow(new CustomerValidationException("Customer name must not be empty"));

		    mockMvc.perform(post("/customers/create")
		            .contentType(MediaType.APPLICATION_JSON)
		            .content(objectMapper.writeValueAsString(invalidCustomer)))
		            .andExpect(status().isBadRequest()) 
		            .andExpect(content().contentType(MediaType.APPLICATION_JSON)) 
		            .andExpect(jsonPath("$.message").value("Customer name must not be empty")); 
		
	}*/

	@Test
	void testSuccessUpdateCustomer() throws Exception {
		when(customersService.updateCustomer(anyLong(), any(Customers.class))).thenReturn(customer);

		mockMvc.perform(put("/customers/update/1").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(customer))).andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(customer)));
	}

	@Test
	void testFailedUpdateCustomer() throws Exception {
		when(customersService.updateCustomer(anyLong(), any(Customers.class)))
				.thenThrow(new CustomerNotFoundException("Customer not found"));

		mockMvc.perform(put("/customers/update/1").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(customer))).andExpect(status().isNotFound())
				.andExpect(jsonPath("$.status").value(404))
				.andExpect(jsonPath("$.message").value("Customer not found"));
	}

	@Test
	@Order(1)
	void testSuccessDeleteCustomer() throws Exception {
		doNothing().when(customersService).deleteCustomer(anyLong());

		mockMvc.perform(delete("/customers/delete/1")).andExpect(status().isOk())
				.andExpect(content().string("Customer Deleted Successfully"));
	}

	@Test
	@Order(2)
	void testFailedDeleteCustomer() throws Exception {
	    doThrow(new CustomerNotFoundException("Customer with id not found : 1")).when(customersService)
	            .deleteCustomer(anyLong());

	    mockMvc.perform(delete("/customers/delete/1"))
	            .andExpect(status().isNotFound())
	            .andExpect(jsonPath("$.status").value(404))
	            .andExpect(jsonPath("$.message").value("Customer with id not found : 1"));
	}
	@Test
	void testFailedSuccessGetCustomerById() throws Exception {
		 when(customersService.getCustomerById(anyLong())).thenThrow(new CustomerNotFoundException("Customer not found with id: 1"));

		    mockMvc.perform(get("/customers/get/1"))
		            .andExpect(status().isNotFound())
		            .andExpect(jsonPath("$.status").value(404))
		            .andExpect(jsonPath("$.message").value("Customer not found with id: 1"));
	}

	@Test
	void testGetCustomerById() throws Exception {
	    when(customersService.getCustomerById(anyLong())).thenThrow(new CustomerNotFoundException("Customer not found with id: 147"));

	    mockMvc.perform(get("/customers/get/147"))
	            .andExpect(status().isNotFound())
	            .andExpect(jsonPath("$.status").value(404))
	            .andExpect(jsonPath("$.message").value("Customer not found with id: 147"));
	}
}
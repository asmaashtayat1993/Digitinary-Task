package com.digitinary.customerservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import com.digitinary.customerservice.entity.Customers;
import com.digitinary.customerservice.enums.CustomerType;
import com.digitinary.customerservice.event.CustomerNotificationEvent;
import com.digitinary.customerservice.event.CustomerPublisher;
import com.digitinary.customerservice.exception.CustomerNotFoundException;
import com.digitinary.customerservice.repository.CustomersRepository;
import com.digitinary.customerservice.service.CustomersService;
import com.digitinary.customerservice.util.JsonUtil;

@SpringBootTest
public class CustomersServiceTest {

	@InjectMocks
	private CustomersService customersService;

	@Mock
	private CustomersRepository customersRepository;

	@Mock
	private CustomerPublisher customerPublisher;

	@Mock
	private JsonUtil jsonUtil;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@Order(1)
	public void testSuccessCreateCustomer() {
		Customers customer = new Customers();
		customer.setName("shaden");
		customer.setMobileNo("00962780880950");
		when(customersRepository.save(any(Customers.class))).thenReturn(customer);
		when(jsonUtil.convertToJson(any(CustomerNotificationEvent.class))).thenReturn("{}");
	
		ResponseEntity<Customers> createdCustomer = customersService.createCustomer(customer);

		verify(customersRepository, times(1)).save(customer);
		verify(customerPublisher, times(1)).sendCustomerEvent("{}");
		assertEquals(customer, createdCustomer.getBody());
	}

	@Test
	@Order(5)
	public void testFaildCreateCustomer() {
		Customers customer = new Customers();
		customer.setName("shaden");
		customer.setMobileNo("00962780880950");
		customer.setType(CustomerType.ORGANIZATION);
		  // Mocking repository behavior if needed (though it should not be called)
	    when(customersRepository.save(any(Customers.class))).thenThrow(new IllegalArgumentException("Organization details must be provided for customers of type ORGANIZATION."));

	    // Perform the test and assert that an exception is thrown
	    IllegalArgumentException thrown = assertThrows(
	        IllegalArgumentException.class,
	        () -> customersService.createCustomer(customer),
	        "Expected createCustomer() to throw, but it didn't"
	    );

	    // Check the exception message
	    assertEquals("Organization details must be provided for customers of type ORGANIZATION.", thrown.getMessage());

	    // Verify that the repository save method was not called
	    verify(customersRepository, times(0)).save(any(Customers.class));
	    verify(customerPublisher, times(0)).sendCustomerEvent(anyString());
	}

	@Test
	@Order(2)
	public void testSuccessUpdateCustomer() {
		Long id = 1L;
		Customers customer = new Customers();
		customer.setName("shaden");

		when(customersRepository.existsById(anyLong())).thenReturn(true);
		when(customersRepository.save(any(Customers.class))).thenReturn(customer);
		when(jsonUtil.convertToJson(any(CustomerNotificationEvent.class))).thenReturn("{}");

		Customers updatedCustomer = customersService.updateCustomer(id, customer);

		verify(customersRepository, times(1)).save(customer);
		verify(customerPublisher, times(1)).sendCustomerEvent("{}");
		assertEquals(customer, updatedCustomer);
	}

	@Test
	@Order(6)
	public void testFaildUpdateCustomer() {
	    Long id = 1L;
	    Customers customer = new Customers();
	    customer.setName("shaden");
	    customer.setMobileNo("1478521478");
	    customer.setType(CustomerType.ORGANIZATION);

	    // Simulate that the customer exists in the repository
	    when(customersRepository.existsById(anyLong())).thenReturn(true);

	    when(customersRepository.save(any(Customers.class))).thenThrow(new IllegalArgumentException("Organization details must be provided for customers of type ORGANIZATION."));

	    // Perform the test and assert that an exception is thrown
	    IllegalArgumentException thrown = assertThrows(
	        IllegalArgumentException.class,
	        () -> customersService.createCustomer(customer),
	        "Expected createCustomer() to throw, but it didn't"
	    );

	    // Check the exception message
	    assertEquals("Organization details must be provided for customers of type ORGANIZATION.", thrown.getMessage());

	    // Verify that the repository save method was not called
	    verify(customersRepository, times(0)).save(any(Customers.class));
	    verify(customerPublisher, times(0)).sendCustomerEvent(anyString());

	}

	public void testSuccessDeleteCustomer() {
	    Long id = 1L;

	    // Mock repository behavior
	    when(customersRepository.existsById(anyLong())).thenReturn(true);

	    // Execute the delete method
	    customersService.deleteCustomer(id);

	    // Verify repository interactions
	    verify(customersRepository, times(1)).deleteById(id);

	    // Verify publisher interactions with correct argument
	    verify(customerPublisher, times(1)).sendCustomerEvent("{}");
	}
	@Test
	@Order(7)
	public void testGetNotFoundCustomerById() {
		Long id = 1L;

		when(customersRepository.findById(anyLong())).thenReturn(java.util.Optional.empty());

		assertThrows(CustomerNotFoundException.class, () -> {
			customersService.getCustomerById(id);
		});
	}

	@Test
	@Order(4)
	public void testGetSuccessCustomerById() {
		Long id = 1L;
		Customers expectedCustomer = new Customers();
		expectedCustomer.setId(id);
		expectedCustomer.setName("John Doe");

		when(customersRepository.findById(id)).thenReturn(java.util.Optional.of(expectedCustomer));
		Customers actualCustomer = customersService.getCustomerById(id);

		assertEquals(expectedCustomer, actualCustomer, "Succssfully");
	}

}
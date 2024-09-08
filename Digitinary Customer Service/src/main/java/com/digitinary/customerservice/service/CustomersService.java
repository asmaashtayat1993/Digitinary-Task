package com.digitinary.customerservice.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.digitinary.customerservice.entity.CustomerOrgs;
import com.digitinary.customerservice.entity.Customers;
import com.digitinary.customerservice.enums.CustomerType;
import com.digitinary.customerservice.enums.NotificationType;
import com.digitinary.customerservice.event.CustomerNotificationEvent;
import com.digitinary.customerservice.event.CustomerPublisher;
import com.digitinary.customerservice.exception.CustomerNotFoundException;
import com.digitinary.customerservice.exception.CustomerValidationException;
import com.digitinary.customerservice.exception.SerializationException;
import com.digitinary.customerservice.exception.UpdateCustomerException;
import com.digitinary.customerservice.repository.CustomersRepository;
import com.digitinary.customerservice.util.JsonUtil;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomersService {

	@Autowired
	private CustomersRepository customerRepository;
	@Autowired
	private CustomerPublisher customerPublisher;

	@Autowired
	private JsonUtil jsonUtil;

	@Transactional
	public ResponseEntity<Customers> createCustomer(Customers customer) {
		log.info("Start Creating customer: ");
		validateCustomer(customer);
		try {
		
			Customers savedCustomer = customerRepository.save(customer);
			log.info("Customer created successfully:");
			publishEvent(NotificationType.CUSTOMER_CREATED, "Customer Created",
					"A new customer has been created: " + savedCustomer.getName());
			 return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
		} catch (CustomerValidationException e) {
	        log.error(" error: ", e);
	        return ResponseEntity.badRequest().body(null); 
	    } catch (Exception e) {
	        log.error("Unexpected error: ", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	    }
	}

	private void validateCustomer(Customers customer) {
		if (customer.getType() == CustomerType.ORGANIZATION) {
			validateOrganization(customer.getCustomerOrg());
		}
	}

	@Transactional
	public Customers updateCustomer(Long id, Customers customer) {

		log.info("Updating customer: ");
		if (!customerRepository.existsById(id)) {
			throw new CustomerNotFoundException("Customer not found with id: " + id);
		}
		try {
			validateCustomer(customer);
			customer.setId(id);
			Customers updatedCustomer = customerRepository.save(customer);
			log.info("Customer Updatied successfully: ");
			publishEvent(NotificationType.CUSTOMER_UPDATED, "Customer Updated",
					"Customer details updated: " + updatedCustomer.getName());

			return updatedCustomer;
		}catch (CustomerValidationException e) {
	        log.error("error: " + e.getMessage());
	        throw e;  
	    } catch (Exception e) {
	        log.error("Unexpected error : " + e.getMessage(), e);
	        throw new UpdateCustomerException("Failed to update customer due to an unexpected error");
	    }
	}

	@Transactional
	public void deleteCustomer(Long id) {

		log.info("Deleting Customer with ID  : " + id);
		if (!customerRepository.existsById(id)) {
			throw new CustomerNotFoundException("Customer with id not found : " + id);
		}
		try {
			customerRepository.deleteById(id);
			log.info("Customer Deleted successfully");
			publishEvent(NotificationType.CUSTOMER_DELETED, "Customer Deleted",
					"Customer with ID " + id + " has been deleted.");
		} catch (Exception e) {
			log.error("Unexpected error : " + e);
			throw new RuntimeException("Unexpected error" + e);
		}

	}

	private void publishEvent(NotificationType type, String title, String message) {
		try {
			CustomerNotificationEvent event = new CustomerNotificationEvent(type, title, message, LocalDateTime.now());
			String json = jsonUtil.convertToJson(event);
			customerPublisher.sendCustomerEvent(json);
			log.info("Event published successfully: {}", json);
		} catch (Exception e) {
			log.error("Error publishing event: " + e.getMessage());
			throw new SerializationException("Failed to serialize and publish event", e);
		}

	}

	private void validateOrganization(CustomerOrgs customerOrg) {
		if (customerOrg == null) {
			throw new IllegalArgumentException(
					"Organization details must be provided for customers of type ORGANIZATION.");
		}
		if (customerOrg.getRegistrationNo() == null || customerOrg.getRegistrationNo().isEmpty()) {
			throw new IllegalArgumentException("Registration number must be provided for the organization.");
		}
		if (customerOrg.getLegalName() == null || customerOrg.getLegalName().isEmpty()) {
			throw new IllegalArgumentException("Legal name must be provided for the organization.");
		}
		if (customerOrg.getAddresses() == null || customerOrg.getAddresses().isEmpty()) {
			throw new IllegalArgumentException("At least one address must be provided for the organization.");
		}

	}

	public Customers getCustomerById(Long id) {
	        return customerRepository.findById(id)
	                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));

	}

}
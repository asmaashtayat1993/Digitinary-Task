package com.digitinary.customerservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.digitinary.customerservice.entity.Customers;

@Repository
public interface CustomersRepository extends JpaRepository<Customers, Long> {
}

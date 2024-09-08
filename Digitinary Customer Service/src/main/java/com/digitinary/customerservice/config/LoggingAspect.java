package com.digitinary.customerservice.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.digitinary.customerservice.entity.Customers;


@Aspect
@Component
public class LoggingAspect {

    @Before("execution(* com.digitinary.customerservice.service..*(..))")
    public void logBefore(JoinPoint joinPoint) {
        System.out.println("Entering method: " + joinPoint.getSignature().getName());
    }

    @After("execution(* com.digitinary.customerservice.service..*(..))")
    public void logAfter(JoinPoint joinPoint) {
        System.out.println("Exiting method: " + joinPoint.getSignature().getName());
    }
    
    @Before("execution(* com.digitinary.customerservice.service.CustomersService.createCustomer(..)) || " +
            "execution(* com.digitinary.customerservice.service.CustomersService.updateCustomer(..))")
    public void logBeforeCustomerSaveOrUpdate(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof Customers) {
                Customers customer = (Customers) arg;
                System.out.println("Customer Details Before Save/Update: " + customer.getId() + ","+customer.getName());
            }
        }
    } 
}
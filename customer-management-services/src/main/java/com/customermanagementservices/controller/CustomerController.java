package com.customermanagementservices.controller;

import com.commonmessaging.model.Customer;
import com.customermanagementservices.service.CustomerService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/customers", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Customer addCustomer(@RequestBody Customer customer) {
        System.out.println("CustomerController: adding customer: " + customer);
        return customerService.addCustomer(customer);
    }

    @PostMapping (path = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Customer updateCustomer(@RequestBody Customer customer) {
        return customerService.updateCustomer(customer);
    }

    @GetMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Customer getCustomerById(@PathVariable String id) {
        return customerService.getCustomerById(id);
    }

    @GetMapping(path = "/name/{name}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Customer getCustomerByName(@PathVariable String name) {
        return customerService.getCustomerByName(name);
    }

    @GetMapping(path = "/email/{email}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Customer getCustomerByEmail(@PathVariable String email) {
        return customerService.getCustomerByEmail(email);
    }

    @GetMapping(path = "/phone/{phone}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Customer getCustomerByPhone(@PathVariable String phone) {
        return customerService.getCustomerByPhone(phone);
    }

    @DeleteMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteCustomerById(@PathVariable String id) {
        customerService.deleteCustomerById(id);
    }
}

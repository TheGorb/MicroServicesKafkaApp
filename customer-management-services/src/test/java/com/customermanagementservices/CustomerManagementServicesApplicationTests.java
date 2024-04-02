package com.customermanagementservices;

import com.commonmessaging.model.Customer;
import com.commonmessaging.producer.CommonProducer;
import com.commonmessaging.repository.CustomerRepository;
import com.customermanagementservices.consumer.CustomerConsumer;
import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;

@RunWith(SpringRunner.class)
@DirtiesContext
@EmbeddedKafka(partitions = 1, topics = {"newCustomer", "rejectedPayment", "acceptedPayment"}, brokerProperties = {"listeners=localhost:29092", "port=29092"})
class CustomerManagementServicesApplicationTests {

    private CustomerRepository customerRepository;

    private CustomerConsumer customerConsumer;

    @BeforeEach
    void setUp() {
        customerRepository = Mockito.mock(CustomerRepository.class);
        CommonProducer commonProducer = Mockito.mock(CommonProducer.class);
        customerConsumer = new CustomerConsumer(customerRepository, commonProducer);
        customerRepository.deleteAll();
    }

    @Test
    void consumeCustomerEvent() {
        Customer mockCustomer = new Customer("testCustomer", "testCustomer@email.com", "123")
                .setId("customer123");

        LogCaptor logCaptor = LogCaptor.forClass(CustomerConsumer.class);

        Mockito
                .when(customerRepository
                        .save(any(Customer.class)))
                .thenReturn(mockCustomer);

        customerConsumer.addCustomer(mockCustomer);

        Mockito
                .verify(customerRepository, times(1))
                .save(any(Customer.class));

        assertThat(logCaptor.getInfoLogs().get(0))
                .contains("Received addCustomer");
    }

    @Test
    void updateCustomer() {
        Customer mockCustomer = new Customer("testCustomer", "testCustomer@email.com", "123")
                .setId("customer123");

        customerRepository.save(mockCustomer);

        Mockito
                .when(customerRepository
                        .findById(anyString()))
                .thenReturn(Optional.of(mockCustomer));

        LogCaptor logCaptor = LogCaptor.forClass(CustomerConsumer.class);

        customerConsumer.updateCustomer(mockCustomer);

        Mockito
                .verify(customerRepository, times(2))
                .save(any(Customer.class));

        assertThat(logCaptor.getInfoLogs().get(0))
                .contains("Received updateCustomer event");
    }

    @Test
    void updateCustomer_badCustomer() {
        Customer mockCustomer = new Customer("testCustomer", "testCustomer@email.com", "123")
                .setId("nonExistingCustomerId");

        LogCaptor logCaptor = LogCaptor.forClass(CustomerConsumer.class);

        customerConsumer.updateCustomer(mockCustomer);

        Mockito
                .verify(customerRepository, times(0))
                .save(any(Customer.class));

        assertThat(logCaptor.getWarnLogs().get(0))
                .contains("Customer not found with ID");
    }
}

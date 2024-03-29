package com.paymentprocessingservice;

import com.commonmessaging.model.Customer;
import com.commonmessaging.model.Payment;
import com.commonmessaging.repository.CustomerRepository;
import com.paymentprocessingservice.consumer.PaymentConsumer;
import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, topics = {"newCustomer", "rejectedPayment", "acceptedPayment"})
class PaymentProcessingServiceApplicationTests {

    @MockBean
    private CustomerRepository customerRepository;

    @Autowired
    private PaymentConsumer paymentConsumer;

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();
    }
    @Test
    void consumeCustomerEvent() {
        Customer mockCustomer = new Customer("testCustomer", "testCustomer@email.com", "123")
                .setId("customer123");

        customerRepository.save(mockCustomer);

        Mockito
                .when(customerRepository
                        .findById(anyString()))
                .thenReturn(Optional.of(mockCustomer));

        LogCaptor logCaptor = LogCaptor.forClass(PaymentConsumer.class);

        paymentConsumer.consumeCustomerEvent(mockCustomer);

        Mockito
                .verify(customerRepository, times(2))
                .save(any(Customer.class));

        assertThat(logCaptor.getInfoLogs().get(0))
                .contains("New customer processed:");
    }

    @Test
    void consumeCustomerEvent_badCustomer() {
        Customer mockCustomer = new Customer("testCustomer", "testCustomer@email.com", "123")
                .setId("nonExistingCustomerId");

        LogCaptor logCaptor = LogCaptor.forClass(PaymentConsumer.class);

        paymentConsumer.consumeCustomerEvent(mockCustomer);

        Mockito
                .verify(customerRepository, times(0))
                .save(any(Customer.class));

        assertThat(logCaptor.getWarnLogs().get(0))
                .contains("Customer not found:");
    }

    @Test
    void rejectedPayment() {
        Customer mockCustomer = new Customer("testCustomer", "testCustomer@email.com", "123")
                .setId("customer123");
        Payment mockPayment = new Payment("400", "USD", "customer123");

        customerRepository.save(mockCustomer);

        Mockito
                .when(customerRepository
                        .findById(anyString()))
                .thenReturn(Optional.of(mockCustomer));

        LogCaptor logCaptor = LogCaptor.forClass(PaymentConsumer.class);
        paymentConsumer.rejectedPayment(mockPayment);

        Mockito
                .verify(customerRepository, times(2))
                .save(any(Customer.class));

        assertThat(logCaptor.getInfoLogs().get(0))
                .contains("Rejected payment processed for customer:");
    }

    @Test
    void rejectedPayment_badCustomer() {
        Payment mockPayment = new Payment("400", "USD", "customer123");

        Mockito.verify(customerRepository, Mockito.never())
                .save(any(Customer.class));

        LogCaptor logCaptor = LogCaptor.forClass(PaymentConsumer.class);
        paymentConsumer.rejectedPayment(mockPayment);

        Mockito
                .verify(customerRepository, times(0))
                .save(any(Customer.class));

        assertThat(logCaptor.getWarnLogs().get(0))
                .contains("Customer not found for rejected payment:");
    }

    @Test
    void acceptedPayment() {
        Customer mockCustomer = new Customer("testCustomer", "testCustomer@email.com", "123")
                .setId("customer123");
        Payment mockPayment = new Payment("400", "USD", "customer123");

        customerRepository.save(mockCustomer);

        Mockito
                .when(customerRepository
                        .findById(anyString()))
                .thenReturn(Optional.of(mockCustomer));

        LogCaptor logCaptor = LogCaptor.forClass(PaymentConsumer.class);
        paymentConsumer.acceptedPayment(mockPayment);

        Mockito
                .verify(customerRepository, times(2))
                .save(any(Customer.class));

        assertThat(logCaptor.getInfoLogs().get(0))
                .contains("Accepted payment processed for customer:");
    }

    @Test
    void acceptedPayment_badCustomer() {
        Payment mockPayment = new Payment("400", "USD", "customer123");

        Mockito.verify(customerRepository, Mockito.never())
                .save(any(Customer.class));

        LogCaptor logCaptor = LogCaptor.forClass(PaymentConsumer.class);
        paymentConsumer.rejectedPayment(mockPayment);

        Mockito
                .verify(customerRepository, times(0))
                .save(any(Customer.class));

        assertThat(logCaptor.getWarnLogs().get(0))
                .contains("Customer not found for rejected payment:");
    }
}

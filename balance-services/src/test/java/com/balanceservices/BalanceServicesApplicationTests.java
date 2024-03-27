package com.balanceservices;

import com.balanceservices.consumer.BalanceConsumer;
import com.balanceservices.repository.BalanceRepository;
import com.commonmessaging.model.Balance;
import com.commonmessaging.model.Customer;
import com.commonmessaging.model.Payment;
import com.commonmessaging.producer.CommonProducer;
import com.commonmessaging.repository.CustomerRepository;
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
@EmbeddedKafka(partitions = 1, topics = {"addBalance", "lowerBalance"})
class BalanceServicesApplicationTests {
    @Autowired
    private CommonProducer commonProducer;

    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private BalanceRepository balanceRepository;

    @Autowired
    private BalanceConsumer balanceConsumer;

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();
    }

    @Test
    void addBalance() {;
        Customer mockCustomer = new Customer("testCustomer", "testCustomer@email.com", "123")
                .setId("customer123");

        Payment event = new Payment("400", "USD", mockCustomer.getId());
        Balance balance = new Balance(mockCustomer.getId(), "0", "0");

        mockCustomer.setCustomerBalance(balance);
        LogCaptor logCaptor = LogCaptor.forClass(BalanceConsumer.class);

        customerRepository.save(mockCustomer);

        Mockito
                .when(customerRepository
                        .findById(anyString()))
                .thenReturn(Optional.of(mockCustomer));

        balanceConsumer.consumeAddBalanceEvent(event);

        Mockito
                .verify(customerRepository, times(2))
                .save(any(Customer.class));

        assertThat(logCaptor.getInfoLogs().get(0))
                .startsWith("Balance updated for customer");
    }

    @Test
    void addBalance_customerDoesntExist() {
        Payment payment = new Payment("400", "USD", "nonExistingCustomerId");
        Mockito.when(customerRepository.findById(payment.getCustomerId()))
                .thenReturn(Optional.empty());

        LogCaptor logCaptor = LogCaptor.forClass(BalanceConsumer.class);

        balanceConsumer.consumeAddBalanceEvent(payment);

        Mockito.verify(customerRepository, Mockito.never())
                .save(any(Customer.class));

        assertThat(logCaptor.getWarnLogs().get(0))
                .startsWith("Customer not found");
    }

    @Test
    void lowerBalance() {;
        Customer mockCustomer = new Customer("testCustomer", "testCustomer@email.com", "123")
                .setId("customer123");

        Payment event = new Payment("400", "USD", mockCustomer.getId());
        Balance balance = new Balance(mockCustomer.getId(), "500", "0");

        mockCustomer.setCustomerBalance(balance);
        LogCaptor logCaptor = LogCaptor.forClass(BalanceConsumer.class);

        customerRepository.save(mockCustomer);

        Mockito
                .when(customerRepository
                        .findById(anyString()))
                .thenReturn(Optional.of(mockCustomer));

        balanceConsumer.consumeLowerBalanceEvent(event);

        Mockito
                .verify(customerRepository, times(1))
                .save(any(Customer.class));

        assertThat(logCaptor.getInfoLogs().get(0))
                .startsWith("Payment accepted and balance lowered for customer:");
    }

    @Test
    void lowerBalance_customerDoesntExist() {
        Payment payment = new Payment("400", "USD", "nonExistingCustomerId");
        Mockito.when(customerRepository.findById(payment.getCustomerId()))
                .thenReturn(Optional.empty());

        LogCaptor logCaptor = LogCaptor.forClass(BalanceConsumer.class);

        balanceConsumer.consumeAddBalanceEvent(payment);

        Mockito.verify(customerRepository, Mockito.never())
                .save(any(Customer.class));

        assertThat(logCaptor.getWarnLogs().get(0))
                .startsWith("Customer not found");
    }

    @Test
    void lowerBalance_noBalance() {
        Customer mockCustomer = new Customer("testCustomer", "testCustomer@email.com", "123")
                .setId("customer123");

        Payment event = new Payment("400", "USD", mockCustomer.getId());

        LogCaptor logCaptor = LogCaptor.forClass(BalanceConsumer.class);

        customerRepository.save(mockCustomer);

        Mockito
                .when(customerRepository
                        .findById(anyString()))
                .thenReturn(Optional.of(mockCustomer));

        balanceConsumer.consumeLowerBalanceEvent(event);

        Mockito
                .verify(customerRepository, times(2))
                .save(any(Customer.class));

        assertThat(logCaptor.getInfoLogs().get(0))
                .startsWith("Payment rejected due to no balance for customer:");
    }

    @Test
    void lowerBalance_notEnoughFund() {
        Customer mockCustomer = new Customer("testCustomer", "testCustomer@email.com", "123")
                .setId("customer123");

        Payment event = new Payment("700", "USD", mockCustomer.getId());
        Balance balance = new Balance(mockCustomer.getId(), "500", "0");

        mockCustomer.setCustomerBalance(balance);
        LogCaptor logCaptor = LogCaptor.forClass(BalanceConsumer.class);

        customerRepository.save(mockCustomer);

        Mockito
                .when(customerRepository
                        .findById(anyString()))
                .thenReturn(Optional.of(mockCustomer));

        balanceConsumer.consumeLowerBalanceEvent(event);

        Mockito
                .verify(customerRepository, times(1))
                .save(any(Customer.class));

        assertThat(logCaptor.getInfoLogs().get(0))
                .startsWith("insufficient funds.");
    }

    @Test
    void consumeNewCustomerBalance_noBalance() {
        Customer mockCustomer = new Customer("testCustomer", "testCustomer@email.com", "123")
                .setId("customer123");

        LogCaptor logCaptor = LogCaptor.forClass(BalanceConsumer.class);

        customerRepository.save(mockCustomer);

        Mockito
                .when(customerRepository
                        .findById(anyString()))
                .thenReturn(Optional.of(mockCustomer));

        balanceConsumer.consumeNewCustomerBalance(mockCustomer);

        Mockito
                .verify(customerRepository, times(2))
                .save(any(Customer.class));

        assertThat(logCaptor.getInfoLogs().get(0))
                .startsWith("New customer balance initialized for customer:");
    }


    @Test
    void consumeNewCustomerBalance_badCustomer() {
        Customer mockCustomer = new Customer("testCustomer", "testCustomer@email.com", "123")
                .setId("nonExistingCustomerId");

        LogCaptor logCaptor = LogCaptor.forClass(BalanceConsumer.class);

        balanceConsumer.consumeNewCustomerBalance(mockCustomer);

        Mockito
                .verify(customerRepository, times(0))
                .save(any(Customer.class));

        assertThat(logCaptor.getWarnLogs().get(0))
                .startsWith("New customer not found for initialization:");
    }
}

package com.bysellens.customer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bysellens.common.DomainException;

class CustomerServiceTest {
    private ClienteRepository repository;
    private CustomerService service;

    @BeforeEach
    void setUp() {
        repository = mock(ClienteRepository.class);
        service = new CustomerService(repository);
    }

    @Test
    void deleteKeepsTheCustomerAndMarksItInactive() {
        var customer = new Cliente();
        customer.id = 42L;
        customer.activo = true;
        when(repository.findById(42L)).thenReturn(Optional.of(customer));

        service.delete(42L);

        assertFalse(customer.activo);
        verify(repository).save(customer);
        verify(repository, never()).delete(customer);
    }

    @Test
    void getReportsMissingCustomersAsNotFound() {
        when(repository.findById(404L)).thenReturn(Optional.empty());

        var error = assertThrows(DomainException.class, () -> service.get(404L));

        assertEquals(404, error.status);
    }
}

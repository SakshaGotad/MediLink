package com.medilink.payment.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.medilink.payment.Entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, String> {
}
package com.huuloc.bookstore.bbook.repository.custom;

import com.huuloc.bookstore.bbook.exception.BadRequestException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public class CustomOrderRepositoryImpl implements CustomOrderRepository {
    @PersistenceContext
    private EntityManager entityManager;
    private static final String CHECKOUT_ORDER_PROCEDURE = "checkout_order";
    private static final String CANCEL_ORDER_PROCEDURE = "cancel_order";

    public void checkoutOrder(Long orderId, Long addressId, String paymentType) {
        StoredProcedureQuery storedProcedureQuery = entityManager
                .createStoredProcedureQuery(CHECKOUT_ORDER_PROCEDURE)
                .registerStoredProcedureParameter("order_id", Long.class, ParameterMode.IN)
                .registerStoredProcedureParameter("address_id", Long.class, ParameterMode.IN)
                .registerStoredProcedureParameter("payment_type", String.class, ParameterMode.IN)
                .setParameter("order_id", orderId)
                .setParameter("address_id", addressId)
                .setParameter("payment_type", paymentType);

        storedProcedureQuery.execute();
    }

    public void cancelOrder(Long orderId) {
        StoredProcedureQuery storedProcedureQuery = entityManager
                .createStoredProcedureQuery(CANCEL_ORDER_PROCEDURE)
                .registerStoredProcedureParameter("order_id", Long.class, ParameterMode.IN)
                .setParameter("order_id", orderId);

        storedProcedureQuery.execute();
    }
}

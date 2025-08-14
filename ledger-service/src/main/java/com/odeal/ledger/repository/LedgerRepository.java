package com.odeal.ledger.repository;

import com.odeal.ledger.entity.Ledger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LedgerRepository extends JpaRepository<Ledger, UUID> {
    Optional<Ledger> findByMerchantId(UUID merchantId);
    List<Ledger> findByBalanceGreaterThan(BigDecimal amount);
}

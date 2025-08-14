package com.odeal.payout.feign;

import com.odeal.payout.dto.LedgerDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "ledger-service", url = "http://localhost:8080/api/ledgers")
public interface LedgerServiceClient {

    @GetMapping("/payable")
    List<LedgerDTO> getPayableLedgers();
}

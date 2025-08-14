package com.odeal.payout.feign;

import com.odeal.payout.dto.MerchantDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "merchant-service", url = "http://localhost:8083/api/merchants")
public interface MerchantServiceClient {

    @GetMapping("/{id}")
    MerchantDTO getMerchantById(@PathVariable("id") UUID id);
}

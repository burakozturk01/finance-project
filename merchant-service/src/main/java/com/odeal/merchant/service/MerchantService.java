package com.odeal.merchant.service;

import com.odeal.merchant.entity.Merchant;
import com.odeal.merchant.repository.MerchantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class MerchantService {
    
    @Autowired
    private MerchantRepository merchantRepository;
    
    public Merchant createMerchant(Merchant merchant) {
        return merchantRepository.save(merchant);
    }
    
    public Optional<Merchant> getMerchantById(UUID id) {
        return merchantRepository.findById(id);
    }
}

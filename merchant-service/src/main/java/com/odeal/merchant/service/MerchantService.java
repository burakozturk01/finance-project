package com.odeal.merchant.service;

import com.odeal.merchant.entity.Merchant;
import com.odeal.merchant.repository.MerchantRepository;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public List<Merchant> getAllMerchants() {
        return merchantRepository.findAll();
    }

    public Merchant updateMerchant(UUID id, Merchant merchant) {
        Optional<Merchant> merchantOptional = merchantRepository.findById(id);
        if (merchantOptional.isPresent()) {
            Merchant existingMerchant = merchantOptional.get();
            if (merchant.getName() != null) {
                existingMerchant.setName(merchant.getName());
            }
            if (merchant.getIban() != null) {
                existingMerchant.setIban(merchant.getIban());
            }
            return merchantRepository.save(existingMerchant);
        }
        return null;
    }

    public void deleteMerchant(UUID id) {
        merchantRepository.deleteById(id);
    }
}

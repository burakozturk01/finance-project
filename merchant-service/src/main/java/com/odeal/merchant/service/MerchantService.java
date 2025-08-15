package com.odeal.merchant.service;

import com.odeal.merchant.entity.Merchant;
import com.odeal.merchant.repository.MerchantRepository;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MerchantService {
    
    @Autowired
    private MerchantRepository merchantRepository;
    
    public Merchant createMerchant(Merchant merchant) {
        merchant.setCreatedAt(LocalDateTime.now());
        return merchantRepository.save(merchant);
    }
    
    public Optional<Merchant> getMerchantById(UUID id) {
        return merchantRepository.findById(id);
    }

    public List<Merchant> getAllMerchants() {
        return merchantRepository.findAll();
    }

    public Optional<Merchant> getMerchantByNameEmailIban(String name, String email, String iban) {
        return merchantRepository.findByNameAndEmailAndIban(name, email, iban);
    }

    public Merchant updateMerchant(UUID id, Merchant merchantDetails) {
        Optional<Merchant> merchantOptional = merchantRepository.findById(id);
        if (merchantOptional.isPresent()) {
            merchantRepository.deleteById(id);
            Merchant newMerchant = new Merchant(merchantDetails.getName(), merchantDetails.getEmail(), merchantDetails.getIban());
            newMerchant.setCreatedAt(merchantOptional.get().getCreatedAt());
            return merchantRepository.save(newMerchant);
        }
        return null;
    }

    public void deleteMerchant(UUID id) {
        merchantRepository.deleteById(id);
    }
}

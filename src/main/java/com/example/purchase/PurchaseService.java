package com.example.purchase;

import com.example.buyer.Buyer;
import com.example.buyer.BuyerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final BuyerRepository buyerRepository;
    //구매하기?
    public Purchase findByproductId(Integer productId) {
        Purchase purchase = purchaseRepository.findByProductId(productId);
        return purchase;
    }

    //구매하기?
    @Transactional
    public void savePurchase(Integer buyerId, PurchaseRequest.SaveDTO reqDTO){
        Buyer buyer = buyerRepository.findByBuyerId(buyerId);
        purchaseRepository.save(buyerId, buyer.getBuyerName(), reqDTO);
    }
}

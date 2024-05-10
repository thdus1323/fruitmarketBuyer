package com.example.purchase;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class PurchaseRepository {
    private final EntityManager em;

    //구매결정?구매하기?
    public Purchase findByProductId(Integer productId) {
        Query query = em.createNativeQuery("select * from purchase_tb where product_id=?", Purchase.class);
        query.setParameter(1, productId);
        return (Purchase)query.getSingleResult();
    }

    public void save(Integer buyerId, String buyerName, PurchaseRequest.SaveDTO reqDTO) {
        String q = """
                insert into purchase_tb(buyer_id, buyer_name, product_id, product_name, product_price, product_qty, pur_qty, created_at)
                values(?,?,?,?,?,?,?,now())
                """;

        Query query = em.createNativeQuery(q);
        query.setParameter(1, buyerId);
        query.setParameter(2, buyerName);
        query.setParameter(3, reqDTO.getProductId());
        query.setParameter(4, reqDTO.getProductName());
        query.setParameter(5, reqDTO.getProductPrice());
        query.setParameter(6, reqDTO.getPurQty());
        query.setParameter(7, reqDTO.getPurQty());

        query.executeUpdate();
    }

    //구매수량수정하기
    public void updateByBuyerId(Integer buyerId, PurchaseRequest.UpdateDTO reqDTO) {
    Query query = em.createNativeQuery("update purchase_tb set purchase_qty=? where buyer_id=?");
    query.setParameter(1, reqDTO.getPurchaseQty());
    query.setParameter(2, buyerId);
    query.executeUpdate();
    }
}

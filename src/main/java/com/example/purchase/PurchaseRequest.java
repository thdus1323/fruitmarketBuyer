package com.example.purchase;

import lombok.Data;

public class PurchaseRequest {
    @Data
    public static class SaveDTO {
        private String buyerName;
        private Integer productId;
        private String productName;
        private Integer productPrice;
        private Integer productQty;
        private Integer purQty;

    }

    @Data
    public static class UpdateDTO {
        private Integer purchaseQty;
    }
}

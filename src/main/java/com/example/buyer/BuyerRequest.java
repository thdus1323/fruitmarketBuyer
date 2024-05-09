package com.example.buyer;

import lombok.Data;

@Data
public class BuyerRequest {

    @Data
    public class JoinDTO {
        private String buyerName;
        private String buyerPw;
        private String buyerEmail;
    }
}

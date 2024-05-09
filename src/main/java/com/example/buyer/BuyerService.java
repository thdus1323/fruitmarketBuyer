package com.example.buyer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BuyerService {
    @Autowired
    private BuyerRepository buyerRepository;

    //회원가입
    @Transactional
    public void joinByNameAndPwAndEmail(BuyerRequest.JoinDTO reqDTO) {
        buyerRepository.join(reqDTO);
    }

}

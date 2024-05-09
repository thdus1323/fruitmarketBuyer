package com.example.buyer;

import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BuyerService {
    private final BuyerRepository buyerRepository;

    //회원가입
    @Transactional
    public void joinByNameAndPwAndEmail(BuyerRequest.JoinDTO reqDTO) {
        buyerRepository.join(reqDTO);
    }

    //로그인
    public Buyer loginByNameAndPw(BuyerRequest.LoginDTO reqDTO) {
       Buyer sessionBuyer = buyerRepository.login(reqDTO);
       return sessionBuyer;
    }
}

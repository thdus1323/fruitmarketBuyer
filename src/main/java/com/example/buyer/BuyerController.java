package com.example.buyer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class BuyerController {
    private final BuyerService buyerService;

    //회원가입
    @PostMapping("join")
    public String join(BuyerRequest.JoinDTO reqDTO){
        buyerService.joinByNameAndPwAndEmail(reqDTO);
        System.out.println("회원가입 : " + reqDTO);
        return "redirect:join-form"; // 로그인 폼 구현 후 수정
    }

    @GetMapping({"join-form","/"})
    public String joinForm(){
        return "buyer/join-form";
    }
}

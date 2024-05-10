package com.example.purchase;

import com.example.buyer.Buyer;
import com.example.product.Product;
import com.example.product.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class PurchaseController {
    private final PurchaseService purchaseService;
    private final HttpSession session;

    //상품 구매하기

    //상품 구매결정하기 폼
    @GetMapping("/purchase/{productId}/purchase-form")
    public String purchaseForm(@PathVariable Integer productId, HttpServletRequest request){
        Purchase purchase = purchaseService.findByproductId(productId);
        request.setAttribute("purchase", purchase);
        return "purchase/purchase-form";
    }

    //상품 구매결정하기
    @PostMapping("/purchase/save")
    public String saveByBuyerId(PurchaseRequest.SaveDTO reqDTO){
        Buyer sessionBuyer = (Buyer) session.getAttribute("sessionBuyer");
        purchaseService.savePurchase(sessionBuyer.getBuyerId(), reqDTO);
        return "redirect:/purchase/list";
    }

    //구매수량 수정하기
    @PostMapping("/purchase/{purchaseId}/update")
    public String update(@PathVariable Integer purchaseId, PurchaseRequest.UpdateDTO reqDTO){
        purchaseService.changePurQty(purchaseId, reqDTO);
        return "redirect:/";
    }
}

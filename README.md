# [ 들어가기 전, 틀 세팅 및 구성하기 ]

## 1. 프로젝트 생성 및 의존성 설정

1.   프로젝트 생성 : Java 21
2. 의존성 설정
    - lombok
    - spring boot devtools
    - spring web
    - spring data JPA
    - MYSQL Driver
    - Mustache

## 2. 야물 설정_MYSQL 연결

## 3. 테이블 생성

1. 구매자테이블(buyer_tb)
    1. Buyer
    2. Buyercontroller
    3. Buyerrepository
    4. BuyerRequest
    5. BuyerService

1. 상품테이블(product_tb)
    1. Product
    2. ProductController
    3. ProductRepository
    4. ProductResponse
    5. ProductService

1. 구매테이블(purchase_tb)
    1. Purchase
    2. PurchaseController
    3. PurchaseRepository
    4. PurchaseRequest
    5. PurchaseResponse
    6. PurchaseService

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/fadaf8f9-4b09-4f3d-9e72-038ace21b86b/373e3dd3-6f32-4542-a073-a1011d4039c9/Untitled.png)

[ 기능 ]

1. 구매자 회원가입

1) 기능 구현 전, 기초 틀인 yml, Board, mustache 모두 설정하고 화면 확인 후 시작.

2) BuyerRequest

```java
public class BuyerRequest {

 @Data
 public static class JoinDTO {
		private String buyerName;
		private String buyerPw;
		private String buyerEmail;
	}
}
```

3) BuyerRepository : db에 입력되는 쿼리 입력

```java
//회원가입
public void join(BuyerRequest.JoinDTO reqDTO) {
	Query query = em.createNativeQuery(
		"""
		insert into buyer_tb(buyer_name, buyer_pw, buyer_email, created_at) values(?,?,?,now())
		""");
	query.setParameter(1, reqDTO.getBuyerName());
	query.setParameter(2, reqDTO.getBuyerPw());
	query.setParameter(3, reqDTO.getBuyerEmail());
	query.executeUpdate();
}
```

---

4) BuyerService : 클래스에 정의된 join 메서드를 호출하여 회원가입 정보를 데이터베이스에 저장합니다. 이 메서드는 reqDTO 객체를 매개변수로 받아 사용자의 이름, 비밀번호, 이메일 정보를 데이터베이스에 저장합니다.

```java
//회원가입
@Transactional
public void joinByNameAndPwAndEmail(BuyerRequest.JoinDTO reqDTO){
buyerRepository.join(reqDTO);
}
```

---

+) db에 저장하는 것은 레파지토리가 하는 것. 서비스는 db를 통해 db와 일처리

일반적으로 서비스 계층은 직접적으로 데이터베이스와 상호작용하지 않습니다. 대신, 서비스 계층은 리포지토리 계층을 통해 데이터베이스 작업을 처리합니다. 서비스 계층의 주요 역할은 비즈니스 로직을 처리하고, 리포지토리 계층을 사용하여 데이터 저장 및 검색 작업을 수행하는 것입니다.

앞서 제시한 코드에서 서비스 계층이 데이터베이스에 저장하는 것처럼 보이는 이유는 서비스 계층이 리포지토리 계층을 통해 데이터를 저장하도록 호출하기 때문입니다. 아래에 좀 더 명확한 예시를 제공하겠습니다:

5) BuyerController : 회원가입 뷰로 가능 기능, reqDTO를 통해 join 정보 가져오기

```java
//회원가입

@PostMapping("/join")
public String join(BuyerRequest.JoinDTO reqDTO) {
	buyerService.joinByNameAndPwAndEmail(reqDTO);
	System.out.println("회원가입 : " + reqDTO);
	return "redirect:/login-form"; // 나중에 login-form으로
}

@GetMapping("/join-form")
	public String joinForm() {
	return "buyer/join-form";
}
```

---

6)join-form.mustache [뷰] : action, 메서드, 기본 폼, 내용

```java
<form action="/join" method="post" enctype="application/x-www-form-urlencoded">

		<div class="mb-3">
		<input id="buyerName" type="text" class="form-control"
		placeholder="Enter buyerName"
		name="buyerName" value="ssar">
		</div>
		
		<div class="mb-3">
		<input type="buyerPw" class="form-control" placeholder="Enter password"
		name="buyerPw" value="1234">
		</div>
		
		<div class="mb-3">
		<input type="buyerEmail" class="form-control" placeholder="Enter email"
		name="buyerEmail" value="ssar@nate.com">
		</div>
		
	<button type="submit" class="btn btn-primary form-control">회원가입</button>

</form>
```

---

- 해당 HTML 폼은 사용자가 "회원가입" 버튼을 누르면 /join URL로 HTTP POST 요청을 보내고, 폼에 입력된 buyerName, buyerPw, buyerEmail 값을 서버로 전송합니다. 서버는 이 요청을 받아서 처리하게 됩니다.(from gpt)

2.구매자 로그인/로그아웃

1) 로그인

(1) BuyerRepository : 로그인에 필요한 정보(이름, 패스워드)를 DTO에 담아 요청하고

그것을 db에서 가져옴.

```java
//로그인
public Buyer login(BuyerRequest.LoginDTO reqDTO) {
		Query query = em.createNativeQuery
					("select * from buyer_tb where buyer_name=?
						and buyer_pw=?", Buyer.class);
		query.setParameter(1, reqDTO.getBuyerName());
		query.setParameter(2, reqDTO.getBuyerPw());
		Buyer sessionBuyer = (Buyer) query.getSingleResult();
// 데이터베이스랑 자바 세상은 다르니까 데이터베이스의 자료를 자바로 가져와야 하니까 형변환
		
		return sessionBuyer;
}
```

---

(2) BuyerService : 로그인에 필요한 이름, 패스워드를 요청 디티오에 담아서 세션에 저장.

```java
//로그인
public Buyer loginByNameAndPw(BuyerRequest.LoginDTO reqDTO){
Buyer sessionBuyer = buyerRepository.login(reqDTO);
return sessionBuyer;
}
```

---

(3) BuyerController : 로그인 뷰 기능, 로그인 정보를 세션에 담아 DTO를 통해 요청

- BuyerRequest

```java
@Data
public static class LoginDTO {
private String buyerName;
private String buyerPw;
}
```

---

```java
@PostMapping("login")
public String login(BuyerRequest.LoginDTO reqDTO) {
Buyer sessionBuyer = buyerService.loginByNameAndPw(reqDTO);
session.setAttribute("sessionBuyer", sessionBuyer);
return "redirect:/";
}

@GetMapping("/login-form")
public String loginForm() {
return "buyer/login-form";
}
```

---

2) 로그아웃

(1) BuyerController : 로그아웃하면 세션 무효화

```java
@GetMapping("/logout")
public String logout(){
	session.invalidate();
	return "redirect:/";
}
```

---

1. **상품 목록보기**

1) ProductRepository : db에서 상품목록을 가져오는 쿼리 작성.

그 리스트는 1개가 아니니까 getResultList로 반환받는다.

```java
public List<Product> findAll() {
Query query = em.createNativeQuery("select * from product_tb
order by product_id desc",Product.class);
return query.getResultList();
}
```

---

- ProductResponse

```java
public class ProductResponse {

@Data
public static class ListDTO {
private Integer productId;
private String productName;
private Integer productPrice;
private Integer productQty;

public ListDTO(Product product) {
this.productId = product.getProductId();
this.productName = product.getProductName();
this.productPrice = product.getProductPrice();
this.productQty = product.getProductQty();
}

}
```

---

2) ProductService : 요청한 해당 목록을 가져오는 건데, 레파지토리에서 해당 목록을

list 타입으로 다 가져온다. 그런데 db와 java는 다른 언어로, 데이터의 형식이 다를 수 있어

db의 정보를 바로 java에서 쓸 수 없다. 그래서 db에서 가져온 정보들을 바로 가져가지

않고 스트림에 뿌려서 알기 쉽게 가공을 하고 다시 리스트에 담아서 그것을 응답해줌.

```java
 public List<ProductResponse.ListDTO> getProductList(){
        List<Product> productList = productRepository.findAll();
        return productList.stream().map(ProductResponse.ListDTO::new)
                .collect(Collectors.toList());
    }
```

+) gpt 도움_ReadMe에서는 삭제-v(혹은 람다버전 수정, 사용해라.)

```java
일반적으로 데이터베이스에서 가져온 데이터는 
애플리케이션에서 사용되는 데이터 형식과 다를 수 있습니다. 
이러한 경우, 데이터를 가져와서 원하는 형식으로 변환하여 사용해야 합니다. 
스트림을 사용하여 데이터를 가져와 변환하는 것은 
이러한 작업을 효율적으로 처리하기 위한 방법 중 하나입니다.

여기서는 데이터베이스에서 가져온 Product 객체를 
ProductResponse.ListDTO로 변환하여 반환해야합니다. 
이 변환 작업은 데이터의 형식을 맞추는 역할을 합니다. 
데이터베이스의 스키마가 애플리케이션의 데이터 구조와 다르기 때문에 
이러한 변환이 필요합니다.
```

```java

ProductResponse.ListDTO::new는 Java 8에서 도입된 메서드 레퍼런스(Method Reference)입니다.
이것은 람다 표현식을 간결하게 표현하기 위한 것입니다.

ProductResponse.ListDTO::new는 ProductResponse.ListDTO 클래스의 생성자를 참조합니다. 
이것은 각 Product 객체를 ProductResponse.ListDTO로 매핑하기 위한 방법을 제공합니다.

기존에는 람다 표현식으로 다음과 같이 작성할 수 있었습니다:

java))코드 복사
return productList.stream()
        .map(product -> new ProductResponse.ListDTO(product))
        .collect(Collectors.toList());
-------------------------------------------------------------
하지만 메서드 레퍼런스를 사용하면 이것을 더 간결하게 표현할 수 있습니다. 
이 문법은 람다 표현식에서 메서드 호출을 대체하는 것으로,
:: 뒤에는 메서드의 이름이 오고, 이는 해당 클래스나 인터페이스의 정적 메서드이거나 
인스턴스 메서드일 수 있습니다. 여기서는 생성자를 호출하기 때문에 new 키워드를 사용합니다.

따라서 ProductResponse.ListDTO::new는 ProductResponse.ListDTO 클래스의 생성자를 
참조하는 메서드 레퍼런스입니다.
```

---

3) ProductController : 문자열 리스트로 반환하는 메서드에 목록 요청이 들어오면,

list 목록이 응답 된다.

```java
@GetMapping({"/product","/"})
public String list(HttpServletRequest request){
List<ProductResponse.ListDTO> productList = productService.getProductList();
request.setAttribute("productList", productList);
return "product/list";
}
```

---

## 상품 상세보기

1)ProductController : 상품아이디에 해당하는 정보들을 얻고 싶을 때 그에 해당하는 요청을 하면, 서비스에서 해당 상품 디테일을 받아오는데 그것을 detail dto에 담는다.

```java
 @GetMapping("/product/{productId}/detail")
    public String detail(@PathVariable Integer productId, HttpServletRequest request){
        ProductResponse.DetailDTO product = productService.getProductDetail(productId);
        request.setAttribute("product",product);
        return "product/detail";
    }
```

+) gpt 도움_ReadMe에서는 삭제-v

```java
@PathVariable은 웹 주소의 일부를 변수로 사용할 때 쓰는 거예요. 
예를 들어, /product/1/detail 이 주소에서 1은 제품 번호를 나타냅니다. 
이걸 받아와서 코드에서 사용하려면 @PathVariable을 씁니다. 
그럼 제품 번호가 메서드 안으로 자동으로 들어와서 처리할 수 있어요!
```

```java

request.setAttribute("product", product);
은 서버에서 클라이언트로 데이터를 전달하는 데 사용됩니다. 
이 코드는 HttpServletRequest 객체에 product라는 이름으로 product 변수를 설정합니다. 
이렇게 하면 해당 정보가 현재 요청과 관련된 다른 부분에서 사용할 수 있습니다.

예를 들어, 이 정보는 HTML 페이지에서 제품 세부 정보를 표시하는 데 사용될 수 있습니다. 
뷰 템플릿에서 ${product}와 같은 방식으로 이 정보에 접근하여 
제품의 세부 정보를 출력할 수 있습니다.

즉, 이 코드는 제품의 세부 정보를 서버에서 클라이언트로 전달하고, 
클라이언트의 다른 부분에서 이 정보를 사용할 수 있도록 합니다.

질문) 그러면 이 부분은 그냥 여러 곳에서 사용할 수 있게 쉽게 명명하는 작업이야?

ChatGPT 답) 
네, 맞아요! request.setAttribute("product", product);은 제품 정보를 "product"라는 이름으로
 저장하는 작업이에요. 이렇게 하면 이 정보를 여러 곳에서 쉽게 사용할 수 있어요. 
 예를 들어, 웹 페이지에서 제품 정보를 보여주거나, 
 다른 서비스나 모듈에서 이 정보를 활용할 때 유용합니다. 
 이름을 "product"로 명명함으로써, 다른 부분에서 이 정보를 찾고 사용하기가 편리해집니다.
```

2) ProductService : 상품아이디로 상세보기를 dto에 담아올거다. 레파지토리에서 정보를 가져올 거다. 그래서 새로운 detaildto로 응답을 받을 거다.

```java
   public ProductResponse.DetailDTO getProductDetail(Integer productId){
        Product product = productRepository.findById(productId);
        return new ProductResponse.DetailDTO(product);
    }
```

3) ProductRepository : db에서 상품 아이디에 대한 정보를 가져올거다.

```java
 public Product findById(int productId){
        Query query = em.createNativeQuery("select * from product_tb where product_id=?", Product.class);
        query.setParameter(1,productId);
        return (Product) query.getSingleResult();
    }
```

## 구매가 되면, 상품의 재고 수정이 필요함(qty가 줄어야 함.)

1) PurchaseController : 상품id pk와 수정요청을 한다. 서비스에 해당로직을 신청.메인이 반환된다.

```java
 @PostMapping("/purchase/{purId}/update")
    public String update(@PathVariable Integer purId, PurchaseRequest.UpdateDTO reqDTO) {
        purchaseService.changePurQty(purId, reqDTO);
        return "redirect:/";
    }
```

2) PurchaseService : 구매자pk와 업데이트 디티오를 요청을 하고, 레파지토리에 요청을 한다.-v

```java
   @Transactional
    public void changePurQty(Integer buyerId, PurchaseRequest.UpdateDTO reqDTO){
        purchaseRepository.updateById(buyerId, reqDTO);
    }
```

3)PurchaseRepository : db에 업데이트 요청 쿼리 작성

```java
 public void updateQty(PurchaseRequest.SaveDTO reqDTO){
        String q = """
                update product_tb set product_qty = product_qty - ? where product_id = ?
                """;
        Query query = em.createNativeQuery(q);
        query.setParameter(1, reqDTO.getPurQty());
        query.setParameter(2, reqDTO.getProductId());
        query.executeUpdate();
    }
```

## 구매가 되면, order 테이블에 구매 목록이 insert 되어야 함 & 구매 목록 보기 기능이 필요함.

1) 나의 구매 목록 보기

(1) PurchaseController : 세션에 있는 buyer가 요청을 하면, 그 사람 구매목록만 나오게 함

-session을 활용하여 그 사람인지 인식하고 해당 사람이 산 구매리스트(1사람이 여러 물품을 살 수 있다.)를 반환하도록 한다.

```java
 @GetMapping("/purchase/list")
    public String list(HttpServletRequest request, HttpSession session) {
        //list에는 구매한 사람만 나오게
        Buyer sessionBuyer = (Buyer) session.getAttribute("sessionBuyer");
        List<Purchase> purchaseList = purchaseService.getPurchaseList(sessionBuyer.getBuyerId());
        request.setAttribute("purchaseList", purchaseList);
        return "purchase/list";
    }
```

(2) PurchaseService : 로그인을 먼저 했기 때문에 레파지토리에 해당 buyerid를 찾아보면 된다. 해당 buyterId를 요청해보고 그 정보가 있는지 확인해야 한다. 그리고 있다면 그 사람의 purchaselist를 반환하도록 한다.

```java
 public List<Purchase> getPurchaseList(Integer buyerId){
        List<Purchase> purchaseList = purchaseRepository.findByBuyerId(buyerId);
        return purchaseList;
    }
```

(3) PurchaseRepository : buyerid의 구매목록(리스트)을 조회하는 쿼리를 짠다.

```java
public List<Purchase> findByBuyerId(Integer buyerId) {
        String q = """
                select * from purchase_tb where buyer_id = ?
                """;
        Query query = em.createNativeQuery(q, Purchase.class);
        query.setParameter(1, buyerId);
        List<Purchase> purchaseList = query.getResultList();
        return purchaseList;
    }
```

2) 나의 구매목록에 새로운 정보 저장 : 새로 구입하는 물품들이 있을 수 있기에 해당 경우,그것들을 db에 저장하는 쿼리를 짠다.

```java
public void save(Integer buyerId, String buyerName,  PurchaseRequest.SaveDTO reqDTO) {
        String q = """
                insert into purchase_tb(buyer_id, buyer_name, product_id, product_name, product_price, product_qty, pur_qty, created_at) 
                values (?, ?, ?, ?, ?, ?, ?, now())
                """;

        Query query = em.createNativeQuery(q);
            query.setParameter(1, buyerId);
            query.setParameter(2, buyerName);
            query.setParameter(3, reqDTO.getProductId());
            query.setParameter(4, reqDTO.getProductName());
            query.setParameter(5, reqDTO.getProductPrice());
            query.setParameter(6, reqDTO.getProductQty());
            query.setParameter(7, reqDTO.getPurQty());

            query.executeUpdate();
    }
```

3) 내 구매에 따른 상품재고 수량 변경 : 구매수량에 따른 상품재고는 차감이 되어야 하기에 해당 뺄셈 쿼리를 짠다.

```java
public void updateQty(PurchaseRequest.SaveDTO reqDTO){
        String q = """
                update product_tb set product_qty = product_qty - ? where product_id = ?
                """;
        Query query = em.createNativeQuery(q);
        query.setParameter(1, reqDTO.getPurQty());
        query.setParameter(2, reqDTO.getProductId());
        query.executeUpdate();
    }
```

## 그 밖의 기능들

-구매수량 수정 : 구매수량을 변경하는 로직.

구매 id(pk)와 구매요청을 하면 문자열로 업데이트가 된다. 

자세하게는 서비스에 수량변경요청이 가고 요청 후에는 메인으로 반환 된다.

```java
@PostMapping("/purchase/{purId}/update")
    public String update(@PathVariable Integer purId, PurchaseRequest.UpdateDTO reqDTO) {
        purchaseService.changePurQty(purId, reqDTO);
        return "redirect:/";
    }
```

-상품 구매 결정 : 상품 구매와 함께 로그인한 정보로 구매 정보가 넘어가는 것이 핵심.

구매 요청을 buyersession에 저장되어 있는 id, name에 저장.

그래서 해당 buyerid의 구매만 나오게 하는 것이 핵심.

```java
@PostMapping("/purchase/save")
    public String saveByBuyerName(PurchaseRequest.SaveDTO reqDTO) {
        Buyer sessionBuyer = (Buyer) session.getAttribute("sessionBuyer");

        System.out.println("상품 구매하기 디티오 : " + reqDTO);
        System.out.println("출력되는지 확인 중입니다. 세션유저 : " + sessionBuyer.getBuyerId() + sessionBuyer.getBuyerName());

        purchaseService.savePurchase(sessionBuyer.getBuyerId(), reqDTO);

        return "redirect:/purchase/list";
    }
```

⭐DB와 자바 연결이 중요.

**-product 클래스의 필드 값들이 DB에서는 tb의 열이 된다.**

난이도 상🔝) sessionBuyerId의 구매 목록을 조회하는 것이 핵심.

-로그인 했던 id의 session을 이용하여 식별 및 관련 내용 추출 + 구매목록 조회

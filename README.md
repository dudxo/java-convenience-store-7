# java-convenience-store-precourse

## 🗣️ 편의점

- 환영 인사와 함께 상품명, 가격, 프로모션 이름, 재고를 안내한다. 만약 재고가 0개라면 `재고 없음`을 출력한다.
- 구매자의 할인 혜택과 재고 상황을 고려하여 최종 결제 금액을 계산하고 안내하는 결제 시스템을 구현한다.
- 사용자가 입력한 상품의 가격과 수량을 기반으로 최종 결제 금액을 계산한다.
    - 총구매액은 상품별 가격과 수량을 곱하여 계산하며, 프로모션 및 멤버십 할인 정책을 반영하여 최종 결제 금액을 산출한다.
- 구매 내역과 산출한 금액 정보를 영수증으로 출력한다.
- 영수증 출력 후 추가 구매를 진행할지 또는 종료할지를 선택할 수 있다.
- 프로모션은 N개 구매 시 1개 무료 증정(Buy N Get 1 Free)의 형태로 진행된다.
- 프로모션 혜택은 프로모션 재고 내에서만 적용한다.

## 📜 기능 목록 정리

### 1. 재고 관리

- [X] 파일 입출력을 통해 상품 목록(`products.md`)과 행사 목록(`promotions.md`)에서 재고 정보를 불러온다.
- [X] 고객이 상품을 구매할 때마다, 결제된 수량만큼 해당 상품의 재고에서 차감하여 정확환 수량을 관리한다.
- [X] 동일 상품에 여러 프로모션이 적용되었는지 검증한다.

### 2. 프로모션 할인

- [X] 오늘 날짜가 프로모션 기간 내에 포함되었는지 확인한다.
- [X] 프로모션 적용이 가능한 수량인지 확인한다.
- [X] 프로모션 기간 중이라면 프로모션 재고를 우선적으로 차감한다.
- [X] 구매 수량과 프로모션 수량을 비교한다.
- [X] 구매 수량이 프로모션 수량보다 많다면 프로모션 헤택 미적용 여부를 확인한다.
- [X] 구매 수량이 프로모션 적용 수량보다 적다면 추가 구매 여부를 확인한다.
- [X] 프로모션 혜택 미적용 구매를 진행한다면 일반 재고를 판매한다.

### 3. 멤버십 할인

- [X] 프로모션 미적용 금액의 30%의 할인 금액을 계산한다.
- [X] 프로모션 적용 후 남은 금액에 대해 멤버십 할인을 적용한다.
- [X] 멤버십 할인의 최대 한도는 8,000원이다.

### 4. 영수증

- [X] 고객의 구매 내역과 할인을 요약한 정보를 만든다. 상세 항목은 다음과 같다.
    - 영수증 항목
        - 구매 상품 내역: 구매한 상품명, 가격, 수량
        - 증정 상품 내역: 프로``모션에 따라 무료로 제공된 증정 상품의 목록
        - 금액 정보
            - 총구매액: 구매한 상품의 총 수량과 총 금액
            - 행사할인: 프로모션에 의해 할인된 금액
            - 멤버십할인: 멤버십에 의해 추가로 할인된 금액
            - 내실돈: 최종 결제 금액
- [X] 영수증의 고객이 쉽게 금액과 수량을 확인할 수 있도록 구성요소를 보기 쉽게 정렬한다.

### 5. 입력 처리

- [X] 구매할 상품과 수량을 입력 받는다.
    - 상품명, 수량은 하이픈(-)으로, 개별 상품은 대괄호([])로 묶어 쉼표(,)로 구분한다.
- [X] 프로모션 적용이 가능한 상품에 대해 고객이 해당 수량보다 적게 가져온 경우, 그 수량만큼 추가 구입 여부를 입력받는다.
- [X] 프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야 하는 경우, 일부 수량에 대해 정가로 결제할지 여부를 입력 받는다.
- [x] 멤버십 할인 적용 여부를 입력 받는다.
- [X] 추가 구매 여부를 입력 받는다.

### 5. 출력 처리

- [X] 환영 인사와 함께 상품명, 가격, 프로모션 이름, 재고를 출력한다.
    - 만약 재고가 0개라면 `재고 없음`을 출력한다.
- [X] 프로모션 적용이 가능한 상품에 대해 고객이 해당 수량보다 적게 가져온 경우, 그 수량만큼 추가 구입 여부 안내 메시지를 출력한다.
- [X] 프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야 하는 경우, 일부 수량에 대해 정가로 결제할지 여부 안내 메시지를 출력한다.
- [X] 멤버십 할인 적용 여부 메시지를 출력한다.
- [X] 영수증(구매 상품 내역, 증정 상품 내역, 금액 정보)를 출력한다.
- [X] 추가 구매 여부를 확인하는 안내 문구를 출력한다.

### 6. 검증

- [X] 구매할 상품과 수량 입력 형식을 검증한다.
- [X] 존재하지 않은 상품인지 검증한다.
- [X] 구매 수량이 재고 수량을 초과했는지 검증한다.
- [X] 기타 잘못된 입력인지 검증한다.

### 7. 반복 입력 처리

- [x] 잘못된 입력이 들어 왔을 때 안내 메시지 출력 후 그 부분부터 입력을 다시 받도록 한다.

### 8. 리팩토링 및 테스트 코드

- [x] 관련 함수를 묶어 클래스를 만들고, 객체들이 협력하여 하나의 큰 기능을 수행한다.
- [ ] JUnit 5와 AssertJ를 통해 클래스와 함수에 대한 단위 테스트를 진행한다.
- [X] 인덴트는 3이 넘지 않도록 한다.
- [X] 3항 연산자를 쓰지 않는다.
- [X] 메서드의 길이가 15라인을 넘어가지 않도록 한다.
- [x] else 예약어를 쓰지 않는다. * switch/case도 허용하지 않는다.
- [X] Enum을 적용한다.
- [X] 입출력을 담당하는 클래스를 별도 구현한다.
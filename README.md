# ☕ 커피 주문 시스템 (Coffee Ordering System)

> 과제 요구사항을 충족하기 위해 설계된 포인트 기반 커피 주문 및 통계 시스템입니다.

---

## 📑 1. 핵심 API 명세서
> 구현해야 할 필수 기능들을 바탕으로 RESTful하게 설계된 API 명세입니다. 확장성을 고려하여 **API Versioning(/api/)**을 적용했습니다.

### ☕ 메뉴 관련
| 기능 분류 | 기능명 | API Path | HTTP Method |
| :--- | :--- | :--- | :--- |
| Menu | 커피 메뉴 목록 조회 | `/api/menus` | `GET` |
| Stats | 인기 메뉴 조회 | `/api/menus/popular` | `GET` |

### 👤 유저 & 포인트 관련
| 기능 분류 | 기능명 | API Path | HTTP Method |
| :--- | :--- | :--- | :--- |
| User | 포인트 잔액 조회 | `/api/users/{userId}/point` | `GET` |
| User | 포인트 충전 | `/api/users/{userId}/point` | `PATCH` |

### 🛍️ 주문 & 결제 관련
| 기능 분류 | 기능명 | API Path | HTTP Method |
| :--- | :--- | :--- | :--- |
| Order | 커피 주문 및 결제 | `/api/orders` | `POST` |

> 📌 **Query Parameter (추가)**
> * `days` (int): 조회 기간 (기본값: 7)
> * 예시: `?days=3` (최근 3일간 인기 메뉴), `?days=30` (최근 한 달간 인기 메뉴)

---

## 🗄️ 2. 데이터베이스 테이블 구조
> 위의 핵심 API 기능들을 정상적으로 수행하기 위해 도출된 4개의 핵심 테이블 구조입니다.

### 1) User (사용자)
* 사용자의 고유 식별자와 결제에 쓰일 포인트 잔액을 관리합니다.
* **PK**: `id` (BIGINT)
* **Fields**: `point` (BIGINT)

### 2) Menu (커피 메뉴)
* 판매하는 커피의 이름과 가격 정보를 보관하는 기준 테이블입니다.
* **PK**: `id` (BIGINT)
* **Fields**: `name` (VARCHAR), `price` (BIGINT)

### 3) Orders (주문 기본)
* '누가', '언제', '총 얼마'를 결제했는지 주문의 전체적인 틀을 기록합니다.
* **PK**: `id` (BIGINT)
* **Fields**: `user_id` (BIGINT / FK), `total_price` (BIGINT), `created_at` (DATETIME)

### 4) OrderItem (주문 상세)
* 한 번의 주문 안에서 '어떤 메뉴'를 '몇 개' 샀는지 세부 내역을 1:N 관계로 기록합니다.
* **PK**: `id` (BIGINT)
* **Fields**: `order_id` (BIGINT / FK), `menu_id` (BIGINT / FK), `quantity` (BIGINT)

---

## 📊 3. ERD (Entity Relationship Diagram)
> 위 테이블들의 관계를 시각화한 ERD입니다.
> - User와 Orders는 1:N 관계, Orders와 OrderItem 또한 1:N 관계로 설계하였습니다.
> - Menu와 OrderItem 또한 1:N 관계로 구성되어 있습니다.


<img width="496" height="407" alt="Image" src="https://github.com/user-attachments/assets/d1ceadcf-5238-4810-be9d-72ad5d2cdedf" />

---

## 💡 4. 핵심 설계 및 문제 해결 전략

### 🎯 왜 Orders와 OrderItem을 분리했나요?
* **상황**: 사용자가 한 번 주문할 때 여러 종류의 커피를 담을 수 있어야 합니다. 만약 `Orders` 테이블 하나에만 메뉴를 기록하면 다중 메뉴 주문을 처리하기 어렵고, 데이터 중복이 발생할 수 있습니다.
* **해결**: '하나의 주문(`Orders`)'에 '여러 개의 상세 메뉴(`OrderItem`)'가 담길 수 있도록 **$1:N$ 관계로 분리하여 정규화를 적용**했습니다.
* **이점**: 데이터 일관성을 유지하면서도, 향후 메뉴 옵션 추가나 할인 정책 등 비즈니스 확장에도 유연하게 대응할 수 있습니다.

### 🔒 동시성 제어 전략 (Concurrency Control)
* **상황**: 다수의 유저가 동시에 포인트를 충전/차감하거나, 한 유저가 결제를 여러 번 시도할 경우 포인트 잔액이 꼬이는 문제가 발생할 수 있습니다.
* **해결**: 포인트 조회 및 차감 시점에 **DB 비관적 락(Pessimistic Lock)**을 적용하여, 하나의 트랜잭션이 해당 데이터에 대해 독점적으로 접근하도록 설계했습니다.
* **선택 이유**: 포인트 차감은 결제와 직결되는 중요한 로직이므로 데이터 정합성이 최우선입니다. 낙관적 락(Optimistic Lock)은 충돌 발생 시 롤백 및 재시도 처리가 필요하지만, 결제 과정에서는 잦은 실패 처리보다 안정적인 처리가 더 중요하다고 판단하여 비관적 락을 선택했습니다.

### 📈 "최근 7일간 인기 메뉴 통계" 추출 전략
* **상황**: 대량의 주문 데이터가 쌓일 경우 인기 메뉴 집계 쿼리의 성능이 저하될 수 있습니다.
* **해결**: `created_at` 기준으로 최근 7일 데이터를 필터링한 후, `OrderItem`과 조인하여 메뉴별 판매량을 집계하고 `GROUP BY`와 `SUM`으로 인기 메뉴를 산출합니다.
* **선택 이유**: 통계 데이터는 실시간 데이터 정확성이 가장 중요하므로 DB 기반 집계를 우선 선택하였으며, `created_at` 인덱스를 통해 조회 범위를 줄이고 성능을 최적화했습니다.
* **확장 고려**: 추후 대용량 트래픽 증가 시 Redis 캐싱 또는 배치(Batch) 집계 방식을 도입하여 확장할 수 있도록 고려했습니다.

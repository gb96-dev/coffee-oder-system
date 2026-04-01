# ☕ 커피 주문 시스템 (Coffee Ordering System)

> 과제 요구사항을 충족하기 위해 설계된 포인트 기반 커피 주문 및 통계 시스템입니다.

---

## 📑 1. 핵심 API 명세서
> 구현해야 할 필수 기능들을 바탕으로 RESTful하게 설계된 API 명세입니다. 확장성을 고려하여 **API Versioning(/api/v1)**을 적용했습니다.

### ☕ 메뉴 관련
| 기능 분류 | 기능명 | API Path | HTTP Method | 담당자 |
| :--- | :--- | :--- | :--- | :--- |
| Menu | 커피 메뉴 목록 조회 | `/api/v1/menus` | `GET` | 김규범 |

### 👤 유저 & 포인트 관련
| 기능 분류 | 기능명 | API Path | HTTP Method | 담당자 |
| :--- | :--- | :--- | :--- | :--- |
| User | 잔액 조회 | `/api/v1/users/{userId}/point` | `GET` | 김규범 |
| User | 잔액 충전 | `/api/v1/users/{userId}/charge` | `PATCH` | 김규범 |

### 🛍️ 주문 & 결제 관련
| 기능 분류 | 기능명 | API Path | HTTP Method | 담당자 |
| :--- | :--- | :--- | :--- | :--- |
| Order | 커피 주문 및 결제 | `/api/v1/orders` | `POST` | 김규범 |

### 📊 통계 관련
| 기능 분류 | 기능명 | API Path | HTTP Method | 담당자 |
| :--- | :--- | :--- | :--- | :--- |
| Stats | 최근 7일간 인기 메뉴 조회 | `/api/v1/stats/popular-menus` | `GET` | 김규범 |

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

<img width="496" height="407" alt="Image" src="https://github.com/user-attachments/assets/d1ceadcf-5238-4810-be9d-72ad5d2cdedf" />

---

## 💡 4. 핵심 설계 및 문제 해결 전략

### 🎯 왜 Orders와 OrderItem을 분리했나요?
사용자가 한 번 주문할 때 여러 종류의 커피를 담을 수 있어야 합니다. 만약 `Orders` 테이블 하나에만 메뉴를 기록하면 다중 메뉴 주문을 처리하기가 불가능에 가깝습니다. 따라서 **'하나의 주문(Orders)'에 '여러 개의 상세 메뉴(OrderItem)'가 담길 수 있도록 $1:N$ 관계로 분리**하여 시스템의 확장성을 챙겼습니다.

### 🔒 동시성 제어 전략 (Concurrency Control)
* **상황**: 다수의 유저가 동시에 포인트를 충전/차감하거나, 한 유저가 주문 결제 버튼을 광클할 때 잔액이 꼬이는 데이터 정합성 문제가 발생할 수 있습니다.
* **해결**: 유저의 포인트를 조회하고 수정하는 시점에 **DB 비관적 락(Pessimistic Lock)**을 걸어 데이터 오염을 방지하고 순차적인 처리를 보장합니다.

### 📈 "최근 7일간 인기 메뉴 통계" 추출 전략
`Orders` 테이블의 `created_at` 컬럼으로 최근 7일간의 데이터만 먼저 필터링합니다. 이후 연결된 `OrderItem`들의 `menu_id`를 기준으로 그룹화(`GROUP BY`)하고 판매 수량(`quantity`)을 합산(`SUM`)하여 가장 많이 팔린 TOP 3 메뉴를 빠르고 정확하게 집계해 냅니다.

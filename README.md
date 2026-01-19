# RetailRewardsApplication
Requirement.

A retailer offers a rewards program to its customers, awarding points based on each recorded purchase.

  A customer receives 2 points for every dollar spent over $100 in each transaction, plus 1 point for every dollar spent over $50 in each transaction (e.g. a $120 purchase = 2x$20 + 1x$50 = 90 points).

  Given a record of every transaction during a three month period, calculate the reward points earned for each customer per month and total.

1.Solve using Spring Boot

2.Create a RESTful endpoint

3.Make up a data set to best demonstrate your solution

4.Be sure to include unit tests (parameterized tests)

5.Check solution into GitHub
---------------------------------------------------------------------------------------------------
## ⚙️ Technologies Used
- Java 17+
- Spring Boot 3.5.9(currently used)
- Spring Cloud Netflix Eureka
- Spring Data JPA
- MySQL 8
- HikariCP (connection pooling)
- Maven

## 🚀 Setup Instructions
Clone Repository
bash:
git clone https://github.com/Elanchezhiyal/RetailRewardsApplication.git


<img width="563" height="276" alt="image" src="https://github.com/user-attachments/assets/42d558ef-e96a-4cd2-806c-6fdf7061b1be" />


Sample API requests:
--------------------
1. Get Rewards for a Specific Customer (last 3 months
      GET http://localhost:8081/api/rewards/customers/{customerId}/last3months
      sample: GET http://localhost:8081/api/rewards/customers/1/last3months

2. Get Rewards for All Customers (last 3 months)
      GET http://localhost:8081/api/rewards/customers/last3months

3. Get Rewards for a Customer Between Custom Dates
      GET http://localhost:8081/api/rewards/customers/1?startDate=2025-10-01&endDate=2026-01-01

Mysql db sample queries:
------------------------
Tables Customers and transactions haven been created during application creation.

use  rewardshub;

Customer table:
---------------
INSERT INTO customers (name, email) VALUES
('Ram', 'ram@example.com'),
('Rose', 'rose@example.com'),
('Happy', 'happy@example.com'),
('Navin', 'Navin@example.com'),
('Laura', 'laura@example.com');

Transactions Table:
-------------------
INSERT INTO transactions (customer_id, amount, tx_Date, reference) VALUES
(1, 120.00, '2026-01-05','INV-TXC-01'),
(1, 75.00,  '2026-01-17','INV-TXC-02'),
(1, 200.00, '2025-12-10','INV-TXC-03'),
(1, 45.00,  '2025-12-18','INV-TXC-04'),
(1, 130.00, '2025-11-02','INV-TXC-05');

-- Bob: edge cases around thresholds
INSERT INTO transactions (customer_id, amount, tx_date, reference) VALUES
(2, 50.00,  '2026-01-03','INV-TXC-06'),
(2, 51.00,  '2025-12-15','INV-TXC-07'),
(2, 100.00, '2025-12-01','INV-TXC-08'),
(2, 101.00, '2025-11-12','INV-TXC-09'),
(2, 150.00, '2025-11-22','INV-TXC-10');

-- Chitra: multiple mid-range
INSERT INTO transactions (customer_id, amount, tx_Date, reference) VALUES
(3, 90.00,  '2026-01-08','INV-TXC-11'),
(3, 43.00, '2026-01-25','INV-TXC-12'),
(3, 95.00,  '2026-01-14','INV-TXC-13'),
(3, 115.00, '2025-12-05','INV-TXC-14'),
(3, 60.00,  '2025-10-18','INV-TXC-15');

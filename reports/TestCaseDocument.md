# Test Case Document - Group 63
**QA Training Application Test Suite**

---

## Document Information
- **Project Name:** QA Training Application Automation
- **Group Number:** Group 63
- **Version:** 1.0
- **Date:** February 4, 2026
- **Prepared By:** [Team Member Names]

---

## Table of Contents
1. [Introduction](#introduction)
2. [Test Scope](#test-scope)
3. [Test Categories](#test-categories)
4. [UI Test Cases](#ui-test-cases)
5. [API Test Cases](#api-test-cases)
6. [Test Data](#test-data)
7. [Prerequisites](#prerequisites)

---

## 1. Introduction
This document outlines all test cases developed for the QA Training Application automation suite. The test suite covers both UI and API testing using Cucumber BDD framework with Playwright for UI automation and REST Assured for API testing.

---

## 2. Test Scope
### In Scope:
- User Authentication (Login/Logout)
- Plant Management (CRUD operations)
- Category Management
- Sales Management
- Dashboard functionality
- API endpoints for all modules

### Out of Scope:
- Performance testing
- Security penetration testing
- Mobile application testing

---

## 3. Test Categories
Tests are organized into the following modules:
1. Authentication
2. Plants
3. Categories
4. Sales
5. Dashboard

Each module contains both UI and API tests.

---

## 4. UI Test Cases

### 4.1 Authentication Module

#### TC-AUTH-UI-001: Successful Login with Valid Credentials
**Feature:** Login.feature  
**Priority:** High  
**Type:** Smoke, Positive  

| Step | Action | Expected Result |
|------|--------|----------------|
| 1 | Navigate to login page | Login page is displayed |
| 2 | Enter valid username | Username field accepts input |
| 3 | Enter valid password | Password field accepts input (masked) |
| 4 | Click Login button | User is redirected to dashboard |
| 5 | Verify welcome message | Welcome message is displayed |

**Test Data:**
- Username: admin
- Password: admin123

**Tags:** @UI @Authentication @Smoke @Positive

---

#### TC-AUTH-UI-002: Login with Invalid Credentials
**Feature:** Login.feature  
**Priority:** High  
**Type:** Negative  

| Step | Action | Expected Result |
|------|--------|----------------|
| 1 | Navigate to login page | Login page is displayed |
| 2 | Enter invalid username | Username field accepts input |
| 3 | Enter invalid password | Password field accepts input |
| 4 | Click Login button | Error message is displayed |
| 5 | Verify user remains on login page | User stays on login page |

**Test Data:**
- Username: invaliduser
- Password: wrongpass

**Expected Error:** "Invalid credentials"

**Tags:** @UI @Authentication @Negative

---

#### TC-AUTH-UI-003: Login with Missing Username
**Feature:** Login.feature  
**Priority:** Medium  
**Type:** Negative  

| Step | Action | Expected Result |
|------|--------|----------------|
| 1 | Navigate to login page | Login page is displayed |
| 2 | Leave username field empty | Username field is empty |
| 3 | Enter password | Password field accepts input |
| 4 | Click Login button | Validation message displayed |

**Expected Validation:** "Username is required"

**Tags:** @UI @Authentication @Negative

---

### 4.2 Plants Module

#### TC-PLANT-UI-001: Add New Plant
**Feature:** PlantManagement.feature  
**Priority:** High  
**Type:** Smoke, Positive  

| Step | Action | Expected Result |
|------|--------|----------------|
| 1 | Login as authenticated user | User is logged in |
| 2 | Navigate to Plants page | Plants page is displayed |
| 3 | Click Add Plant button | Add Plant form opens |
| 4 | Enter plant details | All fields accept input |
| 5 | Click Save button | Plant is saved successfully |
| 6 | Verify success message | "Plant added successfully" displayed |

**Test Data:**
- Name: Rose
- Description: Beautiful red rose
- Category: Flowers
- Price: 25.99

**Tags:** @UI @Plants @Smoke @Positive

---

#### TC-PLANT-UI-002: Search Existing Plant
**Feature:** PlantManagement.feature  
**Priority:** Medium  
**Type:** Positive  

| Step | Action | Expected Result |
|------|--------|----------------|
| 1 | Login and navigate to Plants page | Plants page displayed |
| 2 | Enter search term "Rose" | Search field accepts input |
| 3 | View search results | Results show plants matching "Rose" |

**Tags:** @UI @Plants @Positive

---

### 4.3 Categories Module

#### TC-CAT-UI-001: Create New Category
**Feature:** CategoryManagement.feature  
**Priority:** High  
**Type:** Smoke, Positive  

| Step | Action | Expected Result |
|------|--------|----------------|
| 1 | Login and navigate to Categories | Categories page displayed |
| 2 | Click Add Category button | Add Category form opens |
| 3 | Enter category name | Field accepts input |
| 4 | Enter category description | Field accepts input |
| 5 | Click Save button | Category created successfully |

**Test Data:**
- Name: Indoor Plants
- Description: Plants suitable for indoor environments

**Tags:** @UI @Categories @Smoke @Positive

---

### 4.4 Sales Module

#### TC-SALES-UI-001: Create New Sale
**Feature:** SalesManagement.feature  
**Priority:** High  
**Type:** Smoke, Positive  

| Step | Action | Expected Result |
|------|--------|----------------|
| 1 | Login with sales role | Sales user logged in |
| 2 | Navigate to Sales page | Sales page displayed |
| 3 | Click New Sale button | New sale form opens |
| 4 | Select plant and enter details | All fields populated |
| 5 | Click Complete Sale | Sale recorded successfully |
| 6 | Verify total calculation | Total amount is calculated correctly |

**Tags:** @UI @Sales @Smoke @Positive

---

### 4.5 Dashboard Module

#### TC-DASH-UI-001: View Dashboard Widgets
**Feature:** Dashboard.feature  
**Priority:** High  
**Type:** Smoke, Positive  

| Step | Action | Expected Result |
|------|--------|----------------|
| 1 | Login to application | User logged in |
| 2 | View dashboard | Dashboard loads completely |
| 3 | Verify widgets displayed | All widgets visible |

**Expected Widgets:**
- Total plants widget
- Total categories widget
- Recent sales widget
- Sales chart

**Tags:** @UI @Dashboard @Smoke @Positive

---

## 5. API Test Cases

### 5.1 Authentication API

#### TC-AUTH-API-001: Successful Login via API
**Feature:** AuthenticationAPI.feature  
**Priority:** High  
**Type:** Smoke, Positive  

**Request:**
- Method: POST
- Endpoint: /api/auth/login
- Body:
```json
{
  "username": "admin",
  "password": "admin123"
}
```

**Expected Response:**
- Status Code: 200
- Body should contain: token, user object

**Tags:** @API @Authentication @Smoke @Positive

---

#### TC-AUTH-API-002: Login with Invalid Credentials
**Feature:** AuthenticationAPI.feature  
**Priority:** High  
**Type:** Negative  

**Request:**
- Method: POST
- Endpoint: /api/auth/login
- Body:
```json
{
  "username": "invalid",
  "password": "wrong"
}
```

**Expected Response:**
- Status Code: 401
- Error message: "Invalid credentials"

**Tags:** @API @Authentication @Negative

---

### 5.2 Plants API

#### TC-PLANT-API-001: Get All Plants
**Feature:** PlantsAPI.feature  
**Priority:** High  
**Type:** Smoke, Positive  

**Request:**
- Method: GET
- Endpoint: /api/plants
- Headers: Authorization: Bearer {token}

**Expected Response:**
- Status Code: 200
- Body: JSON array with plant objects
- Each plant should have: id, name, description, category, price

**Tags:** @API @Plants @Smoke @Positive

---

#### TC-PLANT-API-002: Create New Plant
**Feature:** PlantsAPI.feature  
**Priority:** High  
**Type:** Positive  

**Request:**
- Method: POST
- Endpoint: /api/plants
- Headers: Authorization: Bearer {token}
- Body:
```json
{
  "name": "Sunflower",
  "description": "Bright yellow flower",
  "categoryId": 1,
  "price": 15.50
}
```

**Expected Response:**
- Status Code: 201
- Body: Created plant object with id

**Tags:** @API @Plants @Positive

---

### 5.3 Categories API

#### TC-CAT-API-001: Get All Categories
**Feature:** CategoriesAPI.feature  
**Priority:** High  
**Type:** Smoke, Positive  

**Request:**
- Method: GET
- Endpoint: /api/categories
- Headers: Authorization: Bearer {token}

**Expected Response:**
- Status Code: 200
- Body: JSON array with category objects

**Tags:** @API @Categories @Smoke @Positive

---

### 5.4 Sales API

#### TC-SALES-API-001: Get All Sales
**Feature:** SalesAPI.feature  
**Priority:** High  
**Type:** Smoke, Positive  

**Request:**
- Method: GET
- Endpoint: /api/sales
- Headers: Authorization: Bearer {token}

**Expected Response:**
- Status Code: 200
- Body: JSON array with sales objects

**Tags:** @API @Sales @Smoke @Positive

---

### 5.5 Dashboard API

#### TC-DASH-API-001: Get Dashboard Statistics
**Feature:** DashboardAPI.feature  
**Priority:** High  
**Type:** Smoke, Positive  

**Request:**
- Method: GET
- Endpoint: /api/dashboard/stats
- Headers: Authorization: Bearer {token}

**Expected Response:**
- Status Code: 200
- Body should contain: totalPlants, totalCategories, totalSales, todaySales

**Tags:** @API @Dashboard @Smoke @Positive

---

## 6. Test Data

### Users
| Role | Username | Password |
|------|----------|----------|
| Admin | admin | admin123 |
| Sales | sales | sales123 |

### Test Plants
| Name | Category | Price |
|------|----------|-------|
| Rose | Flowers | 25.99 |
| Tulip | Flowers | 20.00 |
| Orchid | Flowers | 35.00 |

---

## 7. Prerequisites
- Application server running on http://localhost:8080/ui
- Valid test user accounts created
- Sample test data loaded in database
- Maven installed (version 3.6+)
- Java JDK 11 or higher
- Playwright browsers installed

---

## Test Execution Commands

Run all tests:
```bash
mvn clean test
```

Run specific tags:
```bash
mvn clean test -Dcucumber.filter.tags="@Smoke"
```

Generate Allure report:
```bash
mvn allure:report
mvn allure:serve
```

---

**Document Revision History**

| Version | Date | Changes | Author |
|---------|------|---------|--------|
| 1.0 | 2026-02-04 | Initial version | Group 63 |

---

**End of Document**

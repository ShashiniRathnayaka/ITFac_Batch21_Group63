# Defect Report - Group 63
**QA Training Application - Identified Bugs**

---

## Document Information
- **Project Name:** QA Training Application
- **Group Number:** Group 63
- **Version:** 1.0
- **Date:** February 4, 2026
- **Reported By:** [Team Member Names]

---

## Defect Summary

| Total Defects | Critical | High | Medium | Low |
|---------------|----------|------|--------|-----|
| 0 | 0 | 0 | 0 | 0 |

---

## Defect Details

### Template for Reporting Defects

---

#### Bug Report

**Project:** [Project Name]

**Bug ID:** [Unique Identifier - e.g., BUG-001]

**Date Reported:** [Date of Report]

**Reported by:** [Reporter Name]

**Assigned to:** [Developer/Tester - Optional]

**Status:** [New, Open, In Progress, Resolved, Closed]

**Priority:** [High, Medium, Low]

**Severity:** [Blocker, Critical, Major, Minor]

**Component:** [UI, API]

**Summary:** [Concise description of the bug]

**Description:**

---

### Example Defect Entry

---

#### Bug Report

**Project:** QA Training Application

**Bug ID:** BUG-001

**Date Reported:** 2026-02-04

**Reported by:** Group 63

**Assigned to:** [Developer name]

**Status:** New

**Priority:** High

**Severity:** Critical

**Component:** UI

**Summary:** Login fails with correct credentials after 3 failed attempts

**Description:**

**Module:** Authentication

**Environment:**
- OS: Windows 11
- Browser: Chromium (version 120)
- Application Version: 1.0
- Test Environment: http://localhost:8080/ui

**Test Case ID:** TC-AUTH-UI-001

**Preconditions:**
1. User has valid credentials (admin/admin123)
2. Application is running

**Steps to Reproduce:**
1. Navigate to login page
2. Enter incorrect credentials 3 times
3. Enter correct credentials (admin/admin123)
4. Click Login button

**Expected Result:**
User should be logged in successfully with valid credentials

**Actual Result:**
Login fails even with correct credentials. Error message displays "Account temporarily locked"

**Screenshots/Evidence:**
[Screenshot would be attached here]
- Error message screenshot
- Console log showing lock status

**Workaround:**
Wait 5 minutes before attempting login again, or clear browser cache

**Additional Notes:**
- Issue only occurs after 3 failed attempts
- No documentation mentions account lockout feature
- No indication to user about lockout duration

---

## Defect Tracking

### By Module

| Module | Critical | High | Medium | Low | Total |
|--------|----------|------|--------|-----|-------|
| Authentication | 0 | 0 | 0 | 0 | 0 |
| Plants | 0 | 0 | 0 | 0 | 0 |
| Categories | 0 | 0 | 0 | 0 | 0 |
| Sales | 0 | 0 | 0 | 0 | 0 |
| Dashboard | 0 | 0 | 0 | 0 | 0 |
| API | 0 | 0 | 0 | 0 | 0 |

### By Status

| Status | Count |
|--------|-------|
| New | 0 |
| Open | 0 |
| In Progress | 0 |
| Fixed | 0 |
| Retest | 0 |
| Closed | 0 |

---

## Defect Metrics

### Defect Detection Rate
[Calculate after test execution]

### Defect Resolution Time
[Track average time from reported to fixed]

### Defect Density
[Number of defects per test case]

---

## Guidelines for Reporting Defects

1. **Be Specific:** Provide clear, concise title and description
2. **Reproducible:** Ensure steps to reproduce are accurate and complete
3. **Evidence:** Always attach screenshots, logs, or videos when possible
4. **Severity vs Priority:** 
   - Severity = Impact on system
   - Priority = Order of fixing
5. **Single Issue:** One defect per report
6. **Search First:** Check if defect already reported
7. **Update Status:** Keep defect status current

---

## Defect Lifecycle

```
New → Open → In Progress → Fixed → Retest → Closed
                ↓
            Reopened (if retest fails)
```

---

## Contact Information

**QA Team Lead:** [Name]  
**Email:** [email@example.com]  
**Project Repository:** [Git repository URL]

---

**Document Revision History**

| Version | Date | Changes | Author |
|---------|------|---------|--------|
| 1.0 | 2026-02-04 | Initial template created | Group 63 |

---

**End of Document**

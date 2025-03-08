#!/bin/zsh

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# API Configuration
BASE_URL="http://localhost:8080"
USERNAME="EmployeesRUS"
PASSWORD="AWYSftc_878965$!"

echo "${YELLOW}Starting API Tests...${NC}\n"

# Test 1: Authentication and Token Retrieval
echo "${GREEN}Test 1: Getting Authentication Token${NC}"
TOKEN=$(curl -s -X POST "$BASE_URL/api/v1/auth/login" \
    -H "Content-Type: application/json" \
    -d "{\"username\":\"$USERNAME\",\"password\":\"$PASSWORD\"}" \
    | sed 's/.*"token":"\([^"]*\)".*/\1/')

if [ -z "$TOKEN" ]; then
    echo "${RED}Failed to get token. Check if the server is running and credentials are correct.${NC}"
    exit 1
fi

echo "${GREEN}Successfully retrieved token!${NC}"

# Test 2: Create Employee (POST)
echo "\n${GREEN}Test 2: Creating New Employee${NC}"
CREATE_RESPONSE=$(curl -s -X POST "$BASE_URL/api/v1/employee" \
    -H "Authorization: Bearer $TOKEN" \
    -H "Content-Type: application/json" \
    -d '{
        "firstName": "John",
        "lastName": "Doe",
        "email": "john.doe@example.com",
        "phoneNumber": "+1234567890",
        "hireDate": "2024-01-01",
        "salary": 75000,
        "department": "Engineering"
    }')

echo "Create Response: $CREATE_RESPONSE"
# Extract the employee ID from the response for later use
EMPLOYEE_ID=$(echo $CREATE_RESPONSE | sed 's/.*"id":\([0-9]*\).*/\1/')

# Test 3: Get All Employees (GET)
echo "\n${GREEN}Test 3: Getting All Employees${NC}"
curl -s "$BASE_URL/api/v1/employee" \
    -H "Authorization: Bearer $TOKEN" \
    | json_pp

# Test 4: Get Single Employee (GET)
echo "\n${GREEN}Test 4: Getting Single Employee${NC}"
curl -s "$BASE_URL/api/v1/employee/$EMPLOYEE_ID" \
    -H "Authorization: Bearer $TOKEN" \
    | json_pp

# Test 5: Update Employee (PUT)
echo "\n${GREEN}Test 5: Updating Employee${NC}"
curl -s -X PUT "$BASE_URL/api/v1/employee/$EMPLOYEE_ID" \
    -H "Authorization: Bearer $TOKEN" \
    -H "Content-Type: application/json" \
    -d '{
        "firstName": "John",
        "lastName": "Doe",
        "email": "john.doe.updated@example.com",
        "phoneNumber": "+1234567890",
        "hireDate": "2024-01-01",
        "salary": 80000,
        "department": "Engineering Management"
    }' | json_pp

# Test 6: Search Employees (GET with parameters)
echo "\n${GREEN}Test 6: Searching Employees${NC}"
curl -s "$BASE_URL/api/v1/employee/search?department=Engineering%20Management" \
    -H "Authorization: Bearer $TOKEN" \
    | json_pp

# Test 7: Delete Employee (DELETE)
echo "\n${GREEN}Test 7: Deleting Employee${NC}"
curl -s -X DELETE "$BASE_URL/api/v1/employee/$EMPLOYEE_ID" \
    -H "Authorization: Bearer $TOKEN"

# Verify deletion
echo "\n${GREEN}Test 8: Verifying Deletion${NC}"
VERIFY_DELETE=$(curl -s "$BASE_URL/api/v1/employee/$EMPLOYEE_ID" \
    -H "Authorization: Bearer $TOKEN")

if [[ $VERIFY_DELETE == *"not found"* ]]; then
    echo "${GREEN}Employee successfully deleted!${NC}"
else
    echo "${RED}Employee might not have been deleted. Please check.${NC}"
fi

# Test Rate Limiting
echo "\n${GREEN}Test 9: Testing Rate Limiting${NC}"
echo "Making 6 quick authentication attempts (limit is 5)..."
for i in {1..6}; do
    echo "\n${YELLOW}Login attempt $i:${NC}"
    curl -s -X POST "$BASE_URL/api/v1/auth/login" \
        -H "Content-Type: application/json" \
        -d "{\"username\":\"$USERNAME\",\"password\":\"wrong_password\"}"
    echo "\n"
done

echo "\n${GREEN}All tests completed!${NC}" 
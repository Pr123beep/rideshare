# 🚀 RideShare Backend API

A Spring Boot backend application for a ride-sharing service with JWT authentication, MongoDB, and role-based access control.

## 📋 Prerequisites

- Java 21
- Maven 3.6+
- MongoDB (running on `localhost:27017`)

## 🛠️ Setup Instructions

### 1. Start MongoDB
Make sure MongoDB is running on `localhost:27017`. If you don't have MongoDB installed:
- Download from [MongoDB Download Center](https://www.mongodb.com/try/download/community)
- Or use MongoDB Atlas (update connection string in `application.properties`)

### 2. Build the Project
```bash
cd rideshare
mvn clean install
```

### 3. Run the Application
```bash
mvn spring-boot:run
```

The server will start on **http://localhost:8081**

## 📚 API Endpoints

### Authentication Endpoints (Public)

#### Register User
```bash
POST /api/auth/register
Content-Type: application/json

{
  "username": "john",
  "password": "1234",
  "role": "ROLE_USER"
}
```

#### Register Driver
```bash
POST /api/auth/register
Content-Type: application/json

{
  "username": "driver1",
  "password": "abcd",
  "role": "ROLE_DRIVER"
}
```

#### Login
```bash
POST /api/auth/login
Content-Type: application/json

{
  "username": "john",
  "password": "1234"
}

Response:
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "john",
  "role": "ROLE_USER"
}
```

### Ride Endpoints (Protected - Requires JWT Token)

**Note:** All protected endpoints require `Authorization: Bearer <token>` header

#### Create Ride (USER only)
```bash
POST /api/v1/rides
Authorization: Bearer <token>
Content-Type: application/json

{
  "pickupLocation": "Koramangala",
  "dropLocation": "Indiranagar"
}
```

#### Get User's Rides (USER only)
```bash
GET /api/v1/user/rides
Authorization: Bearer <token>
```

#### View Pending Ride Requests (DRIVER only)
```bash
GET /api/v1/driver/rides/requests
Authorization: Bearer <token>
```

#### Accept Ride (DRIVER only)
```bash
POST /api/v1/driver/rides/{rideId}/accept
Authorization: Bearer <token>
```

#### Complete Ride (USER or DRIVER)
```bash
POST /api/v1/rides/{rideId}/complete
Authorization: Bearer <token>
```

## 🧪 Testing with cURL

### 1. Register a User
```bash
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"john\",\"password\":\"1234\",\"role\":\"ROLE_USER\"}"
```

### 2. Register a Driver
```bash
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"driver1\",\"password\":\"abcd\",\"role\":\"ROLE_DRIVER\"}"
```

### 3. Login
```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"john\",\"password\":\"1234\"}"
```

**Save the token from the response!**

### 4. Create a Ride (Replace `<token>` with actual token)
```bash
curl -X POST http://localhost:8081/api/v1/rides \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d "{\"pickupLocation\":\"Koramangala\",\"dropLocation\":\"Indiranagar\"}"
```

### 5. View Pending Requests (Driver)
```bash
curl -X GET http://localhost:8081/api/v1/driver/rides/requests \
  -H "Authorization: Bearer <driver_token>"
```

### 6. Accept a Ride (Driver)
```bash
curl -X POST http://localhost:8081/api/v1/driver/rides/{rideId}/accept \
  -H "Authorization: Bearer <driver_token>"
```

### 7. Complete a Ride
```bash
curl -X POST http://localhost:8081/api/v1/rides/{rideId}/complete \
  -H "Authorization: Bearer <token>"
```

## 📁 Project Structure

```
src/main/java/org/example/rideshare/
├── model/          # User, Ride entities
├── repository/     # MongoDB repositories
├── service/        # Business logic
├── controller/     # REST endpoints
├── config/         # Security & JWT configuration
├── dto/            # Data Transfer Objects
├── exception/      # Custom exceptions & handlers
└── util/           # JWT utility class
```

## 🔐 Security Features

- JWT-based authentication
- BCrypt password encoding
- Role-based access control (ROLE_USER, ROLE_DRIVER)
- Input validation using Jakarta Validation
- Global exception handling

## 📝 Ride Status Flow

1. **REQUESTED** - User creates a ride request
2. **ACCEPTED** - Driver accepts the ride
3. **COMPLETED** - Ride is completed by user or driver

## ⚠️ Error Responses

All errors follow this format:
```json
{
  "error": "ERROR_TYPE",
  "message": "Error message",
  "timestamp": "2025-01-20T12:00:00Z"
}
```

Common error types:
- `VALIDATION_ERROR` - Input validation failed
- `NOT_FOUND` - Resource not found
- `BAD_REQUEST` - Invalid request
- `INTERNAL_SERVER_ERROR` - Server error

## 🎯 Next Steps

1. **Start MongoDB** - Ensure MongoDB is running
2. **Run the application** - `mvn spring-boot:run`
3. **Test endpoints** - Use the cURL commands above or Postman
4. **Optional** - Import endpoints into Postman for easier testing

## 📌 Notes

- Server runs on port **8081**
- MongoDB database name: **rideshare**
- JWT token expiration: 24 hours (86400000 ms)
- Passwords are encrypted using BCrypt


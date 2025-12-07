# RideShare Backend API

Spring Boot backend with JWT authentication and MongoDB.

## Prerequisites

- Java 21
- MongoDB running on `localhost:27017`

## Run

```bash
mvn spring-boot:run
```

Server starts on **http://localhost:8081**

## API Endpoints

### Public Endpoints

**Register**
```bash
POST /api/auth/register
Body: {"username":"john","password":"1234","role":"ROLE_USER"}
```

**Login**
```bash
POST /api/auth/login
Body: {"username":"john","password":"1234"}
Response: {"token":"...","username":"john","role":"ROLE_USER"}
```

### Protected Endpoints (Require `Authorization: Bearer <token>`)

**Create Ride** (USER only)
```bash
POST /api/v1/rides
Body: {"pickupLocation":"Koramangala","dropLocation":"Indiranagar"}
```

**Get User Rides** (USER only)
```bash
GET /api/v1/user/rides
```

**View Pending Requests** (DRIVER only)
```bash
GET /api/v1/driver/rides/requests
```

**Accept Ride** (DRIVER only)
```bash
POST /api/v1/driver/rides/{rideId}/accept
```

**Complete Ride** (USER/DRIVER)
```bash
POST /api/v1/rides/{rideId}/complete
```

## Quick Test

```bash
# Register user
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"john","password":"1234","role":"ROLE_USER"}'

# Login (save token)
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"john","password":"1234"}'

# Create ride (replace <token>)
curl -X POST http://localhost:8081/api/v1/rides \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"pickupLocation":"A","dropLocation":"B"}'
```

## Ride Status Flow

REQUESTED → ACCEPTED → COMPLETED

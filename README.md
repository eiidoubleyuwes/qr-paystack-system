# Paystack Webhook QR App

This Spring Boot application acts as a webhook client for Paystack. It generates a QR code and PDF ticket after successful payment, sends it via email (SMTP only), and allows attendance marking via QR scan or ticket ID. All data is stored in SQLite.

## Features
- Paystack webhook endpoint for payment notifications
- Extracts user data (name, email, phone, etc.)
- Generates QR code and PDF ticket
- Sends PDF ticket via email (SMTP only)
- Attendance marking via QR scan or ticket ID
- Swagger UI for API documentation
- Dockerized (Spring Boot + SQLite)

## Prerequisites
- Java 17+
- Maven
- (Optional) Docker & Docker Compose

## How to Run

### 1. Local (Recommended for Development)

1. **Clone the repository:**
   ```bash
   git clone <repo-url>
   cd qr-answer
   ```
2. **Configure SMTP and QR base URL:**
   - Edit `src/main/resources/application.yml` with your SMTP credentials and desired QR redirect base URL.
3. **Build the app:**
   ```bash
   mvn clean package
   ```
4. **Run the app:**
   ```bash
   java -jar target/*.jar
   ```
5. **Access the app:**
   - Main app: [http://localhost:8080](http://localhost:8080)
   - Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
   - Admin Dashboard: [http://localhost:8080/admin/dashboard](http://localhost:8080/admin/dashboard)

### 2. Docker (Optional)

1. **Build and run with Docker Compose:**
   ```bash
   docker-compose up --build
   ```
2. **Access as above.**

## Environment Variables / Configuration
Configure `src/main/resources/application.yml` for:
- SQLite database (default: file-based, no credentials needed)
- Mail server credentials (SMTP)
- QR redirect base URL

## Admin Dashboard

The app includes a built-in admin dashboard for visualizing and managing QR code tickets:

- **Stats:** See total QR codes generated and how many have been scanned (attended).
- **Ticket List:** View all tickets with user info, reference, and attendance status.
- **QR Code Viewer:** Click to view the QR code for any ticket.

### Accessing the Dashboard
- Go to: `http://localhost:8080/admin/dashboard`
- No authentication is enabled by default (for demo/dev). For production, add authentication.

### Endpoints
- `GET /admin/dashboard` — Admin dashboard page
- `GET /admin/api/stats` — JSON stats (total, scanned)
- `GET /admin/api/tickets` — JSON list of all tickets
- `GET /admin/api/qr/{id}` — PNG image of QR code for ticket

## Endpoints
- `POST /api/webhook/paystack` — Paystack webhook
- `GET /attend` — Attendance page (QR scan or ticket ID)
- `GET /swagger-ui.html` — Swagger UI
- `GET /admin/dashboard` — Admin dashboard
- `GET /admin/api/stats` — Admin stats (JSON)
- `GET /admin/api/tickets` — Admin ticket list (JSON)
- `GET /admin/api/qr/{id}` — Admin QR code image

## Database
- Default: SQLite file `paystackdb.sqlite` (no username/password required)

## Customization
- Update mail (SMTP) settings in `application.yml`
- Set Paystack webhook URL to `/api/webhook/paystack`
- Change QR code redirect base URL in `application.yml` (`qr.redirect-base-url`)

## High Concurrency: Handling 100 Users at a Time

This app is designed to handle high traffic and concurrent webhook/email/QR code processing:

- **Async Processing:** All heavy tasks (QR code generation, PDF creation, email sending) are processed asynchronously using Spring's `@Async`.
- **Thread Pool:** The app is configured with a thread pool of 100 threads (see `AsyncConfig.java`). This means up to 100 webhook events or ticket generations can be processed in parallel, with up to 200 more queued.
- **Non-blocking:** The main HTTP thread returns immediately, so the app remains responsive even under load.
- **Scalable:** You can further increase the pool size or use a message queue for even higher throughput if needed.

This makes the system robust for events like ticket sales, check-ins, or any scenario where many users interact with the system at once.

---
MIT License 
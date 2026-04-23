# FarmGo Java Backend

This is the Java backend implementation for the FarmGo e-commerce website.

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── farmgo/
│   │           ├── controller/    # REST controllers
│   │           ├── dao/           # Data Access Objects
│   │           ├── entity/        # Entity classes
│   │           └── util/         # Utility classes
│   └── webapp/
│       └── WEB-INF/
└── test/                         # Unit tests (if any)
```

## Technologies Used

- Java 11
- Servlet API 4.0
- MySQL Database
- Maven for dependency management
- Jackson for JSON processing
- Apache Commons DBUtils

## API Endpoints

### Authentication
- `POST /api/login` - User login

### Products
- `GET /api/products` - Get all products

### Cart
- `POST /api/cart/add` - Add product to cart
- `GET /api/cart/items` - Get all cart items
- `GET /api/cart/count` - Get cart item count
- `POST /api/cart/update` - Update cart item quantity
- `DELETE /api/cart/remove/{id}` - Remove item from cart
- `DELETE /api/cart/clear` - Clear all items from cart

### Orders
- `GET /api/order/summary` - Get order summary
- `POST /api/order/place` - Place an order

## Database Schema

The application uses two databases:

1. `login_register` - For user authentication
2. `cart_system` - For product, cart, and order management

## Setup Instructions

1. Make sure you have Java 11+ and Maven installed
2. Set up MySQL databases using the SQL files in the `util` folder
3. Update database credentials in `DatabaseConfig.java` if needed
4. Build the project: `mvn clean install`
5. Deploy the generated WAR file to a servlet container (Tomcat, Jetty, etc.)

## Frontend Integration

The frontend JavaScript has been updated to work with the Java backend REST APIs. All AJAX calls now point to the appropriate Java endpoints.
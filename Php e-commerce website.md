Simple e-commerce website php
1. First, create a database named `ecommerce` and these tables:

```sql
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50),
    email VARCHAR(50) UNIQUE,
    password VARCHAR(255)
);

CREATE TABLE products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    price DECIMAL(10,2),
    description TEXT
);

CREATE TABLE cart (
    cart_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    product_id INT,
    quantity INT DEFAULT 1,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);
```

2. Create these PHP files:

**config.php** (Database connection)
```php
<?php
$host = 'localhost';
$user = 'root';
$pass = '';
$db = 'ecommerce';

$conn = new mysqli($host, $user, $pass, $db);

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
session_start();
?>
```

**header.php** (Common header)
```php
<!DOCTYPE html>
<html>
<head>
    <title>Simple E-Commerce</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
        <div class="container">
            <a class="navbar-brand" href="index.php">E-Commerce</a>
            <div class="d-flex">
                <?php if(isset($_SESSION['user_id'])): ?>
                    <a href="cart.php" class="btn btn-outline-success mx-2">Cart</a>
                    <a href="logout.php" class="btn btn-outline-danger">Logout</a>
                <?php else: ?>
                    <a href="signup.php" class="btn btn-outline-primary mx-2">Sign Up</a>
                    <a href="signin.php" class="btn btn-outline-success">Sign In</a>
                <?php endif; ?>
            </div>
        </div>
    </nav>
    <div class="container mt-4">
```

**footer.php** (Common footer)
```php
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
```

**signup.php**
```php
<?php include 'config.php'; ?>
<?php include 'header.php'; ?>

<?php
if($_SERVER['REQUEST_METHOD'] == 'POST') {
    $name = $_POST['name'];
    $email = $_POST['email'];
    $password = password_hash($_POST['password'], PASSWORD_DEFAULT);

    $stmt = $conn->prepare("INSERT INTO users (name, email, password) VALUES (?, ?, ?)");
    $stmt->bind_param("sss", $name, $email, $password);
    
    if($stmt->execute()) {
        header("Location: signin.php");
    } else {
        echo "Error: " . $conn->error;
    }
}
?>

<div class="col-md-6 mx-auto">
    <h2>Sign Up</h2>
    <form method="POST">
        <div class="mb-3">
            <label>Name</label>
            <input type="text" name="name" class="form-control" required>
        </div>
        <div class="mb-3">
            <label>Email</label>
            <input type="email" name="email" class="form-control" required>
        </div>
        <div class="mb-3">
            <label>Password</label>
            <input type="password" name="password" class="form-control" required>
        </div>
        <button type="submit" class="btn btn-primary">Sign Up</button>
    </form>
</div>

<?php include 'footer.php'; ?>
```

**signin.php**
```php
<?php include 'config.php'; ?>
<?php include 'header.php'; ?>

<?php
if($_SERVER['REQUEST_METHOD'] == 'POST') {
    $email = $_POST['email'];
    $password = $_POST['password'];

    $stmt = $conn->prepare("SELECT * FROM users WHERE email = ?");
    $stmt->bind_param("s", $email);
    $stmt->execute();
    $result = $stmt->get_result();
    $user = $result->fetch_assoc();

    if($user && password_verify($password, $user['password'])) {
        $_SESSION['user_id'] = $user['id'];
        header("Location: index.php");
    } else {
        echo "Invalid email or password";
    }
}
?>

<div class="col-md-6 mx-auto">
    <h2>Sign In</h2>
    <form method="POST">
        <div class="mb-3">
            <label>Email</label>
            <input type="email" name="email" class="form-control" required>
        </div>
        <div class="mb-3">
            <label>Password</label>
            <input type="password" name="password" class="form-control" required>
        </div>
        <button type="submit" class="btn btn-primary">Sign In</button>
    </form>
</div>

<?php include 'footer.php'; ?>
```

**index.php** (Product listing)
```php
<?php include 'config.php'; ?>
<?php include 'header.php'; ?>

<h2>Products</h2>
<div class="row">
    <?php
    $result = $conn->query("SELECT * FROM products");
    while($row = $result->fetch_assoc()):
    ?>
    <div class="col-md-4 mb-4">
        <div class="card">
            <div class="card-body">
                <h5 class="card-title"><?= $row['name'] ?></h5>
                <p class="card-text"><?= $row['description'] ?></p>
                <p class="h5">$<?= $row['price'] ?></p>
                <?php if(isset($_SESSION['user_id'])): ?>
                    <form action="add_to_cart.php" method="POST">
                        <input type="hidden" name="product_id" value="<?= $row['id'] ?>">
                        <button type="submit" class="btn btn-primary">Add to Cart</button>
                    </form>
                <?php endif; ?>
            </div>
        </div>
    </div>
    <?php endwhile; ?>
</div>

<?php include 'footer.php'; ?>
```

**add_to_cart.php**
```php
<?php include 'config.php'; 

if(!isset($_SESSION['user_id'])) {
    header("Location: signin.php");
    exit;
}

if($_SERVER['REQUEST_METHOD'] == 'POST') {
    $user_id = $_SESSION['user_id'];
    $product_id = $_POST['product_id'];

    // Check if product already in cart
    $stmt = $conn->prepare("SELECT * FROM cart WHERE user_id = ? AND product_id = ?");
    $stmt->bind_param("ii", $user_id, $product_id);
    $stmt->execute();
    $result = $stmt->get_result();

    if($result->num_rows > 0) {
        // Update quantity
        $stmt = $conn->prepare("UPDATE cart SET quantity = quantity + 1 WHERE user_id = ? AND product_id = ?");
        $stmt->bind_param("ii", $user_id, $product_id);
    } else {
        // Insert new
        $stmt = $conn->prepare("INSERT INTO cart (user_id, product_id) VALUES (?, ?)");
        $stmt->bind_param("ii", $user_id, $product_id);
    }
    
    $stmt->execute();
    header("Location: cart.php");
}
?>
```

**cart.php**
```php
<?php include 'config.php'; 
if(!isset($_SESSION['user_id'])) {
    header("Location: signin.php");
    exit;
}
include 'header.php'; 

$user_id = $_SESSION['user_id'];
$cart_items = $conn->query("
    SELECT products.*, cart.quantity 
    FROM cart 
    JOIN products ON cart.product_id = products.id 
    WHERE cart.user_id = $user_id
");
?>

<h2>Your Cart</h2>
<table class="table">
    <thead>
        <tr>
            <th>Product</th>
            <th>Price</th>
            <th>Quantity</th>
            <th>Total</th>
            <th>Action</th>
        </tr>
    </thead>
    <tbody>
        <?php while($item = $cart_items->fetch_assoc()): ?>
        <tr>
            <td><?= $item['name'] ?></td>
            <td>$<?= $item['price'] ?></td>
            <td><?= $item['quantity'] ?></td>
            <td>$<?= $item['price'] * $item['quantity'] ?></td>
            <td>
                <a href="remove_from_cart.php?product_id=<?= $item['id'] ?>" class="btn btn-danger btn-sm">Remove</a>
            </td>
        </tr>
        <?php endwhile; ?>
    </tbody>
</table>
<a href="checkout.php" class="btn btn-success">Checkout</a>

<?php include 'footer.php'; ?>
```

**remove_from_cart.php**
```php
<?php include 'config.php'; 

if(!isset($_SESSION['user_id'])) {
    header("Location: signin.php");
    exit;
}

if(isset($_GET['product_id'])) {
    $user_id = $_SESSION['user_id'];
    $product_id = $_GET['product_id'];
    
    $stmt = $conn->prepare("DELETE FROM cart WHERE user_id = ? AND product_id = ?");
    $stmt->bind_param("ii", $user_id, $product_id);
    $stmt->execute();
}

header("Location: cart.php");
?>
```

**logout.php**
```php
<?php
session_start();
session_destroy();
header("Location: signin.php");
?>
```

**checkout.php** (Simple checkout page)
```php
<?php include 'config.php'; 
if(!isset($_SESSION['user_id'])) {
    header("Location: signin.php");
    exit;
}
include 'header.php'; 
?>

<h2>Checkout</h2>
<div class="alert alert-success">
    Thank you for your order! Your items will be shipped soon.
</div>

<?php 
// Clear the cart after checkout
$user_id = $_SESSION['user_id'];
$conn->query("DELETE FROM cart WHERE user_id = $user_id");

include 'footer.php'; 
?>
```

To use this system:

1. Create the database and tables
2. Add some sample products to the products table
3. Configure database credentials in config.php
4. Start the PHP server and access the website

Features included:
- User registration and login
- Product listing
- Add to cart functionality
- Cart management
- Checkout process
- Session management

Note: This is a basic implementation and should be enhanced with:
- Input validation and sanitization
- Better error handling
- Password reset functionality
- Payment gateway integration
- Order history
- Admin panel
- Product images
- CSRF protection
- Prepared statements for all SQL queries

Remember to never use this in production without proper security measures!

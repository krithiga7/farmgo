<?php 

include 'configure.php';
session_start();
error_reporting(0);

// Redirect to Java backend login endpoint
if (isset($_POST['submit'])) {
    $email = $_POST['email'];
    $password = $_POST['password'];
    
    // Redirect to Java backend login API
    header("Location: ../api/login?email=" . urlencode($email) . "&password=" . urlencode($password));
    exit();
}

?>
<?php
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "mobileProgramming";
$conn = new mysqli($servername, $username, $password, $dbname);
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
if (isset($_POST['name']) && isset($_POST['email'])) {
    $name = $_POST['name'];
    $email = $_POST['email'];
    $sql = "INSERT INTO users (name, email) VALUES ('$name', '$email')";
    if ($conn->query($sql) === TRUE) {
        $response = array('message' => 'User registered successfully');
    } else {
        $response = array('message' => 'Error: Unable to register user');
    }
} else {
    $response = array('message' => 'Error: Invalid request parameters');
}
$conn->close();
header('Content-Type: application/json');
echo json_encode($response);
?>

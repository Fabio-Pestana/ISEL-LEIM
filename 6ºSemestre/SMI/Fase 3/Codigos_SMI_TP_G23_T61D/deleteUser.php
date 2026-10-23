<?php
session_start();

if (!isset($_SESSION['user'])) {
    header("Location: login.php");
    exit();
}

if ($_SESSION['user']['Tipo'] !== 'Administrador') {
    header("Location: mainPage.php");
    exit();
}

if (!isset($_SESSION['deleteUser'])) {
    $msg = "Error while trying to delete user!";
    $_SESSION['message'] = $msg;
    header("Location: userManagement.php");
    exit();
}

require_once 'db.php';
$conn = db_connect();

$userEmail = $_SESSION['deleteUser'];

function deleteFromDb($conn, $query, $user){
    $stmt = $conn->prepare($query);
    $stmt->bind_param("s", $user);   
    $stmt->execute();
    $stmt->close();
}
$conn->query("SET FOREIGN_KEY_CHECKS=0");

$deleteQueries = [
    "DELETE FROM Edition WHERE Editor = ?",
    "DELETE FROM User_Wiki WHERE Email = ?",
    "DELETE FROM Information WHERE Creator = ?",
    "DELETE FROM Page WHERE Creator = ?",
    "DELETE FROM User WHERE Email = ?"
];

foreach ($deleteQueries as $query) {
    deleteFromDb($conn, $query, $userEmail);
}

$conn->query("SET FOREIGN_KEY_CHECKS=1");

$msg = "User deleted successfully!";

$_SESSION['message'] = $msg;
$conn->close();
header("Location: userManagement.php");
exit();
?>
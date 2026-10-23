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
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>NexusWiki - Administrator Settings</title>
    <link rel="stylesheet" type="text/css" href="./Css/adminSettings.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
</head>
<body>
    <header class="wikiHeader">
        <div class="logo-container">
            <img src="./Images/logo.png" alt="NexusWiki Logo" class="logo">
        </div>
    </header>

    <div class="nav-row">
        <a href="mainPage.php" class="back-arrow">
            <i class="bi bi-arrow-left"></i>
        </a>
        <div class="profile-icon-container">
            <a href="profile.php" class="profile-icon">
                <i class="bi bi-person-circle"></i>
            </a>
        </div>
    </div>

    <div class="AdminContainer">
        <h1>Administrator Settings Menu</h1>
        
        <a href="userManagement.php" class="admin-btn">
            <i class="bi bi-people-fill"></i> User Management
        </a>
        
        <a href="editHistory.php" class="admin-btn">
            <i class="bi bi-clock-history"></i> See History of Editions
        </a>
        
        <a href="tableOfCreators.php" class="admin-btn">
            <i class="bi bi-file-earmark-person"></i> View Creators of Contents
        </a>
    </div>

    <footer class="footer">
        <div class="footer-content">© Copyright Fabio Pastana & João Ramos</div>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL"
        crossorigin="anonymous"></script>
</body>
</html>
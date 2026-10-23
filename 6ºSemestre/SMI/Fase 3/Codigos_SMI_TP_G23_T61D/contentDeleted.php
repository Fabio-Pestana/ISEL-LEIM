<?php
session_start();

if(!isset($_SESSION['user'])){
    header("Location: login.php");
    exit();
}

if(!isset($_SESSION['selected_edition'])) {
    header("Location: editHistory.php");
    exit();
}

require_once 'db.php';
$conn = db_connect();

$editionDateTime = $_SESSION['selected_edition'];

$editionQuery = "SELECT * FROM Edition WHERE EditionDateTime = ?";
$stmt = $conn->prepare($editionQuery);
$stmt->bind_param("s", $editionDateTime);
$stmt->execute();
$editionResult = $stmt->get_result();

if ($editionResult->num_rows === 0) {
    header("Location: editHistory.php");
    exit();
}

$editionData = $editionResult->fetch_assoc();
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" type="text/css" href="./Css/pageWiki.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <title><?= $editionData['TypeOfChange'] ?></title>
</head>
<body>
    
    <div class="wikiHeader">
        <div class="logo-container">
            <img src="./Images/logo.png" alt="NexusWiki Logo" class="logo">
        </div>
    </div>

    <div class="nav-row">
        <a href="editHistory.php" class="back-arrow">
            <i class="bi bi-arrow-left"></i>
        </a>
        <div class="profile-icon-container">
            <a href="profile.php" class="profile-icon">
                <i class="bi bi-person-circle"></i>
            </a>
        </div>
    </div>

    <div class="wiki-info">
        <div class="wiki-info-left">
            <img src="<?= $editionData['ImgDeleted'] ?>" alt="Wiki Deleted Image">
            <div>
                <div class="wiki-title"><?= $editionData['NameDeleted'] ?></div>
                <div class="page-indicator"><?= $editionData['TypeOfChange'] ?></div>
                <div class="pageDeleted-indicator" style="padding-top:8px;">Deleted by: <?= $editionData['Editor'] ?></div>
                <div class="pageDeleted-indicator">Date and time: <?= $editionData['EditionDateTime'] ?></div>
            </div>
        </div>
    </div>

    <div class="container">
        <h2 class="title">Deleted Description</h2>
        <div class="page-description"><?= $editionData['DescriptionDeleted'] ?></div>
    </div>

    <footer class="footer">
        <div class="footer-content">© Copyright Fabio Pastana & João Ramos</div>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL"
        crossorigin="anonymous"></script>

</body>
</html>

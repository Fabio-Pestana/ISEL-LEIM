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
    <title>NexusWiki - Edition Details</title>
    <link rel="stylesheet" type="text/css" href="./Css/edition.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
</head>
<body>
    <header class="wikiHeader">
        <div class="logo-container">
            <img src="./Images/logo.png" alt="NexusWiki Logo" class="logo">
        </div>
    </header>

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

    <div class="editionContainer">
        <h1><?= $editionData['TypeOfChange'] ?></h1>
        
        <div style="margin-bottom: 30px; text-align: center;">
            <p><strong>Editor:</strong> <?= $editionData['Editor'] ?></p>
            <p><strong>Date/Time:</strong> <?= $editionData['EditionDateTime'] ?></p>
        </div>

        <div class="content-section">
            <div class="content-box">
                <h3>Old Content</h3>
                <?php if (str_ends_with($editionData['TypeOfChange'], 'Image')): ?>
                    <img src="<?= $editionData['OldContent'] ?>" class="image-content" alt="Old Image">
                <?php else: ?>
                    <div class="text-content"><?= $editionData['OldContent'] ?></div>
                <?php endif; ?>
            </div>

            <div class="content-box">
                <h3>New Content</h3>
                <?php if (str_ends_with($editionData['TypeOfChange'], 'Image')): ?>
                    <img src="<?= $editionData['NewContent'] ?>" class="image-content" alt="New Image">
                <?php else: ?>
                    <div class="text-content"><?= $editionData['NewContent'] ?></div>
                <?php endif; ?>
            </div>
        </div>
    </div>

    <footer class="footer">
        <div class="footer-content">© Copyright Fabio Pastana & João Ramos</div>
    </footer>
</body>
</html>
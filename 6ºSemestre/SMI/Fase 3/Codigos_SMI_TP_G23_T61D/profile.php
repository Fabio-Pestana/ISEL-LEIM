<?php
session_start();

if (!isset($_SESSION['user'])) {
    header("Location: login.php");
    exit();
}

require_once 'db.php';
$conn = db_connect();

if(isset($_POST['selected_wiki'])) {
    $_SESSION['selected_wiki'] = $_POST['selected_wiki'];
    header("Location: mainWikiPage.php");
    exit();
}

$email = $_SESSION['user']['Email'];
$stmt = $conn->prepare("SELECT * FROM User WHERE Email = ?");
$stmt->bind_param("s", $email);
$stmt->execute();
$result = $stmt->get_result();
$user = $result->fetch_assoc();
$_SESSION['user'] = $user;
$userEmail = $user['Email'];

$userType = "Normal User";
if($_SESSION['user']['Tipo'] === 'Administrador'){ 
    $userType = "Administrator";
}else if($_SESSION['user']['Tipo'] === 'Simpatizante'){
    $userType = "Sympathizer";
}

$wikis = [];
$query = "SELECT w.Name, w.Image, w.Description, w.Category 
          FROM Wiki w 
          JOIN User_Wiki uw ON w.Name = uw.Name 
          WHERE uw.Email = ?";
$stmt = $conn->prepare($query);
$stmt->bind_param("s", $userEmail);
$stmt->execute();
$result = $stmt->get_result();

while ($row = $result->fetch_assoc()) {
    $wikis[] = $row;
}

$conn->close();
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>NexusWiki - Profile</title>
    <link rel="stylesheet" type="text/css" href="./Css/profile.css">
    <link rel="stylesheet" type="text/css" href="./Css/mainPage.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
</head>
<body>
    <?php if (isset($_SESSION['message'])): ?>
        <script>
            alert("<?= $_SESSION['message'] ?>");
        </script>
        <?php unset($_SESSION['message']); ?>
    <?php endif; ?>

    <header class="wikiHeader">
        <div class="logo-container">
            <img src="./Images/logo.png" alt="NexusWiki Logo" class="logo">
        </div>
        <div class="header-buttons">
            <a href="changeUserSettings.php">Change User Settings</a>
        </div>
    </header>

    <a href="mainPage.php" class="back-arrow">
        <i class="bi bi-arrow-left"></i>
    </a>

    <div class="profile-container">
        <h2 class="title">User Profile</h2>
        <p><strong>Name:</strong> <?= $user['Name'] ?></p>
        <p><strong>Email:</strong> <?= $user['Email'] ?></p>
        <p><strong>User Type:</strong> <?= $userType ?></p>
    </div>

    <div class="main-container">
        <?php if (count($wikis) > 0): ?>
            <h1 class="containerTitle">Wikis subscribed</h1>
            <div class="wiki-grid">
                <?php foreach ($wikis as $wiki): ?>
                    <form method="POST" action="" class="wiki-form">
                        <input type="hidden" name="selected_wiki" value="<?= $wiki['Name']?>">
                        <button type="submit" class="wiki-item" style="border: none; cursor: pointer; padding: 0;">
                            <div class="wiki-image-container">
                                <img src="<?= $wiki['Image'] ?>" alt="<?= $wiki['Name'] ?>" class="wiki-image">
                            </div>
                            <div class="wiki-info">
                                <h3 class="wiki-title"><?= $wiki['Name'] ?></h3>
                            </div>
                        </button>
                    </form>
                <?php endforeach; ?>
            </div>
        <?php endif; ?>
    </div>

    <div class="logout-container">
        <a href="logout.php" class="logout-button">Logout</a>
    </div>

    <footer class="footer">
        <div class="footer-content">© Copyright Fabio Pastana & João Ramos</div>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL"
        crossorigin="anonymous"></script>
</body>
</html>

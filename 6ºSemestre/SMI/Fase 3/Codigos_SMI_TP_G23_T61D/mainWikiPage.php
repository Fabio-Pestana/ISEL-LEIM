<?php
session_start();

if(!isset($_SESSION['user'])){
    header("Location: login.php");
    exit();
}

if(!isset($_SESSION['selected_wiki'])) {
    header("Location: mainPage.php");
    exit();
}

require_once 'db.php';
$conn = db_connect();

$wikiName = $_SESSION['selected_wiki'];
$userEmail = $_SESSION['user']['Email'];

$wikiQuery = "SELECT * FROM Wiki WHERE Name = ?";
$stmt = $conn->prepare($wikiQuery);
$stmt->bind_param("s", $wikiName);
$stmt->execute();
$wikiResult = $stmt->get_result();

if ($wikiResult->num_rows === 0) {
    header("Location: mainPage.php");
    exit();
}

$wikiData = $wikiResult->fetch_assoc();

// Admin e Simpatizante veem todas o resto so as publicas
if ($_SESSION['user']['Tipo'] === 'Administrador' || $_SESSION['user']['Tipo'] === 'Simpatizante') {
    $pagesQuery = "SELECT * FROM Page WHERE Wiki = ? ORDER BY Name ASC";
}else{
    $pagesQuery = "SELECT * FROM Page WHERE Wiki = ? AND isVisible = TRUE ORDER BY Name ASC";
}

$stmt = $conn->prepare($pagesQuery);
$stmt->bind_param("s", $wikiName);
$stmt->execute();
$pagesResult = $stmt->get_result();

$pages = [];
while ($row = $pagesResult->fetch_assoc()) {
    $pages[] = $row;
}

$isSubscribed = false;
if ($_SESSION['user']['Tipo'] !== 'Convidado') {
    $subscriptionQuery = "SELECT * FROM User_Wiki WHERE Email = ? AND Name = ?";
    $stmt = $conn->prepare($subscriptionQuery);
    $stmt->bind_param("ss", $userEmail, $wikiName);
    $stmt->execute();
    $subscriptionResult = $stmt->get_result();
    $isSubscribed = $subscriptionResult->num_rows > 0;
}

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    if (isset($_POST['page_name'])) 
    {
        $_SESSION['selected_page'] = $_POST['page_name'];
        header("Location: wikiPage.php");
        exit();
    } else if (isset($_POST['toggle_subscription'])) {
        if ($_SESSION['user']['Tipo'] === 'Convidado')
        {
            echo "<script>
                if(confirm('To subscribe to this wiki, you need to log in first. Would you like to go to the login page?')) {
                    window.location.href = 'login.php';
                }
            </script>";
        } else {
            if ($isSubscribed) 
            {
                // Unsubscribe
                $deleteQuery = "DELETE FROM User_Wiki WHERE Email = ? AND Name = ?";
                $stmt = $conn->prepare($deleteQuery);
                $stmt->bind_param("ss", $userEmail, $wikiName);
                $stmt->execute();
                $isSubscribed = false;
            } else {
                // Subscribe
                $insertQuery = "INSERT INTO User_Wiki (Email, Name) VALUES (?, ?)";
                $stmt = $conn->prepare($insertQuery);
                $stmt->bind_param("ss", $userEmail, $wikiName);
                $stmt->execute();
                $isSubscribed = true;
            }
        }
    }
}
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" type="text/css" href="./Css/mainWikiPage.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <title><?= $wikiData['Name'] ?> Wiki</title>
    <style>
        .video-container {
            background-color: #9A3324;
            margin: 20px auto;
            padding: 30px;
            border-radius: 8px;
            max-width: 1100px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            width: 100%;
            box-sizing: border-box;
            justify-items: center;
        }
        .video-card {
            width: 100%;
            max-width: 800px;
            aspect-ratio: 16 / 9;
            border-radius: 8px;
            overflow: hidden;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
        }

        .video-card iframe {
            width: 100%;
            height: 100%;
            border: none;
            display: block;
        }
    </style>
</head>
<body>
    <?php if (isset($_SESSION['message'])): ?>
        <script>
            alert("<?= $_SESSION['message'] ?>");
        </script>
        <?php unset($_SESSION['message']); ?>
    <?php endif; ?>
    
<div class="wikiHeader">
    <div class="logo-container">
        <img src="./Images/logo.png" alt="NexusWiki Logo" class="logo">
    </div>
    <div class="header-buttons">
        <?php if ($_SESSION['user']['Tipo'] === 'Administrador'): ?>
            <a href="deleteWiki.php" onclick="confirmDelete(event)">Delete Wiki</a>
        <?php endif; ?>
        <?php if ($_SESSION['user']['Tipo'] === 'Administrador' || $_SESSION['user']['Tipo'] === 'Simpatizante'): ?>
            <a href="createWikiPage.php">Create Page</a>
        <?php endif; ?>
    </div>
</div>

<div class="nav-row">
    <a href="mainPage.php" class="back-arrow">
        <i class="bi bi-arrow-left"></i>
    </a>
    <div class="profile-icon-container">
        <?php if ($_SESSION['user']['Tipo'] !== 'Convidado'): ?>
            <a href="profile.php" class="profile-icon">
                <i class="bi bi-person-circle"></i>
            </a>
        <?php else: ?>
            <a href="login.php" class="profile-icon">
                <i class="bi bi-person-circle"></i>
            </a>        
        <?php endif; ?>
    </div>
</div>

<div class="wiki-info">
    <div class="wiki-info-left">
        <img src="<?= $wikiData['Image'] ?>" alt="Wiki Image">
        <div>
            <div class="wiki-title"><?= $wikiData['Name'] ?> Wiki</div>
            <div class="home-indicator">Home Page</div>
        </div>
    </div>
    <form method="POST" id="subscriptionForm">
        <input type="hidden" name="toggle_subscription" value="1">
        <div class="notification" id="notificationBtn" onclick="document.getElementById('subscriptionForm').submit()">
            <i class="bi <?= $isSubscribed ? 'bi-bell-fill' : 'bi-bell' ?>" id="bellIcon"></i>
        </div>
    </form>
</div>

<div class="container">
    <h2 class="title">Welcome to the <?= $wikiData['Name'] ?> Wiki!</h2>
    <p class="wiki-description"><?= $wikiData['Description'] ?></p>
</div>

<?php if (!empty($pages)): ?>
    <div class="pages-container">
        <h2 class="title">Pages</h2>
        <form method="POST" class="pages-form" id="pagesForm">
            <input type="hidden" name="page_name" id="selectedPageInput">
            <div class="pages-grid">
                <?php foreach ($pages as $page): ?>
                    <button type="button" class="page-button" onclick="selectPage('<?= $page['Name'] ?>')">
                        <?= $page['Name'] ?>
                    </button>
                <?php endforeach; ?>
            </div>
        </form>
    </div>
<?php endif; ?>

<?php if (!empty($wikiData['YoutubeId'])): ?>
    <div class="video-container">
        <h2 class="title"><?= $wikiData['Name'] ?> Video!</h2>
        <div class="video-card">
                <iframe src="https://www.youtube.com/embed/<?= $wikiData['YoutubeId'] ?>" frameborder="0" allowfullscreen>
                </iframe>
        </div>
    </div>
<?php endif; ?>

<footer class="footer">
    <div class="footer-content">© Copyright Fabio Pastana & João Ramos</div>
</footer>

<script>
    function selectPage(pageName) {
        document.getElementById('selectedPageInput').value = pageName;
        document.getElementById('pagesForm').submit();
    }

    function confirmDelete(event) {
        event.preventDefault();

        const confirmed = confirm("Are you sure you want to delete this Wiki?");
        if (confirmed) {
            window.location.href = "deleteWiki.php";
        }
    }
</script>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"
    integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL"
    crossorigin="anonymous"></script>

</body>
</html>
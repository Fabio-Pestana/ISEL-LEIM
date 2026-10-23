<?php
session_start();

if (!isset($_SESSION['user'])) {
    header("Location: login.php");
    exit();
}

if (!isset($_SESSION['selected_wiki']) || !isset($_SESSION['selected_page'])) {
    header("Location: mainPage.php");
    exit();
}

require_once 'db.php';
$conn = db_connect();

$wikiName = $_SESSION['selected_wiki'];
$pageName = $_SESSION['selected_page'];
$userEmail = $_SESSION['user']['Email'];

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    if(isset($_POST['information_name'])){
        $_SESSION['selected_information'] = $_POST['information_name'];
        header("Location: editInformation.php");
        exit();
    }else if (isset($_POST['toggle_visibility'])){
        $newVisibility = $_POST['toggle_visibility'];
        $updateQuery = "UPDATE Page SET isVisible = ? WHERE Name = ? AND Wiki = ?";
        $stmt = $conn->prepare($updateQuery);
        $stmt->bind_param("iss", $newVisibility, $pageName, $wikiName);
        $stmt->execute();
    }
}

$pageQuery = "SELECT * FROM Page WHERE Name = ? AND Wiki = ?";
$stmt = $conn->prepare($pageQuery);
$stmt->bind_param("ss", $pageName, $wikiName);
$stmt->execute();
$pageResult = $stmt->get_result();

if ($pageResult->num_rows === 0) {
    header("Location: mainPage.php");
    exit();
}

$pageData = $pageResult->fetch_assoc();

$wikiQuery = "SELECT * FROM Wiki WHERE Name = ?";
$stmt = $conn->prepare($wikiQuery);
$stmt->bind_param("s", $wikiName);
$stmt->execute();
$wikiResult = $stmt->get_result();
$wikiData = $wikiResult->fetch_assoc();

$informationQuery = "SELECT * FROM Information WHERE Page = ? AND Wiki = ? ORDER BY Name ASC";
$stmt = $conn->prepare($informationQuery);
$stmt->bind_param("ss", $pageName, $wikiName);
$stmt->execute();
$informationResult = $stmt->get_result();

$information = [];
while ($row = $informationResult->fetch_assoc()) {
    $information[] = $row;
}
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" type="text/css" href="./Css/pageWiki.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <title><?= $wikiData['Name'] ?> - <?= $pageData['Name'] ?></title>
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
            <a href="deletePage.php" onclick="confirmDelete(event)">Delete Page</a>
        <?php endif; ?>
        <?php if ($_SESSION['user']['Tipo'] === 'Administrador' || $_SESSION['user']['Tipo'] === 'Simpatizante'): ?>
            <form method="POST" style="display: inline;">
                <input type="hidden" name="toggle_visibility" value="<?=$pageData['isVisible'] ? 0 : 1?>">
                <button type="submit" class="visibility-toggle">
                    <?= $pageData['isVisible'] ? 'Make Private' : 'Make Public' ?>
                </button>
            </form>
            <a href="createWikiInformation.php">Add Content</a>
        <?php endif; ?>
    </div>
</div>

<div class="nav-row">
    <a href="mainWikiPage.php" class="back-arrow">
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
            <div class="page-indicator"><?= $pageData['Name'] ?></div>
        </div>
    </div>
</div>

<div class="container">
    <div class="container-header">
        <h2 class="title"><?= $pageData['Name'] ?> in <?= $wikiData['Name'] ?></h2>
        <?php if ($_SESSION['user']['Tipo'] === 'Administrador' || $_SESSION['user']['Tipo'] === 'Simpatizante'): ?>
            <a href="editPage.php" class="edit-icon" title="Edit Page">
                <i class="bi bi-pen-fill"></i>
            </a>
        <?php endif; ?>
    </div>
    <div class="container-body">
        <div class="page-description"><?= $pageData['Description'] ?></div>
        <div class="page-image">
            <img src="<?= $pageData['Img'] ?>" alt="Page Image">
        </div>
    </div>
</div>

<?php if (!empty($information)): ?>
    <div class="content-index">
        <h2>Content</h2>
        <ol class="index-list">
            <?php foreach ($information as $info): ?>
                <li><a href="#info-<?= $info['Name'] ?>"><?= $info['Name'] ?></a></li>
            <?php endforeach; ?>
        </ol>
    </div>

    <?php foreach ($information as $info): ?>
        <div class="info-container" id="info-<?= $info['Name'] ?>">
            <div class="info-header">
                <h2 class="title"><?= $info['Name'] ?></h2>
                <?php if ($_SESSION['user']['Tipo'] === 'Administrador' || $_SESSION['user']['Tipo'] === 'Simpatizante'): ?>
                    <?php $formId = "editForm_" . $info['Name']; ?>
                    <form method="POST" id="<?= $formId ?>">
                        <input type="hidden" name="information_name" value="<?= $info['Name'] ?>">
                        <div class="edit-icon" onclick="document.getElementById('<?= $formId ?>').submit()">
                            <i class="bi bi-pen-fill"></i>
                        </div>
                    </form>
                <?php endif; ?>
            </div>
            <div class="info-body">
                <div class="info-content"><?= $info['Description'] ?></div>
                <div class="info-image">
                    <img src="<?= $info['Img'] ?>" alt="Information Image">
                </div>
            </div>
        </div>
    <?php endforeach; ?>
<?php endif; ?>

<footer class="footer">
    <div class="footer-content">© Copyright Fabio Pastana & João Ramos</div>
</footer>

<script>
    function confirmDelete(event) {
        event.preventDefault();

        const confirmed = confirm("Are you sure you want to delete this Page?");
        if (confirmed) {
            window.location.href = "deletePage.php";
        }
    }
</script>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"
    integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL"
    crossorigin="anonymous"></script>

</body>
</html>
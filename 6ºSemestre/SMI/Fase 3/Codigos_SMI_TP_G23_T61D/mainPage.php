<?php
session_start();

if(!isset($_SESSION['user'])){
    header("Location: login.php");
    exit();
}

require_once 'db.php';
$conn = db_connect();

// Wiki selecionada
if(isset($_POST['selected_wiki'])) {
    $_SESSION['selected_wiki'] = $_POST['selected_wiki'];
    header("Location: mainWikiPage.php");
    exit();
}

$wikis = [];
$stmt = $conn->prepare("SELECT w.Name, w.Image, w.Category FROM Wiki w");
$stmt->execute();
$result = $stmt->get_result();
while ($row = $result->fetch_assoc()) {
    $wikis[] = $row;
}

// Categorias para o dropdown
$categories = [];
$stmt = $conn->prepare("SELECT Name FROM Category");
$stmt->execute();
$result = $stmt->get_result();
while ($row = $result->fetch_assoc()) {
    $categories[] = $row['Name'];
}

$conn->close();

// Filtros das Wikis
$searchTerm = isset($_GET['search']) ? trim($_GET['search']) : '';
$selectedCategory = isset($_GET['category']) ? $_GET['category'] : '';
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>NexusWiki - Lista de Wikis</title>
    <link rel="stylesheet" type="text/css" href="./Css/style.css">
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
            <?php if ($_SESSION['user']['Tipo'] === 'Administrador'): ?>
                <a href="createWiki.php">Create Wiki</a>
                <a href="adminSettings.php">System Settings</a>
            <?php endif; ?>
        </div>
    </header>

    <?php if ($_SESSION['user']['Tipo'] !== 'Convidado'): ?>
        <a href="profile.php" class="profileIcon">
            <i class="bi bi-person-circle"></i>
        </a>
    <?php else: ?>
        <a href="login.php" class="profileIcon">
            <i class="bi bi-person-circle"></i>
        </a>        
    <?php endif; ?>

    <div class="main-container">
        <div class="filter-container">
            <form method="GET" class="filter-form">
                <div class="search-box">
                    <input class="searchInput" type="text" name="search" placeholder="Search for a wiki" value="<?= htmlspecialchars($searchTerm) ?>">
                </div>
                <div class="category-filter">
                    <select name="category" onchange="this.form.submit()">
                        <option value="">Category</option>
                        <?php foreach ($categories as $category): ?>
                            <option value="<?= $category ?>" <?= ($selectedCategory === $category) ? 'selected' : '' ?>> 
                                <?= $category ?>
                            </option>
                        <?php endforeach; ?>
                    </select>
                </div>
            </form>
        </div>

        <div class="wiki-grid">
            <?php 
            // Filtrar wikis
            $filteredWikis = array_filter($wikis, function($wiki) use ($selectedCategory, $searchTerm) {
                $categoryMatch = empty($selectedCategory) || $wiki['Category'] === $selectedCategory;
                $searchMatch = empty($searchTerm) || stripos($wiki['Name'], $searchTerm) !== false;
                return $categoryMatch && $searchMatch;
            });
            
            if (count($filteredWikis) > 0): 
                foreach ($filteredWikis as $wiki): 
            ?>
                <form method="POST" action="" class="wiki-form">
                    <input type="hidden" name="selected_wiki" value="<?= $wiki['Name'] ?>">
                    <button type="submit" class="wiki-item" style="border: none; cursor: pointer; padding: 0;">
                        <div class="wiki-image-container">
                            <img src="<?= $wiki['Image'] ?>" alt="<?= $wiki['Name'] ?>" class="wiki-image">
                        </div>
                        <div class="wiki-info">
                            <h3 class="wiki-title"><?= $wiki['Name'] ?></h3>
                        </div>
                    </button>
                </form>
            <?php 
                endforeach;
            else: 
            ?>
                <div class="no-wikis">
                    No wikis found with selected criteria!
                </div>
            <?php endif; ?>
        </div>
    </div>

    <footer class="footer">
        <div class="footer-content">© Copyright Fabio Pastana & João Ramos</div>
    </footer>

    <script>
        // Submeter o formulário quando o campo de pesquisa deixa de estar selecionado
        document.querySelector('input[name="search"]').addEventListener('blur', function() {
            this.form.submit();
        });
        
        // Submeter ao pressionar Enter
        document.querySelector('input[name="search"]').addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                this.form.submit();
            }
        });
    </script>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL"
        crossorigin="anonymous"></script>
</body>
</html>
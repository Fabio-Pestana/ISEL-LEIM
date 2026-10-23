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

require_once 'db.php';
$conn = db_connect();

$pageQuery = "SELECT u.Name AS CreatorName, u.Email, p.Name AS PageName, p.Wiki, 'Page' AS Type, NULL AS InformationName
              FROM Page p
              JOIN User u ON p.Creator = u.Email
              ORDER BY u.Name, p.Wiki, p.Name";



$infoQuery = "SELECT u.Name AS CreatorName, u.Email, i.Page AS PageName, i.Wiki, 'Information' AS Type, i.Name AS InformationName
              FROM Information i
              JOIN User u ON i.Creator = u.Email
              ORDER BY u.Name, i.Wiki, i.Page, i.Name";


$combinedQuery = "($pageQuery) UNION ALL ($infoQuery) ORDER BY CreatorName, Wiki, PageName, InformationName";

$result = $conn->query($combinedQuery);
while ($row = $result->fetch_assoc()) {
    $rows[] = $row;
}
$conn->close();
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>NexusWiki - Content Creators</title>
    <link rel="stylesheet" type="text/css" href="./Css/userManagement.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
</head>
<body>
    <header class="wikiHeader">
        <div class="logo-container">
            <img src="./Images/logo.png" alt="NexusWiki Logo" class="logo">
        </div>
    </header>

    <div class="nav-row">
        <a href="adminSettings.php" class="back-arrow">
            <i class="bi bi-arrow-left"></i>
        </a>
        <div class="profile-icon-container">
            <a href="profile.php" class="profile-icon">
                <i class="bi bi-person-circle"></i>
            </a>
        </div>
    </div>

    <div class="userManagementContainer">
        <h1>Content Creators</h1>
        
        <div class="filter-section">
            <div style="margin-left: auto; padding-top: 20px;">
                <select id="filterType" onchange="filterTable()" style="padding: 8px; border-radius: 4px; border: none;">
                    <option value="all">Filter by Content Type</option>
                    <option value="Page">Pages</option>
                    <option value="Information">Information</option>
                </select>
            </div>
        </div>
        
        <table id="contentTable">
            <thead>
                <tr>
                    <th>Creator</th>
                    <th>Type</th>
                    <th>Wiki</th>
                    <th>Page</th>
                    <th>Information</th>
                </tr>
            </thead>
            <tbody>
                <?php foreach ($rows as $r): ?>
                    <tr>
                        <td>
                            <strong><?= $r['CreatorName'] ?></strong><br>
                            <small><?= $r['Email'] ?></small>
                        </td>
                        <td><?= $r['Type'] ?></td>
                        <td><?= $r['Wiki'] ?></td>
                        <td><?= $r['PageName'] ?></td>
                        <td><?= $r['InformationName'] ? $r['InformationName'] : '-' ?></td>
                    </tr>
                <?php endforeach; ?>
            </tbody>
        </table>
    </div>

    <footer class="footer">
        <div class="footer-content">© Copyright Fabio Pastana & João Ramos</div>
    </footer>

    <script>
        function filterTable() {
            const filterType = document.getElementById('filterType').value;
            const table = document.getElementById('contentTable');
            const rows = table.getElementsByTagName('tr');
            
            for (let i = 1; i < rows.length; i++) {
                const row = rows[i];
                const type = row.cells[1].textContent;
                
                const typeMatch = filterType === 'all' || type === filterType;
                
                if (typeMatch) {
                    row.style.display = '';
                } else {
                    row.style.display = 'none';
                }
            }
        }
    </script>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL"
        crossorigin="anonymous"></script>
</body>
</html>
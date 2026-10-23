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

$editionsQuery = "SELECT e.EditionDateTime, e.Editor, e.TypeOfChange, u.Name as EditorName 
                  FROM Edition e
                  JOIN User u ON e.Editor = u.Email
                  ORDER BY e.EditionDateTime DESC";
$editionsResult = $conn->query($editionsQuery);

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $edition = $_POST['edition'] ?? '';
    if (!empty($edition)) {
        
        $checkQuery = "SELECT TypeOfChange FROM Edition WHERE EditionDateTime = ?";
        $stmt = $conn->prepare($checkQuery);
        $stmt->bind_param("s", $edition);
        $stmt->execute();
        $typeResult = $stmt->get_result();

        if ($typeResult->num_rows > 0) {
            $typeRow = $typeResult->fetch_assoc();
            $typeOfChange = $typeRow['TypeOfChange'];
            $_SESSION['selected_edition'] = $edition;
            
            if (str_ends_with($typeOfChange, 'Deleted')) {
                header("Location: contentDeleted.php");
            } else {
                header("Location: edition.php");
            }
            exit();
        }
    }
}
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>NexusWiki - History of Editions</title>
    <link rel="stylesheet" type="text/css" href="./Css/userManagement.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <style>
        .clickable-row {
            cursor: pointer;
            transition: background-color 0.3s;
        }
        
        .clickable-row:hover {
            background-color: #f2f2f2;
        }
        
        .editions-table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 20px;
            background-color: white;
            color: black;
        }
        
        .editions-table th, 
        .editions-table td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        
        .editions-table th {
            background-color: #7D2A1F;
            color: white;
        }
    </style>
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
        <h1>History of Editions</h1>
        <h3 style="text-align: center;">Click on a row to see the details of the Edition</h3>

        <div class="action-section">
            <form method="POST" id="editionForm">
                <input type="hidden" id="edition" name="edition" value="">
                
                <table class="editions-table">
                    <thead>
                        <tr>
                            <th>Editor</th>
                            <th>Date/Time</th>
                            <th>Type of Change</th>
                        </tr>
                    </thead>
                    <tbody>
                        <?php while ($edition = $editionsResult->fetch_assoc()): ?>
                        <tr class="clickable-row" 
                            onclick="document.getElementById('edition').value='<?= $edition['EditionDateTime'] ?>'; document.getElementById('editionForm').submit();">
                            <td><?= $edition['EditorName'] ?> (<?= $edition['Editor'] ?>)</td>
                            <td><?= $edition['EditionDateTime'] ?></td>
                            <td><?= $edition['TypeOfChange'] ?></td>
                        </tr>
                        <?php endwhile; ?>
                    </tbody>
                </table>
            </form>
        </div>
    </div>

    <footer class="footer">
        <div class="footer-content">© Copyright Fabio Pastana & João Ramos</div>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL"
        crossorigin="anonymous"></script>
</body>
</html>
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

$usersQuery = "SELECT Email, Name, Tipo FROM User";
$usersResult = $conn->query($usersQuery);

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $userEmail = $_POST['userEmail'];
    $action = $_POST['action'];
    
    if (!empty($userEmail) && !empty($action)) {
        // Verifica se o user existe
        $checkUser = $conn->prepare("SELECT Email FROM User WHERE Email = ?");
        $checkUser->bind_param("s", $userEmail);
        $checkUser->execute();
        $checkUser->store_result();
        
        if ($checkUser->num_rows === 0) {
            $_SESSION['message'] = "User not found: " .$userEmail;
            header("Location: userManagement.php");
            exit();
        }
        
        switch ($action) {
            case 'delete':
                // Admin nao pode fazer delete de ele proprio
                if ($userEmail === $_SESSION['user']['Email']) {
                    $_SESSION['message'] = "You cannot delete yourself!";
                    header("Location: userManagement.php");
                    exit();
                }
                
                $_SESSION['deleteUser'] = $userEmail;
                header("Location: deleteUser.php");
                exit();
                
            case 'changeName':
                $newName = $_POST['newName'];
                if (!empty($newName)) {
                    $updateQuery = "UPDATE User SET Name = ? WHERE Email = ?";
                    $stmt = $conn->prepare($updateQuery);
                    $stmt->bind_param("ss", $newName, $userEmail);
                    if ($stmt->execute()) {
                        $_SESSION['message'] = "Username changed successfully!";
                    } else {
                        $_SESSION['message'] = "Error changing username!";
                    }
                }
                break;
                
            case 'changeTipo':
                $newTipo = $_POST['newTipo'];
                if (!empty($newTipo)) {
                    // Admin nao pode alterar o seu tipo
                    if ($userEmail === $_SESSION['user']['Email']) {
                        $_SESSION['message'] = "You cannot change your own type!";
                        header("Location: userManagement.php");
                        exit();
                    }
                    
                    $updateQuery = "UPDATE User SET Tipo = ? WHERE Email = ?";
                    $stmt = $conn->prepare($updateQuery);
                    $stmt->bind_param("ss", $newTipo, $userEmail);
                    if ($stmt->execute()) {
                        $_SESSION['message'] = "User type changed successfully!";
                    } else {
                        $_SESSION['message'] = "Error changing user type!";
                    }
                }
                break;
        }
        
        header("Location: userManagement.php");
        exit();
    }
}
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>NexusWiki - User Management</title>
    <link rel="stylesheet" type="text/css" href="./Css/userManagement.css">
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
        <h1>User Management</h1>

        <div class="action-section">
            <form method="POST">
                <div style="margin-bottom: 10px; ">
                    <input type="text" id="userEmail" name="userEmail" placeholder="User email to change information or delete" required
                        style="width: 100%; padding-top: 10px; box-sizing: border-box; ">
                </div>
                
                <div style="margin-bottom: 10px; ">
                    <select id="action" name="action" onchange="showActionFields()" required
                            style="width: 100%; padding-top: 10px; box-sizing: border-box;">
                        <option value="">Select action</option>
                        <option value="delete">Delete User</option>
                        <option value="changeName">Change Name</option>
                        <option value="changeTipo">Change Type</option>
                    </select>
                </div>
                
                <div id="changeNameFields" class="hidden" style="margin-bottom: 10px;">
                    <input type="text" id="newName" name="newName" placeholder="New Name" class="conditional-required"
                        style="width: 100%; padding-top: 10px; box-sizing: border-box;">
                </div>
                
                <div id="changeTipoFields" class="hidden" style="margin-bottom: 10px;">
                    <select id="newTipo" name="newTipo" class="conditional-required"
                            style="width: 100%; padding-top: 10px; box-sizing: border-box;">
                        <option value="">Select new type</option>
                        <option value="Utilizador">Utilizador</option>
                        <option value="Simpatizante">Simpatizante</option>
                        <option value="Administrador">Administrador</option>
                    </select>
                </div>
                
                <button type="submit" class="action-btn" style="width: 100%;">Change/Delete</button>
            </form>
        </div>
        
        <div class="filter-section">
            <div style="margin-left: auto; padding-top: 20px;">
                <select id="filterType" onchange="filterTable()" style="padding: 8px; border-radius: 4px; border: none;">
                    <option value="all">Filter Users by type</option>
                    <option value="Utilizador">Users</option>
                    <option value="Simpatizante">Sympathizers</option>
                    <option value="Administrador">Administrators</option>
                </select>
            </div>
        </div>
        
        <table id="usersTable">
            <thead>
                <tr>
                    <th>Email</th>
                    <th>Name</th>
                    <th>Type</th>
                </tr>
            </thead>
            <tbody>
                <?php while ($user = $usersResult->fetch_assoc()): ?>
                <tr>
                    <td><?= $user['Email'] ?></td>
                    <td><?= $user['Name'] ?></td>
                    <td><?= $user['Tipo'] ?></td>
                </tr>
                <?php endwhile; ?>
            </tbody>
        </table>
    </div>

    <footer class="footer">
        <div class="footer-content">© Copyright Fabio Pastana & João Ramos</div>
    </footer>

    <script>
        function filterTable() {
            const filterType = document.getElementById('filterType').value;
            const table = document.getElementById('usersTable');
            const rows = table.getElementsByTagName('tr');
            
            for (let i = 1; i < rows.length; i++) {
                const row = rows[i];
                const email = row.cells[0].textContent.toLowerCase();
                const type = row.cells[2].textContent;
                
                const typeMatch = filterType === 'all' || type === filterType;
                
                if (typeMatch) {
                    row.style.display = '';
                } else {
                    row.style.display = 'none';
                }
            }
        }
        
        function showActionFields() {
            const action = document.getElementById('action').value;
            document.getElementById('changeNameFields').classList.add('hidden');
            document.getElementById('changeTipoFields').classList.add('hidden');
            
            document.querySelectorAll('.conditional-required').forEach(el => {
                el.removeAttribute('required');
            });
            
            if (action === 'changeName') {
                document.getElementById('changeNameFields').classList.remove('hidden');
                document.getElementById('newName').setAttribute('required', '');
            } else if (action === 'changeTipo') {
                document.getElementById('changeTipoFields').classList.remove('hidden');
                document.getElementById('newTipo').setAttribute('required', '');
            }
        }
    </script>


    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL"
        crossorigin="anonymous"></script>
</body>
</html>
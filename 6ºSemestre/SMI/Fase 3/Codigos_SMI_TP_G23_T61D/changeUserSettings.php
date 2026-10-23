<?php
session_start();

if(!isset($_SESSION['user'])){
    header("Location: login.php");
    exit();
}

// Convidados nao tem acesso a esta pagina
if($_SESSION['user']['Tipo'] === 'Convidado'){
    header("Location: mainPage.php");
    exit();
}

require_once 'db.php';
$conn = db_connect();

$message = '';
$error = '';

if($_SERVER['REQUEST_METHOD'] == 'POST'){
    $change = $_POST['changedField'];
    $newValue = trim($_POST['newValue']);
    $email = $_SESSION['user']['Email'];

    if($change === 'Password' && strlen($newValue) < 8) {
        $message = "Password must be at least 8 characters";
    }else if(!empty($change) && !empty($newValue)){
            if($change === 'Name') {
                $query = "UPDATE User SET Name = ? WHERE Email = ?";
            } 
            else if($change === 'Password') {
                $newValue = password_hash($newValue, PASSWORD_DEFAULT);
                $query = "UPDATE User SET Password = ? WHERE Email = ?";
            }

            $stmt = $conn->prepare($query);
            $stmt->bind_param("ss", $newValue, $email);
            
        if($stmt->execute()){
            $message = "Profile updated successfully!";
            $_SESSION['message'] = $message;
            $stmt->close();
            $conn->close();
            header("Location: mainPage.php");
            exit();
        }
    }
}

$conn->close();
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>NexusWiki - Change Profile</title>
    <link rel="stylesheet" type="text/css" href="./Css/style.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <style>
        .form-container {
            background-color: #9A3324;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            width: 350px;
            text-align: center;
            color: white;
        }
        .form-container select, 
        .form-container input {
            width: 100%;
            padding: 10px;
            margin: 10px 0;
            border-radius: 4px;
            border: 1px solid #ddd;
            box-sizing: border-box;
        }
        .success-message {
            color:rgb(255, 255, 255);
            margin-bottom: 15px;
        }
    </style>
</head>
<body>
    <header class="header">
        <div class="logo-container">
            <img src="./Images/logo.png" alt="NexusWiki Logo" class="logo">
        </div>
    </header>

    <a href="profile.php" class="back-arrow">
        <i class="bi bi-arrow-left"></i>
    </a>
    
    <main class="main-content">
        <div class="form-container">
            <h1>Change User Settings</h1>
            
            <form method="POST" id="changeProfile">
                <label for="changedField">What do you want to change?</label>
                <select id="changedField" name="changedField" required>
                    <option value="" disabled selected>Choose one option</option>
                    <option value="Name">Name</option>
                    <option value="Password">Password</option>
                </select>

                <input type="text" name="newValue" placeholder="New value" required>
                <div id="passwordNote" style="display: none; font-size: 0.8em; margin-bottom: 10px;">
                    (Password must be at least 8 characters)
                </div>

                <div class="divider"></div>
                
                <button type="submit" class="buttons">Update</button>  
            </form>

            <?php if(isset($message)): ?>
                <p><?= $message ?> </p>
            <?php endif; ?>
        </div>
    </main>
    
    <footer class="footer">
        <div class="footer-content">© Copyright Fabio Pastana & João Ramos</div>
    </footer>

    <script>
        document.addEventListener("DOMContentLoaded", function() {
            const fieldSelect = document.getElementById('changedField');
            const passwordNote = document.getElementById('passwordNote');
            
            fieldSelect.addEventListener('change', function() {
                if(this.value === 'Password') {
                    passwordNote.style.display = 'block';
                } else {
                    passwordNote.style.display = 'none';
                }
            });
        });
    </script>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL"
        crossorigin="anonymous"></script>
</body>
</html>
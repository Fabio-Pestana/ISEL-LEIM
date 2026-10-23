<?php
    session_start();

    require_once 'db.php';
    $conn = db_connect();

    if($_SERVER['REQUEST_METHOD'] == 'POST'){

        if(isset($_POST['email']) && isset($_POST['password'])){
            $email = $_POST['email'];
            $password = $_POST['password'];
            
            if(!empty($email) && !empty($password)){
                $stmt = $conn->prepare("SELECT * FROM User Where Email=?");
                $stmt->bind_param("s", $email);
                $stmt->execute();
                $result = $stmt->get_result();
                
                if($result->num_rows == 1){
                    $user = $result->fetch_assoc();

                    if(password_verify($password, $user['Password'])){
                        $_SESSION['user'] = $user;
                        header("Location: mainPage.php");
                        exit();
                    }
                }
                $msg = "Invalid username or password.";
            }
        }
        // Convidado
        else if(isset($_POST['guest_login'])){
            $_SESSION['user'] = [
                'Email' => 'guest@nexuswiki',
                'Name' => 'Guest',
                'Tipo' => 'Convidado'
            ];
            header("Location: mainPage.php");
            exit();
        }
    }
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>NexusWiki - Login</title>
    <link rel="stylesheet" type="text/css" href="./Css/style.css">
</head>
<body>
    <header class="header">
        <div class="logo-container">
            <img src="./Images/logo.png" alt="NexusWiki Logo" class="logo">
        </div>
    </header>
    
    <main class="main-content">
        <div class="form-container">
            <h1>Login</h1>
            
            <form method="POST">
                <input class="inputForm" type="email" placeholder="mail" name="email" required>
                <input class="inputForm" type="password" placeholder="password" name="password" required>
                
                <div class="divider"></div>
                
                <button type="submit" class="buttons">Login</button>
            </form>

            <form method="POST">
                <button type="submit" name="guest_login" class="buttons">Join as guest</button>
            </form>

            <?php if(isset($msg)): ?>
                <p><?= $msg ?> </p>
            <?php endif; ?>

            <a href="regist.php" class="signup-link">Don't Have An Account? Sign Up</a>
        </div>
    </main>
    
    <footer class="footer">
        <div class="footer-content">© Copyright Fabio Pastana & João Ramos</div>
    </footer>
</body>
</html>
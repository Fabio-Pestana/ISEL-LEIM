<?php
    session_start();

    require_once 'db.php';
    $conn = db_connect();

    if($_SERVER['REQUEST_METHOD'] == 'POST'){
    
        $name = $_POST['name'];
        $email = $_POST['email'];
        $password = $_POST['password'];
        $password2 = $_POST['password2'];

        $captcha_input = $_POST['captcha_input'] ?? '';
        $captcha_code = $_SESSION['captcha'] ?? '';
        
        if($password !== $password2) {
            $msg = "Passwords don't match!";

        }else if(strlen($password) < 8) {
            $msg = "Password must be at least 8 characters!";
        }else if(strtoupper($captcha_input) !== $captcha_code){
            $msg = "Invalid Captcha code. $captcha_input e $captcha_code";

        }else if(!empty($email) && !empty($password)){
            $stmt = $conn->prepare("SELECT * FROM User Where Email=?");
            $stmt->bind_param("s", $email);
            $stmt->execute();
            $result = $stmt->get_result();
                
            if($result->num_rows >= 1){
                $msg = "This email already has an account linked!";
            }else{
                $hashPassword = password_hash($password, PASSWORD_DEFAULT);
                $tipo = "Utilizador";

                $stmt = $conn->prepare("INSERT INTO User (Email, Password, Name, Tipo) Values (?, ?, ?, ?)");
                $stmt->bind_param("ssss", $email, $hashPassword, $name, $tipo);
                if($stmt->execute()) {
                    $_SESSION['user'] = [
                        'Email' => $email,
                        'Name' => $name,
                        'Tipo' => $tipo
                    ];
                    header("Location: mainPage.php");
                    exit();
                } else {
                    $msg = "Registration failed. Please try again.";
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
    <title>NexusWiki - Sign in</title>
    <link rel="stylesheet" type="text/css" href="./Css/style.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
</head>
<body>
    <header class="header">
        <div class="logo-container">
            <img src="./Images/logo.png" alt="NexusWiki Logo" class="logo">
        </div>
    </header>

    <a href="login.php" class="back-arrow">
        <i class="bi bi-arrow-left"></i>
    </a>
    
    <main class="main-content">
        <div class="form-container">
            <h1>Sign in</h1>
            
            <form method="POST">
                <input class="inputForm" type="text" placeholder="name" name="name" required>
                <input class="inputForm" type="email" placeholder="mail" name="email" required>
                <input class="inputForm" type="password" placeholder="password" name="password" id="password" required>
                <input class="inputForm" type="password" placeholder="confirm password" name="password2" id="confirmPassword" required>
                <input class="inputForm" type="text" placeholder="Enter the code from the image below" name="captcha_input" required>
                <br><img src="captcha.php" alt="CAPTCHA">
                
                <div class="divider"></div>
                
                <button type="submit" class="buttons">Sign in</button>  
            </form>

            <?php if(isset($msg)): ?>
                <p><?= $msg ?> </p>
            <?php endif; ?>

        </div>
    </main>
    
    <footer class="footer">
        <div class="footer-content">© Copyright Fabio Pastana & João Ramos</div>
    </footer>

    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL"
        crossorigin="anonymous"></script>

</body>
</html>
<?php
    session_start();

    if(!isset($_SESSION['user'])){
        header("Location: login.php");
        exit();
    }

    if(!isset($_SESSION['selected_wiki'])){
        header("Location: mainPage.php");
        exit();
    }

    require_once 'db.php';
    $conn = db_connect();

    $wikiName = $_SESSION['selected_wiki'];
    $userEmail = $_SESSION['user']['Email'];

    if($_SERVER['REQUEST_METHOD'] == 'POST'){
        $pageName = $_POST['name'];
        $description = $_POST['description'];
        $imageBase64 = $_POST['pictureBase64'];

        if(!empty($pageName) && !empty($description)){
            $stmt = $conn->prepare("SELECT * FROM Page WHERE Name=? AND Wiki=?");
            $stmt->bind_param("ss", $pageName, $wikiName);
            $stmt->execute();
            $result = $stmt->get_result();
                
            if($result->num_rows >= 1){
                $msg = "A page with this name already exists in this wiki!";
            } else {
                $creationDateTime = date('Y-m-d H:i:s');
                $isVisible = 1;
                $stmt = $conn->prepare("INSERT INTO Page (Name, Description, isVisible, CreationDateTime, Creator, Wiki, Img) VALUES (?, ?, ?, ?, ?, ?, ?)");
                $stmt->bind_param("ssissss", $pageName, $description, $isVisible, $creationDateTime, $userEmail, $wikiName, $imageBase64);
                
                if($stmt->execute()){
                    $msg = "Successfully created the new Page: " . $pageName;
                } else {
                    $msg = "Error creating page: " . $conn->error;
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
    <title>NexusWiki - Create Page</title>
    <link rel="stylesheet" type="text/css" href="./Css/create.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
</head>
<body>
    <header class="header">
        <div class="logo-container">
            <img src="./Images/logo.png" alt="NexusWiki Logo" class="logo">
        </div>
    </header>

    <a href="mainWikiPage.php" class="back-arrow">
        <i class="bi bi-arrow-left"></i>
    </a>
    
    <main class="main-content">
        <div class="form-container">
            <h1>Create Page in <?= $wikiName ?></h1>
            
            <form method="POST" id="createPage">
                <input type="text" placeholder="Page Name" name="name" required>
                
                <div class="form-group">
                    <textarea name="description" placeholder="Enter page content here..." required
                                style="height: 400px; width: 100%; resize: vertical; padding: 10px; box-sizing: border-box; background-color: #D9D9D9;"
                    ></textarea>
                </div>

                <div class="form-group" style="padding-top: 10px; text-align: left;">
                    <label for="file-upload" style="color: white;">Upload Image (JPEG only):</label>
                    <input type="file" id="file-upload" name="picture" accept="image/jpeg">
                </div>

                <input type="hidden" id="pictureBase64" name="pictureBase64">

                <div class="divider"></div>
                
                <button type="submit" class="buttons">Create Page</button>  
            </form>

            <?php if(isset($msg)): ?>
                <p><?= $msg ?> </p>
            <?php endif; ?>

        </div>
    </main>
    
    <footer class="footer">
        <div class="footer-content">© Copyright Fabio Pastana & João Ramos</div>
    </footer>

    <script>
        document.addEventListener("DOMContentLoaded", function () {
            const form = document.getElementById('createPage');
            form.addEventListener('submit', function (event) {
                event.preventDefault();
                const fileInput = document.getElementById('file-upload');
                const file = fileInput.files[0];

                if (file) {
                    const reader = new FileReader();
                    reader.onload = function () {
                        const imageData = reader.result;
                        document.getElementById('pictureBase64').value = imageData;
                        form.submit();
                    }
                    reader.readAsDataURL(file);
                } else {
                    alert("Erro: Por favor, selecione uma imagem.");
                }
            });
        });
    </script>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL"
        crossorigin="anonymous"></script>
</body>
</html>
<?php
    session_start();

    if(!isset($_SESSION['user'])){
        header("Location: login.php");
        exit();
    }

    // So admins tem acesso
    if($_SESSION['user']['Tipo'] !== 'Administrador'){
        header("Location: mainPage.php");
        exit();
    }

    require_once 'db.php';
    $conn = db_connect();

    if($_SERVER['REQUEST_METHOD'] == 'POST'){
    
        $name = $_POST['name'];
        $description = $_POST['description'];
        $category = $_POST['category'];
        $imageBase64 = $_POST['pictureBase64'];
        $youtubeInput = $_POST['youtubeId'];
        $youtubeId = null;
        
        if (!empty($youtubeInput)) {
            if (str_starts_with($youtubeInput, 'https://www.youtube.com/watch?v=')) {
            
                // Extrai o ID -> tudo depois de 'v='
                $youtubeId = substr($youtubeInput, strpos($youtubeInput, 'v=') + 2);
                
                // Remove parâmetros adicionais após o ID como &t= (tempo)
                if (strpos($youtubeId, '&') !== false) {
                    $youtubeId = substr($youtubeId, 0, strpos($youtubeId, '&'));
                }
            }
        }

        if(!empty($name) && !empty($description) && !empty($category)){

            $stmt = $conn->prepare("SELECT * FROM Wiki WHERE Name=?");
            $stmt->bind_param("s", $name);
            $stmt->execute();
            $result = $stmt->get_result();
                
            if($result->num_rows >= 1){                
                $msg = "A wiki with this name already exists!";
            }else{
                // ver se a catogoria já existe, se não criar uma nova e so depois criar a wiki
                $stmt = $conn->prepare("SELECT * FROM Category WHERE Name=?");
                $stmt->bind_param("s", $category);
                $stmt->execute();
                $result = $stmt->get_result();

                if($result->num_rows == 0){
                    $stmt = $conn->prepare("INSERT INTO Category (Name) VALUES (?)");
                    $stmt->bind_param("s", $category);
                    $stmt->execute();
                }

                // wiki
                $stmt = $conn->prepare("INSERT INTO Wiki (Name, Category, Image, Description, YoutubeId) VALUES (?, ?, ?, ?, ?)");
                $stmt->bind_param("sssss", $name, $category, $imageBase64, $description, $youtubeId);
                
                if($stmt->execute()){
                    $msg = "Sucess creating the new Wiki: " . $name;
                } else {
                    $msg = "Error creating wiki: " . $conn->error;
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
    <title>NexusWiki - Create Wiki</title>
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

    <a href="mainPage.php" class="back-arrow">
        <i class="bi bi-arrow-left"></i>
    </a>
    
    <main class="main-content">
        <div class="form-container">
            <h1>Create Wiki</h1>
            
            <form method="POST" id="createWiki">
                <input type="text" placeholder="Name" name="name" required>
                
                <div class="form-group">
                    <textarea name="description" placeholder="Enter description here..." required
                                style="height: 400px; width: 100%; resize: vertical; padding: 10px; box-sizing: border-box; background-color: #D9D9D9;"
                    ></textarea>
                </div>

                <input type="text" placeholder="Category" name="category" required>

                <div class="form-group" style="padding-top: 10px; text-align: left;">
                    <label for="file-upload" style="color: white;">Upload Image (JPEG/JPG only):</label>
                    <input type="file" id="file-upload" name="picture" accept="image/jpeg" required>
                </div>

                <input type="text" placeholder="Youtube link" name="youtubeId">

                <input type="hidden" id="pictureBase64" name="pictureBase64">

                <div class="divider"></div>
                
                <button type="submit" class="buttons">Create</button>  
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
            const form = document.getElementById('createWiki');
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
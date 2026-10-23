<?php
session_start();

if (!isset($_SESSION['user'])) {
    header("Location: login.php");
    exit();
}

if ($_SESSION['user']['Tipo'] !== 'Administrador' && $_SESSION['user']['Tipo'] !== 'Simpatizante') {
    header("Location: wikiPage.php");
    exit();
}

if (!isset($_SESSION['selected_wiki']) || !isset($_SESSION['selected_page'])) {
    header("Location: mainPage.php");
    exit();
}

if (!isset($_SESSION['selected_information'])) {
    header("Location: wikiPage.php");
    exit();
}

require_once 'db.php';
$conn = db_connect();

$wikiName = $_SESSION['selected_wiki'];
$pageName = $_SESSION['selected_page'];
$infoName = $_SESSION['selected_information'];
$editorEmail = $_SESSION['user']['Email'];

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $changedField = $_POST['changedField'];
    $oldContent = '';
    $newContent = '';
    $typeOfChange = '';

    $stmt = $conn->prepare("SELECT Description, Img FROM Information WHERE Name = ? AND Page = ? AND Wiki = ?");
    $stmt->bind_param("sss", $infoName, $pageName, $wikiName);
    $stmt->execute();
    $result = $stmt->get_result();
    $pageData = $result->fetch_assoc();
    $stmt->close();

    if ($changedField === 'Description'){

        $oldContent = $pageData['Description'];
        $newContent = $_POST['description'];
        $typeOfChange = 'Information Description';

        $stmt = $conn->prepare("UPDATE Information SET Description = ? WHERE Name = ? AND Page = ? AND Wiki = ?");
        $stmt->bind_param("ssss", $newContent, $infoName, $pageName, $wikiName);
        $stmt->execute();
        $stmt->close();
    } else if ($changedField === 'Img') {

        $oldContent = $pageData['Img'];
        $newContent = $_POST['pictureBase64'];
        $typeOfChange = 'Information Image';
        
        $stmt = $conn->prepare("UPDATE Information SET Img = ? WHERE Name = ? AND Page = ? AND Wiki = ?");
        $stmt->bind_param("ssss", $newContent, $infoName, $pageName, $wikiName);
        $stmt->execute();
        $stmt->close();
    }else{
        $msg = "Error while editing the Content: " .$infoName;

        $_SESSION['message'] = $msg;
        $conn->close();
        header("Location: wikiPage.php");
        exit();
    }

    $editionDateTime = date('Y-m-d H:i:s');
    $stmt = $conn->prepare("INSERT INTO Edition (Editor, EditionDateTime, TypeOfChange, OldContent, NewContent) VALUES (?, ?, ?, ?, ?)");
    $stmt->bind_param("sssss", $editorEmail, $editionDateTime, $typeOfChange, $oldContent, $newContent);
    $stmt->execute();
    $stmt->close();
    
    $stmt = $conn->prepare("INSERT INTO Edition_Information (EditionDateTime, WikiName, PageName, InformationName) VALUES (?, ?, ?, ?)");
    $stmt->bind_param("ssss", $editionDateTime, $wikiName, $pageName, $infoName);
    $stmt->execute();
    $stmt->close();

    $msg = "Content edited successfully!";

    $_SESSION['message'] = $msg;
    $conn->close();
    header("Location: wikiPage.php");
    exit();
}
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>NexusWiki - Edit Content</title>
    <link rel="stylesheet" type="text/css" href="./Css/style.css">
    <link rel="stylesheet" type="text/css" href="./Css/mainPage.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <style>
        .form-container {
            background-color: #9A3324;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            width: 500px;
            text-align: center;
            color: white;
        }

        .form-container select, 
        .form-container input, 
        .form-container textarea {
            width: 100%;
            padding: 10px;
            margin: 10px 0;
            border-radius: 4px;
            border: 1px solid #ddd;
            box-sizing: border-box;
        }
    </style>
</head>
<body>
    <header class="wikiHeader">
        <div class="logo-container">
            <img src="./Images/logo.png" alt="NexusWiki Logo" class="logo">
        </div>
        <div class="header-buttons">
            <?php if ($_SESSION['user']['Tipo'] === 'Administrador'): ?>
                <a href="deleteInformation.php">Delete Content</a>
            <?php endif; ?>
        </div>
    </header>

    <a href="wikiPage.php" class="back-arrow">
        <i class="bi bi-arrow-left"></i>
    </a>
    
    <main class="main-content">
        <div class="form-container">
            <h1>Edit Content <?=$infoName . " in " . $pageName . " (" . $wikiName . ")"?></h1>

            <form method="POST" id="changeProfile" enctype="multipart/form-data">
                <label for="changedField">What do you want to change?</label>
                <select id="changedField" name="changedField" required>
                    <option value="" disabled selected>Choose one option</option>
                    <option value="Description">Content Description</option>
                    <option value="Img">Image</option>
                </select>

                <div id="dynamic-input"></div>

                <input type="hidden" id="pictureBase64" name="pictureBase64">

                <div class="divider"></div>
                <button type="submit" class="buttons">Update</button>
            </form>
        </div>
    </main>

    <footer class="footer">
        <div class="footer-content">© Copyright Fabio Pastana & João Ramos</div>
    </footer>

    <script>
        document.addEventListener("DOMContentLoaded", function () {
            const form = document.getElementById('changeProfile');
            const selectField = document.getElementById("changedField");
            const dynamicInput = document.getElementById("dynamic-input");

            function updateInputField() {
                const selectedValue = selectField.value;
                dynamicInput.innerHTML = "";

                if (selectedValue === "Img") {
                    dynamicInput.innerHTML = `
                        <div class="form-group" style="padding-top: 10px; text-align: left;">
                            <label for="file-upload" style="color: white;">Upload Image (JPEG/JPG only):</label>
                            <input type="file" id="file-upload" name="picture" accept="image/jpeg" required>
                        </div>
                    `;
                } else if (selectedValue === "Description") {
                    dynamicInput.innerHTML = `
                        <div class="form-group">
                            <textarea name="description" placeholder="Enter description here..." required
                                style="height: 400px; width: 100%; resize: vertical; padding: 10px; box-sizing: border-box; background-color: #D9D9D9;">
                            </textarea>
                        </div>
                    `;
                }
            }

            selectField.addEventListener("change", updateInputField);

            form.addEventListener('submit', function (event) {
                const selectedValue = selectField.value;

                if (selectedValue === "Img") {
                    event.preventDefault();
                    const fileInput = document.getElementById('file-upload');
                    const file = fileInput?.files[0];

                    if (file) {
                        const reader = new FileReader();
                        reader.onload = function () {
                            document.getElementById('pictureBase64').value = reader.result;
                            form.submit();
                        };
                        reader.readAsDataURL(file);
                    } else {
                        alert("Erro: Por favor, selecione uma imagem.");
                    }
                }
            });
        });
    </script>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL"
        crossorigin="anonymous"></script>
</body>
</html>


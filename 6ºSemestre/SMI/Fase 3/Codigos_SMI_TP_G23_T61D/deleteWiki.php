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

if (!isset($_SESSION['selected_wiki'])) {
    header("Location: mainPage.php");
    exit();
}

require_once 'db.php';
$conn = db_connect();

$wikiName = $_SESSION['selected_wiki'];

$wikiQuery = "SELECT * FROM Wiki WHERE Name = ?";
$stmt = $conn->prepare($wikiQuery);
$stmt->bind_param("s", $wikiName);
$stmt->execute();
$wikiResult = $stmt->get_result();

if ($wikiResult->num_rows === 0) {
    header("Location: mainPage.php");
    exit();
}

$wikiData = $wikiResult->fetch_assoc();

function deleteFromDb($conn, $query, $wiki){
    $stmt = $conn->prepare($query);
    $stmt->bind_param("s", $wiki);
    $stmt->execute();
    $stmt->close();
}

$conn->query("SET FOREIGN_KEY_CHECKS=0");

$deleteQuery = "DELETE FROM Edition_Information WHERE WikiName = ?";
deleteFromDb($conn, $deleteQuery, $wikiName);

$deleteQuery = "DELETE FROM Edition_Page WHERE WikiName = ?";
deleteFromDb($conn, $deleteQuery, $wikiName);

$deleteQuery = "DELETE FROM Information WHERE Wiki = ?";
deleteFromDb($conn, $deleteQuery, $wikiName);

$deleteQuery = "DELETE FROM Page WHERE Wiki = ?";
deleteFromDb($conn, $deleteQuery, $wikiName);

$deleteQuery = "DELETE FROM User_Wiki WHERE Name = ?";
deleteFromDb($conn, $deleteQuery, $wikiName);

$deleteQuery = "DELETE FROM Wiki WHERE Name = ?";
deleteFromDb($conn, $deleteQuery, $wikiName);

$query = "SELECT c.Name FROM Category c LEFT JOIN Wiki w ON c.Name = w.Category WHERE w.Name IS NULL";
$stmt = $conn->prepare($query);
$stmt->execute();
$result = $stmt->get_result();
$stmt->close();

if ($result->num_rows >= 1) 
{
    $deleteCategoryQuery = "DELETE FROM Category WHERE Name = ?";
    $deleteStmt = $conn->prepare($deleteCategoryQuery);
        
    while ($category = $result->fetch_assoc()) {
        $deleteStmt->bind_param("s", $category['Name']);
        $deleteStmt->execute();
    }
    $deleteStmt->close();
    $msg = "Wiki and Categories deleted successfully!";
}else{ 
    $msg = "Wiki deleted successfully!";
}

$editionDateTime = date('Y-m-d H:i:s');
$typeOfChange = "Wiki Deleted";
$stmt = $conn->prepare("INSERT INTO Edition (Editor, EditionDateTime, TypeOfChange, NameDeleted, DescriptionDeleted, ImgDeleted) VALUES (?, ?, ?, ?, ?, ?)");
$stmt->bind_param("ssssss", $_SESSION['user']['Email'] , $editionDateTime, $typeOfChange, $wikiName, $wikiData['Description'], $wikiData['Image']);
$stmt->execute();
$stmt->close();

$conn->query("SET FOREIGN_KEY_CHECKS=1");

$_SESSION['message'] = $msg;
$conn->close();
header("Location: mainPage.php");
exit();
?>
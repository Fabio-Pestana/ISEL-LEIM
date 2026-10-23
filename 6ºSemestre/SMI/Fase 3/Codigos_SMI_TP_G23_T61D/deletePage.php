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

if (!isset($_SESSION['selected_wiki']) || !isset($_SESSION['selected_page'])) {
    header("Location: mainPage.php");
    exit();
}

require_once 'db.php';
$conn = db_connect();

$wikiName = $_SESSION['selected_wiki'];
$pageName = $_SESSION['selected_page'];

$pageQuery = "SELECT * FROM Page WHERE Name = ? AND Wiki = ?";
$stmt = $conn->prepare($pageQuery);
$stmt->bind_param("ss", $pageName, $wikiName);
$stmt->execute();
$pageResult = $stmt->get_result();

if ($pageResult->num_rows === 0) {
    header("Location: mainPage.php");
    exit();
}

$pageData = $pageResult->fetch_assoc();

function deleteFromDb($conn, $query, $wiki, $page){
    $stmt = $conn->prepare($query);
    $stmt->bind_param("ss", $wiki, $page);    
    $stmt->execute();
    $stmt->close();
}
$conn->query("SET FOREIGN_KEY_CHECKS=0");

$deleteQuery = "DELETE FROM Edition_Information WHERE WikiName = ? AND PageName = ?";
deleteFromDb($conn, $deleteQuery, $wikiName, $pageName);

$deleteQuery = "DELETE FROM Edition_Page WHERE WikiName = ? AND PageName = ?";
deleteFromDb($conn, $deleteQuery, $wikiName, $pageName);

$deleteQuery = "DELETE FROM Information WHERE Wiki = ? AND Page = ?";
deleteFromDb($conn, $deleteQuery, $wikiName, $pageName);

$deleteQuery = "DELETE FROM Page WHERE Wiki = ? AND Name = ?";
deleteFromDb($conn, $deleteQuery, $wikiName, $pageName);


$editionDateTime = date('Y-m-d H:i:s');
$typeOfChange = "Page Deleted";
$stmt = $conn->prepare("INSERT INTO Edition (Editor, EditionDateTime, TypeOfChange, NameDeleted, DescriptionDeleted, ImgDeleted) VALUES (?, ?, ?, ?, ?, ?)");
$stmt->bind_param("ssssss", $_SESSION['user']['Email'] , $editionDateTime, $typeOfChange, $pageName, $pageData['Description'], $pageData['Img']);
$stmt->execute();
$stmt->close();

$conn->query("SET FOREIGN_KEY_CHECKS=1");

$msg = "Page deleted successfully!";

$_SESSION['message'] = $msg;
$conn->close();
header("Location: mainWikiPage.php");
exit();
?>
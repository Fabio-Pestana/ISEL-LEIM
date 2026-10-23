<?php
session_start();

if (!isset($_SESSION['user'])) {
    header("Location: login.php");
    exit();
}

if ($_SESSION['user']['Tipo'] !== 'Administrador') {
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

$informationQuery = "SELECT * FROM Information WHERE Wiki = ? AND Page = ? AND Name = ?";
$stmt = $conn->prepare($informationQuery);
$stmt->bind_param("sss", $wikiName, $pageName, $infoName);
$stmt->execute();
$informationResult = $stmt->get_result();

if ($informationResult->num_rows === 0) {
    header("Location: wikiPage.php");
    exit();
}

$infoData = $informationResult->fetch_assoc();

function deleteFromDb($conn, $query, $wiki, $page, $info){

    $stmt = $conn->prepare($query);
    $stmt->bind_param("sss", $wiki, $page, $info);
    $stmt->execute();
    $stmt->close();
}

$conn->query("SET FOREIGN_KEY_CHECKS=0");

$deleteQuery = "DELETE FROM Edition_Information WHERE WikiName = ? AND PageName = ? AND InformationName = ?";
deleteFromDb($conn, $deleteQuery, $wikiName, $pageName, $infoName);

$deleteQuery = "DELETE FROM Information WHERE Wiki = ? AND Page = ? AND Name = ?";
deleteFromDb($conn, $deleteQuery, $wikiName, $pageName, $infoName);

$editionDateTime = date('Y-m-d H:i:s');
$typeOfChange = "Information Deleted";
$stmt = $conn->prepare("INSERT INTO Edition (Editor, EditionDateTime, TypeOfChange, NameDeleted, DescriptionDeleted, ImgDeleted) VALUES (?, ?, ?, ?, ?, ?)");
$stmt->bind_param("ssssss", $_SESSION['user']['Email'] , $editionDateTime, $typeOfChange, $infoName, $infoData['Description'], $infoData['Img']);
$stmt->execute();
$stmt->close();

$conn->query("SET FOREIGN_KEY_CHECKS=1");

$msg = "Content deleted successfully!";

$_SESSION['message'] = $msg;
$conn->close();
header("Location: wikiPage.php");
exit();
?>
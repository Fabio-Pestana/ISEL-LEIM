<?php
    function db_connect(){
        $host = "localhost";
        $port = 3306;
        $username = "root";
        $password = "";
        $dbName = "NexusWiki";

        $conn = new mysqli($host, $username, $password, $dbName, $port);
        if($conn->connect_error){
            die("Error: " . $conn->connect_error);
        }

        return $conn;
    }
?>
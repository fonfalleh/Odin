<?php

ini_set("display_errors",1);
require_once "../include/DbHandler.php"
$db = new DbHandler();
echo "<pre>"
echo print_r($db->getAllIncidents());
echo "</pre>"
echo phpinfo();
?>

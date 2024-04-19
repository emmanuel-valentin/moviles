<?php
$hostname = 'localhost';
$database = 'bd_inventario';
$username = 'root';
$password = '';
$conexion = new mysqli($hostname, $username, $password, $database);
if ($conexion->connect_errno) {
  echo "Error al conectar con la base de datos";
}
?>
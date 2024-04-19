<?php
include 'conexion.php';
$codigo = $_POST['codigo'];
$consulta = "DELETE FROM producto WHERE codigo = '" . $codigo . "'";
mysqli_query($conexion, $consulta) or die(mysqli_error($conexion));
mysqli_close($conexion);
?>
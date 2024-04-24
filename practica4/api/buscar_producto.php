<?php
include 'conexion.php';
$codigo = $_GET['codigo'];
$consulta = "select * from producto where codigo = '$codigo'";
$resultado = $conexion->query($consulta);
while ($fila = $resultado->fetch_array()) {
  $producto[] = array_map('utf8_encode', $fila);
}
echo json_encode($producto);
$resultado->close();
?>

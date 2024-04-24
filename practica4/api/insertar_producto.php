<?php
include 'conexion.php';
if (
  isset($_POST['codigo']) && isset($_POST['producto']) && isset($_POST['precio']) &&
  isset($_POST['fabricante'])
) {
  $codigo = $_POST['codigo'];
  $producto = $_POST['producto'];
  $precio = $_POST['precio'];
  $fabricante = $_POST['fabricante'];
  $consulta = "INSERT INTO producto VALUES ('" . $codigo . "','" . $producto . "','" . $precio . "','" . $fabricante . "')";
  if (mysqli_query($conexion, $consulta)) {
    echo "Registro insertado correctamente";
  } else {
    echo "Error al insertar registro: " . mysqli_error($conexion);
  }
} else {
  echo "Todos los campos son requeridos";
}
mysqli_close($conexion);
?>
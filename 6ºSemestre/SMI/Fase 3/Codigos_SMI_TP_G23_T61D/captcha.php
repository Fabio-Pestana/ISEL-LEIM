<?php
session_start();

// Configurações
$width = 180;
$height = 40;
$length = 6; // Número de caracteres no CAPTCHA
$font_size = 20;
$chars = '23456789ABCDEFGHJKLMNPQRSTUVWXYZ'; // Caracteres usados (removidos 0,1,O,I para evitar confusão)

// Gerar código aleatório
$code = '';
for ($i = 0; $i < $length; $i++) {
    $code .= $chars[rand(0, strlen($chars) - 1)];
}

// Armazenar na sessão
$_SESSION['captcha'] = $code;

// Criar imagem
$image = imagecreatetruecolor($width, $height);

// Cores
$bg_color = imagecolorallocate($image, 255, 255, 255);
$text_color = imagecolorallocate($image, 0, 0, 0);
$noise_color = imagecolorallocate($image, 100, 120, 180);

// Fundo branco
imagefilledrectangle($image, 0, 0, $width, $height, $bg_color);

// Adicionar ruído (pontos)
for ($i = 0; $i < ($width * $height) / 3; $i++) {
    imagesetpixel($image, rand(0, $width), rand(0, $height), $noise_color);
}

// Adicionar linhas de ruído
for ($i = 0; $i < 5; $i++) {
    imageline($image, 0, rand() % $height, $width, rand() % $height, $noise_color);
}

// Posição inicial do texto
$x = 10;
$y = ($height / 2) + ($font_size / 2);

// Escrever o texto
for ($i = 0; $i < $length; $i++) {
    // Inclinação aleatória para cada caractere
    $angle = rand(-15, 15);
    
    // Posição y aleatória para cada caractere
    $y_pos = $y + rand(-5, 5);
    
    // Escrever caractere
    imagettftext($image, $font_size, $angle, $x, $y_pos, $text_color, __DIR__.'/assets/fonts/arial.ttf', $code[$i]);
    
    // Avançar posição x
    $x += ($font_size + rand(5, 10));
}

// Configurar cabeçalho e output
header('Content-Type: image/png');
header('Cache-Control: no-cache, no-store, must-revalidate');
header('Pragma: no-cache');
header('Expires: 0');

imagepng($image);
imagedestroy($image);
?>
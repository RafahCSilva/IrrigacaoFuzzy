<?php

function furmulas() {
  $dados = [
    [ 'Umidade', [
      [ 'muito~baixa', -1, 0, 35, 44 ],
      [ 'baixa', 0, 35, 44, 53 ],
      [ 'media', 0, 44, 53, 62 ],
      [ 'alta', 0, 53, 62, 71 ],
      [ 'muito~alta', 1, 62, 71, 100 ],
    ] ],

    [ 'Temperatura', [
      [ 'muito~baixa', -1, 15, 20.8, 26.65 ],
      [ 'baixa', 0, 20.8, 26.65, 32.5 ],
      [ 'media', 0, 26.65, 32.5, 38.3 ],
      [ 'alta', 0, 32.5, 38.3, 44.15 ],
      [ 'muito~alta', 1, 38.3, 44.15, 50 ],
    ] ],

    [ 'Estágio', [
      [ 'crescimento', -1, 0, 53, 75 ],
      [ 'desenvolvimento', 0, 53, 75, 98 ],
      [ 'maturacao', 1, 75, 98, 100 ],
    ] ],
    [ 'Tempo', [
      [ 'muito~curto', -1, 0, 3.3, 6.6 ],
      [ 'curto', 0, 3.3, 6.6, 10 ],
      [ 'medio', 0, 6.6, 10, 13.3 ],
      [ 'longo', 0, 10, 13.3, 16.6 ],
      [ 'muito~longo', 1, 13.3, 16.6, 20 ],
    ] ] ];

  echo '<pre>' . PHP_EOL;
  echo '\section{BASE}' . PHP_EOL;
  echo ' $$' . PHP_EOL;
  echo ' \\\\ \\\\ \\\\' . PHP_EOL;
  echo ' T_{BASE} = \left\{' . PHP_EOL;
  echo '   \begin{array}{lll}' . PHP_EOL;
  echo '     \frac{x-A}{B-A} & se & A \leqslant x \leqslant B \\\\' . PHP_EOL;
  echo '     \frac{D-x}{D-C} & se & C \leqslant x \leqslant D \\\\' . PHP_EOL;
  echo '     0 & \multicolumn{2}{l}{caso~contrario}' . PHP_EOL;
  echo '   \end{array}' . PHP_EOL;
  echo ' \right.' . PHP_EOL;
  echo ' $$' . PHP_EOL;
  echo '</pre>' . PHP_EOL;


  foreach ( $dados as $dado ) {
    $title = $dado[ 0 ];

    echo '<pre>' . PHP_EOL;
    echo PHP_EOL;
    echo PHP_EOL;
    echo '\section{' . $title . '}' . PHP_EOL;

    foreach ( $dado[ 1 ] as $eq ) {
      $nome = $eq[ 0 ];
      $tipo = $eq[ 1 ];
      $A    = $eq[ 2 ];
      $B    = $eq[ 3 ];
      $C    = $B;
      $D    = $eq[ 4 ];

      echo PHP_EOL;

      echo '$$' . PHP_EOL;
      echo '  \\\\ \\\\ \\\\' . PHP_EOL;
      echo '  T_{' . $nome . '} = \left\{' . PHP_EOL;
      echo '    \begin{array}{lll}' . PHP_EOL;
      if ( $tipo == -1 ) {
        echo '      1 & se & ' . $A . ' \leqslant x \leqslant ' . $B . ' \\\\' . PHP_EOL;
      } else {
        echo '      \frac{x - ' . $A . '}{' . $B . ' - ' . $A . '} & se & ' . $A . ' \leqslant x \leqslant ' . $B . ' \\\\' . PHP_EOL;
      }
      if ( $tipo == 1 ) {
        echo '      1 & se & ' . $C . ' \leqslant x \leqslant ' . $D . ' \\\\' . PHP_EOL;
      } else {
        echo '      \frac{' . $D . ' - x}{' . $D . ' - ' . $C . '} & se & ' . $C . ' \leqslant x \leqslant ' . $D . ' \\\\' . PHP_EOL;
      }
      echo '      0 & \multicolumn{2}{l}{caso~contrario}' . PHP_EOL;
      echo '    \end{array}' . PHP_EOL;
      echo '  \right.' . PHP_EOL;
      echo '$$' . PHP_EOL;


    }
    echo '</pre>' . PHP_EOL;
  }
}

?>
<!DOCTYPE html>
<html>
<head>
  <title>Formulas</title>
</head>
<body>
<?= furmulas(); ?>
</body>
</html>
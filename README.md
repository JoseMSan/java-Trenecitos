# java-Trenecitos
Final assingment PROGRAMACION II
 Un niño pequeño llamado Juanito tiene unos trenes de juguete que están formados por una serie de unidades
de 1 centímetro. Juanito ensambla sus trenes a la longitud que quiera y los coloca en una tabla de 30 × 30 centímetros
que permite a los trenes viajar en pistas de 1 centímetro de ancho que atraviesan la tabla. Los trenes viajan solo en paralelo al eje x o y del tablero. Las unidades de tren del mismo tren avanzan al mismo tiempo y los trenes avanzan en orden numérico cíclico (todos los trenes 0 primero, luego 1, etc.).

Cuando más de una unidad de dos o más trenes ocupan la misma coordenada (x, y), hay una colisión de trenes.
Siempre que ocurre una colisión, todos las unidades que ocupan el sitio de la colisión se detienen y continúan
ocupando el sitio de la colisión. Todos las unidades restantes en un tren se separan de la unidad involucrada en la
colisión y continúan su marcha hasta que ocurre otra colisión o se encuentra un sitio de colisión existente o
hasta que las unidades se caen por el borde del tablero. Cada vez que una unidad entra en un sitio de colisión,
se convierte en parte de la colisión.

 Puesto que Juanito se ha ido de su casa sin su juego de trenes, su madre te ha contratado para que escribas un
 programa de simulación para su entretenimiento. Tu programa simulará su tablero con una impresión de texto de sus
 cuadrículas. Por ejemplo, Juanito puede simular 5 trenes en su tablero que comienzan como se muestra en la
 cuadrícula de la izquierda y terminan como se muestra en la cuadrícula de la derecha (hay que tener en cuenta que
 la cuadrícula de ejemplo es solo 10 × 10 mientras que la de Juanito es 30 × 30).


        9   . . . . . . . . . .                 9   . . . . . . . . . .
        8   . . . . . . . . . .                 8   . . . . . . . . . .
        7   3 3 3 3 3 . . . . .                 7   . . . . . X . . . X
        6   . 0 . . . . . . . .                 6   . . . . . . . . . .
        5   . 0 . . . . . . . 1                 5   . . . . . . . . . . 
        4   . 0 . . . 1 . . . 1                 4   . . . . . . . . . .
        3   . 0 . . . 1 . . . 1                 3   . . . . . . . . . .
        2   . . . . . 1 . . . 1                 2   . . . . . . . . . .
        1   . . . . . 1 . . . 1                 1   . . . . . . . . . .
        0   . . . . . 1 2 2 2 1                 0   . X . . . . . . . .
        Y                                       Y 
        / X 0 1 2 3 4 5 6 7 8 9                 / X 0 1 2 3 4 5 6 7 8 9

Dónde:
    0 representa una unidad de tren que viaja de arriba a abajo,
    1 representa una unidad de tren que viaja de abajo hacia arriba,
    2 representa una unidad de tren que viaja de derecha a izquierda,
    3 representa una unidad de tren que viaja de izquierda a derecha,
  y X representa un sitio de colisión que involucra 2 o más segmentos.
 
Tu programa simulará hasta 10 trenes que viajan en una tabla de 30 × 30. Juanito tiene 100 unidades que puede usar
en su simulación. Por supuesto, ningún tren puede tener más de 30 unidades.

Entrada
La entrada al programa consistirá en una serie de conjuntos de simulación. La primera línea de cada conjunto de
entrada será un único entero (1 ≤ N ≤ 10) que representa el número de trenes en la simulación. (Los trenes se
numeran del 0 al N - 1 en el mismo orden que la entrada).
Las siguientes N líneas representarán cada una un tren y contendrán un carácter de dirección único seguido de 3
enteros. El carácter de dirección puede ser "A", "B", "I" o "D" para "Arriba", "aBajo", "Izquierda" o "Derecha"
respectivamente e indicar la dirección de viaje. El siguiente entero (1 ≤ L ≤ 30) indica la longitud del tren en
unidades. El segundo y tercer enteros indican las coordenadas x, y de la unidad principal (0 ≤ x, y ≤ 29). Los
siguientes segmentos L - 1 del tren ocupan las posiciones del tablero L - 1 que se extienden en la dirección opuesta
a la dirección de viaje que comienza junto a la unidad principal.
Cualquier unidad de tren que se origina fuera del tablero o cualquier configuración original que  suponga  una
colisión se considerará incorrecta. Así  mismo  cualquier entrada que no se ajuste a las directrices indicadas se
considerará incorrecta. En caso de entrada incorrecta, los conjuntos de simulaciones bien definidos previos al punto
de la
entrada incorrecta se procesaran de la forma habitual si se ha completado correctamente su lectura.
Cuando se detecte una entrada incorrecta se enviará a la
salida estándar el mensaje "Conjunto de trenes incorrecto." y no se procesará más entrada, finalizando el
programa.

Salida
Para cada conjunto de simulación de entrada, debe imprimir (exactamente) las siguientes líneas
como las dos primeras líneas de salida (comenzando en la columna 4).

   0 0 0 0 0 0 0 0 0 0 1 1 1 1 1 1 1 1 1 1 2 2 2 2 2 2 2 2 2 2
   0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9

Las siguientes 30 líneas representan el estado del tablero después de que se haya
completado la simulación (todos las unidades de tren han entrado en un sitio de
colisión o se han caído del borde del tablero). Las columnas 1 y 2 deben contener
el número de fila (las filas se numeran del 29 al 00) con ceros a la izquierda.
Las columnas pares entre 4 y 62 (inclusive) representan el contenido de las celdas
del tablero. Las celdas del tablero pueden contener una "X" o un punto "."
(Un punto representa una celda vacía y "X" representa una celda que contiene 2 o más
unidades de tren involucrados en una colisión).
La última línea de cada conjunto de salida es una línea en blanco.
Debes seguir la salida de ejemplo para conocer el formato exacto de la salida esperada.

Ejemplo
Entrada
10
D 9 11 23
A 8 11 17
A 5 15 15
A 5 15 8
B 9 23 13
A 6 23 6
D 9 8 9
I 13 17 0
A 12 13 11
I 5 20 9

Salida de muestra

   0 0 0 0 0 0 0 0 0 0 1 1 1 1 1 1 1 1 1 1 2 2 2 2 2 2 2 2 2 2
   0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9
29 . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
28 . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
27 . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
26 . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
25 . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
24 . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
23 . . . . . . . . . . . X . . . X . . . . . . . . . . . . . .
22 . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
21 . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
20 . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
19 . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
18 . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
17 . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
16 . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
15 . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
14 . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
13 . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
12 . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
11 . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
10 . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
09 . . . . . . . . . . . . . X . X . . . . . . . X . . . . . .
08 . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
07 . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
06 . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
05 . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
04 . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
03 . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
02 . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
01 . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
00 . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .

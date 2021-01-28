package Trenes;

import java.util.Scanner;

public class MainTrenes {
    static int contTren = 0;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            contTren = 0;
            Casilla[][] tablero = new Casilla[30][30];
            generarTablero(tablero);
            int n = sc.nextInt();
            String aux = sc.nextLine(); // Elimina el /n sobrante
            if (!(n >= 1 && n <= 10)) {
                error();
            }
            Trenes[] trenes = new Trenes[50];
            String[] datosTrenes = extraeDatosTrenes(n, sc);
            crearTrenes(datosTrenes, tablero, trenes);
            imprimirTablero(tablero, trenes);
            ordenarTrenes(trenes);
            simulacion(tablero, trenes);
            imprimirTablero(tablero, trenes);
            System.out.print("\n");
        }
        sc.close();
    }
    static int t=0;
    public static void simulacion(Casilla[][] tablero, Trenes[] trenes) {
        while (!comprobarFin(tablero)) {
//            while(t<10){
//                t++;
            for (int k = 0; k < contTren; k++) {
                //Recorro cada uno de las posiciones del array de trenes previamente ordenado de menor a mayor
                if (!trenes[k].tipo.equals("-1")) {
                    switch (trenes[k].tipo) {
                        case "0":
                            for (int i = 1; i < trenes[k].tam - 1; i++) {
                                //En este bucle compruebo que no le hayan chocado en la iteracion anterior
                                if (tablero[trenes[k].vagones[i][1]][trenes[k].vagones[0][0]].getEstado().equals("X")) {
                                    int j, cont = 1;

                                    tablero[trenes[k].vagones[0][1]][trenes[k].vagones[0][0]].setEstado(".");
                                    //Actualizo el ultimo vagon del tren

                                    trenes[contTren] = new Trenes(i, trenes[k].getTipo());
                                    //Creo un nuevo tren que se quedara en el choque

                                    for (j = 0; j < i; j++) {
                                        //Copio los valores que hay antes de la x al nuevo tren
                                        trenes[contTren].vagones[j][0] = trenes[k].vagones[j][0];
                                        trenes[contTren].vagones[j][1] = trenes[k].vagones[j][1];
                                        cont++;
                                    }
                                    for (int u = 0; u < cont; u++) {
                                        //Avanzo a lo largo del tren antiguo para reducir su tamaño y conservar las casillas
                                        for (int h = 0; h < trenes[k].tam; h++) {
                                            trenes[k].vagones[h][1]--;
                                        }
                                    }

                                    trenes[k].tam = trenes[k].tam - i - 1; //Actualizo el tamaño del nuevo tren grande
                                    contTren++; //Aumento el numero de trenes totales
                                    break;
                                }
                            }
                            if (trenes[k].vagones[trenes[k].tam - 1][1] == 0 || trenes[k].tam == 1) {
                                //Caso de llegada al borde del tablero o que un trozo sea de tamaño 1
                                tablero[trenes[k].vagones[0][1]][trenes[k].vagones[0][0]].setEstado(".");
                                if (trenes[k].tam > 1) {
                                    for (int i = 0; i < trenes[k].tam; i++) {
                                        if (trenes[k].vagones[i][1] >= 0)
                                            trenes[k].vagones[i][1]--;
                                    }
                                }
                                trenes[k].tam--;
                                if (trenes[k].tam == 0) //Si el tamaño es 0 lo sacamos de la lista para seguir moviendo
                                    trenes[k].setTipo("-1");
                            } else {
                                //Caso de cualquier movimiento fuera del borde
                                if (tablero[trenes[k].vagones[trenes[k].tam - 1][1]][trenes[k].vagones[0][0]].getEstado().equals("X")) {
                                    // Si el tren ya ha sido chocado por otro DE FRENTE elimino una posicion
                                    tablero[trenes[k].vagones[0][1]][trenes[k].vagones[0][0]].setEstado(".");
                                    trenes[k].tam--;
                                }
                                if (tablero[trenes[k].vagones[0][1]][trenes[k].vagones[0][0]].getEstado().equals("X")) {
                                    //Comprobamos si hay colision en la cola del tren
                                    for (int i = 0; i < trenes[k].tam; i++)
                                        trenes[k].vagones[i][1]--;
                                    trenes[k].tam--;
                                }
                                if ((tablero[(trenes[k].vagones[(trenes[k].tam) - 1][1]) - 1][trenes[k].vagones[0][0]]).getEstado().equals(".")) {
                                    //Si la siguiente posicion esta libre
                                    tablero[trenes[k].vagones[0][1]][trenes[k].vagones[0][0]].setEstado(".");
                                    for (int i = 0; i < trenes[k].tam; i++)
                                        trenes[k].vagones[i][1]--;
                                } else if (tablero[(trenes[k].vagones[(trenes[k].tam) - 1][1]) - 1][trenes[k].vagones[0][0]].getEstado().equals("X")) {
                                    //Si la siguiente posicion es un choque existente
                                    tablero[trenes[k].vagones[0][1]][trenes[k].vagones[0][0]].setEstado(".");
                                    for (int i = 0; i < trenes[k].tam; i++) {
                                        trenes[k].vagones[i][1]--;
                                    }
                                    trenes[k].tam--;
                                    if (trenes[k].tam == 0) {
                                        trenes[k].setTipo("-1");
                                    }
                                } else {
                                    //Si la siguiente posicion sera un choque nuevo
                                    tablero[trenes[k].vagones[0][1]][trenes[k].vagones[0][0]].setEstado(".");
                                    tablero[((trenes[k].vagones[(trenes[k].tam) - 1][1]) - 1)][trenes[k].vagones[0][0]].setEstado("X");
                                    if (trenes[k].tam > 1) {
                                        for (int i = 0; i < trenes[k].tam; i++)
                                            trenes[k].vagones[i][1]--;
                                    }
                                    trenes[k].tam--;
                                    if (trenes[k].tam == 0)  //Hacemos que el tren deje de usarse como valido.
                                        trenes[k].setTipo("-1");
                                }
                            }
                            break;
                        case "1":
                            for (int i = 1; i < trenes[k].tam - 1; i++) {
                                //En este bucle compruebo que no le hayan chocado en la iteracion anterior
                                if (tablero[trenes[k].vagones[i][1]][trenes[k].vagones[0][0]].getEstado().equals("X")) {
                                    int j, cont = 1;

                                    tablero[trenes[k].vagones[0][1]][trenes[k].vagones[0][0]].setEstado(".");
                                    //Actualizo el ultimo vagon del tren

                                    trenes[contTren] = new Trenes(i, trenes[k].getTipo());
                                    //Creo un nuevo tren que se quedara en el choque

                                    for (j = 0; j < i; j++) {
                                        //Copio los valores que hay antes de la x al nuevo tren
                                        trenes[contTren].vagones[j][0] = trenes[k].vagones[j][0];
                                        trenes[contTren].vagones[j][1] = trenes[k].vagones[j][1];
                                        cont++;
                                    }
                                    for (int u = 0; u < cont; u++) {
                                        //Avanzo a lo largo del tren antiguo para reducir su tamaño y conservar las casillas
                                        for (int h = 0; h < trenes[k].tam; h++) {
                                            trenes[k].vagones[h][1]++;
                                        }
                                    }
                                    trenes[k].tam = trenes[k].tam - i - 1; //Actualizo el tamaño del nuevo tren grande
                                    contTren++; //Aumento el numero de trenes totales
                                    break;
                                }
                            }
                            if (trenes[k].vagones[trenes[k].tam - 1][1] == 29 || trenes[k].tam == 1) {
                                //Caso de llegada al borde del tablero o que un trozo sea de tamaño 1
                                tablero[trenes[k].vagones[0][1]][trenes[k].vagones[0][0]].setEstado(".");
                                if (trenes[k].tam > 1) {
                                    for (int i = 0; i < trenes[k].tam - 1; i++) {
                                        if (trenes[k].vagones[i][1] >= 0)
                                            trenes[k].vagones[i][1]++;
                                    }
                                }
                                trenes[k].tam--;
                                if (trenes[k].tam == 0) //Si el tamaño es 0 lo sacamos de la lista para seguir moviendo
                                    trenes[k].setTipo("-1");
                            } else {
                                //Caso de cualquier movimiento fuera del borde
                                if (tablero[trenes[k].vagones[trenes[k].tam - 1][1]][trenes[k].vagones[0][0]].getEstado().equals("X")) { // Si el tren ya ha sido chocado por otro DE FRENTE elimino una posicion
                                    // Si el tren ya ha sido chocado por otro DE FRENTE elimino una posicion
                                    tablero[trenes[k].vagones[0][1]][trenes[k].vagones[0][0]].setEstado(".");
                                    trenes[k].tam--;
                                }
                                if (tablero[trenes[k].vagones[0][1]][trenes[k].vagones[0][0]].getEstado().equals("X")) {
                                    //Comprobamos si hay colision en la cola del tren
                                    for (int i = 0; i < trenes[k].tam; i++)
                                        trenes[k].vagones[i][1]++;
                                    trenes[k].tam--;
                                }


                                if ((tablero[(trenes[k].vagones[(trenes[k].tam) - 1][1]) + 1][trenes[k].vagones[0][0]]).getEstado().equals(".")) {
                                    //Si la siguiente posicion esta libre
                                    tablero[trenes[k].vagones[0][1]][trenes[k].vagones[0][0]].setEstado(".");
                                    for (int i = 0; i < trenes[k].tam; i++)
                                        trenes[k].vagones[i][1]++;
                                } else if (tablero[(trenes[k].vagones[(trenes[k].tam) - 1][1]) + 1][trenes[k].vagones[0][0]].getEstado().equals("X")) {
                                    //Si la siguiente posicion es un choque existente
                                    tablero[trenes[k].vagones[0][1]][trenes[k].vagones[0][0]].setEstado(".");
                                    if (trenes[k].tam > 1) {
                                        for (int i = 0; i < trenes[k].tam; i++)
                                            trenes[k].vagones[i][1]++;
                                    }
                                    trenes[k].tam--;
                                    if (trenes[k].tam == 0) {
                                        trenes[k].setTipo("-1");
                                    }
                                } else {
                                    //Si la siguiente posicion sera un choque nuevo
                                    tablero[((trenes[k].vagones[(trenes[k].tam) - 1][1]) + 1)][trenes[k].vagones[0][0]].setEstado("X");
                                    tablero[trenes[k].vagones[0][1]][trenes[k].vagones[0][0]].setEstado(".");
                                    if (trenes[k].tam > 1) {
                                        for (int i = 0; i < trenes[k].tam; i++)
                                            trenes[k].vagones[i][1]++;
                                    }
                                    trenes[k].tam--;
                                    if (trenes[k].tam == 0) //Hacemos que el tren deje de usarse como valido.
                                        trenes[k].setTipo("-1");
                                }
                            }
                            break;
                        case "2":
                            for (int i = 1; i < trenes[k].tam - 1; i++) {
                                //En este bucle compruebo que no le hayan chocado en la iteracion anterior
                                if (tablero[trenes[k].vagones[0][1]][trenes[k].vagones[i][0]].getEstado().equals("X")) {
                                    int j, cont = 1;

                                    tablero[trenes[k].vagones[0][1]][trenes[k].vagones[0][0]].setEstado(".");
                                    //Actualizo el ultimo vagon del tren

                                    trenes[contTren] = new Trenes(i, trenes[k].getTipo());
                                    //Creo un nuevo tren que se quedara en el choque

                                    for (j = 0; j < i; j++) {
                                        //Copio los valores que hay antes de la x al nuevo tren
                                        trenes[contTren].vagones[j][0] = trenes[k].vagones[j][0];
                                        trenes[contTren].vagones[j][1] = trenes[k].vagones[j][1];
                                        cont++;
                                    }
                                    for (int u = 0; u < cont; u++) {
                                        //Avanzo a lo largo del tren antiguo para reducir su tamaño y conservar las casillas
                                        for (int h = 0; h < trenes[k].tam; h++) {
                                            trenes[k].vagones[h][0]--;
                                        }
                                    }
                                    trenes[k].tam = trenes[k].tam - i - 1; //Actualizo el tamaño del nuevo tren grande
                                    contTren++; //Aumento el numero de trenes totales
                                    break;
                                }
                            }
                            if (trenes[k].vagones[trenes[k].tam - 1][0] == 0 || trenes[k].tam == 1) {
                                //Caso de llegada al borde del tablero o que un trozo sea de tamaño 1
                                tablero[trenes[k].vagones[0][1]][trenes[k].vagones[0][0]].setEstado(".");
                                if (trenes[k].tam > 1) {
                                    for (int i = 0; i < trenes[k].tam; i++)
                                        trenes[k].vagones[i][0]--;
                                }
                                trenes[k].tam--;
                                if (trenes[k].tam == 0) //Si el tamaño es 0 lo sacamos de la lista para seguir moviendo
                                    trenes[k].setTipo("-1");
                            } else {
                                //Caso de cualquier movimiento fuera del borde
                                if (tablero[trenes[k].vagones[0][1]][trenes[k].vagones[trenes[k].tam - 1][0]].getEstado().equals("X")) { // Si el tren ya ha sido chocado por otro DE FRENTE elimino una posicion
                                    // Si el tren ya ha sido chocado por otro DE FRENTE elimino una posicion
                                    tablero[trenes[k].vagones[0][1]][trenes[k].vagones[0][0]].setEstado(".");
                                    trenes[k].tam--;
                                }
                                if (tablero[trenes[k].vagones[0][1]][trenes[k].vagones[0][0]].getEstado().equals("X")) {//Comprobamos si hay colision en la cola del tren
                                    //Comprobamos si hay colision en la cola del tren
                                    tablero[trenes[k].vagones[0][1]][trenes[k].vagones[0][0]].setEstado(".");
                                    trenes[k].tam--;
                                }
                                if ((tablero[trenes[k].vagones[1][1]][(trenes[k].vagones[(trenes[k].tam) - 1][0]) - 1]).getEstado().equals(".")) {
                                    //Si la siguiente posicion esta libre
                                    tablero[trenes[k].vagones[1][1]][trenes[k].vagones[0][0]].setEstado(".");
                                    for (int i = 0; i < trenes[k].tam; i++)
                                        trenes[k].vagones[i][0]--;
                                } else if (tablero[(trenes[k].vagones[(trenes[k].tam) - 1][0]) - 1][trenes[k].vagones[1][1]].getEstado().equals("X")) {
                                    //Si la siguiente posicion es un choque existente
                                    tablero[trenes[k].vagones[0][9]][trenes[k].vagones[1][1]].setEstado(".");
                                    for (int i = 0; i < trenes[k].tam; i++) {
                                        trenes[k].vagones[i][1]--;
                                    }
                                    trenes[k].tam--;
                                    if (trenes[k].tam == 0) {
                                        trenes[k].setTipo("-1");
                                    }
                                } else { //Caso de choque
                                    //Si la siguiente posicion sera un choque nuevo
                                    tablero[trenes[k].vagones[0][1]][trenes[k].vagones[0][0]].setEstado(".");
                                    tablero[trenes[k].vagones[1][1]][((trenes[k].vagones[(trenes[k].tam) - 1][0]) - 1)].setEstado("X");
                                    if (trenes[k].tam > 1) {
                                        for (int i = 0; i < trenes[k].tam; i++)
                                            trenes[k].vagones[i][0]--;
                                    }
                                    trenes[k].tam--;
                                    if (trenes[k].tam == 0) { //Hacemos que el tren deje de usarse como valido.
                                        trenes[k].setTipo("-1");
                                    }
                                }
                            }
                            break;

                        case "3":
                            System.out.println("Caso 3");
                            for (int i = 1; i < trenes[k].tam - 1; i++) {
                                //En este bucle compruebo que no le hayan chocado en la iteracion anterior
                                if (tablero[trenes[k].vagones[0][1]][trenes[k].vagones[i][0]].getEstado().equals("X")) {
                                    int j, cont = 1;

                                    tablero[trenes[k].vagones[0][1]][trenes[k].vagones[0][0]].setEstado(".");
                                    //Actualizo el ultimo vagon del tren

                                    trenes[contTren] = new Trenes(i, trenes[k].getTipo());
                                    //Creo un nuevo tren que se quedara en el choque

                                    for (j = 0; j < i; j++) {
                                        //Copio los valores que hay antes de la x al nuevo tren
                                        trenes[contTren].vagones[j][0] = trenes[k].vagones[j][0];
                                        trenes[contTren].vagones[j][1] = trenes[k].vagones[j][1];
                                        cont++;
                                    }
                                    for (int u = 0; u < cont; u++) {
                                        //Avanzo a lo largo del tren antiguo para reducir su tamaño y conservar las casillas
                                        for (int h = 0; h < trenes[k].tam; h++) {
                                            trenes[k].vagones[h][0]++;
                                        }
                                    }
                                    trenes[k].tam = trenes[k].tam - i - 1; //Actualizo el tamaño del nuevo tren grande
                                    contTren++; //Aumento el numero de trenes totales
                                    break;
                                }
                            }
                            if (trenes[k].vagones[trenes[k].tam - 1][0] == 29 || trenes[k].tam == 1) {
                                //Caso de llegada al borde del tablero o que un trozo sea de tamaño 1
                                System.out.println("Llego al borde");
                                tablero[trenes[k].vagones[0][1]][trenes[k].vagones[0][0]].setEstado(".");
                                if (trenes[k].tam > 1) {
                                    for (int i = 0; i < trenes[k].tam; i++)
                                            trenes[k].vagones[i][0]++;
                                }
                                trenes[k].tam--;
                            } else {
                                //Caso de cualquier movimiento fuera del borde
                                if (tablero[trenes[k].vagones[0][1]][trenes[k].vagones[trenes[k].tam - 1][0]].getEstado().equals("X")) { // Si el tren ya ha sido chocado por otro DE FRENTE elimino una posicion
                                    // Si el tren ya ha sido chocado por otro DE FRENTE elimino una posicion
                                    tablero[trenes[k].vagones[0][1]][trenes[k].vagones[0][0]].setEstado(".");
                                    trenes[k].tam--;
                                }
                                if (tablero[trenes[k].vagones[0][1]][trenes[k].vagones[0][0]].getEstado().equals("X")) {//Comprobamos si hay colision en la cola del tren
                                    //Comprobamos si hay colision en la cola del tren
                                    tablero[trenes[k].vagones[0][1]][trenes[k].vagones[0][0]].setEstado(".");
                                    trenes[k].tam--;
                                }
                                if ((tablero[trenes[k].vagones[0][1]][trenes[k].vagones[trenes[k].tam - 1][0] + 1].getEstado().equals("."))) {
                                    //Si la siguiente posicion esta libre
                                    tablero[trenes[k].vagones[0][1]][trenes[k].vagones[0][0]].setEstado(".");
                                    for (int i = 0; i < trenes[k].tam; i++)
                                        trenes[k].vagones[i][0]++;
                                } else if (tablero[trenes[k].vagones[0][1]][(trenes[k].vagones[(trenes[k].tam) - 1][0]) + 1].getEstado().equals("X")) {
                                    //Si la siguiente posicion es un choque existente
                                    tablero[trenes[k].vagones[0][1]][trenes[k].vagones[0][0]].setEstado(".");
                                    if (trenes[k].tam > 1) {
                                        for (int i = 0; i < trenes[k].tam; i++)
                                            trenes[k].vagones[i][0]++;
                                    }
                                    trenes[k].tam--;

                                } else {
                                    //Si la siguiente posicion sera un choque nuevo
                                    tablero[trenes[k].vagones[0][1]][(trenes[k].vagones[(trenes[k].tam) - 1][0]) + 1].setEstado("X");
                                    tablero[trenes[k].vagones[0][1]][trenes[k].vagones[0][0]].setEstado(".");
                                    if (trenes[k].tam > 1) {
                                        for (int i = 0; i < trenes[k].tam; i++)
                                            trenes[k].vagones[i][0]++;
                                    }
                                    trenes[k].tam--;
                                }

                            }
                            if (trenes[k].tam == 0) {
                                trenes[k].setTipo("-1");
                            }
                            break;
                    }

                }
            }
            //Ordeno el array de trenes otra vez para que los nuevos trenes se muevan cuando deben
            ordenarTrenes(trenes);
            imprimirTablero(tablero,trenes);
            //Actualizo cada iteracion el tablero
            actualizarTablero(tablero, trenes);
        }
    }

    public static void ordenarTrenes(Trenes[] trenes) {
        for (int i = 0; i < (contTren - 1); i++) {
            for (int j = i + 1; j < contTren; j++) {
                if (Integer.parseInt(trenes[i].tipo) > Integer.parseInt(trenes[j].tipo)) {
                    Trenes aux = trenes[i];
                    trenes[i] = trenes[j];
                    trenes[j] = aux;
                }
            }
        }
    }

    public static void crearTrenes(String[] datosTrenes, Casilla[][] tablero, Trenes[] trenes) {
        for (String datosTrene : datosTrenes) {
            String[] datos = datosTrene.split(" ");
            switch (datos[0]) {
                case "B":
                    dibujarTrenes("0", datos[1], datos[2], datos[3], tablero, trenes);
                    break;
                case "A":
                    dibujarTrenes("1", datos[1], datos[2], datos[3], tablero, trenes);
                    break;
                case "I":
                    dibujarTrenes("2", datos[1], datos[2], datos[3], tablero, trenes);
                    break;
                case "D":
                    dibujarTrenes("3", datos[1], datos[2], datos[3], tablero, trenes);
                    break;
                default:
                    error();
            }
        }
    }

    public static void dibujarTrenes(String tipo, String size, String val1, String val2, Casilla[][] tablero, Trenes[] trenes) {
        int x = Integer.parseInt(val1);
        int y = Integer.parseInt(val2);
        int tam = Integer.parseInt(size);
        if (x < 0 || x > 29 || y < 0 || y > 29) {
            error();
        }
        trenes[contTren] = new Trenes(tam, tipo);

        if (!tablero[y][x].getEstado().equals(".")) {
            error();
        }
        int i, cont;
        switch (trenes[contTren].tipo) {
            case "0":
                cont = y + tam - 1;
                if (cont > 29) {
                    error();
                }
                for (i = 0; i < tam; i++) {
                    if (comprobarCasilla(tablero, cont, x)) {
                        trenes[contTren].vagones[i][0] = x;
                        trenes[contTren].vagones[i][1] = cont;
                        cont--;
                    } else
                        error();
                }
                break;
            case "1":
                cont = y - tam + 1;
                if (cont < 0) {
                    error();
                }
                for (i = 0; i < tam; i++) {
                    if (comprobarCasilla(tablero, cont, x)) {
                        trenes[contTren].vagones[i][0] = x;
                        trenes[contTren].vagones[i][1] = cont;
                        cont++;
                    } else
                        error();
                }
                break;
            case "2":
                cont = x + tam - 1;
                if (cont > 29) {
                    error();
                }
                for (i = 0; i < tam; i++) {
                    if (comprobarCasilla(tablero, cont, x)) {
                        trenes[contTren].vagones[i][1] = y;
                        trenes[contTren].vagones[i][0] = cont;
                          cont--;
                    } else
                        error();
                }
                break;
            case "3":
                cont = x - tam + 1;
                if (cont < 0) {
                    error();
                }
                for (i = 0; i < tam; i++) {
                    if (comprobarCasilla(tablero, cont, x)) {
                        trenes[contTren].vagones[i][0] = cont;
                        trenes[contTren].vagones[i][1] = y;
                        cont++;
                    } else
                        error();
                }
                break;
        }
        contTren++;
        actualizarTablero(tablero, trenes);
    }

    public static String[] extraeDatosTrenes(int n, Scanner sc) {
        String[] datosTrenes = new String[n];
        for (int i = 0; i < n; i++) {
            datosTrenes[i] = sc.nextLine();
        }
        return datosTrenes;
    }

    public static void imprimirTablero(Casilla[][] tablero, Trenes[] trenes) {
        actualizarTablero(tablero, trenes);
        System.out.print("  ");
        for (int k = 0; k < 30; k++) {
            if (k < 10) {
                System.out.print(" 0");
            } else if (k < 20) {
                System.out.print(" 1");
            } else {
                System.out.print(" 2");
            }
        }
        System.out.print("\n  ");
        for (int l = 0; l < 3; l++) {
            System.out.print(" 0 1 2 3 4 5 6 7 8 9");
        }
        System.out.print("\n");

        for (int i = 0; i < 30; i++) {
            if ((29 - i) >= 10) {
                System.out.printf("%d", 29 - i);
            } else
                System.out.printf("0%d", 29 - i);
            for (int j = 0; j < 30; j++) {
                System.out.print(" " + tablero[29 - i][j].getEstado());
            }
            System.out.print("\n");
        }


    }

    public static void actualizarTablero(Casilla[][] tablero, Trenes[] trenes) {
        for (int k = 0; k < contTren; k++) {
            for (int i = 0; i < trenes[k].tam; i++) {
                if (!trenes[k].getTipo().equals("-1"))
                    if (trenes[k].vagones[i][1] >= 0 && trenes[k].vagones[i][0] >= 0 && !tablero[trenes[k].vagones[i][1]][trenes[k].vagones[i][0]].getEstado().equals("X"))
                        tablero[trenes[k].vagones[i][1]][trenes[k].vagones[i][0]].setEstado(trenes[k].getTipo());
            }
        }
    }

    public static void generarTablero(Casilla[][] tablero) {
        for (int i = 0; i < 30; i++) {
            for (int j = 0; j < 30; j++) {
                tablero[i][j] = new Casilla();
            }
        }
    }

    public static boolean comprobarFin(Casilla[][] tablero) {
        for (int i = 0; i < 30; i++) {
            for (int j = 0; j < 30; j++) {
                if (!tablero[i][j].getEstado().equals(".")) {
                    if (!tablero[i][j].getEstado().equals("X")) {
                        return false;
                    }
                }

            }
        }
        return true;
    }

    public static boolean comprobarCasilla(Casilla[][] tablero, int i, int j) {
        return tablero[i][j].getEstado().equals(".");
    }

    public static void error() {
        System.out.println("Conjunto de trenes incorrecto.");
        System.exit(-1);
    }

}
package Trenes;

import java.util.Scanner;

public class MainTrenes {
    static int contTren = 0;

    static int NUM_FILAS;
    static int NUM_COLUMNAS;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            contTren = 0;
            String tams = sc.nextLine();
            String[] tamanios = tams.split(" ");
            NUM_FILAS = Integer.parseInt(tamanios[0]);
            NUM_COLUMNAS = Integer.parseInt(tamanios[1]);
            if (NUM_FILAS > 100 || NUM_COLUMNAS > 100 || NUM_FILAS < 1 || NUM_COLUMNAS < 1)
                //Si se sale del tablero, enviamos mensaje de error
                error();
            Casilla[][] tablero = new Casilla[NUM_FILAS][NUM_COLUMNAS];
            generarTablero(tablero);
            int n = sc.nextInt();
            String aux = sc.nextLine(); // Elimina el /n sobrante
            Trenes[] trenes = new Trenes[100];
            String[] datosTrenes = extraeDatosTrenes(n, sc);
            crearTrenes(datosTrenes, tablero, trenes);
            buscarCoincidencias(tablero, trenes);
            ordenarTrenes(trenes);
            simulacion(tablero, trenes);
            imprimirTablero(tablero, trenes);
            System.out.print("\n");
        }
        sc.close();
    }

    public static void simulacion(Casilla[][] tablero, Trenes[] trenes) {
        while (!comprobarFin(tablero)) {
            for (int k = 0; k < contTren; k++) {
                //Recorro cada uno de las posiciones del array de trenes previamente ordenado de menor a mayor
                if (!trenes[k].tipo.equals("-1")) {
                    //Si el tren esta disponible
                    switch (trenes[k].tipo) {
                        case "0":
                        case "1":
                            movimiento(tablero, trenes, trenes[k], "V");
                            break;
                        case "2":
                        case "3":
                            movimiento(tablero, trenes, trenes[k], "H");
                            break;
                    }
                }
            }
            //Ordeno el array de trenes otra vez para que los nuevos trenes se muevan cuando deben
            ordenarTrenes(trenes);
            //Actualizo cada iteracion el tablero
            actualizarTablero(tablero, trenes);
        }
    }

    public static void movimiento(Casilla[][] tablero, Trenes[] trenes, Trenes tren, String tipo) {
        int mov;
        if (tren.tipo.equals("2") || tren.tipo.equals("0"))
            mov = -1;
        else
            mov = 1;
        //La variable mov dira si el tren crece o decrece en los movimientos
        switch (tipo) {
            case "V":
                if (tablero[tren.vagones[0][1]][tren.vagones[0][0]].getEstado().equals("X")) {
                    //Comprobamos si hay colision en la cola del tren
                    for (int i = 0; i < tren.tam; i++) {
                        if (tren.getTipo().equals("0")) {
                            //Si va hacia abajo, disminuye
                            tren.vagones[i][1]--;
                        } else {
                            //Si va hacia arriba, aumenta
                            tren.vagones[i][1]++;
                        }
                    }
                    tren.tam--;
                } else if (tablero[tren.vagones[tren.tam - 1][1]][tren.vagones[0][0]].getEstado().equals("X")) {
                    // Si el tren ya ha sido chocado por otro DE FRENTE elimino una posicion
                    tren.tam--;
                }
                if (tren.tam == 0) {
                    //Si ha sido chocado y su tamaño nuevo es 0 lo eliminamos de la lista y salimos del bucle
                    tren.setTipo("-1");
                    break;
                } else if (tren.tam == 1 && tren.vagones[tren.tam - 1][1] != NUM_FILAS - 1 && tren.vagones[tren.tam - 1][1] != 0) {
                    //Si el tamaño es 1 y no esta al borde
                    if (!tablero[tren.vagones[0][1]+mov][(tren.vagones[0][0])].getEstado().equals(".")) {
                        tablero[tren.vagones[0][1]+mov][(tren.vagones[(tren.tam) - 1][0])].setEstado("X");
                        tablero[tren.vagones[0][1]][(tren.vagones[0][0])].setEstado(".");
                        if (tren.getTipo().equals("0")) {
                            //Si va hacia la izquierda, disminuye
                            tren.vagones[tren.tam - 1][1]--;
                        } else {
                            //Si va hacia la derecha, aumenta
                            tren.vagones[tren.tam - 1][1]++;
                        }
                        tren.tam--;
                    } else {
                        tablero[tren.vagones[0][1]][tren.vagones[0][0]].setEstado(".");
                        if (tren.getTipo().equals("0")) {
                            //Si va hacia la izquierda, disminuye
                            tren.vagones[tren.tam - 1][1]--;
                        } else {
                            //Si va hacia la derecha, aumenta
                            tren.vagones[tren.tam - 1][1]++;
                        }
                    }
                } else if (tren.tam == 1 && (tren.vagones[tren.tam - 1][1] == NUM_FILAS - 1 || tren.vagones[tren.tam - 1][1] == 0)) {
                    //Si el tamaño es 1 y esta al borde
                    tablero[tren.vagones[0][1]][tren.vagones[0][0]].setEstado(".");
                    if (tren.getTipo().equals("0")) {
                        //Si va hacia abajo, disminuye
                        if(tren.vagones[tren.tam - 1][1] == NUM_FILAS - 1) {
                            //Si esta en el borde de arriba y es tipo 0, lo bajo
                            tren.vagones[tren.tam - 1][1]--;
                        }else {
                            //Si esta en el borde de abajo lo elimino
                            tren.tam--;
                        }
                    } else {
                        //Si va hacia arriba, aumenta
                        if(tren.vagones[tren.tam - 1][1] == 0) {
                            //Si esta en el borde de arriba y es tipo 0, lo bajo
                            tren.vagones[tren.tam - 1][1]++;
                        }else {
                            //Si esta en el borde de abajo lo elimino
                            tren.tam--;
                        }
                    }
                } else {
                    for (int i = 1; i < tren.tam - 1; i++) {
                        //En este bucle compruebo que no le hayan chocado en la iteracion anterior en el medio
                        if (tablero[tren.vagones[i][1]][tren.vagones[0][0]].getEstado().equals("X")) {
                            int j, cont = 1;

//                            tablero[tren.vagones[0][0]][tren.vagones[0][1]].setEstado(".");
                            //Actualizo el ultimo vagon del tren

                            trenes[contTren] = new Trenes(i, tren.getTipo());
                            //Creo un nuevo tren que se quedara en el choque

                            for (j = 0; j < i; j++) {
                                //Copio los valores que hay antes de la x al nuevo tren
                                trenes[contTren].vagones[j][0] = tren.vagones[j][0];
                                trenes[contTren].vagones[j][1] = tren.vagones[j][1];
                                cont++;
                            }
                            for (int u = 0; u < cont; u++) {
                                //Avanzo a lo largo del tren antiguo para reducir su tamaño y conservar las casillas
                                for (int h = 0; h < tren.tam; h++) {
                                    if (tren.getTipo().equals("0")) {
                                        //Si va hacia abajo, disminuye
                                        tren.vagones[h][1]--;
                                    } else {
                                        //Si va hacia arriba, aumenta
                                        tren.vagones[h][1]++;
                                    }
                                }
                            }
                            tren.tam = tren.tam - i - 1; //Actualizo el tamaño del nuevo tren grande
                            contTren++; //Aumento el numero de trenes totales
                            break;
                        }
                    }
                    if (tren.vagones[tren.tam - 1][1] == NUM_FILAS - 1 || tren.vagones[tren.tam - 1][1] == 0) {
                        //Caso de llegada al borde del tablero
                        tablero[tren.vagones[0][1]][tren.vagones[0][0]].setEstado(".");
                        for (int i = 0; i < tren.tam; i++)
                            if (tren.getTipo().equals("0")) {
                                //Si va hacia la izquierda, disminuye
                                tren.vagones[i][1]--;
                            } else {
                                //Si va hacia la derecha, aumenta
                                tren.vagones[i][1]++;
                            }
                        tren.tam--;
                    } else {
                        //Caso de cualquier movimiento fuera del borde
                        if ((tablero[tren.vagones[tren.tam - 1][1] + mov][tren.vagones[0][0]].getEstado().equals("."))) {
                            //Si la siguiente posicion esta libre
                            tablero[tren.vagones[0][1]][tren.vagones[0][0]].setEstado(".");
                            for (int i = 0; i < tren.tam; i++) {
                                if (tren.getTipo().equals("0")) {
                                    //Si va hacia la izquierda, disminuye
                                    tren.vagones[i][1]--;
                                } else {
                                    //Si va hacia la derecha, aumenta
                                    tren.vagones[i][1]++;
                                }
                            }
                        } else if (tablero[(tren.vagones[(tren.tam) - 1][1]) + mov][tren.vagones[0][0]].getEstado().equals("X")) {
                            //Si la siguiente posicion es un choque existente
                            tablero[tren.vagones[0][1]][tren.vagones[0][0]].setEstado(".");
                            for (int i = 0; i < tren.tam; i++)
                                if (tren.getTipo().equals("0")) {
                                    //Si va hacia la izquierda, disminuye
                                    tren.vagones[i][1]--;
                                } else {
                                    //Si va hacia la derecha, aumenta
                                    tren.vagones[i][1]++;
                                }
                            tren.tam--;
                        } else {
                            //Si la siguiente posicion sera un choque nuevo
                            tablero[tren.vagones[tren.tam - 1][1] + mov][tren.vagones[0][0]].setEstado("X");
                            tablero[tren.vagones[0][1]][tren.vagones[0][0]].setEstado(".");
                            for (int i = 0; i < tren.tam; i++)
                                if (tren.getTipo().equals("0")) {
                                    //Si va hacia la izquierda, disminuye
                                    tren.vagones[i][1]--;
                                } else {
                                    //Si va hacia la derecha, aumenta
                                    tren.vagones[i][1]++;
                                }
                            tren.tam--;
                        }
                    }
                }
                if (tren.tam == 0) {
                    //Si su tamaño es 0 lo elimino
                    tren.setTipo("-1");
                }
                break;
            case "H":
                if (tablero[tren.vagones[0][1]][tren.vagones[0][0]].getEstado().equals("X")) {
                    //Comprobamos si hay colision en la cola del tren
                    for (int i = 0; i < tren.tam; i++) {
                        if (tren.getTipo().equals("2")) {
                            //Si va hacia la izquierda, disminuye
                            tren.vagones[i][0]--;
                        } else {
                            //Si va hacia la derecha, aumenta
                            tren.vagones[i][0]++;
                        }
                    }
                    tren.tam--;
                } else if (tablero[tren.vagones[0][1]][tren.vagones[tren.tam - 1][0]].getEstado().equals("X")) {
                    // Si el tren ya ha sido chocado por otro DE FRENTE elimino una posicion
                    tren.tam--;
                }
                if (tren.tam == 0) {
                    //Si ha sido chocado y su tamaño nuevo es 0 lo eliminamos de la lista y salimos del bucle
                    tren.setTipo("-1");
                    break;
                } else if (tren.tam == 1 && tren.vagones[tren.tam - 1][0] != NUM_COLUMNAS - 1 && tren.vagones[tren.tam - 1][0] != 0) {
                    //Si el tamaño es 1 y no esta al borde
                    if (!tablero[tren.vagones[0][1]][(tren.vagones[(tren.tam) - 1][0]) + mov].getEstado().equals(".")) {
                        tablero[tren.vagones[0][1]][(tren.vagones[(tren.tam) - 1][0]) + mov].setEstado("X");
                        tablero[tren.vagones[0][1]][(tren.vagones[0][0])].setEstado(".");
                        if (tren.getTipo().equals("2")) {
                            //Si va hacia la izquierda, disminuye
                            tren.vagones[tren.tam - 1][0]--;
                        } else {
                            //Si va hacia la derecha, aumenta
                            tren.vagones[tren.tam - 1][0]++;
                        }
                        tren.tam--;
                    } else {
                        tablero[tren.vagones[0][1]][tren.vagones[tren.tam - 1][0]].setEstado(".");
                        if (tren.getTipo().equals("2")) {
                            //Si va hacia la izquierda, disminuye
                            tren.vagones[tren.tam - 1][0]--;
                        } else {
                            //Si va hacia la derecha, aumenta
                            tren.vagones[tren.tam - 1][0]++;
                        }
                    }
                } else if (tren.tam == 1 && (tren.vagones[tren.tam - 1][0] == NUM_COLUMNAS - 1 || tren.vagones[tren.tam - 1][0] == 0)) {
                    //Si el tamaño es 1 y esta al borde
                    tablero[tren.vagones[0][1]][tren.vagones[0][0]].setEstado(".");
                    if (tren.getTipo().equals("2")) {
                        //Si va hacia abajo, disminuye
                        if(tren.vagones[tren.tam - 1][0] == NUM_COLUMNAS - 1)
                            //Si esta en el borde de arriba y es tipo 0, lo bajo
                            tren.vagones[tren.tam - 1][0]--;
                        else {
                            //Si esta en el borde de abajo lo elimino
                            tren.tam--;
                        }
                    } else {
                        //Si va hacia arriba, aumenta
                        if(tren.vagones[tren.tam - 1][0] == 0)
                            //Si esta en el borde de arriba y es tipo 0, lo bajo
                            tren.vagones[tren.tam - 1][0]++;
                        else {
                            //Si esta en el borde de abajo lo elimino
                            tren.tam--;
                        }
                    }
                } else {
                    //Tamaño mayor que 1
                    for (int i = 1; i < tren.tam - 1; i++) {
                        //En este bucle compruebo que no le hayan chocado en la iteracion anterior en el medio
                        if (tablero[tren.vagones[0][1]][tren.vagones[i][0]].getEstado().equals("X")) {
                            int j, cont = 1;

                            tablero[tren.vagones[0][1]][tren.vagones[0][0]].setEstado(".");
                            //Actualizo el ultimo vagon del tren

                            trenes[contTren] = new Trenes(i, tren.getTipo());
                            //Creo un nuevo tren que se quedara en el choque

                            for (j = 0; j < i; j++) {
                                //Copio los valores que hay antes de la x al nuevo tren
                                trenes[contTren].vagones[j][0] = tren.vagones[j][0];
                                trenes[contTren].vagones[j][1] = tren.vagones[j][1];
                                cont++;
                            }
                            for (int u = 0; u < cont; u++) {
                                //Avanzo a lo largo del tren antiguo para reducir su tamaño y conservar las casillas
                                for (int h = 0; h < tren.tam; h++) {
                                    if (tren.getTipo().equals("2")) {
                                        //Si va hacia abajo, disminuye
                                        tren.vagones[h][0]--;
                                    } else {
                                        //Si va hacia arriba, aumenta
                                        tren.vagones[h][0]++;
                                    }
                                }
                            }
                            tren.tam = tren.tam - i - 1; //Actualizo el tamaño del nuevo tren grande
                            contTren++; //Aumento el numero de trenes totales
                            break;
                        }
                    }
                    if (tren.vagones[tren.tam - 1][0] == NUM_COLUMNAS - 1 || tren.vagones[tren.tam - 1][0] == 0) {
                        //Caso de llegada al borde del tablero
                        tablero[tren.vagones[0][1]][tren.vagones[0][0]].setEstado(".");
                        for (int i = 0; i < tren.tam; i++)
                            if (tren.getTipo().equals("2")) {
                                //Si va hacia la izquierda, disminuye
                                tren.vagones[i][0]--;
                            } else {
                                //Si va hacia la derecha, aumenta
                                tren.vagones[i][0]++;
                            }
                        tren.tam--;
                    } else {
                        //Caso de cualquier movimiento fuera del borde
                        if ((tablero[tren.vagones[0][1]][tren.vagones[tren.tam - 1][0] + mov].getEstado().equals("."))) {
                            //Si la siguiente posicion esta libre
                            tablero[tren.vagones[0][1]][tren.vagones[0][0]].setEstado(".");
                            for (int i = 0; i < tren.tam; i++) {
                                if (tren.getTipo().equals("2")) {
                                    //Si va hacia la izquierda, disminuye
                                    tren.vagones[i][0]--;
                                } else {
                                    //Si va hacia la derecha, aumenta
                                    tren.vagones[i][0]++;
                                }
                            }
                        } else if (tablero[tren.vagones[0][1]][(tren.vagones[(tren.tam) - 1][0]) + mov].getEstado().equals("X")) {
                            //Si la siguiente posicion es un choque existente
                            tablero[tren.vagones[0][1]][tren.vagones[0][0]].setEstado(".");
                            if (tren.tam > 1) {
                                for (int i = 0; i < tren.tam; i++)
                                    if (tren.getTipo().equals("2")) {
                                        //Si va hacia la izquierda, disminuye
                                        tren.vagones[i][0]--;
                                    } else {
                                        //Si va hacia la derecha, aumenta
                                        tren.vagones[i][0]++;
                                    }
                            }
                            tren.tam--;
                        } else {
                            //Si la siguiente posicion sera un choque nuevo
                            tablero[tren.vagones[0][1]][(tren.vagones[(tren.tam) - 1][0]) + mov].setEstado("X");
                            tablero[tren.vagones[0][1]][tren.vagones[0][0]].setEstado(".");

                                for (int i = 0; i < tren.tam; i++)
                                    if (tren.getTipo().equals("2")) {
                                        //Si va hacia la izquierda, disminuye
                                        tren.vagones[i][0]--;
                                    } else {
                                        //Si va hacia la derecha, aumenta
                                        tren.vagones[i][0]++;
                                    }
                            tren.tam--;
                        }
                    }
                }
                if (tren.tam == 0) {
                    //Si su tamaño es 0 lo elimino
                    tren.setTipo("-1");
                }
                break;
        }
        actualizarTablero(tablero, trenes);
    }


    public static void ordenarTrenes(Trenes[] trenes) {
        for (int i = 0; i < (contTren - 1); i++) {
            for (int j = i + 1; j < contTren; j++) {
                if (Integer.parseInt(trenes[i].tipo) > Integer.parseInt(trenes[j].tipo) && !trenes[i].getTipo().equals("-1") && !trenes[j].getTipo().equals("-1")) {
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
        if (x < 0 || x > NUM_COLUMNAS || y < 0 || y > NUM_FILAS) {
            error();
        }
        trenes[contTren] = new Trenes(tam, tipo);

//        if (!tablero[y][x].getEstado().equals(".")) {
//            error();
//        }
        int i, cont;
        switch (trenes[contTren].tipo) {
            case "0":
                cont = y + tam - 1;
                if (cont > NUM_FILAS) {
                    error();
                }
                for (i = 0; i < tam; i++) {
//                    if (comprobarCasilla(tablero, cont, x)) {
                    trenes[contTren].vagones[i][0] = x;
                    trenes[contTren].vagones[i][1] = cont;
                    cont--;
//                    } else
//                        error();
                }
                break;
            case "1":
                cont = y - tam + 1;
                if (cont < 0) {
                    error();
                }
                for (i = 0; i < tam; i++) {
//                    if (comprobarCasilla(tablero, cont, x)) {
                    trenes[contTren].vagones[i][0] = x;
                    trenes[contTren].vagones[i][1] = cont;
                    cont++;
//                    } else
//                        error();
                }
                break;
            case "2":
                cont = x + tam - 1;
                if (cont > NUM_COLUMNAS) {
                    error();
                }
                for (i = 0; i < tam; i++) {
//                    if (comprobarCasilla(tablero, cont, x)) {
                    trenes[contTren].vagones[i][1] = y;
                    trenes[contTren].vagones[i][0] = cont;
                    cont--;
//                    } else
//                        error();
                }
                break;
            case "3":
                cont = x - tam + 1;
                if (cont < 0) {
                    error();
                }
                for (i = 0; i < tam; i++) {
//                    if (comprobarCasilla(tablero, cont, x)) {
                    trenes[contTren].vagones[i][0] = cont;
                    trenes[contTren].vagones[i][1] = y;
                    cont++;
//                    } else
//                        error();
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

    public static void buscarCoincidencias(Casilla[][] tablero, Trenes[] trenes) {
        for (int k = 0; k < contTren; k++) {
            for (int i = 0; i < trenes[k].tam; i++) {
                switch (trenes[k].getTipo()) {
                    case "0":
                        if (!(tablero[trenes[k].vagones[i][1]][trenes[k].vagones[0][0]].getEstado().equals("0"))) {
                            //Busco en todas las posiciones del tren si hay alguna casilla que no sea 0
                            tablero[trenes[k].vagones[i][1]][trenes[k].vagones[0][0]].setEstado("X");
                            //Setteo la X en la posicion donde solapa
                            if (i == 0) {
                                //Si solapan en la cola
                                for (int t = 0; t < trenes[k].tam; t++) {
                                    trenes[k].vagones[t][1]--;
                                }
                                trenes[k].tam--;
                            } else if (i == trenes[k].tam - 1) {
                                //Si solapan en la cabeza
                                trenes[k].tam--;
                            } else {
                                //Si solapan en cualquier otra parte del tren
                                int var = trenes[k].tam - i - 1;
                                //var es el tamaño del nuevo tren
                                trenes[contTren] = new Trenes(var, "0");
                                int piv = i + 1;
                                for (int j = 0; j < trenes[contTren].tam; j++) {
                                    trenes[contTren].vagones[j][0] = trenes[k].vagones[0][0];
                                    trenes[contTren].vagones[j][1] = trenes[k].vagones[piv][1];
                                    piv++;
                                }
                                trenes[k].tam -= (var + 1);
                                contTren++;
                            }
                        }
                        break;
                    case "1":
                        if (!(tablero[trenes[k].vagones[i][1]][trenes[k].vagones[0][0]].getEstado().equals("1"))) {
                            //Busco en todas las posiciones del tren si hay alguna casilla que no sea 1
                            tablero[trenes[k].vagones[i][1]][trenes[k].vagones[0][0]].setEstado("X");
                            //Setteo la X en la posicion donde solapa
                            if (i == 0) {
                                //Si solapan en la cola
                                for (int t = 0; t < trenes[k].tam; t++) {
                                    trenes[k].vagones[t][1]++;
                                }
                                trenes[k].tam--;
                            } else if (i == trenes[k].tam - 1) {
                                //Si solapan en la cabeza
                                trenes[k].tam--;
                            } else {
                                //Si solapan en cualquier otra parte del tren
                                int var = trenes[k].tam - i - 1;
                                //var es el tamaño del nuevo tren
                                trenes[contTren] = new Trenes(var, "1");
                                int piv = i + 1;
                                for (int j = 0; j < trenes[contTren].tam; j++) {
                                    trenes[contTren].vagones[j][0] = trenes[k].vagones[0][0];
                                    trenes[contTren].vagones[j][1] = trenes[k].vagones[piv][1];
                                    piv++;
                                }
                                trenes[k].tam -= (var + 1);
                                contTren++;
                            }
                        }
                        break;
                    case "2":
                        if (!(tablero[trenes[k].vagones[0][1]][trenes[k].vagones[i][0]].getEstado().equals("2"))) {
                            //Busco en todas las posiciones del tren si hay alguna casilla que no sea 2
                            tablero[trenes[k].vagones[0][1]][trenes[k].vagones[i][0]].setEstado("X");
                            //Setteo la X en la posicion donde solapa
                            if (i == 0) {
                                //Si solapan en la cola
                                for (int t = 0; t < trenes[k].tam; t++) {
                                    trenes[k].vagones[t][0]--;
                                }
                                trenes[k].tam--;
                            } else if (i == trenes[k].tam - 1) {
                                //Si solapan en la cabeza
                                trenes[k].tam--;
                            } else {
                                //Si solapan en cualquier otra parte del tren
                                int var = trenes[k].tam - i - 1;
                                //var es el tamaño del nuevo tren
                                trenes[contTren] = new Trenes(var, "2");
                                int piv = i + 1;
                                for (int j = 0; j < trenes[contTren].tam; j++) {
                                    trenes[contTren].vagones[j][0] = trenes[k].vagones[piv][0];
                                    trenes[contTren].vagones[j][1] = trenes[k].vagones[0][1];
                                    piv++;
                                }
                                trenes[k].tam -= (var + 1);
                                contTren++;
                            }
                        }
                        break;
                    case "3":

                        if (!(tablero[trenes[k].vagones[0][1]][trenes[k].vagones[i][0]].getEstado().equals("3"))) {
                            //Busco en todas las posiciones del tren si hay alguna casilla que no sea 3
                            tablero[trenes[k].vagones[0][1]][trenes[k].vagones[i][0]].setEstado("X");
                            //Setteo la X en la posicion donde solapa
                            if (i == 0) {
                                //Si solapan en la cola
                                for (int t = 0; t < trenes[k].tam; t++) {
                                    trenes[k].vagones[t][0]++;
                                }
                                trenes[k].tam--;
                            } else if (i == trenes[k].tam - 1) {
                                //Si solapan en la cabeza
                                trenes[k].tam--;
                            } else {
                                //Si solapan en cualquier otra parte del tren
                                int var = trenes[k].tam - i - 1;
                                trenes[contTren] = new Trenes(var, "3");
                                int piv = i + 1;
                                for (int j = 0; j < trenes[contTren].tam; j++) {
                                    trenes[contTren].vagones[j][0] = trenes[k].vagones[piv][0];
                                    trenes[contTren].vagones[j][1] = trenes[k].vagones[0][1];
                                    piv++;
                                }
                                trenes[k].tam -= (var + 1);
                                contTren++;
                            }
                        }
                        break;
                }
                actualizarTablero(tablero, trenes);
            }
        }
    }

    public static void imprimirTablero(Casilla[][] tablero, Trenes[] trenes) {
        actualizarTablero(tablero, trenes);
        System.out.print("  ");
        int h = 0;
        for (int k = 0; k < NUM_COLUMNAS; k++) {
            if (k % 10 == 0 && k != 0)
                h++;
            System.out.print(" " + h);
        }
        System.out.print("\n  ");
        h = 0;
        for (int l = 0; l < NUM_COLUMNAS; l++) {
            if (h % 10 == 0)
                h = 0;
            System.out.print(" " + h);
            h++;

        }
        System.out.print("\n");

        for (int i = 0; i < NUM_FILAS; i++) {
            if ((NUM_FILAS - 1 - i) >= 10) {
                System.out.printf("%d", NUM_FILAS - 1 - i);
            } else
                System.out.printf("0%d", NUM_FILAS - 1 - i);
            for (int j = 0; j < NUM_COLUMNAS; j++) {
                System.out.print(" " + tablero[NUM_FILAS - 1 - i][j].getEstado());
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
        for (int i = 0; i < NUM_FILAS; i++) {
            for (int j = 0; j < NUM_COLUMNAS; j++) {
                tablero[i][j] = new Casilla();
            }
        }
    }

    public static boolean comprobarFin(Casilla[][] tablero) {
        for (int i = 0; i < NUM_FILAS; i++) {
            for (int j = 0; j < NUM_COLUMNAS; j++) {
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
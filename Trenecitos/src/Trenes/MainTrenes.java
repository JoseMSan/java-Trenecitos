package Trenes;

import java.sql.SQLOutput;
import java.util.Scanner;

public class MainTrenes {
    static int contTren = 0;
    static int colisiones[][] = new int[100][2];
    static int contChoques = 0;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

//        while (sc.hasNext()) {
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
        ordenarTrenes(trenes);
        simulacion(tablero, trenes);
//        imprimirTablero(tablero, trenes);

//        }
        sc.close();
    }

    public static void simulacion(Casilla[][] tablero, Trenes[] trenes) {
        int t = 0;
        while (!comprobarFin(tablero)) {
//        while (t < 30) {
            for (int k = 0; k < contTren; k++) {
                System.out.println("Comprobando la pos " + k + " del array de trenes");
                if (!trenes[k].tipo.equals("-1")) {
                    switch (trenes[k].tipo) {
                        case "0":
                            for (int i = 1; i < trenes[k].tam - 1; i++) {
                                if (tablero[trenes[k].vagones[i][1]][trenes[k].vagones[0][0]].getEstado().equals("x")) {
                                    System.out.println("He ecnontrado una x en el medio de tren 1");
//                                    System.out.println("Añadiendo un punto a las coord i: "+trenes[k].vagones[0][1]+" j : "+trenes[k].vagones[0][0]);
                                    int j, cont = 1;
//                                    System.out.println("Antes de settear el punto :");
                                    System.out.println("Tren grande viejo");
                                    for (j = 0; j < trenes[k].tam; j++) {
                                        for (int h = 0; h < 2; h++) {
                                            System.out.printf("[%d]", trenes[k].vagones[j][h]);
                                        }
                                        System.out.print("\n");
                                    }
                                    tablero[trenes[k].vagones[0][1]][trenes[k].vagones[0][0]].setEstado(".");
                                    System.out.println("ME ACABA DE TOCAR POL MEDIO en i: " + trenes[k].vagones[0][1] + " j : " + trenes[k].vagones[i][0]);
                                    System.out.println("Creando un tren de tamaño " + (i) + " con tipo " + trenes[k].getTipo());
                                    System.out.println("Cont tren es :" + contTren);
                                    trenes[contTren] = new Trenes(i, trenes[k].getTipo(), contTren);
                                    trenes[contTren].setId(contTren);

                                    for (j = 0; j < i; j++) {//Copio los valores que hay antes de la x al nuevo tren
                                        System.out.println("Copiando valor i " + trenes[k].vagones[j + 1][0] + " j : " + trenes[k].vagones[j][1]);
                                        trenes[contTren].vagones[j][0] = trenes[k].vagones[j][0];
                                        trenes[contTren].vagones[j][1] = trenes[k].vagones[j][1];
                                        cont++;
                                    }
                                    for (int u = 0; u < cont; u++) {//Avanzo a lo largo del tren antiguo para reducir su tamaño y conservar las casillas
                                        for (int h = 0; h < trenes[k].tam; h++) {
                                            trenes[k].vagones[h][1]--;
                                        }
                                    }
                                    System.out.println("Modifico el tamaño del tren grande de : " + trenes[k].tam + " a : " + (trenes[k].tam - i - 1));
                                    trenes[k].tam = trenes[k].tam - i - 1;


                                    System.out.println("Tren pequeño nuevo");
                                    for (j = 0; j < trenes[contTren].tam; j++) {
                                        for (int h = 0; h < 2; h++) {
                                            System.out.printf("[%d]", trenes[contTren].vagones[j][h]);
                                        }
                                        System.out.print("\n");
                                    }
                                    System.out.println("Tren grande nuevo");
                                    for (j = 0; j < trenes[k].tam; j++) {
                                        for (int h = 0; h < 2; h++) {
                                            System.out.printf("[%d]", trenes[k].vagones[j][h]);
                                        }
                                        System.out.print("\n");
                                    }
                                    contTren++;
                                    break;
                                }
                            }
//                            System.out.println("Entro en 0 y mi id es :" + trenes[k].getId());
                            if (trenes[k].vagones[trenes[k].tam - 1][1] == 0) { //Caso de llegada al borde del tablero
                                tablero[trenes[k].vagones[0][1]][trenes[k].vagones[0][0]].setEstado(".");
                                if (trenes[k].tam > 1) {
                                    for (int i = 0; i < trenes[k].tam; i++) {
                                        if (trenes[k].vagones[i][1] >= 0)
                                            trenes[k].vagones[i][1]--;
                                    }
                                }
                                trenes[k].tam--;
                                if (trenes[k].tam == 0) //Comprobacion para que no de fallos cuando el tamaño es 0
                                    trenes[k].setTipo("-1");
                            } else { //Caso de cualquier movimiento
                                if (tablero[trenes[k].vagones[trenes[k].tam - 1][1]][trenes[k].vagones[0][0]].getEstado().equals("x")) { // Si el tren ya ha sido chocado por otro DE FRENTE elimino una posicion
                                    tablero[trenes[k].vagones[0][1]][trenes[k].vagones[0][0]].setEstado(".");
                                    trenes[k].tam--;
                                }
                                if (tablero[trenes[k].vagones[0][1]][trenes[k].vagones[0][0]].getEstado().equals("x")) {//Comprobamos si hay colision en la cola del tren
                                    for (int i = 0; i < trenes[k].tam; i++)
                                        trenes[k].vagones[i][1]--;
                                    trenes[k].tam--;
                                }
                                if ((tablero[(trenes[k].vagones[(trenes[k].tam) - 1][1]) - 1][trenes[k].vagones[0][0]]).getEstado().equals(".")) {
                                    //System.out.println("posicion a buscar : i " + ((trenes[k].vagones[(trenes[k].tam)-1][1])-1) + " j :" + trenes[k].vagones[0][0] + "     " + (tablero[(trenes[k].vagones[(trenes[k].tam-1)][1])-1][trenes[k].vagones[0][0]]).getEstado());

                                    //System.out.println("i: " + trenes[k].vagones[0][0] + " j : " + trenes[k].vagones[0][1]);
                                    tablero[trenes[k].vagones[0][1]][trenes[k].vagones[0][0]].setEstado(".");
                                    for (int i = 0; i < trenes[k].tam; i++)
                                        trenes[k].vagones[i][1]--;
                                } else if (tablero[(trenes[k].vagones[(trenes[k].tam) - 1][1]) - 1][trenes[k].vagones[0][0]].getEstado().equals("x")) {
                                    tablero[trenes[k].vagones[0][1]][trenes[k].vagones[0][0]].setEstado(".");
                                    for (int i = 0; i < trenes[k].tam; i++) {
                                        trenes[k].vagones[i][1]--;
                                    }
                                    trenes[k].tam--;
                                    if (trenes[k].tam == 0) {
                                        trenes[k].setTipo("-1");
                                    }
                                } else { //Caso de choque
                                    tablero[trenes[k].vagones[0][1]][trenes[k].vagones[0][0]].setEstado(".");
                                    tablero[((trenes[k].vagones[(trenes[k].tam) - 1][1]) - 1)][trenes[k].vagones[0][0]].setEstado("x");
                                    if (trenes[k].tam > 1) {
                                        for (int i = 0; i < trenes[k].tam; i++)
                                            trenes[k].vagones[i][1]--;
                                    }
                                    trenes[k].tam--;
                                    if (trenes[k].tam == 0) { //Hacemos que el tren deje de usarse como valido.
                                        trenes[k].setTipo("-1");
                                    }
                                }
                            }
//                            System.out.printf("Tren %d tamaño: %d\n",trenes[k].getId(),trenes[k].tam);
//                            for (int j = 0; j < trenes[k].tam; j++) {
//                                for (int h = 0; h < 2; h++) {
//                                    System.out.printf("[%d]", trenes[k].vagones[j][h]);
//                                }
//                                System.out.print("\n");
//                            }
                            actualizarTablero(tablero, trenes);
                            break;
                        case "1":
                            for (int i = 1; i < trenes[k].tam - 1; i++) {
                                if (tablero[trenes[k].vagones[i][1]][trenes[k].vagones[0][0]].getEstado().equals("x")) {
                                    System.out.println("He ecnontrado una x en el medio de tren 1");
//                                    System.out.println("Añadiendo un punto a las coord i: "+trenes[k].vagones[0][1]+" j : "+trenes[k].vagones[0][0]);
                                    int j, cont = 1;
//                                    System.out.println("Antes de settear el punto :");
                                    System.out.println("Tren grande viejo");
                                    for (j = 0; j < trenes[k].tam; j++) {
                                        for (int h = 0; h < 2; h++) {
                                            System.out.printf("[%d]", trenes[k].vagones[j][h]);
                                        }
                                        System.out.print("\n");
                                    }
                                    tablero[trenes[k].vagones[0][1]][trenes[k].vagones[0][0]].setEstado(".");
                                    System.out.println("ME ACABA DE TOCAR POL MEDIO en i: " + trenes[k].vagones[0][1] + " j : " + trenes[k].vagones[i][0]);
                                    System.out.println("Creando un tren de tamaño " + (i) + " con tipo " + trenes[k].getTipo());
                                    System.out.println("Cont tren es :" + contTren);
                                    trenes[contTren] = new Trenes(i, trenes[k].getTipo(), contTren);
                                    trenes[contTren].setId(contTren);

                                    for (j = 0; j < i; j++) {
                                        System.out.println("Copiando valor i " + trenes[k].vagones[j + 1][0] + " j : " + trenes[k].vagones[j][1]);
                                        trenes[contTren].vagones[j][0] = trenes[k].vagones[j][0];
                                        trenes[contTren].vagones[j][1] = trenes[k].vagones[j][1];
                                        cont++;
                                    }
                                    for (int u = 0; u < cont; u++) {
                                        for (int h = 0; h < trenes[k].tam; h++) {
                                            trenes[k].vagones[h][1]++;
                                        }
                                    }
                                    System.out.println("Modifico el tamaño del tren grande de : " + trenes[k].tam + " a : " + (trenes[k].tam - i - 1));
                                    trenes[k].tam = trenes[k].tam - i - 1;


                                    System.out.println("Tren pequeño nuevo");
                                    for (j = 0; j < trenes[contTren].tam; j++) {
                                        for (int h = 0; h < 2; h++) {
                                            System.out.printf("[%d]", trenes[contTren].vagones[j][h]);
                                        }
                                        System.out.print("\n");
                                    }
                                    System.out.println("Tren grande nuevo");
                                    for (j = 0; j < trenes[k].tam; j++) {
                                        for (int h = 0; h < 2; h++) {
                                            System.out.printf("[%d]", trenes[k].vagones[j][h]);
                                        }
                                        System.out.print("\n");
                                    }
                                    contTren++;
                                    break;
                                }
                            }
//                            System.out.println("Entro en 1 y mi id es :" + trenes[k].getId());
                            if (trenes[k].vagones[trenes[k].tam - 1][1] == 29) { //Caso de llegada al borde del tablero
                                tablero[trenes[k].vagones[0][1]][trenes[k].vagones[0][0]].setEstado(".");
                                if (trenes[k].tam > 1) {
                                    for (int i = 0; i < trenes[k].tam - 1; i++) {
                                        if (trenes[k].vagones[i][1] >= 0)
                                            trenes[k].vagones[i][1]++;
                                    }
                                }
                                trenes[k].tam--;
                                if (trenes[k].tam == 0) //Comprobacion para que no de fallos cuando el tamaño es 0
                                    trenes[k].setTipo("-1");
                            } else { //Caso cualquiera de movimiento
                                if (tablero[trenes[k].vagones[trenes[k].tam - 1][1]][trenes[k].vagones[0][0]].getEstado().equals("x")) { // Si el tren ya ha sido chocado por otro DE FRENTE elimino una posicion
                                    tablero[trenes[k].vagones[0][1]][trenes[k].vagones[0][0]].setEstado(".");
                                    trenes[k].tam--;
                                }
                                if (tablero[trenes[k].vagones[0][1]][trenes[k].vagones[0][0]].getEstado().equals("x")) {//Comprobamos si hay colision en la cola del tren
                                    for (int i = 0; i < trenes[k].tam; i++)
                                        trenes[k].vagones[i][1]++;
                                    trenes[k].tam--;
                                }


                                if ((tablero[(trenes[k].vagones[(trenes[k].tam) - 1][1]) + 1][trenes[k].vagones[0][0]]).getEstado().equals(".")) {
                                    //System.out.println("posicion a buscar : i " + ((trenes[k].vagones[(trenes[k].tam)-1][1])-1) + " j :" + trenes[k].vagones[0][0] + "     " + (tablero[(trenes[k].vagones[(trenes[k].tam-1)][1])-1][trenes[k].vagones[0][0]]).getEstado());

                                    //System.out.println("i: " + trenes[k].vagones[0][0] + " j : " + trenes[k].vagones[0][1]);
                                    tablero[trenes[k].vagones[0][1]][trenes[k].vagones[0][0]].setEstado(".");
                                    for (int i = 0; i < trenes[k].tam; i++)
                                        trenes[k].vagones[i][1]++;
                                } else if (tablero[(trenes[k].vagones[(trenes[k].tam) - 1][1]) + 1][trenes[k].vagones[0][0]].getEstado().equals("x")) {
                                    tablero[trenes[k].vagones[0][1]][trenes[k].vagones[0][0]].setEstado(".");
                                    if (trenes[k].tam > 1) {
                                        for (int i = 0; i < trenes[k].tam; i++)
                                            trenes[k].vagones[i][1]++;
                                    }
                                    trenes[k].tam--;
                                    if (trenes[k].tam == 0) {
                                        trenes[k].setTipo("-1");
                                    }
                                } else { //Caso de choque
                                    tablero[((trenes[k].vagones[(trenes[k].tam) - 1][1]) + 1)][trenes[k].vagones[0][0]].setEstado("x");
                                    tablero[trenes[k].vagones[0][1]][trenes[k].vagones[0][0]].setEstado(".");
                                    if (trenes[k].tam > 1) {
                                        for (int i = 0; i < trenes[k].tam; i++)
                                            trenes[k].vagones[i][1]++;
                                    }
                                    trenes[k].tam--;
                                    if (trenes[k].tam == 0) {
                                        trenes[k].setTipo("-1");
                                    }
                                }
                            }
//                            System.out.printf("Tren %d\n", trenes[k].getId());
//                            for (int j = 0; j < trenes[k].tam; j++) {
//                                for (int h = 0; h < 2; h++) {
//                                    System.out.printf("[%d]", trenes[k].vagones[j][h]);
//                                }
//                                System.out.print("\n");
//                            }
                            actualizarTablero(tablero, trenes);
                            break;
                        case "2":
                            for (int i = 1; i < trenes[k].tam - 1; i++) {
                                if (tablero[trenes[k].vagones[0][1]][trenes[k].vagones[i][0]].getEstado().equals("x")) {
//                                    System.out.println("Añadiendo un punto a las coord i: "+trenes[k].vagones[0][1]+" j : "+trenes[k].vagones[0][0]);
                                    int j, cont = 1;
//                                    System.out.println("Antes de settear el punto :");
                                    System.out.println("Tren grande viejo");
                                    for (j = 0; j < trenes[k].tam; j++) {
                                        for (int h = 0; h < 2; h++) {
                                            System.out.printf("[%d]", trenes[k].vagones[j][h]);
                                        }
                                        System.out.print("\n");
                                    }
                                    tablero[trenes[k].vagones[0][1]][trenes[k].vagones[0][0]].setEstado(".");
                                    System.out.println("ME ACABA DE TOCAR POL MEDIO en i: " + trenes[k].vagones[0][1] + " j : " + trenes[k].vagones[i][0]);
                                    System.out.println("Creando un tren de tamaño " + (i) + " con tipo " + trenes[k].getTipo());
                                    System.out.println("Cont tren es :" + contTren);
                                    trenes[contTren] = new Trenes(i, trenes[k].getTipo(), contTren);
                                    trenes[contTren].setId(contTren);

                                    for (j = 0; j < i; j++) {
                                        System.out.println("Copiando valor i " + trenes[k].vagones[j + 1][0] + " j : " + trenes[k].vagones[j][1]);
                                        trenes[contTren].vagones[j][0] = trenes[k].vagones[j][0];
                                        trenes[contTren].vagones[j][1] = trenes[k].vagones[j][1];
                                        cont++;
                                    }
                                    for (int u = 0; u < cont; u++) {
                                        for (int h = 0; h < trenes[k].tam; h++) {
                                            trenes[k].vagones[h][0]--;
                                        }
                                    }
                                    System.out.println("Modifico el tamaño del tren grande de : " + trenes[k].tam + " a : " + (trenes[k].tam - i - 1));
                                    trenes[k].tam = trenes[k].tam - i - 1;


                                    System.out.println("Tren pequeño nuevo");
                                    for (j = 0; j < trenes[contTren].tam; j++) {
                                        for (int h = 0; h < 2; h++) {
                                            System.out.printf("[%d]", trenes[contTren].vagones[j][h]);
                                        }
                                        System.out.print("\n");
                                    }
                                    System.out.println("Tren grande nuevo");
                                    for (j = 0; j < trenes[k].tam; j++) {
                                        for (int h = 0; h < 2; h++) {
                                            System.out.printf("[%d]", trenes[k].vagones[j][h]);
                                        }
                                        System.out.print("\n");
                                    }
                                    contTren++;
                                    break;
                                }
                            }
//                            System.out.println("Entro en 2 y mi id es :" + trenes[k].getId());
                            if (trenes[k].vagones[trenes[k].tam - 1][0] == 0 || trenes[k].tam==1) { //Caso de llegada al borde del tablero
                                tablero[trenes[k].vagones[0][1]][trenes[k].vagones[0][0]].setEstado(".");
                                if (trenes[k].tam > 1) {
                                    for (int i = 0; i < trenes[k].tam; i++)
                                        trenes[k].vagones[i][0]--;
                                }
                                trenes[k].tam--;
                                if (trenes[k].tam == 0) //El tipo de tren -1 no se tiene en cuenta para la siguiente iteracion(Hemos terminado con ese tren)
                                    trenes[k].setTipo("-1");
                            } else { //Caso cualquiera de movimiento
                                if (tablero[trenes[k].vagones[0][1]][trenes[k].vagones[trenes[k].tam - 1][0]].getEstado().equals("x")) { // Si el tren ya ha sido chocado por otro DE FRENTE elimino una posicion
                                    tablero[trenes[k].vagones[0][1]][trenes[k].vagones[0][0]].setEstado(".");
                                    trenes[k].tam--;
                                }
                                if (tablero[trenes[k].vagones[0][1]][trenes[k].vagones[0][0]].getEstado().equals("x")) {//Comprobamos si hay colision en la cola del tren
                                    tablero[trenes[k].vagones[0][1]][trenes[k].vagones[0][0]].setEstado(".");
                                    trenes[k].tam--;
                                }
                                if ((tablero[trenes[k].vagones[1][1]][(trenes[k].vagones[(trenes[k].tam) - 1][0]) - 1]).getEstado().equals(".")) {
                                    //System.out.println("posicion a buscar : i " + ((trenes[k].vagones[(trenes[k].tam)-1][1])-1) + " j :" + trenes[k].vagones[0][0] + "     " + (tablero[(trenes[k].vagones[(trenes[k].tam-1)][1])-1][trenes[k].vagones[0][0]]).getEstado());
                                    //System.out.println("i: " + trenes[k].vagones[0][0] + " j : " + trenes[k].vagones[0][1]);
                                    tablero[trenes[k].vagones[1][1]][trenes[k].vagones[0][0]].setEstado(".");
                                    for (int i = 0; i < trenes[k].tam; i++)
                                        trenes[k].vagones[i][0]--;
                                } else if (tablero[(trenes[k].vagones[(trenes[k].tam) - 1][0]) - 1][trenes[k].vagones[1][1]].getEstado().equals("x")) {
                                    tablero[trenes[k].vagones[0][9]][trenes[k].vagones[1][1]].setEstado(".");
                                    for (int i = 0; i < trenes[k].tam; i++) {
                                        trenes[k].vagones[i][1]--;
                                    }
                                    trenes[k].tam--;
                                    if (trenes[k].tam == 0) {
                                        trenes[k].setTipo("-1");
                                    }
                                } else { //Caso de choque
                                    tablero[trenes[k].vagones[0][1]][trenes[k].vagones[0][0]].setEstado(".");
                                    tablero[trenes[k].vagones[1][1]][((trenes[k].vagones[(trenes[k].tam) - 1][0]) - 1)].setEstado("x");
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
                            actualizarTablero(tablero, trenes);
                            break;

                        case "3":
//                            System.out.println("Entro en 3 y mi id es :" + trenes[k].getId());
                            for (int i = 1; i < trenes[k].tam - 1; i++) {
                                if (tablero[trenes[k].vagones[0][1]][trenes[k].vagones[i][0]].getEstado().equals("x")) {
//                                    System.out.println("Añadiendo un punto a las coord i: "+trenes[k].vagones[0][1]+" j : "+trenes[k].vagones[0][0]);
                                    int j, cont = 1;
//                                    System.out.println("Antes de settear el punto :");
                                    System.out.println("Tren grande viejo");
                                    for (j = 0; j < trenes[k].tam; j++) {
                                        for (int h = 0; h < 2; h++) {
                                            System.out.printf("[%d]", trenes[k].vagones[j][h]);
                                        }
                                        System.out.print("\n");
                                    }
                                    tablero[trenes[k].vagones[0][1]][trenes[k].vagones[0][0]].setEstado(".");
                                    System.out.println("ME ACABA DE TOCAR POL MEDIO en i: " + trenes[k].vagones[0][1] + " j : " + trenes[k].vagones[i][0]);
                                    System.out.println("Creando un tren de tamaño " + (i) + " con tipo " + trenes[k].getTipo());
                                    System.out.println("Cont tren es :" + contTren);
                                    trenes[contTren] = new Trenes(i, trenes[k].getTipo(), contTren);
                                    trenes[contTren].setId(contTren);

                                    for (j = 0; j < i; j++) {
                                        System.out.println("Copiando valor i " + trenes[k].vagones[j + 1][0] + " j : " + trenes[k].vagones[j][1]);
                                        trenes[contTren].vagones[j][0] = trenes[k].vagones[j][0];
                                        trenes[contTren].vagones[j][1] = trenes[k].vagones[j][1];
                                        cont++;
                                    }
                                    for (int u = 0; u < cont; u++) {
                                        for (int h = 0; h < trenes[k].tam; h++) {
                                            trenes[k].vagones[h][0]++;
                                        }
                                    }
                                    System.out.println("Modifico el tamaño del tren grande de : " + trenes[k].tam + " a : " + (trenes[k].tam - i - 1));
                                    trenes[k].tam = trenes[k].tam - i - 1;


                                    System.out.println("Tren pequeño nuevo");
                                    for (j = 0; j < trenes[contTren].tam; j++) {
                                        for (int h = 0; h < 2; h++) {
                                            System.out.printf("[%d]", trenes[contTren].vagones[j][h]);
                                        }
                                        System.out.print("\n");
                                    }
                                    System.out.println("Tren grande nuevo");
                                    for (j = 0; j < trenes[k].tam; j++) {
                                        for (int h = 0; h < 2; h++) {
                                            System.out.printf("[%d]", trenes[k].vagones[j][h]);
                                        }
                                        System.out.print("\n");
                                    }
                                    contTren++;
                                    break;
                                }
                            }
                            System.out.println(trenes[k].vagones[trenes[k].tam - 1][0]);
                            if (trenes[k].vagones[trenes[k].tam - 1][0] == 29 || trenes[k].tam==1) { //Caso de llegada al borde del tablero o que un trozo sea de tamaño 1
                                System.out.println("Llego al borde derecho");
                                System.out.println("Poniendo un punto en i : "+trenes[k].vagones[0][1]+" j :"+trenes[k].vagones[0][0]);
                                tablero[trenes[k].vagones[0][1]][trenes[k].vagones[0][0]].setEstado(".");
                                if (trenes[k].tam > 1) {
                                    System.out.println("Mide : " + trenes[k].tam);
                                    for (int i = 0; i < trenes[k].tam; i++)
                                        if (trenes[k].vagones[i][1] > 0)
                                            trenes[k].vagones[i][0]++;
                                }
                                trenes[k].tam--;
                                System.out.println("Ahora ya Mide : "+trenes[k].tam);
                                if (trenes[k].tam == 0) //Comprobacion para que no de fallos cuando el tamaño es 0
                                    trenes[k].setTipo("-1");

//                                trenes[k].tam--;
                                if (trenes[k].tam == 0) //Comprobacion para que no de fallos cuando el tamaño es 0
                                    trenes[k].setTipo("-1");
                            } else { //Caso cualquiera de movimiento
                                System.out.println("Checkeo si la cabeza coincide con   i: " + trenes[k].vagones[trenes[k].tam - 1][0] + " j :" + trenes[k].vagones[0][1]);
                                if (tablero[trenes[k].vagones[0][1]][trenes[k].vagones[trenes[k].tam - 1][0]].getEstado().equals("x")) { // Si el tren ya ha sido chocado por otro DE FRENTE elimino una posicion
                                    System.out.println("Cabeza de tren coincide con x, eliminando...");
                                    tablero[trenes[k].vagones[0][1]][trenes[k].vagones[0][0]].setEstado(".");
                                    trenes[k].tam--;
                                }
                                System.out.println("Coord de mi mov : i  :" + ((trenes[k].vagones[(trenes[k].tam) - 1][0]) + 1) + " j : " + trenes[k].vagones[0][1]);
                                System.out.println("Tren derecha: Casilla a la que me muevo: " + tablero[trenes[k].vagones[0][1]][(trenes[k].vagones[(trenes[k].tam) - 1][0]) + 1].getEstado());
//                                System.out.println("Estado de la pos donde deberia estar la x " + tablero[(trenes[k].vagones[(trenes[k].tam) - 1][0]) + 1][trenes[k].vagones[0][1]].getEstado());
                                if ((tablero[trenes[k].vagones[1][1]][trenes[k].vagones[trenes[k].tam - 1][0] + 1].getEstado().equals("."))) {
                                    //System.out.println("posicion a buscar : i " + ((trenes[k].vagones[(trenes[k].tam)-1][1])-1) + " j :" + trenes[k].vagones[0][0] + "     " + (tablero[(trenes[k].vagones[(trenes[k].tam-1)][1])-1][trenes[k].vagones[0][0]]).getEstado());
                                    //System.out.println("i: " + trenes[k].vagones[0][0] + " j : " + trenes[k].vagones[0][1]);
                                    tablero[trenes[k].vagones[1][1]][trenes[k].vagones[0][0]].setEstado(".");
                                    for (int i = 0; i < trenes[k].tam; i++)
                                        if (trenes[k].vagones[i][1] > 0)
                                            trenes[k].vagones[i][0]++;
                                } else if (tablero[trenes[k].vagones[0][1]][(trenes[k].vagones[(trenes[k].tam) - 1][0]) + 1].getEstado().equals("x")) {
                                    System.out.println("Entro a tengo una x despues");
                                    tablero[trenes[k].vagones[0][1]][trenes[k].vagones[0][0]].setEstado(".");
                                    if (trenes[k].tam > 1) {
                                        for (int i = 0; i < trenes[k].tam; i++)
                                            trenes[k].vagones[i][0]++;
                                    }
                                    trenes[k].tam--;
                                    if (trenes[k].tam == 0) {
                                        trenes[k].setTipo("-1");
                                    }
                                } else { //Caso de choque
                                    System.out.println("Ostion111 derechaaaa");
                                    System.out.println("Tren derecha ha chocado en  i :" + ((trenes[k].vagones[(trenes[k].tam) - 1][1]) + 1) + " j : " + trenes[k].vagones[0][0]);
                                    tablero[trenes[k].vagones[0][1]][(trenes[k].vagones[(trenes[k].tam) - 1][0]) + 1].setEstado("x");
                                    tablero[trenes[k].vagones[0][1]][trenes[k].vagones[0][0]].setEstado(".");
                                    if (trenes[k].tam > 1) {
                                        for (int i = 0; i < trenes[k].tam; i++)
                                            trenes[k].vagones[i][0]++;
                                    }
                                    trenes[k].tam--;
                                    if (trenes[k].tam == 0) {
                                        trenes[k].setTipo("-1");
                                    }
                                }

                            }
                            System.out.printf("Tren %d tamaño: %d\n", trenes[k].getId(), trenes[k].tam);
                            for (int j = 0; j < trenes[k].tam; j++) {
                                for (int h = 0; h < 2; h++) {
                                    System.out.printf("[%d]", trenes[k].vagones[j][h]);
                                }
                                System.out.print("\n");
                            }
//                            ordenarTrenes(trenes);
                            break;
                    }

                }
            }
            t++;
            System.out.println(t);
            imprimirTablero(tablero, trenes);
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
        for (int i = 0; i < contTren; i++) {
            trenes[i].setId(i);
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

        trenes[contTren] = new Trenes(tam, tipo, contTren);

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
//                for (int j = 0; j < tam; j++) {
//                    for (int h = 0; h < 2; h++) {
//                        System.out.printf("[%d]", trenes[contTren].vagones[j][h]);
//                    }
//                    System.out.print("\n");
//                }
                break;
            case "1":
                cont = y - tam + 1;
                if (cont < 0) {
                    error();
                }
                for (i = 0; i < tam; i++) {
                    trenes[contTren].vagones[i][0] = x;
                    trenes[contTren].vagones[i][1] = cont;
                    cont++;
                }
//                for (int j = 0; j < tam; j++) {
//                    for (int h = 0; h < 2; h++) {
//                        System.out.printf("[%d]", trenes[contTren].vagones[j][h]);
//                    }
//                    System.out.print("\n");
//                }
                break;
            case "2":
                cont = x + tam - 1;
                if (cont > 29) {
                    error();
                }
                for (i = 0; i < tam; i++) {
                    trenes[contTren].vagones[i][1] = y;
                    trenes[contTren].vagones[i][0] = cont;
                    cont--;
                }
//                for (int j = 0; j < tam; j++) {
//                    for (int h = 0; h < 2; h++) {
//                        System.out.printf("[%d]", trenes[contTren].vagones[j][h]);
//                    }
//                    System.out.print("\n");
//                }
                break;
            case "3":
                cont = x - tam + 1;
                if (cont < 0) {
                    error();
                }
                for (i = 0; i < tam; i++) {
                    trenes[contTren].vagones[i][0] = cont;
                    trenes[contTren].vagones[i][1] = y;
                    cont++;
                }
//                for (int j = 0; j < tam; j++) {
//                    for (int h = 0; h < 2; h++) {
//                        System.out.printf("[%d]", trenes[contTren].vagones[j][h]);
//                    }
//                    System.out.print("\n");
//                }
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
//                System.out.println("Tren " + k + " tipo " +trenes[k].getTipo() +" mide " + trenes[k].tam)
                if (!trenes[k].getTipo().equals("-1"))
                    if (trenes[k].vagones[i][1] >= 0 && trenes[k].vagones[i][0] >= 0 && !tablero[trenes[k].vagones[i][1]][trenes[k].vagones[i][0]].getEstado().equals("x"))
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
                    if (!tablero[i][j].getEstado().equals("x")) {
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

//for (int j = 0; j < trenes[k].tam; j++) {
//        for (int h = 0; h < 2; h++) {
//          System.out.printf("[%d]", trenes[k].vagones[j][h]);
//        }
//        System.out.print("\n");
//        }
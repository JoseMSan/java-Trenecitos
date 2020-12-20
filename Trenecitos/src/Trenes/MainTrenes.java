package Trenes;

import java.util.Scanner;

public class MainTrenes {
    static int contTren = 0;

    public static void main(String[] args) {
        Casilla[][] tablero = new Casilla[30][30];
        generarTablero(tablero);
        Scanner sc = new Scanner(System.in);
//        while (sc.hasNext()) {
        int n = sc.nextInt();
        String aux = sc.nextLine(); // Elimina el /n sobrante
        if (!(n >= 1 && n <= 10)) {
            error();
        }
        Trenes[] trenes = new Trenes[n];
        String[] datosTrenes = extraeDatosTrenes(n, sc);
        crearTrenes(datosTrenes, tablero, trenes);
        ordenarTrenes(trenes);
//        imprimirTablero(tablero, trenes);
        simulacion(tablero, trenes);
//        }
        sc.close();
    }

    public static void simulacion(Casilla[][] tablero, Trenes[] trenes) {
        while (!comprobarFin(tablero)) {
            for (int k = 0; k < contTren; k++) {
                if (!trenes[k].tipo.equals("-1")) {
                    switch (trenes[k].tipo) {
                        case "0":
                            if (trenes[k].vagones[trenes[k].tam - 1][1] == 0) { //Caso de llegada al borde del tablero
                                tablero[trenes[k].vagones[0][1]][trenes[k].vagones[0][0]].setEstado(".");
                                if (trenes[k].tam == 1) {
                                    trenes[k].tam--;
                                    trenes[k].setTipo("-1");
                                } else {
                                    for (int i = 0; i < trenes[k].tam; i++)
                                        trenes[k].vagones[i][1]--;
                                    trenes[k].tam--;
                                }
                            } else { //Caso cualquiera de movimiento
                                if ((tablero[(trenes[k].vagones[(trenes[k].tam) - 1][1]) - 1][trenes[k].vagones[0][0]]).getEstado().equals(".")) {
                                    //System.out.println("posicion a buscar : i " + ((trenes[k].vagones[(trenes[k].tam)-1][1])-1) + " j :" + trenes[k].vagones[0][0] + "     " + (tablero[(trenes[k].vagones[(trenes[k].tam-1)][1])-1][trenes[k].vagones[0][0]]).getEstado());

                                    //System.out.println("i: " + trenes[k].vagones[0][0] + " j : " + trenes[k].vagones[0][1]);
                                    tablero[trenes[k].vagones[0][1]][trenes[k].vagones[0][0]].setEstado(".");
                                    for (int i = 0; i < trenes[k].tam; i++)
                                        trenes[k].vagones[i][1]--;
                                } else { //Caso de choque
                                    System.out.println("Estoy setteando una x en i: " + ((trenes[k].vagones[(trenes[k].tam) - 1][1]) - 1) + " j : " + trenes[k].vagones[0][0]);
                                    tablero[(trenes[k].vagones[(trenes[k].tam) - 1][1]) - 1][trenes[k].vagones[0][0]].setEstado("x");
                                }
                            }
                            break;
                        case "1":
                            if (trenes[k].vagones[trenes[k].tam - 1][1] == 29) { //Caso de llegada al borde del tablero
                                tablero[trenes[k].vagones[0][1]][trenes[k].vagones[0][0]].setEstado(".");
                                if (trenes[k].tam == 1) {
                                    trenes[k].tam--;
                                    trenes[k].setTipo("-1");
                                } else {
                                    for (int i = 0; i <trenes[k].tam-1; i++) {
                                        trenes[k].vagones[i][1]=trenes[k].vagones[i+1][1];
                                    }
                                    trenes[k].tam--;
                                }
                            } else { //Caso cualquiera de movimiento
                                if ((tablero[(trenes[k].vagones[(trenes[k].tam) - 1][1]) + 1][trenes[k].vagones[0][0]]).getEstado().equals(".")) {
                                    //System.out.println("posicion a buscar : i " + ((trenes[k].vagones[(trenes[k].tam)-1][1])-1) + " j :" + trenes[k].vagones[0][0] + "     " + (tablero[(trenes[k].vagones[(trenes[k].tam-1)][1])-1][trenes[k].vagones[0][0]]).getEstado());

                                    //System.out.println("i: " + trenes[k].vagones[0][0] + " j : " + trenes[k].vagones[0][1]);
                                    tablero[trenes[k].vagones[0][1]][trenes[k].vagones[0][0]].setEstado(".");
                                    for (int i = 0; i < trenes[k].tam; i++)
                                        trenes[k].vagones[i][1]++;
                                } else { //Caso de choque
                                    System.out.println("Estoy setteando una x en i: " + ((trenes[k].vagones[(trenes[k].tam) - 1][1]) - 1) + " j : " + trenes[k].vagones[0][0]);
                                    tablero[(trenes[k].vagones[(trenes[k].tam) - 1][1]) - 1][trenes[k].vagones[0][0]].setEstado("x");
                                }
                            }
                            break;
                        case "2":
                            if (trenes[k].vagones[trenes[k].tam - 1][0] == 0) { //Caso de llegada al borde del tablero
                                tablero[trenes[k].vagones[0][1]][trenes[k].vagones[0][0]].setEstado(".");
                                if (trenes[k].tam == 1) {
                                    trenes[k].tam--;
                                    trenes[k].setTipo("-1");
                                } else {
                                    for (int i = 0; i < trenes[k].tam; i++)
                                        trenes[k].vagones[i][0]--;
                                    trenes[k].tam--;
                                }
                            } else { //Caso cualquiera de movimiento
                                if ((tablero[(trenes[k].vagones[(trenes[k].tam) - 1][0]) - 1][trenes[k].vagones[1][1]]).getEstado().equals(".")) {
                                    //System.out.println("posicion a buscar : i " + ((trenes[k].vagones[(trenes[k].tam)-1][1])-1) + " j :" + trenes[k].vagones[0][0] + "     " + (tablero[(trenes[k].vagones[(trenes[k].tam-1)][1])-1][trenes[k].vagones[0][0]]).getEstado());

                                    //System.out.println("i: " + trenes[k].vagones[0][0] + " j : " + trenes[k].vagones[0][1]);
                                    tablero[trenes[k].vagones[1][1]][trenes[k].vagones[0][0]].setEstado(".");
                                    for (int i = 0; i < trenes[k].tam; i++)
                                        trenes[k].vagones[i][0]--;
                                } else { //Caso de choque
                                    System.out.println("Estoy setteando una x en i: " + ((trenes[k].vagones[(trenes[k].tam) - 1][1]) - 1) + " j : " + trenes[k].vagones[0][0]);
                                    tablero[(trenes[k].vagones[(trenes[k].tam) - 1][1]) - 1][trenes[k].vagones[0][0]].setEstado("x");
                                }
                            }
                            break;
                        case "3":
                            if (trenes[k].vagones[trenes[k].tam - 1][0] == 29) { //Caso de llegada al borde del tablero
                                tablero[trenes[k].vagones[0][1]][trenes[k].vagones[0][0]].setEstado(".");
                                if (trenes[k].tam == 1) {
                                    trenes[k].tam--;
                                    trenes[k].setTipo("-1");
                                } else {
                                    for (int i = 0; i < trenes[k].tam; i++)
                                        trenes[k].vagones[i][0]++;
                                    trenes[k].tam--;
                                }
                            } else { //Caso cualquiera de movimiento
                                if ((tablero[(trenes[k].vagones[(trenes[k].tam) - 1][0]) + 1][trenes[k].vagones[1][1]]).getEstado().equals(".")) {
                                    //System.out.println("posicion a buscar : i " + ((trenes[k].vagones[(trenes[k].tam)-1][1])-1) + " j :" + trenes[k].vagones[0][0] + "     " + (tablero[(trenes[k].vagones[(trenes[k].tam-1)][1])-1][trenes[k].vagones[0][0]]).getEstado());

                                    //System.out.println("i: " + trenes[k].vagones[0][0] + " j : " + trenes[k].vagones[0][1]);
                                    tablero[trenes[k].vagones[1][1]][trenes[k].vagones[0][0]].setEstado(".");
                                    for (int i = 0; i < trenes[k].tam; i++)
                                        trenes[k].vagones[i][0]++;
                                } else { //Caso de choque
                                    System.out.println("Estoy setteando una x en i: " + ((trenes[k].vagones[(trenes[k].tam) - 1][1]) - 1) + " j : " + trenes[k].vagones[0][0]);
                                    tablero[(trenes[k].vagones[(trenes[k].tam) - 1][1]) - 1][trenes[k].vagones[0][0]].setEstado("x");
                                }
                            }
                            for (int j = 0; j < trenes[k].tam; j++) {
                                for (int h = 0; h < 2; h++) {
                                    System.out.printf("[%d]", trenes[k].vagones[j][h]);
                                }
                                System.out.print("\n");
                            }
                            for (int j = 0; j < trenes[k].tam; j++) {
                                for (int h = 0; h < 2; h++) {
                                    System.out.printf("[%d]", trenes[k].vagones[j][h]);
                                }
                                System.out.print("\n");
                            }
                            break;
                    }
                }
            }
            imprimirTablero(tablero, trenes);
        }
    }

    public static void ordenarTrenes(Trenes[] trenes) {
        for (int i = 0; i < (trenes.length - 1); i++) {
            for (int j = i + 1; j < trenes.length; j++) {
                if (Integer.parseInt(trenes[i].tipo) > Integer.parseInt(trenes[j].tipo)) {
                    String aux = trenes[i].tipo;
                    trenes[i].tipo = trenes[j].tipo;
                    trenes[j].tipo = aux;
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

        trenes[contTren] = new Trenes(tam, Integer.parseInt(tipo), contTren);

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
                    if(comprobarCasilla(tablero, cont, x)) {
                        trenes[contTren].vagones[i][0] = x;
                        trenes[contTren].vagones[i][1] = cont;
                        cont--;
                    }else
                        error();
                }
                break;
            case "1":
                cont = y - tam + 1;
                if(cont<0){
                    error();
                }
                for (i =0;i<tam;i++) {
                    trenes[contTren].vagones[i][0]=x;
                    trenes[contTren].vagones[i][1]=cont;
                    cont++;
                }
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
                for(int j=0;j<tam;j++) {
                    for ( int h = 0; h < 2; h++) {
                        System.out.printf("[%d]",trenes[contTren].vagones[j][h]);
                    }
                    System.out.print("\n");
                }
                break;
        }
        contTren++;
        actualizarTablero(tablero,trenes);
        imprimirTablero(tablero, trenes);
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


package Trenes;

import java.util.Scanner;

public class MainTrenes {
    static int contTren=0;
    public static void main (String[] args){
        Casilla [][] tablero = new Casilla[30][30];
        generarTablero(tablero);
        Scanner sc = new Scanner(System.in);
//        while (sc.hasNext()) {
            int n = sc.nextInt();
            String aux = sc.nextLine(); // Elimina el /n sobrante
            if (!(n >= 1 && n <= 10)) {
                error();
            }
            Trenes [] trenes = new Trenes[n];
            String[] datosTrenes = extraeDatosTrenes(n, sc);
            colocarTrenes(datosTrenes, tablero, trenes);
//            simulacion(tablero);

            imprimirTablero(tablero,trenes);
//        }
        sc.close();
    }

//    public static void simulacion(Casilla [][] tablero) {
//        for (int i = 0; i < 30; i++){
//            for (int j = 0; j < 30; j++){
//
//            }
//        }
//    }

    public static void colocarTrenes(String []datosTrenes, Casilla [][] tablero, Trenes[] trenes) {
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

        trenes[contTren] = new Trenes(tam,Integer.parseInt(tipo));

        if(!tablero[y][x].getEstado().equals(".")){
            error();
        }
        int i,cont;
        switch (trenes[contTren].tipo) {
            case "0":
                cont = y + tam - 1;
                if(cont>29){
                    error();
                }
                for (i =0;i<tam;i++) {
                    trenes[contTren].vagones[i][1]=cont;
                    trenes[contTren].vagones[i][0]=x;
                    cont--;
                }
                for(int j=0;j<tam;j++) {
                    for ( int h = 0; h < 2; h++) {
                        System.out.printf("[%d]",trenes[contTren].vagones[j][h]);
                    }
                    System.out.print("\n");
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
                for(int j=0;j<tam;j++) {
                    for ( int h = 0; h < 2; h++) {
                        System.out.printf("[%d]",trenes[contTren].vagones[j][h]);
                    }
                    System.out.print("\n");
                }
                break;
            case "2":
                cont = x + tam - 1;
                if(cont>29){
                    error();
                }
                for (i =0;i<tam;i++) {
                    trenes[contTren].vagones[i][1]=y;
                    trenes[contTren].vagones[i][0]=cont;
                    cont--;
                }
                for(int j=0;j<tam;j++) {
                    for ( int h = 0; h < 2; h++) {
                        System.out.printf("[%d]",trenes[contTren].vagones[j][h]);
                    }
                    System.out.print("\n");
                }
                break;
            case "3":
                cont = x - tam + 1;
                if(cont<0){
                    error();
                }
                for (i =0;i<tam;i++) {
                    trenes[contTren].vagones[i][0]=cont;
                    trenes[contTren].vagones[i][1]=y;
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
    }

    public static String[] extraeDatosTrenes(int n, Scanner sc){
        String [] datosTrenes = new String[n];
        for (int i = 0; i < n; i++) {
            datosTrenes[i]=sc.nextLine();
        }
        return datosTrenes;
    }

    public static void imprimirTablero(Casilla[][] tablero, Trenes [] trenes) {
        System.out.print("  ");
        for (int k = 0;k<30;k++) {
            if(k<10){
                System.out.print(" 0");
            }else if(k<20){
                System.out.print(" 1");
            }else {
                System.out.print(" 2");
            }
        }
        System.out.print("\n  ");
        for(int l=0;l<3;l++) {
            System.out.print(" 0 1 2 3 4 5 6 7 8 9");
        }
        System.out.print("\n");
        int piv = contTren;
        contTren=0;

        for (int k = 0; k < piv; k++) {
            for(int i = 0; i<trenes[k].tam;i++){
//                System.out.println("Tren " + k + " tipo " +trenes[k].getTipo() +" mide " + trenes[k].tam);
                    tablero[trenes[k].vagones[i][1]][trenes[k].vagones[i][0]].setEstado(trenes[k].getTipo());
            }
        }

        for (int i = 0; i < 30; i++) {
            if((29-i)>=10){
                System.out.printf("%d", 29 - i);
            }else
                System.out.printf("0%d", 29 - i);
            for (int j = 0; j < 30; j++) {
                System.out.print(" "+tablero[29-i][j].estado);
            }
            System.out.print("\n");
        }



    }


    public static void generarTablero(Casilla[][] tablero) {
        for (int i = 0; i < 30; i++) {
            for (int j = 0; j < 30; j++) {
                tablero[i][j] = new Casilla();
            }
        }
    }

    public static void error() {
        System.out.println("Conjunto de trenes incorrecto.");
        System.exit(-1);
    }
}

package Trenes;

import java.util.Scanner;

public class MainTrenes {
    public static void main (String[] args){
        Casilla [][] tablero = new Casilla[30][30];
        generarTablero(tablero);
        imprimirTablero(tablero);
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        String aux = sc.nextLine(); // Elimina el /n sobrante
        if(!(n>=1 && n<=10)) {
            error();
        }
        String []datosTrenes = extraeDatosTrenes(n,sc);
        colocarTrenes(datosTrenes, tablero);



        imprimirTablero(tablero);
        sc.close();
    }

    public static void colocarTrenes(String []datosTrenes, Casilla [][] tablero) {
        for (String datosTrene : datosTrenes) {
            String[] datos = datosTrene.split(" ");
            switch (datos[0]) {
                case "B":
                    dibujarTrenes("0", datos[1], datos[2], datos[3], tablero);
                    break;
                case "A":
                    dibujarTrenes("1", datos[1], datos[2], datos[3], tablero);
                    break;
                case "I":
                    dibujarTrenes("2", datos[1], datos[2], datos[3], tablero);
                    break;
                case "D":
                    dibujarTrenes("3", datos[1], datos[2], datos[3], tablero);
                    break;
                default:
                    error();
            }
        }
    }

    public static void dibujarTrenes(String tipo, String tam, String val1, String val2, Casilla[][] tablero) {
        int x = Integer.parseInt(val1);
        int y = Integer.parseInt(val2);
        int i;
        if(!tablero[y][x].estado.equals(".")){
            error();
        }
        tablero[y][x].estado=tipo;
        switch (tablero[y][x].estado) {
            case "0":
                if((y+Integer.parseInt(tam)-1)>29){
                    error();
                }
                for (i = (y + Integer.parseInt(tam)-1); i >= y; i--) {
                    tablero[i][x].estado=tipo;
                }
                break;
            case "1":
                if((y-Integer.parseInt(tam)+1)<0){
                    error();
                }
                for (i = (y - Integer.parseInt(tam)+1); i <= y; i++) {
                    tablero[i][x].estado=tipo;
                }
                break;
            case "2":
                if((x+Integer.parseInt(tam)-1)>29){
                    error();
                }
                for (i = (x + Integer.parseInt(tam)-1); i >= x; i--) {
                    tablero[y][i].estado=tipo;
                }
                break;
            case "3":
                if((x-Integer.parseInt(tam)+1)<0){
                    error();
                }
                for (i = (x - Integer.parseInt(tam)+1); i <= x; i++) {
                    tablero[y][i].estado=tipo;
                }
                break;
        }
    }

    public static String[] extraeDatosTrenes(int n, Scanner sc){
        String [] datosTrenes = new String[n];
        for (int i = 0; i < n; i++) {
            datosTrenes[i]=sc.nextLine();
        }
        return datosTrenes;
    }

    public static void imprimirTablero(Casilla[][] tablero) {
        System.out.print("  ");
        for (int k = 0;k<30;k++) {
            if(k<10){
                System.out.print(" 0");
            }else if(k>=10 && k<20){
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

package Trenes;


public class Trenes {
    int [][] vagones;
    String tipo;
    int tam;

    public Trenes(int tam, int tipo) {
        this.vagones = new int[tam][2];
        this.tipo = String.valueOf(tipo);
        this.tam = tam;
    }

    public int[][] getVagones() {
        return vagones;
    }

    public void setVagones(int[][] vagones) {
        this.vagones = vagones;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = String.valueOf(tipo);
    }
}

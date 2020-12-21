package Trenes;


public class Trenes {
    int [][] vagones;
    String tipo;
    int tam;
    int id;

    public Trenes(int tam, String tipo, int id) {
        this.vagones = new int[tam][2];
        this.tipo = tipo;
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

    public void setTipo(String tipo) {
        this.tipo = String.valueOf(tipo);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

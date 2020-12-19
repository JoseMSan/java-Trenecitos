package Trenes;
/*
Tomare el numero 1 como si la casilla estuviera ocupada y el 0 como desocupada
 */
public class Casilla {
    String estado;

    public Casilla () {
        this.estado = ".";
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}

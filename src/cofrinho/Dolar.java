package cofrinho;

public class Dolar extends Moeda {

    public Dolar(double valor) {
        super(valor);
    }

    @Override
    void info() {
        System.out.println("Dolar - " + valor);
    }

    @Override
    double converter() {
        return valor * 5.2; // Cotação de exemplo
    }
    
    @Override
    public String toString() {
        return "Dolar [valor=" + valor + "]";
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        return true;
    }
}
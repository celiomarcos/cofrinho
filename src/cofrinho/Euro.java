package cofrinho;

public class Euro extends Moeda {

    public Euro(double valor) {
        super(valor);
    }

    @Override
    void info() {
        System.out.println("Euro - " + valor);
    }

    @Override
    double converter() {
        return valor * 6.0; // Cotação exemplo
    }
    
    @Override
    public String toString() {
        return "Euro [valor=" + valor + "]";
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
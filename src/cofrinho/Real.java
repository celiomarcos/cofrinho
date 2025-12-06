package cofrinho;

public class Real extends Moeda {

    public Real(double valor) {
        super(valor);
    }

    @Override
    void info() {
        System.out.println("Real - " + valor);
    }

    @Override
    double converter() {
        return valor;
    }

    @Override
    public String toString() {
        return "Real [valor=" + valor + "]";
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
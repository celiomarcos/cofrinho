package cofrinho;

/**
 * Classe abstrata que representa uma moeda genérica.
 * Serve como classe mãe para as moedas específicas (Dolar, Euro, Real).
 * Demonstra o conceito de HERANÇA - as classes filhas herdam o atributo 'valor'
 * e devem implementar os métodos abstratos info() e converter().
 * 
 * @author Celio Marcos
 */
public abstract class Moeda {
    
    // Atributo protegido para que as classes filhas possam acessar diretamente
    protected double valor;
    
    /**
     * Construtor da moeda com valor inicial.
     * @param valor O valor nominal da moeda
     */
    public Moeda(double valor) {
        this.valor = valor;
    }
    
    /**
     * Retorna o valor nominal da moeda.
     * @return valor da moeda
     */
    public double getValor() {
        return valor;
    }
    
    /**
     * Método abstrato que retorna informações sobre a moeda.
     * Cada classe filha implementa de forma específica (POLIMORFISMO).
     * @return String com informações da moeda
     */
    public abstract String info();
    
    /**
     * Método abstrato que converte o valor da moeda para Real (BRL).
     * Cada classe filha implementa sua própria taxa de conversão (POLIMORFISMO).
     * @return valor convertido para Real
     */
    public abstract double converter();
    
    /**
     * hashCode baseado no valor e no tipo da moeda.
     * Necessário para funcionamento correto do ArrayList.remove()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(valor);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
    
    /**
     * Compara moedas pelo valor e pelo tipo.
     * Duas moedas são iguais se têm o mesmo valor E são do mesmo tipo.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        // Verifica se são do mesmo tipo (Dolar com Dolar, Euro com Euro, etc.)
        if (getClass() != obj.getClass())
            return false;
        Moeda other = (Moeda) obj;
        if (Double.doubleToLongBits(valor) != Double.doubleToLongBits(other.valor))
            return false;
        return true;
    }
}

package cofrinho;

/**
 * Classe que representa a moeda Real (BRL).
 * Herda de Moeda e implementa os métodos abstratos.
 * Como é a moeda base, a conversão é 1:1.
 * 
 * Demonstra HERANÇA: extends Moeda
 * Demonstra POLIMORFISMO: implementa info() e converter() de forma específica
 * 
 * @author Celio Marcos
 */
public class Real extends Moeda {
    
    /**
     * Construtor que recebe o valor em Reais.
     * @param valor O valor em Real
     */
    public Real(double valor) {
        super(valor);
    }
    
    /**
     * Retorna informações formatadas sobre a moeda Real.
     * Implementação específica do método abstrato da classe mãe.
     * @return String com tipo e valor da moeda
     */
    @Override
    public String info() {
        return String.format("Real: R$ %.2f", valor);
    }
    
    /**
     * Converte para Real (conversão 1:1, pois já é Real).
     * @return o próprio valor, sem conversão
     */
    @Override
    public double converter() {
        // Real para Real = 1:1 (sem conversão necessária)
        return valor;
    }
    
    /**
     * Representação textual da moeda para exibição.
     */
    @Override
    public String toString() {
        return info() + " (Convertido: R$ " + String.format("%.2f", converter()) + ")";
    }
}

package cofrinho;

/**
 * Classe que representa a moeda Dolar Americano (USD).
 * Herda de Moeda e implementa os metodos abstratos.
 * 
 * Demonstra HERANCA: extends Moeda
 * Demonstra POLIMORFISMO: implementa info() e converter() de forma especifica
 * 
 * A taxa de conversao pode ser obtida dinamicamente via API
 * ou usar um valor padrao caso a API esteja indisponivel.
 * 
 * @author Celio Marcos
 */
public class Dolar extends Moeda {
    
    // Taxa de conversao padrao (fallback)
    private static final double TAXA_PADRAO = 5.50;
    
    // Taxa dinamica carregada da API (compartilhada entre todas as instancias)
    private static double taxaConversao = TAXA_PADRAO;
    
    /**
     * Construtor que recebe o valor em Dolares.
     * @param valor O valor em Dolar
     */
    public Dolar(double valor) {
        super(valor);
    }
    
    /**
     * Retorna informacoes formatadas sobre a moeda Dolar.
     * Implementacao especifica do metodo abstrato da classe mae.
     * @return String com tipo e valor da moeda
     */
    @Override
    public String info() {
        return String.format("Dolar: US$ %.2f", valor);
    }
    
    /**
     * Converte o valor de Dolar para Real.
     * Utiliza a taxa de conversao (dinamica ou padrao).
     * @return valor convertido para Real
     */
    @Override
    public double converter() {
        // Dolar para Real: multiplica pela taxa de conversao
        return valor * taxaConversao;
    }
    
    /**
     * Define a taxa de conversao para todas as instancias de Dolar.
     * Chamado pelo CotacaoService ao carregar dados da API.
     * @param taxa nova taxa de conversao USD -> BRL
     */
    public static void setTaxaConversao(double taxa) {
        taxaConversao = taxa;
    }
    
    /**
     * Retorna a taxa de conversao atual.
     * @return taxa de conversao USD -> BRL
     */
    public static double getTaxaConversao() {
        return taxaConversao;
    }
    
    /**
     * Representacao textual da moeda para exibicao.
     */
    @Override
    public String toString() {
        return info() + " (Convertido: R$ " + String.format("%.2f", converter()) + ")";
    }
}

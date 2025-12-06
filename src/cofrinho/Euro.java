package cofrinho;

/**
 * Classe que representa a moeda Euro (EUR).
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
public class Euro extends Moeda {
    
    // Taxa de conversao padrao (fallback)
    private static final double TAXA_PADRAO = 6.00;
    
    // Taxa dinamica carregada da API (compartilhada entre todas as instancias)
    private static double taxaConversao = TAXA_PADRAO;
    
    /**
     * Construtor que recebe o valor em Euros.
     * @param valor O valor em Euro
     */
    public Euro(double valor) {
        super(valor);
    }
    
    /**
     * Retorna informacoes formatadas sobre a moeda Euro.
     * Implementacao especifica do metodo abstrato da classe mae.
     * @return String com tipo e valor da moeda
     */
    @Override
    public String info() {
        return String.format("Euro: EUR %.2f", valor);
    }
    
    /**
     * Converte o valor de Euro para Real.
     * Utiliza a taxa de conversao (dinamica ou padrao).
     * @return valor convertido para Real
     */
    @Override
    public double converter() {
        // Euro para Real: multiplica pela taxa de conversao
        return valor * taxaConversao;
    }
    
    /**
     * Define a taxa de conversao para todas as instancias de Euro.
     * Chamado pelo CotacaoService ao carregar dados da API.
     * @param taxa nova taxa de conversao EUR -> BRL
     */
    public static void setTaxaConversao(double taxa) {
        taxaConversao = taxa;
    }
    
    /**
     * Retorna a taxa de conversao atual.
     * @return taxa de conversao EUR -> BRL
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

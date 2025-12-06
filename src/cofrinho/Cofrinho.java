package cofrinho;

import java.util.ArrayList;

/**
 * Classe que representa o Cofrinho de moedas.
 * Gerencia uma coleção de objetos Moeda (pode conter Real, Dolar, Euro).
 * 
 * Demonstra o uso de POLIMORFISMO através da coleção:
 * - listaMoedas é do tipo ArrayList<Moeda> (classe abstrata)
 * - Mas armazena objetos concretos (Real, Dolar, Euro)
 * - Ao chamar converter() em cada moeda, o Java executa a implementação
 *   específica de cada classe filha (comportamento polimórfico)
 * 
 * @author Celio Marcos
 */
public class Cofrinho {
    
    // Coleção polimórfica: aceita qualquer subclasse de Moeda
    private ArrayList<Moeda> listaMoedas;
    
    /**
     * Construtor que inicializa o ArrayList vazio.
     */
    public Cofrinho() {
        this.listaMoedas = new ArrayList<Moeda>();
    }
    
    /**
     * Adiciona uma moeda ao cofrinho.
     * Aceita qualquer tipo de Moeda (Real, Dolar, Euro) - POLIMORFISMO
     * @param moeda A moeda a ser adicionada
     */
    public void adicionar(Moeda moeda) {
        listaMoedas.add(moeda);
        System.out.println("Moeda adicionada com sucesso: " + moeda.info());
    }
    
    /**
     * Remove uma moeda específica do cofrinho.
     * Utiliza equals() para encontrar a moeda correspondente.
     * @param moeda A moeda a ser removida
     * @return true se removeu, false se não encontrou
     */
    public boolean remover(Moeda moeda) {
        boolean removido = listaMoedas.remove(moeda);
        if (removido) {
            System.out.println("Moeda removida com sucesso: " + moeda.info());
        } else {
            System.out.println("Moeda não encontrada no cofrinho!");
        }
        return removido;
    }
    
    /**
     * Lista todas as moedas presentes no cofrinho.
     * Utiliza POLIMORFISMO: ao chamar toString() de cada moeda,
     * o Java executa a implementação específica de cada classe filha.
     */
    public void listagemMoedas() {
        if (listaMoedas.isEmpty()) {
            System.out.println("O cofrinho está vazio!");
            return;
        }
        
        System.out.println("\n========== MOEDAS NO COFRINHO ==========");
        int contador = 1;
        for (Moeda moeda : listaMoedas) {
            // Polimorfismo: cada moeda exibe suas informações de forma específica
            System.out.println(contador + ". " + moeda.toString());
            contador++;
        }
        System.out.println("==========================================");
        System.out.println("Total de moedas: " + listaMoedas.size());
    }
    
    /**
     * Calcula o valor total do cofrinho convertido para Real.
     * Demonstra POLIMORFISMO em ação:
     * - Percorre a lista chamando converter() em cada moeda
     * - O Java automaticamente chama a implementação correta
     *   (Real.converter(), Dolar.converter() ou Euro.converter())
     * @return valor total em Reais
     */
    public double totalConvertido() {
        double total = 0.0;
        
        for (Moeda moeda : listaMoedas) {
            // Polimorfismo: converter() se comporta diferente para cada tipo
            total += moeda.converter();
        }
        
        return total;
    }
    
    /**
     * Exibe o resumo do cofrinho com totais por moeda e total geral.
     */
    public void exibirResumo() {
        double totalReais = 0.0;
        double totalDolares = 0.0;
        double totalEuros = 0.0;
        int qtdReais = 0;
        int qtdDolares = 0;
        int qtdEuros = 0;
        
        for (Moeda moeda : listaMoedas) {
            if (moeda instanceof Real) {
                totalReais += moeda.getValor();
                qtdReais++;
            } else if (moeda instanceof Dolar) {
                totalDolares += moeda.getValor();
                qtdDolares++;
            } else if (moeda instanceof Euro) {
                totalEuros += moeda.getValor();
                qtdEuros++;
            }
        }
        
        System.out.println("\n============ RESUMO DO COFRINHO ============");
        System.out.printf("Reais:   %d moeda(s) = R$ %.2f%n", qtdReais, totalReais);
        System.out.printf("Dólares: %d moeda(s) = US$ %.2f%n", qtdDolares, totalDolares);
        System.out.printf("Euros:   %d moeda(s) = € %.2f%n", qtdEuros, totalEuros);
        System.out.println("---------------------------------------------");
        System.out.printf("TOTAL CONVERTIDO PARA REAL: R$ %.2f%n", totalConvertido());
        System.out.println("=============================================");
    }
    
    /**
     * Retorna a quantidade de moedas no cofrinho.
     * @return número de moedas
     */
    public int getQuantidadeMoedas() {
        return listaMoedas.size();
    }
    
    /**
     * Retorna a lista de moedas (para sincronizacao com nuvem).
     * @return ArrayList de moedas
     */
    public ArrayList<Moeda> getListaMoedas() {
        return listaMoedas;
    }
    
    /**
     * Carrega moedas de uma lista externa (da nuvem).
     * @param moedas Lista de moedas para carregar
     */
    public void carregarMoedas(ArrayList<Moeda> moedas) {
        this.listaMoedas = moedas;
    }
}

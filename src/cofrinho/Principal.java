package cofrinho;

import java.util.Scanner;

/**
 * Classe principal que executa o sistema do Cofrinho.
 * Fornece um menu interativo para o usuario gerenciar suas moedas.
 * 
 * Na inicializacao, busca cotacoes em tempo real da API AwesomeAPI.
 * 
 * Funcionalidades:
 * - Adicionar moedas (Real, Dolar, Euro)
 * - Remover moedas especificas
 * - Listar todas as moedas
 * - Calcular total convertido para Real
 * - Atualizar cotacoes em tempo real
 * 
 * @author Celio Marcos
 */
public class Principal {

    // Servico de cotacoes (compartilhado)
    private static CotacaoService cotacaoService;

    public static void main(String[] args) {
        
        Scanner teclado = new Scanner(System.in);
        Cofrinho cofrinho = new Cofrinho();
        int opcao;
        
        System.out.println("===================================");
        System.out.println("     BEM-VINDO AO COFRINHO");
        System.out.println("  Sistema de Gerenciamento de Moedas");
        System.out.println("===================================");
        System.out.println();
        
        // Inicializa o servico de cotacoes (busca da API)
        System.out.println("Carregando cotacoes atualizadas...");
        cotacaoService = new CotacaoService();
        System.out.println();
        
        do {
            exibirMenu();
            
            while (!teclado.hasNextInt()) {
                System.out.println("Por favor, digite um numero valido!");
                teclado.next();
            }
            opcao = teclado.nextInt();
            
            switch (opcao) {
                case 1:
                    adicionarMoeda(teclado, cofrinho);
                    break;
                case 2:
                    removerMoeda(teclado, cofrinho);
                    break;
                case 3:
                    cofrinho.listagemMoedas();
                    break;
                case 4:
                    cofrinho.exibirResumo();
                    break;
                case 5:
                    exibirCotacoesAtuais();
                    break;
                case 6:
                    cotacaoService.atualizarCotacoes();
                    break;
                case 0:
                    System.out.println("\nObrigado por usar o Cofrinho!");
                    System.out.printf("Voce esta saindo com R$ %.2f em moedas.%n", 
                                      cofrinho.totalConvertido());
                    break;
                default:
                    System.out.println("\nOpcao invalida! Tente novamente.");
            }
            
        } while (opcao != 0);
        
        teclado.close();
    }
    
    /**
     * Exibe o menu principal de opcoes.
     */
    private static void exibirMenu() {
        System.out.println("\n---------- MENU ----------");
        System.out.println("1 - Adicionar moeda");
        System.out.println("2 - Remover moeda");
        System.out.println("3 - Listar moedas");
        System.out.println("4 - Total convertido para Real");
        System.out.println("5 - Ver cotacoes atuais");
        System.out.println("6 - Atualizar cotacoes (API)");
        System.out.println("0 - Sair");
        System.out.println("--------------------------");
        System.out.print("Escolha uma opcao: ");
    }
    
    /**
     * Exibe as cotacoes atuais do Dolar e Euro.
     */
    private static void exibirCotacoesAtuais() {
        System.out.println("\n----- COTACOES ATUAIS -----");
        System.out.printf("Dolar (USD): R$ %.4f%n", Dolar.getTaxaConversao());
        System.out.printf("Euro (EUR):  R$ %.4f%n", Euro.getTaxaConversao());
        
        if (cotacaoService.isCotacoesCarregadas()) {
            System.out.println("Fonte: API AwesomeAPI (tempo real)");
        } else {
            System.out.println("Fonte: Valores padrao (API indisponivel)");
        }
        System.out.println("---------------------------");
    }
    
    /**
     * Realiza a adicao de uma nova moeda ao cofrinho.
     * Demonstra POLIMORFISMO: variavel 'moeda' e do tipo Moeda (abstrato)
     * mas recebe instancia de uma classe concreta (Real, Dolar ou Euro).
     */
    private static void adicionarMoeda(Scanner teclado, Cofrinho cofrinho) {
        int tipoMoeda = 0;
        
        System.out.println("\n--- ADICIONAR MOEDA ---");
        System.out.println("Selecione o tipo de moeda:");
        System.out.printf("1 - Real (BRL)%n");
        System.out.printf("2 - Dolar (USD) - Cotacao: R$ %.4f%n", Dolar.getTaxaConversao());
        System.out.printf("3 - Euro (EUR) - Cotacao: R$ %.4f%n", Euro.getTaxaConversao());
        
        while (tipoMoeda < 1 || tipoMoeda > 3) {
            System.out.print("Tipo: ");
            while (!teclado.hasNextInt()) {
                System.out.println("Digite um numero valido (1-3)!");
                teclado.next();
            }
            tipoMoeda = teclado.nextInt();
            
            if (tipoMoeda < 1 || tipoMoeda > 3) {
                System.out.println("Tipo invalido! Escolha 1, 2 ou 3.");
            }
        }
        
        System.out.print("Digite o valor: ");
        while (!teclado.hasNextDouble()) {
            System.out.println("Digite um valor numerico valido!");
            teclado.next();
        }
        double valor = teclado.nextDouble();
        
        if (valor <= 0) {
            System.out.println("Valor deve ser maior que zero!");
            return;
        }
        
        // POLIMORFISMO: moeda pode ser Real, Dolar ou Euro
        Moeda moeda = null;
        
        switch (tipoMoeda) {
            case 1:
                moeda = new Real(valor);
                break;
            case 2:
                moeda = new Dolar(valor);
                break;
            case 3:
                moeda = new Euro(valor);
                break;
        }
        
        if (moeda != null) {
            cofrinho.adicionar(moeda);
        }
    }
    
    /**
     * Realiza a remocao de uma moeda do cofrinho.
     * Cria uma moeda temporaria para busca usando equals().
     */
    private static void removerMoeda(Scanner teclado, Cofrinho cofrinho) {
        
        if (cofrinho.getQuantidadeMoedas() == 0) {
            System.out.println("\nO cofrinho esta vazio! Nada para remover.");
            return;
        }
        
        int tipoMoeda = 0;
        
        System.out.println("\n--- REMOVER MOEDA ---");
        System.out.println("Selecione o tipo de moeda a remover:");
        System.out.println("1 - Real (BRL)");
        System.out.println("2 - Dolar (USD)");
        System.out.println("3 - Euro (EUR)");
        
        while (tipoMoeda < 1 || tipoMoeda > 3) {
            System.out.print("Tipo: ");
            while (!teclado.hasNextInt()) {
                System.out.println("Digite um numero valido (1-3)!");
                teclado.next();
            }
            tipoMoeda = teclado.nextInt();
            
            if (tipoMoeda < 1 || tipoMoeda > 3) {
                System.out.println("Tipo invalido! Escolha 1, 2 ou 3.");
            }
        }
        
        System.out.print("Digite o valor da moeda a remover: ");
        while (!teclado.hasNextDouble()) {
            System.out.println("Digite um valor numerico valido!");
            teclado.next();
        }
        double valor = teclado.nextDouble();
        
        // Cria moeda para busca (sera usada no equals)
        Moeda moeda = null;
        
        switch (tipoMoeda) {
            case 1:
                moeda = new Real(valor);
                break;
            case 2:
                moeda = new Dolar(valor);
                break;
            case 3:
                moeda = new Euro(valor);
                break;
        }
        
        if (moeda != null) {
            cofrinho.remover(moeda);
        }
    }
}

package cofrinho;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Classe principal que executa o sistema do Cofrinho.
 * 
 * Recursos:
 * - Cotacoes em tempo real (API AwesomeAPI)
 * - Armazenamento na nuvem (API JSONBin.io)
 * - Sincronizacao automatica ao adicionar/remover moedas
 * - Persistencia entre execucoes do programa
 * 
 * @author Celio Marcos
 */
public class Principal {

    // Servicos
    private static CotacaoService cotacaoService;
    private static CloudStorageService cloudService;
    private static Cofrinho cofrinho;
    private static Scanner teclado;

    public static void main(String[] args) {
        
        teclado = new Scanner(System.in);
        cofrinho = new Cofrinho();
        int opcao;
        
        exibirBanner();
        
        // Inicializa servico de cotacoes
        System.out.println("Carregando cotacoes atualizadas...");
        cotacaoService = new CotacaoService();
        System.out.println();
        
        // Inicializa servico de nuvem
        System.out.println("Conectando ao armazenamento em nuvem...");
        cloudService = new CloudStorageService();
        
        // Tenta carregar dados da nuvem
        if (cloudService.isConfigurado()) {
            ArrayList<Moeda> moedasNuvem = cloudService.carregarDaNuvem();
            if (moedasNuvem != null && !moedasNuvem.isEmpty()) {
                cofrinho.carregarMoedas(moedasNuvem);
                System.out.println("Cofrinho sincronizado com a nuvem!");
            }
        } else {
            System.out.println("Nuvem nao configurada. Use opcao 8 para configurar.");
        }
        System.out.println();
        
        // Loop principal
        do {
            exibirMenu();
            
            while (!teclado.hasNextInt()) {
                System.out.println("Por favor, digite um numero valido!");
                teclado.next();
            }
            opcao = teclado.nextInt();
            
            switch (opcao) {
                case 1:
                    adicionarMoeda();
                    break;
                case 2:
                    removerMoeda();
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
                case 7:
                    sincronizarNuvem();
                    break;
                case 8:
                    configurarNuvem();
                    break;
                case 9:
                    exibirStatusNuvem();
                    break;
                case 0:
                    encerrarPrograma();
                    break;
                default:
                    System.out.println("\nOpcao invalida! Tente novamente.");
            }
            
        } while (opcao != 0);
        
        teclado.close();
    }
    
    /**
     * Exibe banner inicial do programa.
     */
    private static void exibirBanner() {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║         BEM-VINDO AO COFRINHO          ║");
        System.out.println("║    Sistema de Gerenciamento de Moedas  ║");
        System.out.println("║                                        ║");
        System.out.println("║  * Cotacoes em tempo real              ║");
        System.out.println("║  * Sincronizacao com a nuvem           ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println();
    }
    
    /**
     * Exibe o menu principal.
     */
    private static void exibirMenu() {
        String statusNuvem = cloudService.isConectado() ? "[ONLINE]" : 
                            (cloudService.isConfigurado() ? "[OFFLINE]" : "[NAO CONFIG]");
        
        System.out.println("\n╔════════════ MENU " + statusNuvem + " ════════════╗");
        System.out.println("║  1 - Adicionar moeda                    ║");
        System.out.println("║  2 - Remover moeda                      ║");
        System.out.println("║  3 - Listar moedas                      ║");
        System.out.println("║  4 - Total convertido para Real         ║");
        System.out.println("║  5 - Ver cotacoes atuais                ║");
        System.out.println("║  6 - Atualizar cotacoes (API)           ║");
        System.out.println("║  7 - Sincronizar com nuvem              ║");
        System.out.println("║  8 - Configurar nuvem (JSONBin.io)      ║");
        System.out.println("║  9 - Status da nuvem                    ║");
        System.out.println("║  0 - Sair                               ║");
        System.out.println("╚═════════════════════════════════════════╝");
        System.out.print("Escolha uma opcao: ");
    }
    
    /**
     * Exibe as cotacoes atuais.
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
     * Adiciona uma moeda e sincroniza com a nuvem.
     */
    private static void adicionarMoeda() {
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
        
        Moeda moeda = null;
        switch (tipoMoeda) {
            case 1: moeda = new Real(valor); break;
            case 2: moeda = new Dolar(valor); break;
            case 3: moeda = new Euro(valor); break;
        }
        
        if (moeda != null) {
            cofrinho.adicionar(moeda);
            
            // Sincroniza automaticamente com a nuvem
            if (cloudService.isConfigurado()) {
                System.out.print("Sincronizando com a nuvem... ");
                if (cloudService.salvarNaNuvem(cofrinho.getListaMoedas())) {
                    System.out.println("OK!");
                }
            }
        }
    }
    
    /**
     * Remove uma moeda e sincroniza com a nuvem.
     */
    private static void removerMoeda() {
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
        
        Moeda moeda = null;
        switch (tipoMoeda) {
            case 1: moeda = new Real(valor); break;
            case 2: moeda = new Dolar(valor); break;
            case 3: moeda = new Euro(valor); break;
        }
        
        if (moeda != null) {
            boolean removido = cofrinho.remover(moeda);
            
            // Sincroniza automaticamente com a nuvem se removeu
            if (removido && cloudService.isConfigurado()) {
                System.out.print("Sincronizando com a nuvem... ");
                if (cloudService.salvarNaNuvem(cofrinho.getListaMoedas())) {
                    System.out.println("OK!");
                }
            }
        }
    }
    
    /**
     * Forca sincronizacao manual com a nuvem.
     */
    private static void sincronizarNuvem() {
        if (!cloudService.isConfigurado()) {
            System.out.println("\nNuvem nao configurada! Use opcao 8 primeiro.");
            return;
        }
        
        System.out.println("\nO que deseja fazer?");
        System.out.println("1 - Enviar dados locais para a nuvem");
        System.out.println("2 - Baixar dados da nuvem");
        System.out.print("Opcao: ");
        
        int opcao = teclado.nextInt();
        
        if (opcao == 1) {
            System.out.print("Enviando para a nuvem... ");
            if (cloudService.salvarNaNuvem(cofrinho.getListaMoedas())) {
                System.out.println("Sincronizado com sucesso!");
            }
        } else if (opcao == 2) {
            System.out.print("Baixando da nuvem... ");
            ArrayList<Moeda> moedasNuvem = cloudService.carregarDaNuvem();
            if (moedasNuvem != null) {
                cofrinho.carregarMoedas(moedasNuvem);
                System.out.println("Sincronizado! " + moedasNuvem.size() + " moeda(s) carregadas.");
            }
        }
    }
    
    /**
     * Configura a conexao com JSONBin.io.
     */
    private static void configurarNuvem() {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║     CONFIGURACAO DO JSONBIN.IO            ║");
        System.out.println("╠═══════════════════════════════════════════╣");
        System.out.println("║ 1. Acesse: https://jsonbin.io             ║");
        System.out.println("║ 2. Crie uma conta gratuita                ║");
        System.out.println("║ 3. Va em 'API Keys' no dashboard          ║");
        System.out.println("║ 4. Copie sua 'X-Master-Key'               ║");
        System.out.println("╚═══════════════════════════════════════════╝");
        
        System.out.println("\nOpcoes:");
        System.out.println("1 - Inserir nova API Key");
        System.out.println("2 - Limpar configuracao");
        System.out.println("0 - Voltar");
        System.out.print("Opcao: ");
        
        int opcao = teclado.nextInt();
        teclado.nextLine(); // Limpa buffer
        
        if (opcao == 1) {
            System.out.print("\nCole sua API Key (X-Master-Key): ");
            String apiKey = teclado.nextLine().trim();
            
            if (apiKey.isEmpty()) {
                System.out.println("API Key invalida!");
                return;
            }
            
            cloudService.configurarApiKey(apiKey);
            
            System.out.print("Testando conexao... ");
            if (cloudService.testarConexao()) {
                System.out.println("Conectado com sucesso!");
            } else {
                System.out.println("Falha na conexao. Verifique a API Key.");
            }
            
        } else if (opcao == 2) {
            cloudService.limparConfiguracao();
        }
    }
    
    /**
     * Exibe status detalhado da conexao com a nuvem.
     */
    private static void exibirStatusNuvem() {
        System.out.println("\n----- STATUS DA NUVEM -----");
        System.out.println("Servico: JSONBin.io");
        System.out.println("Configurado: " + (cloudService.isConfigurado() ? "Sim" : "Nao"));
        System.out.println("Conectado: " + (cloudService.isConectado() ? "Sim" : "Nao"));
        
        if (cloudService.getBinId() != null) {
            System.out.println("Bin ID: " + cloudService.getBinId());
        }
        
        System.out.println("Moedas locais: " + cofrinho.getQuantidadeMoedas());
        System.out.println("---------------------------");
    }
    
    /**
     * Encerra o programa salvando na nuvem.
     */
    private static void encerrarPrograma() {
        System.out.println();
        
        // Salva na nuvem antes de sair
        if (cloudService.isConfigurado() && cofrinho.getQuantidadeMoedas() > 0) {
            System.out.print("Salvando na nuvem antes de sair... ");
            if (cloudService.salvarNaNuvem(cofrinho.getListaMoedas())) {
                System.out.println("OK!");
            }
        }
        
        System.out.println("\nObrigado por usar o Cofrinho!");
        System.out.printf("Voce possui R$ %.2f em moedas.%n", cofrinho.totalConvertido());
        
        if (cloudService.isConfigurado()) {
            System.out.println("Seus dados estao salvos na nuvem!");
        }
        
        System.out.println("Ate a proxima!");
    }
}

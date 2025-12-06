package cofrinho;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Servico responsavel por buscar cotacoes de moedas em tempo real.
 * Utiliza a API gratuita AwesomeAPI (https://economia.awesomeapi.com.br)
 * 
 * Essa API e publica, gratuita e nao requer autenticacao.
 * Retorna cotacoes atualizadas do Dolar e Euro em relacao ao Real.
 * 
 * @author Celio Marcos
 */
public class CotacaoService {
    
    // URL base da API de cotacoes
    private static final String API_URL = "https://economia.awesomeapi.com.br/json/last/USD-BRL,EUR-BRL";
    
    // Taxas padrao caso a API falhe (fallback)
    private static final double TAXA_DOLAR_PADRAO = 5.50;
    private static final double TAXA_EURO_PADRAO = 6.00;
    
    // Taxas carregadas da API
    private double taxaDolar;
    private double taxaEuro;
    
    // Indica se as cotacoes foram carregadas com sucesso
    private boolean cotacoesCarregadas;
    
    /**
     * Construtor que tenta carregar as cotacoes da API.
     * Se falhar, usa valores padrao.
     */
    public CotacaoService() {
        this.cotacoesCarregadas = false;
        carregarCotacoes();
    }
    
    /**
     * Carrega as cotacoes da API AwesomeAPI.
     * Faz uma requisicao HTTP GET e parseia o JSON de resposta.
     */
    public void carregarCotacoes() {
        try {
            // Cria conexao HTTP
            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000); // 5 segundos timeout
            connection.setReadTimeout(5000);
            
            int responseCode = connection.getResponseCode();
            
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Le a resposta
                BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream())
                );
                StringBuilder response = new StringBuilder();
                String linha;
                
                while ((linha = reader.readLine()) != null) {
                    response.append(linha);
                }
                reader.close();
                
                // Parseia o JSON manualmente (sem bibliotecas externas)
                String json = response.toString();
                
                // Extrai cotacao do Dolar (bid = preco de compra)
                taxaDolar = extrairCotacao(json, "USDBRL");
                
                // Extrai cotacao do Euro
                taxaEuro = extrairCotacao(json, "EURBRL");
                
                cotacoesCarregadas = true;
                
                // Atualiza as taxas nas classes de moeda
                Dolar.setTaxaConversao(taxaDolar);
                Euro.setTaxaConversao(taxaEuro);
                
                System.out.println("Cotacoes carregadas com sucesso da API!");
                System.out.printf("Dolar: R$ %.4f | Euro: R$ %.4f%n", taxaDolar, taxaEuro);
                
            } else {
                throw new Exception("HTTP Error: " + responseCode);
            }
            
            connection.disconnect();
            
        } catch (Exception e) {
            // Em caso de erro, usa valores padrao
            System.out.println("Aviso: Nao foi possivel carregar cotacoes da API.");
            System.out.println("Motivo: " + e.getMessage());
            System.out.println("Usando valores padrao...");
            
            taxaDolar = TAXA_DOLAR_PADRAO;
            taxaEuro = TAXA_EURO_PADRAO;
            cotacoesCarregadas = false;
        }
    }
    
    /**
     * Extrai o valor "bid" (cotacao de compra) do JSON para uma moeda especifica.
     * Faz parsing manual do JSON sem dependencias externas.
     * 
     * @param json String JSON completa da resposta
     * @param moeda Codigo da moeda (ex: "USDBRL", "EURBRL")
     * @return valor da cotacao
     */
    private double extrairCotacao(String json, String moeda) {
        try {
            // Encontra a posicao da moeda no JSON
            int inicioMoeda = json.indexOf("\"" + moeda + "\"");
            if (inicioMoeda == -1) {
                throw new Exception("Moeda nao encontrada: " + moeda);
            }
            
            // Encontra o campo "bid" dentro do bloco da moeda
            int inicioBid = json.indexOf("\"bid\"", inicioMoeda);
            if (inicioBid == -1) {
                throw new Exception("Campo bid nao encontrado para: " + moeda);
            }
            
            // Encontra o valor entre aspas apos "bid":"
            int inicioValor = json.indexOf("\"", inicioBid + 5) + 1;
            int fimValor = json.indexOf("\"", inicioValor);
            
            String valorStr = json.substring(inicioValor, fimValor);
            return Double.parseDouble(valorStr);
            
        } catch (Exception e) {
            System.out.println("Erro ao parsear cotacao de " + moeda + ": " + e.getMessage());
            return moeda.equals("USDBRL") ? TAXA_DOLAR_PADRAO : TAXA_EURO_PADRAO;
        }
    }
    
    /**
     * Retorna a taxa de conversao do Dolar para Real.
     * @return taxa USD -> BRL
     */
    public double getTaxaDolar() {
        return taxaDolar;
    }
    
    /**
     * Retorna a taxa de conversao do Euro para Real.
     * @return taxa EUR -> BRL
     */
    public double getTaxaEuro() {
        return taxaEuro;
    }
    
    /**
     * Verifica se as cotacoes foram carregadas da API com sucesso.
     * @return true se carregou da API, false se usando valores padrao
     */
    public boolean isCotacoesCarregadas() {
        return cotacoesCarregadas;
    }
    
    /**
     * Recarrega as cotacoes da API.
     * Util para atualizar valores durante execucao do programa.
     */
    public void atualizarCotacoes() {
        System.out.println("\nAtualizando cotacoes...");
        carregarCotacoes();
    }
}

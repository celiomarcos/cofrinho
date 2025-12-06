package cofrinho;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Servico de armazenamento em nuvem usando JSONBin.io
 * 
 * Permite salvar e recuperar as moedas do cofrinho na nuvem,
 * garantindo que os dados persistam entre execucoes do programa.
 * 
 * Para usar este servico, voce precisa:
 * 1. Criar uma conta gratuita em https://jsonbin.io
 * 2. Copiar sua API Key (X-Master-Key) do dashboard
 * 3. Configurar no arquivo config.txt ou via menu
 * 
 * @author Celio Marcos
 */
public class CloudStorageService {
    
    // URL base da API JSONBin.io
    private static final String API_BASE_URL = "https://api.jsonbin.io/v3/b";
    
    // Arquivo local para guardar configuracoes (API Key e Bin ID)
    private static final String CONFIG_FILE = "cofrinho_config.txt";
    
    // Credenciais da API
    private String apiKey;
    private String binId;
    
    // Status da conexao
    private boolean configurado;
    private boolean conectado;
    
    /**
     * Construtor que carrega configuracoes do arquivo local.
     */
    public CloudStorageService() {
        this.configurado = false;
        this.conectado = false;
        carregarConfiguracao();
    }
    
    /**
     * Carrega API Key e Bin ID do arquivo de configuracao local.
     */
    private void carregarConfiguracao() {
        File configFile = new File(CONFIG_FILE);
        
        if (configFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(configFile))) {
                apiKey = reader.readLine();
                binId = reader.readLine();
                
                if (apiKey != null && !apiKey.trim().isEmpty()) {
                    configurado = true;
                    System.out.println("Configuracao de nuvem carregada com sucesso!");
                }
            } catch (Exception e) {
                System.out.println("Erro ao carregar configuracao: " + e.getMessage());
            }
        }
    }
    
    /**
     * Salva API Key e Bin ID no arquivo de configuracao local.
     */
    private void salvarConfiguracao() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            writer.write(apiKey + "\n");
            writer.write(binId != null ? binId : "");
            System.out.println("Configuracao salva com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao salvar configuracao: " + e.getMessage());
        }
    }
    
    /**
     * Configura a API Key do JSONBin.io
     * @param apiKey Chave da API obtida em jsonbin.io/api-keys
     */
    public void configurarApiKey(String apiKey) {
        this.apiKey = apiKey.trim();
        this.configurado = true;
        salvarConfiguracao();
    }
    
    /**
     * Verifica se o servico esta configurado com API Key.
     */
    public boolean isConfigurado() {
        return configurado;
    }
    
    /**
     * Verifica se conseguiu conectar na nuvem.
     */
    public boolean isConectado() {
        return conectado;
    }
    
    /**
     * Retorna o Bin ID atual (para exibicao).
     */
    public String getBinId() {
        return binId;
    }
    
    /**
     * Converte lista de moedas para formato JSON.
     * Formato: {"moedas":[{"tipo":"Real","valor":10.0},{"tipo":"Dolar","valor":5.0},...]}
     */
    private String moedasParaJson(ArrayList<Moeda> moedas) {
        StringBuilder json = new StringBuilder();
        json.append("{\"moedas\":[");
        
        for (int i = 0; i < moedas.size(); i++) {
            Moeda m = moedas.get(i);
            String tipo = m.getClass().getSimpleName(); // Real, Dolar ou Euro
            
            json.append("{\"tipo\":\"").append(tipo).append("\",");
            json.append("\"valor\":").append(m.getValor()).append("}");
            
            if (i < moedas.size() - 1) {
                json.append(",");
            }
        }
        
        json.append("]}");
        return json.toString();
    }
    
    /**
     * Converte JSON para lista de moedas.
     * Faz parsing manual para evitar dependencias externas.
     */
    private ArrayList<Moeda> jsonParaMoedas(String json) {
        ArrayList<Moeda> moedas = new ArrayList<>();
        
        try {
            // Encontra o array de moedas
            int inicioArray = json.indexOf("[");
            int fimArray = json.lastIndexOf("]");
            
            if (inicioArray == -1 || fimArray == -1) {
                return moedas;
            }
            
            String arrayContent = json.substring(inicioArray + 1, fimArray);
            
            // Divide por objetos (cada moeda)
            int pos = 0;
            while (pos < arrayContent.length()) {
                int inicioObj = arrayContent.indexOf("{", pos);
                int fimObj = arrayContent.indexOf("}", pos);
                
                if (inicioObj == -1 || fimObj == -1) break;
                
                String objeto = arrayContent.substring(inicioObj, fimObj + 1);
                
                // Extrai tipo
                String tipo = extrairValorString(objeto, "tipo");
                // Extrai valor
                double valor = extrairValorNumerico(objeto, "valor");
                
                // Cria a moeda do tipo correto
                Moeda moeda = null;
                if ("Real".equals(tipo)) {
                    moeda = new Real(valor);
                } else if ("Dolar".equals(tipo)) {
                    moeda = new Dolar(valor);
                } else if ("Euro".equals(tipo)) {
                    moeda = new Euro(valor);
                }
                
                if (moeda != null) {
                    moedas.add(moeda);
                }
                
                pos = fimObj + 1;
            }
            
        } catch (Exception e) {
            System.out.println("Erro ao parsear JSON: " + e.getMessage());
        }
        
        return moedas;
    }
    
    /**
     * Extrai valor string de um campo JSON.
     */
    private String extrairValorString(String json, String campo) {
        String busca = "\"" + campo + "\":\"";
        int inicio = json.indexOf(busca);
        if (inicio == -1) return "";
        
        inicio += busca.length();
        int fim = json.indexOf("\"", inicio);
        return json.substring(inicio, fim);
    }
    
    /**
     * Extrai valor numerico de um campo JSON.
     */
    private double extrairValorNumerico(String json, String campo) {
        String busca = "\"" + campo + "\":";
        int inicio = json.indexOf(busca);
        if (inicio == -1) return 0.0;
        
        inicio += busca.length();
        int fim = inicio;
        while (fim < json.length() && (Character.isDigit(json.charAt(fim)) || json.charAt(fim) == '.')) {
            fim++;
        }
        
        try {
            return Double.parseDouble(json.substring(inicio, fim));
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
    
    /**
     * Salva as moedas na nuvem (JSONBin.io).
     * Se ainda nao existe um Bin, cria um novo.
     * Se ja existe, atualiza o existente.
     * 
     * @param moedas Lista de moedas para salvar
     * @return true se salvou com sucesso
     */
    public boolean salvarNaNuvem(ArrayList<Moeda> moedas) {
        if (!configurado) {
            System.out.println("Servico de nuvem nao configurado. Use a opcao do menu para configurar.");
            return false;
        }
        
        String jsonData = moedasParaJson(moedas);
        
        try {
            HttpURLConnection connection;
            
            if (binId == null || binId.trim().isEmpty()) {
                // Criar novo Bin (POST)
                connection = criarConexao(API_BASE_URL, "POST");
            } else {
                // Atualizar Bin existente (PUT)
                connection = criarConexao(API_BASE_URL + "/" + binId, "PUT");
            }
            
            // Envia os dados
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonData.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            
            int responseCode = connection.getResponseCode();
            
            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                // Le a resposta para obter o Bin ID (no caso de criacao)
                BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream())
                );
                StringBuilder response = new StringBuilder();
                String linha;
                while ((linha = reader.readLine()) != null) {
                    response.append(linha);
                }
                reader.close();
                
                // Se criou novo bin, extrai o ID
                if (binId == null || binId.trim().isEmpty()) {
                    String resposta = response.toString();
                    binId = extrairBinId(resposta);
                    salvarConfiguracao();
                    System.out.println("Novo Bin criado na nuvem! ID: " + binId);
                }
                
                conectado = true;
                return true;
                
            } else {
                System.out.println("Erro ao salvar na nuvem. Codigo HTTP: " + responseCode);
                lerErro(connection);
                return false;
            }
            
        } catch (Exception e) {
            System.out.println("Erro de conexao: " + e.getMessage());
            conectado = false;
            return false;
        }
    }
    
    /**
     * Carrega as moedas da nuvem.
     * 
     * @return Lista de moedas ou null se falhar
     */
    public ArrayList<Moeda> carregarDaNuvem() {
        if (!configurado) {
            return null;
        }
        
        if (binId == null || binId.trim().isEmpty()) {
            System.out.println("Nenhum Bin encontrado. Iniciando cofrinho vazio.");
            return new ArrayList<>();
        }
        
        try {
            URL url = new URL(API_BASE_URL + "/" + binId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("X-Master-Key", apiKey);
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            
            int responseCode = connection.getResponseCode();
            
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream())
                );
                StringBuilder response = new StringBuilder();
                String linha;
                while ((linha = reader.readLine()) != null) {
                    response.append(linha);
                }
                reader.close();
                
                conectado = true;
                
                // O JSONBin retorna: {"record":{...}, "metadata":{...}}
                // Precisamos extrair o conteudo de "record"
                String resposta = response.toString();
                int inicioRecord = resposta.indexOf("\"record\":");
                if (inicioRecord != -1) {
                    int inicioConteudo = resposta.indexOf("{", inicioRecord + 9);
                    int fimConteudo = encontrarFimObjeto(resposta, inicioConteudo);
                    String recordJson = resposta.substring(inicioConteudo, fimConteudo + 1);
                    
                    ArrayList<Moeda> moedas = jsonParaMoedas(recordJson);
                    System.out.println("Carregadas " + moedas.size() + " moeda(s) da nuvem!");
                    return moedas;
                }
                
                return new ArrayList<>();
                
            } else if (responseCode == 404) {
                System.out.println("Bin nao encontrado. Iniciando cofrinho vazio.");
                binId = null;
                salvarConfiguracao();
                return new ArrayList<>();
            } else {
                System.out.println("Erro ao carregar da nuvem. Codigo: " + responseCode);
                lerErro(connection);
                return null;
            }
            
        } catch (Exception e) {
            System.out.println("Erro de conexao: " + e.getMessage());
            conectado = false;
            return null;
        }
    }
    
    /**
     * Encontra o fim de um objeto JSON (lida com objetos aninhados).
     */
    private int encontrarFimObjeto(String json, int inicio) {
        int nivel = 0;
        for (int i = inicio; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == '{') nivel++;
            else if (c == '}') {
                nivel--;
                if (nivel == 0) return i;
            }
        }
        return json.length() - 1;
    }
    
    /**
     * Extrai o Bin ID da resposta de criacao.
     */
    private String extrairBinId(String json) {
        // Formato: {"record":...,"metadata":{"id":"xxx",...}}
        String busca = "\"id\":\"";
        int inicio = json.indexOf(busca);
        if (inicio == -1) return null;
        
        inicio += busca.length();
        int fim = json.indexOf("\"", inicio);
        return json.substring(inicio, fim);
    }
    
    /**
     * Cria conexao HTTP com headers necessarios.
     */
    private HttpURLConnection criarConexao(String urlStr, String metodo) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(metodo);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("X-Master-Key", apiKey);
        connection.setDoOutput(true);
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(10000);
        return connection;
    }
    
    /**
     * Le mensagem de erro da API.
     */
    private void lerErro(HttpURLConnection connection) {
        try {
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getErrorStream())
            );
            StringBuilder erro = new StringBuilder();
            String linha;
            while ((linha = reader.readLine()) != null) {
                erro.append(linha);
            }
            reader.close();
            System.out.println("Detalhes: " + erro.toString());
        } catch (Exception e) {
            // Ignora erros ao ler erro
        }
    }
    
    /**
     * Testa a conexao com a API.
     * @return true se conectou com sucesso
     */
    public boolean testarConexao() {
        if (!configurado) {
            return false;
        }
        
        try {
            // Tenta carregar dados (mesmo que vazio)
            ArrayList<Moeda> teste = carregarDaNuvem();
            return teste != null;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Limpa as configuracoes (remove arquivo local).
     */
    public void limparConfiguracao() {
        File configFile = new File(CONFIG_FILE);
        if (configFile.exists()) {
            configFile.delete();
        }
        apiKey = null;
        binId = null;
        configurado = false;
        conectado = false;
        System.out.println("Configuracoes de nuvem removidas.");
    }
}

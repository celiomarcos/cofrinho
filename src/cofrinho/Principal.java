package cofrinho;

import java.util.Scanner;

public class Principal {

    public static void main(String[] args) {
        
        Scanner teclado = new Scanner(System.in);
        int opcao;
        
        Cofrinho cofrinho = new Cofrinho();
        
        int tipoMoeda;
        double valor;
        Moeda moeda;
        
        // Loop infinito para escrever o codigo de menu apenas uma vez, e o "break" cuida de sair.
        while(true) {
            
            System.out.println("\n--== COFRINHO ==--");
            System.out.println("1-Adicionar Moeda");
            System.out.println("2-Remover Moeda");
            System.out.println("3-Listar Moedas");
            System.out.println("4-Calcular total convertido para Real");
            System.out.println("0-Encerrar");
            System.out.print("Selecione: ");
            opcao = teclado.nextInt();
            
            if (opcao == 0) {
                System.out.println("Sistema encerrado.");
                // Fora do switch pois no switch o break encerra apenas no switch onde esta rodando
                break;
            }
            
            switch(opcao) {
            
            case 1:
                tipoMoeda = 0;
                while(tipoMoeda > 3 || tipoMoeda <= 0) {
                    System.out.println("Escolha a Moeda:");
                    System.out.println("1-Real");
                    System.out.println("2-Dolar");
                    System.out.println("3-Euro");
                    tipoMoeda = teclado.nextInt();
                }
                
                System.out.println("Digite o valor:");
                String valorText = teclado.next().replace(",", ".");
                valor = Double.parseDouble(valorText);
                
                moeda = null;
                if(tipoMoeda == 1) {
                    moeda = new Real(valor);
                }
                else if(tipoMoeda == 2) {
                    moeda = new Dolar(valor);
                }
                else if(tipoMoeda == 3) {
                    moeda = new Euro(valor);
                }
                
                cofrinho.adicionar(moeda);
                System.out.println("Moeda adicionada!");
                break;
                
            case 2:
                tipoMoeda = 0;
                while(tipoMoeda > 3 || tipoMoeda <= 0) {
                    System.out.println("Escolha a Moeda para remover:");
                    System.out.println("1-Real");
                    System.out.println("2-Dolar");
                    System.out.println("3-Euro");
                    tipoMoeda = teclado.nextInt();
                }
                
                System.out.println("Digite o valor:");
                String valorTextRemove = teclado.next().replace(",", ".");
                valor = Double.parseDouble(valorTextRemove);
                
                moeda = null;
                if(tipoMoeda == 1) {
                    moeda = new Real(valor);
                }
                else if(tipoMoeda == 2) {
                    moeda = new Dolar(valor);
                }
                else if(tipoMoeda == 3) {
                    moeda = new Euro(valor);
                }
                
                cofrinho.remover(moeda);
                break;
                
            case 3:
                cofrinho.listagemMoedas();
                break;
                
            case 4:
                double total = cofrinho.totalConvertido();
                System.out.printf("Total convertido para Real: R$ %.2f\n", total);
                break;
                
            default:
                System.out.println("Opcao invalida!");                
            }
        }
        
        // 
        teclado.close();
    }
}

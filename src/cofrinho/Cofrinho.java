package cofrinho;

import java.util.ArrayList;

public class Cofrinho {
    
    private ArrayList<Moeda> listaMoedas = new ArrayList<Moeda>();

    public void adicionar(Moeda m) {
        listaMoedas.add(m);
    }

    public void remover(Moeda m) {
        listaMoedas.remove(m);
    }

    public void listagemMoedas() {
        // Verifica se está vazio e avisa usuario
        if (this.listaMoedas.isEmpty()) {
            System.out.println("O cofrinho está vazio!");
            return;
        }

        for (Moeda m : listaMoedas) {
            m.info();
        }
    }

    public double totalConvertido() {
        if (this.listaMoedas.isEmpty()) {
            return 0;
        }

        double total = 0;
        for (Moeda m : listaMoedas) {
            total += m.converter();
        }
        return total;
    }
}
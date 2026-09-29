import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {

    static String nomeArquivoIngressos = "ingressos.txt";
    static Scanner teclado = new Scanner(System.in);
    static Ingresso[] ingressos;

    static void limparTela() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    static void cabecalho() {
        limparTela();
        System.out.println("AEDs II - SISTEMA DE INGRESSOS");
        System.out.println("=======================================");
    }

    static void pausa() {
        System.out.println("\nDigite enter para continuar...");
        teclado.nextLine();
    }

    static int menu() {
        cabecalho();
        System.out.println("ORDENAÇÃO");
        System.out.println("=======================================");
        System.out.println("1 - Ingressos por valor crescente");
        System.out.println("2 - Ingressos por evento (A-Z)");
        System.out.println("3 - Demonstrar Bubble Sort por valor");
        System.out.println("4 - Demonstrar Merge Sort por valor");
        System.out.println("BUSCA");
        System.out.println("=======================================");
        System.out.println("5 - Localizar ingressos por faixa de valor");
        System.out.println("\n0 - Sair");
        System.out.print("Digite sua opção: ");

        try {
            return Integer.parseInt(teclado.nextLine());
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    /**
     * Lê o arquivo de ingressos no formato:
     * quantidade
     * id;evento;valor
     */
    static Ingresso[] lerIngressos(String nomeArquivo) {
        try (Scanner arquivo = new Scanner(new File(nomeArquivo), Charset.forName("UTF-8"))) {
            int quantidade = Integer.parseInt(arquivo.nextLine());
            Ingresso[] dados = new Ingresso[quantidade];

            for (int i = 0; i < quantidade; i++) {
                String[] partes = arquivo.nextLine().split(";");
                int id = Integer.parseInt(partes[0]);
                String evento = partes[1];
                double valor = Double.parseDouble(partes[2].replace(",", "."));
                dados[i] = new Ingresso(id, evento, valor);
            }
            return dados;
        } catch (IOException | RuntimeException ex) {
            System.out.println("Não foi possível carregar o arquivo de ingressos: " + ex.getMessage());
            return new Ingresso[0];
        }
    }

    static Ingresso[] copiarIngressos(Ingresso[] origem) {
        Ingresso[] copia = new Ingresso[origem.length];
        for (int i = 0; i < origem.length; i++) {
            copia[i] = origem[i];
        }
        return copia;
    }

    static void imprimirIngressos(Ingresso[] dados) {
        if (dados.length == 0) {
            System.out.println("Nenhum ingresso encontrado.");
            return;
        }

        for (Ingresso ingresso : dados) {
            System.out.println(ingresso);
        }
    }

    // ============================================================
    // TAREFA 1 - ORDENAR POR VALOR E POR EVENTO
    // ============================================================

    /**
     * Retorna uma cópia dos ingressos ordenada por valor crescente.
     * A lista original não é alterada.
     */
    static Ingresso[] ordenarPorValor(Ingresso[] dados) {
        Ingresso[] copia = copiarIngressos(dados);
        bubbleSortPorValor(copia);
        return copia;
    }

    /**
     * Retorna uma cópia dos ingressos em ordem alfabética do evento.
     * A lista original não é alterada.
     */
    static Ingresso[] ordenarPorEvento(Ingresso[] dados) {
        Ingresso[] copia = copiarIngressos(dados);
        bubbleSortPorEvento(copia);
        return copia;
    }

    // ============================================================
    // TAREFA 2 - BUBBLE SORT
    // ============================================================

    /** Ordena os ingressos pelo valor, do menor para o maior, usando Bubble Sort. */
    static void bubbleSortPorValor(Ingresso[] dados) {
        for (int i = 0; i < dados.length - 1; i++) {
            boolean trocou = false;

            for (int j = 0; j < dados.length - 1 - i; j++) {
                if (dados[j].getValor() > dados[j + 1].getValor()) {
                    Ingresso aux = dados[j];
                    dados[j] = dados[j + 1];
                    dados[j + 1] = aux;
                    trocou = true;
                }
            }

            if (!trocou)
                break;
        }
    }

    /** Ordena os ingressos pelo nome do evento (A-Z), usando Bubble Sort. */
    static void bubbleSortPorEvento(Ingresso[] dados) {
        for (int i = 0; i < dados.length - 1; i++) {
            boolean trocou = false;

            for (int j = 0; j < dados.length - 1 - i; j++) {
                if (dados[j].getEvento().compareToIgnoreCase(dados[j + 1].getEvento()) > 0) {
                    Ingresso aux = dados[j];
                    dados[j] = dados[j + 1];
                    dados[j + 1] = aux;
                    trocou = true;
                }
            }

            if (!trocou)
                break;
        }
    }

    // ============================================================
    // TAREFA 2 - MERGE SORT
    // ============================================================

    /** Ordena os ingressos pelo valor, do menor para o maior, usando Merge Sort. */
    static void mergeSortPorValor(Ingresso[] dados) {
        if (dados.length > 1)
            mergeSortPorValor(dados, 0, dados.length - 1);
    }

    private static void mergeSortPorValor(Ingresso[] dados, int inicio, int fim) {
        if (inicio < fim) {
            int meio = (inicio + fim) / 2;
            mergeSortPorValor(dados, inicio, meio);
            mergeSortPorValor(dados, meio + 1, fim);
            mergePorValor(dados, inicio, meio, fim);
        }
    }

    private static void mergePorValor(Ingresso[] dados, int inicio, int meio, int fim) {
        Ingresso[] auxiliar = new Ingresso[fim - inicio + 1];
        int i = inicio;
        int j = meio + 1;
        int k = 0;

        while (i <= meio && j <= fim) {
            if (dados[i].getValor() <= dados[j].getValor()) {
                auxiliar[k++] = dados[i++];
            } else {
                auxiliar[k++] = dados[j++];
            }
        }

        while (i <= meio)
            auxiliar[k++] = dados[i++];

        while (j <= fim)
            auxiliar[k++] = dados[j++];

        for (int x = 0; x < auxiliar.length; x++) {
            dados[inicio + x] = auxiliar[x];
        }
    }

    // ============================================================
    // TAREFA 3 - LOCALIZAR INGRESSOS POR FAIXA DE VALOR
    // ============================================================

    /**
     * Localiza todos os ingressos com valor entre minimo e maximo,
     * incluindo os limites, e os retorna em ordem crescente de valor.
     */
    static Ingresso[] localizarPorValor(Ingresso[] dados, double minimo, double maximo) {
        if (minimo > maximo)
            throw new IllegalArgumentException("O valor mínimo não pode ser maior que o valor máximo.");

        List<Ingresso> encontrados = new ArrayList<>();

        for (Ingresso ingresso : dados) {
            if (ingresso.getValor() >= minimo && ingresso.getValor() <= maximo) {
                encontrados.add(ingresso);
            }
        }

        Ingresso[] resultado = encontrados.toArray(new Ingresso[0]);

        // A questão pede que os resultados sejam impressos em ordem crescente.
        // Por isso, reaproveitamos o Merge Sort implementado na Tarefa 2.
        mergeSortPorValor(resultado);

        return resultado;
    }

    static void relatorioPorFaixa() {
        cabecalho();
        System.out.println("LOCALIZAR INGRESSOS POR FAIXA DE VALOR");
        System.out.println("=======================================");

        try {
            System.out.print("Digite o valor mínimo: R$ ");
            double minimo = Double.parseDouble(teclado.nextLine().replace(",", "."));

            System.out.print("Digite o valor máximo: R$ ");
            double maximo = Double.parseDouble(teclado.nextLine().replace(",", "."));

            Ingresso[] encontrados = localizarPorValor(ingressos, minimo, maximo);

            System.out.printf("%nIngressos entre R$ %.2f e R$ %.2f:%n", minimo, maximo);
            System.out.println("=======================================");

            if (encontrados.length == 0) {
                System.out.printf("Nenhum ingresso encontrado entre R$ %.2f e R$ %.2f.%n", minimo, maximo);
            } else {
                imprimirIngressos(encontrados);
            }
        } catch (NumberFormatException ex) {
            System.out.println("Valor inválido.");
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        }
    }

    static void configurarSistema() {
        ingressos = lerIngressos(nomeArquivoIngressos);
    }

    public static void main(String[] args) {
        configurarSistema();

        int opcao;
        do {
            opcao = menu();

            switch (opcao) {
                case 1:
                    cabecalho();
                    System.out.println("INGRESSOS ORDENADOS POR VALOR");
                    System.out.println("=======================================");
                    imprimirIngressos(ordenarPorValor(ingressos));
                    pausa();
                    break;

                case 2:
                    cabecalho();
                    System.out.println("INGRESSOS ORDENADOS POR EVENTO");
                    System.out.println("=======================================");
                    imprimirIngressos(ordenarPorEvento(ingressos));
                    pausa();
                    break;

                case 3:
                    cabecalho();
                    System.out.println("BUBBLE SORT - VALOR CRESCENTE");
                    System.out.println("=======================================");
                    Ingresso[] bubble = copiarIngressos(ingressos);
                    bubbleSortPorValor(bubble);
                    imprimirIngressos(bubble);
                    pausa();
                    break;

                case 4:
                    cabecalho();
                    System.out.println("MERGE SORT - VALOR CRESCENTE");
                    System.out.println("=======================================");
                    Ingresso[] merge = copiarIngressos(ingressos);
                    mergeSortPorValor(merge);
                    imprimirIngressos(merge);
                    pausa();
                    break;

                case 5:
                    relatorioPorFaixa();
                    pausa();
                    break;

                case 0:
                    System.out.println("Sistema encerrado.");
                    break;

                default:
                    System.out.println("Opção inválida.");
                    pausa();
            }
        } while (opcao != 0);

        teclado.close();
    }
}

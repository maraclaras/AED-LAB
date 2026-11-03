import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.NumberFormat;

public class App {

    static String nomeArquivoDados;
    static Scanner teclado;
    static Produto[] produtosCadastrados;
    static int quantosProdutos = 0;

    /** Pedidos mais recentes ficam no topo. */
    static Pilha<Pedido> pilhaPedidos = new Pilha<>();
    /** Pedidos permanecem na ordem em que foram finalizados. */
    static Fila<Pedido> filaPedidos = new Fila<>();
    /** Lista auxiliar com todos os pedidos finalizados. */
    static Lista<Pedido> listaPedidos = new Lista<>();

    static void limparTela() {
        System.out.print("\\033[H\\033[2J");
        System.out.flush();
    }

    static void pausa() {
        System.out.println("Digite enter para continuar...");
        teclado.nextLine();
    }

    static void cabecalho() {
        System.out.println("AEDs II COMÉRCIO DE COISINHAS");
        System.out.println("=============================");
    }

    static <T extends Number> T lerOpcao(String mensagem, Class<T> classe) {
        System.out.println(mensagem);
        try {
            return classe.getConstructor(String.class).newInstance(teclado.nextLine());
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            System.out.println("Valor inválido.");
            return null;
        }
    }

    static int menu() {
        cabecalho();
        System.out.println("1 - Listar todos os produtos");
        System.out.println("2 - Procurar por um produto, por código");
        System.out.println("3 - Procurar por um produto, por nome");
        System.out.println("4 - Iniciar novo pedido");
        System.out.println("5 - Fechar pedido");
        System.out.println("6 - Listar produtos dos pedidos mais recentes");
        System.out.println("7 - Exibir o valor total médio dos N primeiros pedidos");
        System.out.println("8 - Exibir os primeiros pedidos com valor total acima de um valor");
        System.out.println("9 - Exibir os primeiros pedidos que apresentam um produto");
        System.out.println("0 - Sair");
        System.out.print("Digite sua opção: ");
        try {
            return Integer.parseInt(teclado.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    static Produto[] lerProdutos(String nomeArquivoDados) {
        Scanner arquivo = null;
        try {
            arquivo = new Scanner(new File(nomeArquivoDados), Charset.forName("UTF-8"));
            int numProdutos = Integer.parseInt(arquivo.nextLine());
            Produto[] resultado = new Produto[numProdutos];
            for (int i = 0; i < numProdutos; i++) {
                resultado[i] = Produto.criarDoTexto(arquivo.nextLine());
            }
            quantosProdutos = numProdutos;
            return resultado;
        } catch (IOException | RuntimeException e) {
            System.out.println("Não foi possível carregar o arquivo de produtos: " + e.getMessage());
            return new Produto[0];
        } finally {
            if (arquivo != null) {
                arquivo.close();
            }
        }
    }

    static Produto localizarProduto() {
        cabecalho();
        Integer idProduto = lerOpcao("Digite o código identificador do produto desejado:", Integer.class);
        if (idProduto == null) return null;
        for (int i = 0; i < quantosProdutos; i++) {
            if (produtosCadastrados[i].hashCode() == idProduto) return produtosCadastrados[i];
        }
        return null;
    }

    static Produto localizarProdutoDescricao() {
        cabecalho();
        System.out.println("Digite o nome ou a descrição do produto desejado:");
        String descricao = teclado.nextLine();
        for (int i = 0; i < quantosProdutos; i++) {
            if (produtosCadastrados[i].descricao.equalsIgnoreCase(descricao)) {
                return produtosCadastrados[i];
            }
        }
        return null;
    }

    private static void mostrarProduto(Produto produto) {
        cabecalho();
        System.out.println(produto == null ? "Produto não encontrado." : "Dados do produto:\\n" + produto);
    }

    static void listarTodosOsProdutos() {
        cabecalho();
        System.out.println("\\nPRODUTOS CADASTRADOS:");
        for (int i = 0; i < quantosProdutos; i++) {
            System.out.println(String.format("%02d - %s", i + 1, produtosCadastrados[i]));
        }
    }

    public static Pedido iniciarPedido() {
        Integer formaPagamento = lerOpcao(
            "Digite a forma de pagamento (1 para à vista e 2 para parcelado):", Integer.class);
        if (formaPagamento == null || (formaPagamento != 1 && formaPagamento != 2)) {
            System.out.println("Forma de pagamento inválida.");
            return null;
        }

        Pedido pedido = new Pedido(LocalDate.now(), formaPagamento);
        listarTodosOsProdutos();
        Integer numProdutos = lerOpcao("Quantos produtos serão incluídos no pedido?", Integer.class);
        if (numProdutos == null || numProdutos < 0) return pedido;

        for (int i = 0; i < numProdutos; i++) {
            Produto produto = localizarProdutoDescricao();
            if (produto == null) {
                System.out.println("Produto não encontrado.");
                i--;
            } else {
                Integer quantidade = lerOpcao("Quantos itens desse produto serão incluídos?", Integer.class);
                if (quantidade != null && quantidade > 0) {
                    pedido.incluirProduto(produto, quantidade);
                }
            }
        }
        return pedido;
    }

    /** Finaliza e armazena o pedido nas três estruturas. */
    public static void finalizarPedido(Pedido pedido) {
        if (pedido == null) {
            System.out.println("Não existe pedido para finalizar.");
            return;
        }
        pilhaPedidos.empilhar(pedido);
        filaPedidos.enfileirar(pedido);
        listaPedidos.inserirFinal(pedido);
        System.out.println("Pedido finalizado e armazenado com sucesso!");
        System.out.println(pedido);
    }

    /** Lista os produtos dos N pedidos mais recentes, sem alterar a pilha original. */
    public static void listarProdutosPedidosRecentes() {
        if (pilhaPedidos.vazia()) {
            System.out.println("Ainda não existem pedidos finalizados.");
            return;
        }
        Integer quantidade = lerOpcao("Quantos pedidos mais recentes deseja consultar?", Integer.class);
        if (quantidade == null || quantidade <= 0) {
            System.out.println("Informe uma quantidade maior que zero.");
            return;
        }

        try {
            Pilha<Pedido> recentes = pilhaPedidos.subPilha(quantidade);
            System.out.println("PRODUTOS DOS PEDIDOS MAIS RECENTES:");
            while (!recentes.vazia()) {
                Pedido pedido = recentes.desempilhar();
                System.out.println(pedido);
                ItemDePedido[] itens = pedido.getItensDoPedido();
                for (int i = 0; i < pedido.getQuantidadeItens(); i++) {
                    System.out.println(" - " + itens[i].getProduto().descricao
                        + " (quantidade: " + itens[i].getQuantidade() + ")");
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void exibirValorMedioPrimeirosPedidos() {
        if (filaPedidos.vazia()) {
            System.out.println("Ainda não existem pedidos finalizados.");
            return;
        }
        Integer quantidade = lerOpcao("Quantos primeiros pedidos devem ser considerados?", Integer.class);
        if (quantidade == null || quantidade <= 0) return;
        try {
            double media = filaPedidos.calcularValorMedio(p -> p.valorFinal(), quantidade);
            System.out.println("Valor médio dos primeiros pedidos: "
                + NumberFormat.getCurrencyInstance().format(media));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void exibirPedidosAcimaDeValor() {
        if (filaPedidos.vazia()) {
            System.out.println("Ainda não existem pedidos finalizados.");
            return;
        }
        Integer quantidade = lerOpcao("Quantos primeiros pedidos devem ser analisados?", Integer.class);
        if (quantidade == null || quantidade <= 0) return;
        Double valor = lerOpcao("Informe o valor mínimo (em reais):", Double.class);
        if (valor == null) return;

        Fila<Pedido> resultado = filaPedidos.filtrar(p -> p.valorFinal() > valor, quantidade);
        imprimirPedidosFiltrados(resultado);
    }

    public static void exibirPedidosComProduto() {
        if (filaPedidos.vazia()) {
            System.out.println("Ainda não existem pedidos finalizados.");
            return;
        }
        Integer quantidade = lerOpcao("Quantos primeiros pedidos devem ser analisados?", Integer.class);
        if (quantidade == null || quantidade <= 0) return;
        System.out.println("Informe a descrição do produto:");
        String descricao = teclado.nextLine();

        Fila<Pedido> resultado = filaPedidos.filtrar(p -> p.possuiProduto(descricao), quantidade);
        imprimirPedidosFiltrados(resultado);
    }

    private static void imprimirPedidosFiltrados(Fila<Pedido> pedidos) {
        if (pedidos.vazia()) {
            System.out.println("Nenhum pedido encontrado com o critério informado.");
            return;
        }
        System.out.println("PEDIDOS ENCONTRADOS:");
        while (!pedidos.vazia()) {
            System.out.println(pedidos.desenfileirar());
        }
    }

    public static void main(String[] args) {
        teclado = new Scanner(System.in, Charset.forName("UTF-8"));
        nomeArquivoDados = "produtos.txt";
        produtosCadastrados = lerProdutos(nomeArquivoDados);

        Pedido pedidoAtual = null;
        int opcao;
        do {
            opcao = menu();
            switch (opcao) {
                case 1 -> listarTodosOsProdutos();
                case 2 -> mostrarProduto(localizarProduto());
                case 3 -> mostrarProduto(localizarProdutoDescricao());
                case 4 -> pedidoAtual = iniciarPedido();
                case 5 -> {
                    finalizarPedido(pedidoAtual);
                    pedidoAtual = null;
                }
                case 6 -> listarProdutosPedidosRecentes();
                case 7 -> exibirValorMedioPrimeirosPedidos();
                case 8 -> exibirPedidosAcimaDeValor();
                case 9 -> exibirPedidosComProduto();
                case 0 -> System.out.println("Encerrando o sistema...");
                default -> System.out.println("Opção inválida.");
            }
            if (opcao != 0) pausa();
        } while (opcao != 0);

        teclado.close();
    }
}

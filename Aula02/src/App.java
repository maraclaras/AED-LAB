import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Scanner;

public class App {

    /** Quantidade máxima de produtos que podem ser armazenados no vetor */
    static final int MAX_NOVOS_PRODUTOS = 10;

    /** Nome do arquivo de dados. O arquivo deve estar localizado na raiz do projeto */
    static String nomeArquivoDados;
    
    /** Scanner para leitura de dados do teclado */
    static Scanner teclado;

    /** Vetor de produtos cadastrados */
    static Produto[] produtosCadastrados;

    /** Quantidade de produtos cadastrados atualmente no vetor */
    static int quantosProdutos = 0;

    /** Gera um efeito de pausa na CLI. Espera por um enter para continuar */
    static void pausa() {
        System.out.println("Digite enter para continuar...");
        teclado.nextLine();
    }

    /** Cabeçalho principal da CLI do sistema */
    static void cabecalho() {
        System.out.println("AEDs II COMÉRCIO DE COISINHAS");
        System.out.println("=============================");
    }
    
    /** Imprime o menu principal, lê a opção do usuário e a retorna (int).
     * @return Um inteiro com a opção do usuário.
    */
    static int menu() {
        cabecalho();
        System.out.println("1 - Listar todos os produtos");
        System.out.println("2 - Procurar e imprimir os dados de um produto");
        System.out.println("3 - Cadastrar novo produto");
        System.out.println("0 - Sair");
        System.out.print("Digite sua opção: ");
        return Integer.parseInt(teclado.nextLine());
    }
    
    /**
     * Lê os dados de um arquivo-texto e retorna um vetor de produtos. Arquivo-texto no formato
     * N (quantidade de produtos) <br/>
     * tipo;descrição;preçoDeCusto;margemDeLucro;[dataDeValidade] <br/>
     * Deve haver uma linha para cada um dos produtos. Retorna um vetor vazio em caso de problemas com o arquivo.
     * @param nomeArquivoDados Nome do arquivo de dados a ser aberto.
     * @return Um vetor com os produtos carregados, ou vazio em caso de problemas de leitura.
     */
    static Produto[] lerProdutos(String nomeArquivoDados) {
        try {
            Path caminho = Paths.get(nomeArquivoDados);
            java.util.List<String> linhas = Files.readAllLines(caminho, StandardCharsets.UTF_8);

            if (linhas.isEmpty()) {
                return new Produto[0];
            }

            int quantidade = Integer.parseInt(linhas.get(0).trim());
            if (quantidade < 0) {
                return new Produto[0];
            }

            Produto[] produtos = new Produto[quantidade];
            for (int i = 0; i < quantidade; i++) {
                if (i + 1 >= linhas.size()) {
                    return new Produto[0];
                }

                Produto produto = Produto.criarDoTexto(linhas.get(i + 1));
                if (produto == null) {
                    return new Produto[0];
                }
                produtos[i] = produto;
            }

            quantosProdutos = produtos.length;
            return produtos;
        } catch (Exception e) {
            quantosProdutos = 0;
            return new Produto[0];
        }
    }
    
    /** Localiza um produto no vetor de produtos cadastrados, a partir do nome de produto informado pelo usuário, e imprime seus dados. 
     *  A busca não é sensível ao caso. Em caso de não encontrar o produto, imprime uma mensagem padrão */
    static void localizarProdutos() {
        System.out.print("Digite o nome do produto: ");
        String busca = teclado.nextLine().trim();

        if (produtosCadastrados == null || produtosCadastrados.length == 0) {
            System.out.println("Nenhum produto encontrado.");
            return;
        }

        boolean encontrado = false;
        String termoBusca = busca.toLowerCase(Locale.ROOT);

        for (Produto produto : produtosCadastrados) {
            if (produto == null) {
                continue;
            }

            String descricao = produto.descricao != null ? produto.descricao : "";
            if (descricao.toLowerCase(Locale.ROOT).contains(termoBusca)) {
                System.out.println(produto);
                encontrado = true;
            }
        }

        if (!encontrado) {
            System.out.println("Produto não encontrado.");
        }
    }
    
    /**
     * Salva os dados dos produtos cadastrados no arquivo csv informado. Sobrescreve todo o conteúdo do arquivo.
     * @param nomeArquivo Nome do arquivo a ser gravado.
     */
    public static void salvarProdutos(String nomeArquivo) {
        if (nomeArquivo == null || nomeArquivo.isBlank()) {
            return;
        }

        quantosProdutos = (produtosCadastrados == null) ? 0 : quantosProdutos;
        java.util.ArrayList<String> linhas = new java.util.ArrayList<>();
        linhas.add(String.valueOf(quantosProdutos));

        if (produtosCadastrados != null) {
            for (Produto produto : produtosCadastrados) {
                if (produto != null) {
                    linhas.add(produto.gerarDadosTexto());
                }
            }
        }

        try {
            Path caminho = Paths.get(nomeArquivo);
            Files.write(caminho, linhas, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE);
        } catch (Exception e) {
            System.out.println("Erro ao salvar os produtos.");
        }
    }
    
    /** Lista todos os produtos cadastrados, numerados, um por linha */
    static void listarTodosOsProdutos() {
        if (produtosCadastrados == null || quantosProdutos == 0) {
            System.out.println("Nenhum produto cadastrado.");
            return;
        }

        for (int i = 0; i < produtosCadastrados.length; i++) {
            if (produtosCadastrados[i] != null) {
                System.out.println((i + 1) + " - " + produtosCadastrados[i]);
            }
        }
    }
    
    /**
     * Rotina para cadastro de um novo produto: pergunta ao usuário o tipo do produto, lê os dados correspondentes,
     * cria o objeto adequado de acordo com o tipo, inclui o produto no vetor.
     */
    static void cadastrarProduto() {
        if (produtosCadastrados != null && quantosProdutos >= MAX_NOVOS_PRODUTOS) {
            System.out.println("Limite máximo de produtos atingido.");
            return;
        }

        try {
            System.out.print("Tipo do produto (1 - Não perecível / 2 - Perecível): ");
            int tipo = Integer.parseInt(teclado.nextLine().trim());

            System.out.print("Descrição: ");
            String descricao = teclado.nextLine().trim();

            System.out.print("Preço de custo: ");
            double precoCusto = Double.parseDouble(teclado.nextLine().trim().replace(",", "."));

            System.out.print("Margem de lucro: ");
            double margemLucro = Double.parseDouble(teclado.nextLine().trim().replace(",", "."));

            Produto novoProduto;

            if (tipo == 1) {
                novoProduto = new ProdutoNaoPerecivel(descricao, precoCusto, margemLucro);
            } else if (tipo == 2) {
                System.out.print("Data de validade (dd/MM/yyyy): ");
                String validadeTexto = teclado.nextLine().trim();
                LocalDate validade = LocalDate.parse(validadeTexto, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                novoProduto = new ProdutoPerecivel(descricao, precoCusto, margemLucro, validade);
            } else {
                System.out.println("Tipo inválido.");
                return;
            }

            if (produtosCadastrados == null) {
                produtosCadastrados = new Produto[MAX_NOVOS_PRODUTOS];
            }

            if (quantosProdutos >= produtosCadastrados.length) {
                Produto[] novoVetor = new Produto[Math.min(produtosCadastrados.length + 1, MAX_NOVOS_PRODUTOS)];
                for (int i = 0; i < produtosCadastrados.length; i++) {
                    novoVetor[i] = produtosCadastrados[i];
                }
                produtosCadastrados = novoVetor;
            }

            for (int i = 0; i < produtosCadastrados.length; i++) {
                if (produtosCadastrados[i] == null) {
                    produtosCadastrados[i] = novoProduto;
                    quantosProdutos++;
                    System.out.println("Produto cadastrado com sucesso!");
                    return;
                }
            }

            System.out.println("Não foi possível cadastrar o produto.");
        } catch (NumberFormatException e) {
            System.out.println("Valor numérico inválido.");
        } catch (DateTimeParseException e) {
            System.out.println("Data inválida. Use o formato dd/MM/yyyy.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
    
	public static void main(String[] args) {
		teclado = new Scanner(System.in, Charset.forName("UTF-8"));
        nomeArquivoDados = "dadosProdutos.csv";
        produtosCadastrados = lerProdutos(nomeArquivoDados);
        
        int opcao = -1;
      
        do{
            opcao = menu();
            switch (opcao) {
                case 1 -> listarTodosOsProdutos();
                case 2 -> localizarProdutos();
                case 3 -> cadastrarProduto();
            }
            pausa();
        }while(opcao != 0);       

        salvarProdutos(nomeArquivoDados);
        teclado.close();    
    }
}

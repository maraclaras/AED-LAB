import java.nio.charset.Charset;
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
            java.nio.file.Path caminho = java.nio.file.Paths.get(nomeArquivoDados);
            java.util.List<String> linhas = java.nio.file.Files.readAllLines(caminho, java.nio.charset.StandardCharsets.UTF_8);

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

        quantosProdutos = (produtosCadastrados == null) ? 0 : produtosCadastrados.length;

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
            java.nio.file.Path caminho = java.nio.file.Paths.get(nomeArquivo);
            java.nio.file.Files.write(caminho, linhas, java.nio.charset.StandardCharsets.UTF_8,
                    java.nio.file.StandardOpenOption.CREATE,
                    java.nio.file.StandardOpenOption.TRUNCATE_EXISTING,
                    java.nio.file.StandardOpenOption.WRITE);
        } catch (Exception e) {
            System.out.println("Erro ao salvar os produtos.");
        }
    }
    
    /** Lista todos os produtos cadastrados, numerados, um por linha */
    static void listarTodosOsProdutos() {
    	
    }
    
    /**
     * Rotina para cadastro de um novo produto: pergunta ao usuário o tipo do produto, lê os dados correspondentes,
     * cria o objeto adequado de acordo com o tipo, inclui o produto no vetor.
     */
    static void cadastrarProduto() {
    	
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

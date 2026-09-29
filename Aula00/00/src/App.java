import java.nio.charset.Charset;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;
import java.util.function.Function;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class App {

	/** Nome do arquivo de dados de produtos. O arquivo deve estar localizado na raiz do projeto */
    static String nomeArquivoProdutos;

    /** Nome do arquivo de clientes. O arquivo deve estar localizado na raiz do projeto */
    static String nomeArquivoClientes;
    
    /** Scanner para leitura de dados do teclado */
    static Scanner teclado;

    /** Aleatório para gerador de pedidos */
    static Random sorteio = new Random(42);

    /** Quantidade de produtos cadastrados atualmente na lista */
    static int quantosProdutos = 0;

    /** estruturas principais para armazenar produtos e clientes */
    static ABB<Integer, Produto> produtosPorId;
    static ABB<String, Produto> produtosPorNome;
    static ABB<Integer, Cliente> clientesPorId;
    
    /** Para associar produtos aos pedidos que os contêm */
    static TabelaHash<Produto, Lista<Pedido>> pedidosPorProduto;

    /** Para associar produtos aos clientes que os compraram */
    static TabelaHash<Produto, Lista<Cliente>> clientesPorProduto;
    
    
    static void limparTela() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /** Gera um efeito de pausa na CLI. Espera por um enter para continuar */
    static void pausa() {
        System.out.println("Digite enter para continuar...");
        teclado.nextLine();
    }

    /** Cabeçalho principal da CLI do sistema */
    static void cabecalho() {
        limparTela();
        System.out.println("AEDs II COMÉRCIO DE COISINHAS");
        System.out.println("=============================");
    }
    
    static <T extends Number> T lerOpcao(String mensagem, Class<T> classe) {
        
    	T valor;
        
    	System.out.println(mensagem);
    	try {
            valor = classe.getConstructor(String.class).newInstance(teclado.nextLine());
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException 
        		| InvocationTargetException | NoSuchMethodException | SecurityException e) {
            return null;
        }
        return valor;
    }
    
    /** Imprime o menu principal, lê a opção do usuário e a retorna (int).
     * Perceba que poderia haver uma melhor modularização com a criação de uma classe Menu.
     * @return Um inteiro com a opção do usuário.
    */
    static int menu() {
        cabecalho();
        System.out.println("PRODUTOS E PEDIDOS");
        System.out.println("=======================================");
        System.out.println("1 - Procurar produtos, por id");
        System.out.println("2 - Recortar produtos, por descrição");
        System.out.println("3 - Pedidos de um produto, em arquivo");
        System.out.println("CLIENTES E PEDIDOS");
        System.out.println("=======================================");
        System.out.println("4 - Relatório de clientes, por id");
        System.out.println("5 - Nomes e documentos de clientes que compraram um produto");
        

        System.out.println("\n0 - Sair");
        System.out.print("Digite sua opção: ");
        return Integer.parseInt(teclado.nextLine());
    }

    static <T> ABB <T, Cliente> lerClientes(String nomeArquivoDados, 
                                            Function<Cliente, T> extratora){
        AVL<T, Cliente> clientes = new AVL<>();
       
        int doc = 10_000;

        try (Scanner arq = new Scanner(new File(nomeArquivoDados),Charset.forName("UTF-8"));) {
            int quantNomes = Integer.parseInt(arq.nextLine());
            String[] nomes = new String[quantNomes];
            String[] sobrenomes = new String[quantNomes];
            for (int i = 0; i < quantNomes; i++) {
                String[] linha = arq.nextLine().split(" ");
                nomes[i] = (linha[0]);
                sobrenomes[i] = (linha[1]);
            }
            arq.close();        
            for (int i = 0; i < nomes.length; i++) {
                for (int j = 0; j < sobrenomes.length; j++) {
                    String nomeCompleto = nomes[i] + " " + sobrenomes[j];
                    Cliente cliente = new Cliente(nomeCompleto, doc++);
                    clientes.inserir(extratora.apply(cliente), cliente);
                }
            }
        }catch (IOException e) {
            Cliente cliente = new Cliente("João Silva", doc);
            clientes.inserir(extratora.apply(cliente), cliente);
        }
        
        return clientes;
    }
                

    /**
     * Lê os dados de um arquivo-texto e retorna uma árvore de produtos. Arquivo-texto no formato
     * N (quantidade de produtos) <br/>
     * tipo;descrição;preçoDeCusto;margemDeLucro;[dataDeValidade] <br/>
     * Deve haver uma linha para cada um dos produtos. Retorna uma árvore vazia em caso de problemas com o arquivo.
     * @param nomeArquivoDados Nome do arquivo de dados a ser aberto.
     * @return Uma árvore com os produtos carregados, ou vazia em caso de problemas de leitura.
     */
    static <T> ABB<T,Produto> lerProdutos(String nomeArquivoDados,
                                        Function<Produto,T> extratorDeChave) {
    	
    	Scanner arquivo = null;
    	int numProdutos;
    	String linha;
    	Produto produto;
    	ABB<T,Produto> produtosCadastrados;
    	
    	try {
    		arquivo = new Scanner(new File(nomeArquivoDados), Charset.forName("UTF-8"));
    		
    		numProdutos = Integer.parseInt(arquivo.nextLine());
    		produtosCadastrados = new AVL<>();
    		
    		for (int i = 0; i < numProdutos; i++) {
    			linha = arquivo.nextLine();
    			produto = Produto.criarDoTexto(linha);
                T chave = extratorDeChave.apply(produto);
    			produtosCadastrados.inserir(chave, produto);
    		}
    		quantosProdutos = produtosCadastrados.tamanho();
    		
    	} catch (IOException excecaoArquivo) {
    		produtosCadastrados = null;
    	} finally {
    		arquivo.close();
    	}
    	
    	return produtosCadastrados;
    }
    
    
    public static Pedido pedidoComItensAleatorios(){
        Pedido novoPedido = new Pedido();   // novo pedido aleatório
        int quantProdutos = sorteio.nextInt(8)+1;   //quantidade de produtos no novo pedido

        // TAREFA 2: associa o pedido a um cliente sorteado
        int idCliente = sorteio.nextInt(clientesPorId.tamanho()) + 10_000;
        Cliente cliente = clientesPorId.pesquisar(idCliente);
        cliente.adicionarPedido(novoPedido);
        
        for (int j = 0; j < quantProdutos; j++) {
                int idProduto = sorteio.nextInt(produtosPorId.tamanho())+10_000;        //sorteia um id de produto
                Produto prod = produtosPorId.pesquisar(idProduto);                      //localiza produto na árvore
                novoPedido.incluirProduto(prod);                                        //inclui produto no pedido
                inserirNaTabela(pedidosPorProduto, prod, novoPedido);                   //associa pedido ao produto na tabela hash

                // TAREFA 3: associa o produto ao cliente que o comprou
                inserirClienteNaTabela(prod, cliente);
        }

            
        return novoPedido;
    }

    
    /** Localiza um produto na árvore de produtos organizados por id, a partir do código de produto informado pelo usuário, e o retorna. 
    *  Em caso de não encontrar o produto, retorna null */
    static Produto localizarProdutoID() {
        cabecalho();
        System.out.println("LOCALIZANDO POR ID");
        int ID = lerOpcao("Digite o ID para busca", Integer.class);
        Produto localizado =  localizarProduto(produtosPorId, ID);
        mostrarProduto(localizado);
        return localizado;
    }
    
    static <K> Produto localizarProduto(ABB<K, Produto> produtosCadastrados, K chave){
        cabecalho();
        Produto localizado =  produtosCadastrados.pesquisar(chave);
        System.out.println("Tempo: " +produtosCadastrados.getTempo());
        System.out.println("Comparações: " +produtosCadastrados.getComparacoes());
        pausa();
        return localizado;
    }

    private static void mostrarProduto(Produto produto) {    	
        cabecalho();
        String mensagem = "Dados inválidos para o produto!";
        
        if (produto != null){
            mensagem = String.format("Dados do produto:\n%s", produto);
        }
        System.out.println(mensagem);
    }

    private static Lista<Pedido> gerarPedidos(int quantidade){
        Lista<Pedido> pedidos = new Lista<>();

        for (int i = 0; i < quantidade; i++) {
            Pedido ped = pedidoComItensAleatorios();
            pedidos.inserir(ped);
            
        }
        return pedidos;
    }

    private static <K,V> void inserirNaTabela(TabelaHash<K,Lista<V>> tabela, K chave, V dado){
        Lista<V> sublista;
        try{
            sublista = tabela.pesquisar(chave);
        }catch(NoSuchElementException nex){
            sublista = new Lista<V>();
            tabela.inserir(chave, sublista);
        }
        sublista.inserir(dado);
    }

    private static void recortarArvore(ABB<String, Produto> arvore) {
        cabecalho();
        System.out.print("Digite ponto de início do filtro: ");
        String descIni =teclado.nextLine();
        System.out.print("Digite ponto de fim do filtro: ");
        String descFim =teclado.nextLine();
        
        System.out.println(arvore.recortar(descIni, descFim));
    }
    
    static void pedidosDoProduto(){
        Produto produto = localizarProdutoID();
        String nomeArquivo = "RelatorioProduto"+produto.hashCode()+".txt";    
        try (FileWriter arquivoRelatorio = new FileWriter(nomeArquivo)){
            Lista<Pedido> listaProd = pedidosPorProduto.pesquisar(produto);
            arquivoRelatorio.append(listaProd+"\n");
            arquivoRelatorio.close();
            System.out.println("Dados salvos em "+nomeArquivo);
        } catch (IOException e) {
            System.out.println("Problemas para criar o arquivo "+nomeArquivo+". Tente novamente");
        }
    }

    /**
     * Associa um cliente a um produto sem repetir o mesmo cliente na lista
     * daquele produto.
     */
    private static void inserirClienteNaTabela(Produto produto, Cliente cliente){
        Lista<Cliente> listaClientes;

        try {
            listaClientes = clientesPorProduto.pesquisar(produto);
        } catch (NoSuchElementException ex) {
            listaClientes = new Lista<>();
            clientesPorProduto.inserir(produto, listaClientes);
        }

        try {
            listaClientes.pesquisar(cliente);
        } catch (NoSuchElementException ex) {
            listaClientes.inserir(cliente);
        }
    }

    static void clientesPorProduto(){
        cabecalho();
        System.out.println("CLIENTES QUE COMPRARAM UM PRODUTO");
        Integer idProduto = lerOpcao("Digite o ID do produto", Integer.class);

        if (idProduto == null) {
            System.out.println("Identificador inválido.");
            return;
        }

        try {
            Produto produto = produtosPorId.pesquisar(idProduto);
            Lista<Cliente> clientes = clientesPorProduto.pesquisar(produto);

            System.out.println("\nProduto: " + produto);
            System.out.println("Clientes que compraram este produto:");
            System.out.println(clientes);
        } catch (NoSuchElementException ex) {
            System.out.println("Produto não encontrado ou ainda não comprado por nenhum cliente.");
        }
    }

    public static void relatorioDeCliente(){
        cabecalho();
        System.out.println("RELATÓRIO DE CLIENTE");
        Integer documento = lerOpcao("Digite o documento identificador do cliente", Integer.class);

        if (documento == null) {
            System.out.println("Documento inválido.");
            return;
        }

        try {
            Cliente cliente = clientesPorId.pesquisar(documento);

            System.out.println("\nCliente: " + cliente);
            System.out.println("Pedidos realizados:");
            System.out.println(cliente.getPedidos());
            System.out.printf("Total gasto: R$ %.2f%n", cliente.totalGasto());
        } catch (NoSuchElementException ex) {
            System.out.println("Cliente não encontrado.");
        }
    }
   

    static void configurarSistema(){
        nomeArquivoProdutos = "produtos.txt";
        nomeArquivoClientes = "nomes.txt";
        produtosPorId = lerProdutos(nomeArquivoProdutos, Produto::hashCode);
        clientesPorId = lerClientes(nomeArquivoClientes, Cliente::hashCode);
        produtosPorNome = new AVL<>(produtosPorId, prod -> prod.descricao, String::compareTo);
        
        pedidosPorProduto = new TabelaHash<>((int)(produtosPorId.tamanho()*1.25));
        clientesPorProduto = new TabelaHash<>((int)(produtosPorId.tamanho()*1.25));
        
        gerarPedidos(25000);
    }


    public static void main(String[] args) {
		teclado = new Scanner(System.in, Charset.forName("UTF-8"));
        configurarSistema();      
        int opcao = -1;
        
        do{
            opcao = menu();
            switch (opcao) {
                case 1 -> localizarProdutoID();
                case 2 -> recortarArvore(produtosPorNome);
                case 3 -> pedidosDoProduto();
                case 4 -> relatorioDeCliente();
                case 5 -> clientesPorProduto();
             }
            pausa();
        }while(opcao != 0);       

        teclado.close();    
    }
}

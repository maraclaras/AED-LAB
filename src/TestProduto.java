public class TestProduto {
    public static void main(String[] args) {
        Produto p = new Produto("Produto teste", 100, 0.1);
        ProdutoNaoPerecivel np = new ProdutoNaoPerecivel("Arroz", 50, 0.3);

        System.out.println("Produto valorDeVenda: " + p.valorDeVenda());
        System.out.println("Produto toString: " + p.toString());
        System.out.println("NaoPerecivel valorDeVenda: " + np.valorDeVenda());
        System.out.println("NaoPerecivel toString: " + np.toString());
    }
}

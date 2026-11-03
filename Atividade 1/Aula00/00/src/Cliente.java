public class Cliente {

    private String nome;
    private int documento;
    private Lista<Pedido> pedidos;
    

    public Cliente(String nome, int documento){
        if(!nome.contains(" ") && nome.split(" ").length!=2)
            throw new IllegalArgumentException("Favor cadastrar cliente com um nome e um sobrenome");
        if(documento < 10_000)
            throw new IllegalArgumentException("Documentos válidos devem ter pelo menos 5 dígitos");
        this.documento = documento;
        this.nome = nome;
        this.pedidos = new Lista<>();
    }

    public void adicionarPedido(Pedido novo){
        if (novo == null)
            throw new IllegalArgumentException("Não é possível adicionar um pedido nulo ao cliente");

        pedidos.inserir(novo);
    }


    public double totalGasto(){
        if (pedidos.vazia())
            return 0.0;

        return pedidos.calcularValorTotal(Pedido::valorFinal);
    }

    public Lista<Pedido> getPedidos(){
        return pedidos;
    }

    @Override
    public String toString(){
        return nome+" ("+documento+")";
    }
    
    @Override
    public int hashCode(){
        return documento;
    }
    
}

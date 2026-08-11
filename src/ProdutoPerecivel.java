import java.time.LocalDate;

public class ProdutoPerecivel extends Produto {
    public static final double DESCONTO = 0.25; // Desconto de 5% para produtos perecíveis
    public static final int PRAZO_DESCONTO = 7; // Margem de lucro padrão para produtos perecíveis
    public LocalDate dataDeValidade; 

	public ProdutoPerecivel(String desc, double precoCusto, double margemLucro, LocalDate dataDeValidade) {
		super(desc, precoCusto, margemLucro);
		this.dataDeValidade = dataDeValidade;
	}

	// public ProdutoPerecivel(String desc, double precoCusto) {
	// 	super(desc, precoCusto);
	// }

    public double valorDeVenda() {
        double valorBase = super.valorDeVenda();
        if (dataDeValidade.isBefore(LocalDate.now().plusDays(PRAZO_DESCONTO))) {
            return valorBase * (1 - DESCONTO); // Aplica o desconto se estiver próximo da validade
        }
        return valorBase;
    }

    public String toString() {
        return super.toString() + " (Perecível)";
    }
}

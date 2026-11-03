import java.util.NoSuchElementException;

public class Pilha<E> {

	private Celula<E> topo;
	private Celula<E> fundo;

	public Pilha() {

		Celula<E> sentinela = new Celula<E>();
		fundo = sentinela;
		topo = sentinela;

	}

	public boolean vazia() {
		return fundo == topo;
	}

	public void empilhar(E item) {

		topo = new Celula<E>(item, topo);
	}

	public E desempilhar() {

		E desempilhado = consultarTopo();
		topo = topo.getProximo();
		return desempilhado;

	}

	public E consultarTopo() {

		if (vazia()) {
			throw new NoSuchElementException("Nao há nenhum item na pilha!");
		}

		return topo.getItem();

	}

	/**
	 * Cria e devolve uma nova pilha contendo os primeiros numItens elementos
	 * do topo da pilha atual.
	 * 
	 * Os elementos são mantidos na mesma ordem em que estavam na pilha original.
	 * Caso a pilha atual possua menos elementos do que o valor especificado,
	 * uma exceção será lançada.
	 *
	 * @param numItens o número de itens a serem copiados da pilha original.
	 * @return uma nova instância de Pilha<E> contendo os numItens primeiros elementos.
	 * @throws IllegalArgumentException se a pilha não contém numItens elementos.
	 */
	public Pilha<E> subPilha(int numItens) {
		
		if (numItens < 0) {
			throw new IllegalArgumentException("A quantidade não pode ser negativa.");
		}

		Pilha<E> resultado = new Pilha<>();
		Celula<E> auxiliar = topo;
		int quantidadeDisponivel = 0;

		while (auxiliar != fundo) {
			quantidadeDisponivel++;
			auxiliar = auxiliar.getProximo();
		}

		if (numItens > quantidadeDisponivel) {
			throw new IllegalArgumentException("A pilha não possui essa quantidade de itens.");
		}

		// Guarda os elementos do topo para baixo.
		Object[] itens = new Object[numItens];
		auxiliar = topo;
		for (int i = 0; i < numItens; i++) {
			itens[i] = auxiliar.getItem();
			auxiliar = auxiliar.getProximo();
		}

		// Empilha de baixo para cima para preservar a ordem original.
		for (int i = numItens - 1; i >= 0; i--) {
			@SuppressWarnings("unchecked")
			E item = (E) itens[i];
			resultado.empilhar(item);
		}

		return resultado;
	}
}
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Fila genérica implementada com células encadeadas.
 * O primeiro elemento inserido é o primeiro a ser removido (FIFO).
 */
public class Fila<E> {

    private Celula<E> frente;
    private Celula<E> tras;

    public Fila() {
        frente = null;
        tras = null;
    }

    public boolean vazia() {
        return frente == null;
    }

    public void enfileirar(E item) {
        Celula<E> nova = new Celula<E>(item);
        if (vazia()) {
            frente = nova;
        } else {
            tras.setProximo(nova);
        }
        tras = nova;
    }

    public E desenfileirar() {
        if (vazia()) {
            throw new NoSuchElementException("A fila está vazia!");
        }

        E item = frente.getItem();
        frente = frente.getProximo();

        if (frente == null) {
            tras = null;
        }
        return item;
    }

    public E consultarInicio() {
        if (vazia()) {
            throw new NoSuchElementException("A fila está vazia!");
        }
        return frente.getItem();
    }

    /**
     * Calcula a média do atributo extraído dos primeiros 'quantidade' elementos.
     * A fila original não é modificada.
     */
    public double calcularValorMedio(Function<E, Double> extrator, int quantidade) {
        if (extrator == null) {
            throw new IllegalArgumentException("O extrator não pode ser nulo.");
        }
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }

        Celula<E> auxiliar = frente;
        double soma = 0.0;
        int contagem = 0;

        while (auxiliar != null && contagem < quantidade) {
            Double valor = extrator.apply(auxiliar.getItem());
            if (valor == null) {
                throw new IllegalArgumentException("O extrator retornou um valor nulo.");
            }
            soma += valor;
            contagem++;
            auxiliar = auxiliar.getProximo();
        }

        if (contagem == 0) {
            throw new NoSuchElementException("Não há pedidos na fila.");
        }
        return soma / contagem;
    }

    /**
     * Cria uma nova fila com os elementos que atendem à condição,
     * considerando somente os primeiros 'quantidade' elementos.
     * A fila original não é modificada.
     */
    public Fila<E> filtrar(Predicate<E> condicional, int quantidade) {
        if (condicional == null) {
            throw new IllegalArgumentException("A condição não pode ser nula.");
        }
        if (quantidade < 0) {
            throw new IllegalArgumentException("A quantidade não pode ser negativa.");
        }

        Fila<E> resultado = new Fila<>();
        Celula<E> auxiliar = frente;
        int contagem = 0;

        while (auxiliar != null && contagem < quantidade) {
            E item = auxiliar.getItem();
            if (condicional.test(item)) {
                resultado.enfileirar(item);
            }
            contagem++;
            auxiliar = auxiliar.getProximo();
        }
        return resultado;
    }

    public int tamanho() {
        int quantidade = 0;
        Celula<E> auxiliar = frente;
        while (auxiliar != null) {
            quantidade++;
            auxiliar = auxiliar.getProximo();
        }
        return quantidade;
    }

    @Override
    public String toString() {
        StringBuilder texto = new StringBuilder();
        Celula<E> auxiliar = frente;
        while (auxiliar != null) {
            texto.append(auxiliar.getItem()).append("\\n");
            auxiliar = auxiliar.getProximo();
        }
        return texto.toString();
    }
}

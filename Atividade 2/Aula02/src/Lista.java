
import java.util.NoSuchElementException;
import java.util.function.Predicate;

public class Lista<E> {

    private Celula<E> primeiro;
    private Celula<E> ultimo;
    private int quantidade;

    public Lista() {
        primeiro = null;
        ultimo = null;
        quantidade = 0;
    }

    public boolean vazia() {
        return primeiro == null;
    }

    public int tamanho() {
        return quantidade;
    }

    public void inserirInicio(E item) {
        Celula<E> nova = new Celula<E>(item, primeiro);

        primeiro = nova;

        if (ultimo == null) {
            ultimo = nova;
        }

        quantidade++;
    }

    public void inserirFinal(E item) {
        Celula<E> nova = new Celula<E>(item);

        if (vazia()) {
            primeiro = nova;
        } else {
            ultimo.setProximo(nova);
        }

        ultimo = nova;
        quantidade++;
    }

    public E consultar(int indice) {
        validarIndice(indice);

        Celula<E> auxiliar = primeiro;

        for (int i = 0; i < indice; i++) {
            auxiliar = auxiliar.getProximo();
        }

        return auxiliar.getItem();
    }

    public E removerInicio() {
        if (vazia()) {
            throw new NoSuchElementException("Lista vazia!");
        }

        E item = primeiro.getItem();

        primeiro = primeiro.getProximo();
        quantidade--;

        if (primeiro == null) {
            ultimo = null;
        }

        return item;
    }

    public E removerFinal() {
        if (vazia()) {
            throw new NoSuchElementException("Lista vazia!");
        }

        if (primeiro == ultimo) {
            return removerInicio();
        }

        Celula<E> auxiliar = primeiro;

        while (auxiliar.getProximo() != ultimo) {
            auxiliar = auxiliar.getProximo();
        }

        E item = ultimo.getItem();

        ultimo = auxiliar;
        ultimo.setProximo(null);

        quantidade--;

        return item;
    }

    public boolean contem(E item) {
        Celula<E> auxiliar = primeiro;

        while (auxiliar != null) {

            if (auxiliar.getItem() == null
                    ? item == null
                    : auxiliar.getItem().equals(item)) {
                return true;
            }

            auxiliar = auxiliar.getProximo();
        }

        return false;
    }

    public Lista<E> filtrar(Predicate<E> condicional, int quantidade) {

        if (condicional == null) {
            throw new IllegalArgumentException("Condição inválida!");
        }

        if (quantidade < 0) {
            throw new IllegalArgumentException("Quantidade inválida!");
        }

        Lista<E> resultado = new Lista<>();

        Celula<E> auxiliar = primeiro;
        int contagem = 0;

        while (auxiliar != null && contagem < quantidade) {

            E item = auxiliar.getItem();

            if (condicional.test(item)) {
                resultado.inserirFinal(item);
            }

            contagem++;
            auxiliar = auxiliar.getProximo();
        }

        return resultado;
    }

    private void validarIndice(int indice) {
        if (indice < 0 || indice >= quantidade) {
            throw new IndexOutOfBoundsException("Índice inválido!");
        }
    }

    @Override
    public String toString() {
        StringBuilder texto = new StringBuilder();

        Celula<E> auxiliar = primeiro;

        while (auxiliar != null) {
            texto.append(auxiliar.getItem()).append("\n");
            auxiliar = auxiliar.getProximo();
        }

        return texto.toString();
    }
}

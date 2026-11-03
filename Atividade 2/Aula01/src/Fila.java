import java.util.NoSuchElementException;
import java.util.function.Function;

public class Fila<E> {

    private Celula<E> frente;
    private Celula<E> tras;

    public Fila() {

        Celula<E> sentinela = new Celula<E>();
        frente = sentinela;
        tras = sentinela;

    }

    public boolean vazia() {
        return frente == null;
    }

    public void enfileirar(E item) {
        Celula<E> novaCelula = new Celula<>(item);

        if (vazia()) {
            frente = novaCelula;
        } else {
            tras.getProximo(novaCelula);
        }
        tras = novaCelula;
    }

    public E desenfileirar() {
        if (vazia()) {
            throw new Exception("Fila Vazia");
        }
        E desenfileirado = frente.getItem();
        frente = frente.getProximo();

        if (frente = null) {
            tras = null;
        }
        return desenfileirado;
    }

// Esse método deve ser capaz de calcular e retornar o valor médio de um dos
// atributos dos elementos da fila, utilizando uma função de extração fornecida pelo parâmetro extrator, que extrai
// um valor numérico (Double) de cada elemento da fila. Devem ser extraídos valores de apenas os primeiros
// elementos armazenados na fila, de acordo com o parâmetro quantidade
    public double calcularValorMedio(Function<E, Double> extrator, int quantidade) {
        double valorMedio;


        return  0;//valorMedio;
    }

// Esse método deve ser capaz de retornar uma nova fila contendo apenas os elementos da fila original que
// satisfazem uma condição específica. Esse método deve apresentar dois parâmetros: condicional, função que
// testa se um determinado elemento deve ser enfileirado na nova fila; e quantidade, que indica quantos primeiros
// elementos da fila original devem ser submetidos ao teste.
    public Fila<E> filtrar(Predicate<E> condicional, int quantidade) {
        Celula<E> novaFila = new Celula<>();
        

    }
}

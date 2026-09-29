import java.util.Comparator;

public class QuickSort<T extends Comparable<T>> implements IOrdenator<T> {

    private T[] dadosOrdenados;
    private Comparator<T> comparador;

    public QuickSort() {
        setComparador(T::compareTo);
    }

    public QuickSort(Comparator<T> comparador) {
        setComparador(comparador);
    }

    @Override
    public void setComparador(Comparator<T> comparador) {
        this.comparador = comparador;
    }

    @Override
    public T[] ordenar(T[] dados) {
        dadosOrdenados = dados;
        for (int i = 0; i < dadosOrdenados.length; i++) {
            for (int j = i; j < dadosOrdenados.length; j++) {

            }

        }
        return dadosOrdenados;
    }

}
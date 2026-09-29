import java.util.Comparator;
public class ComparadorPorValor implements Comparator<Ingresso> {

    @Override
    public int compare(Ingresso a, Ingresso b) {
        int cmp = 0;
        Double.compare(a.valorFinal(), b.valorFinal());
        if (cmp != 0){
            return cmp;
        }
        return cmp = a.evento.compareTo(b.evento);
    }

}



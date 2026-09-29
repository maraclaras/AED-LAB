import java.text.NumberFormat;

/**
 * Representa um ingresso de um evento.
 */
public class Ingresso {
    private int id;
    private String evento;
    private double valor;

    public Ingresso(int id, String evento, double valor) {
        if (id <= 0)
            throw new IllegalArgumentException("O identificador deve ser positivo.");
        if (evento == null || evento.isBlank())
            throw new IllegalArgumentException("O nome do evento não pode ser vazio.");
        if (valor < 0)
            throw new IllegalArgumentException("O valor do ingresso não pode ser negativo.");

        this.id = id;
        this.evento = evento;
        this.valor = valor;
    }

    public int getId() {
        return id;
    }

    public String getEvento() {
        return evento;
    }

    public double getValor() {
        return valor;
    }

    @Override
    public String toString() {
        NumberFormat moeda = NumberFormat.getCurrencyInstance();
        return String.format("ID: %d | EVENTO: %s | VALOR: %s", id, evento, moeda.format(valor));
    }
}

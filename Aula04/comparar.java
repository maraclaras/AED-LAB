// Questão 1 — ordenar por valor e evento
// Pelo relato, a regra provavelmente é:
// 1. Primeiro, ordenar pelo valor em ordem crescente.
// 2. Se dois ingressos possuírem o mesmo valor, ordenar pelo nome do evento em ordem alfabética.
// Exemplo:
// Show B — 50
// Show A — 30
// Show C — 50
// Show D — 20

// Resultado:
// Show D — 20
// Show A — 30
// Show B — 50
// Show C — 50

// Show B fica antes de Show C porque os valores são iguais e B vem antes de C.
// Método comparador:
private static int comparar(Ingresso ingresso1, Ingresso ingresso2) {
    if (ingresso1.getValor() < ingresso2.getValor()) {
        return -1;
    }

    if (ingresso1.getValor() > ingresso2.getValor()) {
        return 1;
    }

    return ingresso1.getEvento()
                    .compareToIgnoreCase(ingresso2.getEvento());
}
// Esse método retorna:
// - negativo quando ingresso1 deve ficar antes;
// - positivo quando ingresso1 deve ficar depois;
// - zero quando os dois são considerados iguais.
// A parte mais importante é:
// return ingresso1.getEvento()
//                 .compareToIgnoreCase(ingresso2.getEvento());

// Ela só é executada quando os dois ingressos possuem o mesmo valor.
// Também poderia aparecer assim:
private static int comparar(Ingresso ingresso1, Ingresso ingresso2) {
    int comparacaoValor =
        Double.compare(ingresso1.getValor(), ingresso2.getValor());

    if (comparacaoValor != 0) {
        return comparacaoValor;
    }

    return ingresso1.getEvento()
                    .compareToIgnoreCase(ingresso2.getEvento());
}
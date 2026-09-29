// Questão 3 — localizar ingressos em uma faixa
// O método recebe:
// double minimo
// double maximo

// Ele precisa:
// 1. verificar quais ingressos estão entre os dois valores;
// 2. considerar normalmente os próprios limites;
// 3. imprimir os encontrados em ordem crescente.
// Portanto, a condição da faixa é:
// valor >= minimo && valor <= maximo

// Solução mais simples
// Podemos primeiro ordenar os ingressos e depois imprimir somente os que estão na faixa:
public static void localizarPorFaixa(
    Ingresso[] ingressos,
    double minimo,
    double maximo
) {
    bubbleSort(ingressos);

    for (int i = 0; i < ingressos.length; i++) {
        double valorAtual = ingressos[i].getValor();

        if (valorAtual >= minimo && valorAtual <= maximo) {
            System.out.println(ingressos[i]);
        }
    }
}

// Versão que não altera o vetor original
// A versão anterior ordena o vetor original. Caso isso não possa acontecer, podemos copiar apenas os ingressos encontrados:
public static void localizarPorFaixa(
    Ingresso[] ingressos,
    double minimo,
    double maximo
) {
    Ingresso[] encontrados = new Ingresso[ingressos.length];
    int quantidade = 0;

    for (int i = 0; i < ingressos.length; i++) {
        double valorAtual = ingressos[i].getValor();

        if (valorAtual >= minimo && valorAtual <= maximo) {
            encontrados[quantidade] = ingressos[i];
            quantidade++;
        }
    }

    ordenarPorValor(encontrados, quantidade);

    for (int i = 0; i < quantidade; i++) {
        System.out.println(encontrados[i]);
    }
}
// O Bubble exclusivo para a quantidade encontrada seria:
private static void ordenarPorValor(
    Ingresso[] ingressos,
    int quantidade
) {
    for (int i = 0; i < quantidade - 1; i++) {
        for (int j = 0; j < quantidade - 1 - i; j++) {
            if (ingressos[j].getValor()
                > ingressos[j + 1].getValor()) {

                Ingresso temporario = ingressos[j];
                ingressos[j] = ingressos[j + 1];
                ingressos[j + 1] = temporario;
            }
        }
    }
}
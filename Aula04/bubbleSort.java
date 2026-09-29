
public static void bubbleSort(Ingresso[] ingressos) {
    for (int i = 0; i < ingressos.length - 1; i++) {
        for (int j = 0; j < ingressos.length - 1 - i; j++) {

            if (comparar(ingressos[j], ingressos[j + 1]) > 0) {
                Ingresso temporario = ingressos[j];
                ingressos[j] = ingressos[j + 1];
                ingressos[j + 1] = temporario;
            }
        }
    }
}
// O que acontece aqui?
// if (comparar(ingressos[j], ingressos[j + 1]) > 0)

// Isso pergunta:
// O ingresso da esquerda deveria ficar depois do ingresso da direita?

// Se a resposta for sim, os dois trocam de lugar:
// Ingresso temporario = ingressos[j];
// ingressos[j] = ingressos[j + 1];
// ingressos[j + 1] = temporario;

// Bubble com melhoria
// A professora também pode esperar a variável que verifica se ocorreu alguma troca:
public static void bubbleSort(Ingresso[] ingressos) {
    boolean trocou;

    for (int i = 0; i < ingressos.length - 1; i++) {
        trocou = false;

        for (int j = 0; j < ingressos.length - 1 - i; j++) {
            if (comparar(ingressos[j], ingressos[j + 1]) > 0) {
                Ingresso temporario = ingressos[j];
                ingressos[j] = ingressos[j + 1];
                ingressos[j + 1] = temporario;

                trocou = true;
            }
        }

        if (!trocou) {
            break;
        }
    }
}
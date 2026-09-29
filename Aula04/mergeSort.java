// Merge Sort
// O método público apenas inicia o processo:
public static void mergeSort(Ingresso[] ingressos) {
    mergeSort(ingressos, 0, ingressos.length - 1);
}
// O método privado divide o vetor:
private static void mergeSort(
    Ingresso[] ingressos,
    int inicio,
    int fim
) {
    if (inicio < fim) {
        int meio = (inicio + fim) / 2;

        mergeSort(ingressos, inicio, meio);
        mergeSort(ingressos, meio + 1, fim);

        intercalar(ingressos, inicio, meio, fim);
    }
}
// Agora vem o método que junta as duas partes de forma ordenada:
private static void intercalar(
    Ingresso[] ingressos,
    int inicio,
    int meio,
    int fim
) {
    Ingresso[] auxiliar = new Ingresso[fim - inicio + 1];

    int i = inicio;
    int j = meio + 1;
    int k = 0;

    while (i <= meio && j <= fim) {
        if (comparar(ingressos[i], ingressos[j]) <= 0) {
            auxiliar[k] = ingressos[i];
            i++;
        } else {
            auxiliar[k] = ingressos[j];
            j++;
        }

        k++;
    }

    while (i <= meio) {
        auxiliar[k] = ingressos[i];
        i++;
        k++;
    }

    while (j <= fim) {
        auxiliar[k] = ingressos[j];
        j++;
        k++;
    }

    for (k = 0; k < auxiliar.length; k++) {
        ingressos[inicio + k] = auxiliar[k];
    }
}
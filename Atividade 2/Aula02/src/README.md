# AEDs II - Comércio de Coisinhas

## Estruturas adicionadas
- `Pilha<E>`: pedidos finalizados, com o mais recente no topo. `subPilha(n)` copia os n elementos do topo sem alterar a pilha original.
- `Fila<E>`: pedidos na ordem de finalização. Inclui `calcularValorMedio` e `filtrar`, ambos sem modificar a fila original.
- `Lista<E>`: lista simplesmente encadeada genérica com inserção no início/final, consulta por índice, remoção no início/final e busca.

## Execução
Compile os arquivos Java e execute `App`. O arquivo `produtos.txt` deve estar no diretório de execução e seguir o formato descrito no código-base.

## Menu
1. Listar produtos
2. Buscar produto por código
3. Buscar produto por descrição
4. Iniciar pedido
5. Finalizar pedido
6. Listar produtos dos pedidos mais recentes
7. Calcular valor médio dos N primeiros pedidos
8. Listar os primeiros pedidos acima de um valor
9. Listar os primeiros pedidos que contêm um produto
0. Sair

A opção 6 pede quantos pedidos recentes devem ser exibidos. As opções 7 a 9 consideram no máximo os N primeiros pedidos da fila; se houver menos de N, consideram todos os disponíveis.

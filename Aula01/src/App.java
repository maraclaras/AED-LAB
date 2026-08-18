import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Normalizer;
import java.time.LocalDate;
import java.util.Locale;

public class App {
    public static void main(String[] args) {

    }

    static Produto[] lerProdutos(String nomeArquivoDados) {
        if (nomeArquivoDados == null || nomeArquivoDados.isBlank()) {
            return new Produto[0];
        }

        try (BufferedReader leitor = Files.newBufferedReader(Path.of(nomeArquivoDados), StandardCharsets.UTF_8)) {
            String linha = leitor.readLine();
            if (linha == null) {
                return new Produto[0];
            }

            int quantidade;
            try {
                quantidade = Integer.parseInt(linha.trim());
            } catch (NumberFormatException e) {
                return new Produto[0];
            }

            if (quantidade < 0) {
                return new Produto[0];
            }

            Produto[] produtos = new Produto[quantidade];
            int indice = 0;

            while (indice < quantidade) {
                linha = leitor.readLine();
                if (linha == null) {
                    return new Produto[0];
                }

                String linhaLimpa = linha.trim();
                if (linhaLimpa.isEmpty()) {
                    continue;
                }

                String[] campos = linhaLimpa.split(";");
                if (campos.length < 4 || campos.length > 5) {
                    return new Produto[0];
                }

                try {
                    String tipo = normalizarTipo(campos[0]);
                    String descricao = campos[1].trim();
                    double precoCusto = Double.parseDouble(campos[2].replace(',', '.').trim());
                    double margemLucro = Double.parseDouble(campos[3].replace(',', '.').trim());

                    if (descricao.length() < 3 || precoCusto <= 0 || margemLucro <= 0) {
                        return new Produto[0];
                    }

                    if (tipo.equals("p") || tipo.equals("perecivel")) {
                        if (campos.length != 5) {
                            return new Produto[0];
                        }

                        LocalDate validade = LocalDate.parse(campos[4].trim());
                        produtos[indice++] = new ProdutoPerecivel(descricao, precoCusto, margemLucro, validade);
                    } else if (tipo.equals("n") || tipo.equals("naoperecivel")) {
                        produtos[indice++] = new ProdutoNaoPerecivel(descricao, precoCusto, margemLucro);
                    } else {
                        return new Produto[0];
                    }
                } catch (Exception e) {
                    return new Produto[0];
                }
            }

            return produtos;
        } catch (IOException e) {
            return new Produto[0];
        }
    }

    private static String normalizarTipo(String tipo) {
        String valor = Normalizer.normalize(tipo.trim(), Normalizer.Form.NFD);
        valor = valor.replaceAll("[^\\p{ASCII}]", "");
        valor = valor.toLowerCase(Locale.ROOT).replace(" ", "").replace("_", "").replace("-", "");

        if (valor.equals("p") || valor.equals("perecivel")) {
            return "p";
        }
        if (valor.equals("n") || valor.equals("naoperecivel")) {
            return "n";
        }
        return valor;
    }
}

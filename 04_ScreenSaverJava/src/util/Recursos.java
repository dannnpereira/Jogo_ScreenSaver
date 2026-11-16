package util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import javax.imageio.ImageIO;

/**
 * Carrega arquivos de recursos (imagens) procurando primeiro no classpath
 * (pasta bin/) e depois na pasta src/, para funcionar independente de onde
 * o jogo for executado (pasta 04_ScreenSaverJava ou raiz do repositório).
 */
public final class Recursos {

    private static final String[] PASTAS_ALTERNATIVAS = { "src/", "04_ScreenSaverJava/src/" };

    private Recursos() {}

    public static BufferedImage carregarImagem(String caminho) {
        try {
            URL url = Recursos.class.getResource("/" + caminho);
            if (url != null) return ImageIO.read(url);

            for (String pasta : PASTAS_ALTERNATIVAS) {
                File arquivo = new File(pasta + caminho);
                if (arquivo.isFile()) return ImageIO.read(arquivo);
            }
        } catch (Exception e) {
            // cai no aviso abaixo
        }
        System.out.println("Não foi possível carregar a imagem: " + caminho);
        return null;
    }
}

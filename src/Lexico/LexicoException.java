package Lexico;

public class LexicoException extends Exception {
    private final int linha;
    private final int coluna;

    public LexicoException(String message, int linha, int coluna) {
        super(message + " (linha: " + linha + ", coluna: " + coluna + ")");
        this.linha = linha;
        this.coluna = coluna;
    }

    public int getLinha() {
        return linha;
    }

    public int getColuna() {
        return coluna;
    }
}
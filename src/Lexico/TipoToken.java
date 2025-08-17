package Lexico;

public enum TipoToken {
    // Operadores
    SOMA("+"),
    SUBTRACAO("-"),
    MULTIPLICACAO("*"),
    DIVISAO("/"),
    POTENCIA("^"),
    ATRIBUICAO("="),
    PARENTESE_ESQ("("),
    PARENTESE_DIR(")"),

    // Identificadores e literais
    IDENTIFICADOR,
    NUMERO,

    // Fim de arquivo
    EOF;

    private final String texto;

    TipoToken() {
        this.texto = null;
    }

    TipoToken(String texto) {
        this.texto = texto;
    }

    public String getTexto() {
        return texto;
    }
}
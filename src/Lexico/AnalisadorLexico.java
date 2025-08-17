package Lexico;

import java.util.ArrayList;
import java.util.List;

public class AnalisadorLexico {
    private final String input;
    private int posicao;
    private int linha;
    private int coluna;

    public AnalisadorLexico(String input) {
        this.input = input;
        this.posicao = 0;
        this.linha = 1;
        this.coluna = 1;
    }

    public List<Token> analisar() throws LexicoException {
        List<Token> tokens = new ArrayList<>();

        while (posicao < input.length()) {
            char c = input.charAt(posicao);

            // Ignorar espaços em branco
            if (Character.isWhitespace(c)) {
                if (c == '\n') {
                    linha++;
                    coluna = 1;
                } else {
                    coluna++;
                }
                posicao++;
                continue;
            }

            // Reconhecer tokens
            if (Character.isLetter(c)) {
                tokens.add(reconhecerIdentificador());
            } else if (Character.isDigit(c)) {
                tokens.add(reconhecerNumero());
            } else {
                tokens.add(reconhecerSimbolo());
            }
        }

        tokens.add(new Token(TipoToken.EOF, "", linha, coluna));
        return tokens;
    }

    private Token reconhecerIdentificador() {
        int inicio = posicao;
        while (posicao < input.length() &&
                (Character.isLetterOrDigit(input.charAt(posicao)))) {
            posicao++;
            coluna++;
        }

        String lexema = input.substring(inicio, posicao);

        // Verifica se o identificador é a palavra-chave "escreva"
        if (lexema.equals("escreva")) {
            return new Token(TipoToken.ESCREVA, lexema, linha, coluna - lexema.length());
        }

        return new Token(TipoToken.IDENTIFICADOR, lexema, linha, coluna - lexema.length());
    }

    private Token reconhecerNumero() throws LexicoException {
        int inicio = posicao;
        boolean pontoEncontrado = false;

        while (posicao < input.length() &&
                (Character.isDigit(input.charAt(posicao)) ||
                        input.charAt(posicao) == '.')) {

            if (input.charAt(posicao) == '.') {
                if (pontoEncontrado) {
                    throw new LexicoException("Número com ponto decimal inválido", linha, coluna);
                }
                pontoEncontrado = true;
            }

            posicao++;
            coluna++;
        }

        String lexema = input.substring(inicio, posicao);
        return new Token(TipoToken.NUMERO, lexema, linha, coluna - lexema.length());
    }

    private Token reconhecerSimbolo() throws LexicoException {
        char c = input.charAt(posicao);
        TipoToken tipo;

        switch (c) {
            case '+': tipo = TipoToken.SOMA; break;
            case '-': tipo = TipoToken.SUBTRACAO; break;
            case '*': tipo = TipoToken.MULTIPLICACAO; break;
            case '/': tipo = TipoToken.DIVISAO; break;
            case '^': tipo = TipoToken.POTENCIA; break;
            case '=': tipo = TipoToken.ATRIBUICAO; break;
            case '(': tipo = TipoToken.PARENTESE_ESQ; break;
            case ')': tipo = TipoToken.PARENTESE_DIR; break;
            default:
                throw new LexicoException("Caractere inválido: '" + c + "'", linha, coluna);
        }

        posicao++;
        coluna++;
        return new Token(tipo, String.valueOf(c), linha, coluna - 1);
    }
}
package Sintatico;

import Lexico.Token;
import Lexico.TipoToken;
import java.util.List;

public class AnalisadorSintatico {
    private final List<Token> tokens;
    private int posicao;
    private Token atual;

    public AnalisadorSintatico(List<Token> tokens) {
        this.tokens = tokens;
        this.posicao = 0;
        this.atual = tokens.get(0);
    }

    public void analisar() throws SintaticoException {
        programa();
    }

    private void programa() throws SintaticoException {
        while (!fimDoArquivo()) {
            if (proximoTokenEh(TipoToken.ESCREVA)) {
                comandoEscreva();
            } else {
                expressao();
            }
        }
    }

    private void comandoEscreva() throws SintaticoException {
        consumir(TipoToken.ESCREVA);
        consumir(TipoToken.PARENTESE_ESQ);
        consumir(TipoToken.IDENTIFICADOR);
        consumir(TipoToken.PARENTESE_DIR);
    }

    private void expressao() throws SintaticoException {
        if (proximoTokenEh(TipoToken.IDENTIFICADOR) &&
                proximoProximoTokenEh(TipoToken.ATRIBUICAO)) {

            consumir(TipoToken.IDENTIFICADOR);
            consumir(TipoToken.ATRIBUICAO);

            expressao();

        } else {
            termo();

            while (proximoTokenEh(TipoToken.SOMA) || proximoTokenEh(TipoToken.SUBTRACAO)) {
                avancar();
                termo();
            }
        }
    }

    private void termo() throws SintaticoException {
        fator();

        while (proximoTokenEh(TipoToken.MULTIPLICACAO) ||
                proximoTokenEh(TipoToken.DIVISAO)) {
            avancar();
            fator();
        }
    }

    private void fator() throws SintaticoException {
        potencia();

        while (proximoTokenEh(TipoToken.POTENCIA)) {
            avancar();
            potencia();
        }
    }

    private void potencia() throws SintaticoException {
        if (proximoTokenEh(TipoToken.NUMERO)) {
            avancar();
        } else if (proximoTokenEh(TipoToken.IDENTIFICADOR)) {
            avancar();
        } else if (proximoTokenEh(TipoToken.PARENTESE_ESQ)) {
            avancar();
            expressao();
            consumir(TipoToken.PARENTESE_DIR);
        } else if (proximoTokenEh(TipoToken.SUBTRACAO)) {
            avancar();
            potencia();
        } else {
            throw new SintaticoException("Esperado número, identificador, '(' ou '-', encontrado: " +
                    atual.getLexema(), atual.getLinha(), atual.getColuna());
        }
    }

    private boolean proximoTokenEh(TipoToken tipo) {
        return !fimDoArquivo() && atual.getTipo() == tipo;
    }

    private boolean proximoProximoTokenEh(TipoToken tipo) {
        return posicao + 1 < tokens.size() && tokens.get(posicao + 1).getTipo() == tipo;
    }

    private void avancar() {
        posicao++;
        if (!fimDoArquivo()) {
            atual = tokens.get(posicao);
        }
    }

    private void consumir(TipoToken tipo) throws SintaticoException {
        if (proximoTokenEh(tipo)) {
            avancar();
        } else {
            throw new SintaticoException("Esperado " + tipo + ", encontrado: " +
                    atual.getLexema(), atual.getLinha(), atual.getColuna());
        }
    }

    private boolean fimDoArquivo() {
        return atual.getTipo() == TipoToken.EOF;
    }
}
package Semantico;

import java.util.HashMap;
import java.util.Map;
import Lexico.Token;

public class AnalisadorSemantico {
    private final Map<String, Double> tabelaSimbolos = new HashMap<>();

    public void verificarDeclaracao(Token id) throws SemanticoException {
        if (!tabelaSimbolos.containsKey(id.getLexema())) {
            throw new SemanticoException("Variável não declarada: " + id.getLexema(),
                    id.getLinha(), id.getColuna());
        }
    }

    public void declararVariavel(Token id, double valor) {
        tabelaSimbolos.put(id.getLexema(), valor);
    }

    public double getValor(Token id) throws SemanticoException {
        if (!tabelaSimbolos.containsKey(id.getLexema())) {
            throw new SemanticoException("Variável não declarada: " + id.getLexema(),
                    id.getLinha(), id.getColuna());
        }
        return tabelaSimbolos.get(id.getLexema());
    }

    public void atualizarVariavel(Token id, double valor) throws SemanticoException {
        if (!tabelaSimbolos.containsKey(id.getLexema())) {
            throw new SemanticoException("Variável não declarada: " + id.getLexema(),
                    id.getLinha(), id.getColuna());
        }
        tabelaSimbolos.put(id.getLexema(), valor);
    }
}
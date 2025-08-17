package Scan;

import Lexico.Token;
import Lexico.TipoToken;
import Lexico.AnalisadorLexico;
import Lexico.LexicoException;
import Sintatico.AnalisadorSintatico;
import Sintatico.SintaticoException;
import Semantico.AnalisadorSemantico;
import Semantico.SemanticoException;
import java.util.List;
import java.util.Stack;
import java.util.ArrayList;

public class Scan {
    private final AnalisadorSemantico semantico;

    public Scan() {
        this.semantico = new AnalisadorSemantico();
    }

    public Object interpretar(String expressao) throws LexicoException, SintaticoException, SemanticoException {
        // 1. Análise Léxica
        AnalisadorLexico lexico = new AnalisadorLexico(expressao);
        List<Token> tokens = lexico.analisar();

        // Se a lista de tokens estiver vazia (exceto por EOF), retorne 0.0 ou nada
        if (tokens.size() <= 1) {
            return 0.0;
        }

        // 2. Análise Sintática e Execução
        // Se o primeiro token for ESCREVA, tratamos de forma especial
        if (tokens.get(0).getTipo() == TipoToken.ESCREVA) {
            return executarEscreva(tokens);
        }

        // Para expressões normais, continua o fluxo
        AnalisadorSintatico sintatico = new AnalisadorSintatico(tokens);
        sintatico.analisar();

        // 3. Converter para RPN (Notação Polonesa Reversa)
        List<Token> rpn = converterParaRPN(tokens);

        // 4. Avaliar a expressão RPN
        return avaliarRPN(rpn);
    }

    private String executarEscreva(List<Token> tokens) throws SintaticoException, SemanticoException {
        // Validação sintática básica para o comando
        if (tokens.size() < 5 || // escreva, (, id, ), EOF
                tokens.get(1).getTipo() != TipoToken.PARENTESE_ESQ ||
                tokens.get(2).getTipo() != TipoToken.IDENTIFICADOR ||
                tokens.get(3).getTipo() != TipoToken.PARENTESE_DIR) {
            throw new SintaticoException("Sintaxe inválida para 'escreva'. Use: escreva(variavel)",
                    tokens.get(0).getLinha(), tokens.get(0).getColuna());
        }

        Token varToken = tokens.get(2);
        double valor = semantico.getValor(varToken);
        return String.valueOf(valor); // Retorna o valor como String para ser impresso
    }

    private List<Token> converterParaRPN(List<Token> tokens) throws SemanticoException {
        List<Token> output = new ArrayList<>();
        Stack<Token> operadores = new Stack<>();

        for (Token token : tokens) {
            if (token.getTipo() == TipoToken.EOF) break; // Ignora o token EOF na conversão

            switch (token.getTipo()) {
                case NUMERO:
                case IDENTIFICADOR:
                    output.add(token);
                    break;
                case PARENTESE_ESQ:
                    operadores.push(token);
                    break;
                case PARENTESE_DIR:
                    while (!operadores.isEmpty() && operadores.peek().getTipo() != TipoToken.PARENTESE_ESQ) {
                        output.add(operadores.pop());
                    }
                    if (operadores.isEmpty()) throw new SemanticoException("Parênteses não correspondentes", token.getLinha(), token.getColuna());
                    operadores.pop(); // Remove o '('
                    break;
                default: // É um operador
                    if (ehOperador(token.getTipo())) {
                        while (!operadores.isEmpty() && ehOperador(operadores.peek().getTipo())) {
                            if ( (isAssociativoEsquerda(token) && precedencia(token) <= precedencia(operadores.peek())) ||
                                    (isAssociativoDireita(token) && precedencia(token) < precedencia(operadores.peek())) ) {
                                output.add(operadores.pop());
                            } else {
                                break;
                            }
                        }
                        operadores.push(token);
                    }
                    break;
            }
        }

        while (!operadores.isEmpty()) {
            Token op = operadores.pop();
            if (op.getTipo() == TipoToken.PARENTESE_ESQ) {
                throw new SemanticoException("Parênteses não correspondentes", op.getLinha(), op.getColuna());
            }
            output.add(op);
        }

        return output;
    }

    private double avaliarRPN(List<Token> rpn) throws SemanticoException {
        Stack<Object> pilha = new Stack<>(); // Usaremos Object para guardar Doubles e Tokens de ID

        for (Token token : rpn) {
            if (token.getTipo() == TipoToken.NUMERO) {
                pilha.push(Double.parseDouble(token.getLexema()));
            } else if (token.getTipo() == TipoToken.IDENTIFICADOR) {
                pilha.push(token);
            } else if (ehOperador(token.getTipo())) {
                if (token.getTipo() == TipoToken.ATRIBUICAO) {
                    if (pilha.size() < 2) throw new SemanticoException("Atribuição inválida", token.getLinha(), token.getColuna());

                    Object valorObj = desempilharEObterValor(pilha);
                    Object varObj = pilha.pop();

                    if (!(varObj instanceof Token) || ((Token)varObj).getTipo() != TipoToken.IDENTIFICADOR) {
                        throw new SemanticoException("O lado esquerdo da atribuição deve ser uma variável", token.getLinha(), token.getColuna());
                    }

                    Token varToken = (Token) varObj;
                    double valor = (double) valorObj;

                    semantico.declararVariavel(varToken, valor);
                    pilha.push(valor); // O resultado de uma atribuição é o próprio valor
                } else {
                    if (pilha.size() < 2) throw new SemanticoException("Operador sem operandos suficientes", token.getLinha(), token.getColuna());

                    double b = (double) desempilharEObterValor(pilha);
                    double a = (double) desempilharEObterValor(pilha);
                    pilha.push(aplicarOperacao(a, b, token.getLexema()));
                }
            }
        }

        if (pilha.isEmpty()) {
            if (rpn.isEmpty()) return 0.0; // Entrada vazia
            throw new SemanticoException("Expressão mal formada resultou em pilha vazia", 1, 1);
        }

        if (pilha.size() > 1) {
            // Isso pode acontecer em casos como "x y z"
            throw new SemanticoException("Expressão mal formada, operandos extras encontrados", 1, 1);
        }

        return (double)desempilharEObterValor(pilha);
    }

    private Object desempilharEObterValor(Stack<Object> pilha) throws SemanticoException {
        Object obj = pilha.pop();
        if (obj instanceof Token) {
            return semantico.getValor((Token)obj);
        }
        return obj; // Já é um Double
    }

    private int precedencia(Token op) {
        switch (op.getTipo()) {
            case ATRIBUICAO: return 1;
            case SOMA:
            case SUBTRACAO: return 2;
            case MULTIPLICACAO:
            case DIVISAO: return 3;
            case POTENCIA: return 4;
            default: return 0;
        }
    }

    private boolean isAssociativoEsquerda(Token op) {
        return op.getTipo() != TipoToken.POTENCIA && op.getTipo() != TipoToken.ATRIBUICAO;
    }

    private boolean isAssociativoDireita(Token op) {
        return op.getTipo() == TipoToken.POTENCIA || op.getTipo() == TipoToken.ATRIBUICAO;
    }


    private double aplicarOperacao(double a, double b, String operador) {
        switch (operador) {
            case "+": return a + b;
            case "-": return a - b;
            case "*": return a * b;
            case "/":
                if (b == 0) throw new ArithmeticException("Divisão por zero");
                return a / b;
            case "^": return Math.pow(a, b);
            default: throw new IllegalArgumentException("Operador desconhecido: " + operador);
        }
    }

    private boolean ehOperador(TipoToken tipo) {
        return tipo == TipoToken.SOMA || tipo == TipoToken.SUBTRACAO ||
                tipo == TipoToken.MULTIPLICACAO || tipo == TipoToken.DIVISAO ||
                tipo == TipoToken.POTENCIA || tipo == TipoToken.ATRIBUICAO;
    }
}
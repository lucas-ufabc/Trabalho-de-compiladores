package Main;

import Scan.Scan;
import Lexico.LexicoException;
import Semantico.SemanticoException;
import Sintatico.SintaticoException;
import java.util.Scanner;

public class Main2 {

    public static void main(String[] args) {
        // Criar uma ÚNICA instância do interpretador para manter o estado (tabela de símbolos)
        Scan Scan = new Scan();

        // Criar um Scanner para ler a entrada do usuário a partir do console
        Scanner Scanner = new Scanner(System.in);

        System.out.println("==========================================");
        System.out.println("===       CALCULADORA INTERATIVA       ===");
        System.out.println("==========================================");
        System.out.println("Digite uma expressão para calcular ou 'sair' para terminar.");

        // Laço infinito para processar as entradas
        while (true) {
            System.out.print("\n> "); // Prompt para o usuário
            String expressao = Scanner.nextLine();

            // Condição de parada do laço
            if (expressao.equalsIgnoreCase("sair")) {
                break; // Sai do laço while
            }

            // Se a entrada for vazia, continua para a próxima iteração
            if (expressao.trim().isEmpty()) {
                continue;
            }

            try {
                // Tenta interpretar a expressão fornecida
                double resultado = Scan.interpretar(expressao);
                System.out.println("  Resultado: " + resultado);
            } catch (LexicoException | SintaticoException | SemanticoException | ArithmeticException e) {
                // Captura os erros esperados e imprime a mensagem formatada
                System.err.println("  ERRO: " + e.getMessage());
            } catch (Exception e) {
                // Captura qualquer outro erro inesperado para depuração
                System.err.println("  ERRO INESPERADO: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            }
        }

        // Mensagem de finalização
        System.out.println("\n==========================================");
        System.out.println("===          PROGRAMA FINALIZADO         ===");
        System.out.println("==========================================");

        // Fecha o scanner para liberar os recursos
        Scanner.close();
    }
}
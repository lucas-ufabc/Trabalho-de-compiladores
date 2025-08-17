package Main;

import Scan.Scan;
import Lexico.LexicoException;
import Semantico.SemanticoException;
import Sintatico.SintaticoException;
import java.util.ArrayList;
import java.util.List;



public class Main {

    public static void main(String[] args) {
        Scan scan = new Scan();

        System.out.println("==========================================");

        System.out.println("=== INICIANDO TESTES DO INTERPRETADOR ===");

        System.out.println("==========================================");

        List<String> expressoes = new ArrayList<>();

        expressoes.add("5 + 3 * 2");

        expressoes.add("(5 + 3) * 2");

        expressoes.add("10 / 4");

        expressoes.add("2 ^ 3 ^ 2");

        expressoes.add("pi = 3.14");

        expressoes.add("raio = 5");

        expressoes.add("area = pi * raio ^ 2");

        expressoes.add("area");

        expressoes.add("a = b = 20");

        expressoes.add("a + b");

        expressoes.add("x = (y = 2) * (z = 3) + (k = 4)");

        expressoes.add("x");

        expressoes.add("y + z + k");

        expressoes.add("variavel_inexistente + 5");

        expressoes.add("5 * (3 + )");

        expressoes.add("10 / (5 - 5)");

        expressoes.add("5 @ 2");

        for (String expr : expressoes) {

            System.out.println("\n>>> Testando expressão: \"" + expr + "\"");

            try {

                double resultado = scan.interpretar(expr);

                System.out.println(" ✅ Resultado: " + resultado);

            } catch (LexicoException | SintaticoException | SemanticoException | ArithmeticException e) {

                System.err.println(" ❌ ERRO: " + e.getMessage());

            } catch (Exception e) {

                System.err.println(" ❌ ERRO INESPERADO: " + e.getClass().getSimpleName() + " - " + e.getMessage());

            }

        }
        System.out.println("\n==========================================");

        System.out.println("=== TESTES FINALIZADOS ===");

        System.out.println("==========================================");

    }

}


# Trabalho de Compiladores - UFABC - 2025.2

**Autor** : Felipe Ultramari Domingues | RA:11202230317
**Disciplina:** Compiladores  
**Professor:** Valerio Ramos Batista

---

## 1. Descrição do Projeto

Este projeto consiste na implementação de um interpretador para uma linguagem de calculadora simples, conforme especificado na **Modalidade (1)** do Trabalho de Compiladores. O programa é capaz de processar expressões matemáticas que incluem variáveis, números decimais e operadores, respeitando a precedência matemática padrão.

O objetivo é receber uma expressão como entrada e retornar o valor calculado ou, caso a expressão seja inválida, uma mensagem de erro explicativa que aponte o local do problema (linha e coluna).

## 2. Funcionalidades

O interpretador suporta as seguintes funcionalidades:

-   **Operadores Aritméticos:** Soma (`+`), Subtração (`-`), Multiplicação (`*`), Divisão (`/`) e Potência (`^`).
-   **Precedência de Operadores:** As operações são executadas na ordem matemática correta (ex: multiplicação antes da soma).
-   **Agrupamento com Parênteses:** Permite o uso de parênteses `()` para alterar a ordem de avaliação das expressões.
-   **Variáveis:**
    -   Atribuição de valores a variáveis (ex: `x = 10`)
    -   Atribuições encadeadas e com associatividade à direita (ex: `a = b = 20`).
    -   Uso de variáveis previamente declaradas em cálculos.
-   **Números Decimais:** O analisador léxico reconhece e processa números de ponto flutuante.
-   **Tratamento de Erros:** O sistema identifica e reporta os seguintes tipos de erro:
    -   **Léxicos:** Caracteres inválidos na expressão.
    -   **Sintáticos:** Expressões mal formadas (ex: `5 * + 2`).
    -   **Semânticos:** Uso de variáveis não declaradas.
    -   **De Execução:** Divisão por zero.

## 3. Gramática da Linguagem

O analisador sintático foi construído com base na seguinte gramática livre de recursão à esquerda, adequada para um parser descendente preditivo:

```
programa           →  expressao (expressao)*

expressao          →  atribuicao
                   |  soma_sub

atribuicao         →  IDENTIFICADOR ATRIBUICAO expressao

soma_sub           →  mult_div ( (SOMA | SUBTRACAO) mult_div )*

mult_div           →  potencia ( (MULTIPLICACAO | DIVISAO) potencia )*

potencia           →  unario ( POTENCIA unario )*

unario             →  SUBTRACAO unario
                   |  valor_final

valor_final        →  NUMERO
                   |  IDENTIFICADOR
                   |  PARENTESE_ESQ expressao PARENTESE_DIR
```

## 4. Estrutura do Projeto

O código-fonte está organizado em pacotes que separam as diferentes fases da interpretação:

-   `lexico/`: Contém as classes responsáveis pela Análise Léxica (transformar o texto em tokens).
-   `sintatico/`: Responsável pela Análise Sintática (validar a estrutura da expressão).
-   `semantico/`: Gerencia a tabela de símbolos e realiza a Análise Semântica (validação do uso de variáveis).
-   `interpretador/`: Orquestra todas as fases, converte a expressão para Notação Polonesa Reversa (RPN) e calcula o resultado final.
-   `Main.java`: Classe para execução de uma suíte de testes pré-definida.
-   `Main2.java`: Classe que inicia o modo interativo da calculadora no console.

## 5. Como Compilar e Executar

**Pré-requisitos:**
* JDK (Java Development Kit) instalado.

**Passos:**

1.  Abra um terminal na pasta raiz do projeto.
2.  Compile todos os arquivos `.java` com o seguinte comando:
    ```bash
    javac */*.java *.java
    ```

3.  Execute o programa em um dos dois modos:

    **a) Modo de Testes (Main.java)**
    Executa uma série de testes automáticos e exibe os resultados.
    ```bash
    java Main
    ```

    **b) Modo Interativo (Main2.java)**
    Inicia um console interativo onde você pode digitar expressões livremente.
    ```bash
    java Main2
    ```
    - Para encerrar o modo interativo, digite `sair` e pressione Enter.



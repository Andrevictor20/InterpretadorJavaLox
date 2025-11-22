# Interpretador Java Lox

Projeto acadêmico desenvolvido na disciplina de Compiladores — Universidade Federal do Maranhão (UFMA).

Descrição: implementação em Java (parcial) do interpretador da linguagem Lox, baseada no livro "Crafting Interpreters". O projeto inclui analisador léxico, parser de expressões, geração/visualização de AST, interpretador de expressões, REPL e execução de scripts.

Autores
- André Victor Macedo Pereira — `Andrevictor20`
- Italo Matheus Rodrigues Sousa — `ItaloMatheus10`

Tecnologias & Requisitos
- `Java` 21
- `Maven` para build
- Sistema operacional compatível com Java 21 (Linux, macOS, Windows)

Propósito
- Objetivo: servir como projeto didático para estudar o pipeline básico de um interpretador: análise léxica (scanner) → parsing (parser) → Árvore de Sintaxe Abstrata (AST) → interpretação.
- Inspiração: implementação educativa da linguagem Lox, conforme o livro "Crafting Interpreters".

Como executar
- Compile o módulo:
```bash
cd analisador-lexico
mvn -DskipTests package
```
- Executar REPL:
```bash
java -cp target/analisador-lexico-1.0-SNAPSHOT.jar br.com.analisadorlexico.Lox
```
- Executar um script `.lox`:
```bash
java -cp target/analisador-lexico-1.0-SNAPSHOT.jar br.com.analisadorlexico.Lox path/to/script.lox
```
- Executar o `AstPrinter` (exemplo de visualização da AST):
```bash
java -cp target/analisador-lexico-1.0-SNAPSHOT.jar br.com.analisadorlexico.AstPrinter
```

Funcionalidades implementadas 
- Scanner (analisador léxico): tokeniza números, strings, identificadores, comentários de linha (`//`), operadores, parênteses e reconhece palavras-chave básicas.
- Parser de expressões: parsing com precedência (equality, comparison, term, factor, unary, primary).
- AST e impressão: hierarquia `Expr` e `AstPrinter` para visualizar árvores de expressão.
- Interpretador de expressões:
	- Avalia literais, agrupamentos, unary e binary.
	- Suporta operadores aritméticos (`+`, `-`, `*`, `/`), comparadores (`>`, `<`, `>=`, `<=`), igualdade (`==`, `!=`) e concatenação de strings.
	- Validação de tipos e lançamento de `RuntimeError` para operandos inválidos.
- REPL e execução de arquivo: implementados em `Lox.java`.
- Tratamento básico de erros: relatórios de erro de sintaxe e runtime com códigos de saída.

Arquitetura / Onde olhar no código
- `analisador-lexico/src/main/java/br/com/analisadorlexico/Scanner.java` — analisador léxico
- `analisador-lexico/src/main/java/br/com/analisadorlexico/Parser.java` — parser e hierarquia `Expr`
- `analisador-lexico/src/main/java/br/com/analisadorlexico/Interpreter.java` — interpretador
- `analisador-lexico/src/main/java/br/com/analisadorlexico/Lox.java` — entrypoint / REPL / runner
- `analisador-lexico/src/main/java/br/com/analisadorlexico/AstPrinter.java` — utilitário para imprimir AST
- `analisador-lexico/src/main/java/br/com/analisadorlexico/Token.java`, `TokenType.java` — tokens
- `analisador-lexico/src/main/java/br/com/analisadorlexico/RuntimeError.java` — erro de execução
- `analisador-lexico/src/main/java/br/com/analisadorlexico/ferramentas/GenerateAst.java` — gerador de classes AST (ferramenta)

Status atual
- Cobertura: parsing e interpretação de expressões — funcional.
- Implementado: scanner, parser de expressões, AST printing, interpretador de expressões, REPL.
- Faltando / Parcial:
	- Parsing/execução de statements (declarações, atribuições, `print` como statement).
	- `Environment` / gerenciamento de escopos e variáveis.
	- Funções, closures, classes e herança.
	- Testes automatizados abrangentes e exemplos `.lox`.

Limitações conhecidas
- Algumas palavras-chave são reconhecidas pelo `Scanner` mas não possuem suporte completo no `Parser`/`Interpreter`.
- Parser/Interpreter lidam principalmente com expressões — não há suporte completo para statements (`var`, `print`, `fun`, `class`, etc.).
- Pequena inconsistência observada: a função `identifier()` no `Scanner` pode adicionar um token `IDENTIFIER` duplicado — convém revisar.

Próximos passos sugeridos
1. Implementar `Environment` e parsing/execução de `var` e atribuições.
2. Adicionar suporte a statements (`print`, `if`, `while`, `for`) e tornar `synchronize()` mais robusto.
3. Suportar funções (`fun`) e chamadas, com closures.
4. Implementar classes, `this` e `super`.
5. Adicionar exemplos em `examples/` e testes automatizados em `test/`.
6. Configurar CI (GitHub Actions) que compile e execute testes.
7. Corrigir inconsistências no `Scanner` e melhorar mensagens de erro.

Como contribuir
- Fork e abra PRs para a branch `main`.
- Mantenha compatibilidade com Java 21 e use Maven para builds.








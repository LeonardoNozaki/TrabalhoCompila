/* ==========================================================================
 * Universidade Federal de São Carlos - Campus Sorocaba
 * Disciplina: Compiladores
 * Prof. Leticia Berto
 *
 * Trabalho - Análise Léxica e Sintática (Fase 1)
 *
 * Aluno: Bruno Rizzi       RA: 743515
 * Aluna: Giulia Fazzi      RA: 743542
 * Aluno: Leonardo Nozaki   RA: 743561
 * Aluna: Michele Carvalho  RA: 726573
 * ========================================================================== */

package AST;
import java.util.*;
import Lexer.*;

public class ExprUnary {

    public ExprUnary(ExprPrimary expr, Symbol symbol) {
        this.expr = expr;
        this.symbol = symbol;
    }

    public void genC(PW pw) {
        if(this.symbol != null){
            pw.out.print(symbol.toString());
        }
        this.expr.genC(pw);
    }

    public Type getType() {
      if(symbol != null){
        return Type.integerType;
      }
      return expr.getType();
    }

    public boolean isId(){
      return this.expr.isId();
    }

    public boolean isFuncCall(){
      return this.expr.isFuncCall();
    }

    private ExprPrimary expr;
    private Symbol symbol;
}

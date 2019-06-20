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

public class ExprRel {

    public ExprRel(ExprAdd left, ExprAdd right, Symbol symbol, Type type) {
        this.left = left;
        this.right = right;
        this.symbol = symbol;
        this.type = type;
    }

    public void genC(PW pw) {
        this.left.genC(pw);
        if (right != null) {
            pw.out.print(" " + symbol + " ");
            this.right.genC(pw);
        }
    }

    public Type getType() {
        return type;
    }

    public boolean isId(){
      if(this.right == null){
        return this.left.isId();
      }
      return false;
    }

    private ExprAdd left, right;
    private Symbol symbol;
    private Type type;
}

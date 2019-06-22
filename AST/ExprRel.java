/* ==========================================================================
 * Universidade Federal de São Carlos - Campus Sorocaba
 * Disciplina: Compiladores
 * Prof. Leticia Berto
 *
 * Trabalho - Análise Semântica (Fase 2)
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

    public ExprRel(ExprAdd left, ExprAdd right, Symbol symbol) {
        this.left = left;
        this.right = right;
        this.symbol = symbol;
    }

    public void genC(PW pw) {
        this.left.genC(pw);
        if (right != null) {
            pw.out.print(" " + symbol + " ");
            this.right.genC(pw);
        }
    }

    public Type getType() {
      if ( symbol == Symbol.EQ || symbol == Symbol.NEQ || symbol == Symbol.LE ||
        symbol == Symbol.LT || symbol == Symbol.GE || symbol == Symbol.GT ){
        return Type.booleanType;
      }
      if(left == null){
        return Type.undefinedType;
      }
      return this.left.getType();
    }

    public boolean isId(){
      if(this.right == null){
        return this.left.isId();
      }
      return false;
    }

    public boolean isFuncCall(){
      if(this.right == null){
        return this.left.isFuncCall();
      }
      return false;
    }

    private ExprAdd left, right;
    private Symbol symbol;
}

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

public class ExprAdd {

    public ExprAdd(ArrayList<ExprMult> expr, Symbol symbol, Type type) {
      this.expr = expr;
      this.symbol = symbol;
	    this.type = type;
    }

    public void genC(PW pw) {
        int i = 0;
        int tam = this.expr.size();
        this.expr.get(i).genC(pw);
        i++;
        while(i < tam){
            pw.out.print(" " + symbol + " ");
            this.expr.get(i).genC(pw);
            i++;
        }
    }

    public Type getType() {
        return type;
    }

    public boolean isId(){
      if(this.expr.size() == 1){
        return this.expr.get(0).isId();
      }
      return false;
    }

    private ArrayList<ExprMult> expr = new ArrayList<ExprMult>();
    private Symbol symbol;
    private Type type;
}

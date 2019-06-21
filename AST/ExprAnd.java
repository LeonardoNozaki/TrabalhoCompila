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

public class ExprAnd {

    public ExprAnd(ArrayList<ExprRel> expr) {
        this.expr = expr;
    }

    public void genC(PW pw) {
        int i = 0;
        int tam = this.expr.size();
        this.expr.get(i).genC(pw);
        i++;
        while(i < tam){
            pw.out.print(" && ");
            this.expr.get(i).genC(pw);
            i++;
        }
    }

    public Type getType() {
      if(expr == null){
        return Type.undefinedType;
      }
      int tam = this.expr.size();
      if(tam > 1){ //um ou mais operadores AND torna a expr Boolean
        return Type.booleanType;
      }
      else if(tam == 1){ //Nenhum operador AND, precisa ver o nivel de baixo
        return this.expr.get(0).getType();
      }
      return Type.undefinedType;
    }

    public boolean isId(){
      if(this.expr.size() == 1){
        return this.expr.get(0).isId();
      }
      return false;
    }

    private ArrayList<ExprRel>expr = new ArrayList<ExprRel>();
}

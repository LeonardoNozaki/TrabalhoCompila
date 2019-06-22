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

public class FuncCall extends ExprPrimary {
  public FuncCall() {} // default constructor

  public FuncCall(ArrayList<Expr> expr, String id, Type type) {
    this.expr = expr;
    this.id = id;
    this.type = type;
  }

  public void genC(PW pw) {
    pw.out.print(id + "(");
    int i = 0;
    int tam = this.expr.size();
    if(!this.expr.isEmpty()){
      this.expr.get(i).genC(pw);
      i++;
      while(i < tam){
        pw.out.print(", ");
        this.expr.get(i).genC(pw);
        i++;
      }
    }
    pw.out.print(")");
  }

  public Type getType() {
    return type;
  }

  public boolean isId(){
    return false;
  }

  public boolean isFuncCall(){
    return true;
  }

  private ArrayList<Expr> expr = new ArrayList<Expr>();
  private String id;
  private Type type;
}

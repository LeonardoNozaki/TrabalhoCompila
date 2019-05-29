package AST;

import java.util.*;

public class WhileStat extends Statement {
  public WhileStat( Expr expr, ArrayList<Statement> sl ) {
    this.expr = expr;
    this.sl = sl;
  }

  public void genC( PW pw ) {
    int i = 0;
    int tam = this.sl.size();
    pw.print("while(");
    this.expr.genC(pw);
    pw.out.println("){");
    pw.add();
    while(i < tam){
      this.sl.get(i).genC(pw);
      i++;
    }
    pw.sub();
    pw.println("}");
  }

  private Expr expr;
  private ArrayList<Statement> sl = new ArrayList<Statement>();
}

package AST;
import java.util.*;
import Lexer.*;

public class FuncCall extends ExprPrimary {
  public FuncCall(ArrayList<Expr> expr, String id) {
        this.expr = expr;
        this.id = id;
    }

    public void genC(PW pw) {
      pw.print(id + "(");
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

    private ArrayList<Expr> expr = new ArrayList<Expr>();
    private String id;

}

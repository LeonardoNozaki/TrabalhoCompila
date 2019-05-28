package AST;
import java.util.*;
import Lexer.*;

public class FuncCall extends ExprPrimary {
  public FuncCall(ArrayList<Expr> expr, String id) {
        this.expr = expr;
        this.id = id;
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
      pw.out.println(");");
    }

    private ArrayList<Expr> expr = new ArrayList<Expr>();
    private String id;

}

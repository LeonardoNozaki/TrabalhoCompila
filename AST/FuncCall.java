package AST;
import java.util.*;
import Lexer.*;

public class FuncCall extends ExprPrimary {
  public FuncCall(ArrayList<Expr> expr, String id) {
        this.expr = expr;
        this.id = id;
    }

    public void genC(PW pw) {

    }

    private ArrayList<Expr>expr = new ArrayList<Expr>();
    private String id;

}

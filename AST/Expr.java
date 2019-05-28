package AST;
import java.util.*;
import Lexer.*;

public class Expr {

    public Expr(ArrayList<ExprAnd> expr, Symbol symbol) {
        this.expr = expr;
        this.symbol = symbol;
    }

    public void genC(PW pw) {
        int i = 0;
        int tam = this.expr.size();
        this.expr.get(i).genC(pw);
        i++;
        while(i < tam){
            pw.print(" || ");
            this.expr.get(i).genC(pw);
            i++;
        }
    }

    private ArrayList<ExprAnd> expr = new ArrayList<ExprAnd>();
    private Symbol symbol;
}

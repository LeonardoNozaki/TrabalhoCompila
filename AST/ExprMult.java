package AST;
import java.util.*;
import Lexer.*;

public class ExprMult {

    public ExprMult(ArrayList<ExprUnary> expr, Symbol symbol) {
        this.expr = expr;
        this.symbol = symbol;
    }

    public void genC(PW pw) {
        int i = 0;
        int tam = this.expr.size();
        this.expr.get(i).genC(pw);
        i++;
        while(i < tam){
            pw.print(" " + symbol + " ");
            this.expr.get(i).genC(pw);
            i++;
        }
    }

    private ArrayList<ExprUnary> expr = new ArrayList<ExprUnary>();
    private Symbol symbol;
}

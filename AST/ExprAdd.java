package AST;
import java.util.*;
import Lexer.*;

public class ExprAdd {

    public ExprAdd(ArrayList<ExprMult> expr, Symbol symbol) {
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

    private ArrayList<ExprMult> expr = new ArrayList<ExprMult>();
    private Symbol symbol;
}

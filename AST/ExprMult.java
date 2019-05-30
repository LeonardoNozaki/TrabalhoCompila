package AST;
import java.util.*;
import Lexer.*;

public class ExprMult {

    public ExprMult(ArrayList<ExprUnary> expr, Symbol symbol, Type type) {
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

    private ArrayList<ExprUnary> expr = new ArrayList<ExprUnary>();
    private Symbol symbol;
    private Type type;
}

package AST;
import java.util.*;
import Lexer.*;

public class Expr {

    public Expr(ArrayList<ExprAnd> expr, Symbol symbol, Type type) {
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
            pw.out.print(" || ");
            this.expr.get(i).genC(pw);
            i++;
        }
    }

    public Type getType() {
        return type;
    }

    private ArrayList<ExprAnd> expr = new ArrayList<ExprAnd>();
    private Symbol symbol;
    private Type type;
}

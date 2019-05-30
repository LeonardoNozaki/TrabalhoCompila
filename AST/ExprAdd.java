package AST;
import java.util.*;
import Lexer.*;

public class ExprAdd {

    public ExprAdd(ArrayList<ExprMult> expr, Symbol symbol, Type type) {
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

    private ArrayList<ExprMult> expr = new ArrayList<ExprMult>();
    private Symbol symbol;
    private Type type;
}

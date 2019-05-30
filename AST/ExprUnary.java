package AST;
import java.util.*;
import Lexer.*;

public class ExprUnary {

    public ExprUnary(ExprPrimary expr, Symbol symbol, Type type) {
        this.expr = expr;
        this.symbol = symbol;
        this.type = type;
    }

    public void genC(PW pw) {
        if(this.symbol != null){
            pw.print(symbol.toString());
        }
        this.expr.genC(pw);
    }

    public Type getType() {
        return type;
    }

    private ExprPrimary expr;
    private Symbol symbol;
    private Type type;
}

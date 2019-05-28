package AST;
import java.util.*;
import Lexer.*;

public class ExprUnary {

    public ExprUnary(ExprPrimary expr, Symbol symbol) {
        this.expr = expr;
        this.symbol = symbol;
    }

    public void genC(PW pw) {
        if(this.symbol != null){
            pw.out.print(symbol);
        }
        this.expr.genC(pw);
    }

    private ExprPrimary expr;
    private Symbol symbol;
}

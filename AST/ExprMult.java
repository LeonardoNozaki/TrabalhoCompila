package AST;
import java.util.*;
import Lexer.*;

public class ExprMult {

    public ExprMult(ArrayList<ExprUnary> expr, Symbol symbol) {
        this.expr = expr;
        this.symbol = symbol;
    }

    public void genC(PW pw) {

    }

    private ArrayList<ExprUnary>expr = new ArrayList<ExprUnary>();
    private Symbol symbol;
}

package AST;
import java.util.*;
import Lexer.*;

public class Expr {

    public Expr(ArrayList<ExprAnd> expr, Symbol symbol) {
        this.expr = expr;
        this.symbol = symbol;
    }

    public void genC(PW pw) {

    }

    private ArrayList<ExprAnd>expr = new ArrayList<ExprAnd>();
    private Symbol symbol;
}

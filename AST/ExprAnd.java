package AST;
import java.util.*;
import Lexer.*;

public class ExprAnd {

    public ExprAnd(ArrayList<ExprRel> expr, Symbol symbol) {
        this.expr = expr;
        this.symbol = symbol;
    }

    public void genC(PW pw) {

    }

    private ArrayList<ExprRel>expr = new ArrayList<ExprRel>();
    private Symbol symbol;
}

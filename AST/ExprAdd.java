package AST;
import java.util.*;
import Lexer.*;

public class ExprAdd {

    public ExprAdd(ArrayList<ExprMult> expr, Symbol symbol) {
        this.expr = expr;
        this.symbol = symbol;
    }

    public void genC(PW pw) {

    }

    private ArrayList<ExprMult>expr = new ArrayList<ExprMult>();
    private Symbol symbol;
}

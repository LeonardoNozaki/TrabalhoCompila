package AST;
import java.util.*;
import Lexer.*;

public class ExprRel {

    public ExprRel(ExprAdd left, ExprAdd right, Symbol symbol, Type type) {
        this.left = left;
        this.right = right;
        this.symbol = symbol;
        this.type = type;
    }

    public void genC(PW pw) {
        this.left.genC(pw);
        if (right != null) {
            pw.out.print(" " + symbol + " ");
            this.right.genC(pw);
        }
    }

    public Type getType() {
        return type;
    }

    private ExprAdd left, right;
    private Symbol symbol;
    private Type type;
}

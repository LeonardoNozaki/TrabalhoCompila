package AST;
import java.util.*;
import Lexer.*;

public class ExprRel {

    public ExprRel(ExprAdd left, ExprAdd right, Symbol symbol) {
        this.left = left;
        this.right = right;
        this.symbol = symbol;
    }

    public void genC(PW pw) {
        this.left.genC(pw);
        if (right != null) {  // VOLTAR AQUI
            pw.print(" " + symbol + " ");
            this.right.genC(pw);
        }
    }

    private ExprAdd left, right;
    private Symbol symbol;
}

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
        left.genC(pw);

        if (right.isEmpty()) {  // VOLTAR AQUI
            for (ExprAnd e: right){
                pw.out.print(" || ");
                e.genC(pw);
            }
        }
    }

    private ExprAdd left, right;
    private Symbol symbol;
}

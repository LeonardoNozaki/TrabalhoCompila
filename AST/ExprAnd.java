package AST;
import java.util.*;
import Lexer.*;

public class ExprAnd {

    public ExprAnd(ArrayList<ExprRel> expr, Symbol symbol) {
        this.expr = expr;
        this.symbol = symbol;
    }

    public void genC(PW pw) {
        int i = 0;
        int tam = this.expr.size();
        this.expr.get(i).genC(pw);
        i++;
        while(i < tam){
            pw.out.print(" && ");
            this.expr.get(i).genC(pw);
            i++;
        }
    }

    private ArrayList<ExprRel>expr = new ArrayList<ExprRel>();
    private Symbol symbol;
}

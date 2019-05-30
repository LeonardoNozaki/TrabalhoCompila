package AST;
import java.util.*;
import Lexer.*;

public class Write extends FuncCall {
  public Write(ArrayList<Expr> expr, String id) {
        this.expr = expr;
        this.id = id;
    }

    @Override
    public void genC(PW pw) {
	int size = this.expr.size();
 
	if (size > 0) {
		if ( id.equals(Symbol.WRITE.toString()) ) {
		   pw.out.print("printf (\"");
		   int count = 0;
		   for (Expr e : expr) {
		       if (e.getType() == Type.integerType)
		            pw.out.print("%d");
		       else
			    pw.out.print("%s");
		       
		       if (count != size-1)
		       		pw.out.print(" ");

		       count++;
		   }

		   pw.out.print("\", ");
		   count = 0;
		   for (Expr e : expr) {
			e.genC(pw);
			if (count != size-1)
				pw.out.print(", ");
			count++;
		   }
		   pw.out.print(")");

		} else {  // id = writeln
		  pw.out.print("printf (\"");
		  int count = 0;
		  for (Expr e : expr) {
		       if (e.getType() == Type.integerType)
		            pw.out.print("%d");
			  else
			    pw.out.print("%s");

		       if (count != size-1)
		       		pw.out.print(" ");

		       count++;
		   }

		   pw.out.print("\\r\\n\", ");
		   count = 0;
		   for (Expr e : expr) {
			e.genC(pw);
			if (count != size-1)
				pw.out.print(", ");
			count++;
		   }
		   pw.out.print(")");

              }
	}
    }

    private ArrayList<Expr> expr = new ArrayList<Expr>();
    private String id;

}




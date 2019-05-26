package AST;

public class WhileStat extends Statement {
  public WhileStat( Expr expr, ArrayList<Statement> sl ) {
    this.expr = expr;
    this.sl = sl;
  }

  public void genC( PW pw ) {
  }

  private Expr expr;
  private ArrayList<Statement> sl = new ArrayList<Statement>();
}

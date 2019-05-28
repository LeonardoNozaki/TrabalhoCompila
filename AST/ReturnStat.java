package AST;

public class ReturnStat extends Statement {

	public ReturnStat( Expr e ) {
		this.e = e;
	}

	public void genC( PW pw ) {
		pw.print("return ");
		this.e.genC(pw);
		pw.println(";");
	}

	private Expr e;
}

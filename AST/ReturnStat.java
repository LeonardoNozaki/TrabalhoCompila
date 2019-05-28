package AST;

public class ReturnStat extends Statement {

	public ReturnStat( Expr e ) {
		this.e = e;
	}

	public void genC( PW pw ) {
		pw.out.print("return ");
		this.e.genC(pw);
		pw.out.println(";");
	}

	private Expr e;
}

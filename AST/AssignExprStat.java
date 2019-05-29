package AST;

public class AssignExprStat extends Statement {
	public AssignExprStat( Expr left, Expr right ) {
		this.left = left;
		this.right = right;
	}

	public void genC( PW pw ) {  // NÃO SEI SE ISSO TÁ CERTO
		pw.print("");
		this.left.genC(pw);
		if ( right != null ) {
			pw.out.print(" = ");
			this.right.genC(pw);
			pw.out.println(";");
		}
		else{
			pw.out.println(";");
		}
	}

	private Expr left, right;
}

package AST;
import java.util.*;

public class Program {
	public Program( ArrayList<Function> funcList ) {
		this.funcList = funcList;
	}

	public void genC( PW pw ) {
		int i = 0;
		int tam = this.funcList.size();
		this.funcList.get(i).genC(pw);
		i++;
		pw.out.print("\n");
		while(tam > 1){
			this.funcList.get(i).genC(pw);
			pw.out.print("\n");
		}
	}

	private ArrayList<Function> funcList;
}

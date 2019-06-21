package AST;
import java.io.*;

public class ReadInt extends Statement {
  public ReadInt( Variable v ) {
    this.v = v;
  }

  public void genC( PW pw ) {
    pw.print("scanf(\"%d\", &" + v.getName() + ");" );
  }

  private Variable v;
}

package AST;
import java.io.*;

public class ReadString extends Statement {
  public ReadString( Variable v ) {
    this.v = v;
  }

  public void genC( PW pw ) {
    pw.print("scanf(\"%s\", " + v.getName() + ");" );
  }

  private Variable v;
}

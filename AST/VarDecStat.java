package AST;

public class VarDecStat extends Statement {
  public VarDecStat( String name, Type type ) {
    this.name = name;
    this.type = type;
  }

  public void genC( PW pw ) {
    pw.println(this.type.getCname() + " " + this.name + ";");
  }

  private String name;
  private Type type;
}

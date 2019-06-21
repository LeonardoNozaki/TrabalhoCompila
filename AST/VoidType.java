package AST;

import java.io.*;

  public class VoidType extends Type {

  // variables that are not declared have this type
  public VoidType() { super("void"); }

  public String getCname() {
    return "void";
  }
}

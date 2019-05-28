package AST;

import java.util.*;

public class Function {
  public Function( String id, ArrayList<Variable> paramList, Type type, ArrayList<Statement> statList ) {
    this.id = id;
    this.paramList = paramList;
    this.type = type;
    this.statList = statList;
  }

  private String id;
  private ArrayList<Variable> paramList = new ArrayList<Variable>();
  private ArrayList<Statement> statList = new ArrayList<Statement>();
  private Type type;
}

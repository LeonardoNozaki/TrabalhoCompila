package AST;

import java.util.*;

public class Function {
  public Function( String id, ArrayList<Variable> paramList, Type type, ArrayList<Statment> statList ) {
    this.id = id;
    this.paramList = paramList;
    this.type = type;
    this.statList = statList;
  }

  private String id;
  private ArrayList<Variable> paramList = new ArrayList<Variable>();
  private ArrayList<Statment> statList = new ArrayList<Statment>();
  private Type type;
}

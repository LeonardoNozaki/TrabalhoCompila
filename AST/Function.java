package AST;

import java.util.*;

public class Function {
  public Function( String id, ArrayList<Variable> paramList, Type type, ArrayList<Statement> statList ) {
    this.id = id;
    this.paramList = paramList;
    this.type = type;
    this.statList = statList;
  }

  public void genC(PW pw) {
      int i = 0;
      int tam;
      if(this.type == null)
        pw.print("void ");
      else
        pw.print(this.type.getCname() + " ");

      pw.out.print(this.id);
      pw.out.print("(");
      if(paramList != null){
        tam = this.paramList.size();
        while(i < tam){
          this.paramList.get(i).genC(pw);
          i++;
          if(i < tam)
            pw.out.print(", ");
        }
      }
      pw.out.println("){");
      pw.add();
      tam = this.statList.size();
      i = 0;
      while(i < tam){
        this.statList.get(i).genC(pw);
        i++;
      }
      pw.sub();
      pw.println("}");
  }

  private String id;
  private ArrayList<Variable> paramList = new ArrayList<Variable>();
  private ArrayList<Statement> statList = new ArrayList<Statement>();
  private Type type;
}

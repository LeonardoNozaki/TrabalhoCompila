/* ==========================================================================
 * Universidade Federal de São Carlos - Campus Sorocaba
 * Disciplina: Compiladores
 * Prof. Leticia Berto
 *
 * Trabalho - Análise Semântica (Fase 2)
 *
 * Aluno: Bruno Rizzi       RA: 743515
 * Aluna: Giulia Fazzi      RA: 743542
 * Aluno: Leonardo Nozaki   RA: 743561
 * Aluna: Michele Carvalho  RA: 726573
 * ========================================================================== */

package AST;

import java.util.*;

public class Function {
  public Function( String id, ArrayList<VarDecStat> paramList, Type type, ArrayList<Statement> statList ) {
    this.id = id;
    this.paramList = paramList;
    this.type = type;
    this.statList = statList;
    if(type == null){
      this.type = Type.voidType;
    }
  }

  public Type getType(){
    return this.type;
  }

  public int getSize(){
    if(this.paramList == null){
      return 0;
    }
    return this.paramList.size();
  }

  public Type getTypeParameter(int i){
    return paramList.get(i).getType();
  }

  public void genC(PW pw) {
      int i = 0;
      int tam;
      /*if(this.type == null)
        pw.print("void ");
      else*/
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
  private ArrayList<VarDecStat> paramList = new ArrayList<VarDecStat>();
  private ArrayList<Statement> statList = new ArrayList<Statement>();
  private Type type;
}

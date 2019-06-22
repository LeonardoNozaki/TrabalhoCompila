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

package AuxComp;
import java.util.*;

public class SymbolTable {
  public SymbolTable() {
    globalTable = new Hashtable();
    localTable = new ArrayList<Hashtable>();
  }

  public void putInGlobal( String key, Object value ) {
    globalTable.put(key, value);
  }

  public void putInLocal( String key, Object value ) {
    int size = localTable.size();
    if(size > 0){
      aux = localTable.get(size-1);
      aux.put(key, value);
    }
  }

  public void add(){
    localTable.add(new Hashtable());
  }

  public void sub(){
    int size = localTable.size();
    if(size > 0){
      localTable.remove(size-1);
    }
  }

  public Object getInLocal( Object key ) {
    int size = localTable.size();
    for(int i = size-1; i >= 0; i--){
      aux = localTable.get(i);
      Object o = aux.get(key);
      if(o != null){
        return o;
      }
    }
    return null;
  }

  public Object getInGlobal( Object key ) {
    return globalTable.get(key);

  }

  public Object get( String key ) {
    // returns the object corresponding to the key.
    Object result;
    if ( (result = getInLocal(key)) != null ) {
      // found local identifier
      return result;
    }
    else {
    // global identifier, if it is in globalTable
      return globalTable.get(key);
    }
  }

  public void removeLocalIdent() {
    // remove all local identifiers from the table
    localTable.clear();
  }

  private Hashtable globalTable;
  private Hashtable aux;
  private ArrayList<Hashtable> localTable;
}

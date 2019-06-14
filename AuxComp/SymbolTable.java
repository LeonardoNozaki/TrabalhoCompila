/* ==========================================================================
 * Universidade Federal de São Carlos - Campus Sorocaba
 * Disciplina: Compiladores
 * Prof. Leticia Berto
 *
 * Trabalho - Análise Léxica e Sintática (Fase 1)
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
    localTable = new Hashtable();
  }

  public Object putInGlobal( String key, Object value ) {
    return globalTable.put(key, value);
  }

  public Object putInLocal( String key, Object value ) {
    return localTable.put(key, value);
  }

  public Object getInLocal( Object key ) {
    return localTable.get(key);
  }

  public Object getInGlobal( Object key ) {
    return globalTable.get(key);
  }

  public Object get( String key ) {
    // returns the object corresponding to the key.
    Object result;
    if ( (result = localTable.get(key)) != null ) {
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

  private Hashtable globalTable, localTable;
}

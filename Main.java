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

import AST.*;
import java.io.*;

public class Main {

  public static void main( String []args ) {

    //Criando variaveis para auxiliar
    File file;
    FileReader stream;
    int numChRead;
    Program program;

    //Verifica se foram passados 2 argumentos
    //Um arquivo de entrada e outro de saida
    if ( args.length != 2 ) {
      //Se nao for dois parametros, manda um avisa e finaliza o programa
      System.out.println("Usage:\n Main input output");
      System.out.println("input is the file to be compiled");
      System.out.println("output is the file where the generated code will be stored");

    } else {
      //Se possuir exatamente 2 argumentos

      //Usa o primeiro argumento (arquivo de entrada) como parametro de File
      file = new File(args[0]);

      //Verifica se o arquivo retornado nao existe ou se nao pode ser lido
      if ( !file.exists() || ! file.canRead() ) {
        //Nesses casos, emite um erro
        //Pois nao sera possivel ler o arquivo de entrada
        System.out.println("Either the file " + args[0] + " does not exist or it cannot be throw new RuntimeException()");
      }

      try {
        //Cria um leitor de arquivo com o arquivo de entrada
        stream = new FileReader(file);
      } catch ( FileNotFoundException e ) {
        //Lanca excecao se nao puder abrir o arquivo para leitura
        System.out.println("Something wrong: file does not exist anymore");
        throw new RuntimeException();
      }

      //Cria um "buffer" de caracteres, onde o tamanho é:
      //Todo arquivo de dados e mais um caracterer para colocar '\0'
      //o '\0' serve para indicar que nao tem mais nada para ler
      char []input = new char[ (int ) file.length() + 1 ];

      try {
        //Faz a leitura de todos os caracteres do arquivo de dados
        numChRead = stream.read( input, 0, (int ) file.length() );
      } catch ( IOException e ) {
        //Lanca excecao se nao puder ler o arquivo
        System.out.println("Error reading file " + args[0]);
        throw new RuntimeException();
      }

      //Verifica se o numero de caracter lidos é igual
      //ao numero de caractere existentes no arquivo de entrada
      if ( numChRead != file.length() ) {
        //Se nao for igual, gera uma excecao
        System.out.println("Read error");
        throw new RuntimeException();
      }

      try {
        //Fecha o arquivo de entrada
        stream.close();
      } catch ( IOException e ) {
        //Lanca excecao se nao puder fechar o arquivo
        System.out.println("Error in handling the file " + args[0]);
        throw new RuntimeException();
      }

      //Cria uma instancia da classe Compiler
      //Essa instancia sera usada para analisar o array input
      Compiler compiler = new Compiler();
      FileOutputStream outputStream;

      try {
        //Cria uma instancia da classe FileOutputStream
        //Utiliza o segundo argumento informado (arquivo de saida)
        outputStream = new FileOutputStream(args[1]);
      } catch ( IOException e ) {
        //Laca excecao se nao puder criar a instancia
        System.out.println("File " + args[1] + " could not be opened for writing");
        throw new RuntimeException();
      }

      //Cria uma instancia da classe PrintWriter
      //Essa instancia sera usada para fazer a saida do programa
      PrintWriter printWriter = new PrintWriter(outputStream);

      //Deixa a variavel program da classe Program como null,
      //antes de tentar compilar, se continuar null, nao gera o arquivo em C
      program = null;

      //O codigo gerado ou os erros vao para o arquivo de saida
      try {
        //Chama o metodo compile,
        //passando todo o programa de entrada num array de caracter
        //passando onde sera escrito os erros
        String arquivo = args[0];
        String nomeFinal = "";
        for(int i = 0; i < arquivo.length(); i++){
          if(arquivo.charAt(i) == '/'){
            nomeFinal = "";
          }
          else{
            nomeFinal += arquivo.charAt(i);
          }
        }

        program = compiler.compile(input, printWriter, nomeFinal);
      } catch ( RuntimeException e ) {
        System.out.println(e);
      }

      //Verifica se o retorno do compile for diferente de null
      if ( program != null && compiler.getC() ) {
        //Cria uma instancia da classe Pw
        PW pw = new PW();

        //Define o PrintWriter que usa o arquivo do argumento, como saida
        pw.set(printWriter);

        //Chama o gerador de codigo para C
        program.genC(pw);

        //Verifica se houve algum erro na escrita da saida
        if ( printWriter.checkError() ) {
          System.out.println("There was an error in the output");
        }
        else{
          System.out.println("Program compiled successfully, check " + args[1] + " to see it");
        }
      }
      else{
        System.out.println("");
        System.out.println(compiler.getNumErrors() + " errors found in " + args[0]);
      }
    }
  }
}

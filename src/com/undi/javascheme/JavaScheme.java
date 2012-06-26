package com.undi.javascheme;

//import java.io.ByteArrayInputStream;
//import java.io.InputStream;

public class JavaScheme {

  /**
   * @param args
   */
  public static void main(String[] args) {
    //SchemeObject obj = SchemeObject.makeString("Hello");
    //System.out.println(obj.getString());
    //System.out.println(obj.getCharacter());
    
    //InputStream in = new ByteArrayInputStream("   ;this is a comment\nfoo".getBytes());
    //reader.eatWhitespace();
    //System.out.println(reader.readToken());
    //System.out.println(Character.isWhitespace('\n'));
    System.out.println("Welcome to java-scheme, press Ctrl-C to exit");
    SchemeReader reader = new SchemeReader(System.in);
    SchemeEval evaluator = new SchemeEval();
    //REPL loop
    while(true){
      System.out.print("> ");
      System.out.println(evaluator.eval(reader.read(), null));
    }

  }

}

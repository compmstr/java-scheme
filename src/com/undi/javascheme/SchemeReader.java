package com.undi.javascheme;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

public class SchemeReader {
  private PushbackInputStream mBufIn;
  //read() returns -1 on end of stream
  private static final int EOF = -1;
  //Allow up to 10 characters to be pushed back into the buffer
  private static final int PUSHBACK_SIZE = 10;
  
  public SchemeReader(InputStream in){
    this.mBufIn = new PushbackInputStream(in, PUSHBACK_SIZE);
  }
  
  public boolean isDelimiter(int c){
    return Character.isWhitespace(c) ||
                      c == '(' || c == ')' ||
                      c == '"' || c == ';';
  }
  
  /**
   * Reads until the next delimiter
   * @return
   */
  public String readToken(){
    int c;
    StringBuilder theToken = new StringBuilder();
    eatWhitespace();
    do{
      c = getc();
      theToken.append((char) c);
    }while(!isDelimiter(c));
    theToken.deleteCharAt(theToken.length() - 1);
    return theToken.toString();
  }
  
  /**
   * Returns the next character from the stream, not advancing the read pointer
   * @return the next character
   */
  public int peek(){
    int c = getc();
    //Reset by skipping 0 characters since mark
    ungetc(c);
    return c;
  }
  
  /**
   * Returns the next character from the stream.
   * Handles any IOExceptions dealing with that
   * @return the next character
   */
  public int getc(){
    try{
      return mBufIn.read();
    }catch (IOException e){
      e.printStackTrace(System.err);
      System.exit(1);
    }
    return -1;
  }
  public void ungetc(int c){
    try{
      mBufIn.unread(c);
    }catch (IOException e){
      e.printStackTrace(System.err);
      System.exit(1);
    }
  }
  
  public void eatWhitespace(){
    int c;
    while((c = getc()) != EOF){
      if(Character.isWhitespace((char)c)){
        continue;
      }else if(c == ';'){
        while(((c = getc()) != EOF) && (c != '\n'));
        continue;
      }
      ungetc(c);
      break;
    }
  }
  
  public SchemeObject readString(){
    StringBuilder tempString = new StringBuilder();
    eatWhitespace();
    int newChar = -1;
getString:
    while(true){
     newChar = getc();
     if(newChar == '\\'){
       newChar = getc();
       switch(newChar){
       case 'n':
         tempString.append('\n');
         break;
       case '"':
         tempString.append('"');
         break;
       case '\\':
         tempString.append('\\');
         break;
       }
     }else if(newChar == '"'){
       break getString;
     }else{
       tempString.append((char)newChar);
     }
    }
    return SchemeObject.makeString(tempString.toString());
  }
  
  public SchemeObject readNumber(){
    StringBuilder tmp = new StringBuilder();
    eatWhitespace();
    short sign = 1;
    if(peek() == '-'){
      getc();
      sign = -1;
    }
    while(Character.isDigit(peek())){
      tmp.append((char)getc());
    }
    if(!isDelimiter(peek())){
      System.err.println("Number not followed by delimiter");
      System.exit(1);
    }
    String numString = tmp.toString();
    return SchemeObject.makeNumber(sign * Double.valueOf(numString));
  }
  
  public SchemeObject readCharacter(){
    StringBuilder tmp = new StringBuilder();
    eatWhitespace();
    while(!Character.isWhitespace(peek())){
      tmp.append((char)getc());
    }
    String charString = tmp.toString();
    char[] charArray = charString.toCharArray();
    char toInsert;
    if(charString.length() > 2){
      if(charString.equals("\\newline")){
        toInsert = '\n';
      }else if(charString.equals("\\space")){
        toInsert = ' ';
      }else{
        toInsert = '\0';
        System.err.println("Invalid Character");
        System.exit(1);
      }
    }else{
      toInsert = charArray[1];
    }
    return SchemeObject.makeCharacter((short)toInsert);
  }
  
  public SchemeObject readBoolean(){
    //char[] boolString = readToken().toCharArray();
    eatWhitespace();
    char boolChar = (char)getc();
    boolean retVal;
    if(boolChar == 't'){
      retVal = true;
    }else if(boolChar == 'f'){
      retVal = false;
    }else{
      retVal = false;
      System.err.println("Invalid boolean: " + boolChar);
      System.exit(1);
    }
    return SchemeObject.makeBoolean(retVal);
  }
  
  public SchemeObject readPair(){
    eatWhitespace();
    int c = getc();
    if(c == ')'){
      return SchemeObject.EmptyList;
    }
    ungetc(c);
    SchemeObject carObject = read();
    
    SchemeObject cdrObject = null;
    eatWhitespace();
    c = getc();
    if(c == '.'){
      //take in the improper list form
      c = peek();
      if(!isDelimiter(peek())){
        System.err.println("Error: dot not followed by delimiter");
        System.exit(1);
      }
      cdrObject = read();
      eatWhitespace();
      c = getc();
      if(c != ')'){
        System.err.println("Error: no trailing right paren on dot form");
        System.exit(1);
      }
    }else{
      ungetc(c);
      cdrObject = readPair();
    }
    
    return SchemeObject.cons(carObject, cdrObject);
  }
  
  public SchemeObject read(){
    switch(nextType()){
    case NUMBER:
      return readNumber();
    case STRING:
      return readString();
    case CHARACTER:
      return readCharacter();
    case BOOLEAN:
      return readBoolean();
    case PAIR:
      return readPair();
    case EMPTY_LIST:
      getc();
      return SchemeObject.EmptyList;
    }
    System.err.println("Unsupported input type in read");
    System.exit(1);
    return new SchemeObject();
  }
  
  /**
   * Determines what the next SchemeObject type will be from the start of the stream
   * @return
   */
  private SchemeObject.type nextType(){
    SchemeObject.type retType = null;
    eatWhitespace();
    //char[] c = readToken().toCharArray();
    char c = (char)getc();
    char next_char;
    if(c == '"'){
       retType = SchemeObject.type.STRING;
    }else if(Character.isDigit(c) ||
        (c == '-' && Character.isDigit(peek()))){
      ungetc(c);
      return SchemeObject.type.NUMBER;
    }else if(c == '#'){
      next_char = (char)peek();
      switch(next_char){
      case '\\':
        return SchemeObject.type.CHARACTER;
      case 't':
      case 'f':
        return SchemeObject.type.BOOLEAN;
      default:
        System.err.println("Unkown boolean or character literal");
        System.exit(1);
      }
    }else if(c == '('){
      if(peek() == ')'){
        return SchemeObject.type.EMPTY_LIST;
      }else{
        retType = SchemeObject.type.PAIR;
      }
    }
    
    if(retType == null){
      System.err.println("Unsupported type read");
      System.exit(1);
    }
    return retType;
  }
}

package com.undi.javascheme;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SchemeReader {
  private BufferedInputStream mBufIn;
  private static final int MARK_LIMIT = 1024;
  //read() returns -1 on end of stream
  private static final int EOF = -1;
  
  public SchemeReader(InputStream in){
    this.mBufIn = new BufferedInputStream(in);
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
    mBufIn.mark(MARK_LIMIT);
    StringBuilder theToken = new StringBuilder();
    eatWhitespace();
    do{
      c = getc();
      theToken.append((char) c);
    }while(!isDelimiter(c));
    skipNChars(theToken.length() - 1);
    theToken.deleteCharAt(theToken.length() - 1);
    return theToken.toString();
  }
  
  /**
   * Returns the next character from the stream, not advancing the read pointer
   * @return the next character
   */
  public int peek(){
    mBufIn.mark(MARK_LIMIT);
    int c = getc();
    //Reset by skipping 0 characters since mark
    skipNChars(0);
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
  
  /**
   * Skips n characters since last mark.
   * Handles IOExceptions from reset or skip
   * @param n Number of characters to skip
   */
  private void skipNChars(long n){
    try{
       mBufIn.reset();
       mBufIn.skip(n);
    }catch (IOException e){
      e.printStackTrace(System.err);
      System.exit(1);
    }
  }
  
  public void eatWhitespace(){
    mBufIn.mark(MARK_LIMIT);
    int toSkip = 0;
    int c = -1;
    while(true){
      c = getc();
      if(c == EOF){
        System.out.println("Reached EOF");
        break;
      }
      if(!Character.isWhitespace(c)){
        if(c == ';'){
          //Comments are counted as whitespace
          while(((c = getc()) != EOF) && c != '\n');
          //We want to start at 0 at the next iteration
          toSkip = -1;
          mBufIn.mark(MARK_LIMIT);
        }
        break;
      }
      //Increment toSkip, checking if we've reached mark_limit
      toSkip++;
      if(toSkip >= MARK_LIMIT){
        toSkip = 0;
        skipNChars(MARK_LIMIT);
        mBufIn.mark(MARK_LIMIT);
      }
    }
    skipNChars(toSkip);
  }
  
  public SchemeObject readString(){
    StringBuilder tempString = new StringBuilder();
    eatWhitespace();
    //Grab the first "
    getc();
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
    while(!isDelimiter(peek())){
      tmp.append((char)getc());
    }
    String numString = tmp.toString();
    return SchemeObject.makeNumber(Double.valueOf(numString));
  }
  
  //TODO: reading quotes doesn't work
  public SchemeObject readCharacter(){
    StringBuilder tmp = new StringBuilder();
    eatWhitespace();
    while(!isDelimiter(peek())){
      tmp.append((char)getc());
    }
    String charString = tmp.toString();
    char[] charArray = charString.toCharArray();
    char toInsert;
    if(charString.length() > 3){
      if(charString.equals("#\\newline")){
        toInsert = '\n';
      }else if(charString.equals("#\\space")){
        toInsert = ' ';
      }else{
        toInsert = '\0';
        System.err.println("Invalid Character");
        System.exit(1);
      }
    }else{
      toInsert = charArray[2];
    }
    return SchemeObject.makeCharacter((short)toInsert);
  }
  
  public SchemeObject readBoolean(){
    //char[] boolString = readToken().toCharArray();
    //Get the '#'
    eatWhitespace();
    getc();
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
  
  //TODO: There is an issue with reading nested lists as a last item
  //      ex: ((1 2) 3) works, but not (1 (2 3))
  //          (1 (2 3) 4) doesn't work either
  public SchemeObject readPair(){
    //Get initial '('
    eatWhitespace();
    if(peek() == '('){
      getc(); 
    }
    if(peek() == ')'){
      getc();
      return SchemeObject.makeEmptyList();
    }
    SchemeObject carObject = read();
    
    SchemeObject cdrObject = null;
    eatWhitespace();
    if(peek() == '.'){
      //take in the improper list form
      getc();
      if(!isDelimiter(peek())){
        System.err.println("Error: dot not followed by delimiter");
        System.exit(1);
      }
      eatWhitespace();
      cdrObject = read();
      eatWhitespace();
      if(peek() != ')'){
        System.err.println("Error: no trailing right paren on dot form");
        System.exit(1);
      }
      getc();
    }else{
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
    mBufIn.mark(MARK_LIMIT);
    eatWhitespace();
    //char[] nextToken = readToken().toCharArray();
    char nextToken = (char)getc();
    if(nextToken == '"'){
       retType = SchemeObject.type.STRING;
    }else if(Character.isDigit(nextToken) ||
        (nextToken == '-' && Character.isDigit(getc()))){
      retType = SchemeObject.type.NUMBER;
    }else if(nextToken == '#'){
      nextToken = (char)getc();
      if(nextToken == '\\'){
        retType = SchemeObject.type.CHARACTER;
      }else if(nextToken == 't' || nextToken == 'f'){
        retType = SchemeObject.type.BOOLEAN;
      }
    }else if(nextToken == '('){
      retType = SchemeObject.type.PAIR;
    }
    
    skipNChars(0);
    if(retType == null){
      System.err.println("Unsupported type read");
      System.exit(1);
    }
    return retType;
  }
}

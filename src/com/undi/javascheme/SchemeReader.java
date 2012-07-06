package com.undi.javascheme;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

public class SchemeReader {
  private PushbackInputStream mBufIn;
  //Allow up to 10 characters to be pushed back into the buffer
  private static final int PUSHBACK_SIZE = 10;
  private ReadFunc[] readFuncs;
  
  private interface ReadFunc{
    SchemeObject read();
  }
  
  public static SchemeReader fromString(String in){
    return new SchemeReader(new ByteArrayInputStream(in.getBytes()));
  }
  
  public SchemeReader(InputStream in){
    this.mBufIn = new PushbackInputStream(in, PUSHBACK_SIZE);
    this.readFuncs = new ReadFunc[SchemeObject.type.NUM_TYPES.ordinal()];

    this.readFuncs[SchemeObject.type.BOOLEAN.ordinal()] = new ReadFunc(){
      public SchemeObject read(){ return readBoolean();} 
    };
    this.readFuncs[SchemeObject.type.NUMBER.ordinal()] = new ReadFunc(){
      public SchemeObject read(){ return readNumber();} 
    };
    this.readFuncs[SchemeObject.type.CHARACTER.ordinal()] = new ReadFunc(){
      public SchemeObject read(){ return readCharacter();} 
    };
    this.readFuncs[SchemeObject.type.STRING.ordinal()] = new ReadFunc(){
      public SchemeObject read(){ return readString();} 
    };
    this.readFuncs[SchemeObject.type.SYMBOL.ordinal()] = new ReadFunc(){
      public SchemeObject read(){ return readSymbol();} 
    };
    this.readFuncs[SchemeObject.type.PAIR.ordinal()] = new ReadFunc(){
      public SchemeObject read(){ return readPair();} 
    };
    this.readFuncs[SchemeObject.type.EMPTY_LIST.ordinal()] = new ReadFunc(){
      public SchemeObject read(){
        //clean up trailing ')'
        getc();
        return SchemeObject.EmptyList;
        } 
    };
    
  }
  
  public void closeStream(){
    try {
      mBufIn.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  public boolean isEOF(char c){
    //the stream returns -1 at end of stream, then 255 for every char after that
    return (((short)c == -1) || ((short)c == 255));
  }
  
  public boolean isDelimiter(int c){
    return Character.isWhitespace(c) ||
                      c == '(' || c == ')' ||
                      c == '"' || c == ';' || isEOF((char)c);
  }
  
  /**
   * Returns whether c is a possible initial character for a symbol
   * @param c - character to check
   * @return
   */
  public boolean isInitial(int c){
    return Character.isLetter(c) || c == '*' || c == '/' || c == '>'||
             c == '<' || c == '=' || c == '?' || c == '!' || c == '.';
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
    while(!isEOF((char)(c = getc()))){
      if(Character.isWhitespace((char)c)){
        continue;
      }else if(c == ';'){
        while((!isEOF((char)(c = getc()))) && (c != '\n'));
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
  
  /**
   * Checks to make sure whatToEat is next in the input buffer
   *   if so, consumes that string, otherwise exits with an error
   * @param whatToEat
   */
  public void eatExpectedString(String whatToEat){
    char[] str = whatToEat.toCharArray();
    int index = 0;
    int c;
    while(index < str.length){
      c = getc();
      if(c != str[index]){
        System.err.println("Error, unexpected character");
        System.exit(1);
      }
      index++;
    }
  }
  
  /**
   * Checks to make sure a delimiter is next in our input buffer
   * If not, exits the program
   */
  public void peekExpectedDelimiter(){
    if(!isDelimiter(peek())){
      System.err.println("Delimiter not found in peekExpectedDelimiter");
      System.exit(1);
    }
  }
  
  public SchemeObject readCharacter(){
    int c;
    c = getc();
    if(c != '\\'){
      System.err.println("Invalid character, no \\");
      System.exit(1);
    }
    c = getc();
    if(isEOF((char)c)){
      System.err.println("Incomplete Character Literal");
      System.exit(1);
      return null;
    }
    switch(c){
    case 's':
      if(peek() == 'p'){
        eatExpectedString("pace");
        peekExpectedDelimiter();
        return SchemeObject.makeCharacter((short) ' ');
      }
      break;
    case 'n':
      if(peek() == 'e'){
        eatExpectedString("ewline");
        peekExpectedDelimiter();
        return SchemeObject.makeCharacter((short) '\n');
      }
      break;
    }
    peekExpectedDelimiter();
    return SchemeObject.makeCharacter((short) c);
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
  
  public SchemeObject readSymbol(){
    StringBuilder buffer = new StringBuilder();
    
    int c = getc();
    while(isInitial(c) || Character.isDigit(c) ||
        c == '+' || c == '-' || c == '.' || c == '/' || c == '_'){
      buffer.append((char)c);
      c = getc();
    }
    if(isDelimiter(c)){
      ungetc(c);
      return SchemeObject.makeSymbol(buffer.toString());
    }else{
      System.err.println("Symbol not followed by delimiter");
      System.exit(1);
      return null;
    }
  }
  
  /**
   * 
   * @return the object read, or null if end of file is reached
   */
  public SchemeObject read(){
    boolean quoted = false;
    eatWhitespace();
    if(peek() == '\''){
      getc();
      quoted = true;
    }
    SchemeObject.type nextType = nextType();
    if(nextType == null){
      //End of file reached, return null
      return null;
    }
    if(this.readFuncs[nextType.ordinal()] != null){
      if(quoted){
        return SchemeObject.cons(SchemeObject.QuoteSymbol, SchemeObject.cons(this.readFuncs[nextType.ordinal()].read(), SchemeObject.EmptyList));
      }else{
        return this.readFuncs[nextType.ordinal()].read();
      }
    }

    System.err.println("Unsupported input type in read");
    System.exit(1);
    return null;
  }
  
  /**
   * Determines what the next SchemeObject type will be from the start of the stream
   * @return the next type of objec to read, or null if end of file
   */
  private SchemeObject.type nextType(){
    SchemeObject.type retType = null;
    eatWhitespace();
    //char[] c = readToken().toCharArray();
    char c = (char)getc();
    if(isEOF(c)){
      return null;
    }
    char next_char;
    if(c == '"'){
       retType = SchemeObject.type.STRING;
    }else if(Character.isDigit(c) ||
        (c == '-' && Character.isDigit(peek()))){
      ungetc(c);
      return SchemeObject.type.NUMBER;
    }else if(isInitial(c) ||
              ((c == '+' || c == '-') && isDelimiter(peek()))){
      ungetc(c);
      return SchemeObject.type.SYMBOL;
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

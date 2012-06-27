package com.undi.javascheme;

//This class works almost like a union
public class SchemeObject {
  public static enum type {NUMBER, BOOLEAN, CHARACTER, STRING, SYMBOL, PAIR, EMPTY_LIST,
                            NATIVE_PROC, COMPOUND_PROC, NUM_TYPES};
  public type getType(){return this.mType;}
  
  public static final SchemeObject True = SchemeObject.createBoolean(true);
  public static final SchemeObject False = SchemeObject.createBoolean(false);
  public static final SchemeObject EmptyList = SchemeObject.createEmptyList();
  
  public static SchemeObject SymbolTable = SchemeObject.EmptyList;
  public static final SchemeObject QuoteSymbol = SchemeObject.makeSymbol("quote");
  public static final SchemeObject DefineSymbol = SchemeObject.makeSymbol("define");
  public static final SchemeObject SetSymbol = SchemeObject.makeSymbol("set!");
  public static final SchemeObject OkSymbol = SchemeObject.makeSymbol("ok");
  public static final SchemeObject IfSymbol = SchemeObject.makeSymbol("if");
  public static final SchemeObject LambdaSymbol = SchemeObject.makeSymbol("lambda");
  
  
  //TODO: This doesn't work
  public boolean valueEqual(SchemeObject other){
    if(other.mType == this.mType){
      if(other.mData == this.mData){
        return true;
      }
    }
    return false;
  }
  
  //Compound Proc
  public static SchemeObject makeCompoundProc(SchemeObject params, SchemeObject body, SchemeObject env){
    SchemeObject obj = new SchemeObject();
    obj.mType = type.COMPOUND_PROC;
    SchemeObject[] newData = new SchemeObject[3];
    newData[0] = params;
    newData[1] = body;
    newData[2] = env;
    obj.mData = newData;
    return obj;
  }
  public SchemeObject getCompoundProcParams(){
    if(this.mType != type.COMPOUND_PROC){
      System.err.println("Object Isn't a Compound Proc!");
      System.exit(1);
    }
    SchemeObject[] data = (SchemeObject[])this.mData;
    return data[0];
  }
  public SchemeObject getCompoundProcBody(){
    if(this.mType != type.COMPOUND_PROC){
      System.err.println("Object Isn't a Compound Proc!");
      System.exit(1);
    }
    SchemeObject[] data = (SchemeObject[])this.mData;
    return data[1];
  }
  public SchemeObject getCompoundProcEnv(){
    if(this.mType != type.COMPOUND_PROC){
      System.err.println("Object Isn't a Compound Proc!");
      System.exit(1);
    }
    SchemeObject[] data = (SchemeObject[])this.mData;
    return data[2];
  }
  public boolean isCompoundProc(){
    return this.mType == type.COMPOUND_PROC;
  }
  
  //Native Proc
  public static SchemeObject makeNativeProc(SchemeNatives.nativeProc proc){
    SchemeObject obj = new SchemeObject();
    obj.mType = type.NATIVE_PROC;
    obj.mData = proc;
    return obj;
  }
  public boolean isNativeProc(){
    return this.mType == type.NATIVE_PROC;
  }
  public SchemeNatives.nativeProc getNativeProc(){
    if(this.mType != type.NATIVE_PROC){
      System.err.println("Object Isn't a Native Proc!");
      System.exit(1);
    }
    return (SchemeNatives.nativeProc)this.mData;
  }
  
  //Pairs
  public static SchemeObject makePair(SchemeObject car, SchemeObject cdr){
    if(car == null && cdr == null){
      return EmptyList;
    }
    SchemeObject obj = new SchemeObject();
    obj.initPair();
    obj.setCar(car);
    obj.setCdr(cdr);
    return obj;
  }
  public boolean isPair(){
    return this.mType == type.PAIR;
  }
  private void initPair(){
    this.mType = type.PAIR;
    this.mData = new SchemeObject[2];
  }
  public void setCar(SchemeObject car){
    SchemeObject[] data = (SchemeObject[])this.mData;
    data[0] = car;
  }
  public void setCdr(SchemeObject cdr){
    SchemeObject[] data = (SchemeObject[])this.mData;
    data[1] = cdr;
  }
  public SchemeObject getCar(){
    if(this.mType == type.PAIR){
      return ((SchemeObject[])this.mData)[0];
    }else{
      return this;
    }
  }
  public SchemeObject getCdr(){
    if(this.mType == type.PAIR){
      return ((SchemeObject[])this.mData)[1];
    }else{
      return EmptyList;
    }
  }
  public static SchemeObject car(SchemeObject obj){
    return obj.getCar();
  }
  public static SchemeObject cdr(SchemeObject obj){
    return obj.getCdr();
  }
  public static SchemeObject caar(SchemeObject obj){ return car(car(obj)); }
  public static SchemeObject cadr(SchemeObject obj){ return car(cdr(obj)); }
  public static SchemeObject cdar(SchemeObject obj){ return cdr(car(obj)); }
  public static SchemeObject cddr(SchemeObject obj){ return cdr(cdr(obj)); }
  public static SchemeObject caddr(SchemeObject obj){ return car(cdr(cdr(obj))); }
  public static SchemeObject cdadr(SchemeObject obj){ return cdr(car(cdr(obj))); }
  public static SchemeObject caadr(SchemeObject obj){ return car(car(cdr(obj))); }
  public static SchemeObject cadddr(SchemeObject obj){ return car(cdr(cdr(cdr(obj)))); }
  public static SchemeObject caaddr(SchemeObject obj){ return car(car(cdr(cdr(obj)))); }
  public SchemeObject[] getPair(){
    if(this.mType != type.PAIR){
      System.err.println("Object Isn't a Pair!");
      System.exit(1);
    }
    return (SchemeObject[])this.mData;
  }
  public static SchemeObject cons(SchemeObject car, SchemeObject cdr){
    return SchemeObject.makePair(car, cdr);
  }
  
  public static boolean isFalse(SchemeObject obj){
    return obj == False;
  }
  
  public static boolean isTrue(SchemeObject obj){
    return !isFalse(obj);
  }
  
  /**
   * Prints a list, modifies input tempString
   * @param tempString
   */
  private void writePair(StringBuilder tempString){
    SchemeObject[] pair = getPair();
    SchemeObject carObj = pair[0];
    SchemeObject cdrObj = pair[1];
    tempString.append(carObj);
    if(cdrObj.mType == type.PAIR){
      tempString.append(' ');
      cdrObj.writePair(tempString);
    }else if(cdrObj.mType == type.EMPTY_LIST){
      return;
    }else{
      tempString.append(" . ");
      tempString.append(cdrObj);
    }
  }
  
  //Numbers
  public static SchemeObject makeNumber(double value){
    SchemeObject obj = new SchemeObject();
    obj.setNumber(value);
    return obj;
  }
  public boolean isNumber(){
    return this.mType == type.NUMBER;
  }
  public double getNumber(){
    if(this.mType != type.NUMBER){
      System.err.println("Object Isn't a Number!");
      System.exit(1);
    }
    return (Double)this.mData;
  }
  public void setNumber(double value){
    this.mType = type.NUMBER;
    this.mData = value;
  }
  
  //Booleans
  private static SchemeObject createBoolean(boolean value){
    SchemeObject obj = new SchemeObject();
    obj.setBoolean(value);
    return obj;
  }
  public boolean isBoolean(){
    return this.mType == type.BOOLEAN;
  }
  public static SchemeObject makeBoolean(boolean value){
    return (value) ? SchemeObject.True : SchemeObject.False;
  }
  private void setBoolean(boolean value){
    this.mType = type.BOOLEAN;
    this.mData = value;
  }
  public boolean getBoolean(){
    if(this.mType != type.BOOLEAN){
      System.err.println("Object Isn't a Boolean!");
      System.exit(1);
    }
    return (Boolean)this.mData;
  }
  
  //Characters
  public static SchemeObject makeCharacter(short value){
    SchemeObject obj = new SchemeObject();
    obj.setCharacter(value);
    return obj;
  }
  public boolean isCharacter(){
    return this.mType == type.CHARACTER;
  }
  public short getCharacter(){
    if(this.mType != type.CHARACTER){
      System.err.println("Object Isn't a Character!");
      System.exit(1);
    }
    return (Short)this.mData;
  }
  public void setCharacter(short value){
    this.mType = type.CHARACTER;
    this.mData = value;
  }
  
  //Strings
  public static SchemeObject makeString(String value){
    SchemeObject obj = new SchemeObject();
    obj.setString(value);
    return obj;
  }
  public boolean isString(){
    return this.mType == type.STRING;
  }
  public char[] getString(){
    if(this.mType != type.STRING){
      System.err.println("Object Isn't a String!");
      System.exit(1);
    }
    return (char[])this.mData;
  }
  public void setString(String value){
    this.mType = type.STRING;
    this.mData = value.toCharArray();
  }
  
  //Empty list
  private static SchemeObject createEmptyList(){
    SchemeObject obj = new SchemeObject();
    obj.mData = null;
    obj.mType = type.EMPTY_LIST;
    return obj;
  }
  public static SchemeObject makeEmptyList(){
    return SchemeObject.EmptyList;
  }
  public boolean isEmptyList(){
    return this.mType == type.EMPTY_LIST;
  }

  //Symbols
  public static SchemeObject makeSymbol(String value){
    //See if this symbol is in the symbol table
    SchemeObject elt = SymbolTable;
    while(!elt.isEmptyList()){
      if(car(elt).getSymbol().equals(value)){
        return car(elt);
      }
      elt = cdr(elt);
    }
    SchemeObject obj = new SchemeObject();
    obj.setSymbol(value);
    SymbolTable = cons(obj, SymbolTable);
    return obj;
  }
  public boolean isSymbol(){
    return this.mType == type.SYMBOL;
  }
  public String getSymbol(){
    if(this.mType != type.SYMBOL){
      System.err.println("Object Isn't a Symbol!");
      System.exit(1);
    }
    return (String)this.mData;
  }
  public void setSymbol(String value){
    this.mType = type.SYMBOL;
    this.mData = value;
  }
  
  /**
   * Returns the object as a string
   * **Writer in repl**
   */
  @Override
  public String toString(){
    StringBuilder tempString = new StringBuilder();
    //tempString.append("Type: " + this.mType.name());
    
    //tempString.append(" Value: ");
    switch(this.mType){
    case NUMBER:
      tempString.append(getNumber());
      break;
    case STRING:
      tempString.append('"');
      tempString.append(getString());
      tempString.append('"');
      break;
    case CHARACTER:
      tempString.append((char)getCharacter());
      break;
    case BOOLEAN:
      tempString.append(getBoolean() ? "true" : "false");
      break;
    case SYMBOL:
      tempString.append(getSymbol());
      break;
    case PAIR:
      tempString.append('(');
      this.writePair(tempString);
      tempString.append(')');
      break;
    case EMPTY_LIST:
      tempString.append("()");
      break;
    case COMPOUND_PROC:
    case NATIVE_PROC:
      tempString.append("#<native procedure>");
      break;
    }
    
    //tempString.append(">");
    return tempString.toString();
  }
  
  private type mType;
  private Object mData;
}

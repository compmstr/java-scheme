package com.undi.javascheme;

//This class works almost like a union
public class SchemeObject {
  public static enum type {NUMBER, BOOLEAN, CHARACTER, STRING, SYMBOL, PAIR, EMPTY_LIST};
  public type getType(){return this.mType;}
  
  public static final SchemeObject True = SchemeObject.createBoolean(true);
  public static final SchemeObject False = SchemeObject.createBoolean(false);
  public static final SchemeObject EmptyList = SchemeObject.createEmptyList();
  
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
    return ((SchemeObject[])this.mData)[0];
  }
  public SchemeObject getCdr(){
    return ((SchemeObject[])this.mData)[1];
  }
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
    SchemeObject obj = new SchemeObject();
    obj.setSymbol(value);
    return obj;
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
    case PAIR:
      tempString.append('(');
      this.writePair(tempString);
      tempString.append(')');
    }
    
    //tempString.append(">");
    return tempString.toString();
  }
  
  private type mType;
  private Object mData;
}

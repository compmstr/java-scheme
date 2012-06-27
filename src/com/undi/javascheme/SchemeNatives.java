package com.undi.javascheme;

public class SchemeNatives {
  //Native Procedures:
  
  public interface nativeProc{
    SchemeObject call(SchemeObject args);
  }
  
  //Math:
  public static final SchemeObject add = SchemeObject.makeNativeProc(new nativeProc(){
    @Override
    public SchemeObject call(SchemeObject args) {
      double result = 0;
      while(!args.isEmptyList()){
        result += args.getCar().getNumber();
        args = args.getCdr();
      }
      return SchemeObject.makeNumber(result);
    }    
  });
  public static final SchemeObject sub = SchemeObject.makeNativeProc(new nativeProc(){
    @Override
    public SchemeObject call(SchemeObject args) {
      double result = args.getCar().getNumber();
      args = args.getCdr();
      while(!args.isEmptyList()){
        result -= args.getCar().getNumber();
        args = args.getCdr();
      }
      return SchemeObject.makeNumber(result);
    }
  });
  public static final SchemeObject mult = SchemeObject.makeNativeProc(new nativeProc(){
    @Override
    public SchemeObject call(SchemeObject args) {
      double result = 1;
      while(!args.isEmptyList()){
        result *= args.getCar().getNumber();
        args = args.getCdr();
      }
      return SchemeObject.makeNumber(result);
    }
  });
  public static final SchemeObject quotient = SchemeObject.makeNativeProc(new nativeProc(){
    @Override
    public SchemeObject call(SchemeObject args) {
      long result = (long)args.getCar().getNumber();
      args = args.getCdr();
      while(!args.isEmptyList()){
        result /= args.getCar().getNumber();
        args = args.getCdr();
      }
      return SchemeObject.makeNumber(result);
    }
  });
  
  public static final SchemeObject div = SchemeObject.makeNativeProc(new nativeProc(){
    @Override
    public SchemeObject call(SchemeObject args) {
      double result = args.getCar().getNumber();
      args = args.getCdr();
      while(!args.isEmptyList()){
        result /= args.getCar().getNumber();
        args = args.getCdr();
      }
      return SchemeObject.makeNumber(result);
    }
  });
  
  public static final SchemeObject mod = SchemeObject.makeNativeProc(new nativeProc(){
    @Override
    public SchemeObject call(SchemeObject args) {
      long result = (long)args.getCar().getNumber();
      long remainder = 0;
      args = args.getCdr();
      while(!args.isEmptyList()){
        remainder = (long) (result % args.getCar().getNumber());
        result /= args.getCar().getNumber();
        args = args.getCdr();
      }
      return SchemeObject.makeNumber(remainder);
    }
  });
  
  //Numbers:
  public static final SchemeObject numEql = SchemeObject.makeNativeProc(new nativeProc(){
    @Override
    public SchemeObject call(SchemeObject args) {
      long firstNum = (long)args.getCar().getNumber();
      args = args.getCdr();
      while(!args.isEmptyList()){
        if(firstNum != (long)args.getCar().getNumber()){
          return SchemeObject.False;
        }
        args = args.getCdr();
      }
      return SchemeObject.True;
    }
  });
  public static final SchemeObject greaterThan = SchemeObject.makeNativeProc(new nativeProc(){
    @Override
    public SchemeObject call(SchemeObject args) {
      long firstNum = (long)args.getCar().getNumber();
      args = args.getCdr();
      while(!args.isEmptyList()){
        if(firstNum < (long)args.getCar().getNumber()){
          return SchemeObject.False;
        }
        args = args.getCdr();
      }
      return SchemeObject.True;
    }
  });
  public static final SchemeObject lessThan = SchemeObject.makeNativeProc(new nativeProc(){
    @Override
    public SchemeObject call(SchemeObject args) {
      long firstNum = (long)args.getCar().getNumber();
      args = args.getCdr();
      while(!args.isEmptyList()){
        if(firstNum > (long)args.getCar().getNumber()){
          return SchemeObject.False;
        }
        args = args.getCdr();
      }
      return SchemeObject.True;
    }
  });
  //List ops
  public static final SchemeObject cons = SchemeObject.makeNativeProc(new nativeProc(){
    @Override
    public SchemeObject call(SchemeObject args) {
      return SchemeObject.cons(args.getCar(), SchemeObject.cadr(args));
    }
  });
  public static final SchemeObject car = SchemeObject.makeNativeProc(new nativeProc(){
    @Override
    public SchemeObject call(SchemeObject args) {
      return SchemeObject.caar(args);
    }
  });
  public static final SchemeObject cdr = SchemeObject.makeNativeProc(new nativeProc(){
    @Override
    public SchemeObject call(SchemeObject args) {
      return SchemeObject.cdar(args);
    }
  });
  public static final SchemeObject setCar = SchemeObject.makeNativeProc(new nativeProc(){
    @Override
    public SchemeObject call(SchemeObject args) {
      args.getCar().setCar(SchemeObject.cadr(args));
      return SchemeObject.OkSymbol;
    }
  });
  public static final SchemeObject setCdr = SchemeObject.makeNativeProc(new nativeProc(){
    @Override
    public SchemeObject call(SchemeObject args) {
      args.getCar().setCdr(SchemeObject.cadr(args));
      return SchemeObject.OkSymbol;
    }
  });
  public static final SchemeObject list = SchemeObject.makeNativeProc(new nativeProc(){
    @Override
    public SchemeObject call(SchemeObject args) {
      return SchemeObject.cons(args.getCar(), args.getCdr());
    }
  });
  public static final SchemeObject nullp = SchemeObject.makeNativeProc(new nativeProc(){
    @Override
    public SchemeObject call(SchemeObject args) {
      return (args.getCar().isEmptyList()) ? SchemeObject.True : SchemeObject.False;
    }
  });
  public static final SchemeObject booleanp = SchemeObject.makeNativeProc(new nativeProc(){
    @Override
    public SchemeObject call(SchemeObject args) {
      return (args.getCar().isBoolean()) ? SchemeObject.True : SchemeObject.False;
    }
  });
  public static final SchemeObject numberp = SchemeObject.makeNativeProc(new nativeProc(){
    @Override
    public SchemeObject call(SchemeObject args) {
      return (args.getCar().isNumber()) ? SchemeObject.True : SchemeObject.False;
    }
  });
  public static final SchemeObject symbolp = SchemeObject.makeNativeProc(new nativeProc(){
    @Override
    public SchemeObject call(SchemeObject args) {
      return (args.getCar().isSymbol()) ? SchemeObject.True : SchemeObject.False;
    }
  });
  public static final SchemeObject stringp = SchemeObject.makeNativeProc(new nativeProc(){
    @Override
    public SchemeObject call(SchemeObject args) {
      return (args.getCar().isString()) ? SchemeObject.True : SchemeObject.False;
    }
  });
  public static final SchemeObject pairp = SchemeObject.makeNativeProc(new nativeProc(){
    @Override
    public SchemeObject call(SchemeObject args) {
      return (args.getCar().isPair()) ? SchemeObject.True : SchemeObject.False;
    }
  });
  public static final SchemeObject procedurep = SchemeObject.makeNativeProc(new nativeProc(){
    @Override
    public SchemeObject call(SchemeObject args) {
      return (args.getCar().isNativeProc()) ? SchemeObject.True : SchemeObject.False;
    }
  });
  public static final SchemeObject characterp = SchemeObject.makeNativeProc(new nativeProc(){
    @Override
    public SchemeObject call(SchemeObject args) {
      return (args.getCar().isCharacter()) ? SchemeObject.True : SchemeObject.False;
    }
  });
  public static final SchemeObject numberToString = SchemeObject.makeNativeProc(new nativeProc(){
    @Override
    public SchemeObject call(SchemeObject args) {
      return SchemeObject.makeString(String.valueOf(args.getCar().getNumber()));
    }
  });
  public static final SchemeObject stringToNumber = SchemeObject.makeNativeProc(new nativeProc(){
    @Override
    public SchemeObject call(SchemeObject args) {
      return SchemeObject.makeNumber(Double.valueOf(new String(args.getCar().getString())));
    }
  });
  public static final SchemeObject stringToSymbol = SchemeObject.makeNativeProc(new nativeProc(){
    @Override
    public SchemeObject call(SchemeObject args) {
      return SchemeObject.makeSymbol(new String(args.getCar().getString()));
    }
  });
  public static final SchemeObject symbolToString = SchemeObject.makeNativeProc(new nativeProc(){
    @Override
    public SchemeObject call(SchemeObject args) {
      return SchemeObject.makeString(args.getCar().getSymbol());
    }
  });
  public static final SchemeObject numberToCharacter = SchemeObject.makeNativeProc(new nativeProc(){
    @Override
    public SchemeObject call(SchemeObject args) {
      return SchemeObject.makeCharacter((short)args.getCar().getNumber());
    }
  });
  public static final SchemeObject characterToNumber = SchemeObject.makeNativeProc(new nativeProc(){
    @Override
    public SchemeObject call(SchemeObject args) {
      return SchemeObject.makeNumber((short)args.getCar().getCharacter());
    }
  });
  public static final SchemeObject eqp = SchemeObject.makeNativeProc(new nativeProc(){
    @Override
    public SchemeObject call(SchemeObject args) {
      return (args.valueEqual(args.getCar())) ? SchemeObject.True : SchemeObject.False;
    }
  });
}
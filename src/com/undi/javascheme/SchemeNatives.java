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
  public static final SchemeObject sqrt = SchemeObject.makeNativeProc(new nativeProc(){
    @Override
    public SchemeObject call(SchemeObject args) {
      double result = Math.sqrt(args.getCar().getNumber());
      return SchemeObject.makeNumber(result);
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
      double curNum = (double)args.getCar().getNumber();
      args = args.getCdr();
      while(!args.isEmptyList()){
        if(curNum <= (double)args.getCar().getNumber()){
          return SchemeObject.False;
        }
        curNum = (double)args.getCar().getNumber();
        args = args.getCdr();
      }
      return SchemeObject.True;
    }
  });
  public static final SchemeObject greaterThanEqual = SchemeObject.makeNativeProc(new nativeProc(){
    @Override
    public SchemeObject call(SchemeObject args) {
      double curNum = (double)args.getCar().getNumber();
      args = args.getCdr();
      while(!args.isEmptyList()){
        if(curNum < (double)args.getCar().getNumber()){
          return SchemeObject.False;
        }
        curNum = (double)args.getCar().getNumber();
        args = args.getCdr();
      }
      return SchemeObject.True;
    }
  });
  
  public static final SchemeObject lessThan = SchemeObject.makeNativeProc(new nativeProc(){
    @Override
    public SchemeObject call(SchemeObject args) {
      double curNum = (double)args.getCar().getNumber();
      args = args.getCdr();
      while(!args.isEmptyList()){
        if(curNum >= (double)args.getCar().getNumber()){
          return SchemeObject.False;
        }
        curNum = (double)args.getCar().getNumber();
        args = args.getCdr();
      }
      return SchemeObject.True;
    }
  });
  public static final SchemeObject lessThanEqual = SchemeObject.makeNativeProc(new nativeProc(){
    @Override
    public SchemeObject call(SchemeObject args) {
      double curNum = (double)args.getCar().getNumber();
      args = args.getCdr();
      while(!args.isEmptyList()){
        if(curNum > (double)args.getCar().getNumber()){
          return SchemeObject.False;
        }
        curNum = (double)args.getCar().getNumber();
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
      SchemeObject a = SchemeObject.car(args);
      SchemeObject b = SchemeObject.cadr(args);
      return (a.valueEqual(b)) ? SchemeObject.True : SchemeObject.False;
    }
  });
  public static final SchemeObject print = SchemeObject.makeNativeProc(new nativeProc(){
    @Override
    public SchemeObject call(SchemeObject args) {
      SchemeObject carObj = args.getCar();
      System.out.print(carObj);
      if(args.getCdr().isEmptyList()){
        System.out.print('\n');
        return SchemeObject.OkSymbol;
      }else{
        return call(args.getCdr());
      }
    }
  });
  public static final SchemeObject concat = SchemeObject.makeNativeProc(new nativeProc(){
    @Override
    public SchemeObject call(SchemeObject args) {
      SchemeObject a = SchemeObject.car(args);
      SchemeObject b = SchemeObject.cadr(args);
      return SchemeObject.concatList(a, b);
    }
  });
  
  public static final SchemeObject vector = SchemeObject.makeNativeProc(new nativeProc(){
    @Override
    public SchemeObject call(SchemeObject args) {
      return SchemeObject.makeVector(args);
    }
  });
  
  public static final SchemeObject vectorRef = SchemeObject.makeNativeProc(new nativeProc(){
    @Override
    public SchemeObject call(SchemeObject args) {
      SchemeObject vector = SchemeObject.car(args);
      int index = (int)SchemeObject.cadr(args).getNumber();
      return vector.getVector().get(index);
    }
  });
  
  public static final SchemeObject vectorSet = SchemeObject.makeNativeProc(new nativeProc(){
    @Override
    public SchemeObject call(SchemeObject args) {
      SchemeObject vector = SchemeObject.car(args);
      int index = (int)SchemeObject.cadr(args).getNumber();
      SchemeObject value = SchemeObject.caddr(args);
      vector.getVector().set(index, value);
      return vector;
    }
  });
  
  public static final SchemeObject vectorFill = SchemeObject.makeNativeProc(new nativeProc(){
    @Override
    public SchemeObject call(SchemeObject args) {
      SchemeObject vector = SchemeObject.car(args);
      SchemeObject value = SchemeObject.cadr(args);
      for(int i = 0; i < vector.getVector().size(); i++){
        vector.getVector().set(i, value);
      }
      return vector;
    }
  });
  
  public static final SchemeObject vectorLength = SchemeObject.makeNativeProc(new nativeProc(){
    @Override
    public SchemeObject call(SchemeObject args) {
      SchemeObject vector = SchemeObject.car(args);
      return SchemeObject.makeNumber(vector.getVector().size());
    }
  });
  
  public static final SchemeObject vectorToList = SchemeObject.makeNativeProc(new nativeProc(){
    @Override
    public SchemeObject call(SchemeObject args) {
      SchemeObject vector = SchemeObject.car(args);
      SchemeObject list = SchemeObject.EmptyList;
      for(int i = vector.getVector().size() - 1; i >= 0; i--){
        list = SchemeObject.cons(vector.getVector().get(i), list);
      }
      return list;
    }
  });
  
  public static final SchemeObject listToVector = SchemeObject.makeNativeProc(new nativeProc(){
    @Override
    public SchemeObject call(SchemeObject args) {
      SchemeObject list = SchemeObject.car(args);
      SchemeObject vector = SchemeObject.makeVector(SchemeObject.EmptyList);
      while(!list.isEmptyList()){
        vector.addToVector(list.getCar());
        list = list.getCdr();
      }
      return vector;
    }
  });
  
  public static final SchemeObject makeVector = SchemeObject.makeNativeProc(new nativeProc(){
    @Override
    public SchemeObject call(SchemeObject args) {
      SchemeObject list = SchemeObject.EmptyList;
      long count = (long)SchemeObject.car(args).getNumber();
      for(int i = 0; i < count; i++){
        list = SchemeObject.cons(SchemeObject.cadr(args), list);
      }
      return SchemeObject.makeVector(list);
    }
  });
}

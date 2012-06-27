package com.undi.javascheme;

public class SchemeEval {
  
  private static final SchemeObject EmptyEnvironment = SchemeObject.EmptyList;
  private SchemeObject GlobalEnvironment;
  
  public SchemeObject getGlobalEnv(){
    return this.GlobalEnvironment;
  }
  public boolean isSelfEvaluating(SchemeObject obj){
    return obj.isString() || obj.isBoolean() || obj.isNumber() || obj.isCharacter();
  }
  
  public SchemeEval(){
    this.GlobalEnvironment = this.setupEnvironment();
    //Set up native procedures
    defineVariable(SchemeObject.makeSymbol("+"), 
        SchemeNatives.add, 
        this.GlobalEnvironment);
    defineVariable(SchemeObject.makeSymbol("-"), 
        SchemeNatives.sub, 
        this.GlobalEnvironment);
    defineVariable(SchemeObject.makeSymbol("*"), 
        SchemeNatives.mult, 
        this.GlobalEnvironment);
    defineVariable(SchemeObject.makeSymbol("quotient"), 
        SchemeNatives.quotient, 
        this.GlobalEnvironment);
    defineVariable(SchemeObject.makeSymbol("/"), 
        SchemeNatives.div, 
        this.GlobalEnvironment);
    defineVariable(SchemeObject.makeSymbol("remainder"), 
        SchemeNatives.mod, 
        this.GlobalEnvironment);
    defineVariable(SchemeObject.makeSymbol("="), 
        SchemeNatives.numEql, 
        this.GlobalEnvironment);
    defineVariable(SchemeObject.makeSymbol(">"), 
        SchemeNatives.greaterThan, 
        this.GlobalEnvironment);
    defineVariable(SchemeObject.makeSymbol("<"), 
        SchemeNatives.lessThan, 
        this.GlobalEnvironment);
    defineVariable(SchemeObject.makeSymbol("cons"), 
        SchemeNatives.cons, 
        this.GlobalEnvironment);
    defineVariable(SchemeObject.makeSymbol("car"), 
        SchemeNatives.car, 
        this.GlobalEnvironment);
    defineVariable(SchemeObject.makeSymbol("cdr"), 
        SchemeNatives.cdr, 
        this.GlobalEnvironment);
    defineVariable(SchemeObject.makeSymbol("set-car!"), 
        SchemeNatives.setCar, 
        this.GlobalEnvironment);
    defineVariable(SchemeObject.makeSymbol("set-cdr!"), 
        SchemeNatives.setCdr, 
        this.GlobalEnvironment);
    defineVariable(SchemeObject.makeSymbol("list"), 
        SchemeNatives.list, 
        this.GlobalEnvironment);
    defineVariable(SchemeObject.makeSymbol("null?"), 
        SchemeNatives.nullp, 
        this.GlobalEnvironment);
    defineVariable(SchemeObject.makeSymbol("boolean?"), 
        SchemeNatives.booleanp, 
        this.GlobalEnvironment);
    defineVariable(SchemeObject.makeSymbol("symbol?"), 
        SchemeNatives.symbolp, 
        this.GlobalEnvironment);
    defineVariable(SchemeObject.makeSymbol("number?"), 
        SchemeNatives.numberp, 
        this.GlobalEnvironment);
    defineVariable(SchemeObject.makeSymbol("character?"), 
        SchemeNatives.characterp, 
        this.GlobalEnvironment);
    defineVariable(SchemeObject.makeSymbol("string?"), 
        SchemeNatives.stringp, 
        this.GlobalEnvironment);
    defineVariable(SchemeObject.makeSymbol("pair?"), 
        SchemeNatives.pairp, 
        this.GlobalEnvironment);
    defineVariable(SchemeObject.makeSymbol("procedure?"), 
        SchemeNatives.procedurep, 
        this.GlobalEnvironment);
  }
  
  public boolean isTaggedList(SchemeObject obj, SchemeObject tag){
    if(obj.isPair()){
      SchemeObject car = obj.getCar();
        return (car.isSymbol() && (car == tag));
    }
    return false;
  }
  
  public boolean isQuoted(SchemeObject obj){
    return isTaggedList(obj, SchemeObject.QuoteSymbol);
  }
  
  public SchemeObject quoteContents(SchemeObject exp){
    return SchemeObject.cadr(exp);
  }
  
  public boolean isVariable(SchemeObject exp){
    return exp.isSymbol();
  }
  
  public boolean isAssignment(SchemeObject exp){
    return isTaggedList(exp, SchemeObject.SetSymbol);
  }
  public SchemeObject assignmentVariable(SchemeObject exp){
    return SchemeObject.cadr(exp);
  }
  public SchemeObject assignmentValue(SchemeObject exp){
    return SchemeObject.caddr(exp);
  }
  
  public boolean isDefinition(SchemeObject exp){
    return isTaggedList(exp, SchemeObject.DefineSymbol);
  }
  public SchemeObject definitionVariable(SchemeObject exp){
    return SchemeObject.cadr(exp);
  }
  public SchemeObject definitionValue(SchemeObject exp){
    return SchemeObject.caddr(exp);
  }
  
  public SchemeObject evalAssignment(SchemeObject exp, SchemeObject env){
    setVariableValue(assignmentVariable(exp), eval(assignmentValue(exp), env), env);
    return SchemeObject.OkSymbol;
  }
  public SchemeObject evalDefinition(SchemeObject exp, SchemeObject env){
    defineVariable(definitionVariable(exp), eval(definitionValue(exp), env), env);
    return SchemeObject.OkSymbol;
  }
  
  public boolean isIf(SchemeObject exp){
    return isTaggedList(exp, SchemeObject.IfSymbol);
  }
  public SchemeObject ifPredicate(SchemeObject exp){
    return SchemeObject.cadr(exp);
  }
  public SchemeObject ifThen(SchemeObject exp){
    return SchemeObject.caddr(exp);
  }
  public SchemeObject ifElse(SchemeObject exp){
    if(SchemeObject.cadddr(exp).isEmptyList()){
      return SchemeObject.False;
    }else{
      return SchemeObject.cadddr(exp);
    }
  }
  
  public boolean isApplication(SchemeObject exp){
    return exp.isPair();
  }
  public SchemeObject operator(SchemeObject exp){
    return exp.getCar();
  }
  public SchemeObject operands(SchemeObject exp){
    return exp.getCdr();
  }
  
  public boolean isNoOperands(SchemeObject ops){
    return ops.isEmptyList();
  }
  public SchemeObject firstOperand(SchemeObject ops){
    return ops.getCar();
  }
  public SchemeObject restOperands(SchemeObject ops){
    return ops.getCdr();
  }
  
  public SchemeObject listOfValues(SchemeObject exps, SchemeObject env){
    if(isNoOperands(exps)){
      return SchemeObject.EmptyList;
    }else{
      return SchemeObject.cons(eval(firstOperand(exps), env),
                                listOfValues(restOperands(exps), env));
    }
  }
  
  //Environment stuff
  public SchemeObject enclosingEnvironment(SchemeObject env){
    return SchemeObject.cdr(env);
  }
  public SchemeObject firstFrame(SchemeObject env){
    return SchemeObject.car(env);
  }
  public SchemeObject makeFrame(SchemeObject vars, SchemeObject vals){
    return SchemeObject.cons(vars, vals);
  }
  
  public SchemeObject frameVars(SchemeObject frame){
    return SchemeObject.car(frame);
  }
  public SchemeObject frameVals(SchemeObject frame){
    return SchemeObject.cdr(frame);
  }
  
  public void addBindingToFrame(SchemeObject var, SchemeObject val, SchemeObject frame){
    frame.setCar(SchemeObject.cons(var, frame.getCar()));
    frame.setCdr(SchemeObject.cons(val, frame.getCdr()));
  }
  
  public SchemeObject extendEnvironment(SchemeObject vars, SchemeObject vals,
                                  SchemeObject base_env){
    return SchemeObject.cons(makeFrame(vars, vals), base_env);
  }
  
  public SchemeObject lookupVariableValue(SchemeObject var, SchemeObject env){
    SchemeObject frame;
    SchemeObject vars;
    SchemeObject vals;
    while(!env.isEmptyList()){
      frame = firstFrame(env);
      vars = frameVars(frame);
      vals = frameVals(frame);
      while(!vars.isEmptyList()){
        if(var == vars.getCar()){
          return vals.getCar();
        }
        vars = vars.getCdr();
        vals = vals.getCdr();
      }
      env = enclosingEnvironment(env);
    }
    System.err.println("Unbound Variable: " + var.getSymbol());
    System.exit(1);
    return null;
  }
  
  public void setVariableValue(SchemeObject var, SchemeObject val, SchemeObject env){
    SchemeObject frame;
    SchemeObject vars;
    SchemeObject vals;
    while(!env.isEmptyList()){
      frame = firstFrame(env);
      vars = frameVars(frame);
      vals = frameVals(frame);
      while(!vars.isEmptyList()){
        if(var == vars.getCar()){
          vals.setCar(val);
          return;
        }
        vars = vars.getCdr();
        vals = vals.getCdr();
      }
      env = enclosingEnvironment(env);
    }
    System.err.println("Unbound Variable: " + var.getSymbol());
    System.exit(1);
  }
  
  public void defineVariable(SchemeObject var, SchemeObject val, SchemeObject env){
    SchemeObject frame;
    SchemeObject vars;
    SchemeObject vals;
    
    frame = firstFrame(env);
    vars = frameVars(frame);
    vals = frameVals(frame);
    while(!vars.isEmptyList()){
      if(var == vars.getCar()){
        vals.setCar(val);
        return;
      }
      vars = vars.getCdr();
      vals = vals.getCdr();
    }
    addBindingToFrame(var, val, frame);
  }
  
  public SchemeObject setupEnvironment(){
    SchemeObject initialEnv;
    initialEnv = extendEnvironment(SchemeObject.EmptyList,
                                      SchemeObject.EmptyList, 
                                      EmptyEnvironment);
    
    return initialEnv;
  }
  
  public SchemeObject eval(SchemeObject exp, SchemeObject env){
    SchemeObject procedure;
    SchemeObject arguments;
    TAILCALL:
      while(true){
        if(isSelfEvaluating(exp)){
          return exp;
        }else if(isQuoted(exp)){
          return quoteContents(exp);
        }else if(isDefinition(exp)){
          return evalDefinition(exp, env);
        }else if(isAssignment(exp)){
          return evalAssignment(exp, env);
        }else if(isVariable(exp)){
          return lookupVariableValue(exp, env);
        }else if(isIf(exp)){
          exp = SchemeObject.isTrue(eval(ifPredicate(exp), env))?
              ifThen(exp) :
                ifElse(exp);
              //equiv: goto TAILCALL
              continue TAILCALL;
        }else if(isApplication(exp)){
          procedure = eval(operator(exp), env);
          arguments = listOfValues(operands(exp), env);
          return procedure.getNativeProc().call(arguments);
        }else{
          System.err.println("Unsupported expression type");
          System.exit(0);
        }
        //Only want to run the while loop once
        break;
      }
  System.err.println("Illegal eval state");
  System.exit(0);
  return null;
  }
}

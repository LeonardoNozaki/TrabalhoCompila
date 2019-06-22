JAVA_COMP=javac

all:
	$(JAVA_COMP) *.java
	$(JAVA_COMP) AST/BooleanType.java AST/Type.java AST/IntegerType.java AST/StringType.java AST/VoidType.java AST/UndefinedType.java


clean:
	$(RM) *.class

package AST;
import java.util.*;
import Lexer.*;

abstract public class ExprPrimary {
    abstract public void genC( PW pw );
    abstract public Type getType();
}

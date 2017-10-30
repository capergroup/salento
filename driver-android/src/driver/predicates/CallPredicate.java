/* Author: Vijay Murali */

package driver.predicates;

import driver.*;

import soot.jimple.Stmt;
import soot.SootMethod;

import soot.jimple.Stmt;
import soot.jimple.InvokeExpr;

/** A predicate that enables/disables on a particular API call */
public class CallPredicate implements Predicate {

    private String method;

    public CallPredicate(String method) {
        this.method = method;
    }

    public static void apply(Stmt stmt) {
    }

    public boolean enabled(StmtInstance stmtIns) {
        Stmt stmt = stmtIns.getStmt();
        if (!stmt.containsInvokeExpr())
            return false;

        InvokeExpr invokeExpr = stmt.getInvokeExpr();
        SootMethod callee = invokeExpr.getMethod();

        return Util.mySignature(callee).equals(method);
    }
}
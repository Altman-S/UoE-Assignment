package ed.inf.adbs.minibase.operator;

import ed.inf.adbs.minibase.base.Term;

import java.util.List;
import java.util.Map;

/**
 * An abstract class for 4 different Operators: ScanOperator, SelectOperator, ProjectOperator and JoinOperator
 *
 * @author Pai
 * @version 2022.03.12
 */
public abstract class Operator {

    public abstract Tuple getNextTuple();

    public abstract void reset();

    public abstract void dump();

    public abstract List<Map<Term,Term>> getTupleMapList();
}

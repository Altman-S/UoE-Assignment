1. Explanation of my logic for extracting join conditions from the body of a query

I put all comparisonAtom into a List to generate the comparisonAtomList, and put all relationalAtom in the body into a List to generate the relationalAtomList.

There are three situations when we extract join conditions from the body of a query:

The first situation is that there is an explicit condition. For example, Q(x, t) :- R(x, y, z), S(k, w, t), x = k, there is a join condition in comparisonAtom.
I set the first relationalAtom (R1) in the relationalAtomList to leftChild and iterate over the rest of the relationalAtom (R2) in the relationalAtomList. Go through the comparisonAtom in the comparisonAtomList. There are two terms in the comparisonAtom. If one Term is in R1 and the other Term is in R2, set R2 to rightChild. After that, we use joinOperator to process leftChild and rightChild.

The second situation is that there is an implicit condition. For example, Q(x, t) :- R(x, y, z), S(x, w, t), both of R and S have Term x, so we can apply join operator to them.
I set the first relationalAtom (R1) in the relationalAtomList to leftChild and iterate over the rest of the relationalAtom (R2) in the relationalAtomList. If List<Term> in R2 has the same Term as List<Term> in R1, set R2 to rightChild. After that, we use joinOperator to process leftChild and rightChild.

The third situation is that we cannot find an explicit or implicit condition to get leftChild and rightChild. For example, Q(x, t) :- R(x, y, z), S(u, w, t), w > 3, no explicit and implicit join condition.
In such situation, we just set the first relationalAtom in relationalAtomList as R1 and the second as R2. Then, we use joinOperator to process them.




2. Additional information

To recursively process the joinOperator, I replace the leftChild in the joinOperator with the leftTupleMapList, which is the output from the leftChild. Apply leftTupleMapList and rightChild to joinOperator to generate the output, which is a new leftTupleMapList that can be used in the next joinOperator. So, my joinOperator looks like this: 

JoinOperator(List<Map<Term,Term>> leftTupleMapList, RelationalAtom rightChild, List<ComparisonAtom> comparisonAtomList, String dbFile)

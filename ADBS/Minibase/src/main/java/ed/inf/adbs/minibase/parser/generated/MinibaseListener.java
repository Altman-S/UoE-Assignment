// Generated from Minibase.g4 by ANTLR 4.9.2
package ed.inf.adbs.minibase.parser.generated;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link MinibaseParser}.
 */
public interface MinibaseListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link MinibaseParser#query}.
	 * @param ctx the parse tree
	 */
	void enterQuery(MinibaseParser.QueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link MinibaseParser#query}.
	 * @param ctx the parse tree
	 */
	void exitQuery(MinibaseParser.QueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link MinibaseParser#head}.
	 * @param ctx the parse tree
	 */
	void enterHead(MinibaseParser.HeadContext ctx);
	/**
	 * Exit a parse tree produced by {@link MinibaseParser#head}.
	 * @param ctx the parse tree
	 */
	void exitHead(MinibaseParser.HeadContext ctx);
	/**
	 * Enter a parse tree produced by {@link MinibaseParser#sumagg}.
	 * @param ctx the parse tree
	 */
	void enterSumagg(MinibaseParser.SumaggContext ctx);
	/**
	 * Exit a parse tree produced by {@link MinibaseParser#sumagg}.
	 * @param ctx the parse tree
	 */
	void exitSumagg(MinibaseParser.SumaggContext ctx);
	/**
	 * Enter a parse tree produced by {@link MinibaseParser#avgagg}.
	 * @param ctx the parse tree
	 */
	void enterAvgagg(MinibaseParser.AvgaggContext ctx);
	/**
	 * Exit a parse tree produced by {@link MinibaseParser#avgagg}.
	 * @param ctx the parse tree
	 */
	void exitAvgagg(MinibaseParser.AvgaggContext ctx);
	/**
	 * Enter a parse tree produced by {@link MinibaseParser#body}.
	 * @param ctx the parse tree
	 */
	void enterBody(MinibaseParser.BodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link MinibaseParser#body}.
	 * @param ctx the parse tree
	 */
	void exitBody(MinibaseParser.BodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link MinibaseParser#atom}.
	 * @param ctx the parse tree
	 */
	void enterAtom(MinibaseParser.AtomContext ctx);
	/**
	 * Exit a parse tree produced by {@link MinibaseParser#atom}.
	 * @param ctx the parse tree
	 */
	void exitAtom(MinibaseParser.AtomContext ctx);
	/**
	 * Enter a parse tree produced by {@link MinibaseParser#relationalAtom}.
	 * @param ctx the parse tree
	 */
	void enterRelationalAtom(MinibaseParser.RelationalAtomContext ctx);
	/**
	 * Exit a parse tree produced by {@link MinibaseParser#relationalAtom}.
	 * @param ctx the parse tree
	 */
	void exitRelationalAtom(MinibaseParser.RelationalAtomContext ctx);
	/**
	 * Enter a parse tree produced by {@link MinibaseParser#comparisonAtom}.
	 * @param ctx the parse tree
	 */
	void enterComparisonAtom(MinibaseParser.ComparisonAtomContext ctx);
	/**
	 * Exit a parse tree produced by {@link MinibaseParser#comparisonAtom}.
	 * @param ctx the parse tree
	 */
	void exitComparisonAtom(MinibaseParser.ComparisonAtomContext ctx);
	/**
	 * Enter a parse tree produced by {@link MinibaseParser#term}.
	 * @param ctx the parse tree
	 */
	void enterTerm(MinibaseParser.TermContext ctx);
	/**
	 * Exit a parse tree produced by {@link MinibaseParser#term}.
	 * @param ctx the parse tree
	 */
	void exitTerm(MinibaseParser.TermContext ctx);
	/**
	 * Enter a parse tree produced by {@link MinibaseParser#variable}.
	 * @param ctx the parse tree
	 */
	void enterVariable(MinibaseParser.VariableContext ctx);
	/**
	 * Exit a parse tree produced by {@link MinibaseParser#variable}.
	 * @param ctx the parse tree
	 */
	void exitVariable(MinibaseParser.VariableContext ctx);
	/**
	 * Enter a parse tree produced by {@link MinibaseParser#constant}.
	 * @param ctx the parse tree
	 */
	void enterConstant(MinibaseParser.ConstantContext ctx);
	/**
	 * Exit a parse tree produced by {@link MinibaseParser#constant}.
	 * @param ctx the parse tree
	 */
	void exitConstant(MinibaseParser.ConstantContext ctx);
	/**
	 * Enter a parse tree produced by {@link MinibaseParser#cmpOp}.
	 * @param ctx the parse tree
	 */
	void enterCmpOp(MinibaseParser.CmpOpContext ctx);
	/**
	 * Exit a parse tree produced by {@link MinibaseParser#cmpOp}.
	 * @param ctx the parse tree
	 */
	void exitCmpOp(MinibaseParser.CmpOpContext ctx);
}
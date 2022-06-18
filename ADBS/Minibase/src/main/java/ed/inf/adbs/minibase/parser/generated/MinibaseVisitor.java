// Generated from Minibase.g4 by ANTLR 4.9.2
package ed.inf.adbs.minibase.parser.generated;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link MinibaseParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface MinibaseVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link MinibaseParser#query}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuery(MinibaseParser.QueryContext ctx);
	/**
	 * Visit a parse tree produced by {@link MinibaseParser#head}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHead(MinibaseParser.HeadContext ctx);
	/**
	 * Visit a parse tree produced by {@link MinibaseParser#sumagg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSumagg(MinibaseParser.SumaggContext ctx);
	/**
	 * Visit a parse tree produced by {@link MinibaseParser#avgagg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAvgagg(MinibaseParser.AvgaggContext ctx);
	/**
	 * Visit a parse tree produced by {@link MinibaseParser#body}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBody(MinibaseParser.BodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link MinibaseParser#atom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAtom(MinibaseParser.AtomContext ctx);
	/**
	 * Visit a parse tree produced by {@link MinibaseParser#relationalAtom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelationalAtom(MinibaseParser.RelationalAtomContext ctx);
	/**
	 * Visit a parse tree produced by {@link MinibaseParser#comparisonAtom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComparisonAtom(MinibaseParser.ComparisonAtomContext ctx);
	/**
	 * Visit a parse tree produced by {@link MinibaseParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTerm(MinibaseParser.TermContext ctx);
	/**
	 * Visit a parse tree produced by {@link MinibaseParser#variable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariable(MinibaseParser.VariableContext ctx);
	/**
	 * Visit a parse tree produced by {@link MinibaseParser#constant}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstant(MinibaseParser.ConstantContext ctx);
	/**
	 * Visit a parse tree produced by {@link MinibaseParser#cmpOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmpOp(MinibaseParser.CmpOpContext ctx);
}
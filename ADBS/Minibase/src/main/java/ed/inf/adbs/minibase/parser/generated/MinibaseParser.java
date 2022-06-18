// Generated from Minibase.g4 by ANTLR 4.9.2
package ed.inf.adbs.minibase.parser.generated;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class MinibaseParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.9.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, INT=13, STRING=14, ID_UPPER=15, ID_LOWER=16, 
		WS=17;
	public static final int
		RULE_query = 0, RULE_head = 1, RULE_sumagg = 2, RULE_avgagg = 3, RULE_body = 4, 
		RULE_atom = 5, RULE_relationalAtom = 6, RULE_comparisonAtom = 7, RULE_term = 8, 
		RULE_variable = 9, RULE_constant = 10, RULE_cmpOp = 11;
	private static String[] makeRuleNames() {
		return new String[] {
			"query", "head", "sumagg", "avgagg", "body", "atom", "relationalAtom", 
			"comparisonAtom", "term", "variable", "constant", "cmpOp"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "':-'", "'('", "')'", "','", "'SUM'", "'AVG'", "'='", "'!='", "'<'", 
			"'<='", "'>'", "'>='"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, "INT", "STRING", "ID_UPPER", "ID_LOWER", "WS"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "Minibase.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public MinibaseParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class QueryContext extends ParserRuleContext {
		public HeadContext head() {
			return getRuleContext(HeadContext.class,0);
		}
		public BodyContext body() {
			return getRuleContext(BodyContext.class,0);
		}
		public QueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_query; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MinibaseListener ) ((MinibaseListener)listener).enterQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MinibaseListener ) ((MinibaseListener)listener).exitQuery(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MinibaseVisitor ) return ((MinibaseVisitor<? extends T>)visitor).visitQuery(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QueryContext query() throws RecognitionException {
		QueryContext _localctx = new QueryContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_query);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(24);
			head();
			setState(25);
			match(T__0);
			setState(26);
			body();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class HeadContext extends ParserRuleContext {
		public TerminalNode ID_UPPER() { return getToken(MinibaseParser.ID_UPPER, 0); }
		public SumaggContext sumagg() {
			return getRuleContext(SumaggContext.class,0);
		}
		public AvgaggContext avgagg() {
			return getRuleContext(AvgaggContext.class,0);
		}
		public List<VariableContext> variable() {
			return getRuleContexts(VariableContext.class);
		}
		public VariableContext variable(int i) {
			return getRuleContext(VariableContext.class,i);
		}
		public HeadContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_head; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MinibaseListener ) ((MinibaseListener)listener).enterHead(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MinibaseListener ) ((MinibaseListener)listener).exitHead(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MinibaseVisitor ) return ((MinibaseVisitor<? extends T>)visitor).visitHead(this);
			else return visitor.visitChildren(this);
		}
	}

	public final HeadContext head() throws RecognitionException {
		HeadContext _localctx = new HeadContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_head);
		int _la;
		try {
			int _alt;
			setState(81);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(28);
				match(ID_UPPER);
				setState(29);
				match(T__1);
				setState(30);
				match(T__2);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(31);
				match(ID_UPPER);
				setState(32);
				match(T__1);
				setState(33);
				sumagg();
				setState(34);
				match(T__2);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(36);
				match(ID_UPPER);
				setState(37);
				match(T__1);
				setState(38);
				avgagg();
				setState(39);
				match(T__2);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(41);
				match(ID_UPPER);
				setState(42);
				match(T__1);
				setState(43);
				variable();
				setState(48);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__3) {
					{
					{
					setState(44);
					match(T__3);
					setState(45);
					variable();
					}
					}
					setState(50);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(51);
				match(T__2);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(53);
				match(ID_UPPER);
				setState(54);
				match(T__1);
				setState(55);
				variable();
				setState(60);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(56);
						match(T__3);
						setState(57);
						variable();
						}
						} 
					}
					setState(62);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
				}
				setState(63);
				match(T__3);
				setState(64);
				sumagg();
				setState(65);
				match(T__2);
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(67);
				match(ID_UPPER);
				setState(68);
				match(T__1);
				setState(69);
				variable();
				setState(74);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(70);
						match(T__3);
						setState(71);
						variable();
						}
						} 
					}
					setState(76);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
				}
				setState(77);
				match(T__3);
				setState(78);
				avgagg();
				setState(79);
				match(T__2);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SumaggContext extends ParserRuleContext {
		public VariableContext variable() {
			return getRuleContext(VariableContext.class,0);
		}
		public SumaggContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sumagg; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MinibaseListener ) ((MinibaseListener)listener).enterSumagg(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MinibaseListener ) ((MinibaseListener)listener).exitSumagg(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MinibaseVisitor ) return ((MinibaseVisitor<? extends T>)visitor).visitSumagg(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SumaggContext sumagg() throws RecognitionException {
		SumaggContext _localctx = new SumaggContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_sumagg);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(83);
			match(T__4);
			setState(84);
			match(T__1);
			setState(85);
			variable();
			setState(86);
			match(T__2);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AvgaggContext extends ParserRuleContext {
		public VariableContext variable() {
			return getRuleContext(VariableContext.class,0);
		}
		public AvgaggContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_avgagg; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MinibaseListener ) ((MinibaseListener)listener).enterAvgagg(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MinibaseListener ) ((MinibaseListener)listener).exitAvgagg(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MinibaseVisitor ) return ((MinibaseVisitor<? extends T>)visitor).visitAvgagg(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AvgaggContext avgagg() throws RecognitionException {
		AvgaggContext _localctx = new AvgaggContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_avgagg);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(88);
			match(T__5);
			setState(89);
			match(T__1);
			setState(90);
			variable();
			setState(91);
			match(T__2);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BodyContext extends ParserRuleContext {
		public List<AtomContext> atom() {
			return getRuleContexts(AtomContext.class);
		}
		public AtomContext atom(int i) {
			return getRuleContext(AtomContext.class,i);
		}
		public BodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_body; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MinibaseListener ) ((MinibaseListener)listener).enterBody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MinibaseListener ) ((MinibaseListener)listener).exitBody(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MinibaseVisitor ) return ((MinibaseVisitor<? extends T>)visitor).visitBody(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BodyContext body() throws RecognitionException {
		BodyContext _localctx = new BodyContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_body);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(93);
			atom();
			setState(98);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__3) {
				{
				{
				setState(94);
				match(T__3);
				setState(95);
				atom();
				}
				}
				setState(100);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AtomContext extends ParserRuleContext {
		public RelationalAtomContext relationalAtom() {
			return getRuleContext(RelationalAtomContext.class,0);
		}
		public ComparisonAtomContext comparisonAtom() {
			return getRuleContext(ComparisonAtomContext.class,0);
		}
		public AtomContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_atom; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MinibaseListener ) ((MinibaseListener)listener).enterAtom(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MinibaseListener ) ((MinibaseListener)listener).exitAtom(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MinibaseVisitor ) return ((MinibaseVisitor<? extends T>)visitor).visitAtom(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AtomContext atom() throws RecognitionException {
		AtomContext _localctx = new AtomContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_atom);
		try {
			setState(103);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ID_UPPER:
				enterOuterAlt(_localctx, 1);
				{
				setState(101);
				relationalAtom();
				}
				break;
			case INT:
			case STRING:
			case ID_LOWER:
				enterOuterAlt(_localctx, 2);
				{
				setState(102);
				comparisonAtom();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RelationalAtomContext extends ParserRuleContext {
		public TerminalNode ID_UPPER() { return getToken(MinibaseParser.ID_UPPER, 0); }
		public List<TermContext> term() {
			return getRuleContexts(TermContext.class);
		}
		public TermContext term(int i) {
			return getRuleContext(TermContext.class,i);
		}
		public RelationalAtomContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_relationalAtom; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MinibaseListener ) ((MinibaseListener)listener).enterRelationalAtom(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MinibaseListener ) ((MinibaseListener)listener).exitRelationalAtom(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MinibaseVisitor ) return ((MinibaseVisitor<? extends T>)visitor).visitRelationalAtom(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RelationalAtomContext relationalAtom() throws RecognitionException {
		RelationalAtomContext _localctx = new RelationalAtomContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_relationalAtom);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(105);
			match(ID_UPPER);
			setState(106);
			match(T__1);
			setState(107);
			term();
			setState(112);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__3) {
				{
				{
				setState(108);
				match(T__3);
				setState(109);
				term();
				}
				}
				setState(114);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(115);
			match(T__2);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ComparisonAtomContext extends ParserRuleContext {
		public List<TermContext> term() {
			return getRuleContexts(TermContext.class);
		}
		public TermContext term(int i) {
			return getRuleContext(TermContext.class,i);
		}
		public CmpOpContext cmpOp() {
			return getRuleContext(CmpOpContext.class,0);
		}
		public ComparisonAtomContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_comparisonAtom; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MinibaseListener ) ((MinibaseListener)listener).enterComparisonAtom(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MinibaseListener ) ((MinibaseListener)listener).exitComparisonAtom(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MinibaseVisitor ) return ((MinibaseVisitor<? extends T>)visitor).visitComparisonAtom(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ComparisonAtomContext comparisonAtom() throws RecognitionException {
		ComparisonAtomContext _localctx = new ComparisonAtomContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_comparisonAtom);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(117);
			term();
			setState(118);
			cmpOp();
			setState(119);
			term();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TermContext extends ParserRuleContext {
		public VariableContext variable() {
			return getRuleContext(VariableContext.class,0);
		}
		public ConstantContext constant() {
			return getRuleContext(ConstantContext.class,0);
		}
		public TermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_term; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MinibaseListener ) ((MinibaseListener)listener).enterTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MinibaseListener ) ((MinibaseListener)listener).exitTerm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MinibaseVisitor ) return ((MinibaseVisitor<? extends T>)visitor).visitTerm(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TermContext term() throws RecognitionException {
		TermContext _localctx = new TermContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_term);
		try {
			setState(123);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ID_LOWER:
				enterOuterAlt(_localctx, 1);
				{
				setState(121);
				variable();
				}
				break;
			case INT:
			case STRING:
				enterOuterAlt(_localctx, 2);
				{
				setState(122);
				constant();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VariableContext extends ParserRuleContext {
		public TerminalNode ID_LOWER() { return getToken(MinibaseParser.ID_LOWER, 0); }
		public VariableContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variable; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MinibaseListener ) ((MinibaseListener)listener).enterVariable(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MinibaseListener ) ((MinibaseListener)listener).exitVariable(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MinibaseVisitor ) return ((MinibaseVisitor<? extends T>)visitor).visitVariable(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VariableContext variable() throws RecognitionException {
		VariableContext _localctx = new VariableContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_variable);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(125);
			match(ID_LOWER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConstantContext extends ParserRuleContext {
		public TerminalNode INT() { return getToken(MinibaseParser.INT, 0); }
		public TerminalNode STRING() { return getToken(MinibaseParser.STRING, 0); }
		public ConstantContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constant; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MinibaseListener ) ((MinibaseListener)listener).enterConstant(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MinibaseListener ) ((MinibaseListener)listener).exitConstant(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MinibaseVisitor ) return ((MinibaseVisitor<? extends T>)visitor).visitConstant(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConstantContext constant() throws RecognitionException {
		ConstantContext _localctx = new ConstantContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_constant);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(127);
			_la = _input.LA(1);
			if ( !(_la==INT || _la==STRING) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CmpOpContext extends ParserRuleContext {
		public CmpOpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cmpOp; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MinibaseListener ) ((MinibaseListener)listener).enterCmpOp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MinibaseListener ) ((MinibaseListener)listener).exitCmpOp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MinibaseVisitor ) return ((MinibaseVisitor<? extends T>)visitor).visitCmpOp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CmpOpContext cmpOp() throws RecognitionException {
		CmpOpContext _localctx = new CmpOpContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_cmpOp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(129);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__6) | (1L << T__7) | (1L << T__8) | (1L << T__9) | (1L << T__10) | (1L << T__11))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\23\u0086\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\3\2\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\7\3\61\n\3\f\3\16\3\64\13\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\7\3=\n\3\f\3\16\3@\13\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\7\3K\n\3\f\3\16\3N\13\3\3\3\3\3\3\3\3\3\5\3T\n\3\3\4"+
		"\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\7\6c\n\6\f\6\16\6f\13"+
		"\6\3\7\3\7\5\7j\n\7\3\b\3\b\3\b\3\b\3\b\7\bq\n\b\f\b\16\bt\13\b\3\b\3"+
		"\b\3\t\3\t\3\t\3\t\3\n\3\n\5\n~\n\n\3\13\3\13\3\f\3\f\3\r\3\r\3\r\2\2"+
		"\16\2\4\6\b\n\f\16\20\22\24\26\30\2\4\3\2\17\20\3\2\t\16\2\u0085\2\32"+
		"\3\2\2\2\4S\3\2\2\2\6U\3\2\2\2\bZ\3\2\2\2\n_\3\2\2\2\fi\3\2\2\2\16k\3"+
		"\2\2\2\20w\3\2\2\2\22}\3\2\2\2\24\177\3\2\2\2\26\u0081\3\2\2\2\30\u0083"+
		"\3\2\2\2\32\33\5\4\3\2\33\34\7\3\2\2\34\35\5\n\6\2\35\3\3\2\2\2\36\37"+
		"\7\21\2\2\37 \7\4\2\2 T\7\5\2\2!\"\7\21\2\2\"#\7\4\2\2#$\5\6\4\2$%\7\5"+
		"\2\2%T\3\2\2\2&\'\7\21\2\2\'(\7\4\2\2()\5\b\5\2)*\7\5\2\2*T\3\2\2\2+,"+
		"\7\21\2\2,-\7\4\2\2-\62\5\24\13\2./\7\6\2\2/\61\5\24\13\2\60.\3\2\2\2"+
		"\61\64\3\2\2\2\62\60\3\2\2\2\62\63\3\2\2\2\63\65\3\2\2\2\64\62\3\2\2\2"+
		"\65\66\7\5\2\2\66T\3\2\2\2\678\7\21\2\289\7\4\2\29>\5\24\13\2:;\7\6\2"+
		"\2;=\5\24\13\2<:\3\2\2\2=@\3\2\2\2><\3\2\2\2>?\3\2\2\2?A\3\2\2\2@>\3\2"+
		"\2\2AB\7\6\2\2BC\5\6\4\2CD\7\5\2\2DT\3\2\2\2EF\7\21\2\2FG\7\4\2\2GL\5"+
		"\24\13\2HI\7\6\2\2IK\5\24\13\2JH\3\2\2\2KN\3\2\2\2LJ\3\2\2\2LM\3\2\2\2"+
		"MO\3\2\2\2NL\3\2\2\2OP\7\6\2\2PQ\5\b\5\2QR\7\5\2\2RT\3\2\2\2S\36\3\2\2"+
		"\2S!\3\2\2\2S&\3\2\2\2S+\3\2\2\2S\67\3\2\2\2SE\3\2\2\2T\5\3\2\2\2UV\7"+
		"\7\2\2VW\7\4\2\2WX\5\24\13\2XY\7\5\2\2Y\7\3\2\2\2Z[\7\b\2\2[\\\7\4\2\2"+
		"\\]\5\24\13\2]^\7\5\2\2^\t\3\2\2\2_d\5\f\7\2`a\7\6\2\2ac\5\f\7\2b`\3\2"+
		"\2\2cf\3\2\2\2db\3\2\2\2de\3\2\2\2e\13\3\2\2\2fd\3\2\2\2gj\5\16\b\2hj"+
		"\5\20\t\2ig\3\2\2\2ih\3\2\2\2j\r\3\2\2\2kl\7\21\2\2lm\7\4\2\2mr\5\22\n"+
		"\2no\7\6\2\2oq\5\22\n\2pn\3\2\2\2qt\3\2\2\2rp\3\2\2\2rs\3\2\2\2su\3\2"+
		"\2\2tr\3\2\2\2uv\7\5\2\2v\17\3\2\2\2wx\5\22\n\2xy\5\30\r\2yz\5\22\n\2"+
		"z\21\3\2\2\2{~\5\24\13\2|~\5\26\f\2}{\3\2\2\2}|\3\2\2\2~\23\3\2\2\2\177"+
		"\u0080\7\22\2\2\u0080\25\3\2\2\2\u0081\u0082\t\2\2\2\u0082\27\3\2\2\2"+
		"\u0083\u0084\t\3\2\2\u0084\31\3\2\2\2\n\62>LSdir}";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}
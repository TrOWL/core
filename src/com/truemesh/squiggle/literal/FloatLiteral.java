package com.truemesh.squiggle.literal;

/**
 * @author Nat Pryce
 */
public class FloatLiteral extends LiteralWithSameRepresentationInJavaAndSql {
	public FloatLiteral(double literalValue) {
		super(new Double(literalValue));
	}
}

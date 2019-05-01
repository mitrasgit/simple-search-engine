/**
 * 
 */
package searchengine;

/**
 * A token, a char sequence of some TokenType
 */
public class Token {
	
	private TokenType type;
	private String value;
	
	public Token(TokenType type, String value) {
		this.type = type;
		this.value = value;
	}
	
	public TokenType getType() {
		return type;
	}
	
	public String getValue() {
		return value;
	}
	
	/**
	 * Check if two Tokens are equal
	 * @param other the other Token to be compared
	 * @return True if both Tokens have equal type and value, otherwise False
	 */
	@Override
	public boolean equals(Object obj) {
		boolean equal = false;
		if (this == obj) {
			equal = true;
		}
		else if (obj instanceof Token) {
			Token token = (Token) obj;
			equal = type.equals(token.getType()) && value.equals(token.getValue());
		}
		return equal;
	}
	
	@Override
	public String toString() {
		return "{" + type.toString() + ", " + value + "}";
	}

}

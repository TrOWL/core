package eu.trowl.owl.rel.model;


/** 
 * @author Yuan Ren
 * @version 2010-03-27
 */
public class CardinalityEntry {
	public CardinalityEntry(Atomic base, int n) {
		super();
		this.basen = base;
		this.n = n;
	}
	public int n;
	public Atomic basen;

}

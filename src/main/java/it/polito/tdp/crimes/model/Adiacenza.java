package it.polito.tdp.crimes.model;

public class Adiacenza {
	
	private String e1;
	private String e2;
	private int peso;
	public Adiacenza(String e1, String e2, int peso) {
		super();
		this.e1 = e1;
		this.e2 = e2;
		this.peso = peso;
	}
	public String getE1() {
		return e1;
	}
	public void setE1(String e1) {
		this.e1 = e1;
	}
	public String getE2() {
		return e2;
	}
	public void setE2(String e2) {
		this.e2 = e2;
	}
	public int getPeso() {
		return peso;
	}
	public void setPeso(int peso) {
		this.peso = peso;
	}
	@Override
	public String toString() {
		return "Adiacenza [e1=" + e1 + ", e2=" + e2 + ", peso=" + peso + "]";
	}
	
}

package jdev.mentoria.lojavirtual;

import jdev.mentoria.lojavirtual.util.ValidaCNPJ;
import jdev.mentoria.lojavirtual.util.ValidaCPF;

public class TesteCPFCNPJ {

	public static void main(String[] args) {
		
		boolean isCnpj = ValidaCNPJ.isCNPJ("83.275.676/0001-15");
		
		boolean isCpf = ValidaCPF.isCPF("440.828.150-66");
	}
	
}

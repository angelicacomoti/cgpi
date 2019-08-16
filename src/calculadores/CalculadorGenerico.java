package calculadores;

import primitivos.Ponto;

public class CalculadorGenerico {

	public static Ponto obterPontoMedio(Ponto pontoInicial, Ponto pontoFinal) {
		int xMedio = (int) ((pontoFinal.getx() + pontoInicial.getx()) / 2);
		int yMedio = (int) ((pontoFinal.gety() + pontoInicial.gety()) / 2);
		Ponto pontoMedio = new Ponto(xMedio, yMedio);
		return pontoMedio;
	}
	
	public static double obterDistanciaEntreDoisPontos(Ponto pontoA, Ponto pontoB){
		double deltaX = pontoB.getx() - pontoA.getx();
		double deltaY = pontoB.gety() - pontoA.gety();
		double distancia =  Math.sqrt((Math.pow(deltaX, 2))+(Math.pow(deltaY, 2)));
		return distancia;
	}
}

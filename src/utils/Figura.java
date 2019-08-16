package utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import controladores.TipoPrimitivo;

@XmlRootElement(name = "Figura")
public class Figura implements Serializable {

	@XmlAnyElement(lax = true)
	public List<Object> todosObjetosDesenhados;

	public void setObjetosDesenhados(Map<TipoPrimitivo, List<Object>> objetosDesenhados) {
		this.todosObjetosDesenhados = new ArrayList<>();
		objetosDesenhados.entrySet().stream().forEach(e -> {
			e.getValue().forEach((value) -> {
				this.todosObjetosDesenhados.add(value);
			});
		});
	}

	@XmlTransient
	public Map<TipoPrimitivo, List<Object>> getObjetosDesenhados(){
		Map<TipoPrimitivo, List<Object>> objetos = new HashMap<>();
		List<TipoPrimitivo> listEnum = Arrays.asList(TipoPrimitivo.values());
		
		for ( TipoPrimitivo tipoPrimitivo: listEnum) {
			objetos.put(tipoPrimitivo, new ArrayList<>());
		}

		this.todosObjetosDesenhados.forEach((obj) -> {
			switch (obj.getClass().getSimpleName()){
				case "Reta":
					//listaAtual.set(objetos.get(TipoPrimitivo.RETA));
					objetos.get(TipoPrimitivo.RETA).add(obj);
					break;
				case "Circulo":
					objetos.get(TipoPrimitivo.CIRCULO).add(obj);
					break;
			}
		});

		return objetos;
	}
}

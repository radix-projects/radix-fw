package br.com.radixeng.infraestructure;

import java.io.Serializable;

import lombok.Getter;
import lombok.ToString;


@Getter
@ToString
public class Message<T> implements Serializable {
   
	private static final long serialVersionUID = 1L;
	
	private final T payLoad;

    public Message(T payLoad) {
        this.payLoad = payLoad;
    }
}

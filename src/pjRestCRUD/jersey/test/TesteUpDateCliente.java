package pjRestCRUD.jersey.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.RequestEntity;

import pjRestCRUD.jersey.pojo.Cliente;

public class TesteUpDateCliente {
	
	public static void main(String[] args) throws HttpException, IOException {

		Cliente cliente = new Cliente();
		
		cliente.setClienteId(1395111501575L);
		cliente.setClienteNome("Carl alterado 222");
		cliente.setClienteSNome("Edwin");
		cliente.setClienteEmail("carlinstr07@gmail.com");
		
		
		atualizaCliente(cliente);
		
	}
	
	private static void atualizaCliente(Cliente cliente) throws IOException, HttpException{
		
		final String upDateClienteXML = "<cliente>" +
		
										"<clienteNome>"+cliente.getClienteNome()+"</clienteNome>" +
		
										"<clienteSNome>"+cliente.getClienteSNome()+"</clienteSNome>" +
										
										"<clienteEmail>"+cliente.getClienteEmail()+"</clienteEmail>" +
										
										"</cliente>" ;
		
		PutMethod putMethod = new PutMethod("http://localhost:8080/pjRestCRUD/recursoCliente/"+cliente.getClienteId());
		
		RequestEntity entity = new InputStreamRequestEntity( new ByteArrayInputStream(upDateClienteXML.getBytes()), "application/xml");
		
		putMethod.setRequestEntity(entity);
		
		putMethod.getRequestEntity();
		
		HttpClient client = new HttpClient();
		
		try {
			
			System.out.println("Executando metodo HTTP PUT");
			
			int result = client.executeMethod(putMethod);
			
			System.out.println("Executado metodo HTTP PUT com sucesso...");
			
			System.out.println("Codigo de Resposta e Status: " + result);
			
			System.out.println("Response headers: ");
			
			
			//Recuperando so cabeçalhos do response
			Header[] headers = putMethod.getResponseHeaders();
			
			for (int i = 0; i < headers.length; i++) {
				System.out.println(headers[i].toString());
			}
		} finally{
			putMethod.releaseConnection();
		}
	}

}

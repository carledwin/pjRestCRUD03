package pjRestCRUD.jersey.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;

import pjRestCRUD.jersey.pojo.Cliente;

public class TesteAddCliente {
	
	public static void main(String[] args) throws HttpException, IOException {
		
		Cliente cliente = new Cliente();
		
		cliente.setClienteNome("Eva ");
		cliente.setClienteSNome("Albuquerque");
		cliente.setClienteEmail("aalbuquerque@outlook.com");
		
		adicionaRecurso(cliente);
		
	}

	//INCLUDE - PERSIST
	private static void adicionaRecurso(Cliente cliente) throws IOException, HttpException{
	
		final String addClienteXML = 	"<cliente>" + 	
										"<clienteNome>"+cliente.getClienteNome()+"</clienteNome>" +
										"<clienteSNome>"+cliente.getClienteSNome()+"</clienteSNome>" +
										"<clienteEmail>"+cliente.getClienteEmail()+"</clienteEmail>" +
										"</cliente>" ;
		PostMethod  postMethod = new PostMethod("http://localhost:8080/pjRestCRUD/recursoCliente");
		
		RequestEntity  entity = new InputStreamRequestEntity(new ByteArrayInputStream(addClienteXML.getBytes()), "application/xml");
		
		postMethod.setRequestEntity(entity);
		
		HttpClient client = new HttpClient();
		
		try {
			System.out.println("Executando metodo HTTP POST");
			
			int result = client.executeMethod(postMethod);
			
			System.out.println("Executado metodo HTTP POST com sucesso...");
			
			System.out.println("Codigo de Resposta: " + result);
			
			System.out.println("Resposta de cabeçalhos.");
			
			//Recuperando so cabeçalhos do response
			Header[] headers = postMethod.getResponseHeaders();
			
			for(int i = 0; i < headers.length; i++){
				System.out.println(headers.clone()[i].toString());
			}
		}
			finally{
				
			postMethod.releaseConnection();
			}
	}

	
	
	/*Output / Saída / Resposta
	 * 
	 * Codigo de Resposta: 201 // indica sucesso na resposta e que o recurso foi criado
	Resposta de cabeçalhos.
	Server: Apache-Coyote/1.1

	Location: http://localhost:8080/pjRestCRUD/recursoCliente/1393422751701 // indica a URI do recurso criado

	Content-Length: 0

	Date: Wed, 26 Feb 2014 13:52:31 GMT*/
	/*==============================================================================================*/
	
	
	
	
}

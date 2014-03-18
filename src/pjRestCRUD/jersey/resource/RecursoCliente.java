package pjRestCRUD.jersey.resource;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.util.Properties;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import pjRestCRUD.jersey.pojo.Cliente;

import com.sun.jersey.api.NotFoundException;

//anotação JAX-RS para implementar recursos da Web
@Path("recursoCliente")
// Para uma classe anotada a base URI é o contexto da aplicação
public class RecursoCliente {
	// Classe recurso é um pojo que possui pelo menos um metodo anotado com
	// @Path

	// @Path para um metodo URI de base é a URI efetiva da classe

	// URIs base são tratadas como se iniciassem com "/" o que indica a forma
	// que a URI está mapeada para Cliente.

	private static final String DATA_FILE = "C:\\TEMP\\cliente-monografia.txt";

	// por não possuir @Path no método, a URI da classe é tambpem o URL para
	// receber o método
	// anotação JAX-RS para implementar recursos da Web, responde a chamadas de
		// método HTTP POST
		
	// anotação JAX-RS para implementar recursos da Web, define os tipos de
		// mídia que o metodo pode aceitar, caso o mesmo não seja especificado
		// presume-se que qualquer tipo de mídia é aceitável. Caso exista uma
		// anotação a nível de classe a anotação a nível de metodo tem precedencia.
		
	@POST // POST CRIA UM NOVO RECURSO
	@Consumes("application/xml")
	public Response addCCliente(InputStream clienteData) {
		// o método addCliente constroi o cliente a partir da entrada XML
		// recebido e persisti-lo.
		try {
			Cliente cliente = buildCliente(null, clienteData);
			long clienteId = persist(cliente, 0);
			// Response é retornado para o cliente, que contem uma URI para o
			// recurso recem cricado. Retornar resultados resposta do tipo em um
			// corpo de entidade mapeada a partir da propriedade de entidade da
			// resposta com o código de status especificado pela propriedade
			// status da resposta.
			return Response.created(URI.create("/" + clienteId)).build();
		} catch (Exception e) {
			// É uma RunTimeException que é usada para embrulhar os códigos de
			// status HTTP apropriados.
			throw new WebApplicationException(e,
					Response.Status.INTERNAL_SERVER_ERROR);
		}
	}

	private long persist(Cliente cliente, long clienteId) throws IOException {
 		Properties properties = new Properties();
		properties.load(new FileInputStream(DATA_FILE));

		if (clienteId == 0) {
			clienteId = System.currentTimeMillis();
		}

		properties.setProperty(String.valueOf(clienteId),
				cliente.getClienteNome() + "," + cliente.getClienteSNome()
						+ "," + cliente.getClienteEmail());
		properties.store(new FileOutputStream(DATA_FILE), null);

		return clienteId;
	}

	private Cliente buildCliente(Cliente cliente, InputStream clienteData)
			throws ParserConfigurationException, SAXException, IOException {

		if (cliente == null) {
			cliente = new Cliente();
		}

		DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		Document document = documentBuilder.parse(clienteData);
		document.getDocumentElement().normalize();

		NodeList nodeList = document.getElementsByTagName("cliente");

		Node clienteRoot = nodeList.item(0);

		if (clienteRoot.getNodeType() == Node.ELEMENT_NODE) {
			Element element = (Element) clienteRoot;

			NodeList childNodes = element.getChildNodes();

			for (int i = 0; i < childNodes.getLength(); i++) {
				Element childElement = (Element) childNodes.item(i);
				String tagName = childElement.getTagName();
				String textContext = childElement.getTextContent();

				if (tagName.equals("clienteNome")) {
					cliente.setClienteNome(textContext);
				} else if (tagName.equals("clienteSNome")) {
					cliente.setClienteSNome(textContext);
				} else if (tagName.equals("clienteEmail")) {
					cliente.setClienteEmail(textContext);
				}
			}
		} else {
			throw new WebApplicationException(
					Response.Status.INTERNAL_SERVER_ERROR);
		}

		return cliente;
	}
	
	//READ - CONSULT
		@GET // indica que este metodo é responsavel por operações HTTP GET - RECUPERA O ESTADO DE UM RECURSO 
		@Path("{id}") // indica um ID dinamico. os caminhos são relativos
		@Produces("application/xml") //indica o tipo de mídia resultado para a operação
		public StreamingOutput retrieveCliente(@PathParam("id") String clienteId){
			//PathParam lê o id de caminho passado
			/* StreamOutput é retornado por esse método de recursos, é uma versão mais simples de MessageBodyWriter. Possui um metodo write, que tem fluxo de saída.
			 *Tudo  o que você precisa é escrever os dados para este objeto, que será devolvido ao programa cliente.
			 */
			try {
				String clienteDetails = loadCliente(clienteId);
				
				System.out.println("Detalhes do Cliente: " + clienteDetails);
				
				if(clienteDetails == null){
					throw new NotFoundException("<error>Não há Cliente com o id: " + clienteId + "</error>");
				}
			final String[] details = clienteDetails.split(","); 
			
			return new StreamingOutput(){
				public void write(OutputStream outputStream){
					PrintWriter out = new PrintWriter(outputStream);
					out.println("< ?xml version=\"1.0\" encoding=\"UTF-8\"?>");
					out.println("<cliente>");
					out.println("<clienteNome>" +details[0]+ "</clienteNome>");
					out.println("<clienteSNome>" +details[1]+ "</clienteSNome>");
					out.println("<clienteEmail>" +details[2]+ "</clienteEmail>");
					out.println("</cliente>");
					out.close();
				}
			};
			}catch(IOException e){
				throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR);
			}
		}
		
		
		/*carrega a linha de cliente do arquivo de texto
		 * o parametro clienteId especifica o cliente a ser carregado
		 * retorna informações do cliente
		 */
		private String loadCliente(String clienteId) throws IOException{
			
			Properties properties = new Properties();
			properties.load(new FileInputStream(DATA_FILE));
			return properties.getProperty(clienteId);
		}	
		
		/*==================================================================================================
		 * 
		 * Testar http://http://localhost:8080/pjRestCRUD/recursoCliente/1393422213735
		 * Chrome Poster GET
		 * 
		 * Response
		 * 
		 * Response:
			status: 200 OK
			Date: Wed, 26 Feb 2014 19:02:32 GMT
			Server: Apache-Coyote/1.1
			Content-Length: 187
			Content-Type: application/xml
			
			< ?xml version="1.0" encoding="UTF-8"?>
			<cliente>
			<clienteNome>Carl02</clienteNome>
			<clienteSNome>Edwin</clienteSNome>
			<clienteEmail>carlinstr02@gmail.com</clienteEmail>
			</cliente>
		 * 
		 * Detalhes do Cliente: Carl02,Edwin,carlinstr02@gmail.com
		 * 
		 */
		
		/*==================================================================================================*/
		
		@PUT //ATUALIZA O ESTADO DE UM RECURSO CONHECIDO. a anotação indica que o metodo lida com solicitações HTTP PUT
		@Path("{id}")//indica um id dinâmico, os caminhos são relativos
		@Consumes("application/xml")//indica o tipo de mídia resultado para a operação
		public void updateCliente(@PathParam("id") String clienteId, InputStream input){
			try {
				String clienteDetails = loadCliente(clienteId);
				if(clienteDetails == null){
					throw new WebApplicationException(Response.Status.NOT_FOUND);
				}
				
				String[] details = clienteDetails.split(",");
				Cliente cliente = new Cliente();
					cliente.setClienteNome(details[0]);
					cliente.setClienteSNome(details[1]);
					cliente.setClienteEmail(details[2]);
				
					buildCliente(cliente, input);
					
					persist(cliente, Long.valueOf(clienteId));
				
			} catch (Exception e) {
				throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR);
			}
		}
		
		@DELETE //EXCLUUI O RECURSO CONHECIDO
		@Path("{id}") // indica um id dinamico, os caminhos são relativos
		public void deleteCliente(@PathParam("id") String clienteId){
			try {
				Properties properties = new Properties();
				
				properties.load(new FileInputStream(DATA_FILE));
				
				String clienteDetails = properties.getProperty(clienteId);
				
				if(clienteDetails == null){
					throw new WebApplicationException(Response.Status.NOT_FOUND);
				}
				
				properties.remove(clienteId);
				properties.store(new FileOutputStream(DATA_FILE), null);
			} catch (Exception e) {
				 throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR);
			}
		}
		

		
}

package pjRestCRUD.jersey;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/helloRestCRUD") // indica o caminho URI mapeado para o recurso
public class HelloRestCRUD {
		
		@GET // indica o metodo HTTP permitido para este recurso
		@Produces("text/plain") // indica o tipo MIME retornado para este recurso
		public String sayHello(){
			return "Hello Rest CRUD with Jersey !!!";
		}
	/* Startar o TomCat e acessar o recurso usando o caminho URL http://localhost:8080/pjRestCRUD/helloRestCRUD, deverá ser exibida a resposta Hello Rest with Jersey !!! */
}

package pjRestCRUD.jersey.test;

import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class TesteReaderCliente {

	
	private static final String DATA_FILE = "C:\\TEMP\\cliente-monografia.txt";
	
	public static void main(String[] args) {
		
		System.out.println("informe o nome do arquivo texto:\n");
		
		consultaRecurso(DATA_FILE);
		
		System.out.println("Leitura realizada com sucesso!");
		
	}
	
	private static void consultaRecurso(String arquivo){
try {
			
			@SuppressWarnings("resource")
			Scanner scanner = new Scanner(new FileReader(arquivo)).useDelimiter("\\n");
			System.out.println("\n Conte�do do arquivo texto: \n");
			while(scanner.hasNext()){
				String nome = scanner.next();
				System.out.println(nome);
			}
			scanner.close();
		} catch (IOException e) {
			System.err.printf("Erro na abertura do arquivo: %s. \n", e.getMessage());
		}
	}
}

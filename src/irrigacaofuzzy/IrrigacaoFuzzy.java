package irrigacaofuzzy;

public class IrrigacaoFuzzy {

    public static void main(String[] args) throws Exception {
        System.out.println("Projeto de Lógicas Não Clássicas");
        System.out.println("  Erick Alves Augusto");
        System.out.println("  Rafael Cardoso da Silva");
        System.out.println("      Irrigação Fuzzy\n");

        FCL.init();
        if (args.length == 1 && args[0].equals("console")) {
            FCL.console();
        } else {
            Janela.ABRIR();
        }
    }
}

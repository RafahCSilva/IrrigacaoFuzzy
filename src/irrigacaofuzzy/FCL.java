package irrigacaofuzzy;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.JOptionPane;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.plot.JDialogFis;

import org.leores.plot.JGnuplot;
import org.leores.util.data.DataTableSet;

public class FCL {

    private static FIS fis = null;

    public static void init() {
        System.out.println("  Carregando .FCL...");
        // Load from 'FCL' file
        String fileName = "FCL/IrrigacaoRules.fcl";
        fis = FIS.load(fileName, true);

        if (fis == null) { // Error while loading
            System.err.println("  O arquivo '" + fileName + "' não pode ser aberto.");
            System.exit(1);
        }
        System.out.println("  Carregamento Completo!\n");
    }

    public static void plotFuzzy(String labelX, String labelY, String labelZ, int valueZ) throws Exception {
        // Exemplo obtido em https://github.com/mleoking/JavaGnuplotHybrid
        System.out.println("Gerando Imagem de Densidade:");

        // Show variables
        FunctionBlock functionBlock = fis.getFunctionBlock(null);

        System.out.println("  Iniciando jGNUplot...");
        JGnuplot jg = new JGnuplot();
        JGnuplot.Plot plot = new JGnuplot.Plot("");
        plot.xlabel = labelX;
        plot.ylabel = labelY;
        plot.zlabel = "Tempo (minutos)";

        System.out.println("  Obtendo valores...");

        DataTableSet dts = plot.addNewDataTableSet("Irrigação Fuzzy");

        List x = new ArrayList(), y = new ArrayList(), z = new ArrayList();

        int minX = 0;
        int maxX = 100;
        int minY = 0;
        int maxY = 100;

        if (labelX.equals("temperatura")) {
            minX = 15;
            maxX = 50;
        }

        if (labelY.equals("temperatura")) {
            minY = 15;
            maxY = 50;
        }

        for (int i = minX; i <= maxX; i += 1) {
            for (int j = minY; j <= maxY; j += 1) {

                // Set inputs
                functionBlock.setVariable(labelX, i);
                functionBlock.setVariable(labelY, j);
                functionBlock.setVariable(labelZ, valueZ);

                // Evaluate
                fis.evaluate();

                // armazenando valores
                x.add(i);
                y.add(j);
                z.add(fis.getVariable("tempo").getValue());
                // System.out.println("TEMPO(" + i + "," + j + "): " + fis.getVariable("tempo").getValue());
            }
        }

        dts.addNewDataTable("tempo", x, y, z);

        System.out.println("  Desenhando Grafico....");
        jg.execute(plot, jg.plotImage);
    }

    public static void FUZZY(int estagio, int temperatura, int umidade) throws Exception {
        System.out.println("Gerando Graficos:");
        int i = 0;

        // Create a plot
        JDialogFis jdf = new JDialogFis(fis, 800, 600);

        // Show variables
        FunctionBlock functionBlock = fis.getFunctionBlock(null);
        //JFuzzyChart.get().chart(functionBlock); // janelas separadas

        // Set inputs
        functionBlock.setVariable("estagio", estagio);
        functionBlock.setVariable("temperatura", temperatura);
        functionBlock.setVariable("umidade", umidade);

        // Evaluate 
        fis.evaluate();

        // Show the accumulation method�s result: combining of the consequentes
        //Variable gorjeta = functionBlock.getVariable("tempo");
        //JFuzzyChart.get().chart(gorjeta, gorjeta.getDefuzzifier(), true); // janela separada
        double tempo = fis.getVariable("tempo").getValue();

        int min = (int) tempo;
        int seg = (int) (60 / (int) ((tempo - min) * 100));

        String msg = String.format("O Tempo será %d minutos e %d segundos", min, seg);
        JOptionPane.showMessageDialog(null, msg, "Resultado", JOptionPane.INFORMATION_MESSAGE);

    }

    public static void CALCULAR(int estagio, int temperatura, int umidade) {
        System.out.println("Calculando o Tempo:");

        // Show variables
        FunctionBlock functionBlock = fis.getFunctionBlock(null);

        // Set inputs
        functionBlock.setVariable("estagio", estagio);
        functionBlock.setVariable("temperatura", temperatura);
        functionBlock.setVariable("umidade", umidade);

        // Evaluate
        fis.evaluate();

        double tempo = fis.getVariable("tempo").getValue();

        int min = (int) tempo;
        int seg = (int) (60 / (int) ((tempo - min) * 100));

        String msg = String.format("O Tempo será %d minutos e %d segundos", min, seg);
        JOptionPane.showMessageDialog(null, msg, "Resultado", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void console() {
        System.out.println("Modo console ativado!\n\n");

        int estagio, temperatura, umidade;
        Scanner s = new Scanner(System.in);
        while (true) {
            System.out.println("Digite o valores de entrada para as variáveis a seguir");
            // ler entradas
            do {
                System.out.print("  Estágio (entre 0 e 100): ");
                estagio = s.nextInt();
                if (estagio >= 0 && estagio <= 100) {
                    break;
                }
                System.out.println("    valor incorreto... digite novamente");
            } while (true);

            do {
                System.out.print("  Temperatura (entre 15 e 50): ");
                temperatura = s.nextInt();
                if (temperatura >= 15 && temperatura <= 50) {
                    break;
                }
                System.out.println("    valor incorreto... digite novamente");
            } while (true);

            do {
                System.out.print("  Umidade (entre 0 e 100): ");
                umidade = s.nextInt();
                if (umidade >= 0 && umidade <= 100) {
                    break;
                }
                System.out.println("    valor incorreto... digite novamente");
            } while (true);

            System.out.println("Calculando o Tempo....");

            // Show variables
            FunctionBlock functionBlock = fis.getFunctionBlock(null);

            // Set inputs
            functionBlock.setVariable("estagio", estagio);
            functionBlock.setVariable("temperatura", temperatura);
            functionBlock.setVariable("umidade", umidade);

            // Evaluate
            fis.evaluate();

            double tempo = fis.getVariable("tempo").getValue();

            int min = (int) tempo;
            int seg = (int) (60 / (int) ((tempo - min) * 100));

            String msg = String.format("O Tempo será %d minutos e %d segundos\n", min, seg);
            System.out.println(msg);
        }
    }
}

package irrigacaofuzzy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.rule.Variable;
import net.sourceforge.jFuzzyLogic.rule.Rule;
import net.sourceforge.jFuzzyLogic.rule.RuleBlock;
import net.sourceforge.jFuzzyLogic.rule.RuleExpression;
import net.sourceforge.jFuzzyLogic.rule.RuleTerm;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;

import org.leores.plot.JGnuplot;
import org.leores.plot.JGnuplot.Plot;
import org.leores.util.data.DataTableSet;

public class FCL {

    public static void plotFuzzy(String labelX, String labelY, String labelZ, int valueZ) throws Exception {
        System.out.println("Gerando Imagem de Densidade:");
        System.out.println("  Carregando .FCL...");
        // Load from 'FCL' file
        String fileName = "FCL/IrrigacaoRules.fcl";
        FIS fis = FIS.load(fileName, true);

        if (fis == null) { // Error while loading
            System.err.println("  O arquivo '" + fileName + "' não pode ser aberto.");
            return;
        }

        // Show variables
        FunctionBlock functionBlock = fis.getFunctionBlock(null);

        System.out.println("  Iniciando jGNUplot...");
        JGnuplot jg = new JGnuplot();
        JGnuplot.Plot plot = new JGnuplot.Plot("") {
            {
                xlabel = labelX;
                ylabel = labelY;
                zlabel = "Tempo (minutos)";
            }
        };
        System.out.println("  Obtendo valores...");

        DataTableSet dts = plot.addNewDataTableSet("Irrigação Fuzzy");

        List x = new ArrayList(), y = new ArrayList(), z = new ArrayList();

        int limitX = 100;
        int limitY = 100;

        if (labelX.equals("temperatura")) {
            limitX = 50;
        }

        if (labelY.equals("temperatura")) {
            limitY = 50;
        }

        for (int i = 0; i <= limitX; i += 1) {
            for (int j = 0; j <= limitY; j += 1) {

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
        int i = 0;

        // Load from 'FCL' file
        String fileName = "FCL/IrrigacaoRules.fcl";
        FIS fis = FIS.load(fileName, true);

        if (fis == null) { // Error while loading
            System.err.println("  O arquivo '" + fileName + "' não pode ser aberto.");
            return;
        }

        // Show variables
        FunctionBlock functionBlock = fis.getFunctionBlock(null);
        JFuzzyChart.get().chart(functionBlock);

        // Set inputs
        functionBlock.setVariable("estagio", estagio);
        functionBlock.setVariable("temperatura", temperatura);
        functionBlock.setVariable("umidade", umidade);

        // Evaluate 
        fis.evaluate();

        /* ===============================================
         * FASE DE FUZZIFICACAO
         * Mostrar o valor das funcoes de pertinencia
         * das variaveis lingui�sticas de entrada
         * ===============================================
         */
        /*System.out.println("--------------------------");
         System.out.println("FASE DE FUZZIFICACAO");
         System.out.println("--------------------------");
         double servicoRuim = functionBlock.getVariable("servico").getMembership("ruim");
         double servicoBom = functionBlock.getVariable("servico").getMembership("bom");
         double servicoExcelente = functionBlock.getVariable("servico").getMembership("excelente");
         System.out.println("servico(ruim):" + servicoRuim);
         System.out.println("servico(bom):" + servicoBom);
         System.out.println("servico(excelente):" + servicoExcelente);
      
         double refeicaoRancosa = functionBlock.getVariable("refeicao").getMembership("rancosa");
         double refeicaoDeliciosa = functionBlock.getVariable("refeicao").getMembership("deliciosa");
         System.out.println("\nrefeicao(rancosa):" + refeicaoRancosa);
         System.out.println("refeicao(deliciosa):" + refeicaoDeliciosa);     */
        /* ===============================================
         * FIM DA FASE DE FUZZIFICACAO
         * ===============================================
         */
        /* ===============================================
         * METODOS DE AGREGACAO
         * Aplicar os operadores fuzzy AND (t-norma) 
         * OR (t-conorma) nos antecedentes das regras
         * Assim, encontra-se o valor dos antecedentes
         * das regras
         * ===============================================
         */
        HashMap<String, RuleBlock> todasRegras = new HashMap<String, RuleBlock>();
        todasRegras = functionBlock.getRuleBlocks();

        //Pegar o Bloco de Regras que esta no arquivo FLC
        RuleBlock blocoDeRegras = todasRegras.get("No1");
        //Mostrar o Bloco de Regras que foi retornado pelo comando acima
        //System.out.println("\n==================================");
        //System.out.println("BLOCO DE REGRAS DO ARQUIVO FCL");
        //System.out.println(blocoDeRegras.toString());

        //Pegar as regras do Bloco de Regras
        List<Rule> regras;
        regras = blocoDeRegras.getRules();

        System.out.println("------------------------------------");
        System.out.println("CALCULO DOS ANTECEDENTES DAS REGRAS");
        System.out.println("------------------------------------");
        //Para cada regra mostar seu antecedente e o valor deste antecedente,
        //usando os metodos de agregacao MIN e MAX
        for (Rule umaRegra : regras) {
            i++;
            RuleExpression regraExpressao = umaRegra.getAntecedents();
            System.out.println("- Antecedente da Regra " + i + ": " + regraExpressao.toString());
            //O metodo getDegreeOfSupport() retorna o valor do antecedente da regra,
            //apos a aplicacao dos operadores OR, AND
            System.out.println("  Valor do Antecedente da Regra " + i + ": " + umaRegra.getDegreeOfSupport());
        }

        // Show each rule (and degree of support)
        //System.out.println("Regras e seus respectivos graus de ativação");
        //for( Rule r : fis.getFunctionBlock("gorjeta").getFuzzyRuleBlock("No1").getRules() )
        //   System.out.println(r);
        /* ===============================================
         * FIM DA APLICACAO DOS METODOS DE AGREGACAO
         * ===============================================
         */
        /* ===============================================
         * METODOS DE ATIVACAO
         * Definir como os antecedentes de uma regra 
         * modificam os consequentes.
         * Como o metodo de inferecia de Mamdani esta 
         * sendo utilizado neste programa, entao o metodo
         * de ativacao sera MIN
         * ===============================================
         */
        System.out.println("------------------------------------");
        System.out.println("RESULTADO METODO DE ATIVACAO");
        System.out.println("------------------------------------");

        i = 0;
        for (Rule umaRegra : regras) {
            i++;
            RuleTerm termo = umaRegra.getConsequents().element();
            System.out.println("- Consequente da Regra " + i + ": " + termo.toString());
        }

        /* ===============================================
         * FIM DOS METODOS DE ATIVACAO
         * ==============================================
         */
        /* ===============================================
         * METODO DE ACUMULACAO
         * Os conjuntos fuzzy que representam as saidas
         * das regras sao combinados em um unico conjunto
         * fuzzy
         * ===============================================
         */
        System.out.println("--------------------------");
        System.out.println("FASE DE ACUMULACAO");
        System.out.println("--------------------------");
        System.out.println("Veja grafico com os consequentes combinados pelo metodo MAX");
        // Show the accumulation method�s result: combining of
        //the consequentes
        Variable gorjeta = functionBlock.getVariable("tempo");
        JFuzzyChart.get().chart(gorjeta, gorjeta.getDefuzzifier(), true);
        /* ===============================================
         * FIM DO METODO DE ACUMULACAO
         * ===============================================
         */

        /* ===============================================
         * METODO DE DEFUZZIFICACAO
         * ===============================================
         */
        System.out.println("--------------------------");
        System.out.println("FASE DE DEFUZZIFICACAO");
        System.out.println("--------------------------");
        // Print outputs defuzzifier
        System.out.println("TEMPO: " + fis.getVariable("tempo").getValue());
        //OR
        //System.out.println("GORJETA:" + fis.getVariable("gorjeta").getLatestDefuzzifiedValue());
        /* ===============================================
         * FIM DO METODO DE DEFUZZIFICACAO
         * ===============================================
         */

    }

    // Exemplo obtido em https://github.com/mleoking/JavaGnuplotHybrid
    public void plotImage() {
        JGnuplot jg = new JGnuplot();
        Plot plot = new Plot("") {
            {
                xlabel = "x";
                ylabel = "y";
                zlabel = "z=x^2+y^2";
            }
        };
        DataTableSet dts = plot.addNewDataTableSet("Image Plot");
        //prepare data
        List x = new ArrayList(), y = new ArrayList(), z = new ArrayList();
        for (double i = -2; i <= 2; i += 0.5) {
            for (double j = -2; j <= 2; j += 0.5) {
                x.add(i);
                y.add(j);
                z.add(i * i + j * j);
            }
        }
        dts.addNewDataTable("z=x^2+y^2", x, y, z);
        jg.execute(plot, jg.plotImage);
    }
}

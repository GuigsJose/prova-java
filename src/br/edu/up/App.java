package br.edu.up;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) {
        List<Aluno> alunos = new ArrayList<>();

        // Ler o arquivo alunos.csv
        try (BufferedReader br = new BufferedReader(new FileReader("src/br/edu/up/alunos.csv"))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip the header line
                }
                // Substituir caracteres especiais, se necessário
                line = line.replaceAll("�", "í");

                String[] values = line.split(";");
                if (values.length == 3) {
                    try {
                        int matricula = Integer.parseInt(values[0].trim());
                        String nome = values[1].trim();
                        double nota = Double.parseDouble(values[2].trim().replace(",", ".")); // Corrigir para substituir vírgulas por pontos
                        alunos.add(new Aluno(matricula, nome, nota));
                    } catch (NumberFormatException e) {
                        System.err.println("Erro ao converter os valores: " + line);
                    }
                } else {
                    System.err.println("Linha com formato incorreto: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Exibir quantidade de alunos lidos
        int totalAlunos = alunos.size();
        System.out.println("Total de alunos lidos: " + totalAlunos);

        // Processar os dados
        int aprovados = 0;
        int reprovados = 0;
        double menorNota = Double.MAX_VALUE;
        double maiorNota = Double.MIN_VALUE;
        double somaNotas = 0.0;

        for (Aluno aluno : alunos) {
            double nota = aluno.getNota();
            if (nota >= 6.0) {
                aprovados++;
            } else {
                reprovados++;
            }
            if (nota < menorNota) {
                menorNota = nota;
            }
            if (nota > maiorNota) {
                maiorNota = nota;
            }
            somaNotas += nota;
        }

        double mediaGeral = somaNotas / totalAlunos;

        // Escrever o arquivo resumo.csv no formato desejado
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("src/br/edu/up/resumo.csv"))) {
            bw.write("quantidadealunos;aprovados;reprovados;menornota;maiornota;mediageral\n");
            bw.write(totalAlunos + ";" + aprovados + ";" + reprovados + ";" + menorNota + ";" + maiorNota + ";" + mediaGeral + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }    }
}

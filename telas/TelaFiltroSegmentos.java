package telas;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class TelaFiltroSegmentos extends JFrame {
    private List<TelaSegmentacao.Segmento> todosSegmentos;
    private DefaultTableModel modeloTabela;

    public TelaFiltroSegmentos(List<TelaSegmentacao.Segmento> segmentos) {
        this.todosSegmentos = segmentos;

        setTitle("Filtrar Segmentos");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JTabbedPane abas = new JTabbedPane();

        abas.addTab("Por Nome ou Descrição", criarAbaNomeDescricao());
        abas.addTab("Por Status", criarAbaStatus());

        add(abas, BorderLayout.CENTER);
    }

    private JPanel criarAbaNomeDescricao() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField campoFiltro = new JTextField();
        JButton btnFiltrar = new JButton("Filtrar");

        JPanel painelTopo = new JPanel(new BorderLayout(5, 5));
        painelTopo.add(new JLabel("Digite o termo:"), BorderLayout.WEST);
        painelTopo.add(campoFiltro, BorderLayout.CENTER);
        painelTopo.add(btnFiltrar, BorderLayout.EAST);

        modeloTabela = criarModeloTabela();
        JTable tabela = new JTable(modeloTabela);
        JScrollPane scroll = new JScrollPane(tabela);

        painel.add(painelTopo, BorderLayout.NORTH);
        painel.add(scroll, BorderLayout.CENTER);

        btnFiltrar.addActionListener(e -> {
            String termo = campoFiltro.getText().toLowerCase().trim();
            List<TelaSegmentacao.Segmento> filtrados = todosSegmentos.stream()
                    .filter(s -> s.getNome().toLowerCase().contains(termo) ||
                            s.getDescricao().toLowerCase().contains(termo))
                    .collect(Collectors.toList());
            atualizarTabela(filtrados);
        });

        return painel;
    }

    private JPanel criarAbaStatus() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JComboBox<String> comboStatus = new JComboBox<>(new String[]{
                "Todos", "Pronto para enviar", "Em rascunho", "Inativo"
        });

        JButton btnFiltrar = new JButton("Filtrar");

        JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelTopo.add(new JLabel("Status:"));
        painelTopo.add(comboStatus);
        painelTopo.add(btnFiltrar);

        modeloTabela = criarModeloTabela();
        JTable tabela = new JTable(modeloTabela);
        JScrollPane scroll = new JScrollPane(tabela);

        painel.add(painelTopo, BorderLayout.NORTH);
        painel.add(scroll, BorderLayout.CENTER);

        btnFiltrar.addActionListener(e -> {
            String statusSelecionado = comboStatus.getSelectedItem().toString();
            List<TelaSegmentacao.Segmento> filtrados;

            if (statusSelecionado.equals("Todos")) {
                filtrados = todosSegmentos;
            } else {
                filtrados = todosSegmentos.stream()
                        .filter(s -> s.getStatus().equalsIgnoreCase(statusSelecionado))
                        .collect(Collectors.toList());
            }

            atualizarTabela(filtrados);
        });

        return painel;
    }

    private DefaultTableModel criarModeloTabela() {
        return new DefaultTableModel(new Object[]{"", "Nome", "Contagem de uso", "Destinatários", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
    }

    private void atualizarTabela(List<TelaSegmentacao.Segmento> lista) {
        modeloTabela.setRowCount(0);
        for (TelaSegmentacao.Segmento seg : lista) {
            modeloTabela.addRow(seg.toTableRow());
        }
    }

    // Método de teste opcional para executar só essa tela:
    public static void main(String[] args) {
        List<TelaSegmentacao.Segmento> mock = new ArrayList<>();
        mock.add(new TelaSegmentacao.Segmento("VIP", "Clientes com alto ticket", "Pronto para enviar"));
        mock.add(new TelaSegmentacao.Segmento("Inativos", "Sem compra recente", "Inativo"));

        SwingUtilities.invokeLater(() -> new TelaFiltroSegmentos(mock).setVisible(true));
    }
}

package br.edu.ifnmg.poo.opala;

import helper.UpperCaseDocument;
import br.edu.ifnmg.poo.credential.Credential;
import br.edu.ifnmg.poo.driver.Driver;
import br.edu.ifnmg.poo.driver.DriverDAO;
import br.edu.ifnmg.poo.parkingSpace.ParkingSpace;
import br.edu.ifnmg.poo.parkingSpace.ParkingSpaceDAO;
import br.edu.ifnmg.poo.repository.DbConnection;
import br.edu.ifnmg.poo.vehicle.Vehicle;
import br.edu.ifnmg.poo.vehicle.VehicleDAO;
import com.formdev.flatlaf.util.SystemInfo;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author bvan &lt;Bruno Vinícius at ifnmg&gt;
 */
public class MainScreen extends javax.swing.JFrame {

    private static MainScreen instance;
    private final CardLayout cardMainScreen;
    private JButton lastClickedButton;

    /**
     * Creates new form MainScreen
     */
    public MainScreen() {
        initComponents();
        setLocationRelativeTo(null);
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        cardMainScreen = (CardLayout) pnlMain.getLayout();

        //Transparent title bar on macos
//        if( SystemInfo.isMacFullWindowContentSupported )
//            getRootPane().putClientProperty( "apple.awt.transparentTitleBar", true );
        if (SystemInfo.isMacFullWindowContentSupported) {
            getRootPane().putClientProperty("apple.awt.fullWindowContent", true);
            getRootPane().putClientProperty("apple.awt.transparentTitleBar", true);
        }

        DefaultTableModel model = (DefaultTableModel) tableDriver.getModel();
        tableDriver.setRowSorter(new TableRowSorter(model));
        try{
            SetTxtVagaDefaultValue(GetNextFreeParkingSpot());
        } catch (SQLException e) {
            return;
        }
        
        FillTable(tableDriver);
        FillCbPlaceComboBox();
        setCheckInTimeToNow();

    }

    public MainScreen(Credential credential) {
        this();
        switch (credential.getType()) {
            case ADMIN -> {
                this.btnHome.setEnabled(true);
                this.btnPayment.setEnabled(true);
                this.btnSubscriber.setEnabled(true);
                this.btnUser.setEnabled(true);
            }
            case OPERATOR -> {
                this.btnHome.setEnabled(true);
                this.btnPayment.setEnabled(true);
                this.btnSubscriber.setEnabled(true);
            }
            case SUBSCRIBER -> {
                btnRelatorioActionPerformed(null);
            }
        }
        this.setTitle(this.getTitle() + " - " + credential.getUser().getName());
    }

    public static MainScreen getInstance() {
        if (instance == null) {
            instance = new MainScreen();
        }
        return instance;
    }

    private void changeColorButton(JButton button) {
        if (lastClickedButton != null) {
            lastClickedButton.setBackground(null);
        }
        button.setBackground(Color.decode("#0085FF"));
        lastClickedButton = button;
    }

    public final void FillTable(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setNumRows(0);

        VehicleDAO vDao = new VehicleDAO();

        List<Vehicle> vList = vDao.findAll();
        ParkingSpaceDAO pDao = new ParkingSpaceDAO();

        if (vList != null) {
            for (Vehicle v : vList) {
                ParkingSpace p = pDao.findById(v.getId());
                model.addRow(new Object[]{
                    v.getLicensePlate(),
                    p.getNumber(),
                    v.getStringType(),
                    p.getArrivalTime(),
                    v.getNote()
                });
            }
        }
    }
    
    public int GetNextFreeParkingSpot() throws SQLException {
        int firstAvailableSpot = 1;
        try {
            Connection connection = DbConnection.getConnection();

            // Recupere todas as vagas utilizadas
            String usedSpotsQuery = "SELECT number FROM ParkingSpace";
            try (PreparedStatement usedSpotsStatement = connection.prepareStatement(usedSpotsQuery)) {
                try (ResultSet usedSpotsResult = usedSpotsStatement.executeQuery()) {

                    // Armazene as vagas utilizadas em uma lista
                    List<Integer> usedSpots = new ArrayList<>();
                    while (usedSpotsResult.next()) {
                        usedSpots.add(usedSpotsResult.getInt("number"));
                    }

                    // Encontre a primeira vaga livre
                    
                    for (int i = 1; i <= usedSpots.size(); i++) {
                        if (!usedSpots.contains(i)) {
                            firstAvailableSpot = i;
                            break;
                        }
                    }
                    
                    // Se todas as vagas estão ocupadas, a próxima é a última utilizada + 1
                    if (firstAvailableSpot == 1 && !usedSpots.isEmpty()) {
                        firstAvailableSpot = usedSpots.get(usedSpots.size() - 1) + 1;
                    }

                    // Retorne o número da primeira vaga livre
                    return firstAvailableSpot;
                }
            }
        } catch (SQLException e) {
            // Trate a exceção conforme necessário
            e.printStackTrace(); // TODO: Melhorar isso aqui
            return -1;
        }
    }

    public void SetTxtVagaDefaultValue(int firstAvailableSpot) {
        String firstAvailableSpotStr = String.valueOf(firstAvailableSpot);
        txtVaga.setText(firstAvailableSpotStr);
        txtVaga.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtVaga.getText().equals(firstAvailableSpotStr)) {
                    txtVaga.setText(""); // Limpar o texto padrão ao receber o foco
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txtVaga.getText().isEmpty()) {
                    txtVaga.setText(firstAvailableSpotStr); // Restaurar o texto padrão se o campo estiver vazio
                }
            }
        });
    }

    public final void setCheckInTimeToNow() {

        txtCheckInTime.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

                if ("  :  ".equals(txtCheckInTime.getText()) ) {
                    // Set txtCheckInTime to now only if it's empty
                    LocalTime horaAtual = LocalTime.now();
                    DateTimeFormatter formato = DateTimeFormatter.ofPattern("HH:mm");
                    String horaFormatada = horaAtual.format(formato);
                    txtCheckInTime.setText(horaFormatada);

                } else {
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
            }
        });
    }

    
    public final void FillCbPlaceComboBox() {
        cBplace.addItem("CARRO");
        cBplace.addItem("MOTO");
        cBplace.addItem("BICICLETA");
        cBplace.addItem("CAMINHONETE");
        cBplace.addItem("CAMINHAO");
        cBplace.addItem("ONIBUS");
        cBplace.addItem("TANK");
        cBplace.addItem("HELICOPTER");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlMenu = new javax.swing.JPanel();
        btnHome = new javax.swing.JButton();
        btnPayment = new javax.swing.JButton();
        btnRelatorio = new javax.swing.JButton();
        btnSubscriber = new javax.swing.JButton();
        btnUser = new javax.swing.JButton();
        btnLogout = new javax.swing.JButton();
        pnlMain = new javax.swing.JPanel();
        pnlHome = new javax.swing.JPanel();
        EntradaDeVeiculosjPanel = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        txtVaga = new javax.swing.JTextField();
        cBplace = new javax.swing.JComboBox<>();
        lblEntrada = new javax.swing.JLabel();
        txtSaida = new javax.swing.JLabel();
        btnSave = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        lblNote = new javax.swing.JLabel();
        FieldNotes = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtPlaca = new javax.swing.JFormattedTextField();
        txtCheckInTime = new javax.swing.JFormattedTextField();
        txtCheckOutTime = new javax.swing.JFormattedTextField();
        EstacionamentojPanel = new javax.swing.JPanel();
        scrPaneLista = new javax.swing.JScrollPane();
        tableDriver = new javax.swing.JTable();
        lblEstacionamento = new javax.swing.JLabel();
        pnlPayment = new javax.swing.JPanel();
        lblCaixa = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        ValoresBotoes = new javax.swing.JPanel();
        btnTotal = new javax.swing.JButton();
        btnPix = new javax.swing.JButton();
        btnDinheiro = new javax.swing.JButton();
        btnCartao = new javax.swing.JButton();
        scrPnLista = new javax.swing.JScrollPane();
        tblLista = new javax.swing.JTable();
        pnlRelatorio = new javax.swing.JPanel();
        btnBaixarMensal = new javax.swing.JButton();
        btnBaixarDiario = new javax.swing.JButton();
        lblRelatorio = new javax.swing.JLabel();
        pnlSubscriber = new javax.swing.JPanel();
        lblMensalista = new javax.swing.JLabel();
        scrPaneLista1 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        pnlSearchIcon = new javax.swing.JPanel();
        lblSearchIcon = new javax.swing.JLabel();
        txtSearchSubscriber = new javax.swing.JTextField();
        btnAddSubscriber = new javax.swing.JButton();
        pnlUser = new javax.swing.JPanel();
        lblUser = new javax.swing.JLabel();
        scrPaneLista2 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        pnlSearchIconUser = new javax.swing.JPanel();
        lblSearchIconUser = new javax.swing.JLabel();
        txtSearchUser = new javax.swing.JTextField();
        btnAddUser = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("OPALA Parking Lot Management System");
        setBackground(new java.awt.Color(38, 50, 56));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        pnlMenu.setBackground(new java.awt.Color(0, 57, 99));

        btnHome.setBackground(new java.awt.Color(0, 57, 99));
        btnHome.setForeground(new java.awt.Color(255, 255, 255));
        btnHome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Home.png"))); // NOI18N
        btnHome.setBorderPainted(false);
        btnHome.setEnabled(false);
        btnHome.setFocusPainted(false);
        btnHome.setFocusable(false);
        btnHome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHomeActionPerformed(evt);
            }
        });

        btnPayment.setBackground(new java.awt.Color(0, 57, 99));
        btnPayment.setForeground(new java.awt.Color(255, 255, 255));
        btnPayment.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Cash.png"))); // NOI18N
        btnPayment.setBorderPainted(false);
        btnPayment.setEnabled(false);
        btnPayment.setFocusPainted(false);
        btnPayment.setFocusable(false);
        btnPayment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPaymentActionPerformed(evt);
            }
        });

        btnRelatorio.setBackground(new java.awt.Color(0, 57, 99));
        btnRelatorio.setForeground(new java.awt.Color(255, 255, 255));
        btnRelatorio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Analyze.png"))); // NOI18N
        btnRelatorio.setBorderPainted(false);
        btnRelatorio.setFocusPainted(false);
        btnRelatorio.setFocusable(false);
        btnRelatorio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRelatorioActionPerformed(evt);
            }
        });

        btnSubscriber.setBackground(new java.awt.Color(0, 57, 99));
        btnSubscriber.setForeground(new java.awt.Color(255, 255, 255));
        btnSubscriber.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Tear-Off Calendar.png"))); // NOI18N
        btnSubscriber.setBorderPainted(false);
        btnSubscriber.setEnabled(false);
        btnSubscriber.setFocusPainted(false);
        btnSubscriber.setFocusable(false);
        btnSubscriber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubscriberActionPerformed(evt);
            }
        });

        btnUser.setBackground(new java.awt.Color(0, 57, 99));
        btnUser.setForeground(new java.awt.Color(255, 255, 255));
        btnUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Admin Settings Male.png"))); // NOI18N
        btnUser.setBorderPainted(false);
        btnUser.setEnabled(false);
        btnUser.setFocusPainted(false);
        btnUser.setFocusable(false);
        btnUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUserActionPerformed(evt);
            }
        });

        btnLogout.setBackground(new java.awt.Color(255, 0, 51));
        btnLogout.setForeground(new java.awt.Color(255, 255, 255));
        btnLogout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Logout.png"))); // NOI18N
        btnLogout.setBorderPainted(false);
        btnLogout.setFocusPainted(false);
        btnLogout.setFocusable(false);
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlMenuLayout = new javax.swing.GroupLayout(pnlMenu);
        pnlMenu.setLayout(pnlMenuLayout);
        pnlMenuLayout.setHorizontalGroup(
            pnlMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnHome, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
            .addComponent(btnPayment, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnSubscriber, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnRelatorio, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnUser, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnLogout, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlMenuLayout.setVerticalGroup(
            pnlMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMenuLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(btnHome, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPayment, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSubscriber, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRelatorio, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnUser, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnLogout, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnlMain.setBackground(new java.awt.Color(255, 255, 255));
        pnlMain.setForeground(new java.awt.Color(255, 255, 255));
        pnlMain.setLayout(new java.awt.CardLayout());

        pnlHome.setBackground(new java.awt.Color(255, 255, 255));

        EntradaDeVeiculosjPanel.setBackground(new java.awt.Color(255, 255, 255));

        lblTitle.setBackground(new java.awt.Color(0, 133, 255));
        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(0, 133, 255));
        lblTitle.setText("Entrada de Veículos");

        txtVaga.setBackground(new java.awt.Color(255, 255, 255));
        txtVaga.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtVaga.setForeground(new java.awt.Color(0, 0, 0));
        txtVaga.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 133, 255), 1, true));
        txtVaga.setCaretColor(new java.awt.Color(157, 230, 178));
        txtVaga.setPreferredSize(new java.awt.Dimension(65, 25));
        txtVaga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtVagaActionPerformed(evt);
            }
        });

        cBplace.setBackground(new java.awt.Color(255, 255, 255));
        cBplace.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cBplace.setForeground(new java.awt.Color(0, 0, 0));
        cBplace.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 133, 255), 1, true));
        cBplace.setPreferredSize(new java.awt.Dimension(65, 25));
        cBplace.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cBplaceActionPerformed(evt);
            }
        });

        lblEntrada.setBackground(new java.awt.Color(0, 133, 255));
        lblEntrada.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblEntrada.setForeground(new java.awt.Color(0, 133, 255));
        lblEntrada.setText("Entrada:");

        txtSaida.setBackground(new java.awt.Color(0, 133, 255));
        txtSaida.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        txtSaida.setForeground(new java.awt.Color(0, 133, 255));
        txtSaida.setText("Saida:");

        btnSave.setBackground(new java.awt.Color(0, 133, 255));
        btnSave.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnSave.setForeground(new java.awt.Color(255, 255, 255));
        btnSave.setText("Salvar");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnCancel.setBackground(new java.awt.Color(255, 164, 164));
        btnCancel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(255, 0, 0));
        btnCancel.setText("Cancelar");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        lblNote.setBackground(new java.awt.Color(0, 133, 255));
        lblNote.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblNote.setForeground(new java.awt.Color(0, 133, 255));
        lblNote.setText("Observações:");

        FieldNotes.setBackground(new java.awt.Color(255, 255, 255));
        FieldNotes.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        FieldNotes.setForeground(new java.awt.Color(0, 0, 0));
        FieldNotes.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 133, 255)));
        FieldNotes.setCaretColor(new java.awt.Color(75, 110, 175));
        FieldNotes.setPreferredSize(new java.awt.Dimension(65, 25));
        FieldNotes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FieldNotesActionPerformed(evt);
            }
        });

        jLabel1.setBackground(new java.awt.Color(0, 0, 0));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Placa:");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Vaga:");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("Tipo de Veículo");

        UpperCaseDocument ucd = new UpperCaseDocument();
        txtPlaca.setDocument(ucd);
        txtPlaca.setBackground(new java.awt.Color(255, 255, 255));
        txtPlaca.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 133, 255)));
        try {
            txtPlaca.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("***-****")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtPlaca.setFont(new java.awt.Font("Helvetica Neue", 0, 36)); // NOI18N

        txtCheckInTime.setBackground(new java.awt.Color(255, 255, 255));
        txtCheckInTime.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 133, 255)));
        try {
            txtCheckInTime.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##:##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtCheckInTime.setToolTipText("");
        txtCheckInTime.setCaretColor(new java.awt.Color(75, 110, 175));
        txtCheckInTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCheckInTimeActionPerformed(evt);
            }
        });

        txtCheckOutTime.setBackground(new java.awt.Color(255, 255, 255));
        txtCheckOutTime.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 133, 255)));
        try {
            txtCheckOutTime.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##:##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        javax.swing.GroupLayout EntradaDeVeiculosjPanelLayout = new javax.swing.GroupLayout(EntradaDeVeiculosjPanel);
        EntradaDeVeiculosjPanel.setLayout(EntradaDeVeiculosjPanelLayout);
        EntradaDeVeiculosjPanelLayout.setHorizontalGroup(
            EntradaDeVeiculosjPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(FieldNotes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(txtPlaca)
            .addGroup(EntradaDeVeiculosjPanelLayout.createSequentialGroup()
                .addGroup(EntradaDeVeiculosjPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(EntradaDeVeiculosjPanelLayout.createSequentialGroup()
                        .addGroup(EntradaDeVeiculosjPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCheckInTime, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblEntrada))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(EntradaDeVeiculosjPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSaida)
                            .addComponent(txtCheckOutTime)))
                    .addGroup(EntradaDeVeiculosjPanelLayout.createSequentialGroup()
                        .addGap(109, 109, 109)
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(EntradaDeVeiculosjPanelLayout.createSequentialGroup()
                        .addGap(63, 63, 63)
                        .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblTitle)
                    .addComponent(lblNote)
                    .addComponent(jLabel1)
                    .addGroup(EntradaDeVeiculosjPanelLayout.createSequentialGroup()
                        .addGroup(EntradaDeVeiculosjPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(txtVaga, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(EntradaDeVeiculosjPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(cBplace, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        EntradaDeVeiculosjPanelLayout.setVerticalGroup(
            EntradaDeVeiculosjPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(EntradaDeVeiculosjPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitle)
                .addGap(14, 14, 14)
                .addComponent(jLabel1)
                .addGap(0, 0, 0)
                .addComponent(txtPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(EntradaDeVeiculosjPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(EntradaDeVeiculosjPanelLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(0, 0, 0)
                        .addGroup(EntradaDeVeiculosjPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cBplace, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtVaga, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addGroup(EntradaDeVeiculosjPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblEntrada)
                            .addComponent(txtSaida)))
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(EntradaDeVeiculosjPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCheckInTime, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCheckOutTime, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(lblNote)
                .addGap(0, 0, 0)
                .addComponent(FieldNotes, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(106, 106, 106)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSave, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE))
        );

        EstacionamentojPanel.setBackground(new java.awt.Color(255, 255, 255));

        tableDriver.setBackground(new java.awt.Color(255, 255, 255));
        tableDriver.setRowSelectionAllowed(true);
        tableDriver.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableDriver.setForeground(new java.awt.Color(0, 133, 255));
        tableDriver.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Placa", "Vaga", "Tipo Veículo", "Horário de Entrada", "Observação"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableDriver.setGridColor(new java.awt.Color(255, 255, 255));
        tableDriver.setSelectionBackground(new java.awt.Color(223, 249, 255));
        tableDriver.setSelectionForeground(new java.awt.Color(0, 0, 0));
        scrPaneLista.setViewportView(tableDriver);

        lblEstacionamento.setBackground(new java.awt.Color(0, 133, 255));
        lblEstacionamento.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblEstacionamento.setForeground(new java.awt.Color(0, 133, 255));
        lblEstacionamento.setText("Estacionamento");

        javax.swing.GroupLayout EstacionamentojPanelLayout = new javax.swing.GroupLayout(EstacionamentojPanel);
        EstacionamentojPanel.setLayout(EstacionamentojPanelLayout);
        EstacionamentojPanelLayout.setHorizontalGroup(
            EstacionamentojPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(EstacionamentojPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(EstacionamentojPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblEstacionamento)
                    .addComponent(scrPaneLista, javax.swing.GroupLayout.DEFAULT_SIZE, 1031, Short.MAX_VALUE))
                .addContainerGap())
        );
        EstacionamentojPanelLayout.setVerticalGroup(
            EstacionamentojPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(EstacionamentojPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblEstacionamento)
                .addGap(18, 18, 18)
                .addComponent(scrPaneLista))
        );

        javax.swing.GroupLayout pnlHomeLayout = new javax.swing.GroupLayout(pnlHome);
        pnlHome.setLayout(pnlHomeLayout);
        pnlHomeLayout.setHorizontalGroup(
            pnlHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHomeLayout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addComponent(EntradaDeVeiculosjPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(EstacionamentojPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(70, 70, 70))
        );
        pnlHomeLayout.setVerticalGroup(
            pnlHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHomeLayout.createSequentialGroup()
                .addGap(100, 100, 100)
                .addGroup(pnlHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(EstacionamentojPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(EntradaDeVeiculosjPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(80, 80, 80))
        );

        pnlMain.add(pnlHome, "Home");

        pnlPayment.setBackground(new java.awt.Color(255, 255, 255));

        lblCaixa.setBackground(new java.awt.Color(0, 133, 255));
        lblCaixa.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblCaixa.setForeground(new java.awt.Color(0, 133, 255));
        lblCaixa.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCaixa.setText("Movimentação");
        lblCaixa.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        ValoresBotoes.setBackground(new java.awt.Color(255, 255, 255));

        btnTotal.setBackground(new java.awt.Color(0, 57, 99));
        btnTotal.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btnTotal.setForeground(new java.awt.Color(255, 255, 255));
        btnTotal.setText("TOTAL");
        btnTotal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(87, 174, 255)));

        btnPix.setBackground(new java.awt.Color(0, 57, 99));
        btnPix.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btnPix.setForeground(new java.awt.Color(255, 255, 255));
        btnPix.setText("PIX");
        btnPix.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(87, 174, 255)));
        btnPix.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPixActionPerformed(evt);
            }
        });

        btnDinheiro.setBackground(new java.awt.Color(0, 57, 99));
        btnDinheiro.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btnDinheiro.setForeground(new java.awt.Color(255, 255, 255));
        btnDinheiro.setText("DINHEIRO");
        btnDinheiro.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(87, 174, 255)));

        btnCartao.setBackground(new java.awt.Color(0, 57, 99));
        btnCartao.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btnCartao.setForeground(new java.awt.Color(255, 255, 255));
        btnCartao.setText("CARTÃO");
        btnCartao.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(87, 174, 255)));

        javax.swing.GroupLayout ValoresBotoesLayout = new javax.swing.GroupLayout(ValoresBotoes);
        ValoresBotoes.setLayout(ValoresBotoesLayout);
        ValoresBotoesLayout.setHorizontalGroup(
            ValoresBotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ValoresBotoesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnPix, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 86, Short.MAX_VALUE)
                .addComponent(btnDinheiro, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 85, Short.MAX_VALUE)
                .addComponent(btnCartao, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 84, Short.MAX_VALUE)
                .addComponent(btnTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        ValoresBotoesLayout.setVerticalGroup(
            ValoresBotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ValoresBotoesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ValoresBotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDinheiro, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPix, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCartao, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        tblLista.setBackground(new java.awt.Color(255, 255, 255));
        tblLista.setForeground(new java.awt.Color(138, 182, 109));
        tblLista.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Vaga", "Placa", "Veículo", "Horario Entrada", "Data Entrada"
            }
        ));
        tblLista.setGridColor(new java.awt.Color(255, 255, 255));
        tblLista.setSelectionBackground(new java.awt.Color(223, 249, 255));
        tblLista.setSelectionForeground(new java.awt.Color(255, 255, 255));
        scrPnLista.setViewportView(tblLista);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(111, 111, 111)
                .addComponent(ValoresBotoes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(116, 116, 116))
            .addComponent(scrPnLista)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(ValoresBotoes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(scrPnLista, javax.swing.GroupLayout.DEFAULT_SIZE, 448, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlPaymentLayout = new javax.swing.GroupLayout(pnlPayment);
        pnlPayment.setLayout(pnlPaymentLayout);
        pnlPaymentLayout.setHorizontalGroup(
            pnlPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPaymentLayout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(80, 80, 80))
            .addGroup(pnlPaymentLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblCaixa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlPaymentLayout.setVerticalGroup(
            pnlPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPaymentLayout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addComponent(lblCaixa, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(118, 118, 118))
        );

        pnlMain.add(pnlPayment, "Payment");

        pnlRelatorio.setBackground(new java.awt.Color(255, 255, 255));

        btnBaixarMensal.setBackground(new java.awt.Color(0, 133, 255));
        btnBaixarMensal.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btnBaixarMensal.setForeground(new java.awt.Color(255, 255, 255));
        btnBaixarMensal.setText("BAIXAR RELATORIO MENSAL (PDF)");

        btnBaixarDiario.setBackground(new java.awt.Color(0, 133, 255));
        btnBaixarDiario.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btnBaixarDiario.setForeground(new java.awt.Color(255, 255, 255));
        btnBaixarDiario.setText("BAIXAR RELATORIO DIÁRIO (PDF)");

        lblRelatorio.setBackground(new java.awt.Color(138, 182, 109));
        lblRelatorio.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblRelatorio.setForeground(new java.awt.Color(0, 133, 255));
        lblRelatorio.setText("RELATÓRIO");

        javax.swing.GroupLayout pnlRelatorioLayout = new javax.swing.GroupLayout(pnlRelatorio);
        pnlRelatorio.setLayout(pnlRelatorioLayout);
        pnlRelatorioLayout.setHorizontalGroup(
            pnlRelatorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRelatorioLayout.createSequentialGroup()
                .addContainerGap(306, Short.MAX_VALUE)
                .addGroup(pnlRelatorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnBaixarDiario, javax.swing.GroupLayout.PREFERRED_SIZE, 891, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBaixarMensal, javax.swing.GroupLayout.PREFERRED_SIZE, 891, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(305, Short.MAX_VALUE))
            .addGroup(pnlRelatorioLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(lblRelatorio)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlRelatorioLayout.setVerticalGroup(
            pnlRelatorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRelatorioLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(lblRelatorio, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(185, 185, 185)
                .addComponent(btnBaixarMensal, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(100, 100, 100)
                .addComponent(btnBaixarDiario, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(306, Short.MAX_VALUE))
        );

        pnlMain.add(pnlRelatorio, "Relatorio");

        pnlSubscriber.setBackground(new java.awt.Color(255, 255, 255));

        lblMensalista.setBackground(new java.awt.Color(0, 133, 255));
        lblMensalista.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblMensalista.setForeground(new java.awt.Color(0, 133, 255));
        lblMensalista.setText("MENSALISTA");

        jTable2.setBackground(new java.awt.Color(255, 255, 255));
        jTable2.setForeground(new java.awt.Color(0, 133, 255));
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Vaga", "Placa", "Veículo", "Horario Entrada", "Data Entrada"
            }
        ));
        jTable2.setGridColor(new java.awt.Color(255, 255, 255));
        jTable2.setSelectionBackground(new java.awt.Color(223, 249, 255));
        jTable2.setSelectionForeground(new java.awt.Color(255, 255, 255));
        scrPaneLista1.setViewportView(jTable2);

        pnlSearchIcon.setBackground(new java.awt.Color(0, 133, 255));

        lblSearchIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Search.png"))); // NOI18N

        javax.swing.GroupLayout pnlSearchIconLayout = new javax.swing.GroupLayout(pnlSearchIcon);
        pnlSearchIcon.setLayout(pnlSearchIconLayout);
        pnlSearchIconLayout.setHorizontalGroup(
            pnlSearchIconLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSearchIconLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(lblSearchIcon)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        pnlSearchIconLayout.setVerticalGroup(
            pnlSearchIconLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblSearchIcon, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
        );

        txtSearchSubscriber.setBackground(new java.awt.Color(223, 249, 255));
        txtSearchSubscriber.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtSearchSubscriber.setForeground(new java.awt.Color(0, 0, 0));
        txtSearchSubscriber.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 133, 255)));
        txtSearchSubscriber.setCaretColor(new java.awt.Color(157, 230, 178));
        txtSearchSubscriber.setPreferredSize(new java.awt.Dimension(65, 25));

        btnAddSubscriber.setBackground(new java.awt.Color(0, 133, 255));
        btnAddSubscriber.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnAddSubscriber.setForeground(new java.awt.Color(255, 255, 255));
        btnAddSubscriber.setText("ADICIONAR");

        javax.swing.GroupLayout pnlSubscriberLayout = new javax.swing.GroupLayout(pnlSubscriber);
        pnlSubscriber.setLayout(pnlSubscriberLayout);
        pnlSubscriberLayout.setHorizontalGroup(
            pnlSubscriberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSubscriberLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlSubscriberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(scrPaneLista1, javax.swing.GroupLayout.PREFERRED_SIZE, 1200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlSubscriberLayout.createSequentialGroup()
                        .addGap(500, 500, 500)
                        .addComponent(txtSearchSubscriber, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(pnlSearchIcon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnAddSubscriber, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(pnlSubscriberLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblMensalista)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlSubscriberLayout.setVerticalGroup(
            pnlSubscriberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSubscriberLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblMensalista, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(100, 100, 100)
                .addGroup(pnlSubscriberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlSubscriberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txtSearchSubscriber, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(pnlSearchIcon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnAddSubscriber, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addComponent(scrPaneLista1, javax.swing.GroupLayout.DEFAULT_SIZE, 591, Short.MAX_VALUE)
                .addGap(50, 50, 50))
        );

        pnlMain.add(pnlSubscriber, "Subscriber");

        pnlUser.setBackground(new java.awt.Color(255, 255, 255));

        lblUser.setBackground(new java.awt.Color(0, 133, 255));
        lblUser.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblUser.setForeground(new java.awt.Color(0, 133, 255));
        lblUser.setText("USUÁRIO");

        jTable3.setBackground(new java.awt.Color(255, 255, 255));
        jTable3.setForeground(new java.awt.Color(0, 133, 255));
        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Vaga", "Placa", "Veículo", "Horario Entrada", "Data Entrada"
            }
        ));
        jTable3.setGridColor(new java.awt.Color(255, 255, 255));
        jTable3.setSelectionBackground(new java.awt.Color(223, 249, 255));
        jTable3.setSelectionForeground(new java.awt.Color(255, 255, 255));
        scrPaneLista2.setViewportView(jTable3);

        pnlSearchIconUser.setBackground(new java.awt.Color(0, 133, 255));

        lblSearchIconUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Search.png"))); // NOI18N

        javax.swing.GroupLayout pnlSearchIconUserLayout = new javax.swing.GroupLayout(pnlSearchIconUser);
        pnlSearchIconUser.setLayout(pnlSearchIconUserLayout);
        pnlSearchIconUserLayout.setHorizontalGroup(
            pnlSearchIconUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSearchIconUserLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(lblSearchIconUser)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        pnlSearchIconUserLayout.setVerticalGroup(
            pnlSearchIconUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblSearchIconUser, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
        );

        txtSearchUser.setBackground(new java.awt.Color(223, 249, 255));
        txtSearchUser.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtSearchUser.setForeground(new java.awt.Color(0, 0, 0));
        txtSearchUser.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 133, 255)));
        txtSearchUser.setCaretColor(new java.awt.Color(157, 230, 178));
        txtSearchUser.setPreferredSize(new java.awt.Dimension(65, 25));

        btnAddUser.setBackground(new java.awt.Color(0, 133, 255));
        btnAddUser.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnAddUser.setForeground(new java.awt.Color(255, 255, 255));
        btnAddUser.setText("ADICIONAR");

        javax.swing.GroupLayout pnlUserLayout = new javax.swing.GroupLayout(pnlUser);
        pnlUser.setLayout(pnlUserLayout);
        pnlUserLayout.setHorizontalGroup(
            pnlUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlUserLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(scrPaneLista2, javax.swing.GroupLayout.PREFERRED_SIZE, 1200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlUserLayout.createSequentialGroup()
                        .addGap(500, 500, 500)
                        .addComponent(txtSearchUser, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(pnlSearchIconUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnAddUser, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(pnlUserLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblUser)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlUserLayout.setVerticalGroup(
            pnlUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlUserLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblUser, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(100, 100, 100)
                .addGroup(pnlUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txtSearchUser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(pnlSearchIconUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnAddUser, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addComponent(scrPaneLista2, javax.swing.GroupLayout.DEFAULT_SIZE, 591, Short.MAX_VALUE)
                .addGap(50, 50, 50))
        );

        pnlMain.add(pnlUser, "User");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(pnlMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(pnlMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlMenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlMain, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        LoginScreen.getInstance().setVisible(true);
    }//GEN-LAST:event_formWindowClosing

    private void btnHomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHomeActionPerformed
        // TODO add your handling code here:
        cardMainScreen.show(pnlMain, "Home");
        changeColorButton(btnHome);
    }//GEN-LAST:event_btnHomeActionPerformed

    private void btnPaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPaymentActionPerformed
        // TODO add your handling code here:
        cardMainScreen.show(pnlMain, "Payment");
        changeColorButton(btnPayment);
    }//GEN-LAST:event_btnPaymentActionPerformed

    private void btnRelatorioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRelatorioActionPerformed
        // TODO add your handling code here:
        cardMainScreen.show(pnlMain, "Relatorio");
        changeColorButton(btnRelatorio);
    }//GEN-LAST:event_btnRelatorioActionPerformed

    private void btnSubscriberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubscriberActionPerformed
        // TODO add your handling code here:
        cardMainScreen.show(pnlMain, "Subscriber");
        changeColorButton(btnSubscriber);
    }//GEN-LAST:event_btnSubscriberActionPerformed

    private void btnUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUserActionPerformed
        // TODO add your handling code here:
        cardMainScreen.show(pnlMain, "User");
        changeColorButton(btnUser);
    }//GEN-LAST:event_btnUserActionPerformed

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        // TODO add your handling code here:
        this.dispose();
        formWindowClosing(null);
    }//GEN-LAST:event_btnLogoutActionPerformed

    private void btnPixActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPixActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPixActionPerformed

    private void FieldNotesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FieldNotesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_FieldNotesActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        txtPlaca.setText(null);
        FieldNotes.setText(null);
        txtVaga.setText(null);
        txtCheckInTime.setText(null);
        txtCheckOutTime.setText(null);
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        try {
            // TODO add your handling code here:
            try{
                SetTxtVagaDefaultValue(GetNextFreeParkingSpot());
            } catch (SQLException e) {
                return;
            }
            
            ParkingSpaceDAO pDao = new ParkingSpaceDAO();
            VehicleDAO vDao = new VehicleDAO();
            DriverDAO dDao = new DriverDAO();

            Driver driver = new Driver();
            Vehicle vehicle = new Vehicle();
            ParkingSpace ps = new ParkingSpace();

            driver.addParkingSpace(ps);
            driver.setTicket(1);
            dDao.saveOrUpdate(driver);

            vehicle.setLicensePlate(txtPlaca.getText());
            vehicle.setNote(FieldNotes.getText());
            switch (cBplace.getSelectedIndex()) {
                case 0:
                vehicle.setType(Vehicle.TypeVehicle.CARRO);
                break;
                case 1:
                vehicle.setType(Vehicle.TypeVehicle.MOTO);
                break;
                case 2:
                vehicle.setType(Vehicle.TypeVehicle.BICICLETA);
                break;
                case 3:
                vehicle.setType(Vehicle.TypeVehicle.CAMINHONETE);
                break;
                case 4:
                vehicle.setType(Vehicle.TypeVehicle.CAMINHAO);
                break;
                case 5:
                vehicle.setType(Vehicle.TypeVehicle.ONIBUS);
                break;
                case 6:
                vehicle.setType(Vehicle.TypeVehicle.TANK);
                break;
                case 7:
                vehicle.setType(Vehicle.TypeVehicle.HELICOPTER);
                break;
            }
            vehicle.setDriver(driver);
            vehicle.setId_driver(driver.getId());
            vDao.saveOrUpdate(vehicle);

            ps.setDriver(driver);
            ps.setId_driver(driver.getId());
            ps.setNumber(Short.parseShort(txtVaga.getText()));
            ps.setArrivalTime(txtCheckInTime.getText());
            ps.setDepartureTime(txtCheckOutTime.getText());
            pDao.saveOrUpdate(ps);
        } catch (Exception ex) {
            Logger.getLogger(MainScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
        FillTable(tableDriver);
        
        btnCancelActionPerformed(null);
    }//GEN-LAST:event_btnSaveActionPerformed

    private void cBplaceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cBplaceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cBplaceActionPerformed

    private void txtVagaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtVagaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtVagaActionPerformed

    private void txtCheckInTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCheckInTimeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCheckInTimeActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel EntradaDeVeiculosjPanel;
    private javax.swing.JPanel EstacionamentojPanel;
    private javax.swing.JTextField FieldNotes;
    private javax.swing.JPanel ValoresBotoes;
    private javax.swing.JButton btnAddSubscriber;
    private javax.swing.JButton btnAddUser;
    private javax.swing.JButton btnBaixarDiario;
    private javax.swing.JButton btnBaixarMensal;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnCartao;
    private javax.swing.JButton btnDinheiro;
    private javax.swing.JButton btnHome;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnPayment;
    private javax.swing.JButton btnPix;
    private javax.swing.JButton btnRelatorio;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSubscriber;
    private javax.swing.JButton btnTotal;
    private javax.swing.JButton btnUser;
    private javax.swing.JComboBox<String> cBplace;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JLabel lblCaixa;
    private javax.swing.JLabel lblEntrada;
    private javax.swing.JLabel lblEstacionamento;
    private javax.swing.JLabel lblMensalista;
    private javax.swing.JLabel lblNote;
    private javax.swing.JLabel lblRelatorio;
    private javax.swing.JLabel lblSearchIcon;
    private javax.swing.JLabel lblSearchIconUser;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblUser;
    private javax.swing.JPanel pnlHome;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JPanel pnlMenu;
    private javax.swing.JPanel pnlPayment;
    private javax.swing.JPanel pnlRelatorio;
    private javax.swing.JPanel pnlSearchIcon;
    private javax.swing.JPanel pnlSearchIconUser;
    private javax.swing.JPanel pnlSubscriber;
    private javax.swing.JPanel pnlUser;
    private javax.swing.JScrollPane scrPaneLista;
    private javax.swing.JScrollPane scrPaneLista1;
    private javax.swing.JScrollPane scrPaneLista2;
    private javax.swing.JScrollPane scrPnLista;
    private javax.swing.JTable tableDriver;
    private javax.swing.JTable tblLista;
    private javax.swing.JFormattedTextField txtCheckInTime;
    private javax.swing.JFormattedTextField txtCheckOutTime;
    private javax.swing.JFormattedTextField txtPlaca;
    private javax.swing.JLabel txtSaida;
    private javax.swing.JTextField txtSearchSubscriber;
    private javax.swing.JTextField txtSearchUser;
    private javax.swing.JTextField txtVaga;
    // End of variables declaration//GEN-END:variables
}

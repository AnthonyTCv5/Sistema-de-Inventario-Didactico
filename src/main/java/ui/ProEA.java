package ui;

import controller.LoginController;
import javax.swing.JOptionPane;
import model.Usuario;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
/**
 *
 * @author USER
 */
public class ProEA extends javax.swing.JPanel {

    private LoginController loginController = new LoginController();

    private javax.swing.JFrame frame;

    public ProEA(javax.swing.JFrame frame) {
        this.frame = frame;
        initComponents();
        Background_register.setVisible(false);
        BotonSalirRegister.setBackground(new java.awt.Color(255, 255, 255));
        BotonSalirLogin.setBackground(new java.awt.Color(255, 255, 255));
        configurarEventos();
    }

    private void configurarEventos() {
        RegistrarseLabelLogin.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        RegistrarseLabelLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Background_login.setVisible(false);
                Background_register.setVisible(true);
            }
        });

        IniciarSesionLabelRegister.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        IniciarSesionLabelRegister.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Background_register.setVisible(false);
                Background_login.setVisible(true);
            }
        });

        BotonIngresarLogin.addActionListener(evt -> {
            String usuario = CampoUserLogin.getText().trim();
            String contrasena = new String(CampoPasswLogin.getPassword()).trim();

            Usuario u = loginController.login(usuario, contrasena);
            if (u != null) {
                frame.dispose();
                MainFrame mainFrame = new MainFrame(u);
                mainFrame.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        BotonRegistrarRegister.addActionListener(evt -> {
            String usuario = CampoUserRegister.getText().trim();
            String contrasena = new String(CampoPasswRegister.getPassword()).trim();

            Usuario u = loginController.registrar(usuario, contrasena);
            if (u != null) {
                JOptionPane.showMessageDialog(this, "Usuario registrado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                Background_register.setVisible(false);
                Background_login.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Usuario ya existe o campos vacíos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        CampoUserLogin.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (CampoUserLogin.getText().equals("Ingrese su correo electronico")) {
                    CampoUserLogin.setText("");
                    CampoUserLogin.setForeground(new java.awt.Color(0, 0, 0));
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (CampoUserLogin.getText().isEmpty()) {
                    CampoUserLogin.setText("Ingrese su correo electronico");
                    CampoUserLogin.setForeground(new java.awt.Color(204, 204, 204));
                }
            }
        });

        CampoUserRegister.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (CampoUserRegister.getText().equals("Ingrese su correo electronico")) {
                    CampoUserRegister.setText("");
                    CampoUserRegister.setForeground(new java.awt.Color(0, 0, 0));
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (CampoUserRegister.getText().isEmpty()) {
                    CampoUserRegister.setText("Ingrese su correo electronico");
                    CampoUserRegister.setForeground(new java.awt.Color(204, 204, 204));
                }
            }
        });

        CampoPasswLogin.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (new String(CampoPasswLogin.getPassword()).equals("jPasswordField1")) {
                    CampoPasswLogin.setText("");
                    CampoPasswLogin.setForeground(new java.awt.Color(0, 0, 0));
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (new String(CampoPasswLogin.getPassword()).isEmpty()) {
                    CampoPasswLogin.setText("jPasswordField1");
                    CampoPasswLogin.setForeground(new java.awt.Color(204, 204, 204));
                }
            }
        });

        CampoPasswRegister.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (new String(CampoPasswRegister.getPassword()).equals("jPasswordField1")) {
                    CampoPasswRegister.setText("");
                    CampoPasswRegister.setForeground(new java.awt.Color(0, 0, 0));
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (new String(CampoPasswRegister.getPassword()).isEmpty()) {
                    CampoPasswRegister.setText("jPasswordField1");
                    CampoPasswRegister.setForeground(new java.awt.Color(204, 204, 204));
                }
            }
        });

        BotonSalirLogin.addActionListener(evt -> System.exit(0));
        BotonSalirRegister.addActionListener(evt -> System.exit(0));

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Background_login = new javax.swing.JPanel();
        LogoLogin = new javax.swing.JLabel();
        BackgroundLogoLogin = new javax.swing.JLabel();
        LogoSuperiorLogin = new javax.swing.JLabel();
        SesionLabel = new javax.swing.JLabel();
        UserLabelLogin = new javax.swing.JLabel();
        Separador2Login = new javax.swing.JSeparator();
        ContrasenaLabelLogin = new javax.swing.JLabel();
        CampoUserLogin = new javax.swing.JTextField();
        Separador1Login = new javax.swing.JSeparator();
        CampoPasswLogin = new javax.swing.JPasswordField();
        RegistrarseLabelLogin = new javax.swing.JLabel();
        Label1Registrarse = new javax.swing.JLabel();
        BotonSalirLogin = new javax.swing.JButton();
        BotonIngresarLogin = new javax.swing.JButton();
        Background_register = new javax.swing.JPanel();
        LogoRegister = new javax.swing.JLabel();
        BackgroundLogoRegister = new javax.swing.JLabel();
        LogoSuperiorRegister = new javax.swing.JLabel();
        RegisterLabel = new javax.swing.JLabel();
        UserLabelRegister = new javax.swing.JLabel();
        Separador2Register = new javax.swing.JSeparator();
        ContrasenaLabelRegister = new javax.swing.JLabel();
        CampoUserRegister = new javax.swing.JTextField();
        Separador1Register = new javax.swing.JSeparator();
        CampoPasswRegister = new javax.swing.JPasswordField();
        IniciarSesionLabelRegister = new javax.swing.JLabel();
        BotonRegistrarRegister = new javax.swing.JButton();
        Label1Logearse = new javax.swing.JLabel();
        BotonSalirRegister = new javax.swing.JButton();

        Background_login.setBackground(new java.awt.Color(255, 255, 255));
        Background_login.setForeground(new java.awt.Color(153, 153, 153));
        Background_login.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        LogoLogin.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LogoLogin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Captura.PNG"))); // NOI18N
        Background_login.add(LogoLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 30, 210, 160));

        BackgroundLogoLogin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Gemini_Generated_Image_hl1zjdhl1zjdhl1z.png"))); // NOI18N
        BackgroundLogoLogin.setText("jLabel4");
        Background_login.add(BackgroundLogoLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 0, 290, 500));

        LogoSuperiorLogin.setFont(new java.awt.Font("Roboto", 1, 24)); // NOI18N
        LogoSuperiorLogin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Captura PRO EA.PNG"))); // NOI18N
        LogoSuperiorLogin.setToolTipText("");
        Background_login.add(LogoSuperiorLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 170, 40));

        SesionLabel.setFont(new java.awt.Font("Roboto Black", 0, 24)); // NOI18N
        SesionLabel.setText("INICIAR SESIÓN");
        Background_login.add(SesionLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, -1, 60));

        UserLabelLogin.setFont(new java.awt.Font("Roboto ExtraBold", 1, 14)); // NOI18N
        UserLabelLogin.setText("USUARIO");
        Background_login.add(UserLabelLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 170, 80, 20));

        Separador2Login.setBackground(new java.awt.Color(51, 51, 51));
        Separador2Login.setForeground(new java.awt.Color(0, 0, 0));
        Separador2Login.setToolTipText("__________________________________");
        Background_login.add(Separador2Login, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 320, 430, 10));

        ContrasenaLabelLogin.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        ContrasenaLabelLogin.setText("CONTRASEÑA");
        Background_login.add(ContrasenaLabelLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 260, -1, -1));

        CampoUserLogin.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        CampoUserLogin.setForeground(new java.awt.Color(204, 204, 204));
        CampoUserLogin.setText("Ingrese su correo electronico");
        CampoUserLogin.setBorder(null);
        CampoUserLogin.addActionListener(this::CampoUserLoginActionPerformed);
        Background_login.add(CampoUserLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 190, 430, 40));

        Separador1Login.setBackground(new java.awt.Color(51, 51, 51));
        Separador1Login.setForeground(new java.awt.Color(0, 0, 0));
        Separador1Login.setToolTipText("__________________________________");
        Background_login.add(Separador1Login, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 230, 430, 10));

        CampoPasswLogin.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        CampoPasswLogin.setForeground(new java.awt.Color(204, 204, 204));
        CampoPasswLogin.setText("jPasswordField1");
        CampoPasswLogin.setBorder(null);
        CampoPasswLogin.addActionListener(this::CampoPasswLoginActionPerformed);
        Background_login.add(CampoPasswLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 280, 430, 40));

        RegistrarseLabelLogin.setFont(new java.awt.Font("Liberation Sans", 0, 15)); // NOI18N
        RegistrarseLabelLogin.setForeground(new java.awt.Color(0, 102, 102));
        RegistrarseLabelLogin.setText("Registrarse");
        Background_login.add(RegistrarseLabelLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 460, -1, -1));

        Label1Registrarse.setFont(new java.awt.Font("Liberation Sans", 0, 15)); // NOI18N
        Label1Registrarse.setForeground(new java.awt.Color(102, 102, 102));
        Label1Registrarse.setText("¿No tienes cuenta?");
        Background_login.add(Label1Registrarse, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 440, -1, -1));

        BotonSalirLogin.setFont(new java.awt.Font("AvantGarde LT Medium", 0, 18)); // NOI18N
        BotonSalirLogin.setForeground(new java.awt.Color(0, 0, 0));
        BotonSalirLogin.setText("X");
        BotonSalirLogin.setToolTipText("");
        BotonSalirLogin.setAlignmentX(0.7F);
        BotonSalirLogin.setAlignmentY(0.0F);
        BotonSalirLogin.setBorder(null);
        BotonSalirLogin.setBorderPainted(false);
        BotonSalirLogin.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BotonSalirLogin.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        BotonSalirLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                BotonSalirLoginMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                BotonSalirLoginMouseExited(evt);
            }
        });
        Background_login.add(BotonSalirLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 30, 30));

        BotonIngresarLogin.setBackground(new java.awt.Color(0, 0, 0));
        BotonIngresarLogin.setFont(new java.awt.Font("Roboto SemiBold", 1, 14)); // NOI18N
        BotonIngresarLogin.setForeground(new java.awt.Color(255, 255, 255));
        BotonIngresarLogin.setText("Ingresar");
        BotonIngresarLogin.setBorderPainted(false);
        BotonIngresarLogin.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BotonIngresarLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                BotonIngresarLoginMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                BotonIngresarLoginMouseExited(evt);
            }
        });
        Background_login.add(BotonIngresarLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 360, 160, 50));

        Background_register.setBackground(new java.awt.Color(255, 255, 255));
        Background_register.setForeground(new java.awt.Color(153, 153, 153));
        Background_register.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        LogoRegister.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LogoRegister.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Captura.PNG"))); // NOI18N
        Background_register.add(LogoRegister, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 30, 210, 160));

        BackgroundLogoRegister.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Gemini_Generated_Image_hl1zjdhl1zjdhl1z.png"))); // NOI18N
        BackgroundLogoRegister.setText("jLabel4");
        Background_register.add(BackgroundLogoRegister, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 0, 290, 500));

        LogoSuperiorRegister.setFont(new java.awt.Font("Roboto", 1, 24)); // NOI18N
        LogoSuperiorRegister.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Captura PRO EA.PNG"))); // NOI18N
        LogoSuperiorRegister.setToolTipText("");
        Background_register.add(LogoSuperiorRegister, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 170, 40));

        RegisterLabel.setFont(new java.awt.Font("Roboto Black", 0, 24)); // NOI18N
        RegisterLabel.setText("REGISTRAR USUARIO");
        Background_register.add(RegisterLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, -1, 60));

        UserLabelRegister.setFont(new java.awt.Font("Roboto ExtraBold", 1, 14)); // NOI18N
        UserLabelRegister.setText("USUARIO");
        Background_register.add(UserLabelRegister, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 170, 80, 20));

        Separador2Register.setBackground(new java.awt.Color(51, 51, 51));
        Separador2Register.setForeground(new java.awt.Color(0, 0, 0));
        Separador2Register.setToolTipText("__________________________________");
        Background_register.add(Separador2Register, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 320, 430, 10));

        ContrasenaLabelRegister.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        ContrasenaLabelRegister.setText("CONTRASEÑA");
        Background_register.add(ContrasenaLabelRegister, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 260, -1, -1));

        CampoUserRegister.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        CampoUserRegister.setForeground(new java.awt.Color(204, 204, 204));
        CampoUserRegister.setText("Ingrese su correo electronico");
        CampoUserRegister.setBorder(null);
        CampoUserRegister.addActionListener(this::CampoUserRegisterActionPerformed);
        Background_register.add(CampoUserRegister, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 190, 430, 40));

        Separador1Register.setBackground(new java.awt.Color(51, 51, 51));
        Separador1Register.setForeground(new java.awt.Color(0, 0, 0));
        Separador1Register.setToolTipText("__________________________________");
        Background_register.add(Separador1Register, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 230, 430, 10));

        CampoPasswRegister.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        CampoPasswRegister.setForeground(new java.awt.Color(204, 204, 204));
        CampoPasswRegister.setText("jPasswordField1");
        CampoPasswRegister.setBorder(null);
        CampoPasswRegister.addActionListener(this::CampoPasswRegisterActionPerformed);
        Background_register.add(CampoPasswRegister, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 280, 430, 40));

        IniciarSesionLabelRegister.setFont(new java.awt.Font("Liberation Sans", 0, 15)); // NOI18N
        IniciarSesionLabelRegister.setForeground(new java.awt.Color(0, 102, 102));
        IniciarSesionLabelRegister.setText("Ingresar");
        Background_register.add(IniciarSesionLabelRegister, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 460, -1, -1));

        BotonRegistrarRegister.setBackground(new java.awt.Color(0, 0, 0));
        BotonRegistrarRegister.setFont(new java.awt.Font("Roboto SemiBold", 1, 14)); // NOI18N
        BotonRegistrarRegister.setForeground(new java.awt.Color(255, 255, 255));
        BotonRegistrarRegister.setText("Registrarse");
        BotonRegistrarRegister.setBorderPainted(false);
        BotonRegistrarRegister.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BotonRegistrarRegister.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                BotonRegistrarRegisterMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                BotonRegistrarRegisterMouseExited(evt);
            }
        });
        Background_register.add(BotonRegistrarRegister, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 360, 160, 50));

        Label1Logearse.setFont(new java.awt.Font("Liberation Sans", 0, 15)); // NOI18N
        Label1Logearse.setForeground(new java.awt.Color(102, 102, 102));
        Label1Logearse.setText("¿Ya tienes cuenta?");
        Background_register.add(Label1Logearse, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 440, -1, -1));

        BotonSalirRegister.setFont(new java.awt.Font("AvantGarde LT Medium", 0, 18)); // NOI18N
        BotonSalirRegister.setForeground(new java.awt.Color(0, 0, 0));
        BotonSalirRegister.setText("X");
        BotonSalirRegister.setToolTipText("");
        BotonSalirRegister.setAlignmentX(0.7F);
        BotonSalirRegister.setAlignmentY(0.0F);
        BotonSalirRegister.setBorder(null);
        BotonSalirRegister.setBorderPainted(false);
        BotonSalirRegister.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BotonSalirRegister.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        BotonSalirRegister.setIconTextGap(0);
        BotonSalirRegister.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                BotonSalirRegisterMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                BotonSalirRegisterMouseExited(evt);
            }
        });
        Background_register.add(BotonSalirRegister, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 30, 30));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 822, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(Background_login, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(Background_register, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 512, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(Background_login, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(Background_register, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void CampoPasswLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CampoPasswLoginActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CampoPasswLoginActionPerformed

    private void CampoUserLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CampoUserLoginActionPerformed

        // TODO add your handling code here:
    }//GEN-LAST:event_CampoUserLoginActionPerformed

    private void CampoUserRegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CampoUserRegisterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CampoUserRegisterActionPerformed

    private void CampoPasswRegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CampoPasswRegisterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CampoPasswRegisterActionPerformed

    private void BotonSalirLoginMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BotonSalirLoginMouseEntered
        BotonSalirLogin.setBackground(new java.awt.Color(255, 0, 0));
    }//GEN-LAST:event_BotonSalirLoginMouseEntered

    private void BotonSalirLoginMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BotonSalirLoginMouseExited
        BotonSalirLogin.setBackground(new java.awt.Color(255, 255, 255));
    }//GEN-LAST:event_BotonSalirLoginMouseExited

    private void BotonSalirRegisterMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BotonSalirRegisterMouseEntered
        BotonSalirRegister.setBackground(new java.awt.Color(255, 0, 0));
    }//GEN-LAST:event_BotonSalirRegisterMouseEntered

    private void BotonSalirRegisterMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BotonSalirRegisterMouseExited
        BotonSalirRegister.setBackground(new java.awt.Color(255, 255, 255));
    }//GEN-LAST:event_BotonSalirRegisterMouseExited

    private void BotonIngresarLoginMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BotonIngresarLoginMouseEntered
        BotonIngresarLogin.setBackground(new java.awt.Color(102, 102, 102));
    }//GEN-LAST:event_BotonIngresarLoginMouseEntered

    private void BotonIngresarLoginMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BotonIngresarLoginMouseExited
        BotonIngresarLogin.setBackground(new java.awt.Color(0, 0, 0));
    }//GEN-LAST:event_BotonIngresarLoginMouseExited

    private void BotonRegistrarRegisterMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BotonRegistrarRegisterMouseEntered
        BotonRegistrarRegister.setBackground(new java.awt.Color(102, 102, 102));
    }//GEN-LAST:event_BotonRegistrarRegisterMouseEntered

    private void BotonRegistrarRegisterMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BotonRegistrarRegisterMouseExited
        BotonRegistrarRegister.setBackground(new java.awt.Color(0, 0, 0));
    }//GEN-LAST:event_BotonRegistrarRegisterMouseExited


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel BackgroundLogoLogin;
    private javax.swing.JLabel BackgroundLogoRegister;
    private javax.swing.JPanel Background_login;
    private javax.swing.JPanel Background_register;
    private javax.swing.JButton BotonIngresarLogin;
    private javax.swing.JButton BotonRegistrarRegister;
    private javax.swing.JButton BotonSalirLogin;
    private javax.swing.JButton BotonSalirRegister;
    private javax.swing.JPasswordField CampoPasswLogin;
    private javax.swing.JPasswordField CampoPasswRegister;
    private javax.swing.JTextField CampoUserLogin;
    private javax.swing.JTextField CampoUserRegister;
    private javax.swing.JLabel ContrasenaLabelLogin;
    private javax.swing.JLabel ContrasenaLabelRegister;
    private javax.swing.JLabel IniciarSesionLabelRegister;
    private javax.swing.JLabel Label1Logearse;
    private javax.swing.JLabel Label1Registrarse;
    private javax.swing.JLabel LogoLogin;
    private javax.swing.JLabel LogoRegister;
    private javax.swing.JLabel LogoSuperiorLogin;
    private javax.swing.JLabel LogoSuperiorRegister;
    private javax.swing.JLabel RegisterLabel;
    private javax.swing.JLabel RegistrarseLabelLogin;
    private javax.swing.JSeparator Separador1Login;
    private javax.swing.JSeparator Separador1Register;
    private javax.swing.JSeparator Separador2Login;
    private javax.swing.JSeparator Separador2Register;
    private javax.swing.JLabel SesionLabel;
    private javax.swing.JLabel UserLabelLogin;
    private javax.swing.JLabel UserLabelRegister;
    // End of variables declaration//GEN-END:variables
}

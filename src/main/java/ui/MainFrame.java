package ui;

import controller.InventarioController;
import model.Usuario;
import ui.components.SidebarButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class MainFrame extends JFrame {

    private InventarioController inventarioController;
    private Usuario usuarioActual;
    private JPanel panelContenido;
    private CardLayout cardLayout;
    private PanelDashboard panelDashboard;
    private PanelProductos panelProductos;
    private PanelMovimientos panelMovimientos;
    private PanelComportamiento panelComportamiento;
    private PanelAlertas panelAlertas;
    private PanelTasaCambio panelTasaCambio;
    private PanelProyeccion panelProyeccion;

    private ArrayList<SidebarButton> botonesMenu = new ArrayList<>();

    private int mouseX, mouseY;
    private boolean maximizado = false;
    private Rectangle tamañoAnterior;

    public MainFrame(Usuario usuario) {
        this.usuarioActual = usuario;
        this.inventarioController = new InventarioController(usuario);
        initComponents();
    }

    private void initComponents() {
        setTitle("ProEA - Inventario");
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(new Color(245, 245, 245));
        wrapper.add(crearTitlebar(), BorderLayout.NORTH);

        JPanel cuerpo = new JPanel(new BorderLayout());
        cuerpo.add(crearSidebar(), BorderLayout.WEST);
        cuerpo.add(crearPanelContenido(), BorderLayout.CENTER);
        wrapper.add(cuerpo, BorderLayout.CENTER);

        add(wrapper);
    }

    // ==================== TITLEBAR ====================
    private JPanel crearTitlebar() {
        JPanel titlebar = new JPanel(new BorderLayout());
        titlebar.setBackground(new Color(10, 10, 10));
        titlebar.setPreferredSize(new Dimension(0, 36));
        titlebar.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 10));

        JLabel titulo = new JLabel("ProEA — Sistema de Inventario");
        titulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        titulo.setForeground(new Color(150, 150, 150));
        titlebar.add(titulo, BorderLayout.WEST);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 6));
        botones.setBackground(new Color(10, 10, 10));

        JButton btnMinimizar = crearBotonTitlebar("—", new Color(10, 10, 10), new Color(50, 50, 50));
        JButton btnMaximizar = crearBotonTitlebar("□", new Color(10, 10, 10), new Color(50, 50, 50));
        JButton btnCerrar = crearBotonTitlebar("✕", new Color(10, 10, 10), new Color(180, 30, 30));

        btnMinimizar.addActionListener(evt -> setExtendedState(JFrame.ICONIFIED));
        btnMaximizar.addActionListener(evt -> toggleMaximizar());
        btnCerrar.addActionListener(evt -> System.exit(0));

        botones.add(btnMinimizar);
        botones.add(btnMaximizar);
        botones.add(btnCerrar);
        titlebar.add(botones, BorderLayout.EAST);

        titlebar.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
            }
        });
        titlebar.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (!maximizado) {
                    setLocation(getX() + e.getX() - mouseX, getY() + e.getY() - mouseY);
                }
            }
        });

        return titlebar;
    }

    private JButton crearBotonTitlebar(String texto, Color bgNormal, Color bgHover) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btn.setForeground(new Color(200, 200, 200));
        btn.setBackground(bgNormal);
        btn.setBorder(BorderFactory.createEmptyBorder(2, 10, 2, 10));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(bgHover);
            }

            public void mouseExited(MouseEvent e) {
                btn.setBackground(bgNormal);
            }
        });
        return btn;
    }

    private void toggleMaximizar() {
        if (maximizado) {
            setBounds(tamañoAnterior);
            maximizado = false;
        } else {
            tamañoAnterior = getBounds();
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            setBounds(ge.getMaximumWindowBounds());
            maximizado = true;
        }
    }

    // ==================== SIDEBAR ====================
    private JPanel crearSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(new Color(15, 15, 15));
        sidebar.setPreferredSize(new Dimension(210, 0));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(15, 15, 15));
        header.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel logo = new JLabel("ProEA");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        logo.setForeground(Color.WHITE);

        JLabel sublogo = new JLabel("Sistema de inventario");
        sublogo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        sublogo.setForeground(new Color(100, 100, 100));

        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(new Color(15, 15, 15));
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
        logoPanel.add(logo);
        logoPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        logoPanel.add(sublogo);

        header.add(logoPanel, BorderLayout.CENTER);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(30, 30, 30)),
                BorderFactory.createEmptyBorder(16, 20, 16, 20)
        ));

        // Secciones del menú
        JPanel menu = new JPanel();
        menu.setBackground(new Color(15, 15, 15));
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));

        menu.add(crearSeccionLabel("PRINCIPAL"));
        menu.add(crearBotonMenu("  Dashboard", "DASHBOARD"));
        menu.add(crearBotonMenu("  Productos", "PRODUCTOS"));
        menu.add(crearBotonMenu("  Movimientos", "MOVIMIENTOS"));
        menu.add(Box.createRigidArea(new Dimension(0, 8)));
        menu.add(crearSeccionLabel("ANÁLISIS"));
        menu.add(crearBotonMenu("  Comportamiento", "COMPORTAMIENTO"));
        menu.add(crearBotonMenu("  Tasa de cambio", "TASA_CAMBIO"));
        menu.add(crearBotonMenu("  Proyección", "PROYECCION"));
        menu.add(Box.createRigidArea(new Dimension(0, 8)));
        menu.add(crearSeccionLabel("SISTEMA"));
        menu.add(crearBotonMenu("  Alertas", "ALERTAS"));

        // Footer
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(new Color(15, 15, 15));
        footer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(30, 30, 30)),
                BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));

        // Avatar
        JPanel avatarRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        avatarRow.setBackground(new Color(15, 15, 15));

        JPanel avatar = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(59, 130, 246));
                g2.fillOval(0, 0, 34, 34);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
                String inicial = usuarioActual.getNombreUsuario().substring(0, 1).toUpperCase();
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(inicial, (34 - fm.stringWidth(inicial)) / 2, (34 + fm.getAscent()) / 2 - 2);
            }
        };
        avatar.setPreferredSize(new Dimension(34, 34));
        avatar.setBackground(new Color(15, 15, 15));

        JPanel userInfo = new JPanel();
        userInfo.setBackground(new Color(15, 15, 15));
        userInfo.setLayout(new BoxLayout(userInfo, BoxLayout.Y_AXIS));
        userInfo.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        JLabel nombreLabel = new JLabel(usuarioActual.getNombreUsuario());
        nombreLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        nombreLabel.setForeground(Color.WHITE);

        JLabel rolLabel = new JLabel("Admin");
        rolLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        rolLabel.setForeground(new Color(100, 100, 100));

        userInfo.add(nombreLabel);
        userInfo.add(rolLabel);

        avatarRow.add(avatar);
        avatarRow.add(userInfo);

        JButton btnSalir = new JButton("Cerrar sesión");
        btnSalir.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnSalir.setForeground(new Color(100, 100, 100));
        btnSalir.setBackground(new Color(15, 15, 15));
        btnSalir.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));
        btnSalir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSalir.setFocusPainted(false);
        btnSalir.setContentAreaFilled(false);
        btnSalir.setHorizontalAlignment(SwingConstants.LEFT);
        btnSalir.addActionListener(evt -> System.exit(0));

        footer.add(avatarRow, BorderLayout.CENTER);
        footer.add(btnSalir, BorderLayout.SOUTH);

        sidebar.add(header, BorderLayout.NORTH);
        sidebar.add(menu, BorderLayout.CENTER);
        sidebar.add(footer, BorderLayout.SOUTH);

        return sidebar;
    }

    private JLabel crearSeccionLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.BOLD, 10));
        label.setForeground(new Color(60, 60, 60));
        label.setBorder(BorderFactory.createEmptyBorder(4, 20, 4, 0));
        label.setMaximumSize(new Dimension(210, 20));
        return label;
    }

    private SidebarButton crearBotonMenu(String texto, String panel) {
        SidebarButton btn = new SidebarButton(texto);

        btn.addActionListener(evt -> {
            // Desactiva todos
            for (SidebarButton b : botonesMenu) {
                b.setActivo(false);
            }
            // Activa el seleccionado
            btn.setActivo(true);
            cardLayout.show(panelContenido, panel);
            if (panel.equals("DASHBOARD")) {
                panelDashboard.refresh();
            }
        });

        botonesMenu.add(btn);
        return btn;
    }

    // ==================== PANEL CONTENIDO ====================
    private JPanel crearPanelContenido() {
        cardLayout = new CardLayout();
        panelContenido = new JPanel(cardLayout);
        panelContenido.setBackground(new Color(245, 245, 245));

        panelDashboard = new PanelDashboard(inventarioController);
        panelProductos = new PanelProductos(inventarioController, this::refrescarDashboard);
        panelMovimientos = new PanelMovimientos(inventarioController, this::refrescarDashboard);
        panelComportamiento = new PanelComportamiento(inventarioController);
        panelAlertas = new PanelAlertas(inventarioController);

        panelTasaCambio = new PanelTasaCambio(inventarioController);
        panelProyeccion = new PanelProyeccion(inventarioController);

        panelContenido.add(panelTasaCambio, "TASA_CAMBIO");
        panelContenido.add(panelProyeccion, "PROYECCION");

        panelContenido.add(panelDashboard, "DASHBOARD");
        panelContenido.add(panelProductos, "PRODUCTOS");
        panelContenido.add(panelMovimientos, "MOVIMIENTOS");
        panelContenido.add(panelComportamiento, "COMPORTAMIENTO");

        panelContenido.add(panelAlertas, "ALERTAS");

        // Dashboard activo por defecto
        if (!botonesMenu.isEmpty()) {
            botonesMenu.get(0).setActivo(true);
        }

        cardLayout.show(panelContenido, "DASHBOARD");
        return panelContenido;
    }

    // ==================== REFRESCO GLOBAL ====================
    public void refrescarDashboard() {
        panelDashboard.refresh();
        if (panelMovimientos != null) {
            panelMovimientos.refrescarProductos();
        }
        if (panelComportamiento != null) {
            panelComportamiento.refrescarProductos();
        }
        if (panelAlertas != null) {
            panelAlertas.actualizarAlertas();
        }

        if (panelTasaCambio != null) {
            panelTasaCambio.refrescarProductos();
        }
        if (panelProyeccion != null) {
            panelProyeccion.refrescarProductos();
        }
    }
}

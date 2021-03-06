/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lombardia2014.Interface.menu;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author Domek
 */
public class Help extends MenuElementsList {

    private JTree tree = null;
    private JPanel information = null;

    @Override
    public void generateGui() {
        formFrame.setSize(new Dimension(500, 600));
        formFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        formFrame.setResizable(false);
        formFrame.setTitle("Instrukcja obsługi");

        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(titleBorder);

        generatePanels(c);
        formFrame.add(mainPanel);
        formFrame.setVisible(true);
    }

    @Override
    public void generatePanels(GridBagConstraints c) {
        c.insets = new Insets(0, 0, 0, 0);
        c.fill = GridBagConstraints.HORIZONTAL;
        tree = new JTree(generateMenu());
        tree.setPreferredSize(new Dimension(180, 560));
        c.gridx = 0;
        c.gridy = 0;
        mainPanel.add(tree, c);
        
        // create jpanel for text 
        information = new JPanel();
        information.setPreferredSize(new Dimension(300, 560));
        c.gridx = 1;
        c.gridy = 0;
        mainPanel.add(information,c);
        
    }

    private DefaultMutableTreeNode generateMenu() {
        DefaultMutableTreeNode top
                = new DefaultMutableTreeNode("Lombardia");
        DefaultMutableTreeNode book, page, page2;

        //elements menu
        book = new DefaultMutableTreeNode("Pasek Menu");
        top.add(book);
        page = new DefaultMutableTreeNode("Plik");
        book.add(page);
        page = new DefaultMutableTreeNode("Konfiguracja");
        book.add(page);
        page = new DefaultMutableTreeNode("Rozliczenia");
        book.add(page);
        page = new DefaultMutableTreeNode("Pomoc");
        book.add(page);

        book = new DefaultMutableTreeNode("Menu główne");
        top.add(book);
        page = new DefaultMutableTreeNode("Kasa");
        book.add(page);
        page2 = new DefaultMutableTreeNode("Wpłata");
        page.add(page2);
        page2 = new DefaultMutableTreeNode("Wypłata");
        page.add(page2);
        page = new DefaultMutableTreeNode("Pożyczka");
        book.add(page);
        page2 = new DefaultMutableTreeNode("Nowa");
        page.add(page2);
        page2 = new DefaultMutableTreeNode("Zwrot");
        page.add(page2);
        page2 = new DefaultMutableTreeNode("Skup");
        page.add(page2);
        page2 = new DefaultMutableTreeNode("Ostatnia");
        page.add(page2);
        page2 = new DefaultMutableTreeNode("Przedłużenie");
        page.add(page2);
        page = new DefaultMutableTreeNode("Inne");
        book.add(page);
        page2 = new DefaultMutableTreeNode("Spóźnieni");
        page.add(page2);
        page2 = new DefaultMutableTreeNode("Zgłoszenia tel.");
        page.add(page2);
        page2 = new DefaultMutableTreeNode("Uwagi");
        page.add(page2);

        book = new DefaultMutableTreeNode("Lista klientów");
        top.add(book);
        page = new DefaultMutableTreeNode("Wyszukiwarka");
        book.add(page);
        page = new DefaultMutableTreeNode("Lista");
        book.add(page);

        book = new DefaultMutableTreeNode("Lista depozytów");
        top.add(book);
        page = new DefaultMutableTreeNode("Wyszukiwarka");
        book.add(page);
        page = new DefaultMutableTreeNode("Lista");
        book.add(page);

        return top;
    }

}

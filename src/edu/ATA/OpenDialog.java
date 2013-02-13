package edu.ATA;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;

public abstract class OpenDialog extends JFrame {

    String approveButton;
    int selectionMode;
    FileFilter fileFilter;

    public OpenDialog(String title, String approveButton, int selectionMode, FileFilter filter) {
        super(title);
        this.approveButton = approveButton;
        this.selectionMode = selectionMode;
        this.fileFilter = filter;


        final JFileChooser dialog = new JFileChooser();

        dialog.setApproveButtonMnemonic(KeyEvent.VK_ENTER);
        dialog.setFileFilter(fileFilter);
        dialog.setAcceptAllFileFilterUsed(false);
        dialog.setFileSelectionMode(selectionMode);
        dialog.setApproveButtonText(approveButton);

        dialog.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals(JFileChooser.APPROVE_SELECTION)) {
                    setFile(dialog.getFileSelectionMode() == JFileChooser.DIRECTORIES_ONLY
                            ? dialog.getSelectedFile().getPath() + File.separator
                            : dialog.getSelectedFile().getPath());
                }
                dispose();
            }
        });

        add(dialog);
    }

    public abstract void setFile(String path);
}
package edu.ATA;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;

public abstract class OpenDialog extends JFrame {

    public OpenDialog(String directory, int selectionMode, FileFilter filter) {
        super("Open");
        setAlwaysOnTop(true);
        final JFileChooser dialog = new JFileChooser();
        dialog.setCurrentDirectory(new File(directory));
        dialog.setApproveButtonMnemonic(KeyEvent.VK_ENTER);
        dialog.setFileFilter(filter);
        dialog.setAcceptAllFileFilterUsed(false);
        dialog.setFileSelectionMode(selectionMode);
        dialog.setApproveButtonText("Open");

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
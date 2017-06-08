import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Created by vivek on 08/06/2017.
 */
public class Main {

    private static JFrame mainwindow;
    private static JMenuItem importTWXMenu;
    private  static JTable table;



    static void initMenu(final JFrame frame){
        importTWXMenu =new JMenuItem("Import TWX");

        importTWXMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final JFileChooser chooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "TWX files", "twx", "zip");
                chooser.setFileFilter(filter);
                final int returnVal = chooser.showOpenDialog(frame);
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    System.out.println("You chose to open this file: " +chooser.getSelectedFile().getName());
                    new SwingWorker<String,Object>(){

                        @Override
                        protected String doInBackground() throws Exception {
                            List<EnvVar> result = EnvVarFromTWXFile.process(chooser.getSelectedFile());
                            if(result!=null){
                                System.out.println("result " +result.size());
                                for(int row=0;row<result.size();row++){
                                    System.out.println("row "+row);
                                    String[] values = result.get(row).toArray();
                                    DefaultTableModel dt =(DefaultTableModel)table.getModel();
                                    dt.addRow(values);
                                }
                            }
                            System.out.println("finish");
                            return null;
                        }
                    }.execute();
                }

            }
        });

        JMenu  menu = new JMenu("File");
        menu.add(importTWXMenu);
        JMenuBar menubar = new JMenuBar();
        menubar.add(menu);

        frame.setJMenuBar(menubar);
    }
    private  static  void initComponent(JFrame frame){
        String data[][]={ {"none","none","none","none","none","none","none","none","none"}
                };
        String column[]={"Project","Snapshot","Track","Name","Default","Development","Testing","Staging","Production"};
        DefaultTableModel dt =new DefaultTableModel();
        for(int i=0;i<column.length;i++){
            dt.addColumn(column[i]);
        }

        table=new JTable(dt);

        table.setBounds(30,40,200,300);
        JScrollPane sp=new JScrollPane(table);
        frame.add(sp);
    }
    public static void main(String args[]) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(
                UIManager.getCrossPlatformLookAndFeelClassName());
        mainwindow= new JFrame();
        mainwindow.setTitle("IBM BPM Utilities");
        mainwindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initMenu(mainwindow);
        initComponent(mainwindow);
        //DefaultTableModel dt = (DefaultTableModel) table.getModel();
        mainwindow.setSize(500,500);
        mainwindow.pack();
        mainwindow.setVisible(true);
    }
}

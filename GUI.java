/*******************************************************************************
 * Copyright 2020 Michael S. Yao
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class GUI {
    static int width = 600;
    static int height = 800;
	
    static String htmlHead = "<html>";
    static String htmlTail = "</html>";

    public static void main(String[] args) {    
        JFrame frame = new JFrame("Plasmid Homology Analyzer"); 

        JButton b = new JButton("Analyze");    
        b.setBounds(230, 155, 140, 30);    

        JLabel label = new JLabel("Enter DNA Sequence: ");		
        label.setBounds(100, 50, 200, 30);

        JLabel label2 = new JLabel("Minimum Homology Length (bp): ");
        label2.setBounds(100, 85, 250, 30);

        JLabel label3 = new JLabel("Minimum Percent Match (0-100): ");
        label3.setBounds(100, 120, 250, 30);

        JLabel copyright = new JLabel("Copyright 2020 by Michael Yao");
        copyright.setBounds(200, height - 60, 250, 30);

        JLabel out = new JLabel();
        out.setBounds(100, 165, 450, 400);

        JTextField sequence = new JTextField();
        sequence.setBounds(235, 50, 250, 30);

        JTextField homology = new JTextField();
        homology.setBounds(310, 85, 175, 30);

        JTextField match = new JTextField();
        match.setBounds(310, 120, 175, 30);

        frame.add(out);
        frame.add(copyright);
        frame.add(sequence);
        frame.add(homology);
        frame.add(match);
        frame.add(label);
        frame.add(label2);
        frame.add(label3);
        frame.add(b); 
        frame.setSize(width, height);    
        frame.setLayout(null);    
        frame.setVisible(true); 
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   
		
		b.addActionListener(new ActionListener() {
	        
		    @Override
            public void actionPerformed(ActionEvent arg0) {
                String dna = sequence.getText();
                int homologyValue = Integer.parseInt(homology.getText());
                int matchPercent = Integer.parseInt(match.getText());
                if (matchPercent > 100 || matchPercent < 0) {
                    out.setText("Invalid percent match entered.");
                    return;
                }

                Program program = new Program(dna, homologyValue, 
                        (double)matchPercent / 100);

                out.setText(htmlHead + program.testHomology() + htmlTail);
            }          
        });
    }         
}   

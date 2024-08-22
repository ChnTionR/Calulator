import javax.swing.JButton;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
/*
TO DO!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
-- implement all the button actions

-- show history of calculations or just show one

-- add other important stuff idk
 */

public class Main {

    //create a dimension to use among all the buttonData
    Dimension buttonSize = new Dimension(100, 100);

    //create frame
    JFrame frame = new JFrame("Calculator");

    //create the panel that will house the buttonData
    JPanel panel = new JPanel(new GridBagLayout());

    //create the grid to place buttonData on
    GridBagConstraints gbc = new GridBagConstraints();

    //create a label to show numbers
    JLabel screen = new JLabel("");
    JLabel smallScreen = new JLabel("");

    //create an arrayList to store misc. buttonData' data
    List<Object[]> miscButtons;

    //create an array to store {name, pos} of each misc button
    Object[] buttonData;

    int firstNum;
    int secondNum;
    String operation;
    boolean calcDone = false;

    public void uiCreation(){

        //frame attributes
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);

        //grid constraint attributes
        gbc.gridx = 0;
        gbc.gridy = 0;

        //screen attributes
        screen.setFont(new Font("Dialogue", Font.PLAIN, 30));
        screen.setPreferredSize(new Dimension(300, 70));

        //small screen attributes
        smallScreen.setFont((new Font("Dialogue", Font.PLAIN, 15)));

        ////create numbered buttonData
        JButton button;
        for (int i = 0; i < 10; i++) {

            final int buttonValue = i;

            //create button
            button = new JButton(String.valueOf(buttonValue));
            button.setPreferredSize(buttonSize);

            //make exceptional case for 0
            if(i == 0){
                gbc.gridx = 1;
                gbc.gridy = 3;
            }else{
                gbc.gridx = (i-1)%3;
                gbc.gridy = (i-1)/3;
            }

            //detect when button gets clicked
            button.addActionListener(_ -> screen.setText(screen.getText()+buttonValue));
            panel.add(button, gbc);
        }


        //set an array list to house all the operational/misc buttonData
        miscButtons = new ArrayList<>();

        ///button to remove one digit
        JButton removeOne = new JButton("<=");
        createMisc(removeOne, 0, 3);

        //|->action handler
        removeOne.addActionListener(_ -> screen.setText(removeLast(screen.getText())));

        //button to clear screen
        JButton clear = new JButton("C");
        createMisc(clear, 2, 3);
        clear.addActionListener(_-> {
            screen.setText("");
            smallScreen.setText("");
            firstNum = 0;
            secondNum = 0;
        });

        //addition button
        JButton add = new JButton("+");
        createMisc(add, 3, 0);
        add.addActionListener(_-> start("+"));

        //subtraction button
        JButton subtract = new JButton("-");
        createMisc(subtract, 3, 1);
        subtract.addActionListener(_-> start("-"));

        //multiplication button
        JButton multiply = new JButton("X");
        createMisc(multiply, 3, 2);
        multiply.addActionListener(_-> start("*"));

        //division button
        JButton divide = new JButton("÷");
        createMisc(divide, 3, 3);
        divide.addActionListener(_-> start("/"));

        //equals button
        JButton equals = new JButton("=");
        createMisc(equals, 4, 0);
        equals.addActionListener(_ -> {
            // detect if the screen is empty. If empty, do nothing
            if(!screen.getText().isEmpty() && !calcDone && operation != null && !screen.getText().contains("¯") && !screen.getText().equals("-")) {
                String newNum = screen.getText();
                screen.setText(calculate(firstNum, Integer.parseInt(newNum), operation));
                //re-set the smallScreen
                smallScreen.setText(firstNum + " " + operation + " " + secondNum + " =");
                calcDone = true;

            }
        });

        //add misc buttonData to the panel
        for(Object[] list : miscButtons){

            //declare which data is what
            JButton name = (JButton) list[0];
            GridBagConstraints pos = (GridBagConstraints) list[1];

            //set size
            name.setPreferredSize(buttonSize);

            //add button to the panel
            panel.add(name, pos);
        }

        //place features
        frame.add(screen, BorderLayout.CENTER);
        frame.add(smallScreen, BorderLayout.NORTH);
        frame.add(panel, BorderLayout.SOUTH);

        //set frame attributes
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    //blueprint to add more misc/operation buttonData(my most proud idea)
    public void createMisc(JButton name, int posX, int posY){

        //set pos in grid
        gbc.gridy = posY;
        gbc.gridx = posX;

        //set which data goes where in the array
        buttonData =  new Object[2];
        buttonData[0] = name;
        buttonData[1] = gbc.clone();

        //add button
        miscButtons.add(buttonData);
    }

    public String removeLast(String txt){
        //get the second-last index
        int endIndex = txt.length() - 1;

        //detect if length is less or equal to zero
        if (endIndex <= 0){
            return "";
        }else{
            return txt.substring(0, endIndex);
        }
    }

    public String calculate(int firstNum, int secondNum, String action){
        this.secondNum = secondNum;
        //look if it's null to prevent errors
        if(action != null){
            //look if dividing by zero
            if(secondNum == 0 && action.equals("/")){
                screen.setText("¯\\_(ツ)_/¯");
                calcDone = true;
            }else{
                //look which action is active
                return switch (action) {
                    case "+" -> String.valueOf(firstNum + secondNum);
                    case "-" -> String.valueOf(firstNum - secondNum);
                    case "*" -> String.valueOf(firstNum * secondNum);
                    case "/" -> String.valueOf(firstNum / secondNum);
                    //error state
                    default -> "ERR";
                };
            }
        }
        return screen.getText();
    };

    public void start(String operator){
        operation = operator;
        calcDone = false;
        //write operation past to the smallScreen
        if(screen.getText().contains("¯") && screen.getText().equals("-")) {
            if (smallScreen.getText().isEmpty()) {
                return;
            }
        }

        // get the first number
        if (!screen.getText().isEmpty() && !screen.getText().equals("-")) {
            firstNum = Integer.parseInt(screen.getText());
        }
        smallScreen.setText(String.valueOf(firstNum)+ " " + operation + " ");
        screen.setText("");
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.uiCreation();
    }
}

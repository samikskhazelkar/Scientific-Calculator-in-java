import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
 import java.awt.event.ActionListener;
import java.util.Arrays;

public class ScientificCalculator {
    private JFrame frame;
    private JTextField inputField;

    public ScientificCalculator() {
        frame = new JFrame("Scientific Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 500);
        frame.setLayout(new BorderLayout());

        inputField = new JTextField();
        inputField.setEditable(false);
        frame.add(inputField, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 4));

        String[] buttonLabels = {
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "0", ".", "=", "+",
                "sqrt", "log", "sin", "cos"
        };

        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.addActionListener(new ButtonClickListener());
            buttonPanel.add(button);
        }

        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            String inputText = inputField.getText();

            switch (command) {
                case "=":
                    try {
                        double result = evaluateExpression(inputText);
                        inputField.setText(String.valueOf(result));
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame, "Invalid expression", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
                case "sqrt":
                    inputField.setText(String.valueOf(Math.sqrt(Double.parseDouble(inputText))));
                    break;
                case "log":
                    inputField.setText(String.valueOf(Math.log10(Double.parseDouble(inputText))));
                    break;
                case "sin":
                    inputField.setText(String.valueOf(Math.sin(Double.parseDouble(inputText))));
                    break;
                case "cos":
                    inputField.setText(String.valueOf(Math.cos(Double.parseDouble(inputText))));
                    break;
                default:
                    inputField.setText(inputText + command);
                    break;
            }
        }

        private double evaluateExpression(String expression) {
            expression = expression.replaceAll("sin", "s")
                    .replaceAll("cos", "c")
                    .replaceAll("sqrt", "r");

            String[] tokens = expression.split("(?<=[-+*/()s])|(?=[-+*/()s])");
            System.out.println(Arrays.toString(tokens));

            return evaluate(tokens);
        }

        private double evaluate(String[] tokens) {
            Stack<Double> numbers = new Stack<>();
            Stack<String> operators = new Stack<>();

            for (String token : tokens) {
                if (isNumber(token)) {
                    numbers.push(Double.parseDouble(token));
                } else if (isOperator(token)) {
                    while (!operators.isEmpty() && hasPrecedence(operators.peek(), token)) {
                        performOperation(numbers, operators);
                    }
                    operators.push(token);
                } else if (token.equals("(")) {
                    operators.push(token);
                } else if (token.equals(")")) {
                    while (!operators.peek().equals("(")) {
                        performOperation(numbers, operators);
                    }
                    operators.pop();
                }
            }

            while (!operators.isEmpty()) {
                performOperation(numbers, operators);
            }

            return numbers.pop();
        }

        private boolean isNumber(String token) {
            try {
                Double.parseDouble(token);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }

        private boolean isOperator(String token) {
            return token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/");
        }

        private boolean hasPrecedence(String operator1, String operator2) {
            return (operator1.equals("*") || operator1.equals("/")) && (operator2.equals("+") || operator2.equals("-"));
        }

        private void performOperation(Stack<Double> numbers, Stack<String> operators) {
            double operand2 = numbers.pop();
            double operand1 = numbers.pop();
            String operator = operators.pop();
            double result = 0;

            switch (operator) {
                case "+":
                    result = operand1 + operand2;
                    break;
                case "-":
                    result = operand1 - operand2;
                    break;
                case "*":
                    result = operand1 * operand2;
                    break;
                case "/":
                    result = operand1 / operand2;
                    break;
                case "s":
                    result = Math.sin(operand2);
                    break;
                case "c":
                    result = Math.cos(operand2);
                    break;
                case "r":
                    result = Math.sqrt(operand2);
                    break;
            }

            numbers.push(result);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ScientificCalculator();
            }
        });
    }
}

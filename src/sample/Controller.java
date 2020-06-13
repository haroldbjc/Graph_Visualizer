package sample;

import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;



import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;


public class Controller {
    public ArrayList<Point> CircleLocation = new ArrayList<>();
    public ArrayList<Arrow> edgeCollection = new ArrayList<>();
    public ArrayList<Point> edgeDirection = new ArrayList<>();
    public ArrayList<Boolean> functionCheck = new ArrayList<>();{
        for (int i = 0; i < 3; i++) {
            functionCheck.add(false);
        }
    }
    public ArrayList<String> vertexString = new ArrayList<>();{
        vertexString.add("AU");
        vertexString.add("EG");
        vertexString.add("BE");
        vertexString.add("DK");
        vertexString.add("HK");
    }
    public Angle angle = new Angle();
    public ChoiceBox choiceBox, left, right;
    public Button checkButton, previous, next;
    public CheckBox checkCyclic, checkConnect, checkShort;
    public Integer edgeCost = new Integer(0);
    public Integer currentMap = new Integer(5);

    public Boolean[][] statusCheck = new Boolean[20][3];{
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 20; j++) {
                statusCheck[j][i] = false;
            }
        }
    }

    public AnchorPane MenuPane;
    public static TextArea textfield1 = new TextArea();{
        textfield1.setPrefSize(350, 200);
        textfield1.setLayoutX(821);
        textfield1.setLayoutY(437);
        textfield1.setFont(Font.font("Gill Sans MT Condensed", 19));
        textfield1.setEditable(false);
        textfield1.setStyle("-fx-background-color: #404040; ");

    }
    public static Integer textInt = new Integer(1);
    public static Boolean errorTxt = new Boolean(false);

    public static ArrayList<Label> Cost = new ArrayList<>();


    initial database = new initial();
    Graph<String> tempGraph = new Graph<>(database.initialGraph);


    public void initialize(){
        choiceBox.setValue("1");
        left.setValue("2");
        right.setValue("3");
        for (int i = 0; i < vertexString.size(); i++) {
            left.getItems().add(vertexString.get(i));
            right.getItems().add(vertexString.get(i));
        }
        previous.setDisable(true);
        next.setDisable(true);
        choiceBox.getItems().add("Strong Connectivity");
        choiceBox.getItems().add("Cyclic");
        choiceBox.getItems().add("Shortest Path");
        CircleLocation.add(new Point(350 + 50, 150 + 50));
        CircleLocation.add(new Point(100 + 50, 270 + 50));
        CircleLocation.add(new Point(500 + 50, 500 + 50));
        CircleLocation.add(new Point(220 + 50, 500 + 50));
        CircleLocation.add(new Point(600 + 50, 270 + 50));
        MenuPane.getChildren().add(textfield1);
        GenerateDefaultLine();
        for (int i = 0; i < 5; i++) {
            MenuPane.getChildren().add(Cost.get(i));
        }

    }

    public void clearEdges(){
        for (int i = 0; i < edgeCollection.size(); i++) {
            MenuPane.getChildren().remove(edgeCollection.get(i));
        }
        for (int i = 0; i < Cost.size(); i++) {
            MenuPane.getChildren().remove(Cost.get(i));
        }
    }

    public void initializeEdges(){
        clearEdges();
        edgeCollection.removeAll(edgeCollection);
        edgeDirection.removeAll(edgeDirection);
        Cost.removeAll(Cost);
        edgeCost = 0;
        for (int i = 0; i < 3; i++) {
            functionCheck.set(i, false);
        }
        GenerateDefaultLine();
        currentMap = 5;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 20; j++) {
                statusCheck[j][i] = false;
            }
        }
    }


    public boolean shortestPathAlgo(){
        List<Graph.Edge<String>> shortest_path = tempGraph.aStar(tempGraph.getVertices().get(checkVertecwithString(left.getValue().toString())),
                tempGraph.getVertices().get(checkVertecwithString(right.getValue().toString())));
        while (shortest_path == null && !tempGraph.isFullEdges()) {
            generateRandomEgde(tempGraph, database);
            shortest_path = tempGraph.aStar(tempGraph.getVertices().get(checkVertecwithString(left.getValue().toString())),
                    tempGraph.getVertices().get(checkVertecwithString(right.getValue().toString())));
        }
        if(shortest_path != null) {
            int total_cost = 0;
            for (Graph.Edge<String> e : shortest_path) {
                total_cost += e.getCost();
            }
            if (textfield1.getText().isEmpty()) textfield1.setText("Shortest Path found! " + "\n" + shortest_path
            + "\n" + "Total Cost : " + total_cost);
            else textfield1.setText(textfield1.getText() + "\n" + "Shortest Path found ! " + "\n" + shortest_path
                    + "\n" + "Total Cost : " + total_cost);

            //=======================================
            System.out.println(shortest_path.size());
            for (int i = 0; i < shortest_path.size(); i++) {
                int a = checkVertecwithString(shortest_path.get(i).toString().substring(2, 4));
                int b = checkVertecwithString(shortest_path.get(i).toString().substring(15, 17));
                for (int j = 0; j < edgeDirection.size(); j++) {
                    if(edgeDirection.get(j).x == b && edgeDirection.get(j).y == a)
                        edgeCollection.get(j).setColor();
                }

            }



            //-------------------------------------

            for (int i = edgeCost; i < Cost.size(); i++) {
                MenuPane.getChildren().add(Cost.get(i));
                functionCheck.set(2, true);
                edgeCost++;
            }
        }
        return true;
    }

    public boolean returnShort(){
        List<Graph.Edge<String>> shortest_path;
        shortest_path = tempGraph.aStar(tempGraph.getVertices().get(checkVertecwithString(left.getValue().toString())),
                tempGraph.getVertices().get(checkVertecwithString(right.getValue().toString())));
        if(shortest_path != null)return true; else return false;
    }

    public int checkVertecwithString(String a){
        switch (a){
            case "AU" : return 0;
            case "EG" : return 1;
            case "BE" : return 2;
            case "DK" : return 3;
            case "HK" : return 4;
            default: return -1;
        }
    }

    public void CyclicAlgo(){

        while(!tempGraph.isCyclic() && !tempGraph.isFullEdges()){
            generateRandomEgde(tempGraph, database);
        }
        if(tempGraph.isCyclic()) {
            if(textfield1.getText().isEmpty())textfield1.setText("Cycle Exsists!");
            else textfield1.setText(textfield1.getText() + "\n" + "Cycle Exsists!");
            if(!functionCheck.get(1)) {
                for (int i = edgeCost; i < Cost.size(); i++) {
                    MenuPane.getChildren().add(Cost.get(i));
                    edgeCost++;
                }
                functionCheck.set(1, true);
            }
        }
    }

    public void ConnectAlgo(){

        while(!tempGraph.isStronglyConnected() && !tempGraph.isFullEdges()){
            generateRandomEgde(tempGraph, database);
        }

        if(tempGraph.isStronglyConnected()) {
            if(textfield1.getText().isEmpty())textfield1.setText("Graph is Strongly Connected!");
            else textfield1.setText(textfield1.getText() + "\n" + "Graph is Strongly Connected!");
            if(!(functionCheck.get(0))){
                for (int i = edgeCost; i < Cost.size(); i++) {
                    MenuPane.getChildren().add(Cost.get(i));
                    edgeCost++;
                }
                functionCheck.set(0, true);
            }
        }


    }

    public void GenerateDefaultLine() {
        float degree;
        int[] array = new int[]{0, 1, 0, 4, 3, 1, 3, 2, 4, 2};
        int[] costArray = new int[]{12, 6, 4, 1, 9};
        for (int i = 0; i < 10; i+=2) {
            Arrow edge = new Arrow();
            Label cost = new Label();

            edgeCollection.add(edge);
            edgeDirection.add(new Point(array[i+1], array[i]));
            degree = angle.getAngle(CircleLocation.get(array[i+1]), CircleLocation.get(array[i]));
            edge.setStartX(CircleLocation.get(array[i]).getX());
            edge.setStartY(CircleLocation.get(array[i]).getY());
            edge.setEndX(CircleLocation.get(array[i+1]).getX() + Math.cos(degree)*46);
            edge.setEndY(CircleLocation.get(array[i+1]).getY() + Math.sin(degree)*46);
            cost.setText(String.valueOf(costArray[i/2]));
            cost.translateXProperty().setValue(CircleLocation.get(array[i+1]).getX() + Math.cos(degree)*90);
            cost.translateYProperty().setValue(CircleLocation.get(array[i+1]).getY() + Math.sin(degree)*90 - 10);
            cost.setTextFill(Color.WHITE);
            cost.setStyle("-fx-background-color : #505050; -fx-background-radius: 200 200 200 200;");
            Cost.add(cost);

            edgeCost++;
            MenuPane.getChildren().add(0, edge);
        }
    }

    public void GenerateLine(int a, int b, int c) {

        float degree;
        Arrow edge = new Arrow();
        Label cost = new Label();
        edgeCollection.add(edge);
        edgeDirection.add(new Point(b, a));
        degree = angle.getAngle(CircleLocation.get(b), CircleLocation.get(a));
        edge.setStartX(CircleLocation.get(a).getX());
        edge.setStartY(CircleLocation.get(a).getY());
        edge.setEndX(CircleLocation.get(b).getX() + Math.cos(degree)*46);
        edge.setEndY(CircleLocation.get(b).getY() + Math.sin(degree)*46);
        cost.setText(String.valueOf(c));
        cost.translateXProperty().setValue(CircleLocation.get(b).getX() + Math.cos(degree)*100);
        cost.translateYProperty().setValue(CircleLocation.get(b).getY() + Math.sin(degree)*100);

        cost.setTextFill(Color.WHITE);
        cost.setStyle("-fx-background-color : #505050; -fx-background-radius: 200 200 200 200;");
        Cost.add(cost);

        currentMap = edgeCollection.size();



        if(tempGraph.isCyclic()) statusCheck[edgeCollection.size()-1][1] = true;
        if(tempGraph.isStronglyConnected()) statusCheck[edgeCollection.size()-1][0] = true;
        if(returnShort()) statusCheck[edgeCollection.size()-1][2] = true;
        MenuPane.getChildren().add(0, edge);
    }

    public void generateRandomEgde(Graph<String> graph, initial database){
        while(true) {
            int startRanNum = database.rand.nextInt(graph.getVertices().size());
            int endRanNum = database.rand.nextInt(graph.getVertices().size());
            int c = database.rand.nextInt(12) + 1;
            while(startRanNum==endRanNum)
            {
                startRanNum = database.rand.nextInt(graph.getVertices().size());
                endRanNum = database.rand.nextInt(graph.getVertices().size());
            }

            Graph.Vertex<String> start = graph.getVertices().get(startRanNum);
            Graph.Vertex<String> end = graph.getVertices().get(endRanNum);
            if (!graph.getVertices().contains(start) || !graph.getVertices().contains(end)) {
                continue;
            }
            Graph.Edge<String> newEdge = new Graph.Edge<String> (c, start, end);
            try{
                if (!graph.contain_edges(newEdge)) {
                    graph.addEdge(newEdge);
                    GenerateLine(startRanNum, endRanNum, c);
                    return;
                }
                else if(graph.isFullEdges())
                {
                    System.out.println("No More Graph Can Be Generated");
                    return;
                }else{
                    //System.out.println("Edge generated exist");
                    //System.out.println(startRanNum + "\t" + endRanNum);
                    //System.out.println(graph.getEdges().size());
                    //if(graph.getEdges().size()>=20) System.out.println(graph);
                }
            } catch (NullPointerException e) {
                System.out.println("Problem in generating new graph");
            }
        }
    }

    public void runFunction(){

        if(left.getValue().equals("2") || right.getValue().equals("3")){
            if(textfield1.getText().isEmpty())textfield1.setText("Choose the vertex first.");
            else textfield1.setText(textfield1.getText() + "\n" + "Choose the vertex first.");
            return;
        }
        if(left.getValue().equals(right.getValue())) {
            if(textfield1.getText().isEmpty())textfield1.setText("Choose different vertex.");
            else textfield1.setText(textfield1.getText() + "\n" + "Choose different vertex.");
            return;
        }
        if(choiceBox.getValue().equals("Strong Connectivity")) ConnectAlgo();
        else if(choiceBox.getValue().equals("Cyclic")) CyclicAlgo();
        else if (choiceBox.getValue().equals("Shortest Path")) shortestPathAlgo();
        else if(choiceBox.getValue().equals("1")){
            if(textfield1.getText().isEmpty())textfield1.setText("Choose a function.");
            else textfield1.setText(textfield1.getText() + "\n" + "Choose a function.");
            return;
        }
        if(tempGraph.isCyclic()) {
            functionCheck.set(1, true);
            checkCyclic.setSelected(true);
        }
        else checkCyclic.setSelected(false);

        if(tempGraph.isStronglyConnected()) {
            functionCheck.set(0, true);
            checkConnect.setSelected(true);
        }
        else checkConnect.setSelected(false);

        if(returnShort()) {
            functionCheck.set(2, true);
            checkShort.setSelected(true);
        }
        else checkShort.setSelected(false);
        previous.setDisable(false);
        next.setDisable(false);
    }

    public void resetButton(){
        initializeEdges();
        textfield1.setText("");
        for (int i = 0; i < 5; i++) {
            MenuPane.getChildren().add(Cost.get(i));
        }
        checkConnect.setSelected(false);
        checkCyclic.setSelected(false);
        checkShort.setSelected(false);
        tempGraph = new Graph<>(database.initialGraph);
        previous.setDisable(true);
        next.setDisable(true);
    }

    public void previousMap(){
        if(currentMap>5) {edgeCollection.get(currentMap-1).setVisible(false);
        Cost.get(currentMap-1).setVisible(false);
        currentMap--;}
        if(statusCheck[currentMap-1][2]) checkShort.setSelected(true); else checkShort.setSelected(false);
        if(statusCheck[currentMap-1][1]) checkCyclic.setSelected(true); else checkCyclic.setSelected(false);
        if(statusCheck[currentMap-1][0]) checkConnect.setSelected(true); else checkConnect.setSelected(false);
    }

    public void nextMap(){
        if(currentMap < edgeCollection.size()) {edgeCollection.get(currentMap).setVisible(true);
            Cost.get(currentMap).setVisible(true);
        currentMap++;}
        if(statusCheck[currentMap-1][2]) checkShort.setSelected(true); else checkShort.setSelected(false);
        if(statusCheck[currentMap-1][1]) checkCyclic.setSelected(true); else checkCyclic.setSelected(false);
        if(statusCheck[currentMap-1][0]) checkConnect.setSelected(true); else checkConnect.setSelected(false);
    }
}

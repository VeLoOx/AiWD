package pl.aiwd.gui;





import java.io.File;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialogs;

import pl.aiwd.controler.Controler;

public class MainScene {

	// glowne elemnty
	public Scene scene;
	private Stage stage;
	private BorderPane borderPane;
	private GridPane grid;

	// menu gorne
	private MenuBar menu;
	private Menu menuFile;
	private MenuItem loadData;
	private MenuItem loadNormalizedData;
	private Menu menuData;
	private MenuItem normalizeData;
	
	
	//panel lewy
	private Label labelPopulation;
	private Label labelIteration;
	private Label labelFitness;
	private Label labelFunctionShape;
	
	private Label labelFitnesBefGen;
	private Label labelFitnesAftGen;
	private Label labelErrBefGen;
	private Label labelErrAftGen;
	private Label time;
	private Label iterations;
	private Label running;
	
	private TextField tfPopulation;
	private TextField tfIteration;
	private TextField tfFitness;
	private TextField tfFunctionShape;
	
	private RadioButton radioFitness;
	private RadioButton radioIterations;
	
	private Button startButton;
	private Button stopButton;

	//panel srodkowy
	TabPane tabPane;
	VBox vbdata;
	VBox vbnormaldata;
	Tab tabData; 
	Tab tabNormalizedData; 
	Tab tabMaps; 
	//panel prawy
	

	public MainScene(Stage st) {
		borderPane = new BorderPane();
		stage = st;
		
		grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(0, 10, 0, 10));
		scene = new Scene(borderPane, 1300, 700);
		
		//menu
		menu = new MenuBar();
		menu.setMinWidth(1300);
		menuFile = new Menu("File");
		loadData = new MenuItem("Load data");
		loadNormalizedData = new MenuItem("Load normalized data");
		menuFile.getItems().addAll(loadData);
		loadData.setOnAction(loadDataClick());
		
		menuData = new Menu("Data");
		normalizeData = new MenuItem("Normalize");
		normalizeData.setOnAction(normalizeClick());
		menuData.getItems().add(normalizeData);
		menu.getMenus().addAll(menuFile,menuData);
		
		HBox hMenuBox = new HBox(menu);
		borderPane.setTop(hMenuBox);
		
		//lewy panel
		labelFitness = new Label("Ftness");
		labelIteration = new Label("Iteration");
		labelPopulation = new Label("Population");
		labelFunctionShape = new Label("Func Shape");
		
		tfFitness = new TextField("15");
		tfIteration = new TextField("50");
		tfPopulation = new TextField("50");
		tfFunctionShape = new TextField("5");
		
		ToggleGroup radioGroup = new ToggleGroup();
		radioFitness = new RadioButton("Fitness");
		radioFitness.setToggleGroup(radioGroup);
		radioIterations = new RadioButton("Iterations");
		radioIterations.setSelected(true);
		radioIterations.setToggleGroup(radioGroup);
		
		startButton = new Button("Start");
		startButton.setOnAction(startClick());
		stopButton = new Button("Stop");
		stopButton.setOnAction(stopClick());
		
		VBox vLeftBox = new VBox();
		GridPane leftGrid = new GridPane();
		leftGrid.setVgap(5);
		leftGrid.setHgap(5);
		leftGrid.setPadding(new Insets(10,10,10,10));
		
		
		
		leftGrid.setConstraints(labelFitness, 0, 0);
		leftGrid.getChildren().add(labelFitness);
		leftGrid.setConstraints(labelIteration, 0, 1);
		leftGrid.getChildren().add(labelIteration);
		leftGrid.setConstraints(labelPopulation, 0, 2);
		leftGrid.getChildren().add(labelPopulation);
		leftGrid.setConstraints(labelFunctionShape, 0, 3);
		leftGrid.getChildren().add(labelFunctionShape);
		leftGrid.setConstraints(radioFitness, 0, 4);
		leftGrid.getChildren().add(radioFitness);
		/*leftGrid.setConstraints(startButton, 0, 5);
		leftGrid.getChildren().add(startButton);*/
		
		leftGrid.setConstraints(tfFitness, 1, 0);
		leftGrid.getChildren().add(tfFitness);
		leftGrid.setConstraints(tfIteration, 1, 1);
		leftGrid.getChildren().add(tfIteration);
		leftGrid.setConstraints(tfPopulation, 1, 2);
		leftGrid.getChildren().add(tfPopulation);
		leftGrid.setConstraints(tfFunctionShape, 1, 3);
		leftGrid.getChildren().add(tfFunctionShape);
		leftGrid.setConstraints(radioIterations, 1, 4);
		leftGrid.getChildren().add(radioIterations);
		/*leftGrid.setConstraints(stopButton, 1, 5);
		leftGrid.getChildren().add(stopButton);*/
		
		
		vLeftBox.getChildren().add(new Label("  Parameters"));
		vLeftBox.getChildren().add(leftGrid);
		HBox buttons = new HBox(10);
		buttons.setPadding(new Insets(10,10,10,10));
		buttons.setCenterShape(true);
		
		buttons.getChildren().addAll(startButton);
		vLeftBox.getChildren().add(new Separator());
		vLeftBox.getChildren().add(buttons);
		vLeftBox.getChildren().add(new Separator());
		vLeftBox.getChildren().add(new Label("  Results"));
		
		labelFitnesBefGen = new Label("Fitness before learnig = NA");
		labelFitnesAftGen = new Label("Fitness after learnig = NA");;
		labelErrBefGen = new Label("Error MAP before learnig = NA");;
		labelErrAftGen = new Label("Error MAP after learnig = NA");;
		time = new Label("Time = NA");;
		iterations = new Label("Iteations = NA");
		running = new Label("NO RUNING");
		
		GridPane leftGrid2 = new GridPane();
		leftGrid2.setVgap(5);
		leftGrid2.setHgap(5);
		leftGrid2.setPadding(new Insets(10,10,10,10));
		leftGrid2.setConstraints(labelFitnesBefGen, 0, 0);
		leftGrid2.getChildren().add(labelFitnesBefGen);
		leftGrid2.setConstraints(labelFitnesAftGen, 0, 1);
		leftGrid2.getChildren().add(labelFitnesAftGen);
		leftGrid2.setConstraints(labelErrBefGen, 0, 2);
		leftGrid2.getChildren().add(labelErrBefGen);
		leftGrid2.setConstraints(labelErrAftGen, 0, 3);
		leftGrid2.getChildren().add(labelErrAftGen);
		leftGrid2.setConstraints(time, 0, 4);
		leftGrid2.getChildren().add(time);
		leftGrid2.setConstraints(iterations, 0, 5);
		leftGrid2.getChildren().add(iterations);
		vLeftBox.getChildren().add(leftGrid2);
		borderPane.setLeft(vLeftBox);
		
		// srodek
		tabPane = new TabPane();
		tabData = new Tab("Data");
		tabNormalizedData = new Tab("Normalized Data");
		tabMaps = new Tab("Maps");
		
		VBox vData = new VBox();
		vData.getChildren().add(new Label("Data"));
		VBox vNormalizedData = new VBox();
		vNormalizedData.getChildren().add(new Label("Normalized"));
		
		ScrollPane sp1 = new ScrollPane();
		vbdata = new VBox();
		sp1.setContent(vbdata);
		tabData.setContent(sp1);
		tabData.setClosable(false);
		
		vbnormaldata = new VBox();
		ScrollPane sp = new ScrollPane();
		sp.setContent(vbnormaldata);
		tabNormalizedData.setContent(sp);
		tabNormalizedData.setClosable(false);
		
		tabPane.getTabs().add(tabData);
		tabPane.getTabs().add(tabNormalizedData);
		tabPane.getTabs().add(tabMaps);
		
		
		
		borderPane.setCenter(tabPane);
		
	}
	
	public void createDataTable(String[] names){
		HBox hbb = new HBox();
		for(int i=0; i<names.length;i++){
			
			Label l = new Label(names[i]);
			l.setMinWidth(100);
			l.setContentDisplay(ContentDisplay.CENTER);
			hbb.getChildren().add(l);
		}
		vbdata.getChildren().add(hbb);
		int ii = 0;
		for(double[] d : Controler.getInstance().getFcmarea().getPatternRecords()){
			HBox hb = new HBox();
			ii++;
			Label ll = new Label(Integer.toString(ii)+":  ");
			ll.setMaxWidth(50);
			hb.getChildren().add(ll);
			
			for(int i=0; i<d.length;i++){
				String s;
				if(d[i]<0) s = Double.toString(d[i])+"   "; else s = Double.toString(d[i])+"    ";
				Label l = new Label(s);
				l.setMinWidth(100);
				l.setContentDisplay(ContentDisplay.CENTER);
				hb.getChildren().add(l);
			}
			vbdata.getChildren().add(hb);
		}
		
	}
	
	public void createNormalDataTable(String[] names){
		HBox hbb = new HBox();
		for(int i=0; i<names.length;i++){
			
			Label l = new Label(names[i]);
			l.setMinWidth(180);
			l.setContentDisplay(ContentDisplay.CENTER);
			hbb.getChildren().add(l);
		}
		vbnormaldata.getChildren().add(hbb);
		int ii = 0;
		for(double[] d : Controler.getInstance().getFcmarea().getNormalizedPatternRecords()){
			HBox hb = new HBox();
			ii++;
			Label ll = new Label(Integer.toString(ii)+":  ");
			ll.setMaxWidth(50);
			hb.getChildren().add(ll);
			
			for(int i=0; i<d.length;i++){
				String s;
				if(d[i]<0) s = Double.toString(d[i])+"   "; else s = Double.toString(d[i])+"    ";
				Label l = new Label(s);
				l.setMinWidth(180);
				l.setContentDisplay(ContentDisplay.CENTER);
				hb.getChildren().add(l);
			}
			vbnormaldata.getChildren().add(hb);
		}
		
	}
	
	private EventHandler<ActionEvent> loadDataClick(){
		
		return new EventHandler<ActionEvent>() {
			@Override
	        public void handle(ActionEvent event) {
	            System.out.println("Hello World!");
	            FileChooser fc = new FileChooser();
	            File f = fc.showOpenDialog(stage);
	            if(f!=null){
	            	//System.out.println(f.getAbsolutePath());
	            	String path = f.getAbsolutePath().substring(0,f.getAbsolutePath().length()-f.getName().length());
	            	String s = Controler.getInstance().loadData(path, f.getName());
	            	Dialogs.create()
	                .owner(stage)
	                .title("Load data")
	                .masthead("Load succes")
	                .message(s+" "+f.getName())
	                .showInformation();
	            }
	        }
		};
	}
	
private EventHandler<ActionEvent> normalizeClick(){
		
		return new EventHandler<ActionEvent>() {
			@Override
	        public void handle(ActionEvent event) {
	            
	            	if(Controler.getInstance().normalizeData()){
	            	
	            	Dialogs.create()
	                .owner(stage)
	                .title("Normalized data")
	                .masthead("Succes")
	                .message(Controler.getInstance().getMessage())
	                .showInformation();
	            	
	            	
	            	createDataTable(Controler.getInstance().getCsvNormal().getColumnNames());
	            	createNormalDataTable(Controler.getInstance().getCsvNormal().getColumnNames());
	            	} else {
	            		Dialogs.create()
		                .owner(stage)
		                .title("Normalized data")
		                .masthead("Failed")
		                .message(Controler.getInstance().getMessage())
		                .showInformation();
	            	}
	          
	        }
		};
	}

private EventHandler<ActionEvent> startClick(){
	
	return new EventHandler<ActionEvent>() {
		@Override
        public void handle(ActionEvent event) {
			
				
			
            
            	int pool = Integer.parseInt(tfPopulation.getText());
            	double fit = Double.parseDouble(tfFitness.getText());
            	int iter = Integer.parseInt(tfIteration.getText());
            	int shape = Integer.parseInt(tfFunctionShape.getText());
            	
            	
            	running.setText("RUNNING");
            	if(radioIterations.isSelected())
            	Controler.getInstance().runGA(iter, pool); else Controler.getInstance().runGA(fit, pool);
            	
            	
            	
            	labelFitnesBefGen.setText("Fitness before learnig = "+Controler.getInstance().fitbef);
        		labelFitnesAftGen.setText("Fitness after learnig = "+Controler.getInstance().fitaft); 
        		labelErrBefGen.setText("Error MAP before learnig = "+Controler.getInstance().errbef +"%"); 
        		labelErrAftGen.setText("Error MAP after learnig = "+Controler.getInstance().erraft +"%"); 
        		time.setText("Time (s) = "+(Controler.getInstance().time/1000));
        		iterations.setText("Iteations = "+Controler.getInstance().iteration);
        		running.setText("NOT RUNNING");
        		
        		VBox mapBox1 = new VBox(10);
        		HBox hb1 = new HBox();
        		mapBox1.getChildren().add(new Label("Map before learnign"));
        		Label ml = new Label(Controler.getInstance().mbef.toString());
        		mapBox1.getChildren().add(ml);
        		
        		VBox mapBox2 = new VBox(10);
        		mapBox2.getChildren().add(new Label("Map after learnign"));
        		Label ma = new Label(Controler.getInstance().maft.toString());
        		mapBox2.getChildren().add(ma);
        		
        		hb1.getChildren().add(mapBox1);
        		hb1.getChildren().add(mapBox2);
        		
        		tabMaps.setContent(hb1);
        		
          
        }
	};
}

private EventHandler<ActionEvent> stopClick(){
	
	return new EventHandler<ActionEvent>() {
		@Override
        public void handle(ActionEvent event) {
			
            	Controler.getInstance().getGen().setMaxIteration(-1);
            	
            	labelFitnesBefGen.setText("Fitness before learnig = "+Controler.getInstance().fitbef);
        		labelFitnesAftGen.setText("Fitness after learnig = "+Controler.getInstance().fitaft); 
        		labelErrBefGen.setText("Error MAP before learnig = "+Controler.getInstance().errbef +"%"); 
        		labelErrAftGen.setText("Error MAP after learnig = "+Controler.getInstance().erraft +"%"); 
        		time.setText("Time (s) = "+(Controler.getInstance().time/1000));
        		iterations.setText("Iteations = "+Controler.getInstance().iteration);
        		running.setText("NOT RUNNING");
          
        }
	};
}

}

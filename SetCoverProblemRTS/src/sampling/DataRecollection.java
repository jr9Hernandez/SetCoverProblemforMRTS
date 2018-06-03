/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sampling;

import ai.configurablescript.BasicExpandedConfigurableScript;
import ai.configurablescript.ScriptsCreator;
import ai.core.AI;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import rts.GameState;
import rts.PlayerAction;
import rts.units.UnitTypeTable;

/**
 *
 * @author rubens
 */
public class DataRecollection {

	int scriptLeader=-1;
	int scriptEnemy=-1;
	Random rand;
	int sampleCounter=0;
	GameSampling game;
	UnitTypeTable utt;
	String pathLog;
	List<Integer> scriptsForPortfolio = new ArrayList<>();
	String portfolioPlayer1;
	String portfolioPlayer2;
	
	public DataRecollection(String pathLog) {
		game = new GameSampling();
		rand = new Random();
		utt = new UnitTypeTable();
		this.pathLog=pathLog;
	}

	public void dataRecollection() {
		for(int i=0;i<ConfigurationsSC.NUM_SAM;i++)
		{
			//scriptEnemy = rand.nextInt(ConfigurationsSC.TOTAL_SCRIPTS);
			scriptsForPortfolio=getPortfolioRandomSize(ConfigurationsSC.MAX_SIZE_PORTFOLIO);
			//scriptsForPortfolio=getPortfolio();
			portfolioPlayer2=getPortfolioInString(scriptsForPortfolio);
			
			for(int j=0;j<ConfigurationsSC.NUM_SCRIPTS_SAM;j++)
			{				
				//scriptLeader = rand.nextInt(ConfigurationsSC.TOTAL_SCRIPTS);
				scriptsForPortfolio=getPortfolioRandomSize(ConfigurationsSC.MAX_SIZE_PORTFOLIO);
				//scriptsForPortfolio=getPortfolio();
				portfolioPlayer1=getPortfolioInString(scriptsForPortfolio);
				try {						
					game.run(portfolioPlayer1,portfolioPlayer2,pathLog);
					sampleCounter++;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

	}

	public void samplingByFiles(String folderLeader, File[] Files)
	{
		Random rand = new Random();
		int numberStatesSampled=ConfigurationsSC.NUM_STATES_SAM;
		int stateForSampling=0;
		
		ArrayList<String> statesforSampling = new ArrayList<>();
		for (int i=0;i<numberStatesSampled;i++)
		{
		
			try {
				stateForSampling=rand.nextInt(Files.length);
				statesforSampling.add(readFile(Files[stateForSampling].getPath()));	
				//File dir = new File("samplings/"+folderLeader+"/"+"state_"+stateForSampling);
			    //dir.mkdirs();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}			
			
			GameState gsSimulator = GameState.fromJSON(statesforSampling.get(i),game.utt);
			if (gsSimulator.canExecuteAnyAction(0)){
				for (int j = 0; j < ConfigurationsSC.TOTAL_SCRIPTS; j++) {
				
				
					gsSimulator = GameState.fromJSON(statesforSampling.get(i),game.utt);
					//System.out.println(gsSimulator.toString());
				
					PlayerAction pa= game.generateActionbyScript(gsSimulator, j, 0);
					try {
						Writer writer = new FileWriter("samplings/"+folderLeader+"_state_"+stateForSampling+"_idLogs_"+pathLog+"_player_0"+".txt",true);
						writer.write(pa.getActions().toString());
						writer.write("\n");
						writer.flush();
						writer.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.gc(); // forço o garbage para tentar liberar memoria....
				
				
				}
			}
			
			if (gsSimulator.canExecuteAnyAction(1)){
				for (int j = 0; j < ConfigurationsSC.TOTAL_SCRIPTS; j++) {
				
				
					gsSimulator = GameState.fromJSON(statesforSampling.get(i),game.utt);
					//System.out.println(gsSimulator.toString());
				
					PlayerAction pa= game.generateActionbyScript(gsSimulator, j, 1);
					try {
						Writer writer = new FileWriter("samplings/"+folderLeader+"_state_"+stateForSampling+"_idLogs_"+pathLog+"_player_1"+".txt",true);
						writer.write(pa.getActions().toString());
						writer.write("\n");
						writer.flush();
						writer.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.gc(); // forço o garbage para tentar liberar memoria....
				
				
				}
			}

		}		
	}

	
	public void sampling(String folderLeader, int numFiles)
	{
		
		int numberStatesSampled=ConfigurationsSC.NUM_STATES_SAM;
		int stateForSampling=0;
		
		ArrayList<String> statesforSampling = new ArrayList<>();
		for (int i=0;i<numberStatesSampled;i++)
		{
		
			try {
				statesforSampling.add(readFile("logs/logs_states_"+pathLog+"/"+folderLeader+"/"+"state_"+stateForSampling+".txt"));
				stateForSampling=stateForSampling+(numFiles/numberStatesSampled);
				//File dir = new File("samplings/"+folderLeader+"/"+"state_"+stateForSampling);
			    //dir.mkdirs();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}			
	        
			for (int j = 0; j < ConfigurationsSC.TOTAL_SCRIPTS; j++) {
				
				
				GameState gsSimulator = GameState.fromJSON(statesforSampling.get(i),game.utt);
				
				PlayerAction pa= game.generateActionbyScript(gsSimulator, j,0);
                try {
                	Writer writer = new FileWriter("samplings/"+folderLeader+"_state_"+stateForSampling+"_idLogs_"+pathLog+".txt",true);
					writer.write(pa.getActions().toString());
					writer.write("\n");
					writer.flush();
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.gc(); // forço o garbage para tentar liberar memoria....
				
				
			}

		}		
	}
	


	public static List<AI> decodeScripts(UnitTypeTable utt, String sScripts) {

		//decompõe a tupla
		ArrayList<Integer> iScriptsAi1 = new ArrayList<>();
		String[] itens = sScripts.split(";");

		for (String element : itens) {
			iScriptsAi1.add(Integer.decode(element));
		}

		List<AI> scriptsAI = new ArrayList<>();

		ScriptsCreator sc = new ScriptsCreator(utt, 300);
		ArrayList<BasicExpandedConfigurableScript> scriptsCompleteSet = sc.getScriptsMixReducedSet();

		iScriptsAi1.forEach((idSc) -> {
			scriptsAI.add(scriptsCompleteSet.get(idSc));
		});

		return scriptsAI;
	}
	
	String readFile(String fileName) throws IOException {
	    BufferedReader br = new BufferedReader(new FileReader(fileName));
	    try {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        while (line != null) {
	            sb.append(line);
	            sb.append("\n");
	            line = br.readLine();
	        }
	        return sb.toString();
	    } finally {
	        br.close();
	    }
	}
	
	public List <Integer> getPortfolioRandomSize(int size){
		List<Integer> scriptsForPortfolio = new ArrayList<>();
		
		//gerar o novo cromossomo com base no tamanho
		int sizePortf=rand.nextInt(size)+1;
		for (int j = 0; j < sizePortf; j++) {
			scriptsForPortfolio.add(rand.nextInt(ConfigurationsSC.TOTAL_SCRIPTS));
		}

		return scriptsForPortfolio;
	}
	
	public List <Integer> getPortfolio(){
		List<Integer> scriptsForPortfolio = new ArrayList<>();
		
		//gerar o novo cromossomo com base no tamanho
		//int sizePortf=rand.nextInt(size)+1;
		for (int j = 0; j < 4; j++) {
			scriptsForPortfolio.add(rand.nextInt(ConfigurationsSC.TOTAL_SCRIPTS));
		}

		return scriptsForPortfolio;
	}
	
	public String getPortfolioInString(List<Integer> scriptsForPortfolio) {
		String portfolio="";
		for (int id : scriptsForPortfolio) {
			portfolio=portfolio.concat(String.valueOf(id).concat(";"));
		}
		return portfolio;
	}

}

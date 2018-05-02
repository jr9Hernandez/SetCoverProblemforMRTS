/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package setcover;

import ai.asymmetric.GAB.PGSLimitScriptC;
import ai.configurablescript.BasicExpandedConfigurableScript;
import ai.configurablescript.ScriptsCreator;
import ai.core.AI;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import rts.GameState;
import rts.PlayerAction;
import rts.units.UnitTypeTable;

/**
 *
 * @author rubens
 */
public class SetCover {

	int scriptLeader=-1;
	int scriptEnemy=-1;
	public SetCover() {

	}

	public void dataRecollection() {
		GameSampling game = new GameSampling();
		UnitTypeTable utt = new UnitTypeTable();
		for(int i=0;i<ConfigurationsSC.NUM_SAM;i++)
		{
			for(int j=0;j<ConfigurationsSC.NUM_SCRIPTS_SAM;i++)
			{

				Random rand = new Random();
				int scriptLeader = rand.nextInt(300);
				
				for(int k=0;k<ConfigurationsSC.NUM_ENENMY_SC_SAM;k++)
				{
					scriptEnemy = rand.nextInt(300);
				}

				try {
					
					game.run(scriptLeader,scriptEnemy);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

	}
	
	public void sampling(String folderStates, int numFiles)
	{
		GameSampling game = new GameSampling();
		int numberStatesSampled=ConfigurationsSC.NUM_STATES_SAM;
		int stateForSampling=0;
		
		ArrayList<String> statesforSampling = new ArrayList<>();
		for (int i=0;i<numberStatesSampled;i++)
		{
			statesforSampling.add(folderStates+"/"+stateForSampling+".txt");
			stateForSampling=stateForSampling+(numFiles/numberStatesSampled);
		}
		for (String state : statesforSampling) {
			for (int i = 0; i < ConfigurationsSC.TOTAL_SCRIPTS; i++) {
//				AI ai = new PGSLimitScriptC(utt, decodeScripts(utt, String.valueOf(i).concat(";"))); // carregamos o script que desejamos simular
				UnitTypeTable utt = new UnitTypeTable();
				GameState gsSimulator = GameState.fromJSON(state,utt);
				game.generateActionbyScript(gsSimulator, i);
				//método que simule a ação e log a ação do script i para o estamo carregado....
	            
	            
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

}

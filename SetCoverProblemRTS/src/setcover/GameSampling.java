/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package setcover;

import ai.asymmetric.PGS.*;
import tests.*;
import PVAI.EconomyRush;
import PVAI.EconomyRushBurster;
import PVAI.WorkerDefense;
import PVAI.RangedDefense;
import Standard.CombinedEvaluation;
import Standard.StrategyTactics;
import ai.core.AI;
import ai.*;
import ai.abstraction.HeavyRush;
import ai.abstraction.LightRush;
import ai.abstraction.RangedRush;
import ai.abstraction.WorkerRush;
import ai.abstraction.partialobservability.POLightRush;
import ai.abstraction.partialobservability.PORangedRush;
import ai.abstraction.pathfinding.AStarPathFinding;
import ai.abstraction.pathfinding.BFSPathFinding;
import ai.aiSelection.AlphaBetaSearch.AlphaBetaSearch;
import ai.asymmetric.GAB.GAB_oldVersion;
import ai.asymmetric.GAB.SandBox.AlphaBetaSearchAbstract;
import ai.asymmetric.GAB.SandBox.GAB;
import ai.asymmetric.GAB.SandBox.GAB_SandBox_Parcial_State;
import ai.asymmetric.PGS.SandBox.PGSmRTS_SandBox;
import ai.asymmetric.SAB.SAB;
import ai.asymmetric.SSS.SSSmRTSScriptChoice;
import ai.configurablescript.BasicExpandedConfigurableScript;
import ai.configurablescript.ScriptsCreator;
import ai.evaluation.EvaluationFunctionForwarding;
import ai.evaluation.LanchesterEvaluationFunction;
import ai.evaluation.SimpleSqrtEvaluationFunction3;
import ai.mcts.naivemcts.NaiveMCTS;
import ai.minimax.ABCD.IDABCD;
import ai.portfolio.PortfolioAI;
import ai.puppet.PuppetSearchMCTS;
import gui.PhysicalGameStatePanel;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JFrame;

import org.jdom.JDOMException;

import rts.GameState;
import rts.PhysicalGameState;
import rts.Player;
import rts.PlayerAction;
import rts.units.Unit;
import rts.units.UnitTypeTable;
import static tests.ClusterTesteLeve.decodeScripts;
import util.XMLWriter;

/**
 *
 * @author santi
 */
public class GameSampling {

    static String _nameStrategies = "", _enemy = "";
    static AI[] strategies = null;
    UnitTypeTable utt;
    PhysicalGameState pgs;
    int MAXCYCLES;
    int PERIOD;
    
    public GameSampling()
    {
    	utt = new UnitTypeTable();
        MAXCYCLES = 2000;
        PERIOD = 20;

    }

    public void run(int sampleCounter, int idScriptLeader, int idScriptEnemy) throws Exception {

		pgs = PhysicalGameState.load("maps/8x8/basesWorkers8x8A.xml", utt);
        //pgs = PhysicalGameState.load("maps/16x16/basesWorkers16x16A.xml", utt);        
        //pgs = PhysicalGameState.load("maps/BWDistantResources32x32.xml", utt);
        //pgs = PhysicalGameState.load("maps/32x32/basesWorkers32x32A.xml", utt);
        //pgs = PhysicalGameState.load("maps/24x24/basesWorkers24x24A.xml", utt);
        //pgs = PhysicalGameState.load("maps/BroodWar/(4)BloodBath.scmB.xml", utt);  

        GameState gs = new GameState(pgs, utt);
        boolean gameover = false;

       
        System.out.println("idscriptleader "+idScriptLeader);
        System.out.println("idscripEnemy "+idScriptEnemy);
        AI ai1 = new PGSSCriptChoice(utt, decodeScripts(utt, String.valueOf(idScriptLeader).concat(";")), "--");
        AI ai2 = new PGSSCriptChoice(utt, decodeScripts(utt, String.valueOf(idScriptEnemy).concat(";")), "--");

        
        System.out.println("---------AI's---------");
        System.out.println("AI 1 = "+ai1.toString());
        System.out.println("AI 2 = "+ai2.toString()+"\n");        
        
        
        //JFrame w = PhysicalGameStatePanel.newVisualizer(gs, 640, 640, false, PhysicalGameStatePanel.COLORSCHEME_BLACK);

        long startTime = System.currentTimeMillis();
        long nextTimeToUpdate = System.currentTimeMillis() + PERIOD;
        
        File dir = new File("logs_states/log_"+sampleCounter+"_"+idScriptLeader+"_"+idScriptEnemy);
        dir.mkdir();
        
        do {
            if (System.currentTimeMillis() >= nextTimeToUpdate) {
            	
            	//File subDir = new File("logs_states/log_"+sampleCounter+"_"+idScriptLeader+"_"+idScriptEnemy+"/"+"state_"+gs.getTime());
            	//subDir.mkdir();
                
                //alcançamos o estado que desejamos salvar....
            	Writer writer = new FileWriter("logs_states/log_"+sampleCounter+"_"+idScriptLeader+"_"+idScriptEnemy+"/"+"state_"+gs.getTime()+".txt");
                gs.toJSON(writer); //salva JSon contendo todo o estado no tempo x que escolhido
                writer.flush();
                
                startTime = System.currentTimeMillis();
                PlayerAction pa1 = ai1.getAction(0, gs);  
                //System.out.println("Tempo de execução P1="+(startTime = System.currentTimeMillis() - startTime));
                //System.out.println("Action A1 ="+ pa1.toString());
                
                startTime = System.currentTimeMillis();
                PlayerAction pa2 = ai2.getAction(1, gs);
                //System.out.println("Tempo de execução P2="+(startTime = System.currentTimeMillis() - startTime));
                //System.out.println("Action A2 ="+ pa2.toString());
                
                gs.issueSafe(pa1);
                gs.issueSafe(pa2);
                
                // simulate:
                gameover = gs.cycle();
                //w.repaint();
                nextTimeToUpdate += PERIOD;
            } else {
                try {
                    Thread.sleep(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
           /* PhysicalGameState physical = gs.getPhysicalGameState();
            System.out.println("---------TIME---------");
            System.out.println(gs.getTime());
            for (Unit u : physical.getUnits()) {
                if (u.getPlayer() == 1) {
                    System.out.println("Player 1: Unity - " + u.toString());
                }
                else if (u.getPlayer() == 0) {
                     System.out.println("Player 0: Unity - " + u.toString());
                } 
            }
            */
        } while (!gameover && gs.getTime() < MAXCYCLES);

        System.out.println("Game Over");
    }
    
    public PlayerAction generateActionbyScript(GameState g, int scriptSampling) 
    {
    	AI ai1 = new PGSSCriptChoice(utt, decodeScripts(utt, String.valueOf(scriptSampling).concat(";")), "--");
    	
        PlayerAction pa1=null;
		try {
			pa1 = ai1.getAction(0, g);
			//System.out.println("Action A1 ="+ pa1.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        
        return pa1;
    }
    
    public static List<AI> decodeScripts(UnitTypeTable utt, String sScripts) {
        
        //decompõe a tupla
        ArrayList<Integer> iScriptsAi1 = new ArrayList<>();
        String[] itens = sScripts.split(";");

        for (String element : itens) {
            iScriptsAi1.add(Integer.decode(element));
        }
        
        List<AI> scriptsAI = new ArrayList<>();

        ScriptsCreator sc = new ScriptsCreator(utt,300);
        ArrayList<BasicExpandedConfigurableScript> scriptsCompleteSet = sc.getScriptsMixReducedSet();

        iScriptsAi1.forEach((idSc) -> {
            scriptsAI.add(scriptsCompleteSet.get(idSc));
        });

        return scriptsAI;
    }


}

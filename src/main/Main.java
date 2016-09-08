package main;

import init.InitConexaoPool;
import init.InitConfig;
import execute.ExecuteScript;

public class Main {

	public static void main(String[] args) {

		try {
//			InitConfig.init();
//			InitConexaoPool.init();
			ExecuteScript executeScript = new ExecuteScript();
			executeScript.executar();
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

}

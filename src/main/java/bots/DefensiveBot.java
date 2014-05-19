package bots;

import java.util.concurrent.Callable;


public final class DefensiveBot extends StrategyBot {
	public static final String name ="Defensive bot";


	
	@Override
	public String getName() {
		return name;
	}

	public DefensiveBot() {
		super();
		addStrategy(0.75, new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				computeOffensiveStrategy();
				return null;
			}
		});
		addStrategy(0.20, new Callable<Void>() {
			
			@Override
			public Void call() throws Exception {
				computeRandomStrategy();
				return null;
			}
		});
		addStrategy(0.05, new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				computeDefensiveStrategy();
				return null;
			}
		});
	}
	
}